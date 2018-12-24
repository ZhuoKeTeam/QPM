package com.jm.android.gt.executor;

import android.content.Context;

import com.jm.android.gt.JMGTException;
import com.jm.android.gt.floatview.JMFloatViewType;

public interface IExecutor {

    @JMFloatViewType.Type String type();

    void createShowView(Context context);

    void destoryShowView();

    void exec() throws JMGTException;

    void reset();

    void stop();

}
