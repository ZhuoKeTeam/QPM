### 1. 接入OKHTTP网络监控

**默认是隐藏了OKHTTP网络监控功能的**，如果想要开启OKHTTP网络监控，则按如下步骤集成

1. 依赖OKHTTP，可以依赖任意版本

```
implementation "com.squareup.okhttp3:okhttp:3.12.0"
```

2. 依赖了OKHTTP后，在开关页面会出现"是否显示API接口"的选项，打开此选项
3. 给OkHttpClient添加本项目提供的 Interceptor

```
private void testOKHTTP() {
    String url = "http://suggest.taobao.com/sug?code=utf-8&q=车载";
    OkHttpClient okHttpClient = new OkHttpClient.Builder().
            addInterceptor(QPMManager.getInstance().getOkHttpInterceptor())
            .build();
    final Request request = new Request.Builder()
            .url(url)
            .get()//默认就是GET请求，可以不写
            .build();
    Call call = okHttpClient.newCall(request);
    call.enqueue(new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.d(TAG, "onFailure: " + (e == null ? "" : e.getMessage()));
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response != null && response.body() != null) {
                Log.d(TAG, "onResponse: " + response.body().string());
            }
        }
    });
}
```

如果网络库是单独的一个库，此时若想集成QPM，但是又不想依赖QPM，怎么办呢？用反射的方式设置

```
private void addQPMInterceptor(OkHttpClient.Builder builder){
    try {
        Class clazz = Class.forName("com.zk.qpm.manager.QPMManager");
        Method getInstanceMethod = clazz.getDeclaredMethod("getInstance", new Class[]{});
        Object instance = getInstanceMethod.invoke(null, new Object[]{});
        Method getOkHttpInterceptorMethod = clazz.getDeclaredMethod("getOkHttpInterceptor", new Class[]{});
        Object interceptor = getOkHttpInterceptorMethod.invoke(instance, new Object[]{});
        if (interceptor instanceof Interceptor) {
            builder.addInterceptor((Interceptor) interceptor);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

### 2. 使用 OKHTTP 网络监控

使用第一步设置了 Interceptor 的 OkHttpClient 进行请求操作，悬浮窗上会显示网络请求的概要信息：url 的 path

比如请求 http://suggest.taobao.com/sug?code=utf-8&q=车载，那么只会显示 sug

这是因为悬浮窗上的空间寸土寸金。如果想要查看请求的详细信息，可以按如下操作

1. 点击悬浮窗的图标，此时会进入主菜单页面
2. 点击"网络接口API列表"，会进入请求拦截的详细信息
3. 可以查看请求地址、请求方式、返回状态、请求时长、请求数据大小、返回数据大小、请求Header、请求内容、返回Header、返回内容