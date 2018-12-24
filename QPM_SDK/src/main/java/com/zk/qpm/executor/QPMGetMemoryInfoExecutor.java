package com.zk.qpm.executor;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;
import android.os.Process;

import com.zk.qpm.QPMException;
import com.zk.qpm.analysis.QPMMemoryAnalysis;
import com.zk.qpm.floatview.QPMFloatViewType;
import com.zk.qpm.floatview.renderer.IFloatViewRenderer;
import com.zk.qpm.floatview.renderer.QPMGetMemoryInfoRenderer;
import com.zk.qpm.manager.QPMFloatViewManager;

import java.lang.reflect.Method;

public class QPMGetMemoryInfoExecutor implements IExecutor {

    private QPMMemoryAnalysis memoryAnalysis;
    private ActivityManager am;

    public QPMGetMemoryInfoExecutor(ActivityManager am) {
        memoryAnalysis = new QPMMemoryAnalysis();
        this.am = am;
    }

    @Override
    public String type() {
        return QPMFloatViewType.TYPE_FLOAT_VIEW_MEMORY_VIEW;
    }

    @Override
    public void createShowView(Context context) {
        IFloatViewRenderer memoryBean = new QPMGetMemoryInfoRenderer();
        QPMFloatViewManager.getInstance().addItem(memoryBean);
    }

    @Override
    public void destoryShowView() {
        QPMFloatViewManager.getInstance().removeItem(type());
    }

    @Override
    public void exec() throws QPMException {
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
