package queries;

import common.Constants;
import fileio.SerialInputData;
import fileio.UserInputData;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FavouriteSerials {

    private final List<UserInputData> users;

    private final List<SerialInputData> serials;

    private final int ID;

    private final int number;

    private final String sort_type;

    private final List<String> years;

    private final List<String> genres;

    public FavouriteSerials(List<UserInputData> users, List<SerialInputData> serials,
                            int ID, final String sort_type, int number, List<String> years,
                            List<String> genres) {
        this.users = users;
        this.serials = serials;
        this.ID = ID;
        this.sort_type = sort_type;
        this.number = number;
        this.years = years;
        this.genres = genres;
    }

    public JSONObject getFavouriteSerials(){

        JSONObject object = new JSONObject();
        List<String> query = new ArrayList<>();

        for (SerialInputData serial : serials){

            int count = 0;
            String title = serial.getTitle();

            for (UserInputData user : users){

                List<String> favourites = user.getFavoriteMovies();
                for (String favourite : favourites){

                    if (favourite.equalsIgnoreCase(title)){

                        count++ ;
                        break;
                    }
                }
            }

            serial.setFavourite_appearences(count);

        }

        // sort serials' list ascendent by average rating;
        if (sort_type.equalsIgnoreCase("asc")){

            for (int i = 0; i < serials.size() - 1; i++){

                for (int j = 0; j < serials.size() - i - 1; j++){

                    if (serials.get(j).getFavourite_appearences() >
                            serials.get(j + 1).getFavourite_appearences()){

                        Collections.swap(serials, j, j + 1);
                    }

                    // if two serials have the same average rating, sort tem ascendent by name;
                    if ((serials.get(j).getFavourite_appearences() == serials.get(j + 1).getFavourite_appearences())
                            && (serials.get(j).getTitle().compareTo(serials.get(j + 1).getTitle()) > 0)){

                        Collections.swap(serials, j, j + 1);
                    }

                }
            }

            int count = 0;
            for (SerialInputData serial : serials) {

                if (count < number) {

                    // verify if the number of appearences in favourite lists is 0;
                    if (serial.getFavourite_appearences() != 0) {

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

            // sort serials' list descendent by average rating;
            for (int i = 0; i < serials.size() - 1; i++){

                for (int j = 0; j < serials.size() - i - 1; j++){

                    if (serials.get(j).getFavourite_appearences() <
                            serials.get(j + 1).getFavourite_appearences()){

                        Collections.swap(serials, j, j + 1);
                    }

                    // if two serials have the same average rating, sort tem descendent by name;
                    if ((serials.get(j).getFavourite_appearences() == serials.get(j + 1).getFavourite_appearences())
                            && (serials.get(j).getTitle().compareTo(serials.get(j + 1).getTitle()) < 0)){

                        Collections.swap(serials, j, j + 1);
                    }

                }
            }

            int count = 0;
            for (SerialInputData serial : serials) {

                if (count < number) {

                    // verify if the number of appearences in favourite lists is 0;
                    if (serial.getFavourite_appearences() != 0) {

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
                            } else {

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
