package queries_videos;

import common.Constants;
import fileio.SerialInputData;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LongestSerials {
    private final List<SerialInputData> serials;

    private final int ID;

    private final int number;

    private final String sort_type;

    private final List<String> years;

    private final List<String> genres;

    public LongestSerials(List<SerialInputData> serials, int ID, int number, String sort_type, List<String> years, List<String> genres) {
        this.serials = serials;
        this.ID = ID;
        this.number = number;
        this.sort_type = sort_type;
        this.years = years;
        this.genres = genres;
    }

    public JSONObject getLongestSerials(){
        JSONObject object = new JSONObject();
        List<String> query = new ArrayList<>();

        // calculate total duration for all of the serials;
        for (SerialInputData serial : serials){

            serial.calculateDuration();
        }

        // sort serials' list ascendent by duration;
        if (sort_type.equalsIgnoreCase("asc")){

            for (int i = 0; i < serials.size() - 1; i++){

                for (int j = 0; j < serials.size() - i - 1; j++){

                    if (serials.get(j).getDuration() > serials.get(j + 1).getDuration()){

                        Collections.swap(serials, j, j + 1);
                    }

                    // if two serials have the same duration, sort them ascendent by name;
                    if ((serials.get(j).getDuration() == serials.get(j + 1).getDuration())
                            && (serials.get(j).getTitle().compareTo(serials.get(j + 1).getTitle()) > 0)){

                        Collections.swap(serials, j, j + 1);
                    }

                }
            }

            int count = 0;
            for (SerialInputData serial : serials) {

                if (count < number) {

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

            object.put(Constants.ID_STRING, this.ID);
            object.put(Constants.MESSAGE, "Query result: " + query);
            return object;
        }

        if (sort_type.equalsIgnoreCase("desc")){

            // sort serials' list descendent by duration;
            for (int i = 0; i < serials.size() - 1; i++){

                for (int j = 0; j < serials.size() - i - 1; j++){

                    if (serials.get(j).getDuration() <
                            serials.get(j + 1).getDuration()){

                        Collections.swap(serials, j, j + 1);
                    }

                    // if two serials have the same duration, sort them descendent by name;
                    if ((serials.get(j).getDuration() == serials.get(j + 1).getDuration())
                            && (serials.get(j).getTitle().compareTo(serials.get(j + 1).getTitle()) < 0)){

                        Collections.swap(serials, j, j + 1);
                    }

                }
            }

            int count = 0;
            for (SerialInputData serial : serials) {

                if (count < number) {

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

            object.put(Constants.ID_STRING, this.ID);
            object.put(Constants.MESSAGE, "Query result: " + query);
            return object;
        }

        object.put(Constants.ID_STRING, this.ID);
        object.put(Constants.MESSAGE, "Query result: " + query);
        return object;
    }
}
