package android.example.popularmovies.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.example.popularmovies.database.FavoriteDatabase;
import android.example.popularmovies.database.FavoriteEntry;

public class DetailViewModel extends ViewModel {

    private LiveData<FavoriteEntry> favorite;

    public DetailViewModel(FavoriteDatabase database, int id) {
        favorite = database.favoriteDao().loadFavoriteById(id);
    }

    public LiveData<FavoriteEntry> getFavorite() {
        return favorite;
    }
}
