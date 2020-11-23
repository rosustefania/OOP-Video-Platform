package queries;

import common.Constants;
import fileio.MovieInputData;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LongestMovies {
    private final List<MovieInputData> movies;

    private final int ID;

    private final int number;

    private final String sort_type;

    private final List<String> years;

    private final List<String> genres;

    public LongestMovies(List<MovieInputData> movies, int ID, int number, String sort_type, List<String> years, List<String> genres) {
        this.movies = movies;
        this.ID = ID;
        this.number = number;
        this.sort_type = sort_type;
        this.years = years;
        this.genres = genres;
    }

    public JSONObject getLongestMovies(){
        JSONObject object = new JSONObject();
        List<String> query = new ArrayList<>();

        // sort movies' list ascendent by duration;
        if (sort_type.equalsIgnoreCase("asc")){

            for (int i = 0; i < movies.size() - 1; i++){

                for (int j = 0; j < movies.size() - i - 1; j++){

                    if (movies.get(j).getDuration() > movies.get(j + 1).getDuration()){

                        Collections.swap(movies, j, j + 1);
                    }

                    // if two movies have the same duration, sort them ascendent by name;
                    if ((movies.get(j).getDuration() == movies.get(j + 1).getDuration())
                            && (movies.get(j).getTitle().compareTo(movies.get(j + 1).getTitle()) > 0)){

                        Collections.swap(movies, j, j + 1);
                    }

                }
            }

            int count = 0;
            for (MovieInputData movie : movies) {

                if (count < number) {

                    boolean flag1 = true;
                    boolean flag2 = true;

                    // apply year filter;
                    if (years.get(0) != null) {

                        flag1 = false;
                        String year_string = String.valueOf(movie.getYear());
                        for (String year : years) {

                            if (year.equalsIgnoreCase(year_string)) {

                                flag1 = true;
                                break;
                            }
                        }
                    }

                    // apply genres filter;
                    if (genres.get(0) != null) {

                        if (!flag1) {

                            flag2 = false;
                        }
                        else {

                            int genres_found = 0;
                            for (String genre : genres) {

                                for (String serial_genre : movie.getGenres()) {

                                    if (genre.equalsIgnoreCase(serial_genre)) {

                                        genres_found++;
                                    }
                                }
                            }

                            if (genres_found != genres.size()) {

                                flag2 = false;
                            }
                        }
                    }

                    if (flag1 && flag2) {

                        query.add(movie.getTitle());
                        count++;
                    }
                }
            }

            object.put(Constants.ID_STRING, this.ID);
            object.put(Constants.MESSAGE, "Query result: " + query);
            return object;
        }

        if (sort_type.equalsIgnoreCase("desc")){

            // sort movies' list descendent by duration;
            for (int i = 0; i < movies.size() - 1; i++){

                for (int j = 0; j < movies.size() - i - 1; j++){

                    if (movies.get(j).getDuration() <
                            movies.get(j + 1).getDuration()){

                        Collections.swap(movies, j, j + 1);
                    }

                    // if two movies have the same duration, sort them descendent by name;
                    if ((movies.get(j).getDuration() == movies.get(j + 1).getDuration())
                            && (movies.get(j).getTitle().compareTo(movies.get(j + 1).getTitle()) < 0)){

                        Collections.swap(movies, j, j + 1);
                    }

                }
            }

            int count = 0;
            for (MovieInputData serial : movies) {

                if (count < number) {

                    boolean flag1 = true;
                    boolean flag2 = true;

                    // apply year filter;
                    if (years.get(0) != null) {

                        flag1 = false;
                        String year_string = String.valueOf(serial.getYear());
                        for (String year : years) {

                            if (year.equalsIgnoreCase(year_string)) {

                                flag1 = true;
                                break;
                            }
                        }
                    }

                    // apply genres filter;
                    if (genres.get(0) != null) {

                        if (!flag1) {

                            flag2 = false;
                        }
                        else {

                            int genres_found = 0;
                            for (String genre : genres) {

                                for (String serial_genre : serial.getGenres()) {

                                    if (genre.equalsIgnoreCase(serial_genre)) {

                                        genres_found++;
                                    }
                                }
                            }

                            if (genres_found != genres.size()) {

                                flag2 = false;
                            }
                        }
                    }

                    if (flag1 && flag2) {

                        query.add(serial.getTitle());
                        count++;
                    }
                }
            }

            object.put(Constants.ID_STRING, this.ID);
            object.put(Constants.MESSAGE, "Query result: " + query);
            return object;
        }

        object.put(Constants.ID_STRING, this.ID);
        object.put(Constants.MESSAGE, "Query result: " + query);
        return object;
    }
}
