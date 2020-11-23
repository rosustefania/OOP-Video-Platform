package queries;

import common.Constants;
import fileio.MovieInputData;
import fileio.UserInputData;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FavouriteMovies {
    private final List<UserInputData> users;

    private final List<MovieInputData> movies;

    private final int ID;

    private final String sort_type;

    private final int number;

    private final List<String> years;

    private final List<String> genres;

    public FavouriteMovies(List<UserInputData> users, List<MovieInputData> movies,
                            int ID, String sort_type, int number, List<String> years,
                            List<String> genres) {
        this.users = users;
        this.movies = movies;
        this.ID = ID;
        this.sort_type = sort_type;
        this.number = number;
        this.years = years;
        this.genres = genres;
    }

    public JSONObject getFavouriteMovies(){

        JSONObject object = new JSONObject();
        List<String> query = new ArrayList<>();

        for (MovieInputData movie : movies){

            int count = 0;
            String title = movie.getTitle();

            for (UserInputData user : users){

                List<String> favourites = user.getFavoriteMovies();
                for (String favourite : favourites){

                    if (favourite.equalsIgnoreCase(title)){

                        count++ ;
                        break;
                    }
                }
            }

            movie.setFavourite_appeareances(count);

        }

        // sort movies' list ascendent by average rating;
        if (sort_type.equalsIgnoreCase("asc")){
            for (int i = 0; i < movies.size() - 1; i++){

                for (int j = 0; j < movies.size() - i - 1; j++){

                    if (movies.get(j).getFavourite_appeareances() >
                            movies.get(j + 1).getFavourite_appeareances()){

                        Collections.swap(movies, j, j + 1);
                    }

                    // if two movies have the same average rating, sort tem ascendent by name;
                    if ((movies.get(j).getFavourite_appeareances() == movies.get(j + 1).getFavourite_appeareances())
                            && (movies.get(j).getTitle().compareTo(movies.get(j + 1).getTitle()) > 0)){

                        Collections.swap(movies, j, j + 1);
                    }

                }
            }

            int count = 0;
            for (MovieInputData movie : movies) {

                if (count < number) {

                    // verify if the movie's average rating is 0;
                    if (movie.getFavourite_appeareances() != 0) {

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

            // sort movies' list descendent by average rating;
            for (int i = 0; i < movies.size() - 1; i++){

                for (int j = 0; j < movies.size() - i - 1; j++){

                    if (movies.get(j).getFavourite_appeareances() <
                            movies.get(j + 1).getFavourite_appeareances()){

                        Collections.swap(movies, j, j + 1);
                    }

                    // if two movies have the same average rating, sort tem descendent by name;
                    if ((movies.get(j).getFavourite_appeareances() == movies.get(j + 1).getFavourite_appeareances())
                            && (movies.get(j).getTitle().compareTo(movies.get(j + 1).getTitle()) < 0)){

                        Collections.swap(movies, j, j + 1);
                    }

                }
            }

            int count = 0;
            for (MovieInputData movie : movies) {

                if (count < number) {

                    if (movie.getFavourite_appeareances() != 0) {

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
