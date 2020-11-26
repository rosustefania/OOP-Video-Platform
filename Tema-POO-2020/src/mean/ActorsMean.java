package mean;

import fileio.ActorInputData;
import fileio.MovieInputData;
import fileio.SerialInputData;

import java.util.List;

public final class ActorsMean {

  private final String name;

  private final List<ActorInputData> actors;

  private final List<MovieInputData> movies;

  private final List<SerialInputData> serials;

  // the average rating of an actor's filmography;
  private Double filmographyMean;

  public ActorsMean(final String name, final List<ActorInputData> actors,
                   final List<MovieInputData> movies, final List<SerialInputData> serials) {
    this.name = name;
    this.actors = actors;
    this.movies = movies;
    this.serials = serials;
    this.filmographyMean = 0.0;
  }

  public String getName() {
    return name;
  }

  public List<ActorInputData> getActors() {
    return actors;
  }

  public List<MovieInputData> getMovies() {
    return movies;
  }

  public List<SerialInputData> getSerials() {
    return serials;
  }

  public Double getFilmographyMean() {
    return filmographyMean;
  }

  /** method that calculates actors' mean */
  public void mean() {
    double sum = 0.0;
    int count = 0;

    for (ActorInputData actor : actors) {

      if (name.equalsIgnoreCase(actor.getName())) {

        // get average rating of every show the actor plays in;
        for (String title : actor.getFilmography()) {

          // the case where the show is a movie;
          for (MovieInputData movie : movies) {

            if (title.equalsIgnoreCase(movie.getTitle())) {

              movie.mean(); // calculate the average rating of the movie;
              if (movie.getRatingsMean() != 0) {

                sum += movie.getRatingsMean();
                count++;
              }
            }
          }

          // the case where the show is a serial;
          for (SerialInputData serial : serials) {

            if (title.equalsIgnoreCase(serial.getTitle())) {

              serial.mean(); // calculate the average rating of the serial;
              if (serial.getRatingsMean() != 0) {

                sum += serial.getRatingsMean();
                count++;
              }
            }
          }
        }
      }
    }

    if (sum != 0) {
      filmographyMean = sum / count;
    }
  }
}
