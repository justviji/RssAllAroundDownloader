package com.example.downloaderinfo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javafx.util.Duration;

import static com.example.downloaderinfo.Downloader.*;
import static com.example.downloaderinfo.Downloader.currentSchene;
import static com.example.downloaderinfo.PopupAddFunction.replaceInvalidCharacters;

public class ShowPaneController {
    public static MediaPlayer currentlyPlayingAudio;
    Slider progressSlider = createProgressSlider();

    public ScrollPane scrollpanePodcastWrapper;
    @FXML
    Label currentlyPlaying;


    VBox scrollpaneMidSingleFeedWrapper;
    @FXML
    void initialize() {

         scrollpaneMidSingleFeedWrapper = new VBox();
        scrollpanePodcastWrapper.setContent(scrollpaneMidSingleFeedWrapper);

        getDownloadedPodcasts().forEach((podcastEntry) -> {
            VBox podcastBox = createPodcastBox(podcastEntry);
            scrollpaneMidSingleFeedWrapper.getChildren().add(podcastBox);
        });
        if (Downloader.currentlyPlaying != null)
            currentlyPlaying.setText(Downloader.currentlyPlaying.title);
    }


// ...

    private void playMP3File(String filePath) {
        Media media = new Media(new File(filePath).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        currentlyPlayingAudio = mediaPlayer;

        mediaPlayer.setOnReady(mediaPlayer::play);

        // Playback has ended
        mediaPlayer.setOnEndOfMedia(mediaPlayer::dispose);

        mediaPlayer.play();
    }
    private Slider createProgressSlider() {
        Slider progressSlider = new Slider();
        if (currentlyPlaying == null){
            return null;
        }

        currentlyPlayingAudio.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            if (!progressSlider.isValueChanging()) {
                progressSlider.setValue(newValue.toSeconds());
            }
        });

        progressSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (progressSlider.isValueChanging()) {
                currentlyPlayingAudio.seek(Duration.seconds(newValue.doubleValue()));
            }
        });

        currentlyPlayingAudio.setOnEndOfMedia(() -> {
            progressSlider.setValue(0);
        });

        return progressSlider;
    }


    private List<PodcastEntry> getDownloadedPodcasts() {
        // Retrieve all downloaded podcast entries from the target directory
        List<PodcastEntry> downloadedPodcasts = new ArrayList<>();
        File[] podcastFolders = Downloader.downloadPath.listFiles(File::isDirectory);
        if (podcastFolders != null) {
            for (File podcastFolder : podcastFolders) {
                File infoFile = new File(podcastFolder, "podcast_info.txt");
                if (infoFile.exists()) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(infoFile))) {
                        String line;
                        String title = "";
                        String description = "";
                        String author = "";
                        while ((line = reader.readLine()) != null) {
                            if (line.startsWith("Podcast Name: ")) {
                                title = line.substring("Podcast Name: ".length());
                            } else if (line.startsWith("Description: ")) {
                                description = line.substring("Description: ".length());
                            } else if (line.startsWith("Author: ")) {
                                author = line.substring("Author: ".length());
                            }
                        }
                        PodcastEntry podcastEntry = new PodcastEntry();
                        podcastEntry.setTitle(title);
                        podcastEntry.setAuthor(author);
                        podcastEntry.setDescription(description);
                        downloadedPodcasts.add(podcastEntry);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return downloadedPodcasts;
    }
    private void deletePodcast(PodcastEntry podcastEntry) {
        String podcastFolderPath = Downloader.downloadPath.getAbsolutePath() + "/" +
                replaceInvalidCharacters(podcastEntry.getTitle());

        // Delete the podcast folder and its contents
        File podcastFolder = new File(podcastFolderPath);
        if (podcastFolder.exists()) {
            try {
                Files.walk(podcastFolder.toPath())
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private VBox createPodcastBox(PodcastEntry podcastEntry) {
        VBox podcastBox = new VBox();
        Label titleLabel = new Label("Title: " + podcastEntry.getTitle());
        Label descriptionLabel = new Label("Description: " + podcastEntry.getDescription());
        Label authorLabel = new Label("Author: " + podcastEntry.getAuthor());
        Button playButton = new Button("Play");
        playButton.setOnAction(playEvent -> {
            String audioFilePath = Downloader.downloadPath.getAbsolutePath() + "/" +
                    replaceInvalidCharacters(podcastEntry.getTitle()) + "/" +
                    replaceInvalidCharacters(podcastEntry.getTitle()) + "-audio.mp3";
            Downloader.currentlyPlaying = podcastEntry;
            podcastStopped = false;
            playMP3File(audioFilePath);
        });

        descriptionLabel.setMaxWidth(700);

        descriptionLabel.setWrapText(true);
        String imageFilePath = Downloader.downloadPath.getAbsolutePath() + "/" +
                replaceInvalidCharacters(podcastEntry.getTitle()) + "/" +
                replaceInvalidCharacters(podcastEntry.getTitle()) + "-image.jpg";
        ImageView imageView = new ImageView(new Image(imageFilePath));
        imageView.setFitHeight(400);
        imageView.setPreserveRatio(true);

        podcastBox.getChildren().addAll(imageView, playButton, titleLabel, descriptionLabel, authorLabel);
        podcastBox.setStyle("-fx-border-style: none none solid none;-fx-border-color: #000000");
        // Create delete button
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(deleteEvent -> {
            deletePodcast(podcastEntry);
            // Remove the deleted podcast entry from the UI
            scrollpaneMidSingleFeedWrapper.getChildren().remove(podcastBox);
        });

        // Add delete button to the podcast box
        podcastBox.getChildren().add(deleteButton);
        return podcastBox;
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
