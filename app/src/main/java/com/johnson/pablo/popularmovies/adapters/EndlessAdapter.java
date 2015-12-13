package com.johnson.pablo.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.johnson.pablo.popularmovies.R;

import java.util.List;

/**
 * Created by Pablo on 12/12/15.
 */
public abstract class EndlessAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    protected static final int VIEW_TYPE_LOAD_MORE = 0;
    protected static final int VIEW_TYPE_ITEM = 1;

    @NonNull
    protected final LayoutInflater mInflater;
    @NonNull
    protected List<T> mMovies;
    protected boolean showLoadMore = false;

    public EndlessAdapter(@NonNull Context context, @NonNull List<T> items) {
        mInflater = LayoutInflater.from(context);
        mMovies = items;
    }

    public void setLoadMore(boolean enabled) {
        if (showLoadMore != enabled) {
            if (showLoadMore) {
                notifyItemRemoved(getItemCount());
                showLoadMore = false;
            } else {
                notifyItemInserted(getItemCount());
                showLoadMore = true;
            }
        }
    }

    public boolean isLoadMore() {
        return showLoadMore;
    }

    @Override
    public int getItemViewType(int position) {
        return isItemLoadMoreType(position) ? VIEW_TYPE_LOAD_MORE : VIEW_TYPE_ITEM;
    }

    public boolean isItemLoadMoreType(int position) {
        return showLoadMore && (position == (getItemCount() - 1));
    }

    public void set(@NonNull List<T> items) {
        mMovies = items;
        notifyDataSetChanged();
    }

    public void add(@NonNull List<T> newItems) {
        if (!newItems.isEmpty()) {
            int currentSize = mMovies.size();
            int amountInserted = newItems.size();

            mMovies.addAll(newItems);
            notifyItemRangeInserted(currentSize, amountInserted);
        }
    }

    @NonNull
    public List<T> getItems() {
        return mMovies;
    }

    public T getItem(int position) {
        return !isItemLoadMoreType(position) ? mMovies.get(position) : null;
    }

    public void clear() {
        if (!mMovies.isEmpty()) {
            mMovies.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewType == VIEW_TYPE_LOAD_MORE
                ? new RecyclerView.ViewHolder(mInflater.inflate(R.layout.load_more_item, parent, false)) {
        }
                : onCreateItemHolder(parent, viewType);
    }

    @Override
    public int getItemCount() {
        return mMovies.size() + countLoadMore();
    }

    protected int countLoadMore() {
        return showLoadMore ? 1 : 0;
    }

    protected abstract VH onCreateItemHolder(ViewGroup parent, int viewType);
}
