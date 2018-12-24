package com.zk.qpm.function;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zk.qpm.R;
import com.zk.qpm.adapter.CommonRecyclerAdapter;
import com.zk.qpm.utils.PrefsParser;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PrefInfoFunction implements IFunction {

    private Context mContext;
    private TitleFunction mTitleFunction;
    private RecyclerView mRecyclerView;
    private CommonRecyclerAdapter<Item> mAdapter;
    private List<Item> mAllItems = new ArrayList<>();
    private List<Item> mItems = new ArrayList<>();
    private List<Item> modifyItems = new ArrayList<>();

    public PrefInfoFunction(Context context, TitleFunction titleFunction) {
        this.mContext = context;
        this.mTitleFunction = titleFunction;
    }

    @Override
    public int viewStubId() {
        return R.id.vs_pref_info;
    }

    @Override
    public void renderer(View layout) {
        initData();
        refreshShowData();
        mRecyclerView = layout.findViewById(R.id.rv_pref_info);
        mAdapter = new CommonRecyclerAdapter<Item>(mContext, mRecyclerView, mItems, new MultiType()) {

            @Override
            protected void onBind(CommonViewHolder viewHolder, View itemView, int position, int viewType, Item data) {
                if (MultiType.TYPE_TITLE == viewType) {
                    rendererTitleItem(itemView, position, data);
                } else if (MultiType.TYPE_CONTENT == viewType) {
                    rendererContentItem(itemView, position, data);
                }
            }
        };
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mAdapter.setLayoutManager(layoutManager, 1, Color.parseColor("#DADADA"));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setDatas(mItems);
        mTitleFunction.initOperateButton(mContext.getString(R.string.jm_gt_modify), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePrefItems();
            }
        }, false);
    }

    private void rendererTitleItem(View itemView, int position, final Item data) {
        TextView titleView = itemView.findViewById(R.id.tv_title);
        ImageView arrowView = itemView.findViewById(R.id.iv_arrow);
        if (data.prefFile != null && !TextUtils.isEmpty(data.prefFile.getName())) {
            titleView.setText(data.prefFile.getName());
        }
        if (data.isFold) {
            arrowView.setImageResource(R.drawable.jm_gt_unfold_arrow);
        } else {
            arrowView.setImageResource(R.drawable.jm_gt_fold_arrow);
        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.isFold = !data.isFold;
                refreshShowData();
                mAdapter.setDatas(mItems);
            }
        });
    }

    private void rendererContentItem(View itemView, int position, final Item data) {
        itemView.setOnClickListener(null);
        TextView typeView = itemView.findViewById(R.id.tv_type);
        TextView keyView = itemView.findViewById(R.id.tv_key);
        EditText valueView = itemView.findViewById(R.id.et_value);
        if (data.prefItem != null && !TextUtils.isEmpty(data.prefItem.type)) {
            typeView.setText(data.prefItem.type);
        }
        if (data.prefItem != null && !TextUtils.isEmpty(data.prefItem.key)) {
            keyView.setText(data.prefItem.key);
        }
        if (valueView.getTag() != null) {
            valueView.removeTextChangedListener((TextWatcher) valueView.getTag());
        }
        if (data.prefItem != null && !TextUtils.isEmpty(data.prefItem.value)) {
            valueView.setText(data.prefItem.value);
        }
        valueView.setBackgroundColor(data.isWriteFail ? Color.RED : Color.WHITE);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                data.prefItem.value = s.toString();
                if (!modifyItems.contains(data)) {
                    modifyItems.add(data);
                }
                mTitleFunction.refreshOperateView(!modifyItems.isEmpty());
            }
        };
        valueView.addTextChangedListener(textWatcher);
        valueView.setTag(textWatcher);
    }

    private void updatePrefItems() {
        if (modifyItems.isEmpty()) {
            return;
        }
        Iterator<Item> iterator = modifyItems.iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            if (PrefsParser.writePrefs(mContext, item.prefFile.getName(), item.prefItem)) {
                item.isWriteFail = false;
                iterator.remove();
            } else {
                item.isWriteFail = true;
            }
        }
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
        mTitleFunction.refreshOperateView(!modifyItems.isEmpty());
    }

    @Override
    public ViewStub.OnInflateListener getCallback() {
        return null;
    }

    private void refreshShowData() {
        mItems.clear();
        for (Item item : mAllItems) {
            if (item.type == MultiType.TYPE_TITLE) {
                mItems.add(item);
                continue;
            }
            Item titleItem = findTitleItemByFile(item.prefFile);
            if (titleItem == null){
                continue;
            }
            item.isFold = titleItem.isFold;
            if (!item.isFold){
                mItems.add(item);
            }
        }
    }

    private Item findTitleItemByFile(File file) {
        for (Item item : mItems) {
            if (item.prefFile == file) {
                return item;
            }
        }
        return null;
    }

    private void initData() {
        mAllItems.clear();
        Map<File, List<PrefsParser.PrefItem>> allPrefs = PrefsParser.getAllPrefs(mContext);
        for (Map.Entry<File, List<PrefsParser.PrefItem>> entry : allPrefs.entrySet()) {
            File file = entry.getKey();
            List<PrefsParser.PrefItem> items = entry.getValue();
            if (items.isEmpty()) {
                continue;
            }
            // 标题
            Item title = new Item(file);
            mAllItems.add(title);
            for (PrefsParser.PrefItem item : items) {
                Item content = new Item(file, item);
                mAllItems.add(content);
            }
        }
    }

    public class Item {
        public int type;
        public File prefFile;
        public boolean isFold = true;
        public PrefsParser.PrefItem prefItem;
        public boolean isWriteFail;

        public Item(File prefFile) {
            this.prefFile = prefFile;
            this.type = MultiType.TYPE_TITLE;
        }

        public Item(File prefFile, PrefsParser.PrefItem prefItem) {
            this.prefFile = prefFile;
            this.prefItem = prefItem;
            this.type = MultiType.TYPE_CONTENT;
        }
    }

    public class MultiType implements CommonRecyclerAdapter.IMultiType<Item> {

        public static final int TYPE_TITLE = 0;
        public static final int TYPE_CONTENT = 1;

        @Override
        public int getLayoutId(int itemType) {
            if (TYPE_TITLE == itemType) {
                return R.layout.jm_gt_item_title_arrow;
            } else if (TYPE_CONTENT == itemType) {
                return R.layout.jm_gt_item_type_key_value_edit;
            }
            return R.layout.jm_gt_item_title;
        }

        @Override
        public int getItemType(int position, Item item) {
            return item.type;
        }
    }
}
