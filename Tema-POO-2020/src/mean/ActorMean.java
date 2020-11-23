package mean;

import fileio.ActorInputData;
import queries_actors.MovieInputData;
import fileio.SerialInputData;

import java.util.List;

public class ActorMean {

    private final String name;

    private final List<ActorInputData> actors;

    private final List<MovieInputData> movies;

    private final List<SerialInputData> serials;

    // the average rating of an actor's filmography;
    private Double filmography_mean;

    public ActorMean(String name, List<ActorInputData> actors,
                     List<MovieInputData> movies, List<SerialInputData> serials){
        this.name = name;
        this.actors = actors;
        this.movies = movies;
        this.serials = serials;
        this.filmography_mean = 0.0;
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

    public Double getFilmography_mean() {
        return filmography_mean;
    }

    public void setFilmography_mean(Double filmography_mean) {
        this.filmography_mean = filmography_mean;
    }

    public void mean(){
        double sum = 0.0;
        int count = 0;

        for (ActorInputData actor : actors){

            if (name.equalsIgnoreCase(actor.getName())){

                // get average rating of every show the actor plays in;
                for (String title: actor.getFilmography()){

                    // the case where the show is a movie;
                    for (MovieInputData movie : movies){

                        if(title.equalsIgnoreCase(movie.getTitle())){

                            movie.mean(); // calculate the average rating of the movie;
                            if(movie.getRatings_mean() != 0){

                                sum += movie.getRatings_mean();
                                count ++;
                            }

                        }
                    }

                    // the case where the show is a serial;
                    for (SerialInputData serial : serials){

                        if(title.equalsIgnoreCase(serial.getTitle())){

                            serial.mean(); // calculate the average rating of the serial;
                            if(serial.getRatings_mean() != 0){

                                sum += serial.getRatings_mean();
                                count ++;
                            }

                        }
                    }
                }
            }
        }

        if (sum != 0){
            setFilmography_mean(sum / count);
        }
    }
}
