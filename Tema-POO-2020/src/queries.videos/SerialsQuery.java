package queries.videos;

import common.Constants;
import fileio.SerialInputData;
import fileio.UserInputData;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SerialsQuery {
  private final List<UserInputData> users;

  private final List<SerialInputData> givenSerials;

  private final int id;

  private final String sortType;

  private final int number;

  private final List<String> years;

  private final List<String> genres;

  public SerialsQuery(final List<UserInputData> users, final List<SerialInputData> givenSerials,
                      final int id, final String sortType, final int number,
                      final List<String> years, final List<String> genres) {
    this.users = users;
    this.givenSerials = givenSerials;
    this.id = id;
    this.sortType = sortType;
    this.number = number;
    this.years = years;
    this.genres = genres;
  }

  /** methods that adds in the query the first n givenSerials based on their rating */
  public final JSONObject getRatingSerials() {
    JSONObject object = new JSONObject();
    List<String> query = new ArrayList<>();
    List<SerialInputData> sortedSerials = new ArrayList<>(givenSerials);

    // sort serials' list ascendent by average rating;
    if (sortType.equalsIgnoreCase("asc")) {
      for (int i = 0; i < sortedSerials.size() - 1; i++) {

        for (int j = 0; j < sortedSerials.size() - i - 1; j++) {

          // calculate serials' average rating;
          sortedSerials.get(j).mean();
          sortedSerials.get(j + 1).mean();

          if (sortedSerials.get(j).getRatingsMean() > sortedSerials.get(j + 1).getRatingsMean()) {

            Collections.swap(sortedSerials, j, j + 1);
          }

          // if two serials have the same average rating, sort tem ascendent by name;
          if ((sortedSerials.get(j).getRatingsMean().equals(sortedSerials.get(j + 1)
                  .getRatingsMean())) && (sortedSerials.get(j).getTitle()
                  .compareTo(sortedSerials.get(j + 1).getTitle()) > 0)) {

            Collections.swap(sortedSerials, j, j + 1);
          }
        }
      }
    }

    if (sortType.equalsIgnoreCase("desc")) {

      // sort serials' list descendent by average rating;
      for (int i = 0; i < sortedSerials.size() - 1; i++) {

        for (int j = 0; j < sortedSerials.size() - i - 1; j++) {

          // calculate serials' average rating;
          sortedSerials.get(j).mean();
          sortedSerials.get(j + 1).mean();

          if (sortedSerials.get(j).getRatingsMean() < sortedSerials.get(j + 1).getRatingsMean()) {

            Collections.swap(sortedSerials, j, j + 1);
          }

          // if two serials have the same average rating, sort tem descendent by name;
          if ((sortedSerials.get(j).getRatingsMean().equals(sortedSerials.get(j + 1)
                  .getRatingsMean())) && (sortedSerials.get(j).getTitle()
                  .compareTo(sortedSerials.get(j + 1).getTitle()) < 0)) {

            Collections.swap(sortedSerials, j, j + 1);
          }
        }
      }
    }

    int count = 0;
    for (SerialInputData serial : sortedSerials) {

      if (count < number) {

        serial.mean();

        // verify if the serial's average rating is 0;
        if (serial.getRatingsMean() != 0) {

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

  /** methods that adds in the query the first n movies based on the number of appereances in
   * favorites' lists */
  public final JSONObject getFavouriteSerials() {

    JSONObject object = new JSONObject();
    List<String> query = new ArrayList<>();
    List<SerialInputData> sortedSerials = new ArrayList<>(givenSerials);

    for (SerialInputData serial : sortedSerials) {

      int count = 0;
      String title = serial.getTitle();

      for (UserInputData user : users) {

        List<String> favourites = user.getFavoriteMovies();
        for (String favourite : favourites) {

          if (favourite.equalsIgnoreCase(title)) {

            count++;
            break;
          }
        }
      }

      serial.setFavouriteAppearences(count);
    }

    // sort serials' list ascendent by appearences' number;
    if (sortType.equalsIgnoreCase("asc")) {

      for (int i = 0; i < sortedSerials.size() - 1; i++) {

        for (int j = 0; j < sortedSerials.size() - i - 1; j++) {

          if (sortedSerials.get(j).getFavouriteAppearences()
                  > sortedSerials.get(j + 1).getFavouriteAppearences()) {

            Collections.swap(sortedSerials, j, j + 1);
          }

          // if two serials have the same appearences' number, sort them ascendent by name;
          if ((sortedSerials.get(j).getFavouriteAppearences()
                  == sortedSerials.get(j + 1).getFavouriteAppearences())
                  && (sortedSerials.get(j).getTitle().compareTo(sortedSerials.get(j + 1)
                  .getTitle()) > 0)) {

            Collections.swap(sortedSerials, j, j + 1);
          }
        }
      }
    }

    if (sortType.equalsIgnoreCase("desc")) {

      // sort serials' list descendent by appearences' number;
      for (int i = 0; i < sortedSerials.size() - 1; i++) {

        for (int j = 0; j < sortedSerials.size() - i - 1; j++) {

          if (sortedSerials.get(j).getFavouriteAppearences()
                  < sortedSerials.get(j + 1).getFavouriteAppearences()) {

            Collections.swap(sortedSerials, j, j + 1);
          }

          // if two serials have the same appearences' number, sort them descendent by name;
          if ((sortedSerials.get(j).getFavouriteAppearences()
                  == sortedSerials.get(j + 1).getFavouriteAppearences())
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

        // verify if the number of appearences in favourite lists is 0;
        if (serial.getFavouriteAppearences() != 0) {

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

  /** methods that adds in the query the first n givenSerials based on their duration */
  public final JSONObject getLongestSerials() {
    JSONObject object = new JSONObject();
    List<String> query = new ArrayList<>();
    List<SerialInputData> sortedSerials = new ArrayList<>(givenSerials);

    // calculate total duration for all of the givenSerials;
    for (SerialInputData serial : sortedSerials) {

      serial.calculateDuration();
    }

    // sort serials' list ascendent by duration;
    if (sortType.equalsIgnoreCase("asc")) {

      for (int i = 0; i < sortedSerials.size() - 1; i++) {

        for (int j = 0; j < sortedSerials.size() - i - 1; j++) {

          if (sortedSerials.get(j).getDuration() > sortedSerials.get(j + 1).getDuration()) {

            Collections.swap(sortedSerials, j, j + 1);
          }

          // if two serials have the same duration, sort them ascendent by name;
          if ((sortedSerials.get(j).getDuration() == sortedSerials.get(j + 1).getDuration())
                  && (sortedSerials.get(j).getTitle().compareTo(sortedSerials.get(j + 1)
                  .getTitle()) > 0)) {

            Collections.swap(sortedSerials, j, j + 1);
          }
        }
      }
    }

    if (sortType.equalsIgnoreCase("desc")) {

      // sort serials' list descendent by duration;
      for (int i = 0; i < sortedSerials.size() - 1; i++) {

        for (int j = 0; j < sortedSerials.size() - i - 1; j++) {

          if (sortedSerials.get(j).getDuration() < sortedSerials.get(j + 1).getDuration()) {

            Collections.swap(sortedSerials, j, j + 1);
          }

          // if two serials have the same duration, sort them descendent by name;
          if ((sortedSerials.get(j).getDuration() == sortedSerials.get(j + 1).getDuration())
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

    object.put(Constants.ID_STRING, this.id);
    object.put(Constants.MESSAGE, "Query result: " + query);
    return object;
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

