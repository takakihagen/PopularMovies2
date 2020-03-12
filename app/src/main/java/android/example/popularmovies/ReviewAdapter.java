package android.example.popularmovies;

import android.content.Context;
import android.example.popularmovies.models.Review;
import android.example.popularmovies.models.Trailer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private Review[] mReviewData;

    public void setRviewData(Review[] reviews){mReviewData = reviews;}

    ReviewAdapter(){
        mReviewData = null;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutForTrailerItem = R.layout.review_list;
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutForTrailerItem, viewGroup, shouldAttachToParentImmediately);
        ReviewViewHolder viewHolder = new ReviewViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder reviewViewHolder, int i) {
        if(mReviewData != null){
            if(i<mReviewData.length){
                reviewViewHolder.mAutherTextView.setText(mReviewData[i].getAuthor());
                reviewViewHolder.mContentTextView.setText(mReviewData[i].getContent());
            }
        }
    }

    @Override
    public int getItemCount() {
        return mReviewData!=null?mReviewData.length:0;
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView mAutherTextView;
        TextView mContentTextView;
        ReviewViewHolder(View itemView){
            super(itemView);
            mAutherTextView = (TextView) itemView.findViewById(R.id.tv_author);
            mContentTextView = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }
}
