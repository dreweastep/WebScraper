package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.jsoup.nodes.Document;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;

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
    void SearchSetlists(ActionEvent event) {
        Document doc = Main.SearchArtist(artistField.getText());
        ObservableList<Concert> table = Main.GetConcerts(doc);
        InitializeTable(table);
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

    void InitializeTable(ObservableList<Concert> list){
        concertTable.getItems().clear();

        artistColumn.setCellValueFactory(cellData -> cellData.getValue().artistProperty());
        venueColumn.setCellValueFactory(cellData -> cellData.getValue().venueProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

        artistColumn.setSortable(false);
        venueColumn.setSortable(false);
        dateColumn.setSortable(false);

        concertTable.setItems(list);
    }

    @FXML
    void SelectConcert(MouseEvent event) {
        //Needs a double click from the left mouse button
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
            String date = concertTable.getSelectionModel().getSelectedItem().dateProperty().getValue();
            Document oldDoc = Main.SearchArtist(artistField.getText());

            Document newDoc = Main.GetConcertByDate(date, oldDoc);

        }
    }
}
