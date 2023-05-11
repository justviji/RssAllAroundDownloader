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

public class DownloaderController {


    public void changeToSettings(ActionEvent actionEvent) {
        currentSchene = settingsSchene;
        stage.setScene(currentSchene);
    }

    public void highlightImageViewNextToButton(MouseEvent mouseEvent) {
    }

    public void nothighlightImageViewNextToButton(MouseEvent mouseEvent) {
    }

    public void rssChangeToAddSchene(ActionEvent actionEvent) {
        currentSchene = addPaneSchene;
        stage.setScene(currentSchene);

    }
}


