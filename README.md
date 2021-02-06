# douban-movie

## 能做什么?

1. 提供vip电影和电视剧资源 (部分支持1080p, 4k, 蓝光)
2. 资源链接直接嵌入豆瓣网页中

## 使用方法:

1. 运行项目:

```shell
mvn clean
mvn spring-boot:run
```

2. 复制`src/main/resources/douban.js`内容到猴油插件(tampermonkey)中
3. 打开电脑端豆瓣任意视频效果如下

## 效果图:

![](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/24b660995df14f899c7611dc5ff599f8~tplv-k3u1fbpfcp-watermark.image)

## 附:

猴油下载地址: [tampermonkey](https://www.tampermonkey.net/)

## 安卓端如何使用?
安卓端可以配合via浏览器并进入豆瓣电脑端来使用:
1. 依次进入 设置 > 脚本 > 新建
   - 域名填: *
   - 代码填:  `src/main/resources/douban.js`的内容