package queries;

import common.Constants;
import fileio.SerialInputData;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RatingSerials {

    private final List<SerialInputData> serials;

    private final int ID;

    private final String sort_type;

    private final int number;

    private final List<String> years;

    private final List<String> genres;

    public RatingSerials(List<SerialInputData> serials, final int ID, String sort_type,
                         int number, final List<String> years, final List<String> genres){
        this.serials = serials;
        this.ID = ID;
        this.sort_type = sort_type;
        this.number = number;
        this.years = years;
        this.genres = genres;
    }

    public JSONObject getRatingSerials(){
        JSONObject object = new JSONObject();
        List<String> query = new ArrayList<>();

        // sort serials' list ascendent by average rating;
        if (sort_type.equalsIgnoreCase("asc")){
            for (int i = 0; i < serials.size() - 1; i++){

                for (int j = 0; j < serials.size() - i - 1; j++){

                    // calculate serials' average rating;
                    serials.get(j).mean();
                    serials.get(j + 1).mean();

                    if (serials.get(j).getRatings_mean() > serials.get(j + 1).getRatings_mean()){

                        Collections.swap(serials, j, j + 1);
                    }

                    // if two serials have the same average rating, sort tem ascendent by name;
                    if ((serials.get(j).getRatings_mean().equals(serials.get(j + 1).getRatings_mean()))
                        && (serials.get(j).getTitle().compareTo(serials.get(j + 1).getTitle()) > 0)){

                        Collections.swap(serials, j, j + 1);
                    }

                }
            }

            int count = 0;
            for (SerialInputData serial : serials) {

                if (count < number) {

                    serial.mean();

                    // verify if the serial's average rating is 0;
                    if (serial.getRatings_mean() != 0) {

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

        if (sort_type.equalsIgnoreCase("desc")){

            // sort serials' list descendent by average rating;
            for (int i = 0; i < serials.size() - 1; i++){

                for (int j = 0; j < serials.size() - i - 1; j++){

                    // calculate serials' average rating;
                    serials.get(j).mean();
                    serials.get(j + 1).mean();

                    if (serials.get(j).getRatings_mean() < serials.get(j + 1).getRatings_mean()){

                        Collections.swap(serials, j, j + 1);
                    }

                    // if two serials have the same average rating, sort tem descendent by name;
                    if ((serials.get(j).getRatings_mean().equals(serials.get(j + 1).getRatings_mean()))
                            && (serials.get(j).getTitle().compareTo(serials.get(j + 1).getTitle()) < 0)){

                        Collections.swap(serials, j, j + 1);
                    }

                }
            }

            int count = 0;
            for (SerialInputData serial : serials) {

                if (count < number) {

                    serial.mean();

                    // verify if the serial's average rating is 0;
                    if (serial.getRatings_mean() != 0) {

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

        return object;
    }



}