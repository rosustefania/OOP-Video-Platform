package queries_videos;

import common.Constants;
import queries_actors.MovieInputData;
import fileio.UserInputData;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Most_viewedMovies {
    private final List<UserInputData> users;

    private final List<MovieInputData> movies;

    private final int ID;

    private final int number;

    private final String sort_type;

    private final List<String> years;

    private final List<String> genres;

    public Most_viewedMovies(List<UserInputData> users, List<MovieInputData> movies,
                             int ID, int number, String sort_type,
                             List<String> years, List<String> genres) {
        this.users = users;
        this.movies = movies;
        this.ID = ID;
        this.number = number;
        this.sort_type = sort_type;
        this.years = years;
        this.genres = genres;
    }

    public JSONObject getMost_viewedMovies(){

        JSONObject object = new JSONObject();
        List<String> query = new ArrayList<>();

        // calculate views number for every movie;
        for (MovieInputData movie : movies){

            int views_number = 0;
            for (UserInputData user : users){

                Map<String, Integer> history = user.getHistory();

                for (Map.Entry<String, Integer> entry : history.entrySet()) {

                    if (entry.getKey().equalsIgnoreCase(movie.getTitle())){

                        views_number += entry.getValue();
                    }
                }
            }
            movie.setViews(views_number);
        }

        // sort movies' list ascendent by number of views;
        if (sort_type.equalsIgnoreCase("asc")){

            for (int i = 0; i < movies.size() - 1; i++){

                for (int j = 0; j < movies.size() - i - 1; j++){

                    if (movies.get(j).getViews() > movies.get(j + 1).getViews()){

                        Collections.swap(movies, j, j + 1);
                    }

                    // if two movies have the same number of views, sort them ascendent by name;
                    if ((movies.get(j).getViews() == movies.get(j + 1).getViews()) &&
                            (movies.get(j).getTitle().compareTo(movies.get(j + 1).getTitle()) > 0)){

                        Collections.swap(movies, j, j + 1);
                    }

                }
            }

            int count = 0;
            for (MovieInputData movie : movies) {

                if (count < number) {

                    // verify if the movie's number of views is 0;
                    if (movie.getViews() != 0) {

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
                            } else {

                                int genres_found = 0;
                                for (String genre : genres) {

                                    for (String movie_genre : movie.getGenres()) {

                                        if (genre.equalsIgnoreCase(movie_genre)) {

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
            }

            object.put(Constants.ID_STRING, this.ID);
            object.put(Constants.MESSAGE, "Query result: " + query);
            return object;
        }

        if (sort_type.equalsIgnoreCase("desc")){

            // sort movies' list descendent by number of views;
            for (int i = 0; i < movies.size() - 1; i++){

                for (int j = 0; j < movies.size() - i - 1; j++){

                    if (movies.get(j).getViews() < movies.get(j + 1).getViews()){

                        Collections.swap(movies, j, j + 1);
                    }

                    // if two movies have the same number of views, sort them descendent by name;
                    if ((movies.get(j).getViews() == movies.get(j + 1).getViews())
                            && (movies.get(j).getTitle().compareTo(movies.get(j + 1).getTitle()) < 0)){

                        Collections.swap(movies, j, j + 1);
                    }

                }
            }

            int count = 0;
            for (MovieInputData movie : movies) {

                if (count < number) {

                    // verify if the movie's number of views is 0;
                    if (movie.getViews() != 0) {

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
                            } else {

                                int genres_found = 0;
                                for (String genre : genres) {

                                    for (String movie_genre : movie.getGenres()) {

                                        if (genre.equalsIgnoreCase(movie_genre)) {

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
            }

            object.put(Constants.ID_STRING, this.ID);
            object.put(Constants.MESSAGE, "Query result: " + query);
            return object;
        }

        return object;
    }

}
