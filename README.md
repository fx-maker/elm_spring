# 饿了么外卖系统 - Spring Cloud 微服务版

一个基于 Spring Cloud 微服务架构的外卖订餐系统，采用前后端分离设计，支持用户端和管理端操作。

## 项目简介

本项目是《Spring Cloud 饿了么外卖系统》的课程实验项目，采用以下技术栈：

### 后端技术栈
- **Spring Cloud Netflix** - 微服务架构核心框架
- **Spring Boot** - 快速构建 Spring 应用
- **MyBatis** - 数据持久层框架
- **MySQL** - 关系型数据库
- **Eureka** - 服务注册与发现
- **Zuul/Gateway** - API 网关
- **Feign** - 服务间调用
- **Hystrix** - 熔断器

### 前端技术栈
- **Vue.js** - 渐进式 JavaScript 框架
- **Vue Router** - 前端路由管理
- **Axios** - HTTP 请求库
- **Font Awesome** - 图标库

## 项目架构

```
┌─────────────────────────────────────────────────────────────┐
│                        前端 (elmclient)                      │
│                    Vue.js + Axios                           │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                    网关服务 (Gateway)                        │
│                  localhost:14000                            │
└─────────────────────────────────────────────────────────────┘
                              │
        ┌─────────┬─────────┬─────────┬─────────┬─────────┐
        ▼         ▼         ▼         ▼         ▼         ▼
   ┌───────┐ ┌───────┐ ┌───────┐ ┌───────┐ ┌───────┐ ┌───────┐
   │ User  │ │Food   │ │Business│ │ Cart  │ │Orders │ │Delivery│
   │Server │ │Server │ │Server  │ │Server │ │Server │ │Address │
   │10100/ │ │10200/ │ │10300/  │ │10400/ │ │10600/ │ │10500/  │
   │10101  │ │10201  │ │10301   │ │10401  │ │10601  │ │10501   │
   └───────┘ └───────┘ └───────┘ └───────┘ └───────┘ └───────┘
        │         │         │         │         │
        └─────────┴─────────┴─────────┴─────────┘
                              │
                              ▼
                  ┌───────────────────────┐
                  │    Eureka Server      │
                  │   localhost:13000     │
                  └───────────────────────┘
```

## 服务模块

| 服务名称 | 端口 | 说明 |
|---------|------|------|
| eureka_server_13000 | 13000 | Eureka 注册中心 |
| eureka_server_13001 | 13001 | Eureka 注册中心（集群） |
| user_server_10100/10101 | 10100/10101 | 用户服务 |
| food_server_10200/10201 | 10200/10201 | 食品服务 |
| business_server_10300/10301 | 10300/10301 | 商家服务 |
| cart_server_10400/10401 | 10400/10401 | 购物车服务 |
| deliveryaddress_server_10500/10501 | 10500/10501 | 配送地址服务 |
| orders_server_10600/10601 | 10600/10601 | 订单服务 |
| wallet_server_10700 | 10700 | 钱包服务 |
| points_server_10800 | 10800 | 积分服务 |
| gateway_server_14000 | 14000 | API 网关 |
| config_server_15000/15001 | 15000/15001 | 配置中心 |

## 功能模块

### 用户端
- [x] 用户注册与登录
- [x] 浏览商家列表
- [x] 查看商家详情和菜单
- [x] 购物车管理（添加、修改、删除）
- [x] 下单与支付
- [x] 订单管理（查看、取消）
- [x] 收货地址管理
- [x] 钱包功能
- [x] 积分系统

### 商家端
- [x] 商家登录
- [x] 管理商家信息
- [x] 管理食品菜单（增删改查）
- [x] 查看店铺订单

### 管理端
- [x] 用户管理
- [x] 用户身份分配（普通用户/商家/管理员）
- [x] 商家分配管理
- [x] 系统管理

## 快速开始

### 环境要求
- JDK 1.8+
- Maven 3.6+
- Node.js 14+
- MySQL 5.7+
- IDE (IntelliJ IDEA / VS Code)

### 数据库配置

创建数据库 `elm` 并执行初始化脚本：

```sql
-- 创建数据库
CREATE DATABASE elm CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- 使用数据库
USE elm;
```

### 后端启动

1. **启动 Eureka Server**
```bash
cd springcloud_elm/eureka_server_13000
mvn spring-boot:run
# 或启动另一个实例
cd springcloud_elm/eureka_server_13001
mvn spring-boot:run
```

2. **启动其他微服务**（按顺序启动）
```bash
# 用户服务
cd springcloud_elm/user_server_10100
mvn spring-boot:run

# 食品服务
cd springcloud_elm/food_server_10200
mvn spring-boot:run

# 商家服务
cd springcloud_elm/business_server_10300
mvn spring-boot:run

# 购物车服务
cd springcloud_elm/cart_server_10400
mvn spring-boot:run

# 配送地址服务
cd springcloud_elm/deliveryaddress_server_10500
mvn spring-boot:run

# 订单服务
cd springcloud_elm/orders_server_10600
mvn spring-boot:run

# 网关服务
cd springcloud_elm/gateway_server_14000
mvn spring-boot:run
```

### 前端启动

```bash
cd elmclient

# 安装依赖
npm install

# 启动开发服务器
npm run serve
```

访问 http://localhost:8080 即可使用系统。

## 项目结构

```
elm_spring/
├── elmclient/                    # 前端项目
│   ├── src/
│   │   ├── api/                 # API 接口封装
│   │   ├── common/              # 公共方法
│   │   ├── components/          # 公共组件
│   │   ├── router/              # 路由配置
│   │   ├── views/               # 页面组件
│   │   ├── App.vue              # 根组件
│   │   └── main.js              # 入口文件
│   └── public/                  # 静态资源
│
├── springcloud_elm/              # 后端微服务
│   ├── eureka_server_13000/     # Eureka 注册中心
│   ├── eureka_server_13001/     # Eureka 注册中心（备份）
│   ├── user_server_10100/       # 用户服务
│   ├── food_server_10200/       # 食品服务
│   ├── business_server_10300/   # 商家服务
│   ├── cart_server_10400/       # 购物车服务
│   ├── deliveryaddress_server_10500/  # 配送地址服务
│   ├── orders_server_10600/     # 订单服务
│   ├── wallet_server_10700/     # 钱包服务
│   ├── points_server_10800/     # 积分服务
│   ├── gateway_server_14000/    # API 网关
│   ├── config_server_15000/     # 配置中心
│   └── pom.xml                  # 父 POM
│
├── 商家用户功能修改记录.md        # 功能修改文档
└── README.md                    # 项目说明文档
```

## 数据库表结构

### 用户表 (user)
| 字段 | 类型 | 说明 |
|------|------|------|
| userId | VARCHAR(50) | 用户ID（主键） |
| password | VARCHAR(50) | 密码 |
| userName | VARCHAR(50) | 用户名 |
| userSex | VARCHAR(10) | 性别 |
| img | VARCHAR(200) | 头像 |
| status | INT | 状态 |
| userType | VARCHAR(20) | 用户类型（USER/BUSINESS/ADMIN） |

### 商家表 (business)
| 字段 | 类型 | 说明 |
|------|------|------|
| businessId | INT | 商家ID（主键） |
| businessName | VARCHAR(100) | 商家名称 |
| businessAddress | VARCHAR(200) | 商家地址 |
| businessExplain | VARCHAR(500) | 商家描述 |
| orderTypeId | INT | 订单类型 |
| starPrice | DECIMAL | 起送价 |
| deliveryPrice | DECIMAL | 配送费 |
| ownerId | VARCHAR(50) | 所有者ID |

### 食品表 (food)
| 字段 | 类型 | 说明 |
|------|------|------|
| foodId | INT | 食品ID（主键） |
| foodName | VARCHAR(100) | 食品名称 |
| foodExplain | VARCHAR(500) | 食品描述 |
| foodImg | VARCHAR(200) | 食品图片 |
| foodPrice | DECIMAL | 食品价格 |
| businessId | INT | 所属商家 |
| remarks | VARCHAR(100) | 备注 |

## API 接口文档

### 用户接口
- `POST /UserController/reg` - 用户注册
- `POST /UserController/login` - 用户登录
- `GET /UserController/getUserById/{userId}` - 获取用户信息
- `GET /UserController/listAllUsers` - 获取所有用户（管理员）
- `PUT /UserController/updateUserType/{userId}/{userType}` - 更新用户身份

### 商家接口
- `GET /BusinessController/listBusinessByOrderTypeId/{orderTypeId}` - 获取商家列表
- `GET /BusinessController/getBusinessById/{businessId}` - 获取商家详情
- `POST /BusinessController/saveBusiness` - 新增商家
- `PUT /BusinessController/updateOwner/{businessId}/{ownerId}` - 分配商家所有者

### 食品接口
- `GET /FoodController/listFoodByBusinessId/{businessId}` - 获取食品列表
- `POST /FoodController/saveFood` - 新增食品
- `PUT /FoodController/updateFood` - 更新食品
- `DELETE /FoodController/deleteFood/{foodId}` - 删除食品

### 订单接口
- `POST /OrdersController/createOrders` - 创建订单
- `GET /OrdersController/listOrdersByUserId/{userId}` - 获取用户订单
- `PUT /OrdersController/updateOrderStatus/{orderId}/{status}` - 更新订单状态

## 用户身份说明

| 身份 | 说明 | 权限 |
|------|------|------|
| USER | 普通用户 | 浏览、下单、查看订单等 |
| BUSINESS | 商家用户 | 管理自己的商家和食品 |
| ADMIN | 管理员 | 系统管理、用户管理等 |

## 课程实验报告

详细的实验报告请查看：[SpringCloud饿了么实验报告.docx](./SpringCloud饿了么实验报告.docx)

## 开发团队

- 指导教师：[待补充]
- 开发学生：[待补充]

## 版本历史

- **v1.0** - 基础功能实现（用户、商家、食品、购物车、订单）
- **v1.1** - 商家用户系统扩展（食品管理、用户身份分配）

## 许可证

本项目仅供学习交流使用。

---

*最后更新：2026年5月*
