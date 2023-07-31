package com.czq.yuapigateway.filter;

import com.czq.apiclientsdk.utils.SignUtils;
import com.czq.apicommon.entity.InterfaceInfo;
import com.czq.apicommon.entity.User;
import com.czq.apicommon.service.ApiBackendService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class InterfaceInvokeFilter implements GatewayFilter, Ordered {

    private static final String INTERFACE_HOST="http://localhost:8123";

    @DubboReference
    private ApiBackendService apiBackendService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;




    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //1.打上请求日志
        //2.黑白名单(可做可不做)
        //3.用户鉴权(API签名认证)
        //4.远程调用判断接口是否存在以及获取调用接口信息
        //5.判断接口是否还有调用次数，如果没有则直接拒绝
        //6.发起接口调用
        //7.获取响应结果，打上响应日志
        //8.接口调用次数+1

        //1.打上请求日志
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String path = INTERFACE_HOST+request.getPath().value();
        String method = request.getMethod().toString();

        log.info("请求id:"+request.getId());
        log.info("请求URI:"+request.getURI());
        log.info("请求PATH:"+request.getPath());
        log.info("请求参数:"+request.getQueryParams());
        log.info("本地请求地址:"+request.getLocalAddress());
        log.info("请求地址："+request.getRemoteAddress());

        //2.黑白名单(可做可不做)
//        InetSocketAddress localAddress = request.getLocalAddress();
//        if (!"127.0.0.1".equals(localAddress.getHostString())){
//            response.setStatusCode(HttpStatus.FORBIDDEN);
//            return response.setComplete();
//        }

        //3.用户鉴权(API签名认证)
        HttpHeaders headers = request.getHeaders();

        String accessKey = headers.getFirst("accessKey");
        String body = headers.getFirst("body");
        String sign = headers.getFirst("sign");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");


        //根据accessKey获取secretKey，判断accessKey是否合法
        User invokeUser = null;
        try {
            invokeUser = apiBackendService.getInvokeUser(accessKey);
        } catch (Exception e) {
            log.error("远程调用获取调用接口用户的信息失败");
            e.printStackTrace();
        }

        if (invokeUser == null){
            return handleNoAuth(response);
        }

        String secretKey = invokeUser.getSecretKey();
        String serverSign = SignUtils.generateSign(body, secretKey);

        if (sign == null || !sign.equals(serverSign)){
            log.error("签名校验失败!!!!");
            return handleNoAuth(response);
        }

        //3.1防重放，使用redis存储请求的唯一标识，随机时间，并定时淘汰，那使用什么redis结构来实现嗯？
        //既然是单个数据，这样用string结构实现即可

        Boolean success = stringRedisTemplate.opsForValue().setIfAbsent(nonce, "1", 5, TimeUnit.MINUTES);
        if (success ==null){
            log.error("随机数存储失败!!!!");
            return handleNoAuth(response);
        }

        //4.远程调用判断接口是否存在以及获取调用接口信息
        InterfaceInfo interFaceInfo = null;
        try {
            interFaceInfo = apiBackendService.getInterFaceInfo(path, method);
        } catch (Exception e) {
            log.info("远程调用获取被调用接口信息失败");
            e.printStackTrace();
        }


        if (interFaceInfo == null){
            log.error("接口不存在！！！！");
            return handleNoAuth(response);
        }



        //5.判断接口是否还有调用次数，如果没有则直接拒绝
        int leftInvokeCount=-1;
        try {
            leftInvokeCount = apiBackendService.getLeftInvokeCount(invokeUser.getId(), interFaceInfo.getId());
        } catch (Exception e) {
            log.info("远程调用获取被调用接口剩余次数失败");
            e.printStackTrace();
        }

        if (leftInvokeCount<=0){
            return handleNoAuth(response);
        }


        //6.发起接口调用，网关路由实现
        Mono<Void> filter = chain.filter(exchange);

        return handleResponse(exchange,chain,interFaceInfo.getId(),invokeUser.getId());

    }

    /**
     * 处理无权限调用异常
     * @param response
     * @return
     */
    @NotNull
    private Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain,long interfaceInfoId,long userId) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();

            //缓存数据
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            //响应状态码
            HttpStatus statusCode = originalResponse.getStatusCode();

            if(statusCode == HttpStatus.OK){
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {

                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);//释放掉内存

                                //7.获取响应结果，打上响应日志
                                // 构建日志
                                log.info("接口调用响应状态码：" + originalResponse.getStatusCode());
                                //responseBody
                                String responseBody= new String(content, StandardCharsets.UTF_8);

                                //8.接口调用成功后，调用次数+1 远程调用实现 or 消息队列实现
                                if (originalResponse.getStatusCode() == HttpStatus.OK){
                                    log.info("接口调用正常-响应体:" + responseBody);


                                    //捕获技术异常
                                    boolean result = false;
                                    try {
                                        result = apiBackendService.invokeCount(userId, interfaceInfoId);
                                    } catch (Exception e) {
                                        log.error("远程调用统计统计接口失败");
                                        e.printStackTrace();
                                    }

                                    //捕获业务异常
                                    if (!result){
                                        return bufferFactory.wrap("网络繁忙，请重试!!!".getBytes());
                                    }
                                }else {
                                    log.error("接口调用异常-响应体"+responseBody);
                                }

                                return bufferFactory.wrap(content);
                            }));
                        } else {
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange);//降级处理返回数据
        }catch (Exception e){
            log.error("gateway log exception.\n" + e);
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return -2;
    }
}