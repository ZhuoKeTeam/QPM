package com.zk.qpm.function;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;
import android.widget.Toast;

import com.zk.qpm.R;
import com.zk.qpm.adapter.CommonRecyclerAdapter;
import com.zk.qpm.utils.BasicInfoUtils;
import com.zk.qpm.utils.ClipboardUtils;

import java.util.ArrayList;
import java.util.List;

public class BasicInfoFunction implements IFunction {

    private Context mContext;

    private RecyclerView mRecyclerView;
    private CommonRecyclerAdapter<BasicInfoUtils.InfoItem> mAdapter;
    private List<BasicInfoUtils.InfoItem> mItems = new ArrayList<>();

    public BasicInfoFunction(Context context) {
        this.mContext = context;
    }

    @Override
    public int viewStubId() {
        return R.id.vs_basic_info;
    }

    @Override
    public void renderer(View layout) {
        mRecyclerView = layout.findViewById(R.id.rv_basic_info);
        mAdapter = new CommonRecyclerAdapter<BasicInfoUtils.InfoItem>(mContext, mRecyclerView, mItems, new MultiType()) {

            @Override
            protected void onBind(CommonViewHolder viewHolder, View itemView, int position, int viewType, BasicInfoUtils.InfoItem data) {
                if (BasicInfoUtils.InfoItem.TYPE_TITLE == viewType) {
                    rendererTitleItem(itemView, position, data);
                } else if (BasicInfoUtils.InfoItem.TYPE_CONTENT == viewType) {
                    rendererContentItem(itemView, position, data);
                }
            }
        };
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mAdapter.setLayoutManager(layoutManager, 1, Color.parseColor("#DADADA"));
        mRecyclerView.setAdapter(mAdapter);
        mItems.clear();
        mItems.addAll(BasicInfoUtils.getBaseInfo(mContext));
        mAdapter.setDatas(mItems);
    }

    private void rendererTitleItem(View itemView, int position, BasicInfoUtils.InfoItem data) {
        itemView.setOnClickListener(null);
        View dividerView = itemView.findViewById(R.id.v_divider);
        TextView titleView = itemView.findViewById(R.id.tv_title);
        if (position == 0 && data.getType() == ManifestFunction.MultiType.TYPE_TITLE) {
            dividerView.setVisibility(View.GONE);
        } else {
            dividerView.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(data.getLabel())) {
            titleView.setText(data.getLabel());
        }
    }

    private void rendererContentItem(View itemView, int position, final BasicInfoUtils.InfoItem data) {
        itemView.setOnClickListener(null);
        TextView keyView = itemView.findViewById(R.id.tv_key);
        TextView valueView = itemView.findViewById(R.id.tv_value);
        String label = data.getLabel();
        final String content = data.getContent();

        if (!TextUtils.isEmpty(label)) {
            keyView.setText(label);
        }
        if (!TextUtils.isEmpty(content)) {
            valueView.setText(content);
            valueView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardUtils.copyText(content);
                    Toast.makeText(mContext, "已经拷贝：" + content, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    
    public class MultiType implements CommonRecyclerAdapter.IMultiType<BasicInfoUtils.InfoItem> {

        @Override
        public int getLayoutId(int itemType) {
            if (BasicInfoUtils.InfoItem.TYPE_TITLE == itemType) {
                return R.layout.jm_gt_item_title;
            } else if (BasicInfoUtils.InfoItem.TYPE_CONTENT == itemType) {
                return R.layout.jm_gt_item_key_value;
            }
            return R.layout.jm_gt_item_title;
        }

        @Override
        public int getItemType(int position, BasicInfoUtils.InfoItem infoItem) {
            return infoItem.getType();
        }
    }

    @Override
    public ViewStub.OnInflateListener getCallback() {
        return null;
    }

}
