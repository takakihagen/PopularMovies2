package android.example.popularmovies.models;

public class Review {
    private String author;
    private String content;

    public Review(String author, String content){
        this.author = author;
        this.content = content;
    }

    public Review(){
        this.author = null;
        this.content = null;
    }

    public String getAuthor(){return author;}
    public String getContent(){return content;}
}
