package fileio;

import java.util.ArrayList;
import java.util.List;

/**
 * Information about a movie, retrieved from parsing the input test files
 * <p>
 * DO NOT MODIFY
 */
public final class MovieInputData extends ShowInput {
    /**
     * Duration in minutes of a season
     */
    private final int duration;

    private final List<Double> ratings = new ArrayList<>();

    // Movie's average rating;
    private Double ratings_mean;

    private int favourite_appeareances;

    private int views;

    public MovieInputData(final String title, final ArrayList<String> cast,
                          final ArrayList<String> genres, final int year,
                          final int duration) {
        super(title, year, cast, genres);
        this.duration = duration;
        this.ratings_mean = 0.0;
        this.favourite_appeareances = 0;
        this.views = 0;
    }

    public int getDuration() {
        return duration;
    }

    public List<Double> getRatings() {
        return ratings;
    }

    public Double getRatings_mean() {
        return ratings_mean;
    }

    public int getFavourite_appeareances() {
        return favourite_appeareances;
    }

    public void setFavourite_appeareances(int favourite_appereances) {
        this.favourite_appeareances = favourite_appereances;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    @Override
    public String toString() {
        return "MovieInputData{" + "title= "
                + super.getTitle() + "year= "
                + super.getYear() + "duration= "
                + duration + "cast {"
                + super.getCast() + " }\n"
                + "genres {" + super.getGenres() + " }\n ";
    }

    // calculate the average rating of the movie
    public void mean(){
        if (!ratings.isEmpty()){
            Double sum = 0.0;

            for (Double rating : ratings) {

                sum += rating;
            }

            ratings_mean = sum / ratings.size();
        }
    }

}
