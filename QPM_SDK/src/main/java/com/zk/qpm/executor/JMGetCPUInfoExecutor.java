package com.jm.android.gt.executor;

import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.text.TextUtils;

import com.jm.android.gt.JMGTException;
import com.jm.android.gt.analysis.JMCPUAnalysis;
import com.jm.android.gt.floatview.JMFloatViewType;
import com.jm.android.gt.floatview.renderer.IFloatViewRenderer;
import com.jm.android.gt.floatview.renderer.JMGetCPUInfoRenderer;
import com.jm.android.gt.manager.JMFloatViewManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class JMGetCPUInfoExecutor implements IExecutor {

    private JMCPUAnalysis analysis;
    private boolean isStop;

    public JMGetCPUInfoExecutor() {
        analysis = new JMCPUAnalysis();
    }

    @Override
    public String type() {
        return JMFloatViewType.TYPE_FLOAT_VIEW_CPU_VIEW;
    }

    @Override
    public void createShowView(Context context) {
        IFloatViewRenderer cpuViewBean = new JMGetCPUInfoRenderer();
        JMFloatViewManager.getInstance().addItem(cpuViewBean);
    }

    @Override
    public void destoryShowView() {
        JMFloatViewManager.getInstance().removeItem(type());
    }

    @Override
    public void exec() throws JMGTException {
        java.lang.Process process = null;
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                process = Runtime.getRuntime().exec("top -n 1 -d 0");
            } else {
                process = Runtime.getRuntime().exec("top -n 1");
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            int cpuIndex = -1;
            while (!isStop && (line = reader.readLine()) != null) {
                line = line.trim();
                if (TextUtils.isEmpty(line)) {
                    continue;
                }
                // 找到CPU指标的下表索引，不同的系统版本下标索引不同
                int tempIndex = getCPUIndex(line);
                if (tempIndex != -1) {
                    cpuIndex = tempIndex;
                    continue;
                }
                if (line.startsWith(String.valueOf(Process.myPid()))) {
                    if (cpuIndex == -1) {
                        continue;
                    }
                    String[] param = line.split("\\s+");
                    if (param.length <= cpuIndex) {
                        continue;
                    }
                    String cpu = param[cpuIndex];
                    // 有些设备自带了%，需要去除
                    if (cpu.endsWith("%")) {
                        cpu = cpu.substring(0, cpu.lastIndexOf("%"));
                    }
                    try {
                        analysis.onCollectCPUInfo(Double.parseDouble(cpu) / Runtime.getRuntime().availableProcessors());
                        return;
                    } catch (NumberFormatException e) {
                        continue;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    @Override
    public void reset() {
        isStop = false;
    }

    private int getCPUIndex(String line) {
        if (line.contains("CPU")) {
            String[] titles = line.split("\\s+");
            for (int i = 0; i < titles.length; i++) {
                if (titles[i].contains("CPU")) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public void stop() {
        isStop = true;
    }
}
