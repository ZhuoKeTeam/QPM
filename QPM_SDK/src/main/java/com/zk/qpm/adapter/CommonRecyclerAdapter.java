package com.zk.qpm.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView的通用适配器
 */
public abstract class CommonRecyclerAdapter<T> extends RecyclerView.Adapter<CommonRecyclerAdapter.CommonViewHolder> {

    private Context mContext;
    private RecyclerView mRecyclerView;
    private List<T> mDatas = new ArrayList<>();
    private IMultiType<T> mMultiType;

    /**
     * 单种类的ItemView
     */
    public CommonRecyclerAdapter(Context context, RecyclerView recyclerView, List<T> datas, int layoutId) {
        this.mContext = context;
        this.mRecyclerView = recyclerView;
        this.mMultiType = new SingleType<T>(layoutId);
        setDatas(datas, false);
    }

    /**
     * 多种类的ItemView
     */
    public CommonRecyclerAdapter(Context context, RecyclerView recyclerView, List<T> datas, IMultiType<T> multiType) {
        this.mContext = context;
        this.mRecyclerView = recyclerView;
        this.mMultiType = multiType;
        setDatas(datas, false);
    }

    /**
     * 不指定分割线填充器
     */
    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        setLayoutManager(layoutManager, null);
    }

    /**
     * 指定默认的分割线填充器
     */
    public void setLayoutManager(RecyclerView.LayoutManager layoutManager, int dividerHeight, int dividerColor) {
        RecyclerView.ItemDecoration decoration = null;
        if (layoutManager instanceof LinearLayoutManager) {
            if (layoutManager instanceof GridLayoutManager) {
                decoration = new GridDecoration(dividerHeight, dividerColor);
            } else{
                if (LinearLayoutManager.VERTICAL == ((LinearLayoutManager) layoutManager).getOrientation()) {
                    decoration = new VerticalDecoration(dividerHeight, dividerColor);
                } else {
                    decoration = new HorizontalDecoration(dividerHeight, dividerColor);
                }
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            decoration = new GridDecoration(dividerHeight, dividerColor);
        }
        setLayoutManager(layoutManager, decoration);
    }

    /**
     *  指定自定义的分割线填充器
     */
    public void setLayoutManager(RecyclerView.LayoutManager layoutManager, RecyclerView.ItemDecoration decoration) {
        mRecyclerView.setLayoutManager(layoutManager);
        if (decoration != null) {
            mRecyclerView.addItemDecoration(decoration);
        }
    }

    /**
     * 更新数据
     */
    public void setDatas(List<T> datas) {
        setDatas(datas, true);
    }

    private void setDatas(List<T> datas, boolean needNotify) {
        mDatas.clear();
        if (datas != null && !datas.isEmpty()) {
            mDatas.addAll(datas);
        }
        if (needNotify) {
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public CommonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new CommonViewHolder(LayoutInflater.from(mContext).inflate(mMultiType.getLayoutId(viewType), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommonViewHolder viewHolder, int position) {
        onBind(viewHolder, viewHolder.itemView, position, viewHolder.getItemViewType(), mDatas.get(position));
    }

    /**
     * 数据与布局绑定
     */
    protected abstract void onBind(CommonViewHolder viewHolder, View itemView, int position, int viewType, T data);

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mMultiType.getItemType(position, mDatas.get(position));
    }

    /**
     * 通用适配器，这里必须用static修饰，不然提示无法重写父类方法
     */
    public static class CommonViewHolder extends RecyclerView.ViewHolder {

        public CommonViewHolder(@NonNull View itemView) {
            super(itemView);
        }

    }

    /**
     * 单种类定义
     */
    public class SingleType<T> implements IMultiType<T> {

        private static final int TYPE = 0;
        private int mLayoutId;

        public SingleType(int layoutId) {
            this.mLayoutId = layoutId;
        }

        @Override
        public int getLayoutId(int itemType) {
            return mLayoutId;
        }

        @Override
        public int getItemType(int position, T item) {
            return TYPE;
        }

    }

    /**
     * 多种类定义
     */
    public interface IMultiType<T> {

        //根据itemType获取布局文件
        int getLayoutId(int itemType);

        //根据position及T 数据类型 返回对应的ItemType
        int getItemType(int position, T t);

    }

    /**
     * 通用分割器
     */
    public abstract class CommonDecoration extends RecyclerView.ItemDecoration {

        protected int dividerHeight = 1;
        protected Paint mPaint;

        public CommonDecoration(int dividerHeight, int dividerColor) {
            this.dividerHeight = dividerHeight;
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setColor(dividerColor);
            mPaint.setStyle(Paint.Style.FILL);
        }
    }

    /**
     * 竖直排列分割线
     */
    public class VerticalDecoration extends CommonDecoration {

        public VerticalDecoration(int dividerHeight, int dividerColor) {
            super(dividerHeight, dividerColor);
        }

        @Override
        public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
            final int left = parent.getPaddingLeft();
            final int right = parent.getMeasuredWidth() - parent.getPaddingRight();
            final int childSize = parent.getChildCount();
            for (int i = 0; i < childSize; i++) {
                final View child = parent.getChildAt(i);
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
                final int top = child.getBottom() + layoutParams.bottomMargin;
                final int bottom = top + dividerHeight;
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.set(0, 0, 0, dividerHeight);
        }
    }

    /**
     * 横向排列分割线
     */
    public class HorizontalDecoration extends CommonDecoration {

        public HorizontalDecoration(int dividerHeight, int dividerColor) {
            super(dividerHeight, dividerColor);
        }

        @Override
        public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
            final int top = parent.getPaddingTop();
            final int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();
            final int childSize = parent.getChildCount();
            for (int i = 0; i < childSize; i++) {
                final View child = parent.getChildAt(i);
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
                final int left = child.getRight() + layoutParams.rightMargin;
                final int right = left + dividerHeight;
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.set(0, 0, dividerHeight, 0);
        }
    }

    /**
     * Grid排列分割线，包括瀑布流
     */
    public class GridDecoration extends CommonDecoration {

        public GridDecoration(int dividerHeight, int dividerColor) {
            super(dividerHeight, dividerColor);
        }

        @Override
        public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
            drawHorizontal(canvas, parent);
            drawVertical(canvas, parent);
        }

        private int getSpanCount(RecyclerView parent) {
            // 列数
            int spanCount = -1;
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
            }
            return spanCount;
        }

        public void drawHorizontal(Canvas canvas, RecyclerView parent) {
            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                final int left = child.getLeft() - params.leftMargin;
                final int right = child.getRight() + params.rightMargin + dividerHeight;
                final int top = child.getBottom() + params.bottomMargin;
                final int bottom = top + dividerHeight;
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }

        public void drawVertical(Canvas canvas, RecyclerView parent) {
            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                final int top = child.getTop() - params.topMargin;
                final int bottom = child.getBottom() + params.bottomMargin;
                final int left = child.getRight() + params.rightMargin;
                final int right = left + dividerHeight;
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }

        /**
         * 是否是最后一列
         */
        private boolean isLastColum(RecyclerView parent, int pos, int spanCount, int childCount) {
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                // 如果是最后一列，则不需要绘制右边
                if ((pos + 1) % spanCount == 0) {
                    return true;
                }
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
                if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                    // 如果是最后一列，则不需要绘制右边
                    if ((pos + 1) % spanCount == 0) {
                        return true;
                    }
                } else {
                    childCount = childCount - childCount % spanCount;
                    if (pos >= childCount)// 如果是最后一列，则不需要绘制右边
                        return true;
                }
            }
            return false;
        }

        /**
         * 是否是最后一行
         */
        private boolean isLastRaw(RecyclerView parent, int pos, int spanCount, int childCount) {
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                childCount = childCount - childCount % spanCount;
                if (pos >= childCount)// 如果是最后一行，则不需要绘制底部
                    return true;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
                if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                    childCount = childCount - childCount % spanCount;
                    // 如果是最后一行，则不需要绘制底部
                    if (pos >= childCount)
                        return true;
                } else {
                    // 如果是最后一行，则不需要绘制底部
                    if ((pos + 1) % spanCount == 0) {
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
            int spanCount = getSpanCount(parent);
            int childCount = parent.getAdapter().getItemCount();
            if (isLastRaw(parent, itemPosition, spanCount, childCount)) {
                // 如果是最后一行，则不需要绘制底部
                outRect.set(0, 0, dividerHeight, 0);
            } else if (isLastColum(parent, itemPosition, spanCount, childCount)) {
                // 如果是最后一列，则不需要绘制右边
                outRect.set(0, 0, 0, dividerHeight);
            } else {
                outRect.set(0, 0, dividerHeight, dividerHeight);
            }

        }
    }
}
