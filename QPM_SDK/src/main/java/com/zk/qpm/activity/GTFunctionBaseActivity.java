package com.jm.android.gt.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;

import com.jm.android.gt.function.IFunction;
import com.jm.android.gt.utils.StatusBarImmersiveUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class GTFunctionBaseActivity extends Activity{

    protected List<IFunction> functions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        StatusBarImmersiveUtils.setLightIcon(this);
        setContentView(contentViewId());
        initAllFunction();
        inflateView();
    }

    protected abstract int contentViewId();

    protected abstract void initAllFunction();

    private void inflateView() {
        for (IFunction function : functions) {
            ViewStub viewStub = findViewById(function.viewStubId());
            viewStub.setOnInflateListener(function.getCallback());
            function.renderer(viewStub.inflate());
        }
    }

}
