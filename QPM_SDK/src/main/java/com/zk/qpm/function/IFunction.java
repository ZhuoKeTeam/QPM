package com.jm.android.gt.function;

import android.view.View;
import android.view.ViewStub;

public interface IFunction {

    int viewStubId();

    void renderer(View layout);

    ViewStub.OnInflateListener getCallback();

}
