package com.example.xmlparser;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movie_table")
public class Event implements Parcelable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "seen")
    private boolean seen = false;

    @ColumnInfo(name = "where")
    private String where = "";

    @ColumnInfo(name = "with")
    private String with = "";

    @ColumnInfo(name = "date")
    private String date = "";

    private String originalTitle;
    private String length;
    private String genres;
    private String link;
    private String summary;
    private String photo;
    private String release;

    public Event() {

    }

    public Event(String title, String original, String length, String releaseDate, String genres, String summary, String link, String photo) {
        this.title = title;
        this.originalTitle = original;
        this.length = convertLength(length);
        this.release = releaseDate;
        this.genres = genres;
        this.summary = summary;
        this.link = link;
        this.photo = changeToHttps(photo);
    }

    public Event(Parcel in){
        this.title = in.readString();
        this.originalTitle = in.readString();
        this.length = in.readString();
        this.release = in.readString();
        this.genres = in.readString();
        this.link = in.readString();
        this.summary =  in.readString();
        this.photo = in.readString();
    }

    private String changeToHttps(String url) {
        String target = "http";
        String replacement = "https";
        return url.replace(target, replacement);
    }

    /**
     * Returns a link to event's associated web page.
     *
     * @return link to event's web page.
     */
    public String getLink() {
        return link;
    }

    public String getGenres() {
        return genres;
    }

    /**
     * Returns the length of the event.
     *
     * @return length of the event in string form.
     */
    public String getLength() {
        return length;
    }

    public String getRelease() {
        return release;
    }

    /**
     * Returns the original title of the event.
     *
     * @return original title of the event.
     */
    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getSummary() {
        return summary;
    }

    public String getPhoto() {
        return photo;
    }

    /**
     * Returns title of the event.
     *
     * @return title of the event.
     */
    public String getTitle() {
        return title;
    }

    public String getWith() {
        return with;
    }

    public void setWith(String with) {
        this.with = with;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    /**
     * Assigns title to the event.
     *
     * @param title title of the event.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Assigns original title to the event.
     *
     * @param originalTitle original title of the event.
     */
    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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
