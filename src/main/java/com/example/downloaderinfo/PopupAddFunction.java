package com.example.downloaderinfo;

import javafx.fxml.FXML;

import java.util.ArrayList;

public class PopupAddFunction {
    @FXML
    void initialize(){
        ArrayList<PodcastEntry> podcasts = ReadRssFeedDomParser.readCSV(Downloader.csvFileForSavingRss.getAbsolutePath());
    }
}
