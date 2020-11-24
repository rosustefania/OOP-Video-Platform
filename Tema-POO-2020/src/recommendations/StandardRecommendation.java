package recommendations;

import common.Constants;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;
import org.json.JSONObject;

import java.util.List;

public class StandardRecommendation {
  private final List<UserInputData> users;

  private final List<MovieInputData> movies;

  private final List<SerialInputData> serials;

  private final int id;

  private final String username;

  // the user with the given username;
  private UserInputData givenUser;

  public StandardRecommendation(final List<UserInputData> users, final List<MovieInputData> movies,
                                final List<SerialInputData> serials, final int id,
                                final String username) {
    this.users = users;
    this.movies = movies;
    this.serials = serials;
    this.id = id;
    this.username = username;
  }

  /** methods that give the first unseen movie by an user */
  public final JSONObject getStandardRecommendation() {
    JSONObject object = new JSONObject();

    // find the user in the users list;
    for (UserInputData user : users) {

      if (user.getUsername().equalsIgnoreCase(username)) {

        givenUser = user;
      }
    }

    // first go through movies' list and see if there's any the user hasn't seen;
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
        object.put(Constants.MESSAGE, "StandardRecommendation result: " + movie.getTitle());
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
        object.put(Constants.MESSAGE, "StandardRecommendation result: " + serial.getTitle());
        return object;
      }
    }

    object.put(Constants.ID_STRING, this.id);
    object.put(Constants.MESSAGE, "StandardRecommendation cannot be applied!");
    return object;
  }
}
