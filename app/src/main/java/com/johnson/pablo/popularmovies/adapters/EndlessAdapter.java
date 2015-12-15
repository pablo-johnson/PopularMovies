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


    protected static final int VIEW_TYPE_ITEM = 1;

    @NonNull
    protected final LayoutInflater mInflater;
    @NonNull
    protected List<T> mMovies;

    public EndlessAdapter(@NonNull Context context, @NonNull List<T> items) {
        mInflater = LayoutInflater.from(context);
        mMovies = items;
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_ITEM;
    }

    public void add(@NonNull List<T> newItems) {
        if (!newItems.isEmpty()) {
            int currentSize = mMovies.size();
            int amountInserted = newItems.size();

            mMovies.addAll(newItems);
            notifyItemRangeInserted(currentSize, amountInserted);
        }
    }

    public void clear() {
        if (!mMovies.isEmpty()) {
            mMovies.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreateItemHolder(parent, viewType);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    protected abstract VH onCreateItemHolder(ViewGroup parent, int viewType);
}
