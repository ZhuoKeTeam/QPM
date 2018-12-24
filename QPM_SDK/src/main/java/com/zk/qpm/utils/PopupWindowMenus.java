package com.jm.android.gt.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

public class PopupWindowMenus<T> {

    private PopupWindow popupWindow;

    public void showPopupWindow(Activity context, PopupWindowInitiator<T> initiator, final PopupWindowCallBack listener) {
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
        View view = LayoutInflater.from(context).inflate(initiator.popupLayoutId(), null, false);
        ListView listview = (ListView) view.findViewById(initiator.listViewId());
        initiator.onListViewDecorate(listview);
        PopupCommonAdapter<T> adapter = new PopupCommonAdapter<T>(context, initiator.loadDatas(), initiator);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
                if (listener != null) {
                    listener.callback(position);
                }
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
            }
        });
        popupWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        if (context != null && !context.isFinishing()) {
            popupWindow.showAsDropDown(initiator.dependOnView());
        }
    }

    public class PopupCommonAdapter<T> extends BaseAdapter {

        private List<T> datas = new ArrayList<T>();
        private Context mContext;
        private PopupWindowInitiator<T> initiator;

        public PopupCommonAdapter(Context context, List<T> datas, PopupWindowInitiator<T> initiator) {
            this.mContext = context;
            this.initiator = initiator;
            if (datas == null || datas.isEmpty()) {
                return;
            }
            this.datas.addAll(datas);
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
                    initiator.itemLayoutId(), position);
            if (initiator != null) {
                initiator.onBind(viewHolder, viewHolder.mConvertView, viewHolder.mPosition, datas.get(position));
            }
            return viewHolder.mConvertView;
        }

    }

    public static class ViewHolder {

        public View mConvertView;
        public int mPosition;

        private ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
            this.mPosition = position;
            this.mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
            this.mConvertView.setTag(this);
        }

        public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
            if (convertView == null) {
                return new ViewHolder(context, parent, layoutId, position);
            } else {
                ViewHolder viewHolder = (ViewHolder) (convertView.getTag());
                viewHolder.mConvertView = convertView;
                viewHolder.mPosition = position;
                return viewHolder;
            }
        }

    }

    public interface PopupWindowCallBack {
        void callback(int position);
    }

    public interface PopupWindowInitiator<T> {
        int popupLayoutId();

        int itemLayoutId();

        int listViewId();

        void onListViewDecorate(ListView listView);

        void onBind(ViewHolder viewHolder, View itemView, int position, T data);

        List<T> loadDatas();

        View dependOnView();
    }
}
