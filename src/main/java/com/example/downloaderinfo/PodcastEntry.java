package com.example.downloaderinfo;

public class PodcastEntry {
    public static String title = "";
    public static String mp3Url = "";
    public static String imageUrl = "";
    public static String author = "";
    public static String description = "";
    @Override
    public String toString(){
        return "Title: " + title + "\nMP3 URL: " + mp3Url + "\nImage URL: " + imageUrl +"\nAuthor: " + author + "\ndescription: " + description;
    }

}
