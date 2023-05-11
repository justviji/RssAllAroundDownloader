package com.example.downloaderinfo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

    public TextArea insertTextArea;
    public Label chosenPath;
    public Label recentlyDownloadedFile;
    public File pathToDownloadTo;
    File getDirectory(){
        File fileChosen= null;
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChosen = fileChooser.showDialog(Downloader.stage);
        return fileChosen;
    }
    @FXML
    File getFile(){
        File fileChosen= null;
        FileChooser fileChooser = new FileChooser();
        fileChosen = fileChooser.showOpenDialog(Downloader.stage);
        return fileChosen;
    }
    File getRssFile(){
        File f;
        do{
            f = getFile();
        }while (!f.getName().endsWith(".rss"));
        return f;
    }
    @FXML
    void addChosenElementToFile() throws IOException {
        File rssFilePath = new File("rss.txt");
        if (!new File("./rss.txt").exists()){
            if(!rssFilePath.createNewFile()){
                System.out.println("error creating file");
            }
        }else {
            rssFilePath = new File("./rss.txt");
        }
        //BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(rssFilePath.getPath())));
        FileOutputStream fos = new FileOutputStream(rssFilePath, true);
        fos.write(insertTextArea.getText().getBytes());
        fos.close();
    }

    public void addButtonAction(ActionEvent actionEvent) {
        Popup popup = new Popup();
        VBox popupContent = new VBox();
        popup.setWidth(400f);
        popup.setHeight(400f);


        FlowPane buttons = new FlowPane();
        Button closeButton = new Button("Cancel");
        closeButton.setStyle(
                " -fx-alignment: center; " +
                "-fx-font-family: 'Montserrat';" +
                "-fx-spacing: 12; " +
                "-fx-font-size: 12"
        );
        buttons.getChildren().setAll(closeButton);
        Label l = new Label("Change current to the add schene");
        l.setStyle(
                "-fx-alignment: center; " +
                "-fx-font-family: 'Montserrat';" +
                "-fx-spacing: 14; -fx-font-size: 14"
        );
        popupContent.getChildren().setAll(l,buttons);

        closeButton.setOnMouseClicked(event -> {
            popup.hide();
        });
        popupContent.getStyleClass().add("popup-content");
        //TODO currentSchene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("com/example/downloaderinfo/popup.css")).toExternalForm());

        popup.show(currentSchene.getWindow());
    }

    public void printRss(ActionEvent actionEvent) {

    }
    public void returnToMainMenu(ActionEvent actionEvent){
        Popup popup = new Popup();
        VBox popupContent = new VBox();
        popup.setWidth(400f);
        popup.setHeight(400f);


        FlowPane buttons = new FlowPane();
        Button closeButton = new Button("Cancel");
        closeButton.setStyle(" -fx-alignment: center; -fx-font-family: 'Montserrat';-fx-spacing: 12; -fx-font-size: 12");
        Button changeSchene = new Button("Confirm");
        changeSchene.setStyle("s-fx-alignment: center; -fx-font-family: 'Montserrat';-fx-spacing: 12; -fx-font-size: 12");
        buttons.getChildren().setAll(changeSchene, closeButton);
        Label l = new Label("Change current to the main menu");
        l.setStyle(" -fx-alignment: center; -fx-font-family: 'Montserrat';-fx-spacing: 14; -fx-font-size: 14");
        popupContent.getChildren().setAll(l,buttons);

        closeButton.setOnMouseClicked(event -> {
            popup.hide();
        });
        changeSchene.setOnMouseClicked(a ->{
            Downloader.stage.setScene(Downloader.defaultScene);
            currentSchene = Downloader.defaultScene;
            popup.hide();
        });
        popupContent.getStyleClass().add("popup-content");
        //TODO currentSchene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("com/example/downloaderinfo/popup.css")).toExternalForm());
        popup.show(currentSchene.getWindow());
    }
    @FXML
    void setFileToDownloadTo(){
        FileChooser directoryChooser = new FileChooser();
        pathToDownloadTo = directoryChooser.showOpenDialog(Downloader.stage);
        chosenPath.setText(pathToDownloadTo.getAbsolutePath());
    }

    @FXML
    public void downloadNoFileAdd() throws IOException {
        if (insertTextArea.getText().trim().isEmpty()){
            recentlyDownloadedFile.setText("INSERT A URL IN THE BOX!");
            return;
        }
        if (!pathToDownloadTo.exists()){
            pathToDownloadTo = getRssFile();
        }
        RSSFeedParser.setUrl(new URL(insertTextArea.getText().trim()));
        Feed feed = RSSFeedParser.readFeed();
        recentlyDownloadedFile.setText(feed.toString());
        InputStream is = RSSFeedParser.read();
        FileWriter fileWriter = new FileWriter(pathToDownloadTo);
        fileWriter.append(Arrays.toString(is.readAllBytes()));



    }

    public void downloadWithFileAdd(ActionEvent actionEvent) throws Exception {
        //downloadNoFileAdd();
        File rssFile = new File("./rss_feed.txt");
        FileWriter fw = new FileWriter(rssFile);
        String FILE_URL = insertTextArea.getText().trim();
        fw.append(FILE_URL).append("\n");

    }
}
