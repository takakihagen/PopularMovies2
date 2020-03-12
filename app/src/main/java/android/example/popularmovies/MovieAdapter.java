package android.example.popularmovies;

import android.content.Context;
import android.example.popularmovies.models.Movie;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.PosterViewHolder>{
    private static int ViewHolderCount;
    private int mNumberOfItem;
    private Movie[] mMovieListData;
    private Movie[] mFavoriteMovieListData;

    private final MovieAdapterOnClickHandler mClickHandler;

    public MovieAdapter(int numberOfItem, Movie[] movieListData, MovieAdapterOnClickHandler clickHandler){
        ViewHolderCount = 0;
        mNumberOfItem = numberOfItem;
        mMovieListData = movieListData;
        mClickHandler = clickHandler;
    }

    public void setMovieData(Movie[] movieData){
        mMovieListData = movieData;
    }

    public void setFavoriteMovieListData(Movie[] movieListData){mFavoriteMovieListData=movieListData;}

    public Movie[] getFavoriteMovieListData(){return mFavoriteMovieListData;}

    public interface MovieAdapterOnClickHandler{
        void onClick(Movie movie);
    }

    class PosterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        ImageView imageView;
        PosterViewHolder (View itemView){
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.tv_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie movie = mMovieListData[adapterPosition];
            mClickHandler.onClick(movie);
        }
    }

    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutForListItem = R.layout.poster_list_item;
        boolean shouldAttachToParentImmediately = false;
        
        View view = inflater.inflate(layoutForListItem, viewGroup, shouldAttachToParentImmediately);
        PosterViewHolder viewHolder = new PosterViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PosterViewHolder posterViewHolder, int i) {
        if(mMovieListData != null) {
            Picasso.get()
                    .load(mMovieListData[i].getImageUrl())
                    .into(posterViewHolder.imageView);

        }
    }

    public void setItemCount(int newNumber){
        mNumberOfItem = newNumber;
    }

    @Override
    public int getItemCount() {
        return mNumberOfItem;
    }
}