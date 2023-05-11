package com.example.downloaderinfo;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.File;

public class SettingsController {
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
    void chooseDirToDownloadTo(){
        if(!textFieldChooseDirToDownloadTo.getText().isBlank()){
            directoryToDownloadTo = new File(textFieldChooseDirToDownloadTo.getText());
            if (directoryToDownloadTo.isDirectory()){
                Downloader.downloadPath = directoryToDownloadTo;
                return;
            }else if (directoryToDownloadTo.isFile()){
                directoryToDownloadTo = directoryToDownloadTo.getParentFile();
                Downloader.downloadPath  = directoryToDownloadTo;
                return;
            }
            else {
                System.out.println("error in settings controller choose dir -> not a file or directory or is null");
            }
            throw new RuntimeException();
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


}
