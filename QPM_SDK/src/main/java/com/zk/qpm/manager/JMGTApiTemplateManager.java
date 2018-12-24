package com.jm.android.gt.manager;

import android.os.Handler;
import android.text.TextUtils;

import com.jm.android.gt.floatview.JMFloatViewType;
import com.jm.android.gt.floatview.renderer.BaseTemplateRenderer;
import com.jm.android.gt.floatview.renderer.IFloatViewRenderer;
import com.jm.android.gt.floatview.renderer.JMTemplateBigTextRenderer;
import com.jm.android.gt.floatview.renderer.JMTemplateCustomRenderer;
import com.jm.android.gt.floatview.renderer.JMTemplateKeyPicRenderer;
import com.jm.android.gt.floatview.renderer.JMTemplateKeyValueRenderer;
import com.jm.android.gt.floatview.renderer.JMTemplatePicValueRenderer;
import com.jm.android.gt.utils.PrefsParser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JMGTApiTemplateManager {

    private static volatile JMGTApiTemplateManager instance;

    private Map<String, BaseTemplateRenderer> rendererMap;
    private Handler mHandler;

    private JMGTApiTemplateManager() {
        rendererMap = new HashMap<>();
        mHandler = new Handler();
    }

    public static JMGTApiTemplateManager getInstance() {
        if (instance == null) {
            synchronized (JMGTApiTemplateManager.class) {
                if (instance == null) {
                    instance = new JMGTApiTemplateManager();
                }
            }
        }
        return instance;
    }

    public void onSwitchChanged(Map<String, Boolean> switchMap) {
        if (switchMap == null) {
            return;
        }
        Boolean switchBigText = switchMap.remove(JMFloatViewType.TYPE_FLOAT_VIEW_TEMPLATE_BIG_TEXT);
        if (!switchBigText) {
            removeTemplateRenderer(JMFloatViewType.TYPE_FLOAT_VIEW_TEMPLATE_BIG_TEXT);
        }
        Boolean switchKeyValue = switchMap.remove(JMFloatViewType.TYPE_FLOAT_VIEW_TEMPLATE_KEY_VALUE);
        if (!switchKeyValue) {
            removeTemplateRenderer(JMFloatViewType.TYPE_FLOAT_VIEW_TEMPLATE_KEY_VALUE);
        }
        Boolean switchKeyPic = switchMap.remove(JMFloatViewType.TYPE_FLOAT_VIEW_TEMPLATE_KEY_PIC);
        if (!switchKeyPic) {
            removeTemplateRenderer(JMFloatViewType.TYPE_FLOAT_VIEW_TEMPLATE_KEY_PIC);
        }
        Boolean switchPicValue = switchMap.remove(JMFloatViewType.TYPE_FLOAT_VIEW_TEMPLATE_PIC_VALUE);
        if (!switchPicValue) {
            removeTemplateRenderer(JMFloatViewType.TYPE_FLOAT_VIEW_TEMPLATE_PIC_VALUE);
        }
        Boolean switchCustom = switchMap.remove(JMFloatViewType.TYPE_FLOAT_VIEW_TEMPLATE_CUSTOM);
        if (!switchCustom) {
            removeTemplateRenderer(JMFloatViewType.TYPE_FLOAT_VIEW_TEMPLATE_CUSTOM);
        }
    }

    public void addBigTextRenderer(String identity, String text) {
        if (TextUtils.isEmpty(identity) || TextUtils.isEmpty(text)) {
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put(JMTemplateBigTextRenderer.PARAM_BIG_TEXT, text);
        addTemplateRenderer(identity, param, new JMTemplateBigTextRenderer(param));
    }

    public void addKeyValueRenderer(String identity, String key, String value) {
        if (TextUtils.isEmpty(identity) || TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put(JMTemplateKeyValueRenderer.PARAM_KEY, key);
        param.put(JMTemplateKeyValueRenderer.PARAM_VALUE, value);
        addTemplateRenderer(identity, param, new JMTemplateKeyValueRenderer(param));
    }

    public void addKeyPicRenderer(String identity, String key, int picId) {
        if (TextUtils.isEmpty(identity) || TextUtils.isEmpty(key)) {
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put(JMTemplateKeyPicRenderer.PARAM_KEY, key);
        param.put(JMTemplateKeyPicRenderer.PARAM_PIC_ID, picId);
        addTemplateRenderer(identity, param, new JMTemplateKeyPicRenderer(param));
    }

    public void addKeyPicRenderer(String identity, String key, String picUrl) {
        if (TextUtils.isEmpty(identity) || TextUtils.isEmpty(key) || TextUtils.isEmpty(picUrl)) {
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put(JMTemplateKeyPicRenderer.PARAM_KEY, key);
        param.put(JMTemplateKeyPicRenderer.PARAM_PIC_URL, picUrl);
        addTemplateRenderer(identity, param, new JMTemplateKeyPicRenderer(param));
    }

    public void addPicValueRenderer(String identity, int picId, String value) {
        if (TextUtils.isEmpty(identity) || TextUtils.isEmpty(value)) {
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put(JMTemplatePicValueRenderer.PARAM_PIC_ID, picId);
        param.put(JMTemplatePicValueRenderer.PARAM_VALUE, value);
        addTemplateRenderer(identity, param, new JMTemplatePicValueRenderer(param));
    }

    public void addPicValueRenderer(String identity, String picUrl, String value) {
        if (TextUtils.isEmpty(identity) || TextUtils.isEmpty(picUrl) || TextUtils.isEmpty(value)) {
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put(JMTemplatePicValueRenderer.PARAM_PIC_URL, picUrl);
        param.put(JMTemplatePicValueRenderer.PARAM_VALUE, value);
        addTemplateRenderer(identity, param, new JMTemplatePicValueRenderer(param));
    }

    public void addCustomRenderer(String identity, JMTemplateCustomRenderer renderer) {
        if (TextUtils.isEmpty(identity) || renderer == null) {
            return;
        }
        addTemplateRenderer(identity, renderer.getParam(), renderer);
    }

    private void addTemplateRenderer(String identity, Map<String, Object> param, BaseTemplateRenderer renderer) {
        if (renderer == null || TextUtils.isEmpty(renderer.type()) || param == null) {
            return;
        }
        if (!JMFloatViewManager.getInstance().isFloatViewShow()) {
            return;
        }
        JMGTSwitchManager switchManager = JMGTSwitchManager.getInstance();
        List<PrefsParser.PrefItem> items = switchManager.getSwitchs();
        boolean switchOpen = switchManager.isSwitchOpen(items, renderer.type());
        if (!switchOpen) {
            return;
        }
        synchronized (JMGTApiTemplateManager.class) {
            if (rendererMap.containsKey(identity)) {
                BaseTemplateRenderer existRenderer = rendererMap.get(identity);
                existRenderer.update(param);
            } else {
                rendererMap.put(identity, renderer);
                JMFloatViewManager.getInstance().addItem(renderer);
            }
        }

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                JMFloatViewManager.getInstance().refreshFloatView();
                JMFloatViewManager.getInstance().rendererFloatViewBean();
            }
        });
    }

    private void removeTemplateRenderer(String type) {
        if (TextUtils.isEmpty(type)) {
            return;
        }
        synchronized (JMGTApiTemplateManager.class) {
            Iterator<Map.Entry<String, BaseTemplateRenderer>> iterator = rendererMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, BaseTemplateRenderer> next = iterator.next();
                IFloatViewRenderer renderer = next.getValue();
                if (TextUtils.equals(type, renderer.type())) {
                    iterator.remove();
                }
            }
            JMFloatViewManager.getInstance().removeItem(type);
        }
    }

    public void removeTemplateRendererByIdentity(String identity) {
        if (TextUtils.isEmpty(identity)) {
            return;
        }
        synchronized (JMGTApiTemplateManager.class) {
            BaseTemplateRenderer renderer = rendererMap.remove(identity);
            if (renderer == null) {
                return;
            }
            JMFloatViewManager.getInstance().removeItem(renderer);
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                JMFloatViewManager.getInstance().refreshFloatView();
                JMFloatViewManager.getInstance().rendererFloatViewBean();
            }
        });
    }

    public void clear() {
        synchronized (JMGTApiTemplateManager.class) {
            rendererMap.clear();
        }
    }
}
