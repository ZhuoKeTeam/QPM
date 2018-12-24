package com.zk.qpm.plugin;

import android.os.Bundle;

/**
 * 每个插件都可以接收从被测应用传递过来的命令(任务)，
 * 具体命令的实现需要实现该接口，并注册到插件对应的PluginItem类
 */
public interface QPMPluginTaskExecutor {
    void execute(Bundle bundle);
}
