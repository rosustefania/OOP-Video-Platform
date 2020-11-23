package queries;

import common.Constants;
import fileio.ActorInputData;
import fileio.MovieInputData;
import fileio.SerialInputData;
import mean.ActorMean;
import org.json.JSONObject;

import java.util.*;

public class Average{

    private final List<ActorInputData> actors;

    private final int ID;

    private final int number;

    private final String sort_type;

    private final List<MovieInputData> movies;

    private final List<SerialInputData> serials;

    public Average(List<ActorInputData> actors, final int ID, final int number,
                   String sort_type, List<MovieInputData> movies,
                   List<SerialInputData> serials){
        this.actors = actors;
        this.ID = ID;
        this.number = number;
        this.sort_type = sort_type;
        this.movies = movies;
        this.serials = serials;
    }

    public JSONObject average_query(){

        ArrayList<String> query = new ArrayList<>();
        JSONObject object = new JSONObject();

        // the case where the sort type is ascedent;
        if(sort_type.equalsIgnoreCase("asc")){

            // sort actors by average rating of their filmography;
            for(int i = 0; i < actors.size() - 1; i++){

                for (int j = 0; j < actors.size() - i - 1; j++){

                    ActorInputData actor1 = actors.get(j);
                    ActorMean am1 = new ActorMean(actor1.getName(),
                            actors, movies, serials);
                    am1.mean(); // calculate average mean of the actor;

                    ActorInputData actor2 = actors.get(j + 1);
                    ActorMean am2 = new ActorMean(actor2.getName(),
                            actors, movies, serials);
                    am2.mean(); // calculate average mean of the actor;

                    // if two actors have the same average ratings, sort them
                    // ascendent by name;

                    if (am1.getFilmography_mean().equals(am2.getFilmography_mean())){

                        if (actor1.getName().compareTo(actor2.getName()) > 0){

                            Collections.swap(actors, j, j + 1);
                        }
                    }

                   if (am1.getFilmography_mean() > am2.getFilmography_mean()){

                       Collections.swap(actors, j, j + 1);
                    }

                }
            }

            int count = 0;

            // ignore the actors that have the average rating 0, and add the first n
            // actors into the query;
            for (int k = 0; k < actors.size(); k++){

                ActorMean am =
                        new ActorMean(actors.get(k).getName(), actors, movies, serials);

                am.mean();

                if (count < number){

                    if (am.getFilmography_mean() != 0){

                        count ++;
                        query.add(actors.get(k).getName());
                    }
                }

            }

            object.put(Constants.ID_STRING, this.ID);
            object.put(Constants.MESSAGE, "Query result: " + query);
            return object;
        }

        // the case where the sort type is descedent;
        if(sort_type.equalsIgnoreCase("desc")){

            // sort actors by average rating of their filmography;
            for(int i = 0; i < actors.size() - 1; i++){

                for (int j = 0; j < actors.size() - i - 1; j++){

                    ActorInputData actor1 = actors.get(j);

                    ActorMean am1 = new ActorMean(actor1.getName(),
                            actors, movies, serials);
                    am1.mean(); // calculate average mean of the actor;

                    ActorInputData actor2 = actors.get(j + 1);
                    ActorMean am2 = new ActorMean(actor2.getName(),
                            actors, movies, serials);
                    am2.mean(); // calculate average mean of the actor;

                    // if two actors have the same average ratings, sort them
                    // descendent by name;
                    if (am1.getFilmography_mean().equals(am2.getFilmography_mean())){

                        if (actor1.getName().compareTo(actor2.getName()) < 0){

                            Collections.swap(actors, j, j + 1);
                        }
                    }

                    if (am1.getFilmography_mean() < am2.getFilmography_mean()){

                        Collections.swap(actors, j, j + 1);
                    }

                }
            }

            // add the first n actors into the query;
            for (int k = 0; k < number; k++) {

                query.add(actors.get(k).getName());

            }

            object.put(Constants.ID_STRING, this.ID);
            object.put(Constants.MESSAGE, "Query result: " + query);
            return object;
        }



        return object;
    }

}
