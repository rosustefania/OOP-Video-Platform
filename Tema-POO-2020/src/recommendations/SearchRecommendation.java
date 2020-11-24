package recommendations;

import common.Constants;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class SearchRecommendation {
    private final List<UserInputData> users;

    private final List<MovieInputData> movies;

    private final List<SerialInputData> serials;

    private final int id;

    private final String username;

    private final String givenGenre;

    // the user with the given username;
    private UserInputData givenUser;

    public  SearchRecommendation(final List<UserInputData> users,
                                      final List<MovieInputData> movies,
                                      final List<SerialInputData> serials, final int id,
                                      final String username, final String givenGenre) {
        this.users = users;
        this.movies = movies;
        this.serials = serials;
        this.id = id;
        this.username = username;
        this.givenGenre = givenGenre;
    }

    /** methods that give all the unseen shows that
     * have a given genre, ascendent by their rating */
    public final JSONObject getSearchRecommendation() {
        JSONObject object = new JSONObject();

        // map that store unseen shows and their rating;
        Map<String, Double> searchShows = new HashMap<>();

        // find the user in the users list;
        for (UserInputData user : users) {

            if (user.getUsername().equalsIgnoreCase(username)) {

                givenUser = user;
            }
        }

        // verify if the user is valid;
        if (givenUser.getSubscriptionType().equalsIgnoreCase("BASIC")) {

            object.put(Constants.ID_STRING, this.id);
            object.put(Constants.MESSAGE, "SearchRecommendation cannot be applied!");
            return object;
        }

        // search for unseen movies that have the given genre and add them in the map;
        for (MovieInputData movie : movies) {

            boolean flag = false;
            for (String seenMovie : givenUser.getHistory().keySet()) {

                if (seenMovie.equalsIgnoreCase(movie.getTitle())) {

                    flag = true;
                    break;
                }
            }

            // the movie isn't seen, check its genre;
            if (!flag) {

                for (String genre : movie.getGenres()) {

                    // if the movie have the given genre add it to the map;
                    if (genre.equalsIgnoreCase(givenGenre)) {

                        movie.mean(); // calculate movie rating;
                        searchShows.put(movie.getTitle(), movie.getRatingsMean());
                    }
                }
            }
        }

        // search for unseen serials that have the given genre and add them in the map;
        for (SerialInputData serial : serials) {

            boolean flag = false;
            for (String seenSerial : givenUser.getHistory().keySet()) {

                if (seenSerial.equalsIgnoreCase(serial.getTitle())) {

                    flag = true;
                    break;
                }
            }

            // the serial isn't seen, check its genre;
            if (!flag) {

                for (String genre : serial.getGenres()) {

                    // if the serial have the given genre add it to the map;
                    if (genre.equalsIgnoreCase(givenGenre)) {

                        serial.mean(); // calculate serial rating;
                        searchShows.put(serial.getTitle(), serial.getRatingsMean());
                    }
                }
            }
        }

        // if the map is empty, then the recommendation cannot be applied;
        if (searchShows.isEmpty()) {

            object.put(Constants.ID_STRING, this.id);
            object.put(Constants.MESSAGE, "SearchRecommendation cannot be applied!");
            return object;
        }

        // sort the list ascendent by rating;
        Map<String, Double> searchShowsSorted = new TreeMap<>(searchShows);

        object.put(Constants.ID_STRING, this.id);
        object.put(Constants.MESSAGE, "SearchRecommendation result: " + searchShowsSorted.keySet());
        return object;
    }
}
