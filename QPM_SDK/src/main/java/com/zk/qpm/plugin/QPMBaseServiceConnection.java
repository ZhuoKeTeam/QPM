package com.zk.qpm.plugin;

public class QPMBaseServiceConnection {

    public Class<?> mClass;
    private String key;

    public QPMBaseServiceConnection(Class<?> claxx){
        this.mClass = claxx;
    }

    public QPMBaseServiceConnection(String key){
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }


}
