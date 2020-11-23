package queries_actors;

import actor.ActorsAwards;
import common.Constants;
import fileio.ActorInputData;
import org.json.JSONObject;

import java.util.*;

public class Awards {

    private final List<ActorInputData> actors;

    private final int ID;

    private final String sort_type;

    private final List<String> awards;

    public Awards(List<ActorInputData> actors, int ID, String sort_type,
                  List<String> awards) {
        this.actors = actors;
        this.ID = ID;
        this.sort_type = sort_type;
        this.awards = awards;
    }

    public JSONObject awards_query() {

        ArrayList<String> query = new ArrayList<>();
        JSONObject object = new JSONObject();

        // list that will store the actors who have won all of the query's awards;
        List<ActorInputData> have_all = new ArrayList<>();

        for (ActorInputData actor : actors){

            // number of awards from given list that the actor has won;
            int awards_found = 0;
            // total number of awards that the actor has won;
            int awards_number = 0;

            // calculate number of awards won from given list;
            for (String award_from_list : awards){

                Map<ActorsAwards, Integer> awards_won = actor.getAwards();

                for (Map.Entry<ActorsAwards, Integer> entry : awards_won.entrySet()) {

                    if (award_from_list.equalsIgnoreCase(entry.getKey().name())) {

                            awards_found++;
                    }

                }
            }

            // verify if all the awards from given list are won;
            if (awards_found == awards.size()){

                Map<ActorsAwards, Integer> awards_won = actor.getAwards();

                // calculate total number of awards won by the actor;
                for (Map.Entry<ActorsAwards, Integer> entry : awards_won.entrySet()) {

                    awards_number += entry.getValue();
                }

                have_all.add(actor);
                actor.setAwards_won(awards_number);
            }


        }

        // the case where the sort type is ascedent;
        if (sort_type.equalsIgnoreCase("asc")){

            // sort actors by number of the awards won;
            for (int i = 0; i < have_all.size() - 1; i++){

                for (int j = 0; j < have_all.size() - i - 1; j++){

                    if (have_all.get(j).getAwards_won() > have_all.get(j + 1).getAwards_won()){

                        Collections.swap(have_all, j, j + 1);
                    }

                    // if two actors have the same number of awards won, sort them ascendent by name;
                    if (have_all.get(j).getAwards_won() == have_all.get(j + 1).getAwards_won()){

                        if (have_all.get(j).getName().compareTo(have_all.get(j + 1).getName()) > 0){

                            Collections.swap(have_all, j, j + 1);
                        }
                    }
                }
            }
        }

        // the case where the sort type is descedent;
        if (sort_type.equalsIgnoreCase("desc")){

            // sort actors by number of the awards won;
            for (int i = 0; i < have_all.size() - 1; i++){

                for (int j = 0; j < have_all.size() - i - 1; j++){

                    if (have_all.get(j).getAwards_won() < have_all.get(j + 1).getAwards_won()){

                        Collections.swap(have_all, j, j + 1);
                    }

                    // if two actors have the same number of awards won, sort them descendent by name;
                    if (have_all.get(j).getAwards_won() == have_all.get(j + 1).getAwards_won()){

                        if (have_all.get(j).getName().compareTo(have_all.get(j + 1).getName()) < 0){

                            Collections.swap(have_all, j, j + 1);
                        }
                    }
                }
            }
        }

        // put the actors name in the query;
        for (ActorInputData actorInputData : have_all) {

            query.add(actorInputData.getName());
        }

        object.put(Constants.ID_STRING, this.ID);
        object.put(Constants.MESSAGE, "Query result: " + query);
        return object;
    }
}
