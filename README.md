# QPM

[![](https://www.jitpack.io/v/ZhuoKeTeam/QPM.svg)](https://www.jitpack.io/#ZhuoKeTeam/QPM)   [![gdky005](https://img.shields.io/badge/%E4%BD%9C%E8%80%85-gdky005-orange.svg)](http://www.gdky005.com)   [![HyperionChen](https://img.shields.io/badge/%E4%BD%9C%E8%80%85-HyperionChen-orange.svg)](https://www.jianshu.com/u/4730943c8fd0)

Quality Performance Monitor ---> QPM
质量性能监控组件，方便查看当前 APP 的性能。

![screen_QPM_home_pic](https://raw.githubusercontent.com/ZhuoKeTeam/QPM/master/pic/screen_QPM_home_pic_s.png)

# Demo 下载地址：
[QPM Demo](https://raw.githubusercontent.com/ZhuoKeTeam/QPM/master/release/qpm_release_v100.apk
): https://raw.githubusercontent.com/ZhuoKeTeam/QPM/master/release/qpm_release_v100.apk

# 文档资料
1. [QPM 性能监控组件<总>](http://gdky005.com/2019/01/03/QPM-%E6%80%A7%E8%83%BD%E7%9B%91%E6%8E%A7%E7%BB%84%E4%BB%B6-%E6%80%BB/)
2. [QPM 之简介](http://gdky005.com/2019/01/03/QPM-%E4%B9%8B%E7%AE%80%E4%BB%8B/)
3. [QPM 之缘起](http://gdky005.com/2019/01/03/QPM-%E4%B9%8B%E7%BC%98%E8%B5%B7/)
4. [QPM 准备优化前的思考](http://gdky005.com/2019/01/03/QPM-%E5%87%86%E5%A4%87%E4%BC%98%E5%8C%96%E5%89%8D%E7%9A%84%E6%80%9D%E8%80%83/)
5. [QPM 之悬浮窗助力性能优化](http://gdky005.com/2019/01/03/QPM-%E4%B9%8B%E6%82%AC%E6%B5%AE%E7%AA%97%E5%8A%A9%E5%8A%9B%E6%80%A7%E8%83%BD%E4%BC%98%E5%8C%96/)
6. [QPM 之悬浮窗设置信息](http://gdky005.com/2019/01/03/QPM-%E4%B9%8B%E6%82%AC%E6%B5%AE%E7%AA%97%E8%AE%BE%E7%BD%AE%E4%BF%A1%E6%81%AF/)
7. [QPM 之同类 PM 对比](http://gdky005.com/2019/01/03/QPM-%E4%B9%8B%E5%90%8C%E7%B1%BB-PM-%E5%AF%B9%E6%AF%94/)
8. [添加一个自定义监控数据步骤  添加新监控项的步骤](https://github.com/ZhuoKeTeam/QPM/blob/master/%E6%B7%BB%E5%8A%A0%E6%96%B0%E7%9B%91%E6%8E%A7%E9%A1%B9%E7%9A%84%E6%AD%A5%E9%AA%A4.md)
9. [添加OKHTTP网络监控](https://github.com/ZhuoKeTeam/QPM/blob/master/%E6%B7%BB%E5%8A%A0OKHTTP%E7%BD%91%E7%BB%9C%E7%9B%91%E6%8E%A7.md)


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
4. 开关页面拖拽，可以使悬浮窗上的选项同步排序（优先关注最关心的选项）
5. 点击悬浮窗上的图标，可以进入主菜单页面（一些不方便在悬浮窗上展示的数据会在这里）

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
implementation "com.github.ZhuoKeTeam:QPM:1.0.5"
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
7. 另有两篇文档 [添加新监控项的步骤.md](https://github.com/ZhuoKeTeam/QPM/blob/master/添加新监控项的步骤.md) 和 [添加OKHTTP网络监控.md](https://github.com/ZhuoKeTeam/QPM/blob/master/添加OKHTTP网络监控.md)

# 备注
本开源项目于2018年12月24日（平安夜）发布，文档等资料待进一步添加，莫着急。如果有好的想法建议直接 issues，或者加微信：gdky005。