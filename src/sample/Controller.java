package sample;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import org.jsoup.nodes.Document;
import java.util.ArrayList;
import javafx.scene.image.*;


public class Controller {
    //DECLARATIONS
    private PageCount page = new PageCount();

    @FXML
    private TabPane tabPane;

        @FXML
        private Tab homeTab;

        @FXML
        private Tab favoritesTab;

        @FXML
        private Tab searchTab;

        @FXML
        private Tab setlistTab;

    @FXML
    private TextField artistField;

    @FXML
    private TableView<Concert> concertTable;

        @FXML
        private TableColumn<Concert, String> artistColumn;

        @FXML
        private TableColumn<Concert, String> venueColumn;

        @FXML
        private TableColumn<Concert, String> dateColumn;

    @FXML
    private TableView<String> songTable;

        @FXML
        private TableColumn<String, String> songColumn;


    @FXML
    private TableView<String> favArtistTable;

        @FXML
        private TableColumn<String, String> favArtistColumn;

    @FXML
    private TableView<String> favVenueTable;

        @FXML
        private TableColumn<String, String> favVenueColumn;

    @FXML
    private ImageView artistPicture;

    @FXML
    private Label artistLabel;

    @FXML
    private Label concertLabel;

    @FXML
    private Label pageLabel;

    @FXML
    private Button backPageButton;

    @FXML
    private Button nextPageButton;

    @FXML
    private Rectangle pageBackground;

    @FXML
    private Button backFavorites;

    /**
     * Decrements pageCount object, then re-searches for matching artist page
     */
    @FXML
    void DecrementPageNum() {
        page.DecrementCount();

        Document doc = Main.SearchSetlist(artistField.getText(), page);
        ObservableList<Concert> table = Main.GetConcerts(doc);
        InitializeConcertTable(table);
    }

    /**
     * Increments pageCount object, then re-searches for matching artist page
     */
    @FXML
    void IncrementPageNum() {
        try {
            page.IncrementCount();
            Document doc = Main.SearchSetlist(artistField.getText(), page);
            ObservableList<Concert> table = Main.GetConcerts(doc);
            InitializeConcertTable(table);
        }
        catch (Exception e){
            nextPageButton.setVisible(false);
            page.DecrementCount();
        }
    }

    @FXML
    void ResetPageCount() {
        page.ResetCount();
    }

    /**
     * Searches setlists based on artist or venue and fills table
     */
    @FXML
    void SearchSetlists() {
        Document doc = Main.SearchSetlist(artistField.getText(), page);
        ObservableList<Concert> table = Main.GetConcerts(doc);
        InitializeConcertTable(table);
        page.setLastPage(Main.GetLastPage(doc));
    }

    /**
     * Adds artist to favorites if not already there, then adds to database
     */
    @FXML
    void SaveArtist() {
         String artistName = artistLabel.getText();
         ArrayList<String> favoriteArtists = Main.ReadArtistDatabase();

        if (!favoriteArtists.contains(artistName)){
            favoriteArtists.add(artistName);
            Main.CreateArtistDatabase(favoriteArtists);
            System.out.println("Added artist to favorites.");
        }
        else{
            System.out.println("Artist is already in favorites list.");
        }
    }

    /**
     * Adds venue to favorites if not already there, then adds to database
     */
    @FXML
    void SaveVenue() {
        String venue = concertTable.getSelectionModel().getSelectedItem().venueProperty().getValue();
        ArrayList<String> favoriteVenues = Main.ReadVenueDatabase();

        if (!favoriteVenues.contains(venue)){
            favoriteVenues.add(venue);
            Main.CreateVenueDatabase(favoriteVenues);
            System.out.println("Added venue to favorites.");
        }
        else{
            System.out.println("Venue is already in favorites list.");
        }
    }

    @FXML
    void ShowHomeTab() {
        tabPane.getSelectionModel().select(homeTab);
    }

    @FXML
    void ShowSearchTab() {
        tabPane.getSelectionModel().select(searchTab);
        concertTable.getItems().clear();
        page.ResetCount();
        pageBackground.setVisible(false);
        pageLabel.setVisible(false);
        backPageButton.setVisible(false);
        nextPageButton.setVisible(false);
        backFavorites.setVisible(false);
    }

    /**
     * Reads database and fills tables with favorites
     */
    @FXML
    void ShowFavoritesTab() {
        tabPane.getSelectionModel().select(favoritesTab);
        ObservableList<String> artistTable = FXCollections.observableArrayList(Main.ReadArtistDatabase());
        InitializeArtistTable(artistTable);
        ObservableList<String> venueTable = FXCollections.observableArrayList(Main.ReadVenueDatabase());
        InitializeVenueTable(venueTable);

    }

    /**
     * @param list List of Concert objects
     */
    private void InitializeConcertTable(ObservableList<Concert> list){
        concertTable.getItems().clear();
        InitializePageLabel();

        artistColumn.setCellValueFactory(cellData -> cellData.getValue().artistProperty());
        venueColumn.setCellValueFactory(cellData -> cellData.getValue().venueProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

        artistColumn.setSortable(false);
        venueColumn.setSortable(false);
        dateColumn.setSortable(false);

        concertTable.setItems(list);
    }

    private void InitializeSongTable(ObservableList<String> list){
        songTable.getItems().clear();
        songColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue()));

        songColumn.setSortable(false);
        songTable.setItems(list);
    }

    private void InitializeArtistTable(ObservableList<String> list) {
        favArtistTable.getItems().clear();
        favArtistColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue()));

        favArtistTable.setItems(list);
    }

    private void InitializeVenueTable(ObservableList<String> list) {
        favVenueTable.getItems().clear();
        favVenueColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue()));

        favVenueTable.setItems(list);
    }

    private void InitializePageLabel(){
        int count = page.getCount();
        pageBackground.setVisible(true);
        pageLabel.setVisible(true);
        pageLabel.setText("Page " + count);
        backPageButton.setVisible(true);
        nextPageButton.setVisible(true);

        if (page.getCount() == 1){
            backPageButton.setVisible(false);
        }

        if (page.getCount() == page.getLastPage()){
            nextPageButton.setVisible(false);
        }
    }

    /**
     * Searches setlist based favorite artist table and fills concert table
     * @param event Used to see if user double-clicked with left mouse button
     */
    @FXML
    void SearchArtists(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) { //Needs a double click from the left mouse button
            ShowSearchTab();
            Document doc = Main.SearchSetlist(favArtistTable.getSelectionModel().getSelectedItem(), page);
            ObservableList<Concert> table = Main.GetConcerts(doc);
            InitializeConcertTable(table);
            page.setLastPage(Main.GetLastPage(doc));
            tabPane.getSelectionModel().select(searchTab);
            artistField.setText(favArtistTable.getSelectionModel().getSelectedItem());
            backFavorites.setVisible(true);
        }
    }


    /**
     * Searches setlist based on favorite venue and fills concert table
     * @param event Used to see if user double-clicked with left mouse button
     */
    @FXML
    void SearchVenues(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) { //Needs a double click from the left mouse button
            ShowSearchTab();
            Document doc = Main.SearchSetlist(favVenueTable.getSelectionModel().getSelectedItem(), page);
            ObservableList<Concert> table = Main.GetConcerts(doc);
            InitializeConcertTable(table);
            page.setLastPage(Main.GetLastPage(doc));
            artistField.setText(favVenueTable.getSelectionModel().getSelectedItem());
            backFavorites.setVisible(true);
        }
    }

    /**
     * Searches setlist based on user input from text box, fills table, and sets artist image
     * @param event Used to see if user double-clicked with left mouse button
     */
    @FXML
    void SelectConcert(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) { //Needs a double click from the left mouse button
            String date = concertTable.getSelectionModel().getSelectedItem().dateProperty().getValue();
            String artist = concertTable.getSelectionModel().getSelectedItem().artistProperty().getValue();
            Document oldDoc = Main.SearchSetlist(artistField.getText(), page);
            Document newDoc;
            ObservableList<String> songTable;
            newDoc = Main.GetConcertByDate(date, artist, oldDoc);

            try {
                songTable = Main.GetSongs(newDoc);
            }
            catch (Exception e){
                newDoc = Main.GetConcertByDate(date, oldDoc);
                songTable = Main.GetSongs(newDoc);
            }

            String url = Main.GetImageUrl(newDoc);
            String defaultUrl = "https://images.wallpaperscraft.com/image/guitar_music_strings_bass_guitar_electric_guitar_82964_300x168.jpg";
            Image png;
            if (url.contains(".cdn")) { //URL's like this will not load correctly
                url = defaultUrl;
            }
            png = new Image(url);
            artistPicture.setImage(png);

            InitializeSongTable(songTable);
            String artistName = concertTable.getSelectionModel().getSelectedItem().artistProperty().getValue();
            artistLabel.setText(artistName);
            String concertDetails = "at " + concertTable.getSelectionModel().getSelectedItem().venueProperty().getValue() + "\n(" +
                    concertTable.getSelectionModel().getSelectedItem().dateProperty().getValue() + ")";
            concertLabel.setText(concertDetails);
            tabPane.getSelectionModel().select(setlistTab);

        }
    }
}
