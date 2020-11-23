package queries_users;

import common.Constants;
import fileio.UserInputData;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NumberOfRatings {
    private final List<UserInputData> users;

    private final int Id;

    private final int number;

    private final String sort_type;

    public NumberOfRatings(List<UserInputData> users, int id, int number, String sort_type) {
        this.users = users;
        Id = id;
        this.number = number;
        this.sort_type = sort_type;
    }

    public JSONObject getNumberOfRatings(){
        JSONObject object = new JSONObject();
        List<String> query = new ArrayList<>();

        // calculate total number of ratings given by an user;
        for (UserInputData user : users){

            user.setGiven_ratings(user.getMoviesRatings().size() +
                    user.getSerialsRatings().size());
        }

        // sort users ascendent by number of given ratings;
        if (sort_type.equalsIgnoreCase("asc")){

            for(int i = 0; i < users.size() - 1; i++){

                for (int j = 0; j < users.size() - i - 1; j++){

                    if (users.get(j).getGiven_ratings() > users.get(j + 1).getGiven_ratings()){

                        Collections.swap(users, j, j + 1);
                    }

                    // if two users have the same number of given ratings, sort them ascendent by name;
                    if (users.get(j).getGiven_ratings() == users.get(j + 1).getGiven_ratings() &&
                            users.get(j).getUsername().compareTo(users.get(j + 1).getUsername()) > 0){

                        Collections.swap(users, j, j + 1);
                    }
                }
            }
        }

        // sort users descendent by number of given ratings;
        if (sort_type.equalsIgnoreCase("desc")){

            for(int i = 0; i < users.size() - 1; i++){

                for (int j = 0; j < users.size() - i - 1; j++){

                    if (users.get(j).getGiven_ratings() < users.get(j + 1).getGiven_ratings()){

                        Collections.swap(users, j, j + 1);
                    }

                    // if two users have the same number of given ratings, sort them descendent by name;
                    if (users.get(j).getGiven_ratings() == users.get(j + 1).getGiven_ratings() &&
                            users.get(j).getUsername().compareTo(users.get(j + 1).getUsername()) < 0){

                        Collections.swap(users, j, j + 1);
                    }
                }
            }
        }
        int count = 0;
        for (UserInputData user : users){

            if (count < number){

                if (user.getGiven_ratings() != 0){

                    count++ ;
                    query.add(user.getUsername());
                }
            }
        }

        object.put(Constants.ID_STRING, this.Id);
        object.put(Constants.MESSAGE, "Query result: " + query);
        return object;
    }

}
