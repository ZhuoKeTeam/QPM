package com.zk.qpm.function;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zk.qpm.QPMRAnalysisResult;
import com.zk.qpm.R;
import com.zk.qpm.adapter.CommonRecyclerAdapter;
import com.zk.qpm.manager.QPMRAnalysisManager;
import com.zk.qpm.utils.DecimalFormatUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NetworkAPIFunction implements IFunction {

    private Context mContext;
    private RecyclerView mRecyclerView;
    private CommonRecyclerAdapter<Item> mAdapter;
    private List<Item> mItems = new ArrayList<>();

    public NetworkAPIFunction(Context context) {
        this.mContext = context;
    }

    @Override
    public int viewStubId() {
        return R.id.vs_network_api;
    }

    @Override
    public void renderer(View layout) {
        initData();
        mRecyclerView = layout.findViewById(R.id.rv_network_api);
        mAdapter = new CommonRecyclerAdapter<Item>(mContext, mRecyclerView, mItems, R.layout.jm_gt_item_network_api) {

            @Override
            protected void onBind(CommonViewHolder viewHolder, View itemView, int position, int viewType, final Item data) {
                TextView timeView = itemView.findViewById(R.id.tv_time);
                LinearLayout titleLayout = itemView.findViewById(R.id.ll_title);
                TextView titleView = itemView.findViewById(R.id.tv_title);
                ImageView arrowView = itemView.findViewById(R.id.iv_arrow);
                LinearLayout detailLayout = itemView.findViewById(R.id.ll_detail);
                TextView sourceUrlView = itemView.findViewById(R.id.tv_source_url);
                TextView requestMethodView = itemView.findViewById(R.id.tv_request_method);
                TextView responseCodeView = itemView.findViewById(R.id.tv_response_code);
                TextView requestTimeView = itemView.findViewById(R.id.tv_request_time);
                TextView requestSizeView = itemView.findViewById(R.id.tv_request_size);
                TextView responseSizeView = itemView.findViewById(R.id.tv_response_size);
                TextView requestHeaderView = itemView.findViewById(R.id.tv_request_header);
                TextView requestContentView = itemView.findViewById(R.id.tv_request_content);
                TextView responseHeaderView = itemView.findViewById(R.id.tv_response_header);
                TextView responseContentView = itemView.findViewById(R.id.tv_response_content);

                timeView.setText(data.time);
                titleView.setText(data.path);
                if (data.isFold) {
                    arrowView.setImageResource(R.drawable.jm_gt_unfold_arrow);
                    detailLayout.setVisibility(View.GONE);
                } else {
                    arrowView.setImageResource(R.drawable.jm_gt_fold_arrow);
                    detailLayout.setVisibility(View.VISIBLE);
                    sourceUrlView.setText(mContext.getString(R.string.jm_gt_source_url, data.networkInfo.originUrl));
                    requestMethodView.setText(mContext.getString(R.string.jm_gt_request_method, data.networkInfo.method));
                    responseCodeView.setText(mContext.getString(R.string.jm_gt_response_code, data.networkInfo.responseCode));
                    requestTimeView.setText(mContext.getString(R.string.jm_gt_request_time, data.networkInfo.requestTime));
                    requestSizeView.setText(mContext.getString(R.string.jm_gt_request_size, DecimalFormatUtil.divider(data.networkInfo.requestSize, 1024, 3)));
                    responseSizeView.setText(mContext.getString(R.string.jm_gt_response_size, DecimalFormatUtil.divider(data.networkInfo.responseSize, 1024, 3)));
                    requestHeaderView.setText(mContext.getString(R.string.jm_gt_request_header, data.networkInfo.requestHeader));
                    requestContentView.setText(mContext.getString(R.string.jm_gt_request_content, data.networkInfo.requestContent));
                    responseHeaderView.setText(mContext.getString(R.string.jm_gt_response_header, data.networkInfo.responseHeader));
                    responseContentView.setText(mContext.getString(R.string.jm_gt_response_content, data.networkInfo.responseContent));
                }

                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        data.isFold = !data.isFold;
                        notifyDataSetChanged();
                    }
                };
                timeView.setOnClickListener(listener);
                titleLayout.setOnClickListener(listener);
            }
        };
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mAdapter.setLayoutManager(layoutManager, 1, Color.parseColor("#DADADA"));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setDatas(mItems);
    }

    private void initData() {
        mItems.clear();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        synchronized (QPMRAnalysisResult.SYNCHRONIZED_NETWORKINFO) {
            QPMRAnalysisResult analysisResult = QPMRAnalysisManager.getInstance().getJMGTRAnalysisResult();
            for (QPMRAnalysisResult.NetworkInfo networkInfo : analysisResult.networkInfos) {
                Item item = new Item();
                item.networkInfo = networkInfo;
                item.time = format.format(new Date(networkInfo.currentTime));
                try {
                    item.path = Uri.parse(item.networkInfo.originUrl).getPath();
                } catch (Exception e) {
                    item.path = item.networkInfo.originUrl;
                }
                mItems.add(item);
            }
        }
    }

    @Override
    public ViewStub.OnInflateListener getCallback() {
        return null;
    }

    public class Item {

        public QPMRAnalysisResult.NetworkInfo networkInfo;
        public boolean isFold = true;
        public String time;
        public String path;


    }

}
