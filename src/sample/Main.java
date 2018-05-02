package sample;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.print.Doc;
import java.io.IOException;

import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
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
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 600, 450));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }


    private static Document SearchArtist() {
        Scanner in = new Scanner(System.in);
        Document doc = null;

        System.out.println("Welcome to the setlist searcher. Please enter a name of an artist: ");

        try {
            doc = Jsoup.connect("https://www.setlist.fm/search?query=" + in.nextLine()).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }
}