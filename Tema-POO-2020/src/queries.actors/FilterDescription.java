package queries.actors;

import common.Constants;
import fileio.ActorInputData;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FilterDescription {
  private final List<ActorInputData> actors;

  private final int id;

  private final String sortType;

  private final List<String> words;

  public FilterDescription(final List<ActorInputData> actors, final int id, final String sortType,
                           final List<String> words) {
    this.actors = actors;
    this.id = id;
    this.sortType = sortType;
    this.words = words;
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
