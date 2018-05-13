package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Concert {

    private final SimpleStringProperty artist;
    private final SimpleStringProperty venue;
    private final SimpleStringProperty date;


    Concert(String artist, String venue, String date){

        this.artist = new SimpleStringProperty(artist);
        this.venue = new SimpleStringProperty(venue);
        this.date = new SimpleStringProperty(date);

    }

    public String getArtist() {
        return artist.get();
    }
    public SimpleStringProperty artistProperty() {
        return artist;
    }


    public String getVenue() {
        return venue.get();
    }
    public SimpleStringProperty venueProperty() {
        return venue;
    }


    public String getDate() {
        return date.get();
    }
    public SimpleStringProperty dateProperty() {
        return date;
    }
}
