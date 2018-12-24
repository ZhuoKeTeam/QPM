package com.jm.android.gt.executor;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;
import android.os.Process;

import com.jm.android.gt.JMGTException;
import com.jm.android.gt.analysis.JMMemoryAnalysis;
import com.jm.android.gt.floatview.JMFloatViewType;
import com.jm.android.gt.floatview.renderer.IFloatViewRenderer;
import com.jm.android.gt.floatview.renderer.JMGetMemoryInfoRenderer;
import com.jm.android.gt.manager.JMFloatViewManager;

import java.lang.reflect.Method;

public class JMGetMemoryInfoExecutor implements IExecutor {

    private JMMemoryAnalysis memoryAnalysis;
    private ActivityManager am;

    public JMGetMemoryInfoExecutor(ActivityManager am) {
        memoryAnalysis = new JMMemoryAnalysis();
        this.am = am;
    }

    @Override
    public String type() {
        return JMFloatViewType.TYPE_FLOAT_VIEW_MEMORY_VIEW;
    }

    @Override
    public void createShowView(Context context) {
        IFloatViewRenderer memoryBean = new JMGetMemoryInfoRenderer();
        JMFloatViewManager.getInstance().addItem(memoryBean);
    }

    @Override
    public void destoryShowView() {
        JMFloatViewManager.getInstance().removeItem(type());
    }

    @Override
    public void exec() throws JMGTException {
        int pid = Process.myPid();
        int memory = getMemoryForProcess(pid);
        memoryAnalysis.onCollectMemoryInfo(memory);
    }

    @Override
    public void reset() {

    }

    @Override
    public void stop() {

    }

    // 进程总内存
    private int getMemoryForProcess(int pid) {
        if (am == null) {
            return 0;
        }
        int[] myMempid = new int[]{pid};
        Debug.MemoryInfo memoryInfo = am.getProcessMemoryInfo(myMempid)[0];
        // 首先用反射拿uss的值
        Class clazz = memoryInfo.getClass();
        try {
            Method getTotalUssMethod = clazz.getDeclaredMethod("getTotalUss", new Class[]{});
            getTotalUssMethod.setAccessible(true);
            Integer totalUss = (Integer) getTotalUssMethod.invoke(memoryInfo, new Object[]{});
            return totalUss;
        } catch (Exception e) {
            // 因为getTotalUss方法是hide的，也许部分手机反射会出问题，所以这里套一个public的获取
            // 经过测试pss 会比uss 高出几M到十M左右
            return memoryInfo.getTotalPss();
        }

    }
}
