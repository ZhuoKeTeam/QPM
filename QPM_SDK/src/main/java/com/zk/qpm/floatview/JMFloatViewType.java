package com.jm.android.gt.floatview;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface JMFloatViewType {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({TYPE_FLOAT_VIEW_PACKAGE,
            TYPE_FLOAT_VIEW_TOP_ACTIVITY,
            TYPE_FLOAT_VIEW_FPS_VIEW,
            TYPE_FLOAT_VIEW_THREAD_AUTO_ADD,
            TYPE_FLOAT_VIEW_CPU_VIEW,
            TYPE_FLOAT_VIEW_MEMORY_VIEW,
            TYPE_FLOAT_VIEW_ACTIVITY_STACK,
            TYPE_FLOAT_VIEW_THREAD_COUNT,
            TYPE_FLOAT_VIEW_FLOW_DATA,
            TYPE_FLOAT_VIEW_NETWORK_API,
            TYPE_FLOAT_VIEW_SCREEN_RECORDER,
            TYPE_FLOAT_VIEW_H5_MONITOR,
            TYPE_FLOAT_VIEW_TEMPLATE_BIG_TEXT,
            TYPE_FLOAT_VIEW_TEMPLATE_KEY_VALUE,
            TYPE_FLOAT_VIEW_TEMPLATE_KEY_PIC,
            TYPE_FLOAT_VIEW_TEMPLATE_PIC_VALUE,
            TYPE_FLOAT_VIEW_TEMPLATE_CUSTOM
    })
    @interface Type {
        String key() default TYPE_FLOAT_VIEW_PACKAGE;
    }


    String TYPE_FLOAT_VIEW_PACKAGE = "type_float_view_package";
    String TYPE_FLOAT_VIEW_TOP_ACTIVITY = "type_float_view_top_activity";
    String TYPE_FLOAT_VIEW_FPS_VIEW = "type_float_view_fps_view";
    String TYPE_FLOAT_VIEW_THREAD_AUTO_ADD = "type_float_view_thread_auto_add";
    String TYPE_FLOAT_VIEW_CPU_VIEW = "type_float_view_cpu_view";
    String TYPE_FLOAT_VIEW_MEMORY_VIEW = "type_float_view_memory_view";
    String TYPE_FLOAT_VIEW_ACTIVITY_STACK = "type_float_view_activity_stack";
    String TYPE_FLOAT_VIEW_THREAD_COUNT = "type_float_view_thread_count";
    String TYPE_FLOAT_VIEW_FLOW_DATA = "type_float_view_flow_data";
    String TYPE_FLOAT_VIEW_NETWORK_API = "type_float_view_network_api";
    String TYPE_FLOAT_VIEW_SCREEN_RECORDER = "type_float_view_screen_recorder";
    String TYPE_FLOAT_VIEW_H5_MONITOR = "type_float_view_h5_monitor";
    String TYPE_FLOAT_VIEW_TEMPLATE_BIG_TEXT = "type_float_view_template_big_text";
    String TYPE_FLOAT_VIEW_TEMPLATE_KEY_VALUE = "type_float_view_template_key_value";
    String TYPE_FLOAT_VIEW_TEMPLATE_KEY_PIC = "type_float_view_template_key_pic";
    String TYPE_FLOAT_VIEW_TEMPLATE_PIC_VALUE = "type_float_view_template_pic_value";
    String TYPE_FLOAT_VIEW_TEMPLATE_CUSTOM = "type_float_view_template_custom";

}
