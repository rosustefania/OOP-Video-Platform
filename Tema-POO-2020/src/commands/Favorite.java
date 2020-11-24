package commands;

import common.Constants;
import fileio.UserInputData;
import fileio.Writer;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class Favorite {
  private final List<UserInputData> users;

  private final String user;

  private final String title;

  private final int id;

  public Favorite(
      final List<UserInputData> users, final String user, final String title, final int id) {
    this.users = users;
    this.user = user;
    this.title = title;
    this.id = id;
  }

  public List<UserInputData> getUsers() {
    return users;
  }

  public String getUser() {
    return user;
  }

  public String getTitle() {
    return title;
  }

  public int getID() {
    return id;
  }

  /** method that adds a video to favorites; */
  public JSONObject addToFavourites() {
    JSONObject object = new JSONObject();
    for (UserInputData u : users) {

      // search for user;
      if (u.getUsername().equalsIgnoreCase(user)) {

        Map<String, Integer> history = u.getHistory();
        ArrayList<String> favoriteMovies = u.getFavoriteMovies();

        // the case where the show it's already in the favourite list;
        for (String movie : favoriteMovies) {

          if (movie.equalsIgnoreCase(title)) {
            object.put(Constants.ID_STRING, this.id);
            object.put(
                Constants.MESSAGE, "error -> " + this.title + " is already in favourite list");
            return object;
          }
        }

        // verify if the show is seen, so it can be put in the favourite
        // list;
        for (String name : history.keySet()) {

          if (name.equalsIgnoreCase(title)) {

            favoriteMovies.add(name);
            object.put(Constants.ID_STRING, this.id);
            object.put(Constants.MESSAGE, "success -> " + this.title + " was added as favourite");
            return object;
          }
        }

        // the case where the show is not seen;
        object.put(Constants.ID_STRING, this.id);
        object.put(Constants.MESSAGE, "error -> " + this.title + " is not seen");
        return object;
      }
    }
    return object;
  }
}
