package com.jm.android.gt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用适配器
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected List<T> mDatas = new ArrayList<T>();
    protected IMultiType mMultiType;

    public CommonAdapter(Context context, List<T> mDatas, int itemLayoutId) {
        this.mContext = context;
        this.mMultiType = new SingleType(itemLayoutId);
        setDatas(mDatas, false);
    }

    public CommonAdapter(Context context, List<T> mDatas, IMultiType multiType) {
        this.mContext = context;
        this.mMultiType = multiType;
        setDatas(mDatas, false);
    }

    public void setDatas(List<T> list) {
        setDatas(list, true);
    }

    private void setDatas(List<T> list, boolean needNotify) {
        mDatas.clear();
        if (list != null) {
            mDatas.addAll(list);
        }
        if (needNotify) {
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return mMultiType.getItemType(position, getItem(position));
    }

    @Override
    public int getViewTypeCount() {
        return mMultiType.getItemTypeCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = mMultiType.getItemType(position, mDatas.get(position));
        int layoutId = mMultiType.getLayoutId(viewType);
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, layoutId, position, viewType);
        onBind(viewHolder, viewHolder.mConvertView, viewHolder.mPosition, viewHolder.mViewType, getItem(position));
        return viewHolder.mConvertView;
    }

    public abstract void onBind(ViewHolder holder, View itemView, int position, int viewType, T data);

    public static class ViewHolder {

        public View mConvertView;
        public int mPosition;
        public int mViewType;

        private ViewHolder(Context context, ViewGroup parent, int layoutId, int position, int viewType) {
            this.mPosition = position;
            this.mViewType = viewType;
            this.mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
            this.mConvertView.setTag(this);
        }

        public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position, int viewType) {
            if (convertView == null) {
                return new ViewHolder(context, parent, layoutId, position, viewType);
            } else {
                ViewHolder viewHolder = (ViewHolder) (convertView.getTag());
                viewHolder.mConvertView = convertView;
                viewHolder.mPosition = position;
                return viewHolder;
            }
        }

    }

    public class SingleType<T> implements IMultiType<T> {

        public static final int TYPE = 0;
        private int mLayoutId;

        public SingleType(int mLayoutId) {
            this.mLayoutId = mLayoutId;
        }

        @Override
        public int getLayoutId(int itemType) {
            return mLayoutId;
        }

        @Override
        public int getItemType(int position, T t) {
            return TYPE;
        }

        @Override
        public int getItemTypeCount() {
            return 1;
        }
    }

    public interface IMultiType<T> {

        //根据itemType获取布局文件
        int getLayoutId(int itemType);

        //根据position及T 数据类型 返回对应的ItemType
        int getItemType(int position, T t);

        //返回总共几种item类型
        int getItemTypeCount();

    }

}
