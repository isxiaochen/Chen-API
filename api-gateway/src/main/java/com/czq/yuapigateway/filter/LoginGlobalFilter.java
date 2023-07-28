package com.czq.yuapigateway.filter;

import cn.hutool.core.text.AntPathMatcher;
import com.czq.apicommon.common.ErrorCode;
import com.czq.apicommon.common.JwtUtils;
import com.czq.apicommon.exception.BusinessException;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class LoginGlobalFilter implements GlobalFilter, Ordered {


    @Resource
    private RateLimiter rateLimiter;



    public static final List<String> NOT_LOGIN_PATH = Arrays.asList(
            "/api/user/login", "/api/user/loginBySms", "/api/user/register", "/api/user/smsCaptcha",
            "/api/user/getCaptcha", "/api/interface/**","/api/third/alipay/**","/api/interfaceInfo/sdk");


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {


        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        HttpHeaders headers = request.getHeaders();

        //限流过滤
        if (!rateLimiter.tryAcquire()) {
            throw new BusinessException(ErrorCode.TOO_MANY_REQUESTS);
        }

        //登录过滤
        String path = request.getPath().toString();
        //判断请求路径是否需要登录
        List<Boolean> collect = NOT_LOGIN_PATH.stream().map(notLoginPath -> {
            AntPathMatcher antPathMatcher = new AntPathMatcher();
            return antPathMatcher.match(notLoginPath, path);
        }).collect(Collectors.toList());

        if (collect.contains(true)){
            return chain.filter(exchange);
        }


        String cookie = headers.getFirst("Cookie");
        if (StringUtils.isBlank(cookie)){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }

        if (!getLoginUserByCookie(cookie)) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }

        return chain.filter(exchange);
    }

    private Boolean getLoginUserByCookie(String cookie) {
        String[] split = cookie.split(";");
        for (String cookeKey : split) {
            String[] split1 = cookeKey.split("=");
            String cookieName = split1[0];
            if (cookieName.trim().equals("token")){
                String cookieValue = split1[1];
                return JwtUtils.checkToken(cookieValue);
            }
        }

        return false;
    }

    @Override
    public int getOrder() {
        return -1;
    }
}