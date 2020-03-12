package android.example.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.lifecycle.ViewModelStore;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.example.popularmovies.database.FavoriteDatabase;
import android.example.popularmovies.database.FavoriteEntry;
import android.example.popularmovies.utils.JsonUtils;
import android.example.popularmovies.utils.NetworkUtils;
import android.example.popularmovies.models.Movie;

import android.example.popularmovies.viewmodels.MainViewModel;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements MovieAdapter.MovieAdapterOnClickHandler, SharedPreferences.OnSharedPreferenceChangeListener{
    private static final int NUM_LIST_ITEMS = 20;
    private static final String LIST_STATE_KEY = "list_state";

    private MovieAdapter mAdapter;
    private RecyclerView mPosterList;
    private GridLayoutManager mLayoutManager;
    private int mScrollPosition;

    private String mOrder;//popular or top_rated

    private ProgressBar mProgressBar;

    private FavoriteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDb = FavoriteDatabase.getInstance(getApplicationContext());

        mPosterList = (RecyclerView) findViewById(R.id.tv_movie_poster);
        mProgressBar = (ProgressBar) findViewById(R.id.pd_load_movie_data);

        mLayoutManager = new GridLayoutManager(this, 2);

        mPosterList.setLayoutManager(mLayoutManager);
        mPosterList.setHasFixedSize(true);
        mAdapter = new MovieAdapter(NUM_LIST_ITEMS, null, this);
        mAdapter.notifyDataSetChanged();
        mPosterList.setAdapter(mAdapter);

        changeOrderOfMovies();
        setUpData();
    }

    private void setUpData(){
        //LiveData<List<FavoriteEntry>> favoriteEntries = mDb.favoriteDao().loadAllFavorites();
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getFavorites().observe(this, new Observer<List<FavoriteEntry>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteEntry> favoriteEntries) {
                Movie[] movies = null;
                if(favoriteEntries!=null){
                    movies = new Movie[favoriteEntries.size()];
                    for(int i=0; i<favoriteEntries.size(); i++){
                        movies[i] = Movie.getMovieFromFavoriteEntry(favoriteEntries.get(i));
                    }
                }
                mAdapter.setFavoriteMovieListData(movies);
                if(mOrder.equals(getString(R.string.pref_value_order_favorite))) {
                    populateFavorite(movies);
                }
            }
        });

    }

    private void changeOrderOfMovies(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mOrder = sharedPreferences.getString(getString(R.string.pref_order_key), getString(R.string.pref_value_order_popular));
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        if(mOrder.equals(getString(R.string.pref_value_order_favorite))){
            Movie[] favoriteMovies = mAdapter.getFavoriteMovieListData();
            populateFavorite(favoriteMovies);
        }else {
            new FetchMovieData().execute(mOrder);
        }
    }

    private void populateFavorite(Movie[] movies){
        mAdapter.setMovieData(movies);
        mAdapter.setItemCount(movies!=null?movies.length:0);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(Movie movie) {
        Context context = this;
        Class destinatoinClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinatoinClass);

        intentToStartDetailActivity.putExtra("movie", movie);
        startActivity(intentToStartDetailActivity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.settings){
            Intent startSettingActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_order_key))){
            changeOrderOfMovies();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int position = mLayoutManager.findFirstVisibleItemPosition();
        outState.putInt(LIST_STATE_KEY, position);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState!=null){
            mScrollPosition = savedInstanceState.getInt(LIST_STATE_KEY);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Display display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        if(rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
            mLayoutManager.setSpanCount(5);
        }
    }

    public class FetchMovieData extends AsyncTask<String, Void, Movie[]>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mPosterList.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie[] doInBackground(String... params) {
            if (params.length == 0){
                return null;
            }
            String movieOrder = params[0];
            URL movieRequestUrl = NetworkUtils.buildUrl(movieOrder);
            try {
                String jsonMovieData = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                Movie[] simpleJasonMovieData = JsonUtils.getSimpleMovieData(jsonMovieData);

                return simpleJasonMovieData;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie[] simpleJasonMovieData) {
            super.onPostExecute(simpleJasonMovieData);
            mAdapter.setMovieData(simpleJasonMovieData);
            mAdapter.setItemCount(simpleJasonMovieData!=null?simpleJasonMovieData.length:0);
            mAdapter.notifyDataSetChanged();
            mPosterList.scrollToPosition(mScrollPosition);
            if(simpleJasonMovieData!=null) {
                mPosterList.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        }
    }
}
