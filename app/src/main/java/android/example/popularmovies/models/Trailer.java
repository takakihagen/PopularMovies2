package android.example.popularmovies.models;

public class Trailer {
    private String key;
    private String name;
    private final String url = "https://www.youtube.com/watch?v=";

    public Trailer(){
        this.key = null;
        this.name = null;
    }

    public Trailer(String key, String name){
        this.key = key;
        this.name = name;
    }

    public String getKey(){return key;}

    public String getName(){return name;}

    public String getUrl(){return url+getKey();}
}
