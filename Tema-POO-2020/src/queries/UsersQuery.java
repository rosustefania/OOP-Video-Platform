package queries;

import common.Constants;
import fileio.UserInputData;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UsersQuery {
  private final List<UserInputData> users;

  private final int id;

  private final int number;

  private final String sortType;

  public UsersQuery(final List<UserInputData> users, final int id, final int number,
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

      users.sort(Comparator.comparing(UserInputData::getGivenRatings).
              thenComparing(UserInputData::getUsername));
    }

    // sort users descendent by number of given ratings;
    if (sortType.equalsIgnoreCase("desc")) {

      users.sort(Comparator.comparing(UserInputData::getGivenRatings).
              thenComparing(UserInputData::getUsername));
      Collections.reverse(users);
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
