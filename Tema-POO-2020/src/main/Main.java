package main;

import checker.Checkstyle;
import checker.Checker;
import commands.Favorite;

import commands.Rating;
import commands.View;
import common.Constants;
import fileio.*;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import queries_actors.Average;
import queries_actors.Awards;
import queries_actors.FilterDescription;
import queries_actors.MovieInputData;
import queries_users.NumberOfRatings;
import queries_videos.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * Call the main checker and the coding style checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(Constants.TESTS_PATH);
        Path path = Paths.get(Constants.RESULT_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        File outputDirectory = new File(Constants.RESULT_PATH);

        Checker checker = new Checker();
        checker.deleteFiles(outputDirectory.listFiles());

        for (File file : Objects.requireNonNull(directory.listFiles())) {

            String filepath = Constants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getAbsolutePath(), filepath);
            }
        }

        checker.iterateFiles(Constants.RESULT_PATH, Constants.REF_PATH, Constants.TESTS_PATH);
        Checkstyle test = new Checkstyle();
        test.testCheckstyle();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    @SuppressWarnings("unchecked")
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        InputLoader inputLoader = new InputLoader(filePath1);
        Input input = inputLoader.readData();

        Writer fileWriter = new Writer(filePath2);
        JSONArray arrayResult = new JSONArray();

        //TODO add here the entry point to your implementation
        List<ActionInputData> commands = input.getCommands();
        List<UserInputData> users = input.getUsers();
        List<MovieInputData> movies = input.getMovies();
        List<SerialInputData> serials = input.getSerials();
        List<ActorInputData> actors = input.getActors();

        for (ActionInputData command : commands) {

            int Id = command.getActionId();
            String user = command.getUsername();
            String action_type = command.getActionType();
            String object_type = command.getObjectType();
            String criteria = command.getCriteria();
            String sort_type = command.getSortType();
            int number = command.getNumber();
            String type = command.getType();
            String title = command.getTitle();
            Double grade = command.getGrade();
            int seasonNumber = command.getSeasonNumber();

            // the command is "favourite";
            if (action_type.equalsIgnoreCase("command") &&
                    type.equalsIgnoreCase("favorite")) {

                Favorite fav = new Favorite(users, user, title, Id);
                JSONObject obj = fav.addToFavourites();
                arrayResult.add(obj);
            }

            // the command is "view";
            if (action_type.equalsIgnoreCase("command") &&
                    type.equalsIgnoreCase("view")){

                View view = new View(users, user, title, Id);
                JSONObject obj = view.markAsViewed();
                arrayResult.add(obj);
            }

            // the command is "rating";
            if (action_type.equalsIgnoreCase("command") &&
                    type.equalsIgnoreCase("rating")){

                Rating rating = new Rating(users, user, title, Id, grade,
                        seasonNumber, movies, serials);
                JSONObject obj = rating.giveGrade();
                arrayResult.add(obj);
            }


            // queries for actors;
            if (action_type.equalsIgnoreCase("query") &&
                   object_type.equalsIgnoreCase("actors")){

                // average query;
                if (criteria.equalsIgnoreCase("average")){

                    Average average = new Average(actors, Id, number, sort_type, movies, serials);
                    JSONObject obj = average.average_query();
                    arrayResult.add(obj);
                }

                // awards query;
                if (criteria.equalsIgnoreCase("awards")){

                    List<String> awardslist = command.getFilters().get(3);
                    Awards awards = new Awards(actors, Id, sort_type, awardslist);
                    JSONObject obj = awards.awards_query();
                    arrayResult.add(obj);
                }

                // filter description query;
                if (criteria.equalsIgnoreCase("filter_description")){

                    List<String> wordslist = command.getFilters().get(2);
                    FilterDescription filter = new FilterDescription(actors, Id,
                            sort_type, wordslist);
                    JSONObject obj = filter.filter_description();
                    arrayResult.add(obj);
                }

            }

            // rating query for videos;
            if (action_type.equalsIgnoreCase("query") &&
                    criteria.equalsIgnoreCase("ratings")){

                List<String> years = command.getFilters().get(0);
                List<String> genres = command.getFilters().get(1);

                // rating query for serials;
                if (object_type.equalsIgnoreCase("shows")){

                    RatingSerials ratingSerials = new RatingSerials(serials, Id,
                            sort_type, number, years, genres);
                    JSONObject obj = ratingSerials.getRatingSerials();
                    arrayResult.add(obj);
                }

                // rating query for movies;
                if (object_type.equalsIgnoreCase("movies")){

                    RatingMovies ratingMovies = new RatingMovies(movies, Id,
                            sort_type, number, years, genres);
                    JSONObject obj = ratingMovies.getRatingMovies();
                    arrayResult.add(obj);
                }
            }

            if (action_type.equalsIgnoreCase("query") &&
                    criteria.equalsIgnoreCase("favorite")){

                List<String> years = command.getFilters().get(0);
                List<String> genres = command.getFilters().get(1);

                // favourite query for serials;
                if (object_type.equalsIgnoreCase("shows")){

                    FavouriteSerials favouriteSerials = new FavouriteSerials(users,
                            serials, Id, sort_type, number, years, genres);
                    JSONObject obj = favouriteSerials.getFavouriteSerials();
                    arrayResult.add(obj);

                }

                // favourite query for movies;
                if (object_type.equalsIgnoreCase("movies")){

                    FavouriteMovies favouriteMovies = new FavouriteMovies(users,
                            movies, Id, sort_type, number, years, genres);
                    JSONObject obj = favouriteMovies.getFavouriteMovies();
                    arrayResult.add(obj);

                }

            }

            if (action_type.equalsIgnoreCase("query") &&
                    criteria.equalsIgnoreCase("longest")){

                List<String> years = command.getFilters().get(0);
                List<String> genres = command.getFilters().get(1);

                // longest query for serials;
                if (object_type.equalsIgnoreCase("shows")){

                    LongestSerials longestSerials = new LongestSerials(serials,
                            Id, number, sort_type, years, genres);
                    JSONObject obj = longestSerials.getLongestSerials();
                    arrayResult.add(obj);

                }

                // longest query for movies;
                if (object_type.equalsIgnoreCase("movies")){

                    LongestMovies longestMovies = new LongestMovies(movies,
                            Id, number, sort_type, years, genres);
                    JSONObject obj = longestMovies.getLongestMovies();
                    arrayResult.add(obj);

                }
            }

            if (action_type.equalsIgnoreCase("query") &&
                    criteria.equalsIgnoreCase("most_viewed")){

                List<String> years = command.getFilters().get(0);
                List<String> genres = command.getFilters().get(1);

                // most_viewed query for serials;
                if (object_type.equalsIgnoreCase("shows")){

                    Most_viewedSerials most_viewedSerials = new Most_viewedSerials(users,
                            serials, Id, number, sort_type, years, genres);
                    JSONObject obj = most_viewedSerials.getMost_viewedSerials();
                    arrayResult.add(obj);

                }
                // most_viewed query for movies;
                if (object_type.equalsIgnoreCase("movies")){

                    Most_viewedMovies most_viewedMovies = new Most_viewedMovies(users,
                            movies, Id, number, sort_type, years, genres);
                    JSONObject obj = most_viewedMovies.getMost_viewedMovies();
                    arrayResult.add(obj);

                }

            }

            // query for users;
            if (action_type.equalsIgnoreCase("query") &&
                    criteria.equalsIgnoreCase("num_ratings")){

                NumberOfRatings numberOfRatings = new NumberOfRatings(users, Id,
                        number, sort_type);
                JSONObject obj = numberOfRatings.getNumberOfRatings();
                arrayResult.add(obj);
            }


        }


        fileWriter.closeJSON(arrayResult);
    }
}
