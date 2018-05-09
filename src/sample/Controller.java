package sample;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.jsoup.nodes.Document;

import javax.print.Doc;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.*;

import javafx.beans.property.StringProperty;

public class Controller {

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab homeTab;

    @FXML
    private Tab statsTab;

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
    private ImageView artistPicture;

    @FXML
    private Label artistLabel;

    @FXML
    private Label concertLabel;

    @FXML
    void SearchSetlists(ActionEvent event) {
        Document doc = Main.SearchArtist(artistField.getText());
        ObservableList<Concert> table = Main.GetConcerts(doc);
        InitializeConcertTable(table);
    }

    @FXML
    void ShowHomeTab(ActionEvent event) {
        tabPane.getSelectionModel().select(homeTab);
    }
    @FXML
    void ShowSearchTab(ActionEvent event) {
        tabPane.getSelectionModel().select(searchTab);
        concertTable.getItems().clear();
    }
    @FXML
    void ShowStatsTab(ActionEvent event) {
        tabPane.getSelectionModel().select(statsTab);
    }

    void InitializeConcertTable(ObservableList<Concert> list){
        concertTable.getItems().clear();

        artistColumn.setCellValueFactory(cellData -> cellData.getValue().artistProperty());
        venueColumn.setCellValueFactory(cellData -> cellData.getValue().venueProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

        artistColumn.setSortable(false);
        venueColumn.setSortable(false);
        dateColumn.setSortable(false);

        concertTable.setItems(list);
    }

    void InitializeSongTable(ObservableList<String> list){
        songTable.getItems().clear();
        songColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue()));

        songColumn.setSortable(false);
        songTable.setItems(list);

    }

    @FXML
    void SelectConcert(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) { //Needs a double click from the left mouse button
            String date = concertTable.getSelectionModel().getSelectedItem().dateProperty().getValue();
            Document oldDoc = Main.SearchArtist(artistField.getText());

            Document newDoc = Main.GetConcertByDate(date, oldDoc);
            ObservableList<String> songTable = Main.GetSongs(newDoc);

            String url = Main.GetImageUrl(newDoc);
            String defaultUrl = "https://images.wallpaperscraft.com/image/guitar_music_strings_bass_guitar_electric_guitar_82964_300x168.jpg";
            Image png;

            if (url.contains("i1.cdn")) { //URL's like this will not show up, nor will they throw any errors
                url = defaultUrl;
            }
            try {
                png = new Image(url);
            }
            catch (Exception e){
                png = new Image(defaultUrl);
            }

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
