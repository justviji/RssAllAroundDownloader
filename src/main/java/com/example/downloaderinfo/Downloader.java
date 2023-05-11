package com.example.downloaderinfo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class Downloader extends Application {

    public static Scene defaultScene;
    public static Scene addPaneSchene;
    public static Scene showElements;
    public static Scene settingsSchene;
    public static Stage stage;
    public static Scene currentSchene;
    public static File downloadPath;
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 650;


    @Override
    public void start(Stage stage) throws IOException {

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