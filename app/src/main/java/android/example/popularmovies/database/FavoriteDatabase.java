package android.example.popularmovies.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;


@Database(entities = {FavoriteEntry.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class FavoriteDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "favoritelist";
    private static FavoriteDatabase sInstance;

    public static FavoriteDatabase getInstance(Context context){
        if(sInstance == null){
            Log.e(FavoriteDatabase.class.getName(), "************************8");
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        FavoriteDatabase.class, FavoriteDatabase.DATABASE_NAME)
                        .build();
            }
        }
        return sInstance;
    }

    public abstract FavoriteDao favoriteDao();
}
