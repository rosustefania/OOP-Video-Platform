package queries.users;

import common.Constants;
import fileio.UserInputData;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NumberOfRatings {
  private final List<UserInputData> users;

  private final int id;

  private final int number;

  private final String sortType;

  public NumberOfRatings(final List<UserInputData> users, final int id, final int number,
                         final String sortType) {
    this.users = users;
    this.id = id;
    this.number = number;
    this.sortType = sortType;
  }

  /** methods that adds in the query the first n users based on the number
   * of ratings they have given */
  public final JSONObject getNumberOfRatings() {
    JSONObject object = new JSONObject();
    List<String> query = new ArrayList<>();

    // calculate total number of ratings given by an user;
    for (UserInputData user : users) {

      user.setGivenRatings(user.getMoviesRatings().size() + user.getSerialsRatings().size());
    }

    // sort users ascendent by number of given ratings;
    if (sortType.equalsIgnoreCase("asc")) {

      for (int i = 0; i < users.size() - 1; i++) {

        for (int j = 0; j < users.size() - i - 1; j++) {

          if (users.get(j).getGivenRatings() > users.get(j + 1).getGivenRatings()) {

            Collections.swap(users, j, j + 1);
          }

          // if two users have the same number of given ratings, sort them ascendent by name;
          if (users.get(j).getGivenRatings() == users.get(j + 1).getGivenRatings()
              && users.get(j).getUsername().compareTo(users.get(j + 1).getUsername()) > 0) {

            Collections.swap(users, j, j + 1);
          }
        }
      }
    }

    // sort users descendent by number of given ratings;
    if (sortType.equalsIgnoreCase("desc")) {

      for (int i = 0; i < users.size() - 1; i++) {

        for (int j = 0; j < users.size() - i - 1; j++) {

          if (users.get(j).getGivenRatings() < users.get(j + 1).getGivenRatings()) {

            Collections.swap(users, j, j + 1);
          }

          // if two users have the same number of given ratings, sort them descendent by name;
          if (users.get(j).getGivenRatings() == users.get(j + 1).getGivenRatings()
              && users.get(j).getUsername().compareTo(users.get(j + 1).getUsername()) < 0) {

            Collections.swap(users, j, j + 1);
          }
        }
      }
    }
    int count = 0;
    for (UserInputData user : users) {

      if (count < number) {

        if (user.getGivenRatings() != 0) {

          count++;
          query.add(user.getUsername());
        }
      }
    }

    object.put(Constants.ID_STRING, this.id);
    object.put(Constants.MESSAGE, "Query result: " + query);
    return object;
  }
}
