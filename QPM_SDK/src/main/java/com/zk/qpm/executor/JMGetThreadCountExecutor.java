package com.jm.android.gt.executor;

import android.content.Context;
import android.os.Process;

import com.jm.android.gt.JMGTException;
import com.jm.android.gt.analysis.JMThreadAnalysis;
import com.jm.android.gt.floatview.JMFloatViewType;
import com.jm.android.gt.floatview.renderer.IFloatViewRenderer;
import com.jm.android.gt.floatview.renderer.JMGetThreadCountRenderer;
import com.jm.android.gt.manager.JMFloatViewManager;
import com.jm.android.gt.manager.JMThreadManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class JMGetThreadCountExecutor implements IExecutor {

    private static final int BUFFER_SIZE = 1000;
    private JMThreadAnalysis threadAnalysis;

    public JMGetThreadCountExecutor() {
        threadAnalysis = new JMThreadAnalysis();
    }

    @Override
    public String type() {
        return JMFloatViewType.TYPE_FLOAT_VIEW_THREAD_COUNT;
    }

    @Override
    public void createShowView(Context context) {
        IFloatViewRenderer threadCountBean = new JMGetThreadCountRenderer();
        JMFloatViewManager.getInstance().addItem(threadCountBean);
    }

    @Override
    public void destoryShowView() {
        JMFloatViewManager.getInstance().removeItem(type());
    }

    @Override
    public void exec() throws JMGTException {
        int pid = Process.myPid();
        int threadCount = getThreadsCount(pid);
        // 计算本GT所占用的线程数
        int gtThreadCount = getGTConsumeThreadCount();
        threadAnalysis.onCollectThreadInfo(threadCount, gtThreadCount);
    }

    @Override
    public void reset() {

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
        return JMThreadManager.getInstance().getAllThreadCount();
    }
}
