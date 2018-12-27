package com.zk.qpm.manager;

import android.os.Handler;
import android.text.TextUtils;

import com.zk.qpm.floatview.QPMFloatViewType;
import com.zk.qpm.floatview.renderer.BaseTemplateRenderer;
import com.zk.qpm.floatview.renderer.IFloatViewRenderer;
import com.zk.qpm.floatview.renderer.QPMTemplateBigTextRenderer;
import com.zk.qpm.floatview.renderer.QPMTemplateCustomRenderer;
import com.zk.qpm.floatview.renderer.QPMTemplateKeyPicRenderer;
import com.zk.qpm.floatview.renderer.QPMTemplateKeyValueRenderer;
import com.zk.qpm.floatview.renderer.QPMTemplatePicValueRenderer;
import com.zk.qpm.utils.PrefsParser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class QPMApiTemplateManager {

    private static volatile QPMApiTemplateManager instance;

    private Map<String, BaseTemplateRenderer> rendererMap;
    private Handler mHandler;

    private QPMApiTemplateManager() {
        rendererMap = new HashMap<>();
        mHandler = new Handler();
    }

    public static QPMApiTemplateManager getInstance() {
        if (instance == null) {
            synchronized (QPMApiTemplateManager.class) {
                if (instance == null) {
                    instance = new QPMApiTemplateManager();
                }
            }
        }
        return instance;
    }

    public void onSwitchChanged(Map<String, Boolean> switchMap) {
        if (switchMap == null) {
            return;
        }
        Boolean switchBigText = switchMap.remove(QPMFloatViewType.TYPE_FLOAT_VIEW_TEMPLATE_BIG_TEXT);
        if (!switchBigText) {
            removeTemplateRenderer(QPMFloatViewType.TYPE_FLOAT_VIEW_TEMPLATE_BIG_TEXT);
        }
        Boolean switchKeyValue = switchMap.remove(QPMFloatViewType.TYPE_FLOAT_VIEW_TEMPLATE_KEY_VALUE);
        if (!switchKeyValue) {
            removeTemplateRenderer(QPMFloatViewType.TYPE_FLOAT_VIEW_TEMPLATE_KEY_VALUE);
        }
        Boolean switchKeyPic = switchMap.remove(QPMFloatViewType.TYPE_FLOAT_VIEW_TEMPLATE_KEY_PIC);
        if (!switchKeyPic) {
            removeTemplateRenderer(QPMFloatViewType.TYPE_FLOAT_VIEW_TEMPLATE_KEY_PIC);
        }
        Boolean switchPicValue = switchMap.remove(QPMFloatViewType.TYPE_FLOAT_VIEW_TEMPLATE_PIC_VALUE);
        if (!switchPicValue) {
            removeTemplateRenderer(QPMFloatViewType.TYPE_FLOAT_VIEW_TEMPLATE_PIC_VALUE);
        }
        Boolean switchCustom = switchMap.remove(QPMFloatViewType.TYPE_FLOAT_VIEW_TEMPLATE_CUSTOM);
        if (!switchCustom) {
            removeTemplateRenderer(QPMFloatViewType.TYPE_FLOAT_VIEW_TEMPLATE_CUSTOM);
        }
    }

    public void addBigTextRenderer(String identity, String text) {
        if (TextUtils.isEmpty(identity) || TextUtils.isEmpty(text)) {
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put(QPMTemplateBigTextRenderer.PARAM_BIG_TEXT, text);
        addTemplateRenderer(identity, param, new QPMTemplateBigTextRenderer(param));
    }

    public void addKeyValueRenderer(String identity, String key, String value) {
        if (TextUtils.isEmpty(identity) || TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put(QPMTemplateKeyValueRenderer.PARAM_KEY, key);
        param.put(QPMTemplateKeyValueRenderer.PARAM_VALUE, value);
        addTemplateRenderer(identity, param, new QPMTemplateKeyValueRenderer(param));
    }

    public void addKeyPicRenderer(String identity, String key, int picId) {
        if (TextUtils.isEmpty(identity) || TextUtils.isEmpty(key)) {
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put(QPMTemplateKeyPicRenderer.PARAM_KEY, key);
        param.put(QPMTemplateKeyPicRenderer.PARAM_PIC_ID, picId);
        addTemplateRenderer(identity, param, new QPMTemplateKeyPicRenderer(param));
    }

    public void addPicValueRenderer(String identity, int picId, String value) {
        if (TextUtils.isEmpty(identity) || TextUtils.isEmpty(value)) {
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put(QPMTemplatePicValueRenderer.PARAM_PIC_ID, picId);
        param.put(QPMTemplatePicValueRenderer.PARAM_VALUE, value);
        addTemplateRenderer(identity, param, new QPMTemplatePicValueRenderer(param));
    }

    public void addCustomRenderer(String identity, QPMTemplateCustomRenderer renderer) {
        if (TextUtils.isEmpty(identity) || renderer == null) {
            return;
        }
        addTemplateRenderer(identity, renderer.getParam(), renderer);
    }

    private void addTemplateRenderer(String identity, Map<String, Object> param, BaseTemplateRenderer renderer) {
        if (renderer == null || TextUtils.isEmpty(renderer.type()) || param == null) {
            return;
        }
        if (!QPMFloatViewManager.getInstance().isFloatViewShow()) {
            return;
        }
        QPMSwitchManager switchManager = QPMSwitchManager.getInstance();
        List<PrefsParser.PrefItem> items = switchManager.getSwitchs();
        boolean switchOpen = switchManager.isSwitchOpen(items, renderer.type());
        if (!switchOpen) {
            return;
        }
        synchronized (QPMApiTemplateManager.class) {
            if (rendererMap.containsKey(identity)) {
                BaseTemplateRenderer existRenderer = rendererMap.get(identity);
                existRenderer.update(param);
            } else {
                rendererMap.put(identity, renderer);
                QPMFloatViewManager.getInstance().addItem(renderer);
            }
        }

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                QPMFloatViewManager.getInstance().refreshFloatView();
                QPMFloatViewManager.getInstance().rendererFloatViewBean();
            }
        });
    }

    private void removeTemplateRenderer(String type) {
        if (TextUtils.isEmpty(type)) {
            return;
        }
        synchronized (QPMApiTemplateManager.class) {
            Iterator<Map.Entry<String, BaseTemplateRenderer>> iterator = rendererMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, BaseTemplateRenderer> next = iterator.next();
                IFloatViewRenderer renderer = next.getValue();
                if (TextUtils.equals(type, renderer.type())) {
                    iterator.remove();
                }
            }
            QPMFloatViewManager.getInstance().removeItem(type);
        }
    }

    public void removeTemplateRendererByIdentity(String identity) {
        if (TextUtils.isEmpty(identity)) {
            return;
        }
        synchronized (QPMApiTemplateManager.class) {
            BaseTemplateRenderer renderer = rendererMap.remove(identity);
            if (renderer == null) {
                return;
            }
            QPMFloatViewManager.getInstance().removeItem(renderer);
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                QPMFloatViewManager.getInstance().refreshFloatView();
                QPMFloatViewManager.getInstance().rendererFloatViewBean();
            }
        });
    }

    public void clear() {
        synchronized (QPMApiTemplateManager.class) {
            rendererMap.clear();
        }
    }
}
