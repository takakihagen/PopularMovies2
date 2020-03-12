package android.example.popularmovies.models;

import android.example.popularmovies.database.FavoriteEntry;
import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private final static String IMAGE_URL = "http://image.tmdb.org/t/p/w185/";
    private String id;
    private String title;
    private String releaseData;
    private String posterPath;
    private String backDropPath;
    private String voteAverage;
    private String overview;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(releaseData);
        dest.writeString(posterPath);
        dest.writeString(backDropPath);
        dest.writeString(voteAverage);
        dest.writeString(overview);
    }

    private Movie(Parcel in){
        this.id = in.readString();
        this.title = in.readString();
        this.releaseData = in.readString();
        this.posterPath = in.readString();
        this.backDropPath = in.readString();
        this.voteAverage = in.readString();
        this.overview = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        public Movie[] newArray(int size){
            return new Movie[size];
        }
    };

    public Movie(String id, String title, String releaseData, String posterPath, String backDropPath, String voteAverage, String overview){
        this.id = id;
        this.title = title;
        this.releaseData = releaseData;
        this.posterPath = posterPath;
        this.backDropPath = backDropPath;
        this.voteAverage = voteAverage;
        this.overview = overview;
    }

    public Movie(){
        this.id = null;
        this.title = null;
        this.releaseData = null;
        this.posterPath = null;
        this.backDropPath = null;
        this.voteAverage = null;
        this.overview = null;
    }

    public String getId(){ return id; }

    public String getTitle(){ return title; }

    public String getReleaseData(){ return releaseData; }

    public String getVoteAverage(){ return voteAverage; }

    public String getOverview(){ return overview; }

    public String getPosterPath(){return  posterPath;}

    public String getBackDropPath(){return backDropPath;}

    public String getImageUrl(){
        String url = IMAGE_URL + posterPath;
        return url;
    }

    public String getBackDropUrl(){
        String url = IMAGE_URL + backDropPath;
        return url;
    }

    public static Movie getMovieFromFavoriteEntry(FavoriteEntry entry){
        String movieId = String.valueOf(entry.getMovieId());
        String title = entry.getTitle();
        String releaseDate = entry.getReleaseDate();
        String posterPath = entry.getPosterPath();
        String backDropPath = entry.getBackDropPath();
        String voteAverage = entry.getVoteAverage();
        String overview = entry.getOverview();
        return new Movie(movieId, title, releaseDate,
                posterPath, backDropPath, voteAverage, overview);
    }
}
