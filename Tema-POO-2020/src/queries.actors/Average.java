package queries.actors;

import common.Constants;
import fileio.ActorInputData;
import fileio.MovieInputData;
import fileio.SerialInputData;
import mean.ActorMean;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Average {

  private final List<ActorInputData> actors;

  private final int id;

  private final int number;

  private final String sortType;

  private final List<MovieInputData> movies;

  private final List<SerialInputData> serials;

  public Average(final List<ActorInputData> actors, final int id, final int number,
                 final String sortType, final List<MovieInputData> movies,
                 final List<SerialInputData> serials) {
    this.actors = actors;
    this.id = id;
    this.number = number;
    this.sortType = sortType;
    this.movies = movies;
    this.serials = serials;
  }

  /** methods that adds in the query the first n actors based on their mean */
  public final JSONObject averageQuery() {

    ArrayList<String> query = new ArrayList<>();
    JSONObject object = new JSONObject();

    // the case where the sort type is ascedent;
    if (sortType.equalsIgnoreCase("asc")) {

      // sort actors by average rating of their filmography;
      for (int i = 0; i < actors.size() - 1; i++) {

        for (int j = 0; j < actors.size() - i - 1; j++) {

          ActorInputData actor1 = actors.get(j);
          ActorMean am1 = new ActorMean(actor1.getName(), actors, movies, serials);
          am1.mean(); // calculate average mean of the actor;

          ActorInputData actor2 = actors.get(j + 1);
          ActorMean am2 = new ActorMean(actor2.getName(), actors, movies, serials);
          am2.mean(); // calculate average mean of the actor;

          // if two actors have the same average ratings, sort them
          // ascendent by name;

          if (am1.getFilmographyMean().equals(am2.getFilmographyMean())) {

            if (actor1.getName().compareTo(actor2.getName()) > 0) {

              Collections.swap(actors, j, j + 1);
            }
          }

          if (am1.getFilmographyMean() > am2.getFilmographyMean()) {

            Collections.swap(actors, j, j + 1);
          }
        }
      }
    }

    // the case where the sort type is descedent;
    if (sortType.equalsIgnoreCase("desc")) {

      // sort actors by average rating of their filmography;
      for (int i = 0; i < actors.size() - 1; i++) {

        for (int j = 0; j < actors.size() - i - 1; j++) {

          ActorInputData actor1 = actors.get(j);

          ActorMean am1 = new ActorMean(actor1.getName(), actors, movies, serials);
          am1.mean(); // calculate average mean of the actor;

          ActorInputData actor2 = actors.get(j + 1);
          ActorMean am2 = new ActorMean(actor2.getName(), actors, movies, serials);
          am2.mean(); // calculate average mean of the actor;

          // if two actors have the same average ratings, sort them
          // descendent by name;
          if (am1.getFilmographyMean().equals(am2.getFilmographyMean())) {

            if (actor1.getName().compareTo(actor2.getName()) < 0) {

              Collections.swap(actors, j, j + 1);
            }
          }

          if (am1.getFilmographyMean() < am2.getFilmographyMean()) {

            Collections.swap(actors, j, j + 1);
          }
        }
      }
    }

    int count = 0;

    // ignore the actors that have the average rating 0, and add the first n
    // actors into the query;
    for (int k = 0; k < actors.size(); k++) {

      ActorMean am = new ActorMean(actors.get(k).getName(), actors, movies, serials);
      am.mean();

      if (count < number) {

        if (am.getFilmographyMean() != 0) {

          count++;
          query.add(actors.get(k).getName());
        }
      }
    }

    object.put(Constants.ID_STRING, this.id);
    object.put(Constants.MESSAGE, "Query result: " + query);
    return object;
  }
}
