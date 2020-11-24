package main;

import checker.Checker;
import checker.Checkstyle;
import commands.Favorite;
import commands.Rating;
import commands.View;
import common.Constants;
import fileio.ActionInputData;
import fileio.ActorInputData;
import fileio.Input;
import fileio.InputLoader;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;
import fileio.Writer;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import queries.actors.Average;
import queries.actors.Awards;
import queries.actors.FilterDescription;
import queries.users.NumberOfRatings;
import queries.videos.FavouriteMovies;
import queries.videos.FavouriteSerials;
import queries.videos.LongestMovies;
import queries.videos.LongestSerials;
import queries.videos.MostViewedMovies;
import queries.videos.MostViewedSerials;

import queries.videos.RatingMovies;
import queries.videos.RatingSerials;
import recommendations.BestUnseenRecommendation;
import recommendations.FavouriteRecommendation;
import recommendations.PopularRecommendation;
import recommendations.SearchRecommendation;
import recommendations.StandardRecommendation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

/** The entry point to this homework. It runs the checker that tests your implentation. */
public final class Main {
  /** for coding style */
  private Main() { }

  /**
   * Call the main checker and the coding style checker
   *
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
  public static void action(final String filePath1, final String filePath2) throws IOException {
    InputLoader inputLoader = new InputLoader(filePath1);
    Input input = inputLoader.readData();

    Writer fileWriter = new Writer(filePath2);
    JSONArray arrayResult = new JSONArray();

    // TODO add here the entry point to your implementation
    List<ActionInputData> commands = input.getCommands();
    List<UserInputData> users = input.getUsers();
    List<MovieInputData> movies = input.getMovies();
    List<SerialInputData> serials = input.getSerials();
    List<ActorInputData> actors = input.getActors();

    for (ActionInputData command : commands) {

      int id = command.getActionId();
      String user = command.getUsername();
      String actionType = command.getActionType();
      String objectType = command.getObjectType();
      String criteria = command.getCriteria();
      String sortType = command.getSortType();
      int number = command.getNumber();
      String type = command.getType();
      String title = command.getTitle();
      Double grade = command.getGrade();
      int seasonNumber = command.getSeasonNumber();

      // the command is "favourite";
      if (actionType.equalsIgnoreCase("command") && type.equalsIgnoreCase("favorite")) {

        Favorite fav = new Favorite(users, user, title, id);
        JSONObject obj = fav.addToFavourites();
        arrayResult.add(obj);
      }

      // the command is "view";
      if (actionType.equalsIgnoreCase("command") && type.equalsIgnoreCase("view")) {

        View view = new View(users, user, title, id);
        JSONObject obj = view.markAsViewed();
        arrayResult.add(obj);
      }

      // the command is "rating";
      if (actionType.equalsIgnoreCase("command") && type.equalsIgnoreCase("rating")) {

        Rating rating = new Rating(users, user, title, id, grade, seasonNumber, movies, serials);
        JSONObject obj = rating.giveGrade();
        arrayResult.add(obj);
      }

      // queries;
      if (actionType.equalsIgnoreCase("query")) {

        // average query for actors;
        if (criteria.equalsIgnoreCase("average")) {

          Average average = new Average(actors, id, number, sortType, movies, serials);
          JSONObject obj = average.averageQuery();
          arrayResult.add(obj);
        }

        // awards query for actors;
        if (criteria.equalsIgnoreCase("awards")) {

          List<String> awardslist = command.getFilters().get(command.getFilters().size() - 1);
          Awards awards = new Awards(actors, id, sortType, awardslist);
          JSONObject obj = awards.awardsQuery();
          arrayResult.add(obj);
        }

        // filter description query for actors;
        if (criteria.equalsIgnoreCase("filter_description")) {

          List<String> wordslist = command.getFilters().get(2);
          FilterDescription filter = new FilterDescription(actors, id, sortType, wordslist);
          JSONObject obj = filter.filterDescription();
          arrayResult.add(obj);
        }

        // rating query for videos;
        if (criteria.equalsIgnoreCase("ratings")) {

          List<String> years = command.getFilters().get(0);
          List<String> genres = command.getFilters().get(1);

          // rating query for serials;
          if (objectType.equalsIgnoreCase("shows")) {

            RatingSerials ratingSerials =
                new RatingSerials(serials, id, sortType, number, years, genres);
            JSONObject obj = ratingSerials.getRatingSerials();
            arrayResult.add(obj);
          }

          // rating query for movies;
          if (objectType.equalsIgnoreCase("movies")) {

            RatingMovies ratingMovies =
                new RatingMovies(movies, id, sortType, number, years, genres);
            JSONObject obj = ratingMovies.getRatingMovies();
            arrayResult.add(obj);
          }
        }

        if (criteria.equalsIgnoreCase("favorite")) {

          List<String> years = command.getFilters().get(0);
          List<String> genres = command.getFilters().get(1);

          // favourite query for serials;
          if (objectType.equalsIgnoreCase("shows")) {

            FavouriteSerials favouriteSerials =
                new FavouriteSerials(users, serials, id, sortType, number, years, genres);
            JSONObject obj = favouriteSerials.getFavouriteSerials();
            arrayResult.add(obj);
          }

          // favourite query for movies;
          if (objectType.equalsIgnoreCase("movies")) {

            FavouriteMovies favouriteMovies =
                new FavouriteMovies(users, movies, id, sortType, number, years, genres);
            JSONObject obj = favouriteMovies.getFavouriteMovies();
            arrayResult.add(obj);
          }
        }

        if (criteria.equalsIgnoreCase("longest")) {

          List<String> years = command.getFilters().get(0);
          List<String> genres = command.getFilters().get(1);

          // longest query for serials;
          if (objectType.equalsIgnoreCase("shows")) {

            LongestSerials longestSerials =
                new LongestSerials(serials, id, number, sortType, years, genres);
            JSONObject obj = longestSerials.getLongestSerials();
            arrayResult.add(obj);
          }

          // longest query for movies;
          if (objectType.equalsIgnoreCase("movies")) {

            LongestMovies longestMovies =
                new LongestMovies(movies, id, number, sortType, years, genres);
            JSONObject obj = longestMovies.getLongestMovies();
            arrayResult.add(obj);
          }
        }

        if (criteria.equalsIgnoreCase("most_viewed")) {

          List<String> years = command.getFilters().get(0);
          List<String> genres = command.getFilters().get(1);

          // most_viewed query for serials;
          if (objectType.equalsIgnoreCase("shows")) {

            MostViewedSerials mostViewedSerials =
                new MostViewedSerials(users, serials, id, number, sortType, years, genres);
            JSONObject obj = mostViewedSerials.getMostViewedSerials();
            arrayResult.add(obj);
          }
          // most_viewed query for movies;
          if (objectType.equalsIgnoreCase("movies")) {

            MostViewedMovies mostViewedMovies =
                new MostViewedMovies(users, movies, id, number, sortType, years, genres);
            JSONObject obj = mostViewedMovies.getMostViewedMovies();
            arrayResult.add(obj);
          }
        }

        // query for users;
        if (criteria.equalsIgnoreCase("num_ratings")) {

          NumberOfRatings numberOfRatings = new NumberOfRatings(users, id, number, sortType);
          JSONObject obj = numberOfRatings.getNumberOfRatings();
          arrayResult.add(obj);
        }
      }

      if (actionType.equalsIgnoreCase("recommendation")) {

        if (type.equalsIgnoreCase("standard")) {

          StandardRecommendation standardRecommendation =
              new StandardRecommendation(users, movies, serials, id, user);
          JSONObject obj = standardRecommendation.getStandardRecommendation();
          arrayResult.add(obj);
        }

        if (type.equalsIgnoreCase("best_unseen")) {

          BestUnseenRecommendation bestUnseen =
                  new BestUnseenRecommendation(users, movies, serials, id, user);
          JSONObject obj = bestUnseen.getBestUnseenRecommendation();
          arrayResult.add(obj);
        }

        if (type.equalsIgnoreCase("popular")) {

          PopularRecommendation popularRecommendation =
                  new PopularRecommendation(users, movies, serials, id, user);
          JSONObject obj = popularRecommendation.getPopularRecommendation();
          arrayResult.add(obj);
        }

        if (type.equalsIgnoreCase("favorite")) {

          FavouriteRecommendation favouriteRecommendation =
                  new FavouriteRecommendation(users, movies, serials, id, user);
          JSONObject obj = favouriteRecommendation.getFavouriteRecommendation();
          arrayResult.add(obj);
        }

        if (type.equalsIgnoreCase("search")) {

          String givenGenre = command.getGenre();
          SearchRecommendation searchRecommendation =
                  new SearchRecommendation(users, movies, serials, id, user, givenGenre);
          JSONObject obj = searchRecommendation.getSearchRecommendation();
          arrayResult.add(obj);
        }
      }
    }

    fileWriter.closeJSON(arrayResult);
  }
}
