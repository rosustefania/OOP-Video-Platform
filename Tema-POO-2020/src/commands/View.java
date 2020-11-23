package commands;

import common.Constants;
import fileio.UserInputData;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class View {
    public List<UserInputData> users;

    public String user;

    public String title;

    public int ID;

    public View(List<UserInputData> users, String user, String title, int ID) {
        this.users = users;
        this.user = user;
        this.title = title;
        this.ID = ID;
    }

    public JSONObject markAsViewed(){
        JSONObject object = new JSONObject();

        // search for user;
        for (UserInputData u : users) {

            if (u.getUsername().equalsIgnoreCase(user)) {

                Map<String, Integer> history = u.getHistory();

                // if the show is already seen, grow views number;
                for (Map.Entry<String, Integer> entry : history.entrySet()) {

                    if (entry.getKey().equalsIgnoreCase(title)) {

                        history.get(entry.setValue(entry.getValue() + 1));
                        object.put(Constants.ID_STRING, this.ID);
                        object.put(Constants.MESSAGE, "success -> " + this.title
                                + " was viewed with total views of "
                                + entry.getValue());
                        return object;
                    }
                }

                // if it's seen for the first time, add it in the history list
                // with no_views = 1;
                history.put(title, 1);
                object.put(Constants.ID_STRING, this.ID);
                object.put(Constants.MESSAGE, "success -> " + this.title +
                        " was viewed with total views of 1");
                return object;

            }
        }
        return object;
    }

}
