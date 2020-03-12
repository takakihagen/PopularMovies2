package android.example.popularmovies.models;

public class TrailersReviews {
    private Trailer[] trailers;
    private Review[] reviews;

    public TrailersReviews(Trailer[] trailers, Review[] reviews){
        this.trailers = trailers;
        this.reviews = reviews;
    }

    public Review[] getReviews() {
        return reviews;
    }

    public Trailer[] getTrailers() {
        return trailers;
    }
}
