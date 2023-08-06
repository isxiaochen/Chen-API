# Chen API

> 一个丰富的API开放调用平台，为开发者提供便捷、实用、安全的API调用体验
>
> Java + React 全栈项目，包括网站前台+后台，感谢[程序员鱼皮](https://github.com/liyupi)提供的项目基础框架，我也是在此基础上作拓展
>
> 
>
> 在线体验地址：[Chen API](http://www.chen-code.work/)







## 项目展示


- 首页

![首页](https://github.com/c-z-q/Chen-Api/blob/master/image/首页.png)

- 接口详情

  ![接口详情](https://github.com/c-z-q/Chen-Api/blob/master/image/接口详情.png)

  

- 接口购买

![购买接口](https://github.com/c-z-q/Chen-Api/blob/master/image/购买接口.png)

- 接口支付

![支付接口](https://github.com/c-z-q/Chen-Api/blob/master/image/支付接口.png)

- 接口管理

![接口管理](https://github.com/c-z-q/Chen-Api/blob/master/image/接口管理.png)

- 接口分析

![接口分析](https://github.com/c-z-q/Chen-Api/blob/master/image/接口分析.png)

- 用户管理

![用户管理](https://github.com/c-z-q/Chen-Api/blob/master/image/用户管理.png)

- 个人中心

![个人中心](https://github.com/c-z-q/Chen-Api/blob/master/image/个人中心.png)










## 项目背景

本人在实习过程中发现公司有调用第三方接口的业务，并且我在学习过程中也有调用第三方接口的尝试，比如手机短信接口，视频点播接口，支付接口等等，一直好奇第三方接口的服务提供方是如何实现安全，方便，快捷的接口的调用体验，遂萌生了一个自己写一个接口平台的想法，于是该项目诞生了



**Chen API 平台根据接口定制化SDK，给用户提供简洁、实用的接口调用体验，并且使用API签名校验保障接口调用的安全性**





## 系统架构
![系统架构图](https://github.com/c-z-q/Chen-Api/blob/master/image/API%E7%B3%BB%E7%BB%9F%E6%9E%B6%E6%9E%84%E5%9B%BE.png)






## 技术栈

### 前端技术栈

- 开发框架：React、Umi
- 脚手架：Ant Design Pro
- 组件库：Ant Design、Ant Design Components
- 语法扩展：TypeScript、Less
- 打包工具：Webpack
- 代码规范：ESLint、StyleLint、Prettier



### 后端技术栈

- 主语言：Java
- 框架：SpringBoot 2.7.0、Mybatis-plus、Spring Cloud
- 数据库：Mysql8.0、Redis
- 中间件：RabbitMq
- 注册中心：Nacos
- 服务调用：Dubbo
- 网关：Spring Cloud Gateway
- 负载均衡：Spring cloud Loadbalancer



## 项目模块

- api-frontend ：为项目前端，前端项目启动具体看readme.md文档
- api-common ：为公共封装类（如公共实体、公共常量，统一响应实体，统一异常处理）
- api-backend ：为接口管理平台，主要包括用户、接口相关的功能
- api-gateway ：为网关服务，**涉及到网关限流，统一鉴权，统一日志处理，接口统计，接口数据一致性处理**
- api-order ：为订单服务，主要涉及到接口的购买等
- api-third-party：为第三方服务，主要涉及到腾讯云短信、支付宝沙箱支付功能
- api-interface：为接口服务，提供可供调用的接口
- api-sdk：提供给开发者的SDK







## 功能模块

> 🌟 亮点功能 🚀 未来计划

- 用户、管理员
  - 🌟登录注册：使用令牌桶算法实现手机短信(邮箱)接口的限流，保护下游服务
  - 个人主页，包括上传头像，显示密钥，重新生成ak,sk
  - 管理员：用户管理
  - 管理员：接口管理
  - 管理员：接口分析、订单分析
- 接口
  - 浏览接口信息
  - 🌟 数字签名校验接口调用权限
  - 🌟 SDK调用接口
  - 接口搜索 (🚀 )
  - 购买接口
  - 下载SDK
  - 用户上传自己的接口（🚀）
- 订单
  - 创建订单
  - 订单超时回滚
  - 支付宝沙箱支付


## 快速上手

### 后端

1. 将各模块配置修改成你自己本地的端口、账号、密码
2. 启动Nacos、Mysql、Redis、RabbitMq
3. 将公共服务 api-common 以及客户端 SDK 安装到本地仓库
4. 按顺序启动服务

服务启动顺序参考：
1. api-backend
2. api-order
3. api-gateway
4. api-third-party
5. api-interface

### 前端

环境要求：Node.js >= 16

安装依赖：

```
yarn
```

启动：

```
npm run start:dev
```
**注意：如果想要体验订单和支付业务并且没有个人云服务器的，需要配置内网穿透才能体验(非必要)**


## 欢迎贡献

项目需要大家的支持，期待更多小伙伴的贡献，你可以：

- 对于项目中的Bug和建议，能够在Issues区提出建议，我会积极响应





