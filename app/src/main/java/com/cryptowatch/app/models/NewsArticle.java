package com.cryptowatch.app.models;

public class NewsArticle {
    private String title;
    private String image;
    private String source;
    private String url;

    public NewsArticle() {

    }

    public NewsArticle(String title, String image, String source, String url) {
        this.title = title;
        this.image = image;
        this.source = source;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}