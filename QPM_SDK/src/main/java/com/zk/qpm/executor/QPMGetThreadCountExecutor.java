package com.zk.qpm.executor;

import android.content.Context;
import android.os.Process;

import com.zk.qpm.QPMException;
import com.zk.qpm.analysis.QPMThreadAnalysis;
import com.zk.qpm.floatview.QPMFloatViewType;
import com.zk.qpm.floatview.renderer.IFloatViewRenderer;
import com.zk.qpm.floatview.renderer.QPMGetThreadCountRenderer;
import com.zk.qpm.manager.QPMFloatViewManager;
import com.zk.qpm.manager.QPMThreadManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class QPMGetThreadCountExecutor implements IExecutor {

    private static final int BUFFER_SIZE = 1000;
    private QPMThreadAnalysis threadAnalysis;

    public QPMGetThreadCountExecutor() {
        threadAnalysis = new QPMThreadAnalysis();
    }

    @Override
    public String type() {
        return QPMFloatViewType.TYPE_FLOAT_VIEW_THREAD_COUNT;
    }

    @Override
    public void createShowView(Context context) {
        IFloatViewRenderer threadCountBean = new QPMGetThreadCountRenderer();
        QPMFloatViewManager.getInstance().addItem(threadCountBean);
    }

    @Override
    public void destoryShowView() {
        QPMFloatViewManager.getInstance().removeItem(type());
    }

    @Override
    public void exec() throws QPMException {
        int pid = Process.myPid();
        int threadCount = getThreadsCount(pid);
        // 计算本GT所占用的线程数
        int gtThreadCount = getGTConsumeThreadCount();
        threadAnalysis.onCollectThreadInfo(threadCount, gtThreadCount);
    }

    @Override
    public void stop() {

    }

    private int getThreadsCount(int pid) {
        int threadCount = 0;
        File threadDir = new File("/proc/" + pid + "/task");
        if (!threadDir.exists()) {
            return threadCount;
        }

        File[] threadFiles = threadDir.listFiles();
        threadCount = threadFiles.length;
        return threadCount;
    }

    private int getThreadsCountByProcess(int pid){
        int threadCount = 0;
        File progressFile = new File("/proc/" + pid + "/stat");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(progressFile)), BUFFER_SIZE);
            String line = reader.readLine();
            String[] fields = line.split(" ");
            threadCount = Integer.parseInt(fields[19]);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return threadCount;
    }

    private int getGTConsumeThreadCount() {
        return QPMThreadManager.getInstance().getAllThreadCount();
    }
}
