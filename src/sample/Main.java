package sample;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.image.Image;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.*;


public class Main extends Application {

    /**
     * Sets stage My Setlist Lookup and sets icon to music note
     * @param primaryStage Window that contains application
     * @throws Exception May fail to load properly
     */
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

    /**
     * Uses Jsoup to connect to document based off of search parameter
     * @param query Creates url based off of search parameter
     * @param page Gets current page count
     * @return Document with setlist elements
     */
    public static Document SearchSetlist(String query, PageCount page) {
        Document doc = null;
        try {
            if (page.getCount() == 0) {
                doc = Jsoup.connect("https://www.setlist.fm/search?query=" + query).get();
            }
            else{
                doc = Jsoup.connect("https://www.setlist.fm/search?page=" + page.getCount() + "&query=" + query).get();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    /**
     * Reads database table of favorite artists
     * @return List of artists
     */
    public static ArrayList<String> ReadArtistDatabase(){
        ArrayList<String> favArtists = new ArrayList<>();

        try {
            String DB_URL = "jdbc:mysql://db4free.net:3306/drewconcerts";
            String USERNAME = "dreweastep";
            String PASSWORD = "Cp82UfOSriPJ0CfW";

            java.sql.Connection MyConn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = MyConn.createStatement();
            String select = "SELECT artist FROM ArtistTable";
            ResultSet rs = stmt.executeQuery(select);

            while (rs.next()) {
                favArtists.add(rs.getString("artist"));
            }
            rs.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return favArtists;
    }

    /**
     * Creates database of favorite artists after deleting the current database
     * @param artistList List of favorite artists
     */
    public static void CreateArtistDatabase(ArrayList<String> artistList) {
        String DB_URL = "jdbc:mysql://db4free.net:3306/drewconcerts";
        String USERNAME = "dreweastep";
        String PASSWORD = "Cp82UfOSriPJ0CfW";

        try {
            java.sql.Connection MyConn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            //Delete existing table
            Statement drop_stmt = MyConn.createStatement();
            String drop = "DROP TABLE ArtistTable";
            drop_stmt.executeUpdate(drop);

            //Create new table
            Statement stmt = MyConn.createStatement();
            String create = "CREATE TABLE ArtistTable " +
                    " (num INTEGER not NULL, " +
                    " artist VARCHAR(255), " +
                    " PRIMARY KEY ( num ))";
            stmt.executeUpdate(create);

            //Write data to table
            for (int num = 0; num < artistList.size(); num++){
                String artist = artistList.get(num);
                String insert = "INSERT INTO ArtistTable " +
                        "VALUES ('"+num+"', '"+artist+"')";
                stmt.executeUpdate(insert);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Reads database table of favorite venues
     * @return List of venues
     */
    public static ArrayList<String> ReadVenueDatabase(){
        ArrayList<String> favVenues= new ArrayList<>();

        try {
            String DB_URL = "jdbc:mysql://db4free.net:3306/drewconcerts";
            String USERNAME = "dreweastep";
            String PASSWORD = "Cp82UfOSriPJ0CfW";

            java.sql.Connection MyConn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            Statement stmt = MyConn.createStatement();
            String select = "SELECT venue FROM VenueTable";
            ResultSet rs = stmt.executeQuery(select);

            while (rs.next()) {
                favVenues.add(rs.getString("venue"));
            }
            rs.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return favVenues;
    }

    /**
     * Creates database of favorite venues after deleting the current database
     * @param venueList List of favorite venues
     */
    public static void CreateVenueDatabase(ArrayList<String> venueList) {
        String DB_URL = "jdbc:mysql://db4free.net:3306/drewconcerts";
        String USERNAME = "dreweastep";
        String PASSWORD = "Cp82UfOSriPJ0CfW";

        try {
            java.sql.Connection MyConn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            //Delete existing table
            Statement drop_stmt = MyConn.createStatement();
            String drop = "DROP TABLE VenueTable";
            drop_stmt.executeUpdate(drop);

            //Create new table
            Statement stmt = MyConn.createStatement();
            String create = "CREATE TABLE VenueTable " +
                    " (num INTEGER not NULL, " +
                    " venue VARCHAR(255), " +
                    " PRIMARY KEY ( num ))";
            stmt.executeUpdate(create);

            //Write data to table
            for (int num = 0; num < venueList.size(); num++){
                String venue = venueList.get(num);
                String insert = "INSERT INTO VenueTable " +
                        "VALUES ('"+num+"', '"+venue+"')";
                stmt.executeUpdate(insert);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Gets last page by searching for class value
     * @param doc Webpage to get page count from
     * @return Integer of last page based off the search parameter
     */
    public static int GetLastPage(Document doc){
        int lastPage = 0;
        Elements liList = doc.getElementsByClass("listPagingNavigator text-center hidden-print").select("li");

        for(Element li : liList){
            try {
                if (li.child(0).attr("title").equals("Go to last page")) {
                    String num = li.child(0).text();
                    lastPage = Integer.parseInt(num);
                }
            } catch (Exception e){}
        }
        return lastPage;
    }

    /**
     * Gets concert by date and artist
     * @param date Date of concert
     * @param artist Name of artist
     * @param currentDoc Document to parse
     * @return Webpage of setlist
     */
    public static Document GetConcertByDate(String date, String artist, Document currentDoc) {
        Document doc = null;
        String[] dates = date.split("/");
        Elements myElements = currentDoc.getElementsByClass("col-xs-12 setlistPreview");

        for (Element item : myElements) {
            if (item.toString().contains(dates[0]) && item.toString().contains(dates[1]) && item.toString().contains(dates[2]) && item.toString().contains(artist)) {
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

    /**
     * Overload method, because sometimes searching by date and artist fails based off of abnormal characters
     * @param date Date of Concert
     * @param currentDoc Document to parse
     * @return Webpage of setlist
     */
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

    public static String GetImageUrl(Document currentDoc) {
        return currentDoc.getElementsByClass("imageContent").first().select("img").attr("src");
    }

    /**
     * Gets list of songs played at concert
     * @param myDoc Webpage of setlist
     * @return List of songs played
     */
    public static ObservableList<String> GetSongs(Document myDoc) {
        ObservableList<String> songList = FXCollections.observableArrayList();
        Elements myElements = myDoc.getElementsByClass("songLabel");

        for (int i = 0; i < myElements.size(); i++) {
            songList.add(Integer.toString(i + 1) + ". " + myElements.get(i).text());
        }
        return songList;
    }

    /**
     * Gets concert in document and instantiates Concert object
     * @param myDoc Webpage based off of query
     * @return List of Concert objects
     */
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