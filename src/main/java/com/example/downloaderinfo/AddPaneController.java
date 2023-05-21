package com.example.downloaderinfo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import static com.example.downloaderinfo.Downloader.*;
import static com.example.downloaderinfo.ShowPaneController.currentlyPlayingAudio;


public class AddPaneController {

    public TextField insertTextField;
    private static Popup viewPopup;
    @FXML
    Label currentlyPlaying;

    public void parseRssAndAddToCSV(ActionEvent actionEvent) {
        ReadRssFeedDomParser.parse(insertTextField.getText());
    }


    public TextField getInsertTextField() {
        return insertTextField;
    }

    public void setInsertTextField(TextField insertTextField) {
        this.insertTextField = insertTextField;
    }

    public static Popup getViewPopup() {
        return viewPopup;
    }

    public static void setViewPopup(Popup viewPopup) {
        AddPaneController.viewPopup = viewPopup;
    }

    @FXML
    public static void showAvailiblePodcasts() throws IOException {
        viewPopup = new Popup();
        //PopupAddFunction controller = new PopupAddFunction();
        FXMLLoader loader = new FXMLLoader(AddPaneController.class.getResource("popup-add-function.fxml"));
        //loader.setController(controller);
        viewPopup.getContent().setAll((Node) loader.load());
        Button closeButton = new Button("close");
        closeButton.setOnAction(e->{
            stage.hide();
            stage.show();
        });
        viewPopup.getContent().add(closeButton);
        closeButton.setStyle(" -fx-background-color: #1c1c1c;\n" +
                "    -fx-alignment: baseline-left;\n" +
                "    -fx-font-family: \"Sans Serif\";\n" +
                "    -fx-font-size: 24;\n" +
                "    -fx-fill: #2c2c2c;\n" +
                "    -fx-pref-columns: 1;\n" +
                "    -fx-pref-height: 200;\n" +
                "    -fx-pref-width: 200;\n" +
                "    -fx-max-height: 50;\n" +
                "    -fx-max-width: 300;\n" +
                "    -fx-border-radius: 4;\n" +
                "    -fx-text-fill: #7f7f7f;\n" +
                "    -fx-min-width: 50;\n" +
                "    -fx-min-height: 50;");
        viewPopup.show(stage);
    }

    public void showAvailiblePodcasts(ActionEvent actionEvent) throws IOException {
        showAvailiblePodcasts();
    }
    public void changeToYourPodcasts(ActionEvent actionEvent){
        currentSchene = showElements;
        stage.setScene(currentSchene);
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
