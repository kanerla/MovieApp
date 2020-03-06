package com.example.xmlparser;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable {
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

    public Event(Parcel in){
        this.title = in.readString();
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
