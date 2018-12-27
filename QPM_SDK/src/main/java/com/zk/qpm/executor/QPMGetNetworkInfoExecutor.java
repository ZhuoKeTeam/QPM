package com.zk.qpm.executor;

import android.content.Context;
import android.text.TextUtils;

import com.zk.qpm.QPMException;
import com.zk.qpm.analysis.QPMNetworkAnalysis;
import com.zk.qpm.floatview.QPMFloatViewType;
import com.zk.qpm.floatview.renderer.IFloatViewRenderer;
import com.zk.qpm.floatview.renderer.QPMGetNetworkInfoRenderer;
import com.zk.qpm.manager.QPMFloatViewManager;
import com.zk.qpm.manager.QPMSwitchManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSource;

public class QPMGetNetworkInfoExecutor implements IExecutor, Interceptor {

    private QPMNetworkAnalysis analysis;
    private boolean isStop = true;

    public QPMGetNetworkInfoExecutor() {
        analysis = new QPMNetworkAnalysis();
    }

    @Override
    public String type() {
        return QPMFloatViewType.TYPE_FLOAT_VIEW_NETWORK_API;
    }

    @Override
    public void createShowView(Context context) {
        IFloatViewRenderer networkBean = new QPMGetNetworkInfoRenderer();
        QPMFloatViewManager.getInstance().addItem(networkBean);
    }

    @Override
    public void destoryShowView() {
        QPMFloatViewManager.getInstance().removeItem(type());
    }

    @Override
    public void exec() throws QPMException {
        //因为网络接口获取是被动的，所以不在这儿处理
    }

    public void reset() {
        isStop = false;
    }

    @Override
    public void stop() {
        isStop = true;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (isStop || !QPMSwitchManager.getInstance().isSwitchOpen(QPMSwitchManager.getInstance().getSwitchs(), this)) {
            return chain.proceed(chain.request());
        }
        Request request = chain.request();
        long startTime = System.currentTimeMillis();
        Response response = chain.proceed(request);
        long endTime = System.currentTimeMillis();

        String originUrl = request.url().toString();
        String method = request.method();
        long requestTime = endTime - startTime;
        String requestHeader = request.headers().toString();
        String responseHeader = response.headers().toString();
        String requestContent = "";
        String responseContent = "";
        long requestSize = 0;
        long responseSize = 0;
        int responseCode = response.code();

        try {
            if (request.body() != null) {
                RequestBody body = request.body();
                requestSize = body.contentLength();
                if (requestSize > 0) {
                    requestContent = getRequestContent(body);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (response.body() != null && response.body().source() != null) {
                BufferedSource source = response.body().source();
                int readSize = 1024 * 10;
                while (source.request(readSize)) {
                    readSize = 2 * readSize;
                }
                Buffer buffer = source.buffer().clone();
                responseSize = buffer.size();
                if (responseSize > 0) {
                    responseContent = getReponseContent(response, buffer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        analysis.onCollectNetworkInfo(originUrl, method, requestSize, requestTime, requestHeader, requestContent,
                responseSize, responseHeader, responseContent, String.valueOf(responseCode), System.currentTimeMillis());

        return response;
    }

    private String getRequestContent(RequestBody body) throws IOException {
        Buffer buffer = new Buffer();
        body.writeTo(buffer);
        BufferedReader reader = new BufferedReader(new InputStreamReader(buffer.inputStream()));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    private String getReponseContent(Response response, Buffer buffer) throws IOException {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        String encoding = response.header("Content-Encoding");
        if (!TextUtils.isEmpty(encoding) && encoding.equals("gzip")) {
            reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(buffer.inputStream())));
        } else {
            reader = new BufferedReader(new InputStreamReader(buffer.inputStream()));
        }
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
}
