package com.zk.qpm.floatview;

public class QPMFloatViewBean {

    public @QPMFloatViewType.Type String type;
    // 开关显示的名字
    public String switchName;
    // 开关的提示内容，不需要可为空
    public String switchTip;
    // 是否默认开启这个开关
    public boolean switchDefault;
    // 开关默认的排序
    public int switchSort;

    public QPMFloatViewBean(String type, String switchName, String switchTip, boolean switchDefault, int switchSort) {
        this.type = type;
        this.switchName = switchName;
        this.switchTip = switchTip;
        this.switchDefault = switchDefault;
        this.switchSort = switchSort;
    }
}
