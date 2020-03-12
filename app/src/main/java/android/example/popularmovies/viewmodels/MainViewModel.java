package android.example.popularmovies.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.example.popularmovies.database.FavoriteDatabase;
import android.example.popularmovies.database.FavoriteEntry;
import android.support.annotation.NonNull;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<FavoriteEntry>> favorites;

    public MainViewModel(@NonNull Application application) {
        super(application);
        FavoriteDatabase database = FavoriteDatabase.getInstance(this.getApplication());
        favorites = database.favoriteDao().loadAllFavorites();
    }

    public LiveData<List<FavoriteEntry>> getFavorites() {
        return favorites;
    }
}
