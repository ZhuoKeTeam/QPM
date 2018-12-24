package com.jm.android.gt.plugin;

public class JMBaseServiceConnection {

    public Class<?> mClass;
    private String key;

    public JMBaseServiceConnection(Class<?> claxx){
        this.mClass = claxx;
    }

    public JMBaseServiceConnection(String key){
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }


}
