package com.example.downloaderinfo;

public class PodcastEntry {
    public  String title = "";
    public  String mp3Url = "";
    public  String imageUrl = "";
    public  String author = "";
    public  String description = "";
    @Override
    public String toString(){
        return "Title: " + title + "\nMP3 URL: " + mp3Url + "\nImage URL: " + imageUrl +"\nAuthor: " + author + "\ndescription: " + description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMp3Url() {
        return mp3Url;
    }

    public void setMp3Url(String mp3Url) {
        this.mp3Url = mp3Url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
