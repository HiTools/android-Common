package dev.hitools.common.widget.pulltorefresh.recycleview;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import dev.hitools.common.R;
import dev.hitools.common.app.provider.InitProvider;
import dev.hitools.common.utils.log.LogUtils;
import dev.hitools.common.widget.pulltorefresh.footer.IPullToRefreshFooter;


public class LoadMoreAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements IPullToRefreshFooter {
    private static final String TAG = "LoadMoreAdapter";
    /**
     * 是否是加载更多的Item
     */
    private static final int TYPE_LOAD_MORE = Integer.MAX_VALUE - 2;
    /**
     * 加载动画的时间
     */
    private static final int ROTATE_ANIM_DURATION = 380;

    /**
     * 包裹的Adapter
     */
    private final RecyclerView.Adapter<VH> innerAdapter;
    /**
     * LoadMore de Views
     */
    private View loadMoreView;
    private TextView loadMoreTextView;
    private ImageView loadMoreLoadingView;
    /**
     * 旋转动画
     */
    private final RotateAnimation rotateAnimation;
    /**
     * 当前的Load More状态
     */
    private int loadingStatus;

    public String normalTips;
    public String loadingTips;
    public String errorTips;
    public String endTips;
    public String emptyTips;

    public LoadMoreAdapter(@NonNull RecyclerView.Adapter<VH> adapter) {
        innerAdapter = adapter;
        innerAdapter.registerAdapterDataObserver(dataObserver);

        rotateAnimation = new RotateAnimation(0, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(ROTATE_ANIM_DURATION * 2);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setFillAfter(false);

        final Resources res = InitProvider.getApp().getResources();
        normalTips = res.getString(R.string.pull2refresh_footer_normal);
        loadingTips = res.getString(R.string.pull2refresh_footer_loading);
        errorTips = res.getString(R.string.pull2refresh_footer_fail);
        endTips = res.getString(R.string.pull2refresh_footer_end);
        emptyTips = res.getString(R.string.pull2refresh_footer_empty);
    }


    @Override
    public int getItemCount() {
        return innerAdapter.getItemCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        int realCount = innerAdapter.getItemCount();
        if (position >= realCount) {
            return TYPE_LOAD_MORE;
        } else {
            return innerAdapter.getItemViewType(position);
        }
    }

    /**
     * 是否是 LoadMore状态
     */
    private boolean isLoadMoreItem(int position) {
        int realCount = innerAdapter.getItemCount();
        return position >= realCount;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_LOAD_MORE) {
            final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            loadMoreView = inflater.inflate(R.layout.layout_pull2refresh_footer, parent, false);
            loadMoreTextView = loadMoreView.findViewById(R.id.pull_to_refresh_footer_text);
            loadMoreLoadingView = loadMoreView.findViewById(R.id.pull_to_refresh_footer_loading);
            return new ViewHolder(loadMoreView);
        } else {
            return innerAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int itemType = getItemViewType(position);
        if (itemType == TYPE_LOAD_MORE) {
            setFooterStatus(loadingStatus);
        } else {
            innerAdapter.onBindViewHolder((VH) holder, position);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        innerAdapter.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        if (!(layoutManager instanceof GridLayoutManager)) {
            // 如果不是GridLayoutManager 那么不进行任何操作
            return;
        }

        final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
        final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (isLoadMoreItem(position)) {
                    return gridLayoutManager.getSpanCount();
                }
                if (spanSizeLookup != null) {
                    return spanSizeLookup.getSpanSize(position);
                }
                return 1;
            }
        });
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        try {
            innerAdapter.unregisterAdapterDataObserver(dataObserver);
        } catch (Exception e) {
            LogUtils.e(TAG, e);
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        innerAdapter.onViewAttachedToWindow((VH)holder);
        if (isLoadMoreItem(holder.getLayoutPosition())) {
            setFullSpan(holder);
            setFooterStatus(loadingStatus);
        }
    }

    private void setFullSpan(RecyclerView.ViewHolder holder) {
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }

    @Override
    public void init(@NonNull ViewGroup parent) {
        loadingStatus = IPullToRefreshFooter.STATUS_NORMAL;
    }

    @Override
    public void setStatus(@FooterStatus int status) {
        loadingStatus = status;
        setFooterStatus(status);
    }

    @Override
    public int getStatus() {
        return loadingStatus;
    }

    private void setFooterStatus(int status) {
        if (loadMoreView == null) {
            return;
        }
        switch (status) {
            case STATUS_NORMAL:
                loadMoreLoadingView.clearAnimation();
                loadMoreLoadingView.setVisibility(View.GONE);
                loadMoreTextView.setText(normalTips);
                break;
            case STATUS_LOADING:
                loadMoreLoadingView.clearAnimation();
                loadMoreLoadingView.startAnimation(rotateAnimation);
                loadMoreLoadingView.setVisibility(View.VISIBLE);
                loadMoreTextView.setText(loadingTips);
                break;
            case STATUS_FAILED:
                loadMoreLoadingView.clearAnimation();
                loadMoreLoadingView.setVisibility(View.GONE);
                loadMoreTextView.setText(errorTips);
                break;
            case STATUS_END:
                loadMoreLoadingView.clearAnimation();
                loadMoreLoadingView.setVisibility(View.GONE);
                if (innerAdapter.getItemCount() == 0) {
                    loadMoreTextView.setText(emptyTips);
                } else {
                    loadMoreTextView.setText(endTips);
                }
                break;
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public RecyclerView.Adapter getInnerAdapter() {
        return innerAdapter;
    }


    /**
     * 注册监听
     */
    final private RecyclerView.AdapterDataObserver dataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
            notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            notifyItemMoved(fromPosition, toPosition);
        }

    };
}
