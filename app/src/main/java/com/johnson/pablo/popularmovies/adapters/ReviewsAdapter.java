package com.johnson.pablo.popularmovies.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.johnson.pablo.popularmovies.R;
import com.johnson.pablo.popularmovies.models.Review;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Pablo Johnson
 */
public class ReviewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Review> mItemList;
    private final LayoutInflater mInflater;

    public ReviewsAdapter(Context context, List<Review> itemList) {
        mItemList = itemList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReviewHolder(mInflater.inflate(R.layout.item_review_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ReviewHolder holder = (ReviewHolder) viewHolder;
        final Review review = mItemList.get(position);
        holder.author.setText(review.getAuthor());
        holder.content.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    final class ReviewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.reviewAuthor)
        TextView author;
        @Bind(R.id.reviewContent)
        TextView content;

        public ReviewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void addReviews(List<Review> reviews) {
        if (mItemList == null) {
            mItemList = reviews;
        } else {
            mItemList.addAll(reviews);
        }
        notifyDataSetChanged();
    }

}
