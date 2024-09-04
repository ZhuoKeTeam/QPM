package com.zk.qpm.function;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zk.qpm.R;
import com.zk.qpm.adapter.CommonRecyclerAdapter;
import com.zk.qpm.utils.FuzzyMatchUtil;
import com.zk.qpm.utils.ManifestParser;
import com.zk.qpm.utils.XMLParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class FourModuleFunction implements IFunction {

    protected Context mContext;

    private EditText queryView;
    private RecyclerView recyclerView;
    private CommonRecyclerAdapter<Item> adapter;
    private List<Item> allDatas;
    private LayoutInflater mInflater;

    public FourModuleFunction(Context mContext) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public void renderer(View layout) {
        queryView = layout.findViewById(R.id.et_query);
        recyclerView = layout.findViewById(R.id.rv_activitys);
        allDatas = getAllDatas();
        List<Item> datas = filter(null);
        adapter = new CommonRecyclerAdapter<Item>(mContext, recyclerView, datas, R.layout.jm_gt_item_four_module) {
            @Override
            protected void onBind(CommonRecyclerAdapter.CommonViewHolder viewHolder, final View itemView, final int position, int viewType, final Item data) {
                rendererModuleName(itemView, data);
                rendererIntentFilter(itemView, data);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClick(itemView, position, data);
                    }
                });
            }
        };
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter.setLayoutManager(layoutManager, 1, Color.parseColor("#E1E1E1"));
        recyclerView.setAdapter(adapter);
        queryView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                List<Item> datas = filter(s.toString());
                adapter.setDatas(datas);
            }
        });
    }

    private List<Item> filter(String key) {
        return FuzzyMatchUtil.match(allDatas, key, true, new FuzzyMatchUtil.IMatchResultAdapter<Item>() {
            @Override
            public String extractStringFromSource(Item item) {
                return item.xmlInfo.getAttributeMap().get("name");
            }

            @Override
            public void fillResultToSource(Item item, List<Integer> matchPosition) {
                item.matchPosition.clear();
                item.matchPosition.addAll(matchPosition);
            }
        });
    }

    private void rendererModuleName(View itemView, Item data) {
        TextView moduleNameView = itemView.findViewById(R.id.tv_module_name);
        if (data.matchPosition == null || data.matchPosition.isEmpty()) {
            moduleNameView.setText(data.xmlInfo.getAttributeMap().get("name"));
        } else {
            SpannableStringBuilder styled = new SpannableStringBuilder(data.xmlInfo.getAttributeMap().get("name"));
            for (Integer index : data.matchPosition) {
                styled.setSpan(new ForegroundColorSpan(Color.RED), index, index + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            moduleNameView.setText(styled);
        }
    }

    private void rendererIntentFilter(View itemView, Item data) {
        LinearLayout intentFilterLayout = itemView.findViewById(R.id.ll_module_intent_filter);
        LinearLayout actionLayout = itemView.findViewById(R.id.ll_module_action);
        LinearLayout dataLayout = itemView.findViewById(R.id.ll_module_data);
        LinearLayout categoryLayout = itemView.findViewById(R.id.ll_module_category);

        if (!data.xmlInfo.getSonTagMap().containsKey("intent-filter")) {
            intentFilterLayout.setVisibility(View.GONE);
        } else {
            List<XMLParser> intentFilters = data.xmlInfo.getSonTagMap().get("intent-filter");
            if (intentFilters.isEmpty()) {
                intentFilterLayout.setVisibility(View.GONE);
            } else {
                intentFilterLayout.setVisibility(View.VISIBLE);
                // 先移除之前添加过的
                actionLayout.removeAllViews();
                actionLayout.setVisibility(View.GONE);
                dataLayout.removeAllViews();
                dataLayout.setVisibility(View.GONE);
                categoryLayout.removeAllViews();
                categoryLayout.setVisibility(View.GONE);

                for (XMLParser intentFilter : intentFilters) {
                    rendererIntentFilterInner(actionLayout, intentFilter, "action");
                    rendererIntentFilterInner(dataLayout, intentFilter, "data");
                    rendererIntentFilterInner(categoryLayout, intentFilter, "category");
                }
            }
        }
    }

    private void rendererIntentFilterInner(LinearLayout layout, XMLParser intentFilter, String key) {
        if (intentFilter.getSonTagMap().containsKey(key)) {
            List<XMLParser> subParsers = intentFilter.getSonTagMap().get(key);
            if (!subParsers.isEmpty()) {
                layout.setVisibility(View.VISIBLE);
                for (XMLParser subParser : subParsers) {
                    Map<String, String> attributeMap = subParser.getAttributeMap();
                    for (Map.Entry<String, String> entry : attributeMap.entrySet()) {
                        View view = mInflater.inflate(R.layout.jm_gt_item_type_key_value, layout, false);
                        TextView typeView = view.findViewById(R.id.tv_type);
                        TextView keyView = view.findViewById(R.id.tv_key);
                        TextView valueView = view.findViewById(R.id.tv_value);
                        typeView.setText(subParser.getTagName());
                        keyView.setText(entry.getKey());
                        valueView.setText(entry.getValue());
                        layout.addView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    }
                }
            }
        }
    }

    private List<Item> getAllDatas() {
        List<Item> items = new ArrayList<>();
        List<XMLParser> activities = ManifestParser.getModule(mContext, moduleName());
        for (XMLParser activity : activities) {
            if (!activity.getAttributeMap().containsKey("name")) {
                continue;
            }
            Item item = new Item();
            item.xmlInfo = activity;
            items.add(item);
        }
        return items;
    }

    protected abstract String moduleName();

    protected abstract void onItemClick(View view, int position, Item data);

    public class Item {

        public XMLParser xmlInfo;
        public List<Integer> matchPosition = new ArrayList<>();

    }
}
