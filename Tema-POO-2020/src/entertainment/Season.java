package entertainment;

import fileio.UserInputData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Information about a season of a tv show
 * <p>
 * DO NOT MODIFY
 */
public final class Season {
    /**
     * Number of current season
     */
    private final int currentSeason;
    /**
     * Duration in minutes of a season
     */
    private int duration;
    /**
     * List of ratings for each season
     */
    private List<Double> ratings;

    // Season's average rating;
    private Double ratings_mean;

    public Season(final int currentSeason, final int duration) {
        this.currentSeason = currentSeason;
        this.duration = duration;
        this.ratings = new ArrayList<>();
        this.ratings_mean = 0.0;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(final int duration) {
        this.duration = duration;
    }

    public List<Double> getRatings() {
        return ratings;
    }

    public void setRatings(final List<Double> ratings) {
        this.ratings = ratings;
    }

    public Double getRatings_mean() {
        return ratings_mean;
    }

    // calculate the average rating of the season
    public void mean(){
        if (!ratings.isEmpty()){
            Double sum = 0.0;

            for (int i = 0; i < ratings.size(); i++){

                sum += ratings.get(i);
            }

            ratings_mean = sum / ratings.size();
        }
    }


    @Override
    public String toString() {
        return "Episode{"
                + "currentSeason="
                + currentSeason
                + ", duration="
                + duration
                + '}';
    }
}

