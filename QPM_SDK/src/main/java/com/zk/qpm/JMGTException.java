package com.jm.android.gt;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class JMGTException extends Exception {

    public @ExceptionType int type;

    public JMGTException(@ExceptionType int type, String message) {
        super(message);
        this.type = type;
    }

    @IntDef({ExceptionType.TYPE_STOP_THREAD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ExceptionType {
        int TYPE_STOP_THREAD = 1;
    }
}
