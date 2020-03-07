package com.example.xmlparser;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable {
    private String title;
    private String original_title;
    private String length;
    private String genres;
    private String link;
    private String summary;
    private String photo;

    public Event(String title, String original, String length, String genres, String summary, String link, String photo) {
        this.title = title;
        this.original_title = original;
        this.length = convertLength(length);
        this.genres = genres;
        this.summary = summary;
        this.link = link;
        this.photo = changeToHttps(photo);
    }

    public Event(Parcel in){
        this.title = in.readString();
        this.original_title = in.readString();
        this.length = in.readString();
        this.genres = in.readString();
        this.link = in.readString();
        this.summary =  in.readString();
        this.photo = in.readString();
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

    public String getGenres() {
        return genres;
    }

    public String getLength() {
        return length;
    }

    public String getOriginal() {
        return original_title;
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

    public String convertLength(String minutes) {
        int minInt = Integer.parseInt(minutes);
        int hours = minInt / 60;
        minInt = minInt % 60;

        return hours + "h " + minInt + "min";
    }

    @Override
    public String toString() {
        return title + "\n" + summary;
    }

    public static final Parcelable.Creator<Event> CREATOR
            = new Parcelable.Creator<Event>() {
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
