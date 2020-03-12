package android.example.popularmovies;

import android.content.Context;
import android.example.popularmovies.models.Movie;
import android.example.popularmovies.models.Trailer;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private Trailer[] mTrailerData;

    private final TrailerAdapterOnClickHandler mClickHandler;

    TrailerAdapter(TrailerAdapterOnClickHandler clickHandler){
        mTrailerData = null;
        mClickHandler = clickHandler;
    }

    public void setTrailerData(Trailer[] trailerData){
        mTrailerData = trailerData;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutForTrailerItem = R.layout.trailer_list;
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutForTrailerItem, viewGroup, shouldAttachToParentImmediately);
        TrailerViewHolder viewHolder = new TrailerViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder trailerViewHolder, int i) {
        if(mTrailerData != null) {
            if (i < mTrailerData.length) {
                trailerViewHolder.trailerTitleTextView.setText(mTrailerData[i].getName());
            }
        }
    }

    @Override
    public int getItemCount() {
        return mTrailerData!=null?mTrailerData.length:0;
    }

    public interface TrailerAdapterOnClickHandler{
        void onClick(Trailer trailer);
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView trailerTitleTextView;

        TrailerViewHolder (View itemView){
            super(itemView);
            trailerTitleTextView = (TextView) itemView.findViewById(R.id.tv_tlailer_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Trailer trailer = mTrailerData[adapterPosition];
            mClickHandler.onClick(trailer);
        }
    }
}
