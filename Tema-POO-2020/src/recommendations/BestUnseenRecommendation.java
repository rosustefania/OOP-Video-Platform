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

public class BestUnseenRecommendation {
    private final List<UserInputData> users;

    private final List<MovieInputData> movies;

    private final List<SerialInputData> serials;

    private final int id;

    private final String username;

    // the user with the given username;
    private UserInputData givenUser;

    public BestUnseenRecommendation(final List<UserInputData> users,
                                    final List<MovieInputData> movies,
                                    final List<SerialInputData> serials, final int id,
                                    final String username) {
        this.users = users;
        this.movies = movies;
        this.serials = serials;
        this.id = id;
        this.username = username;
    }

    /** methods that give the best unseen movie by an user */
    public final JSONObject getBestUnseenRecommendation() {
        JSONObject object = new JSONObject();

        // list that store all the shows that have a rating
        Map<String, Double> bestShowsUnsorted = new HashMap<>();
        Map<String, Double> bestShowsSorted;

        // find the user in the users list;
        for (UserInputData user : users) {

            if (user.getUsername().equalsIgnoreCase(username)) {

                givenUser = user;
            }
        }

        // add all the movies that have rating in the list
        for (MovieInputData movie : movies) {

            movie.mean();
            if (movie.getRatingsMean() != 0) {
                bestShowsUnsorted.put(movie.getTitle(), movie.getRatingsMean());
            }
        }

        // add all the serials that have rating in the list
        for (SerialInputData serial : serials) {

            serial.mean();
            if (serial.getRatingsMean() != 0) {
                bestShowsUnsorted.put(serial.getTitle(), serial.getRatingsMean());
            }
        }

        // sort the list descendent by ratings;
        bestShowsSorted = bestShowsUnsorted
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,
                        LinkedHashMap::new));

        // search in the sorted list to see if there's any show that's unseen;
        for (String title : bestShowsSorted.keySet()) {

            boolean flag = false;
            for (String seenTitle : givenUser.getHistory().keySet()) {

                if (seenTitle.equalsIgnoreCase(title)) {
                    flag = true;
                    break;
                }
            }

            // found one show that isn't seen;
            if (!flag) {

                object.put(Constants.ID_STRING, this.id);
                object.put(Constants.MESSAGE, "BestRatedUnseenRecommendation result: " + title);
                return object;
            }
        }

        // if all the best shows are seen, search in the database and find the first one that isn't
        // seen;
        // first go through movies' list and see if there's any the user hasn't seen;
        for (MovieInputData movie : movies) {

            boolean flag = false;
            for (String seenMovie : givenUser.getHistory().keySet()) {

                if (seenMovie.equalsIgnoreCase(movie.getTitle())) {

                    //System.out.println(movie.getTitle());
                    flag = true;
                    break;
                }
            }

            // found one movie that isn't seen;
            if (!flag) {

                object.put(Constants.ID_STRING, this.id);
                object.put(Constants.MESSAGE, "BestRatedUnseenRecommendation result: "
                        + movie.getTitle());
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
                object.put(Constants.MESSAGE, "BestRatedUnseenRecommendation result: "
                        + serial.getTitle());
                return object;
            }
        }

        object.put(Constants.ID_STRING, this.id);
        object.put(Constants.MESSAGE, "BestRatedUnseenRecommendation cannot be applied!");
        return object;

    }


}
