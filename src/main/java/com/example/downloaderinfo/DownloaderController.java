package com.example.downloaderinfo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

import java.io.*;
import java.util.Objects;

import static com.example.downloaderinfo.Downloader.*;
import static com.example.downloaderinfo.ShowPaneController.currentlyPlayingAudio;

public class DownloaderController {

    public Label currentlyPlaying;
    public void rssChangeToAddSchene(ActionEvent actionEvent) {
        currentSchene = addPaneSchene;
        stage.setScene(currentSchene);

    }
    public void changeToYourPodcasts(ActionEvent actionEvent){
        currentSchene = showElements;
        stage.setScene(currentSchene);
    }
    public void showAvailiblePodcasts(ActionEvent actionEvent) throws IOException {
        AddPaneController.showAvailiblePodcasts();
    }
    @FXML
    void returnToMainMenu(){
        currentSchene = defaultScene;
        stage.setScene(currentSchene);
    }
    @FXML
    void goToSettingsSchene(){
        currentSchene = settingsSchene;
        stage.setScene(currentSchene);
    }
    @FXML
    void browseAvaiblePodcasts() throws IOException {
        AddPaneController.showAvailiblePodcasts();
    }

    public void startStop(ActionEvent actionEvent) {
        if (podcastStopped){
            if (currentlyPlayingAudio != null) {
                currentlyPlayingAudio.stop();
                currentlyPlayingAudio.dispose();
                currentlyPlayingAudio = null;
            }
        }else{
            currentlyPlayingAudio.play();
        }
    }
    @FXML
    void initialize(){
        if (Downloader.currentlyPlaying != null)
            currentlyPlaying.setText(Downloader.currentlyPlaying.title);
    }
}


