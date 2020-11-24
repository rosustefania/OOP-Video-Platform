package commands;

import common.Constants;
import fileio.UserInputData;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public final class View {
  private final List<UserInputData> users;

  private final String user;

  private final String title;

  private final int id;

  public View(
      final List<UserInputData> users, final String user, final String title, final int id) {
    this.users = users;
    this.user = user;
    this.title = title;
    this.id = id;
  }

  /** method that mark a video as viewed; */
  public JSONObject markAsViewed() {
    JSONObject object = new JSONObject();

    // search for user;
    for (UserInputData u : users) {

      if (u.getUsername().equalsIgnoreCase(user)) {

        Map<String, Integer> history = u.getHistory();

        // if the show is already seen, grow views number;
        for (Map.Entry<String, Integer> entry : history.entrySet()) {

          if (entry.getKey().equalsIgnoreCase(title)) {

            history.get(entry.setValue(entry.getValue() + 1));
            object.put(Constants.ID_STRING, this.id);
            object.put(
                Constants.MESSAGE,
                "success -> " + this.title + " was viewed with total views of " + entry.getValue());
            return object;
          }
        }

        // if it's seen for the first time, add it in the history list
        // with no_views = 1;
        history.put(title, 1);
        object.put(Constants.ID_STRING, this.id);
        object.put(
            Constants.MESSAGE, "success -> " + this.title + " was viewed with total views of 1");
        return object;
      }
    }
    return object;
  }
}
