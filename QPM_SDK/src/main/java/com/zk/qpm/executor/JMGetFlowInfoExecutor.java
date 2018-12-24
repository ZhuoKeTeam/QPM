package com.jm.android.gt.executor;

import android.content.Context;
import android.net.TrafficStats;
import android.os.Process;

import com.jm.android.gt.JMGTException;
import com.jm.android.gt.analysis.JMFlowAnalysis;
import com.jm.android.gt.floatview.JMFloatViewType;
import com.jm.android.gt.floatview.renderer.IFloatViewRenderer;
import com.jm.android.gt.floatview.renderer.JMGetFlowInfoRenderer;
import com.jm.android.gt.manager.JMFloatViewManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;

public class JMGetFlowInfoExecutor implements IExecutor {

    private JMFlowAnalysis flowAnalysis;

    public JMGetFlowInfoExecutor() {
        flowAnalysis = new JMFlowAnalysis();
    }

    @Override
    public String type() {
        return JMFloatViewType.TYPE_FLOAT_VIEW_FLOW_DATA;
    }

    @Override
    public void createShowView(Context context) {
        IFloatViewRenderer flowBean = new JMGetFlowInfoRenderer();
        JMFloatViewManager.getInstance().addItem(flowBean);
    }

    @Override
    public void destoryShowView() {
        JMFloatViewManager.getInstance().removeItem(type());
    }

    @Override
    public void exec() throws JMGTException {
        long time = System.currentTimeMillis();
        long[] flow = getFlowInfo();
        flowAnalysis.onCollectFlowInfo(time, flow[0], flow[1]);
    }

    @Override
    public void reset() {

    }

    @Override
    public void stop() {

    }

    private long[] getFlowInfo() {
        int uid = Process.myUid();
        return getFlowByAPI(uid);
        /*try {
            return getFlowByFile(uid);
        } catch (Exception e) {
            return getFlowByAPI(uid);
        }*/
    }

    private long[] getFlowByFile(final int uid) throws Exception {
        File dir = new File("/proc/uid_stat/");
        String[] children = dir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.contains(String.valueOf(uid));
            }
        });
        if (children.length == 0) {
            throw new IllegalAccessException("without uid file");
        }

        File uidFileDir = new File(dir, String.valueOf(uid));
        File uidActualFileReceived = new File(uidFileDir, "tcp_rcv");
        File uidActualFileSent = new File(uidFileDir, "tcp_snd");
        String textReceived = new BufferedReader(new FileReader(uidActualFileReceived)).readLine();
        String textSent = new BufferedReader(new FileReader(uidActualFileSent)).readLine();
        return new long[]{Long.parseLong(textSent), Long.parseLong(textReceived)};
    }

    private long[] getFlowByAPI(int uid) {
        long flowUpload = TrafficStats.getUidTxBytes(uid);
        long flowDownload = TrafficStats.getUidRxBytes(uid);
        return new long[]{flowUpload, flowDownload};
    }
}
