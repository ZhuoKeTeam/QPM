package com.zk.qpm.callback;

import android.support.annotation.StringDef;

import com.zk.qpm.QPMRAnalysisResult;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 回调方法有可能来自子线程，如果在主界面要修改 UI，请使用 handler。
 */
public interface IAnalysisCallback {
    @Documented
    @StringDef({TYPE_REFRESH_PID,
            TYPE_REFRESH_APP_INFO,
            TYPE_REFRESH_DEVICE_INFO,
            TYPE_REFRESH_AUTO_ADD_INFO,
            TYPE_REFRESH_TOP_ACTIVITY,
            TYPE_REFRESH_FPS_INFO,
            TYPE_REFRESH_BIG_BLOCK_INFO,
            TYPE_REFRESH_LOW_SM_INFO,
            TYPE_REFRESH_CPU_INFO,
            TYPE_REFRESH_MEMORY_INFO,
            TYPE_REFRESH_FLOW_INFO,
            TYPE_REFRESH_THREAD_INFO,
            TYPE_REFRESH_NETWORK_INFO,
            TYPE_REFRESH_ACTIVITY_STACK_INFO,
            TYPE_REFRESH_SCREEN_RECORDER_STATUS_INFO,
            TYPE_REFRESH_H5_MONITOR_INFO
    })
    @Target({
            ElementType.PARAMETER,
            ElementType.FIELD,
            ElementType.METHOD,
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface CallbackType {
        String key() default TYPE_REFRESH_PID;
    }

    String TYPE_REFRESH_PID = "type_refresh_pid";
    String TYPE_REFRESH_APP_INFO = "type_refresh_app_info";
    String TYPE_REFRESH_DEVICE_INFO = "type_refresh_device_info";
    String TYPE_REFRESH_AUTO_ADD_INFO = "type_refresh_test_info";
    String TYPE_REFRESH_TOP_ACTIVITY = "type_refresh_top_activity_info";
    String TYPE_REFRESH_FPS_INFO = "type_refresh_fps_info";
    String TYPE_REFRESH_BIG_BLOCK_INFO = "type_refresh_big_block_info";
    String TYPE_REFRESH_LOW_SM_INFO = "type_refresh_low_sm_info";
    String TYPE_REFRESH_CPU_INFO = "type_refresh_cpu_info";
    String TYPE_REFRESH_MEMORY_INFO = "type_refresh_memory_info";
    String TYPE_REFRESH_FLOW_INFO = "type_refresh_flow_info";
    String TYPE_REFRESH_THREAD_INFO = "type_refresh_thread_info";
    String TYPE_REFRESH_NETWORK_INFO = "type_refresh_network_info";
    String TYPE_REFRESH_ACTIVITY_STACK_INFO = "type_refresh_activity_stack_info";
    String TYPE_REFRESH_SCREEN_RECORDER_STATUS_INFO = "type_refresh_screen_recorder_status_info";
    String TYPE_REFRESH_H5_MONITOR_INFO = "type_refresh_h5_monitor_info";

    void refreshInfo(@CallbackType String type, QPMRAnalysisResult analysisResult);
}
