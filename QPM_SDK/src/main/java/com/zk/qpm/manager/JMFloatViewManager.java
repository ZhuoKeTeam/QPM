package com.jm.android.gt.manager;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import com.jm.android.gt.JMGTConstant;
import com.jm.android.gt.R;
import com.jm.android.gt.floatview.JMFloatViewType;
import com.jm.android.gt.floatview.renderer.IFloatViewRenderer;
import com.jm.android.gt.floatview.JMFloatViewBean;
import com.jm.android.gt.service.JMDaemonComponent;
import com.jm.android.gt.service.JMFPSComponent;
import com.jm.android.gt.service.JMFloatViewBaseComponent;
import com.jm.android.gt.service.JMFloatViewCustomComponent;
import com.jm.android.gt.service.JMFloatViewSimpleComponent;
import com.jm.android.gt.utils.FloatViewUtils;
import com.jm.android.gt.utils.PrefsParser;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JMFloatViewManager {

    private List<IFloatViewRenderer> floatViewBeans;

    private static JMFloatViewManager instance = null;

    private JMFloatViewBaseComponent floatViewBaseComponent;
    private JMDaemonComponent daemonComponent;
    private JMFPSComponent fpsComponent;

    private List<JMFloatViewBean> typeBeans = new ArrayList<>();

    private JMFloatViewManager() {
        floatViewBeans = new ArrayList<>();
        // 按照默认优先级添加
        Context context = JMGTManager.getInstance().getContext();
        typeBeans.clear();
        typeBeans.add(new JMFloatViewBean(JMFloatViewType.TYPE_FLOAT_VIEW_PACKAGE, context.getString(R.string.jm_gt_switch_package), null, false, 1));
        typeBeans.add(new JMFloatViewBean(JMFloatViewType.TYPE_FLOAT_VIEW_TOP_ACTIVITY, context.getString(R.string.jm_gt_switch_top_activity), null, true, 2));
        typeBeans.add(new JMFloatViewBean(JMFloatViewType.TYPE_FLOAT_VIEW_FPS_VIEW, context.getString(R.string.jm_gt_switch_fps), null, true, 3));
        typeBeans.add(new JMFloatViewBean(JMFloatViewType.TYPE_FLOAT_VIEW_THREAD_AUTO_ADD, context.getString(R.string.jm_gt_switch_auto_add), null, false, 4));
        typeBeans.add(new JMFloatViewBean(JMFloatViewType.TYPE_FLOAT_VIEW_CPU_VIEW, context.getString(R.string.jm_gt_switch_cpu), null, true, 5));
        typeBeans.add(new JMFloatViewBean(JMFloatViewType.TYPE_FLOAT_VIEW_MEMORY_VIEW, context.getString(R.string.jm_gt_switch_memory), null, true, 6));
        typeBeans.add(new JMFloatViewBean(JMFloatViewType.TYPE_FLOAT_VIEW_ACTIVITY_STACK, context.getString(R.string.jm_gt_switch_activity_stack), null, false, 7));
        typeBeans.add(new JMFloatViewBean(JMFloatViewType.TYPE_FLOAT_VIEW_THREAD_COUNT, context.getString(R.string.jm_gt_switch_thread_count), null, true, 8));
        typeBeans.add(new JMFloatViewBean(JMFloatViewType.TYPE_FLOAT_VIEW_FLOW_DATA, context.getString(R.string.jm_gt_switch_flow), null, false, 9));
        if (JMGTManager.getInstance().isImplementOkHttp()) {
            typeBeans.add(new JMFloatViewBean(JMFloatViewType.TYPE_FLOAT_VIEW_NETWORK_API, context.getString(R.string.jm_gt_switch_api), null, false, 10));
        }
        typeBeans.add(new JMFloatViewBean(JMFloatViewType.TYPE_FLOAT_VIEW_SCREEN_RECORDER, context.getString(R.string.jm_gt_switch_screen_recorder), getScreenRecorderPath(), false, 11));
        typeBeans.add(new JMFloatViewBean(JMFloatViewType.TYPE_FLOAT_VIEW_H5_MONITOR, context.getString(R.string.jm_gt_switch_h5_monitor), null, false, 12));
        typeBeans.add(new JMFloatViewBean(JMFloatViewType.TYPE_FLOAT_VIEW_TEMPLATE_BIG_TEXT, context.getString(R.string.jm_gt_switch_big_text), null, true, 13));
        typeBeans.add(new JMFloatViewBean(JMFloatViewType.TYPE_FLOAT_VIEW_TEMPLATE_KEY_VALUE, context.getString(R.string.jm_gt_switch_keyvalue), null, true, 14));
        typeBeans.add(new JMFloatViewBean(JMFloatViewType.TYPE_FLOAT_VIEW_TEMPLATE_KEY_PIC, context.getString(R.string.jm_gt_switch_key_pic), null, true, 15));
        typeBeans.add(new JMFloatViewBean(JMFloatViewType.TYPE_FLOAT_VIEW_TEMPLATE_PIC_VALUE, context.getString(R.string.jm_gt_switch_pic_value), null, true, 16));
        typeBeans.add(new JMFloatViewBean(JMFloatViewType.TYPE_FLOAT_VIEW_TEMPLATE_CUSTOM, context.getString(R.string.jm_gt_switch_custom), null, true, 17));
    }

    public static JMFloatViewManager getInstance() {
        if (instance == null) {
            synchronized (JMFloatViewManager.class) {
                JMFloatViewManager temp = instance;
                if (temp == null) {
                    temp = new JMFloatViewManager();
                    instance = temp;
                }
            }
        }
        return instance;
    }

    public List<JMFloatViewBean> getTypeBeans() {
        return typeBeans;
    }

    public List<IFloatViewRenderer> getItems() {
        // 先排序
        Collections.sort(floatViewBeans, new Comparator<IFloatViewRenderer>() {
            @Override
            public int compare(IFloatViewRenderer o1, IFloatViewRenderer o2) {
                return JMGTSortManager.getInstance().compare(o1.type(), o2.type());
            }
        });
        return floatViewBeans;
    }

    public List<IFloatViewRenderer> getItem(String type) {
        List<IFloatViewRenderer> renderers = new ArrayList<>();
        synchronized (instance) {
            for (IFloatViewRenderer renderer : floatViewBeans) {
                if (TextUtils.equals(type, renderer.type())) {
                    renderers.add(renderer);
                }
            }
        }
        return renderers;
    }


    public void addItem(IFloatViewRenderer item) {
        synchronized (instance) {
            floatViewBeans.add(item);
        }
    }

    public boolean existItem(IFloatViewRenderer item) {
        if (item == null || floatViewBeans == null || floatViewBeans.isEmpty()) {
            return false;
        }
        for (IFloatViewRenderer bean : floatViewBeans) {
            if (TextUtils.equals(bean.type(), item.type())) {
                return true;
            }
        }
        return false;
    }

    public void removeItem(String type) {
        synchronized (instance) {
            Iterator<IFloatViewRenderer> iterator = floatViewBeans.iterator();
            while (iterator.hasNext()) {
                IFloatViewRenderer renderer = iterator.next();
                if (TextUtils.equals(type, renderer.type())) {
                    iterator.remove();
                }
            }
        }
    }

    public void removeItem(IFloatViewRenderer renderer) {
        synchronized (instance) {
            floatViewBeans.remove(renderer);
        }
    }

    public void clearItem() {
        synchronized (instance) {
            floatViewBeans.clear();
        }
    }

    public boolean isFloatViewShow() {
        if (floatViewBaseComponent == null) {
            return false;
        }
        return floatViewBaseComponent.isShow();
    }

    public void floatViewHide() {
        clearItem();
        JMGTApiTemplateManager.getInstance().clear();
        toggleFloatViewComponent(false);
        toggleFPSComponent(false);
        toggleDaemonComponent(false);
        // 清空线程管理器，避免内存泄露
        JMThreadManager.getInstance().clearAllThread();
        // 强制关闭某些操作，比如正在进行的屏幕录制操作
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && JMScreenRecorderManager.getInstance().isStart()) {
            JMScreenRecorderManager.getInstance().stopRecorder();
        }
    }

    public boolean floatViewShow() {
        boolean hasPermission = FloatViewUtils.checkFloatWindowPermission();
        if (hasPermission) {
            toggleFloatViewComponent(true);
            refreshFloatViewAndComponent(false);
            return true;
        } else {
            FloatViewUtils.applyCommonPermission(JMGTManager.getInstance().getContext());
            return false;
        }
    }

    public void toggleFloatViewComponent(boolean show) {
        if (floatViewBaseComponent != null) {
            JMPluginManager.getInstance().getPluginControler().stopService(floatViewBaseComponent);
            floatViewBaseComponent = null;
        }
        if (show) {
            if (JMGTModeManager.getInstance().isSimpleMode()) {
                floatViewBaseComponent = new JMFloatViewSimpleComponent();
            } else {
                floatViewBaseComponent = new JMFloatViewCustomComponent();
            }
            JMPluginManager.getInstance().getPluginControler().startService(floatViewBaseComponent);
        }
    }

    public void toggleFPSComponent(boolean show) {
        if (show) {
            if (fpsComponent == null) {
                fpsComponent = new JMFPSComponent();
                JMPluginManager.getInstance().getPluginControler().startService(fpsComponent);
            }
        } else {
            if (fpsComponent != null) {
                JMPluginManager.getInstance().getPluginControler().stopService(fpsComponent);
                fpsComponent = null;
            }
        }
    }

    public void toggleDaemonComponent(boolean show) {
        if (show) {
            if (daemonComponent == null) {
                daemonComponent = new JMDaemonComponent();
                JMPluginManager.getInstance().getPluginControler().startService(daemonComponent);
            }
        } else {
            if (daemonComponent != null) {
                JMPluginManager.getInstance().getPluginControler().stopService(daemonComponent);
                daemonComponent = null;
            }
        }
    }

    public void refreshFloatViewAndComponent(boolean hideIfSwitchClose) {
        if (floatViewBaseComponent == null || !floatViewBaseComponent.isShow()) {
            return;
        }
        JMGTSwitchManager switchManager = JMGTSwitchManager.getInstance();
        List<PrefsParser.PrefItem> items = switchManager.getSwitchs();
        Map<String, Boolean> switchMap = new HashMap<>();
        for (JMFloatViewBean typeBean : typeBeans) {
            String type = typeBean.type;
            switchMap.put(type, switchManager.isSwitchOpen(items, type));
        }

        // 特殊处理FPS开关
        Boolean switchFPS = switchMap.remove(JMFloatViewType.TYPE_FLOAT_VIEW_FPS_VIEW);
        if (switchFPS) {
            toggleFPSComponent(true);
        } else if (hideIfSwitchClose) {
            toggleFPSComponent(false);
        }

        // 特殊处理API模板开关
        JMGTApiTemplateManager.getInstance().onSwitchChanged(switchMap);

        // 剩下的开关共同控制后台组件
        boolean switchDaemon = false;
        for (Boolean switchItem : switchMap.values()) {
            if (switchItem) {
                switchDaemon = true;
                break;
            }
        }
        if (switchDaemon) {
            toggleDaemonComponent(true);
        } else if (hideIfSwitchClose) {
            toggleDaemonComponent(false);
        }

        refreshFloatView();
    }

    public void refreshFloatView() {
        if (floatViewBaseComponent == null) {
            return;
        }
        floatViewBaseComponent.refreshContainerLayout();
    }

    public void rendererFloatViewBean() {
        List<IFloatViewRenderer> floatViewBeans = JMFloatViewManager.getInstance().getItems();
        for (IFloatViewRenderer floatViewBean : floatViewBeans) {
            if (floatViewBean != null && floatViewBean.isShow()) {
                floatViewBean.renderer();
            }
        }
    }

    private String getScreenRecorderPath() {
        File dir = new File(Environment.getExternalStorageDirectory(), JMGTConstant.PATH_SCREEN_RECORDER);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath();
    }

}
