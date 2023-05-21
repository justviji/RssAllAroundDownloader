package com.example.downloaderinfo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class PopupAddFunction {
    public ScrollPane scrollpaneMid = new ScrollPane();
    public void download(String url){
        Thread t = new Thread(()->{

        });
    }
    private static void downloadMedia(String url, String mp3Url, String targetDirectory, String podcastName, String description, String author) {
        if (!url.isEmpty()) {
            url = addProtocolIfNeeded(url);
            downloadFile(url, targetDirectory, replaceInvalidCharacters(podcastName), replaceInvalidCharacters(podcastName) + "-image.jpg");
        }

        if (!mp3Url.isEmpty()) {
            mp3Url = addProtocolIfNeeded(mp3Url);
            downloadFile(mp3Url, targetDirectory, replaceInvalidCharacters(podcastName), replaceInvalidCharacters(podcastName) + "-audio.mp3");
        }

        savePodcastInfo(replaceInvalidCharacters(targetDirectory), podcastName, podcastName, description, author);
    }

    private static String addProtocolIfNeeded(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            return "https://" + url; // Assuming HTTPS as the default protocol, change it to "https://" if needed
        }
        return url;
    }


    private static void downloadFile(String fileUrl, String targetDirectory, String subfolderName, String fileName) {
        try (BufferedInputStream in = new BufferedInputStream(new URL(fileUrl).openStream())) {
            Path subfolderPath = Path.of(targetDirectory, subfolderName);
            Files.createDirectories(subfolderPath);

            Path outputPath = Path.of(subfolderPath.toString(), fileName);
            Files.copy(in, outputPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Downloaded file: " + outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void savePodcastInfo(String targetDirectory, String subfolderName, String podcastName, String description, String author) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(Path.of(targetDirectory, subfolderName, "podcast_info.txt").toFile()), StandardCharsets.UTF_8))) {
            writer.write("Podcast Name: " + podcastName+ "");
            writer.newLine();
            writer.write("Description: " + description);
            writer.write("Author: " + author); // Added the author information
            System.out.println("Podcast info saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String replaceInvalidCharacters(String input) {
        return input.replaceAll("[<>:\"/\\\\|?*]|(?<!^[a-zA-Z]):", "_"); // do not replace the : in pe C:
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
            Button downloadButton = new Button("Download");
            downloadButton.setOnAction(event->{
                downloadMedia(podcastEntry.getImageUrl(), podcastEntry.getMp3Url(), Downloader.downloadPath.getAbsolutePath(), podcastEntry.getTitle(), podcastEntry.getDescription(),podcastEntry.author);
            });
            singnleElement.getChildren().addAll(label,downloadButton);
            scrollpaneMidSingleFeedWrapper.getChildren().add(separateDescFromTitle);
        });
    }
}
