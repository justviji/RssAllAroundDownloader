package com.example.downloaderinfo;

import java.io.File;
import java.util.Objects;

public class ShowPaneController {

    void updatePodcasts(){

        for (File f:
                Objects.requireNonNull(
                    Downloader.
                            downloadPath.
                            getAbsoluteFile().
                            listFiles()
                    )
        )
        {
            //TODO:RSSFeedParser.setUrl(f.getAbsolutePath());
            RSSFeedParser.readFeed();

        }
    }


}
