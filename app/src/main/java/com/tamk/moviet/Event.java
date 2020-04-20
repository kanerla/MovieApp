package com.tamk.moviet;

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
    private String title = "";

    @ColumnInfo(name = "seen")
    private boolean seen = false;

    @ColumnInfo(name = "where")
    private String where = "";

    @ColumnInfo(name = "with")
    private String with = "";

    @ColumnInfo(name = "date")
    private String date = "";

    @ColumnInfo(name = "rating")
    private float rating = 0;

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

    /**
     * Replaces the "http" in url with "https".
     *
     * @param url   url to be replaced
     * @return      new, replaced url
     */
    private String changeToHttps(String url) {
        if (url == null) {
            return "https://ventures.wartsila.com/wp-content/uploads/2017/11/placeholder-1024x1024.png";
        }
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

    /**
     * Returns the genres associated with the event.
     *
     * @return event's genres
     */
    public String getGenres() {
        return genres;
    }

    /**
     * Returns the length of the event.
     *
     * @return length of the event in string form
     */
    public String getLength() {
        return length;
    }

    /**
     * Returns the release date of the event.
     *
     * @return  release date of the event in string form
     */
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

    /**
     * Return the summary of the event.
     *
     * @return  summary of the event in string form
     */
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

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
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

    /**
     * Assigns length to the event.
     *
     * @param length    length of the event
     */
    public void setLength(String length) {
        this.length = length;
    }

    /**
     * Assigns release date to the event.
     *
     * @param release   release date of the event in string form
     */
    public void setRelease(String release) {
        this.release = release;
    }

    /**
     * Assigns genres to the event.
     *
     * @param genres    specified genres of the event
     */
    public void setGenres(String genres) {
        this.genres = genres;
    }

    /**
     * Assigns link to the event.
     *
     * @param link  link to event's associated website
     */
    public void setLink(String link) {
        this.link = link;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     * Converts minutes into hours and minutes
     * and returns it in a string form.
     *
     * @param minutes   amount of minutes in string form
     * @return          length of the event in hours and minutes
     */
    private String convertLength(String minutes) {
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

    /**
     * Describes the kinds of special objects contained in this Parcelable instance's representation.
     * Returns either 0 or CONTENTS_FILE_DESCRIPTOR.
     *
     * @return  a bitmask indicating the set of special object types parsed
     */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
