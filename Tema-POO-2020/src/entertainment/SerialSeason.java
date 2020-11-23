package entertainment;

public class SerialSeason {
    private final String title;

    private final int currentSeason;

    public SerialSeason(final String title, final int currentSeason){
        this.title = title;
        this.currentSeason = currentSeason;
    }

    public String getTitle() {
        return title;
    }

    public int getCurrentSeason() {
        return currentSeason;
    }
}
