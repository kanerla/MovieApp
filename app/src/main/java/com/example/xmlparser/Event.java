package com.example.xmlparser;

public class Event {
    public final String title;
    public final String link;
    public final String summary;

    public Event(String title, String summary, String link) {
        this.title = title;
        this.summary = summary;
        this.link = link;
    }

    @Override
    public String toString() {
        return title;
    }
}
