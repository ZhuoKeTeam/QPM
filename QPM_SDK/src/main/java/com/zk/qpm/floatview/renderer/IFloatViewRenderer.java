package com.zk.qpm.floatview.renderer;

import android.view.View;

public interface IFloatViewRenderer {

    String type();

    boolean isShow();

    View getView();

    void renderer();

}
