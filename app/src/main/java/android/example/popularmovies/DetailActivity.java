package android.example.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.example.popularmovies.database.FavoriteDatabase;
import android.example.popularmovies.database.FavoriteEntry;
import android.example.popularmovies.models.Movie;
import android.example.popularmovies.models.Review;
import android.example.popularmovies.models.Trailer;
import android.example.popularmovies.models.TrailersReviews;
import android.example.popularmovies.utils.JsonUtils;
import android.example.popularmovies.utils.NetworkUtils;
import android.example.popularmovies.viewmodels.DetailViewModel;
import android.example.popularmovies.viewmodels.DetailViewModelFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.Date;
import java.util.List;

public class DetailActivity extends AppCompatActivity
    implements TrailerAdapter.TrailerAdapterOnClickHandler{
    private static final String REVIEW_LIST_STATE_KEY = "review_list_state";

    private Movie mMovie;

    private ImageView mImageView;
    private TextView mTitleTextView;
    private TextView mReleaseDataTextView;
    private TextView mVotingTextView;
    private TextView mOverviewTextView;
    private Button mFavoriteButton;

    private FavoriteEntry mFavoriteEntity;
    private boolean isFavorite;

    private TrailerAdapter mTrailerAdapter;
    private RecyclerView mTrailerList;
    private ReviewAdapter mReviewAdapter;
    private RecyclerView mReviewList;

    private FavoriteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mDb = FavoriteDatabase.getInstance(getApplicationContext());

        mImageView = (ImageView) findViewById(R.id.tv_poster);
        mTitleTextView = (TextView) findViewById(R.id.tv_title);
        mReleaseDataTextView = (TextView) findViewById(R.id.tv_releasedate);
        mVotingTextView = (TextView) findViewById(R.id.tv_rating);
        mOverviewTextView = (TextView) findViewById(R.id.tv_overview);
        mFavoriteButton = (Button) findViewById(R.id.favorite_button);

        Intent intentStartActivity = getIntent();
        InsertData(intentStartActivity);

        //Trailer adapter
        mTrailerList = (RecyclerView) findViewById(R.id.tv_movie_trailer);
        //mTrailerList.setNestedScrollingEnabled(false);

        LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(this);
        mTrailerList.setLayoutManager(trailerLayoutManager);
        mTrailerList.setHasFixedSize(true);
        mTrailerAdapter = new TrailerAdapter(this);
        mTrailerList.setAdapter(mTrailerAdapter);

        //Review adapter
        mReviewList = (RecyclerView) findViewById(R.id.tv_movie_review);

        LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(this);
        mReviewList.setLayoutManager(reviewLayoutManager);
        mReviewList.setHasFixedSize(true);
        mReviewAdapter = new ReviewAdapter();
        mReviewList.setAdapter(mReviewAdapter);
    }

    private void InsertData(Intent intent){
        if(intent != null){
            mMovie = intent.getParcelableExtra("movie");
            mTitleTextView.setText(mMovie.getTitle());
            Picasso.get().load(mMovie.getImageUrl()).into(mImageView);
            mReleaseDataTextView.setText(getString(R.string.released_on));
            mReleaseDataTextView.append(mMovie.getReleaseData());

            mVotingTextView.setText(getString(R.string.rating_comment));
            mVotingTextView.append(mMovie.getVoteAverage());
            mVotingTextView.append(getString(R.string.rating_max));
            mOverviewTextView.setText(mMovie.getOverview());

            new FetchTrailerData().execute(mMovie.getId());
            DetailViewModelFactory factory = new DetailViewModelFactory(mDb, (Integer.parseInt(mMovie.getId())));
            final DetailViewModel viewModel = ViewModelProviders.of(this, factory).get(DetailViewModel.class);
            viewModel.getFavorite().observe(this, new Observer<FavoriteEntry>() {
                @Override
                public void onChanged(@Nullable FavoriteEntry favoriteEntry) {
                    if(favoriteEntry!=null){
                        mFavoriteButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_delete_favorite));
                        mFavoriteButton.setText(getString(R.string.button_delete_button));
                        mFavoriteButton.setTextColor(getResources().getColor(R.color.buttonColorFavoriteText));
                        isFavorite = true;
                    }else{
                        mFavoriteButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_insert_favorite));
                        mFavoriteButton.setText(getString(R.string.button_insert_button));
                        mFavoriteButton.setTextColor(getResources().getColor(R.color.buttonColorNotFavoriteText));
                        isFavorite = false;
                    }
                    mFavoriteEntity = favoriteEntry;
                }
            });

            mFavoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            String movieId = mMovie.getId();
                            String title = mMovie.getTitle();
                            String releaseDate = mMovie.getReleaseData();
                            String posterPath = mMovie.getPosterPath();
                            String backDropPath = mMovie.getBackDropPath();
                            String voteAverage = mMovie.getVoteAverage();
                            String overview = mMovie.getOverview();

                            Date updatedAt = new Date();
                            FavoriteEntry fEntry = new FavoriteEntry(Integer.parseInt(movieId), title,
                                    releaseDate, posterPath, backDropPath, voteAverage, overview, updatedAt);
                            if(isFavorite){
                                mDb.favoriteDao().deleteFavorite(mFavoriteEntity);
                            }else{
                                mDb.favoriteDao().insertFavorite(fEntry);
                            }
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onClick(Trailer trailer) {
        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailer.getUrl()));
        try{
            startActivity(youtubeIntent);
        }catch (ActivityNotFoundException ex){
            Log.d(DetailActivity.class.getName(), "Failed to activate video intent.");
        }
    }

    public class FetchTrailerData extends AsyncTask<String, Void, TrailersReviews> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected TrailersReviews doInBackground(String... params) {
            if (params.length == 0){
                return null;
            }
            String movieId = params[0];
            URL movieTrailerUrl = NetworkUtils.buildTrailerUrl(movieId);
            URL movieReviewUrl = NetworkUtils.buildRviewUrl(movieId);

            try{
                String jsonTrailerData = NetworkUtils.getResponseFromHttpUrl(movieTrailerUrl);
                String jsonReviewData = NetworkUtils.getResponseFromHttpUrl(movieReviewUrl);
                Trailer[] simpleJasonTrailerData = JsonUtils.getSimpleTrailerData(jsonTrailerData);
                Review[] simpleJsonReviewData = JsonUtils.getSimpleReviewData(jsonReviewData);
                return new TrailersReviews(simpleJasonTrailerData, simpleJsonReviewData);
            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(TrailersReviews simpleJasonData) {
            super.onPostExecute(simpleJasonData);
            mTrailerAdapter.setTrailerData(simpleJasonData.getTrailers());
            mTrailerAdapter.notifyDataSetChanged();

            mReviewAdapter.setRviewData(simpleJasonData.getReviews());
            mReviewAdapter.notifyDataSetChanged();
        }
    }
}
