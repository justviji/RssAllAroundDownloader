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

import static com.example.downloaderinfo.Downloader.currentSchene;
import static com.example.downloaderinfo.Downloader.stage;



public class AddPaneController {

    public TextField insertTextField;

    public void parseRssAndAddToCSV(ActionEvent actionEvent) {
        ReadRssFeedDomParser.parse(insertTextField.getText());
    }


    public void showAvailiblePodcasts() throws IOException {
        Popup viewPopup = new Popup();
        //PopupAddFunction controller = new PopupAddFunction();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("popup-add-function.fxml"));
        //loader.setController(controller);
        viewPopup.getContent().setAll((Node) loader.load());
        viewPopup.show(stage);

    }
}
