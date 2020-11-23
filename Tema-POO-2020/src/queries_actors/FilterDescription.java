package queries_actors;

import common.Constants;
import fileio.ActorInputData;
import org.json.JSONObject;

import java.util.*;

public class FilterDescription {
    private final List<ActorInputData> actors;

    private final int ID;

    private final String sort_type;

    private final List<String> words;

    public FilterDescription(List<ActorInputData> actors, int ID, String sort_type, List<String> words) {
        this.actors = actors;
        this.ID = ID;
        this.sort_type = sort_type;
        this.words = words;
    }

    public JSONObject filter_description(){

        ArrayList<String> query = new ArrayList<>();
        JSONObject object = new JSONObject();

        for (ActorInputData actor : actors){

            // number of words found in the actor's description;
            int words_found = 0;

            // calculate number of words from given list that find in the actor's
            // description;
            for (String word_from_list : words){

                String word_case1 = " " + word_from_list + " ";
                String word_case2 = " " + word_from_list + ".";
                String word_case3 = " " + word_from_list + ",";
                String word_case4 = "." + word_from_list + " ";
                String word_case5 = "-" + word_from_list + " ";
                String word_case6 = word_from_list + " ";


                if (actor.getCareerDescription().toLowerCase().contains(word_case1.toLowerCase())
                    || actor.getCareerDescription().toLowerCase().contains(word_case2.toLowerCase())
                    || actor.getCareerDescription().toLowerCase().contains(word_case3.toLowerCase())
                    || actor.getCareerDescription().toLowerCase().contains(word_case4.toLowerCase())
                    || actor.getCareerDescription().toLowerCase().contains(word_case5.toLowerCase())
                    || actor.getCareerDescription().toLowerCase().contains(word_case6.toLowerCase())){

                    words_found++;
                }

            }

            // verify if all the words from given list are found in the actor's
            // description;
            if (words_found == words.size()){

                query.add(actor.getName());
            }
        }

        if (sort_type.equalsIgnoreCase("asc")){

            // sort actors by number of the awards won;
            for (int i = 0; i < query.size() - 1; i++){

                for (int j = 0; j < query.size() - i - 1; j++){

                    if (query.get(j).compareTo(query.get(j + 1)) > 0){

                            Collections.swap(query, j, j + 1);
                    }
                }
            }
        }

        if (sort_type.equalsIgnoreCase("desc")){

            // sort actors by number of the awards won;
            for (int i = 0; i < query.size() - 1; i++){

                for (int j = 0; j < query.size() - i - 1; j++){

                    if (query.get(j).compareTo(query.get(j + 1)) < 0){

                        Collections.swap(query, j, j + 1);
                    }
                }
            }
        }

        object.put(Constants.ID_STRING, this.ID);
        object.put(Constants.MESSAGE, "Query result: " + query);
        return object;

    }
}
