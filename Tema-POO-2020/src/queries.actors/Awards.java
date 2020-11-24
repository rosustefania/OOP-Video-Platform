package queries.actors;

import actor.ActorsAwards;
import common.Constants;
import fileio.ActorInputData;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Awards {

  private final List<ActorInputData> actors;

  private final int id;

  private final String sortType;

  private final List<String> awards;

  public Awards(final List<ActorInputData> actors, final int id, final String sortType,
                final List<String> awards) {
    this.actors = actors;
    this.id = id;
    this.sortType = sortType;
    this.awards = awards;
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
}
