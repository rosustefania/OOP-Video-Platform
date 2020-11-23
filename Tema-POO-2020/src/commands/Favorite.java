package commands;

import common.Constants;
import fileio.UserInputData;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Favorite {
    private final List<UserInputData> users;

    private final String user;

    private final String title;

    private final int ID;

    public Favorite(List<UserInputData> users, String user, String title,
                    int ID) {
        this.users = users;
        this.user = user;
        this.title = title;
        this.ID = ID;
    }

    public List<UserInputData> getUsers() {
        return users;
    }

    public String getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public int getID() {
        return ID;
    }

    public JSONObject addToFavourites(){
        JSONObject object = new JSONObject();
        for (UserInputData u : users) {

            // search for user;
            if (u.getUsername().equalsIgnoreCase(user)) {

                Map<String, Integer> history = u.getHistory();
                ArrayList<String> FavoriteMovies = u.getFavoriteMovies();

                // the case where the show it's already in the favourite list;
                for(String movie : FavoriteMovies){

                    if (movie.equalsIgnoreCase(title)) {
                        object.put(Constants.ID_STRING, this.ID);
                        object.put(Constants.MESSAGE, "error -> " + this.title +
                                " is already in favourite list");
                        return object;
                    }
                }

                // verify if the show is seen, so it can be put in the favourite
                // list;
                for (String name : history.keySet()) {

                    if (name.equalsIgnoreCase(title)) {

                        FavoriteMovies.add(name);
                        object.put(Constants.ID_STRING, this.ID);
                        object.put(Constants.MESSAGE, "success -> " + this.title
                                + " was added as favourite");
                        return object;
                    }
                }

                // the case where the show is not seen;
                object.put(Constants.ID_STRING, this.ID);
                object.put(Constants.MESSAGE, "error -> " + this.title +
                        " is not seen");
                return object;
            }
        }
        return object;
    }

}
