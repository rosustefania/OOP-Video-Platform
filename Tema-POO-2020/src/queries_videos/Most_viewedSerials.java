package queries_videos;

import common.Constants;
import fileio.SerialInputData;
import fileio.UserInputData;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Most_viewedSerials {
    private final List<UserInputData> users;

    private final List<SerialInputData> serials;

    private final int ID;

    private final int number;

    private final String sort_type;

    private final List<String> years;

    private final List<String> genres;

    public Most_viewedSerials(List<UserInputData> users, List<SerialInputData> serials,
                              int ID, int number, String sort_type, List<String> years,
                              List<String> genres) {
        this.users = users;
        this.serials = serials;
        this.ID = ID;
        this.number = number;
        this.sort_type = sort_type;
        this.years = years;
        this.genres = genres;
    }

    public JSONObject getMost_viewedSerials(){
        JSONObject object = new JSONObject();
        List<String> query = new ArrayList<>();

        // calculate views number for every serial;
        for (SerialInputData serial : serials){

            int views_number = 0;
            for (UserInputData user : users){

                Map<String, Integer> history = user.getHistory();

                for (Map.Entry<String, Integer> entry : history.entrySet()) {

                    if (entry.getKey().equalsIgnoreCase(serial.getTitle())){

                        views_number += entry.getValue();
                    }
                }
            }
            serial.setViews(views_number);
        }

        // sort serials' list ascendent by views;
        if (sort_type.equalsIgnoreCase("asc")){

            for (int i = 0; i < serials.size() - 1; i++){

                for (int j = 0; j < serials.size() - i - 1; j++){

                    if (serials.get(j).getViews() > serials.get(j + 1).getViews()){

                        Collections.swap(serials, j, j + 1);
                    }

                    // if two serials have the same number of views, sort them ascendent by name;
                    if ((serials.get(j).getViews() == serials.get(j + 1).getViews())
                            && (serials.get(j).getTitle().compareTo(serials.get(j + 1).getTitle()) > 0)){

                        Collections.swap(serials, j, j + 1);
                    }

                }
            }

            int count = 0;
            for (SerialInputData serial : serials) {

                if (count < number) {

                    // verify if number of views is 0;
                    if (serial.getViews() != 0){
                        boolean flag1 = true;
                        boolean flag2 = true;

                        // apply year filter;
                        if (years.get(0) != null) {

                            flag1 = false;
                            String year_string = String.valueOf(serial.getYear());
                            for (String year : years) {

                                if (year.equalsIgnoreCase(year_string)) {

                                    flag1 = true;
                                    break;
                                }
                            }
                        }

                        // apply genres filter;
                        if (genres.get(0) != null) {

                            if (!flag1) {

                                flag2 = false;
                            }
                            else {

                                int genres_found = 0;
                                for (String genre : genres) {

                                    for (String serial_genre : serial.getGenres()) {

                                        if (genre.equalsIgnoreCase(serial_genre)) {

                                            genres_found++;
                                        }
                                    }
                                }

                                if (genres_found != genres.size()) {

                                    flag2 = false;
                                }
                            }
                        }

                        if (flag1 && flag2) {

                            query.add(serial.getTitle());
                            count++;
                        }
                    }
                }
            }

            object.put(Constants.ID_STRING, this.ID);
            object.put(Constants.MESSAGE, "Query result: " + query);
            return object;
        }

        if (sort_type.equalsIgnoreCase("desc")){

            // sort serials' list descendent by number of views;
            for (int i = 0; i < serials.size() - 1; i++){

                for (int j = 0; j < serials.size() - i - 1; j++){

                    if (serials.get(j).getDuration() <
                            serials.get(j + 1).getDuration()){

                        Collections.swap(serials, j, j + 1);
                    }

                    // if two serials have the same number of views, sort them descendent by name;
                    if ((serials.get(j).getDuration() == serials.get(j + 1).getDuration())
                            && (serials.get(j).getTitle().compareTo(serials.get(j + 1).getTitle()) < 0)){

                        Collections.swap(serials, j, j + 1);
                    }

                }
            }

            int count = 0;
            for (SerialInputData serial : serials) {

                if (count < number) {

                    // verify if number of views is 0;
                    if (serial.getViews() != 0){

                        boolean flag1 = true;
                        boolean flag2 = true;

                        // apply year filter;
                        if (years.get(0) != null) {

                            flag1 = false;
                            String year_string = String.valueOf(serial.getYear());
                            for (String year : years) {

                                if (year.equalsIgnoreCase(year_string)) {

                                    flag1 = true;
                                    break;
                                }
                            }
                        }

                        // apply genres filter;
                        if (genres.get(0) != null) {

                            if (!flag1) {

                                flag2 = false;
                            }
                            else {

                                int genres_found = 0;
                                for (String genre : genres) {

                                    for (String serial_genre : serial.getGenres()) {

                                        if (genre.equalsIgnoreCase(serial_genre)) {

                                            genres_found++;
                                        }
                                    }
                                }

                                if (genres_found != genres.size()) {

                                    flag2 = false;
                                }
                            }
                        }

                        if (flag1 && flag2) {

                            query.add(serial.getTitle());
                            count++;
                        }
                    }
                }
            }

            object.put(Constants.ID_STRING, this.ID);
            object.put(Constants.MESSAGE, "Query result: " + query);
            return object;
        }

        object.put(Constants.ID_STRING, this.ID);
        object.put(Constants.MESSAGE, "Query result: " + query);
        return object;
    }

}
