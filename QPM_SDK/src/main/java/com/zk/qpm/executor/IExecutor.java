package com.zk.qpm.executor;

import android.content.Context;

import com.zk.qpm.QPMException;
import com.zk.qpm.floatview.QPMFloatViewType;

public interface IExecutor {

    @QPMFloatViewType.Type String type();

    void createShowView(Context context);

    void destoryShowView();

    void exec() throws QPMException;

    void reset();

    void stop();

}
