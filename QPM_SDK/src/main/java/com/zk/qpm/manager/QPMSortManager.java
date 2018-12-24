package com.zk.qpm.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.zk.qpm.floatview.QPMFloatViewBean;
import com.zk.qpm.floatview.QPMFloatViewType;
import com.zk.qpm.utils.PrefsParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QPMSortManager {

    private static final String SP_NAME_SORT = "jmgt_sort";

    private Map<String, Integer> sorts = new HashMap<>();

    private static volatile QPMSortManager instance;

    private QPMSortManager() {
    }

    public static QPMSortManager getInstance() {
        if (instance == null) {
            synchronized (QPMSortManager.class) {
                if (instance == null) {
                    instance = new QPMSortManager();
                }
            }
        }
        return instance;
    }

    public void reSort(@QPMFloatViewType.Type String type, int sort) {
        sorts.put(type, sort);
        writeSortSP();
    }

    public int compare(@QPMFloatViewType.Type String type1,
                              @QPMFloatViewType.Type String type2) {
        Integer left = sorts.get(type1);
        Integer right = sorts.get(type2);
        if (left == null && right == null) {
            return 0;
        } else if (left == null && right != null) {
            return -1;
        } else if (left != null && right == null) {
            return 1;
        } else {
            return left - right;
        }
    }

    public void init() {
        List<PrefsParser.PrefItem> prefs = PrefsParser.getPrefs(QPMManager.getInstance().getContext(), SP_NAME_SORT, PrefsParser.PARSER_SP);
        List<QPMFloatViewBean> typeBeans = QPMFloatViewManager.getInstance().getTypeBeans();
        // 如果内存中的值和SP存储的值的数量不一致，则直接重置SP
        if (prefs.size() != typeBeans.size()) {
            deleteSortSP();
            prefs.clear();
            createSortSP(typeBeans);
            return;
        }
        loadFromSortSP(prefs);
    }

    private void deleteSortSP() {
        SharedPreferences preferences = QPMManager.getInstance().getContext().getSharedPreferences(SP_NAME_SORT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    private void createSortSP(List<QPMFloatViewBean> typeBeans) {
        sorts.clear();
        for (QPMFloatViewBean typeBean : typeBeans) {
            sorts.put(typeBean.type, typeBean.switchSort);
            PrefsParser.PrefItem item = new PrefsParser.PrefItem(PrefsParser.IPrefTypeDefine.TYPE_INT, typeBean.type, String.valueOf(typeBean.switchSort));
            PrefsParser.writePrefs(QPMManager.getInstance().getContext(), SP_NAME_SORT, item);
        }
    }

    private void loadFromSortSP(List<PrefsParser.PrefItem> prefs) {
        sorts.clear();
        for (PrefsParser.PrefItem item : prefs) {
            sorts.put(item.key, Integer.parseInt(item.value));
        }
    }

    private void writeSortSP() {
        for (Map.Entry<String, Integer> entry : sorts.entrySet()) {
            PrefsParser.PrefItem item = new PrefsParser.PrefItem(PrefsParser.IPrefTypeDefine.TYPE_INT, entry.getKey(), String.valueOf(entry.getValue()));
            PrefsParser.writePrefs(QPMManager.getInstance().getContext(), SP_NAME_SORT, item);
        }
    }
}
