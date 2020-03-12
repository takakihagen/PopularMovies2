package android.example.popularmovies.utils;

import android.example.popularmovies.models.Movie;
import android.example.popularmovies.models.Review;
import android.example.popularmovies.models.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JsonUtils {
    public static Movie[] getSimpleMovieData(String movieDataJasonStr) throws JSONException{
        JSONObject movieJson = new JSONObject(movieDataJasonStr);

        final String OWN_RESULT = "results";

        JSONArray movieArray = movieJson.getJSONArray(OWN_RESULT);
        Movie[] parseMovieData = new Movie[movieArray.length()];
        for (int i=0; i<movieArray.length(); i++){
            JSONObject movieDetail = movieArray.getJSONObject(i);
            String id = movieDetail.getString("id");
            String title = movieDetail.getString("title");
            String releaseData = movieDetail.getString("release_date");
            String posterPath = movieDetail.getString("poster_path");
            String backDropPath = movieDetail.getString("backdrop_path");
            String voteAverage = movieDetail.getString("vote_average");
            String overview = movieDetail.getString("overview");
            parseMovieData[i] = new Movie(id, title, releaseData, posterPath, backDropPath, voteAverage, overview);
        }

        return parseMovieData;
    }

    public static Trailer[] getSimpleTrailerData(String movieDataJasonStr) throws JSONException{
        JSONObject trailerJson = new JSONObject(movieDataJasonStr);

        final String OWN_RESULT = "results";

        JSONArray trailerArray = trailerJson.getJSONArray(OWN_RESULT);
        Trailer[] parseTrailerData = new Trailer[trailerArray.length()];
        for (int i=0; i<trailerArray.length(); i++){
            JSONObject trailerDetail = trailerArray.getJSONObject(i);
            String key = trailerDetail.getString("key");
            String name = trailerDetail.getString("name");
            parseTrailerData[i] = new Trailer(key, name);
        }

        return parseTrailerData;
    }

    public static Review[] getSimpleReviewData(String movieDataJasonStr) throws JSONException{
        JSONObject reviewJson = new JSONObject(movieDataJasonStr);

        final String OWN_RESULT = "results";

        JSONArray reviewArray = reviewJson.getJSONArray(OWN_RESULT);
        Review[] parseTrailerData = new Review[reviewArray.length()];
        for (int i=0; i<reviewArray.length(); i++){
            JSONObject trailerDetail = reviewArray.getJSONObject(i);
            String author = trailerDetail.getString("author");
            String content = trailerDetail.getString("content");
            parseTrailerData[i] = new Review(author, content);
        }

        return parseTrailerData;
    }
}
