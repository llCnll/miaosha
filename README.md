# miaosha
基础秒杀功能

## 优化方案
1. 页面缓存
2. url缓存
3. 对象缓存(未做, 主要缓存User对象)

## 加入rabbitmq
存在卖不玩的情况...  get GoodsKey:gs1 --> "-39992"
相同用户可能重复秒杀, 所以库存预减, 导致reids中的值也依旧往下减
QPS 2570 原先 1500 提高近一倍
基本预期已经达成!

## 秒杀接口地址隐藏
秒杀开始前, 先去请求接口获取秒杀地址
1. 接口改造, 带上PathVariable参数
2. 添加生成地址的接口
3. 秒杀收到秦秋, 先验证PathVariable

## 接口的防刷限流
对接口做限流
1. 使用拦截器


# 总结
## 项目框架搭建
1. Spring Boot 环境搭建
2. 集成Thymeleaf, Result结果封装
3. 集成MyBatis+Druid
4. 集成Jedis+Redis安装+通用缓存Key封装

## 登录功能
1. 明文密码两次MD5处理
2. JSR303参数检验+全局异常处理器(接口的异常, 还未写页面异常)
3. 分布式Session

## 秒杀功能
1. 商品列表页 (页面缓存)
2. 商品详情页 (url缓存/前后端分离)
3. 订单详情页 (前后端分离)

## 页面优化技术
1. 页面缓存+URL缓存+对象缓存
2. 页面静态化, 前后端分离
3. 静态资源优化
4. CDN优化(3, 4 未涉及)

## 接口优化
1. Redis预减库存减少数据库访问
2. 内存标记减少Redis访问
3. RabbitMQ队列缓冲, 异步下单
4. Nginx水平扩展(本机设置)

## 安全优化
1. 秒杀接口地址隐藏
2. 接口防刷