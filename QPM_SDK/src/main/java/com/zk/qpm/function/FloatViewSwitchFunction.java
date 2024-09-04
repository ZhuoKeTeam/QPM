package com.zk.qpm.function;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zk.qpm.R;
import com.zk.qpm.adapter.CommonRecyclerAdapter;
import com.zk.qpm.callback.ICallback;
import com.zk.qpm.floatview.QPMFloatViewBean;
import com.zk.qpm.floatview.QPMFloatViewType;
import com.zk.qpm.manager.QPMFloatViewManager;
import com.zk.qpm.manager.QPMManager;
import com.zk.qpm.manager.QPMModeManager;
import com.zk.qpm.manager.QPMScreenRecorderManager;
import com.zk.qpm.manager.QPMSortManager;
import com.zk.qpm.manager.QPMSwitchManager;
import com.zk.qpm.utils.PrefsParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FloatViewSwitchFunction implements IFunction {

    private Context mContext;
    private CheckBox mModeCheckBox;
    private RecyclerView mRecyclerView;
    private CommonRecyclerAdapter<Item> mAdapter;
    private List<Item> mItems = new ArrayList<>();

    private IModeOperate modeOperate;

    public FloatViewSwitchFunction(Context context) {
        this.mContext = context;
        modeOperate = QPMModeManager.getInstance().isSimpleMode() ? new SimpleModeOperate() : new CustomModeOperate();
    }

    @Override
    public int viewStubId() {
        return R.id.vs_switch;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void renderer(View layout) {
        initData();
        mModeCheckBox = layout.findViewById(R.id.cb_mode_switch);
        mModeCheckBox.setChecked(QPMModeManager.getInstance().isSimpleMode());
        mModeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                QPMModeManager.getInstance().writeMode(isChecked);
                modeOperate = isChecked ? new SimpleModeOperate() : new CustomModeOperate();
                modeOperate.onModeChecked();
            }
        });

        mRecyclerView = layout.findViewById(R.id.rv_switch);
        mAdapter = new CommonRecyclerAdapter<Item>(mContext, mRecyclerView, mItems, R.layout.jm_gt_item_switch) {

            @Override
            protected void onBind(CommonViewHolder viewHolder, View itemView, int position, int viewType, final Item data) {
                TextView switchNameView = itemView.findViewById(R.id.tv_switch);
                TextView switchTipView = itemView.findViewById(R.id.tv_tip);
                CheckBox switchView = itemView.findViewById(R.id.cb_switch);
                switchNameView.setText(data.switchName);
                modeOperate.handleCheckBox(switchView, data);
                if (TextUtils.isEmpty(data.switchTip)) {
                    switchTipView.setVisibility(View.GONE);
                } else {
                    switchTipView.setVisibility(View.VISIBLE);
                    switchTipView.setText(data.switchTip);
                }
            }
        };
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mAdapter.setLayoutManager(layoutManager, 1, Color.parseColor("#DADADA"));
        ItemTouchHelper touchHelper = new ItemTouchHelper(touchCallback);
        touchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setDatas(mItems);

        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP
                        || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    modeOperate.onRecyclerTouchUpOrCancel();
                }
                return false;
            }
        });
    }

    @Override
    public ViewStub.OnInflateListener getCallback() {
        return null;
    }

    private void initData() {
        mItems.clear();
        List<PrefsParser.PrefItem> prefs = QPMSwitchManager.getInstance().getSwitchs();
        List<QPMFloatViewBean> typeBeans = QPMFloatViewManager.getInstance().getTypeBeans();
        for (PrefsParser.PrefItem pref : prefs) {
            Item item = new Item();
            item.item = pref;
            for (QPMFloatViewBean typeBean : typeBeans) {
                if (TextUtils.equals(pref.key, typeBean.type)) {
                    item.switchName = typeBean.switchName;
                    item.switchTip = typeBean.switchTip;
                }
            }
            // 特殊处理视频录制回调
            if (TextUtils.equals(pref.key, QPMFloatViewType.TYPE_FLOAT_VIEW_SCREEN_RECORDER)) {
                item.callback = new ICallback() {
                    @Override
                    public void callback() {
                        Context context = QPMManager.getInstance().getContext();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                                && !QPMSwitchManager.getInstance().isSwitchOpen(context, QPMFloatViewType.TYPE_FLOAT_VIEW_SCREEN_RECORDER)
                                && QPMScreenRecorderManager.getInstance().isStart()) {
                            QPMScreenRecorderManager.getInstance().stopRecorder();
                        }
                    }
                };
                // 视频录制必须要5.0以上
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    continue;
                }
            }
            mItems.add(item);
        }

        Collections.sort(mItems, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return QPMSortManager.getInstance().compare(o1.item.key, o2.item.key);
            }
        });
    }

    public class Item {

        public String switchName;
        public String switchTip;
        public PrefsParser.PrefItem item;
        public ICallback callback;

    }

    private ItemTouchHelper.Callback touchCallback = new ItemTouchHelper.Callback() {

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            // 定义当前可拖动的方向
            if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                final int swipeFlags = 0;
                return makeMovementFlags(dragFlags, swipeFlags);
            } else {
                final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                final int swipeFlags = 0;
                // 可以设置左右滑动
//                final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            // 滑动过程中交换数据
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(mItems, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(mItems, i, i - 1);
                }
            }
            mAdapter.notifyItemMoved(fromPosition, toPosition);
            // 重定义排序
            for (int i = 0; i < mItems.size(); i++) {
                String type = mItems.get(i).item.key;
                QPMSortManager.getInstance().reSort(type, i + 1);
            }
            // 刷新悬浮窗顺序
            QPMFloatViewManager.getInstance().refreshFloatViewAndComponent(true);
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            // 如果设置了swipeFlags，那么在滑动的时候就会调用
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
            // 开始拖动，可以给界面添加UI效果
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            // 停止拖动，去掉刚才加的UI效果
        }
    };

    public interface IModeOperate {

        void onModeChecked();

        void onRecyclerTouchUpOrCancel();

        void handleCheckBox(CheckBox checkBox, Item item);
    }

    public class CustomModeOperate implements IModeOperate {

        @Override
        public void onModeChecked() {
            if (mAdapter != null) {
                mAdapter.setDatas(mItems);
            }
            // 直接显示悬浮窗，内部处理了切换逻辑
            QPMFloatViewManager.getInstance().floatViewShow();
        }

        @Override
        public void onRecyclerTouchUpOrCancel() {
            // 什么都不用处理
        }

        @Override
        public void handleCheckBox(CheckBox checkBox, final Item item) {
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    item.item.value = String.valueOf(isChecked);
                    QPMSwitchManager.getInstance().writeSwitch(item.item);
                    QPMFloatViewManager.getInstance().refreshFloatViewAndComponent(true);
                    if (item.callback != null) {
                        item.callback.callback();
                    }
                }
            });
            checkBox.setChecked(Boolean.parseBoolean(item.item.value));
            checkBox.setEnabled(true);
        }
    }

    public class SimpleModeOperate implements IModeOperate {

        @Override
        public void onModeChecked() {
            // 精简模式选中下，需要把当前所有开关前两个强制选上，其他全部关闭
            for (int i = 0; i < mItems.size(); i++) {
                Item item = mItems.get(i);
                item.item.value = String.valueOf(i < 2);
                QPMSwitchManager.getInstance().writeSwitch(item.item);
                if (item.callback != null) {
                    item.callback.callback();
                }
            }
            if (mAdapter != null) {
                mAdapter.setDatas(mItems);
            }
            // 直接显示悬浮窗，内部处理了切换逻辑
            QPMFloatViewManager.getInstance().floatViewShow();
        }

        @Override
        public void onRecyclerTouchUpOrCancel() {
            // 精简模式下，需要把当前所有开关前两个强制选上，其他全部关闭
            boolean hasChanged = false;
            for (int i = 0; i < mItems.size(); i++) {
                Item item = mItems.get(i);
                boolean value = Boolean.parseBoolean(item.item.value);
                if (i < 2) {
                    if (!value) {
                        hasChanged = true;
                    }
                } else {
                    if (value) {
                        hasChanged = true;
                    }
                }
                item.item.value = String.valueOf(i < 2);
                QPMSwitchManager.getInstance().writeSwitch(item.item);
                if (item.callback != null) {
                    item.callback.callback();
                }
            }
            if (hasChanged && mAdapter != null) {
                mAdapter.setDatas(mItems);
            }
            QPMFloatViewManager.getInstance().refreshFloatViewAndComponent(true);
        }

        @Override
        public void handleCheckBox(CheckBox checkBox, Item item) {
            // 精简模式不允许主动勾选，只能拖动
            checkBox.setOnCheckedChangeListener(null);
            checkBox.setChecked(Boolean.parseBoolean(item.item.value));
            checkBox.setEnabled(false);
        }
    }
}
