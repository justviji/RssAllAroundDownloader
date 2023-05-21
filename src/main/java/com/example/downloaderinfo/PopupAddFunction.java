package com.example.downloaderinfo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class PopupAddFunction {
    public ScrollPane scrollpaneMid = new ScrollPane();

    private static void downloadMedia(String url, String mp3Url, String targetDirectory, String podcastName, String description, String author) {
        if (!url.isEmpty()) {
            url = addProtocolIfNeeded(url);
            downloadFile(url, targetDirectory, replaceInvalidCharacters(podcastName), replaceInvalidCharacters(podcastName) + "-image.jpg");
        }

        if (!mp3Url.isEmpty()) {
            mp3Url = addProtocolIfNeeded(mp3Url);
            downloadFile(mp3Url, targetDirectory, replaceInvalidCharacters(podcastName), replaceInvalidCharacters(podcastName) + "-audio.mp3");
        }

        savePodcastInfo(targetDirectory, podcastName, description, author);
    }

    private static String addProtocolIfNeeded(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            return "https://" + url; // Assuming HTTPS as the default protocol, change it to "https://" if needed
        }
        return url;
    }


    private static void downloadFile(String fileUrl, String targetDirectory, String podcastName, String fileName) {
        Thread t = new Thread(()->{
            try (BufferedInputStream in = new BufferedInputStream(new URL(fileUrl).openStream())) {
                Path subfolderPath = Path.of(targetDirectory, replaceInvalidCharacters(podcastName));
                Files.createDirectories(subfolderPath);

                Path outputPath = Path.of(subfolderPath.toString(), fileName);
                Files.copy(in, outputPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Downloaded file: " + outputPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t.start();
    }
    private static void savePodcastInfo(String targetDirectory, String podcastName, String description, String author) {
        Path subfolderPath = Path.of(targetDirectory, replaceInvalidCharacters(podcastName));
        Path outputPath = subfolderPath.resolve("podcast_info.txt");

        try {
            Files.createDirectories(subfolderPath);
            if (!Files.exists(outputPath)) {
                Files.createFile(outputPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8)) {
            writer.write("Podcast Name: " + podcastName + "\n");
            writer.newLine();
            writer.write("Description: " + description + "\n");
            if (!author.isBlank()) {
                writer.write("Author: " + author + "\n");
            } else {
                writer.write("Author: none specified\n");
            }
            System.out.println("Podcast info saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static String replaceInvalidCharacters(String input) {
        StringBuilder output = new StringBuilder();
        for (char c : input.toCharArray()) {
            if(output.length() > 2) {
                if ((c == '<' || c == '>' || c == ':' || c == '"' || c == '/' || c == '|' || c == '?' || c == '*' || c == '#')) {
                    //output.append(c);
                    System.out.println("replaced illegal character " + c);

                } else {

                    output.append(c);
                }
            }else{
                output.append(c);
            }
        }
        return output.toString();
    }



    @FXML
    void initialize(){
        ArrayList<PodcastEntry> podcasts = ReadRssFeedDomParser.readCSV(Downloader.csvFileForSavingRss.getAbsolutePath());
        scrollpaneMid.setPannable(true);
        VBox scrollpaneMidSingleFeedWrapper = new VBox();
        scrollpaneMid.setContent(scrollpaneMidSingleFeedWrapper);
        podcasts.forEach((podcastEntry)->{
            VBox separateDescFromTitle = new VBox();
            HBox singnleElement = new HBox();
            separateDescFromTitle.getChildren().add(singnleElement);
            Label label = new Label(podcastEntry.getTitle());
            label.setStyle("-fx-border-style: none none solid none;-fx-border-color: #000000");
            label.setMaxWidth(600);
            label.setWrapText(true);
            Button downloadButton = new Button("Download");
            downloadButton.setOnAction(event->{
                downloadMedia(podcastEntry.getImageUrl(), podcastEntry.getMp3Url(), Downloader.downloadPath.getAbsolutePath(), podcastEntry.getTitle(), podcastEntry.getDescription(),podcastEntry.author);
            });
            singnleElement.getChildren().addAll(label,downloadButton);
            scrollpaneMidSingleFeedWrapper.getChildren().add(separateDescFromTitle);
        });
    }

    public void exit(ActionEvent actionEvent) {
        // Close the popup window

    }
}
