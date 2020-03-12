package android.example.popularmovies.utils;

import android.example.popularmovies.BuildConfig;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Scanner;

public final class NetworkUtils {
    private static final String MOVIE_API_URL = "http://api.themoviedb.org/3/movie";
    private static final String apiKey = BuildConfig.MY_API_KEY;
    private static String language = "en-US";

    //Query
    private static String API_KEY_PARAM = "api_key";
    private static String LANGUAGE_PARAM = "language";

    //Path
    private static final String TRAILERS_PATH = "videos";
    private static final String REVIEWS_PATH = "reviews";

    public static URL buildUrl(String movieOrder){
        Uri buildUri = Uri.parse(MOVIE_API_URL).buildUpon()
                .appendPath(movieOrder)
                .appendQueryParameter(API_KEY_PARAM, apiKey)
                .appendQueryParameter(LANGUAGE_PARAM, language)
                .build();

        URL url = null;
        try{
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
        }

        Log.v(NetworkUtils.class.getName(), "Built URI " + url);

        return url;
    }

    private static URL buildPathUrl(String id, String path){
        Uri buildUri = Uri.parse(MOVIE_API_URL).buildUpon()
                .appendPath(id)
                .appendPath(path)
                .appendQueryParameter(API_KEY_PARAM, apiKey)
                .appendQueryParameter(LANGUAGE_PARAM, language)
                .build();

        URL url = null;
        try{
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
        }

        Log.v(NetworkUtils.class.getName(), "Built URI " + url);

        return url;
    }

    public static URL buildTrailerUrl(String id){return buildPathUrl(id, TRAILERS_PATH);}

    public static URL buildRviewUrl(String id){return buildPathUrl(id, REVIEWS_PATH);}

    public static String getResponseFromHttpUrl(URL url) throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try{
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput){
                return scanner.next();
            }else{
                return null;
            }
        }finally {
            urlConnection.disconnect();
        }
    }
}
