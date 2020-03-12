package android.example.popularmovies.database;



import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "favorite")
public class FavoriteEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "movie_id")
    private int movieId;
    private String title;
    @ColumnInfo(name = "release_date")
    private String releaseDate;
    @ColumnInfo(name="poster_path")
    private String posterPath;
    @ColumnInfo(name="backdrop_path")
    private String backDropPath;
    @ColumnInfo(name = "vote_average")
    private String voteAverage;
    private String overview;

    @ColumnInfo(name = "updated_at")
    private Date updatedAt;

    public FavoriteEntry(int id, int movieId, String title,
                         String releaseDate, String posterPath,
                         String backDropPath, String voteAverage,
                         String overview, Date updatedAt){
        this.id = id;
        this.movieId = movieId;
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.backDropPath = backDropPath;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.updatedAt = updatedAt;
    }

    @Ignore
    public FavoriteEntry(int movieId, String title,
                         String releaseDate, String posterPath,
                         String backDropPath, String voteAverage,
                         String overview, Date updatedAt){
        this.movieId = movieId;
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.backDropPath = backDropPath;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.updatedAt = updatedAt;
    }

    public void setId(int id){ this.id = id;}

    public int getId(){ return id;}

    public void setMovieId(int movieId){ this.movieId = movieId;}

    public int getMovieId(){return movieId;}

    public void setTitle(String title){this.title=title;}

    public String getTitle(){return title;}

    public void setReleaseDate(String releaseDate){this.releaseDate=releaseDate;}

    public String getReleaseDate(){return releaseDate;}

    public void setPosterPath(String posterPath){this.posterPath = posterPath;}

    public String getPosterPath(){return posterPath;}

    public void setBackDropPath(String backDropPath){this.backDropPath = backDropPath;}

    public String getBackDropPath(){return backDropPath;}

    public void setVoteAverage(String voteAverage){this.voteAverage = voteAverage;}

    public String getVoteAverage(){return voteAverage;}

    public void setOverview(String overview){this.overview = overview;}

    public String getOverview(){return overview;}

    public void setUpdatedAt(Date updatedAt){this.updatedAt = updatedAt;}

    public Date getUpdatedAt(){return updatedAt;}
}
