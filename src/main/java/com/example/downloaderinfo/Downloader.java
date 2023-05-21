package com.example.downloaderinfo;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;



public class Downloader extends Application {

    public static Scene defaultScene;
    public static Scene addPaneSchene;
    public static Scene showElements;
    public static Scene settingsSchene;
    public static Stage stage;
    public static Scene currentSchene;
    public static File downloadPath = new File(System.getProperty("user.dir"));
    public static File csvFileForSavingRss = new File("./feed-entries.csv");
    public static File settings = new File("./settings.txt");
    public static File defaultSettings = new File("./default-settings.txt");
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 650;
    public static boolean podcastStopped = true;

    public static PodcastEntry currentlyPlaying;



    void createDefaultSettingsFile(File override) throws IOException {
        FileWriter fw = new FileWriter(override);
        fw.write("""
                #do not move the settings file somewhere else it won't be read
                
                #download path:
                
                ./downloads/
                
                #csv for the accessed feeds"
                
                ./feed-entries.csv
                
                #current location of the settings file:
                
                ./settings.txt
                
                #update in here or in the settings of your instance of your reader to modify any value
                
                #if this file is not found or has errors in it it will reset to default and must be changed again!
                """);
        fw.close();

    }
    public static void updateSettings() throws FileNotFoundException {
        try (BufferedReader br = new BufferedReader(new FileReader(settings))) {
            String line;
            int settingsOffset = 0;
            while ((line = br.readLine()) != null) {
                if (line.trim().charAt(0) != '#'){
                    switch (settingsOffset) {
                        case 0 -> downloadPath = new File(line.trim());
                        case 1 -> csvFileForSavingRss = new File(line.trim());
                        case 2 -> settings = new File(line.trim());
                    }
                    settingsOffset++;

                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    @Override
    public void start(Stage stage) throws IOException {
        if (!defaultSettings.exists()){
            if(defaultSettings.createNewFile()){
                //createDefaultSettingsFile(settings);
                createDefaultSettingsFile(defaultSettings);
            }
        }

        Downloader.stage = stage;
        //first schene and main menu
        FXMLLoader fxmlLoader = new FXMLLoader(Downloader.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
        defaultScene = scene;
        //get the rss feed from a url-> TODO:get the rss feed will be called in the browse also!
        FXMLLoader addPane = new FXMLLoader(Downloader.class.getResource("add-pane.fxml"));
        Downloader.addPaneSchene = new Scene(addPane.load(), WIDTH, HEIGHT);

        //TODO: show the rss feeds or the currently playing rss feed -> probably in another class for convenience
        FXMLLoader show = new FXMLLoader(Downloader.class.getResource("show-pane.fxml"));
        showElements = new Scene(show.load(), WIDTH, HEIGHT);
        //settings
        FXMLLoader settingsFxmlloader = new FXMLLoader(Downloader.class.getResource("settings-fe.fxml"));
        settingsSchene = new Scene(settingsFxmlloader.load(), WIDTH, HEIGHT);

        currentSchene = defaultScene;
        stage.setTitle("Rss feed all around application");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}