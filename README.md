# QPM

[![](https://www.jitpack.io/v/ZhuoKeTeam/QPM.svg)](https://www.jitpack.io/#ZhuoKeTeam/QPM)   [![gdky005](https://img.shields.io/badge/%E4%BD%9C%E8%80%85-gdky005-orange.svg)](http://www.gdky005.com)   [![HyperionChen](https://img.shields.io/badge/%E4%BD%9C%E8%80%85-HyperionChen-orange.svg)](https://www.jianshu.com/u/4730943c8fd0)

Quality Performance Monitor ---> QPM
质量性能监控组件，方便查看当前 APP 的性能。

![screen_QPM_home_pic](https://raw.githubusercontent.com/ZhuoKeTeam/QPM/master/pic/screen_QPM_home_pic_s.png)


# 下面做一个性能优化组件的简单介绍：
1. 获取手机常用的基本信息，方便开发和测试快速获取手机基本信息 （是否 root, 版本号，wifi, 硬件信息等等）；
2. 可以实时获取当前 App 的 CPU 和内存信息，可以判断手机是否占用更多资源；
3. 界面卡不卡，就看 FPS。 绿色表示正常，红色表示卡顿；
4. 轻松获取 当前运行的 Activity，快速定位界面。
5. App 已经开启多少线程，是否占用过多资源。
6. 可以查看 App 运行后的流量使用情况，查看 App 在3G，4G 的流量消耗情况。
7. H5 页面经常白屏，可以判断出白屏原因，和资源请求问题。
8. 不方便抓包的时候可以用组件的  显示 API 接口，会记录请求地址，服务器相关状态码，Cookie, 返回数据。
9. Apk 的 AndroidManifest.xml 信息，直接查看常用的四大组件，和注册权限，并且可以测试对应的 Activity。
10. 可以获取到 App 中所有的 SP 存储信息，并且可以修改。及时手机没有 root 也可以，方便开发快速定位问题。

# 人性化的体验
1. 精简模式 （避免悬浮窗占用太大屏幕）；
2. 默认内置五种自定义数据展示（轻松实时监控 App 相关变化数据）；
3. 屏幕录制。

# 本项目开发人员 （期待会有更多贡献者）

- [孤独狂饮](https://www.gdky005.com)
- [HyperionChen](https://www.jianshu.com/u/4730943c8fd0)

# 项目使用方式

1. 使用 gradle 依赖：
```
allprojects {
    repositories {
        jcenter()
        google()
        mavenLocal()
        maven { url 'https://www.jitpack.io' } # 添加这行

    }
}
```


```
implementation "com.github.ZhuoKeTeam:QPM:1.0.0"
```
2. 在 Application 或者最开始的页面添加：
```
QPMManager.getInstance().init(this);
```
3. 显示悬浮窗：
```
if (!QPMManager.getInstance().floatViewShow()) {
    Toast.makeText(mContext, "请开启悬浮窗权限", Toast.LENGTH_SHORT).show();
}
```
4. 隐藏悬浮窗：
```
QPMManager.getInstance().floatViewHide();
```

5. WebView 的 H5 需要单独设置。
6. 本组件最好在 debug 模式下开启, 正式版本也可以带上，但是需要设置一个彩蛋开关，打开开关后方可使用。


# 备注
本开源项目于2018年12月24日（平安夜）发布，文档等资料待进一步添加，莫着急。如果有好的想法或者建议直接 issue，或者直接加WX：gdky005。