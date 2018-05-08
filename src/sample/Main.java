package sample;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.stage.StageStyle;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.print.Doc;
import java.io.IOException;

import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("My Setlist Lookup");
        primaryStage.setScene(new Scene(root, 872, 581));
        primaryStage.getIcons().add(new Image("Icon.png"));
        primaryStage.show();

        //Initialize css
        Scene scene = new Scene(new Group(), 872, 581);
        scene.getStylesheets().add("path/stylesheet.css");
    }


    public static void main(String[] args) {
        launch(args);
    }


    public static Document SearchArtist(String artist) {
        Document doc = null;
        try {
            doc = Jsoup.connect("https://www.setlist.fm/search?query=" + artist).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    public static Document GetConcertByDate(String date, Document currentDoc) {
        Document doc = null;

        String[] dates = date.split("/");

        Elements myElements = currentDoc.getElementsByClass("col-xs-12 setlistPreview");

        for (Element item : myElements) {
            if (item.toString().contains(dates[0]) && item.toString().contains(dates[1]) && item.toString().contains(dates[2])) {
                String url = "https://www.setlist.fm/" + item.select("a").attr("href");

                try {
                    doc = Jsoup.connect(url).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return doc;
    }


    public static ObservableList<Concert> GetConcerts(Document myDoc){
        ObservableList<Concert> concertList = FXCollections.observableArrayList();
        Elements myElements = myDoc.getElementsByClass("col-xs-12 setlistPreview");

        for (Element item : myElements){
            String artist = item.select("a").text().split("at ")[0];
            String venue = item.select("a").text().split("at ")[1].split(artist)[0];
            String date = item.select("div > div > span.month").text() +
                    "/" + item.select("div > div > span.day").text() +
                    "/" + item.select("div > div > span.year").text();

            concertList.add(new Concert(artist, venue, date));
        }
        return concertList;
    }
}