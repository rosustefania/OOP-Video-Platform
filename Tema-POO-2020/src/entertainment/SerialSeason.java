package entertainment;

/** class that help with rating command;
 * stores just the name of the serial and on season of it;
 */
public final class SerialSeason {
    private final String title;

    private final int currentSeason;

    public SerialSeason(final String title, final int currentSeason) {
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
