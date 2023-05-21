package com.example.downloaderinfo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static com.example.downloaderinfo.Downloader.*;
import static com.example.downloaderinfo.ShowPaneController.currentlyPlayingAudio;

public class SettingsController {
    public Label currentlyPlaying;
    //TODO: directory chooser for directory to download to
    //TODO: save settings in json or xml
    //TODO: other settings like <insert idea here>
    @FXML
    TextField textFieldChooseDirToDownloadTo = new TextField(); //the text field where to download to
    File directoryToDownloadTo;
    File tempDir;
    File settingsFile;
    @FXML
    public void initialize(){
        tempDir = new File("./");
        System.out.println(tempDir.getName());
        getSettingsFromSettingsFile();
        if (Downloader.currentlyPlaying != null)
            currentlyPlaying.setText(Downloader.currentlyPlaying.title);
        
    }


    @FXML
    void setTempDir(){
        tempDir = new File(textFieldChooseDirToDownloadTo.getText());
        if (tempDir.isDirectory()){
            return;
        }else if (tempDir.isFile()){
            tempDir = tempDir.getParentFile();
            return;
        }
        else {
            System.out.println("error in settings controller set temp dir -> not a file or directory or is null");
        }
        throw new RuntimeException();
    }
    @FXML
    void chooseDirToDownloadTo() {
        if (!textFieldChooseDirToDownloadTo.getText().isBlank()) {
            File newDownloadPath = new File(textFieldChooseDirToDownloadTo.getText());
            if (newDownloadPath.isDirectory()) {
                Downloader.downloadPath = newDownloadPath;

                // Update paths in the settings file
                updateSettingsFile(newDownloadPath.getAbsolutePath(), csvFileForSavingRss.getAbsolutePath(), settings.getAbsolutePath());
                return;
            } else if (newDownloadPath.isFile()) {
                newDownloadPath = newDownloadPath.getParentFile();
                Downloader.downloadPath = newDownloadPath;

                // Update paths in the settings file
                updateSettingsFile(newDownloadPath.getAbsolutePath(), csvFileForSavingRss.getAbsolutePath(), settings.getAbsolutePath());
                return;
            } else {
                System.out.println("Error in settings controller choose dir -> not a file or directory or is null");
            }
            throw new RuntimeException();
        }
    }
    @FXML
    void chooseFileForCSV() {
        if (!textFieldChooseDirToDownloadTo.getText().isBlank()) {
            File newDownloadPath = new File(textFieldChooseDirToDownloadTo.getText());
            if (newDownloadPath.isDirectory()) {
                Downloader.downloadPath = newDownloadPath;

                // Update CSV file path
                String csvFilePath = "./feed-entries.csv";
                updateSettingsFile(Downloader.settings.getAbsolutePath(), csvFilePath, settings.getAbsolutePath());

                return;
            } else if (newDownloadPath.isFile()) {
                newDownloadPath = newDownloadPath.getParentFile();
                Downloader.downloadPath = newDownloadPath;

                // Update CSV file path
                String csvFilePath = "./feed-entries.csv";
                updateSettingsFile(Downloader.settings.getAbsolutePath(), csvFilePath, settings.getAbsolutePath());

                return;
            } else {
                System.out.println("Error in settings controller choose dir -> not a file or directory or is null");
            }
            throw new RuntimeException();
        }
    }



    private void updateSettingsFile(String downloadPath, String feedEntriesCSVPath, String settingsFilePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(settingsFilePath))) {
            // Write the updated settings to the file
            writer.write("#do not move the settings file somewhere else it won't be read\n");
            writer.write("\n");
            writer.write("#download path:\n");
            writer.write(downloadPath + "\n");
            writer.write("\n");
            writer.write("#csv for the accessed feeds\"\n");
            writer.write(feedEntriesCSVPath + "\n");
            writer.write("\n");
            writer.write("#current location of the settings file:\n");
            writer.write(settingsFilePath + "\n");
            writer.write("\n");
            writer.write("#update in here or in the settings of your instance of your reader to modify any value\n");
            writer.write("\n");
            writer.write("#if this file is not found or has errors in it, it will reset to default and must be changed again! settings file");

            System.out.println("Settings file updated.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void chooseSettingsPath(){
        directoryToDownloadTo = new File(textFieldChooseDirToDownloadTo.getText());
        if (directoryToDownloadTo.isDirectory()){
            return;
        }else if (directoryToDownloadTo.isFile()){
            directoryToDownloadTo = directoryToDownloadTo.getParentFile();
            return;
        }
        else {
            System.out.println("error in settings controller choose dir -> not a file or directory or is null");
        }
        throw new RuntimeException();
    }
    void getSettingsFromSettingsFile(){
        //directoryToDownloadTo = new File()
        // TODO: get things form json File to the ram in the executable -> when changes occur or on startup you get the settings how you set them
    }
    @FXML
    void setSettingsFile(){
        settingsFile = new File(textFieldChooseDirToDownloadTo.getText());
        if (directoryToDownloadTo.isDirectory()){
            return;
        }else if (directoryToDownloadTo.isFile()){
            directoryToDownloadTo = directoryToDownloadTo.getParentFile();
            return;
        }
        else {
            System.out.println("error in settings controller choose dir -> not a file or directory or is null");
        }
        throw new RuntimeException();
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
    public void changeToYourPodcasts(ActionEvent actionEvent){
        currentSchene = showElements;
        stage.setScene(currentSchene);
    }


    public void showAvailiblePodcasts(ActionEvent actionEvent) throws IOException {
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
}
