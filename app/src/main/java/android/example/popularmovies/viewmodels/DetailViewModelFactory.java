package android.example.popularmovies.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.example.popularmovies.database.FavoriteDatabase;
import android.support.annotation.NonNull;

public class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final FavoriteDatabase mDb;
    private final int mMovieId;

    public DetailViewModelFactory(FavoriteDatabase favoriteDatabase, int movieId){
        mDb = favoriteDatabase;
        mMovieId = movieId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailViewModel(mDb,  mMovieId);
    }
}
