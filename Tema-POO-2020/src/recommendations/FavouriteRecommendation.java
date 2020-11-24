package recommendations;

import common.Constants;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class FavouriteRecommendation {
    private final List<UserInputData> users;

    private final List<MovieInputData> movies;

    private final List<SerialInputData> serials;

    private final int id;

    private final String username;

    // the user with the given username;
    private UserInputData givenUser;

    public FavouriteRecommendation(final List<UserInputData> users,
                                   final List<MovieInputData> movies,
                                   final List<SerialInputData> serials, final int id,
                                   final String username) {
        this.users = users;
        this.movies = movies;
        this.serials = serials;
        this.id = id;
        this.username = username;
    }

    /** methods that give the first unseen show based on
     * appearences in the favourites lists*/
    public final JSONObject getFavouriteRecommendation() {
        JSONObject object = new JSONObject();

        // map that store favourites shows and their number of appearences;
        Map<String, Integer> favouritesShows = new HashMap<>();

        for (UserInputData user : users) {

            // find the user in the users list;
            if (user.getUsername().equalsIgnoreCase(username)) {

                givenUser = user;
            }

            // search in every user's favourites list and calculate the
            // number of appearences for every show;
            for (String favourite : user.getFavoriteMovies()) {

                boolean flag = false;
                // verify if the genre is already in the map;
                for (Map.Entry<String, Integer> entryFavourite : favouritesShows.entrySet()) {

                    if (favourite.equalsIgnoreCase(entryFavourite.getKey())) {

                        flag = true;
                        // if it's in the list, grow number of views;
                        entryFavourite.setValue(entryFavourite.getValue() + 1);
                        break;
                    }
                }

                //if it isn't, then add it
                if (!flag) {

                    favouritesShows.put(favourite, 1);
                }
            }
        }

        // verify if the user is valid;
        if (givenUser.getSubscriptionType().equalsIgnoreCase("BASIC")) {

            object.put(Constants.ID_STRING, this.id);
            object.put(Constants.MESSAGE, "FavoriteRecommendation cannot be applied!");
            return object;
        }

        // sort the list descendent by number of appearences;
        Map<String, Integer> favouritesShowsSorted = favouritesShows
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,
                        LinkedHashMap::new));

        // get the most added show in users' favourites lists;
        for (String favouriteShow : favouritesShowsSorted.keySet()) {

            boolean flag = false;
            for (String seenMovie : givenUser.getHistory().keySet()) {

                if (seenMovie.equalsIgnoreCase(favouriteShow)) {

                    flag = true;
                    break;
                }
            }

            // found one movie that isn't seen;
            if (!flag) {

                object.put(Constants.ID_STRING, this.id);
                object.put(Constants.MESSAGE, "FavoriteRecommendation result: " + favouriteShow);
                return object;
            }

        }

        // if there isn't any that is unseen, search in the database;
        for (MovieInputData movie : movies) {

            boolean flag = false;
            for (String seenMovie : givenUser.getHistory().keySet()) {

                if (seenMovie.equalsIgnoreCase(movie.getTitle())) {

                    flag = true;
                    break;
                }
            }

            // found one movie that isn't seen;
            if (!flag) {

                object.put(Constants.ID_STRING, this.id);
                object.put(Constants.MESSAGE, "FavoriteRecommendation result: " + movie.getTitle());
                return object;
            }
        }

        // if it hasn't found an unseen movie, then go through serials' list and
        // see if there's any the user hasn't seen;
        for (SerialInputData serial : serials) {

            boolean flag = false;
            for (String seenSerial : givenUser.getHistory().keySet()) {

                if (seenSerial.equalsIgnoreCase(serial.getTitle())) {

                    flag = true;
                    break;
                }
            }

            // found one serial that isn't seen;
            if (!flag) {

                object.put(Constants.ID_STRING, this.id);
                object.put(Constants.MESSAGE, "FavoriteRecommendation result: "
                        + serial.getTitle());
                return object;
            }
        }

        object.put(Constants.ID_STRING, this.id);
        object.put(Constants.MESSAGE, "FavoriteRecommendation cannot be applied!");
        return object;
    }
}
