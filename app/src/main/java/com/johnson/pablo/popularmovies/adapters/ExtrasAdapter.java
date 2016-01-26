package com.johnson.pablo.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.johnson.pablo.popularmovies.R;
import com.johnson.pablo.popularmovies.models.Review;
import com.johnson.pablo.popularmovies.models.Video;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Pablo Johnson
 */
public class ExtrasAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int mItemViewType;
    private List<T> mItemList;
    private final LayoutInflater mInflater;
    public final static int TRAILER_TYPE = 0;
    public final static int REVIEW_TYPE = 1;
    private OnExtraClickListener mListener;

    public interface OnExtraClickListener {
        void onReviewClicked(@NonNull final Review review, View view, int position);

        void onTrailerClicked(@NonNull final Video trailer, View view, int position);

    }

    public ExtrasAdapter(Context context, List<T> itemList, int itemViewType) {
        mItemList = itemList;
        mInflater = LayoutInflater.from(context);
        mItemViewType = itemViewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mItemViewType == TRAILER_TYPE) {
            return new TrailerHolder(mInflater.inflate(R.layout.item_video_list, parent, false));
        } else {
            return new ReviewHolder(mInflater.inflate(R.layout.item_review_list, parent, false));

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (mItemViewType == REVIEW_TYPE) {
            ReviewHolder holder = (ReviewHolder) viewHolder;
            final Review review = (Review) mItemList.get(position);
            holder.author.setText(review.getAuthor());
            holder.content.setText(review.getContent());
            holder.reviewContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onReviewClicked(review, view, position);
                }
            });
        } else {
            TrailerHolder holder = (TrailerHolder) viewHolder;
            final Video trailer = (Video) mItemList.get(position);
            holder.name.setText(trailer.getName());
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onTrailerClicked(trailer, view, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mItemViewType;
    }

    public void addItems(List<T> items) {
        if (mItemList == null) {
            mItemList = items;
        } else {
            mItemList.addAll(items);
        }
        notifyDataSetChanged();
    }

    public void setListener(@NonNull OnExtraClickListener listener) {
        this.mListener = listener;
    }

    final class ReviewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.reviewAuthor)
        TextView author;
        @Bind(R.id.reviewContent)
        TextView content;
        @Bind(R.id.reviewContainer)
        View reviewContainer;

        public ReviewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    final class TrailerHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.trailerName)
        TextView name;
        @Bind(R.id.trailerContainer)
        View container;

        public TrailerHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
