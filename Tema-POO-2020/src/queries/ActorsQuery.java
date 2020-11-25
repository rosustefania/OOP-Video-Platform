package queries;

import actor.ActorsAwards;
import common.Constants;
import fileio.ActorInputData;
import fileio.MovieInputData;
import fileio.SerialInputData;
import mean.ActorMean;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ActorsQuery {

  private final List<ActorInputData> actors;

  private final int id;

  private final int number;

  private final String sortType;

  private final List<MovieInputData> movies;

  private final List<SerialInputData> serials;

  private final List<String> awards;

  private final List<String> words;
  public ActorsQuery(final List<ActorInputData> actors, final int id, final int number,
                     final String sortType, final List<MovieInputData> movies,
                     final List<SerialInputData> serials, final List<String> awards,
                     final List<String> words) {
    this.actors = actors;
    this.id = id;
    this.number = number;
    this.sortType = sortType;
    this.movies = movies;
    this.serials = serials;
    this.awards = awards;
    this.words = words;
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

  /** method that adds in the query the first n actors based on the awards */
  public JSONObject awardsQuery() {

    ArrayList<String> query = new ArrayList<>();
    JSONObject object = new JSONObject();

    // list that will store the actors who have won all of the query's awards;
    List<ActorInputData> haveAll = new ArrayList<>();

    for (ActorInputData actor : actors) {

      // number of awards from given list that the actor has won;
      int awardsFound = 0;
      // total number of awards that the actor has won;
      int awardsNumber = 0;

      // calculate number of awards won from given list;
      for (String awardFromList : awards) {

        Map<ActorsAwards, Integer> awardsWon = actor.getAwards();

        for (Map.Entry<ActorsAwards, Integer> entry : awardsWon.entrySet()) {

          if (awardFromList.equalsIgnoreCase(entry.getKey().name())) {

            awardsFound++;
          }
        }
      }

      // verify if all the awards from given list are won;
      if (awardsFound == awards.size()) {

        Map<ActorsAwards, Integer> awardsWon = actor.getAwards();

        // calculate total number of awards won by the actor;
        for (Map.Entry<ActorsAwards, Integer> entry : awardsWon.entrySet()) {

          awardsNumber += entry.getValue();
        }

        haveAll.add(actor);
        actor.setAwardsWon(awardsNumber);
      }
    }

    // the case where the sort type is ascedent;
    if (sortType.equalsIgnoreCase("asc")) {

      // sort actors by number of the awards won;
      for (int i = 0; i < haveAll.size() - 1; i++) {

        for (int j = 0; j < haveAll.size() - i - 1; j++) {

          if (haveAll.get(j).getAwardsWon() > haveAll.get(j + 1).getAwardsWon()) {

            Collections.swap(haveAll, j, j + 1);
          }

          // if two actors have the same number of awards won, sort them ascendent by name;
          if (haveAll.get(j).getAwardsWon() == haveAll.get(j + 1).getAwardsWon()) {

            if (haveAll.get(j).getName().compareTo(haveAll.get(j + 1).getName()) > 0) {

              Collections.swap(haveAll, j, j + 1);
            }
          }
        }
      }
    }

    // the case where the sort type is descedent;
    if (sortType.equalsIgnoreCase("desc")) {

      // sort actors by number of the awards won;
      for (int i = 0; i < haveAll.size() - 1; i++) {

        for (int j = 0; j < haveAll.size() - i - 1; j++) {

          if (haveAll.get(j).getAwardsWon() < haveAll.get(j + 1).getAwardsWon()) {

            Collections.swap(haveAll, j, j + 1);
          }

          // if two actors have the same number of awards won, sort them descendent by name;
          if (haveAll.get(j).getAwardsWon() == haveAll.get(j + 1).getAwardsWon()) {

            if (haveAll.get(j).getName().compareTo(haveAll.get(j + 1).getName()) < 0) {

              Collections.swap(haveAll, j, j + 1);
            }
          }
        }
      }
    }

    // put the actors name in the query;
    for (ActorInputData actorInputData : haveAll) {

      query.add(actorInputData.getName());
    }

    object.put(Constants.ID_STRING, this.id);
    object.put(Constants.MESSAGE, "Query result: " + query);
    return object;
  }

  /** methods that adds in the query the first n actors based on their description */
  public final JSONObject filterDescription() {

    ArrayList<String> query = new ArrayList<>();
    JSONObject object = new JSONObject();

    for (ActorInputData actor : actors) {

      // number of words found in the actor's description;
      int wordsFound = 0;

      //split the description into words;
      String[] text = actor.getCareerDescription().split("\\W+");

      // calculate number of words from given list that find in the actor's
      // description;
      for (String wordFromList : words) {

        for (String wordFromText : text) {

          if (wordFromList.equalsIgnoreCase(wordFromText)) {

            wordsFound++;
            break;
          }
        }
      }

      // verify if all the words from given list are found in the actor's
      // description;
      if (wordsFound == words.size()) {

        query.add(actor.getName());
      }
    }

    if (sortType.equalsIgnoreCase("asc")) {

      // sort actors by number of the awards won;
      for (int i = 0; i < query.size() - 1; i++) {

        for (int j = 0; j < query.size() - i - 1; j++) {

          if (query.get(j).compareTo(query.get(j + 1)) > 0) {

            Collections.swap(query, j, j + 1);
          }
        }
      }
    }

    if (sortType.equalsIgnoreCase("desc")) {

      // sort actors by number of the awards won;
      for (int i = 0; i < query.size() - 1; i++) {

        for (int j = 0; j < query.size() - i - 1; j++) {

          if (query.get(j).compareTo(query.get(j + 1)) < 0) {

            Collections.swap(query, j, j + 1);
          }
        }
      }
    }

    object.put(Constants.ID_STRING, this.id);
    object.put(Constants.MESSAGE, "Query result: " + query);
    return object;
  }
}
