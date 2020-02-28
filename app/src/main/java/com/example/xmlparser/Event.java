package com.example.xmlparser;

public class Event {
    private String title;
    private String link;
    private String summary;
    private String photo;

    public Event(String title, String summary, String link, String photo) {
        this.title = title;
        this.summary = summary;
        this.link = link;
        this.photo = changeToHttps(photo);
    }

    public String changeToHttps(String url) {
        String master = url;
        String target = "http";
        String replacement = "https";
        String processed = master.replace(target, replacement);
        return processed;
    }

    public String getLink() {
        return link;
    }

    public String getSummary() {
        return summary;
    }

    public String getPhoto() {
        return photo;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title + "\n" + summary;
    }
}
