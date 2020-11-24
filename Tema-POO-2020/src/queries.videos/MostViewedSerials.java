package queries.videos;

import common.Constants;
import fileio.SerialInputData;
import fileio.UserInputData;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MostViewedSerials {
  private final List<UserInputData> users;

  private final List<SerialInputData> givenSerials;

  private final int id;

  private final int number;

  private final String sortType;

  private final List<String> years;

  private final List<String> genres;

  public MostViewedSerials(final List<UserInputData> users,
                           final List<SerialInputData> givenSerials,
                           final int id, final int number, final String sortType,
                           final List<String> years, final List<String> genres) {
    this.users = users;
    this.givenSerials = givenSerials;
    this.id = id;
    this.number = number;
    this.sortType = sortType;
    this.years = years;
    this.genres = genres;
  }

  /** methods that adds in the query the first n givenSerials based on their views */
  public final JSONObject getMostViewedSerials() {
    JSONObject object = new JSONObject();
    List<String> query = new ArrayList<>();
    List<SerialInputData> sortedSerials = new ArrayList<>(givenSerials);

    // calculate views number for every serial;
    for (SerialInputData serial : sortedSerials) {

      int viewsNumber = 0;
      for (UserInputData user : users) {

        Map<String, Integer> history = user.getHistory();

        for (Map.Entry<String, Integer> entry : history.entrySet()) {

          if (entry.getKey().equalsIgnoreCase(serial.getTitle())) {

            viewsNumber += entry.getValue();
          }
        }
      }
      serial.setViews(viewsNumber);
    }

    // sort serials' list ascendent by views;
    if (sortType.equalsIgnoreCase("asc")) {

      for (int i = 0; i < sortedSerials.size() - 1; i++) {

        for (int j = 0; j < sortedSerials.size() - i - 1; j++) {

          if (sortedSerials.get(j).getViews() > sortedSerials.get(j + 1).getViews()) {

            Collections.swap(sortedSerials, j, j + 1);
          }

          // if two serials have the same number of views, sort them ascendent by name;
          if ((sortedSerials.get(j).getViews() == sortedSerials.get(j + 1).getViews())
              && (sortedSerials.get(j).getTitle().compareTo(sortedSerials.get(j + 1)
                  .getTitle()) > 0)) {

            Collections.swap(sortedSerials, j, j + 1);
          }
        }
      }
    }

    // sort serials' list descendent by views;
    if (sortType.equalsIgnoreCase("desc")) {

      for (int i = 0; i < sortedSerials.size() - 1; i++) {

        for (int j = 0; j < sortedSerials.size() - i - 1; j++) {

          if (sortedSerials.get(j).getViews() < sortedSerials.get(j + 1).getViews()) {

            Collections.swap(sortedSerials, j, j + 1);
          }

          // if two serials have the same number of views, sort them descendent by name;
          if ((sortedSerials.get(j).getViews() == sortedSerials.get(j + 1).getViews())
                  && (sortedSerials.get(j).getTitle().compareTo(sortedSerials.get(j + 1)
                  .getTitle()) < 0)) {

            Collections.swap(sortedSerials, j, j + 1);
          }
        }
      }
    }

    int count = 0;
    for (SerialInputData serial : sortedSerials) {

      if (count < number) {

        // verify if number of views is 0;
        if (serial.getViews() != 0) {

          boolean flag1 = true;
          boolean flag2 = true;

          // apply year filter;
          if (years.get(0) != null) {

            flag1 = false;
            String yearString = String.valueOf(serial.getYear());
            for (String year : years) {

              if (year.equalsIgnoreCase(yearString)) {

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

              int genresFound = 0;
              for (String genre : genres) {

                for (String serialGenre : serial.getGenres()) {

                  if (genre.equalsIgnoreCase(serialGenre)) {

                    genresFound++;
                  }
                }
              }

              if (genresFound != genres.size()) {

                flag2 = false;
              }
            }
          }

          if (flag1 && flag2) {

            query.add(serial.getTitle());
            count++;
          }
        }
      }
    }

    object.put(Constants.ID_STRING, this.id);
    object.put(Constants.MESSAGE, "Query result: " + query);
    return object;
  }
}
