package fileio;

import entertainment.Season;

import java.util.ArrayList;

/**
 * Information about a tv show, retrieved from parsing the input test files
 * <p>
 * DO NOT MODIFY
 */
public final class SerialInputData extends ShowInput {
    /**
     * Number of seasons
     */
    private final int numberOfSeasons;
    /**
     * Season list
     */
    private final ArrayList<Season> seasons;

    // Serial's average rating
    private Double ratings_mean;

    private int favourite_appearences;

    public SerialInputData(final String title, final ArrayList<String> cast,
                           final ArrayList<String> genres,
                           final int numberOfSeasons, final ArrayList<Season> seasons,
                           final int year) {
        super(title, year, cast, genres);
        this.numberOfSeasons = numberOfSeasons;
        this.seasons = seasons;
        this.ratings_mean = 0.0;
        this.favourite_appearences = 0;
    }

    public int getNumberSeason() {
        return numberOfSeasons;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    public Double getRatings_mean() {
        return ratings_mean;
    }

    public int getFavourite_appearences() {
        return favourite_appearences;
    }

    public void setFavourite_appearences(int favourite_appearences) {
        this.favourite_appearences = favourite_appearences;
    }

    // calculate the average rating of the serial;
    public void mean(){
        double sum = 0.0;

        for (Season season : seasons){

            season.mean();
            Double season_mean = season.getRatings_mean();

            if(season_mean != 0){

                sum += season_mean;
            }
        }

        if (sum != 0){

            ratings_mean = sum / numberOfSeasons;
        }
    }

    @Override
    public String toString() {
        return "SerialInputData{" + " title= "
                + super.getTitle() + " " + " year= "
                + super.getYear() + " cast {"
                + super.getCast() + " }\n" + " genres {"
                + super.getGenres() + " }\n "
                + " numberSeason= " + numberOfSeasons
                + ", seasons=" + seasons + "\n\n" + '}';
    }
}
