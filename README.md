# Chen API

> 一个丰富的API开放调用平台，为开发者提供便捷、实用，安全的API调用体验
>
> Java + React 全栈项目，包括网站前台+后台，感谢[程序猿鱼皮](https://github.com/liyupi)提供的项目基础框架，我也是在此基础上作拓展
>
> 
>
> 在线体验地址：[主页 - Panda-API](http://123.207.3.145/)







## 项目展示

待定







## 项目背景

本人在实习过程中发现公司有调用第三方接口的业务，并且我在学习过程中也有调用第三方接口的尝试，比如手机短信接口，视频点播接口，支付接口等等，一直好奇第三方接口的服务提供方是如何实现安全，方便，快捷的接口的调用体验，遂萌生了一个自己写一个接口平台的想法，于是该项目诞生了



Chen API 平台统一抽取接口调用的SDK，动态适配和调用所有接口，给客户提供简单，方便，快捷的接口调用体验，并且使用API签名校验保障接口调用的安全性





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
- api-gateway ：为网关服务，涉及到网关限流，统一鉴权，统一日志处理，接口统计
- api-order ：为订单服务，主要涉及到接口的购买等
- api-third-party：为第三方服务，主要涉及到腾讯云短信、支付宝沙箱支付功能
- api-interface：为接口服务，提供可供调用的接口
- api-sdk：提供给开发者的SDK







## 功能模块

> 🌟 亮点功能 🚀 未来计划

- 用户、管理员
  - 登录注册
  - 个人主页
  - 设置个人信息
  - 管理员：用户管理
  - 管理员：接口管理
  - 管理员：接口分析、订单分析
- 接口
  - 浏览接口信息
  - 🌟 数字签名校验接口调用权限
  - 🌟 SDK调用接口
  - 接口搜索
  - 购买接口
  - 下载SDK
  - 用户上传自己的接口（🚀）
- 订单
  - 创建订单
  - 订单超时回滚
  - 支付宝沙箱支付



