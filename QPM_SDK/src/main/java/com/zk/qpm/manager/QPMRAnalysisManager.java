package com.zk.qpm.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.blankj.utilcode.util.ToastUtils;
import com.zk.qpm.QPMRAnalysisResult;
import com.zk.qpm.callback.IAnalysisCallback;

public class QPMRAnalysisManager {

    private Context applicationContext = null;
    private Handler mainThreadHandler = null;
    public String packageName = null;   //被测进程
    public int pid = -1;                //当前测试进程的pid
    private int lastPid = -1;

    /*  *
     * 数据结果和分析管理器
     */
    private QPMRAnalysisResult jmgtrAnalysisResult;
    private boolean initialized = false;

    private static QPMRAnalysisManager instance = null;

    private QPMRAnalysisManager() {
    }

    public static QPMRAnalysisManager getInstance() {
        if (instance == null) {
            synchronized (QPMRAnalysisManager.class) {
                QPMRAnalysisManager temp = instance;
                if (temp == null) {
                    temp = new QPMRAnalysisManager();
                    instance = temp;
                }
            }
        }
        return instance;
    }

    /**
     * 初始化数据
     */
    private void init() {
        jmgtrAnalysisResult = new QPMRAnalysisResult();
        initialized = true;
    }

    /**
     * 必须首先运行该方法
     *
     * @param context     上下文
     * @param pid         进程 pid
     * @param packageName 当前包名
     */
    public void start(Context context, int pid, String packageName) {
//        pid = Process.myPid();
//        packageName = context.getPackageName();

        //设置初始化数据分析器：
        applicationContext = (context != null)
                ? context.getApplicationContext()
                : QPMManager.getInstance().getContext();


        if (mainThreadHandler == null) {
            mainThreadHandler = new Handler(Looper.getMainLooper());
        }
        lastPid = pid;
        this.pid = pid;
        this.packageName = packageName;
        init();
    }

    public void stop(boolean stopApp) {
        packageName = null;
        pid = -1;
        init();
    }

    public void clear() {
        init();
    }

    @Deprecated
    public void analysis(final String packageName, final int pid, final String data) {
        if (!initialized || this.packageName == null || !this.packageName.equals(packageName)) {
            return;
        }

        if (pid == lastPid) {
            return;
        }

        QPMCallBackManager callBackManager = QPMCallBackManager.getInstance();

        if (pid == -1) {
            ToastUtils.showShort("被测应用已打开：pid=" + pid);
            this.pid = pid;
            jmgtrAnalysisResult.otherInfo.pId = pid;
            callBackManager.refreshInfo(IAnalysisCallback.TYPE_REFRESH_PID, jmgtrAnalysisResult);
        } else if (pid != jmgtrAnalysisResult.otherInfo.pId) {
            ToastUtils.showShort("被测应用已重启：pid=" + pid);
            this.pid = pid;
            init();
            jmgtrAnalysisResult.otherInfo.pId = pid;
            callBackManager.refreshInfo(IAnalysisCallback.TYPE_REFRESH_PID, jmgtrAnalysisResult);
        }

        if (jmgtrAnalysisResult.otherInfo.pId != -1) {
            jmgtrAnalysisResult.otherInfo.pId = pid;
        }
    }

    public QPMRAnalysisResult getJMGTRAnalysisResult() {
        if (jmgtrAnalysisResult == null) {
            throw new RuntimeException("You should execute JMGTRAnalysis.start() it first.");
        }

        return jmgtrAnalysisResult;
    }
}
