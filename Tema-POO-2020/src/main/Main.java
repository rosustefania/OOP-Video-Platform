package main;

import checker.Checker;
import checker.Checkstyle;
import commands.Command;

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
import queries.actors.ActorsQuery;

import queries.users.UsersQuery;
import queries.videos.MoviesQuery;
import queries.videos.SerialsQuery;
import recommendations.Recommendation;

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
      int number = command.getNumber();
      int seasonNumber = command.getSeasonNumber();

      Double grade = command.getGrade();

      String user = command.getUsername();
      String actionType = command.getActionType();
      String objectType = command.getObjectType();
      String criteria = command.getCriteria();
      String sortType = command.getSortType();
      String type = command.getType();
      String title = command.getTitle();

      JSONObject object = new JSONObject();

      // commands;
      if (actionType.equalsIgnoreCase("command")) {

        Command comm = new Command(users, user, title, id, grade, seasonNumber, movies, serials);

        if (type.equalsIgnoreCase("favorite")) {

           object = comm.addToFavourites();
        }

        if (type.equalsIgnoreCase("view")) {

          object = comm.markAsViewed();
        }

        if (type.equalsIgnoreCase("rating")) {

          object = comm.giveGrade();
        }
      }

      // queries;
      if (actionType.equalsIgnoreCase("query")) {

        List<String> years = command.getFilters().get(0);
        List<String> genres = command.getFilters().get(1);
        List<String> awardslist = command.getFilters().get(command.getFilters().size() - 1);
        List<String> wordslist = command.getFilters().get(command.getFilters().size() - 2);

        // queries for actors;
        if (objectType.equalsIgnoreCase("actors")) {

          ActorsQuery actorsQuery =
              new ActorsQuery(actors, id, number, sortType, movies, serials, awardslist, wordslist);

          if (criteria.equalsIgnoreCase("average")) {

            object = actorsQuery.averageQuery();
          }

          if (criteria.equalsIgnoreCase("awards")) {

            object = actorsQuery.awardsQuery();
          }

          if (criteria.equalsIgnoreCase("filter_description")) {

            object = actorsQuery.filterDescription();
          }
        }

        // queries for shows;
        if (objectType.equalsIgnoreCase("shows")) {

          SerialsQuery serialsQuery =
                  new SerialsQuery(users, serials, id, sortType, number, years, genres);

          if (criteria.equalsIgnoreCase("ratings")) {

            object = serialsQuery.getRatingSerials();
          }

          if (criteria.equalsIgnoreCase("favorite")) {

            object = serialsQuery.getFavouriteSerials();
          }

          if (criteria.equalsIgnoreCase("longest")) {

            object = serialsQuery.getLongestSerials();
          }

          if (criteria.equalsIgnoreCase("most_viewed")) {

            object = serialsQuery.getMostViewedSerials();
          }
        }

        // queries for movies;
        if (objectType.equalsIgnoreCase("movies")) {

          MoviesQuery moviesQuery =
                  new MoviesQuery(users, movies, id, sortType, number, years, genres);

          if (criteria.equalsIgnoreCase("ratings")) {

            object = moviesQuery.getRatingMovies();
          }

          if (criteria.equalsIgnoreCase("favorite")) {

            object = moviesQuery.getFavouriteMovies();
          }

          if (criteria.equalsIgnoreCase("longest")) {

            object = moviesQuery.getLongestMovies();
          }

          if (criteria.equalsIgnoreCase("most_viewed")) {

            object = moviesQuery.getMostViewedMovies();
          }
        }

        // query for users;
        if (objectType.equalsIgnoreCase("users")) {

          if (criteria.equalsIgnoreCase("num_ratings")) {

            UsersQuery usersQuery = new UsersQuery(users, id, number, sortType);
            object = usersQuery.getNumberOfRatings();
          }
        }
      }

      // recommendations;
      if (actionType.equalsIgnoreCase("recommendation")) {

        String givenGenre = command.getGenre();
        Recommendation recommendation =
                new Recommendation(users, movies, serials, id, user, givenGenre);

        if (type.equalsIgnoreCase("standard")) {

          object = recommendation.getStandardRecommendation();
        }

        if (type.equalsIgnoreCase("best_unseen")) {

          object = recommendation.getBestUnseenRecommendation();
        }

        if (type.equalsIgnoreCase("popular")) {

          object = recommendation.getPopularRecommendation();
        }

        if (type.equalsIgnoreCase("favorite")) {

          object = recommendation.getFavouriteRecommendation();
        }

        if (type.equalsIgnoreCase("search")) {

          object = recommendation.getSearchRecommendation();
        }
      }
      arrayResult.add(object);
    }
    fileWriter.closeJSON(arrayResult);
  }
}
