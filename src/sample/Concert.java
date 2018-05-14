package sample;

import javafx.beans.property.SimpleStringProperty;

/**
 * Concert object to put into setlist table
 */
public class Concert {

    private final SimpleStringProperty artist;
    private final SimpleStringProperty venue;
    private final SimpleStringProperty date;


    Concert(String artist, String venue, String date){

        this.artist = new SimpleStringProperty(artist);
        this.venue = new SimpleStringProperty(venue);
        this.date = new SimpleStringProperty(date);

    }

    public SimpleStringProperty artistProperty() {
        return artist;
    }
    public SimpleStringProperty venueProperty() {
        return venue;
    }
    public SimpleStringProperty dateProperty() {
        return date;
    }
}
