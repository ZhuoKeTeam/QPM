package com.zk.qpm.function;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.zk.qpm.R;
import com.zk.qpm.activity.QPMActivityListActivity;
import com.zk.qpm.activity.QPMProviderListActivity;
import com.zk.qpm.activity.QPMReceiverListActivity;
import com.zk.qpm.activity.QPMServiceListActivity;
import com.zk.qpm.adapter.CommonRecyclerAdapter;
import com.zk.qpm.utils.ManifestParser;
import com.zk.qpm.utils.XMLParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ManifestFunction implements IFunction {

    private Context mContext;
    private RecyclerView mRecyclerView;
    private CommonRecyclerAdapter<Item> mAdapter;
    private List<Item> mItems = new ArrayList<>();

    public ManifestFunction(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int viewStubId() {
        return R.id.vs_manifest;
    }

    @Override
    public void renderer(View layout) {
        mRecyclerView = layout.findViewById(R.id.rv_manifest);
        mAdapter = new CommonRecyclerAdapter<Item>(mContext, mRecyclerView, mItems, new MultiType()) {

            @Override
            protected void onBind(CommonViewHolder viewHolder, View itemView, int position, int viewType, Item data) {
                if (MultiType.TYPE_TITLE == viewType) {
                    rendererTitleItem(itemView, position, data);
                } else if (MultiType.TYPE_CONTENT == viewType) {
                    rendererContentItem(itemView, position, data);
                } else if (MultiType.TYPE_JUMP == viewType) {
                    rendererJumpItem(itemView, position, data);
                }
            }
        };
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mAdapter.setLayoutManager(layoutManager, 1, Color.parseColor("#DADADA"));
        mRecyclerView.setAdapter(mAdapter);
        buildData();
        mAdapter.setDatas(mItems);
    }

    private void rendererTitleItem(View itemView, int position, Item data) {
        itemView.setOnClickListener(null);
        View dividerView = itemView.findViewById(R.id.v_divider);
        TextView titleView = itemView.findViewById(R.id.tv_title);
        if (position == 0 && data.type == MultiType.TYPE_TITLE) {
            dividerView.setVisibility(View.GONE);
        } else {
            dividerView.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(data.value)) {
            titleView.setText(data.value);
        }
    }

    private void rendererContentItem(View itemView, int position, Item data) {
        itemView.setOnClickListener(null);
        TextView keyView = itemView.findViewById(R.id.tv_key);
        TextView valueView = itemView.findViewById(R.id.tv_value);
        if (!TextUtils.isEmpty(data.key)) {
            keyView.setText(data.key);
        }
        if (!TextUtils.isEmpty(data.value)) {
            valueView.setText(data.value);
        }
    }

    private void rendererJumpItem(View itemView, int position, final Item data) {
        TextView hintView = itemView.findViewById(R.id.tv_hint);
        TextView countView = itemView.findViewById(R.id.tv_count);
        ImageView arrowView = itemView.findViewById(R.id.iv_arrow);
        if (!TextUtils.isEmpty(data.value)) {
            hintView.setText(data.value);
        }
        countView.setText(String.valueOf(data.count));
        arrowView.setVisibility(data.count == 0 ? View.INVISIBLE : View.VISIBLE);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.count > 0 && data.jumpClazz != null) {
                    mContext.startActivity(new Intent(mContext, data.jumpClazz));
                }
            }
        });
    }

    @Override
    public ViewStub.OnInflateListener getCallback() {
        return null;
    }

    public class Item {

        public int type;
        public String key;
        public String value;
        public Class jumpClazz;
        public int count;

        public Item(String value) {
            this.type = MultiType.TYPE_TITLE;
            this.value = value;
        }

        public Item(String key, String value) {
            this.type = MultiType.TYPE_CONTENT;
            this.key = key;
            this.value = value;
        }

        public Item(String value, int count, Class jumpClazz) {
            this.type = MultiType.TYPE_JUMP;
            this.value = value;
            this.count = count;
            this.jumpClazz = jumpClazz;
        }
    }

    public class MultiType implements CommonRecyclerAdapter.IMultiType<Item> {

        public static final int TYPE_TITLE = 0;
        public static final int TYPE_CONTENT = 1;
        public static final int TYPE_JUMP = 2;

        @Override
        public int getLayoutId(int itemType) {
            if (TYPE_TITLE == itemType) {
                return R.layout.jm_gt_item_title;
            } else if (TYPE_CONTENT == itemType) {
                return R.layout.jm_gt_item_key_value;
            } else if (TYPE_JUMP == itemType) {
                return R.layout.jm_gt_item_manifest_jump;
            }
            return R.layout.jm_gt_item_title;
        }

        @Override
        public int getItemType(int position, Item item) {
            return item.type;
        }
    }

    private void buildData() {
        mItems.clear();
        XMLParser manifestInfo = ManifestParser.parseManifestInfoByRecursion(mContext);
        parseXmlParser2Item(manifestInfo);
        // 组装可以跳转的选项
        if (!manifestInfo.getSonTagMap().containsKey("application")) {
            return;
        }
        List<XMLParser> applications = manifestInfo.getSonTagMap().get("application");
        if (applications.size() != 1) {
            return;
        }
        Item jumpTitle = new Item(mContext.getString(R.string.jm_gt_four_module));
        mItems.add(jumpTitle);
        XMLParser application = applications.get(0);
        // 添加Activity
        addFourModuleItem(application, "activity", R.string.jm_gt_register_activity, QPMActivityListActivity.class);
        // 添加service
        addFourModuleItem(application, "service", R.string.jm_gt_register_service, QPMServiceListActivity.class);
        // 添加Receiver
        addFourModuleItem(application, "receiver", R.string.jm_gt_register_receiver, QPMReceiverListActivity.class);
        // 添加Provider
        addFourModuleItem(application, "provider", R.string.jm_gt_register_provider, QPMProviderListActivity.class);
    }

    private void addFourModuleItem(XMLParser application, String moduleName, int strId, Class activityClazz) {
        if (application.getSonTagMap().containsKey(moduleName)) {
            List<XMLParser> modules = application.getSonTagMap().get(moduleName);
            int count = 0;
            for (XMLParser module : modules) {
                if (module.getAttributeMap().containsKey("name")) {
                    count++;
                }
            }
            Item item = new Item(mContext.getString(strId), count, activityClazz);
            mItems.add(item);
        } else {
            Item item = new Item(mContext.getString(strId), 0, activityClazz);
            mItems.add(item);
        }
    }

    private void parseXmlParser2Item(XMLParser parser) {
        // 在四大组件处截断，后续的activity、service等在二级页面展示
        if (TextUtils.equals("activity", parser.getTagName())
                || TextUtils.equals("service", parser.getTagName())
                || TextUtils.equals("receiver", parser.getTagName())
                || TextUtils.equals("provider", parser.getTagName())) {
            return;
        }
        // 如果有属性，才把自己加进去
        if (!parser.getAttributeMap().isEmpty()) {
            // 不存在这个tag，则先创建title
            if (!existTitle(parser.getTagName())) {
                Item title = new Item(parser.getTagName());
                mItems.add(title);
            }
            for (Map.Entry<String, String> entry : parser.getAttributeMap().entrySet()) {
                Item item = new Item(entry.getKey(), entry.getValue());
                mItems.add(item);
            }
        }
        if (!parser.getSonTagMap().isEmpty()) {
            for (List<XMLParser> sonParsers : parser.getSonTagMap().values()) {
                for (XMLParser sonParser : sonParsers) {
                    parseXmlParser2Item(sonParser);
                }
            }
        }
    }

    private boolean existTitle(String title) {
        for (Item item : mItems) {
            if (item.type == MultiType.TYPE_TITLE && TextUtils.equals(title, item.value)) {
                return true;
            }
        }
        return false;
    }
}
