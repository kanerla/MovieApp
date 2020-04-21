package com.tamk.moviet;

import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * XmlParser is the class for parsing xml feed.
 * XmlParser returns a List of Event objects.
 * Each Event object represents a single movie in the XML feed.
 *
 * @author  Laura Kanerva
 * @version %I%, %G%
 */
public class XmlParser {
    private static final String ns = null;
    private static final String TAG = "XmlParser";

    /**
     * Method for setting up the XmlPullParser.
     * Returns collected data from the feed in a list and closes the input stream when finished.
     *
     * @param in                        stream to parse
     * @return                          list of entries in the feed
     * @throws XmlPullParserException   if parser exception occurred
     * @throws IOException              if an input or output exception occurred
     */
    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    /**
     * Reads xml feed and adds entries to a list.
     * If entry's tag name doesn't match the wanted tag, entry is skipped.
     * Returns a list of entries in xml feed.
     *
     * @param parser                    the parser used
     * @return                          list of entries in xml feed
     * @throws XmlPullParserException   if parser exception occurred
     * @throws IOException              if an input or output exception occurred
     */
    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "Events");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("Event")) {
                entries.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    /**
     * Reads an entry from xml feed and assigns values to a Event object.
     * Returns a new Event object with found values.
     *
     * @param parser                    the parser used
     * @return                          a new Event object
     * @throws XmlPullParserException   if parser exception occurred
     * @throws IOException              if an input or output exception occurred
     */
    private Event readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Event");
        String title = null;
        String original = null;
        String length = null;
        String genres = null;
        String summary = null;
        String link = null;
        String photo = null;
        String localRelease = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case "Title":
                    title = readTag(parser, "Title");
                    break;
                case "OriginalTitle":
                    original = readTag(parser, "OriginalTitle");
                    break;
                case "LengthInMinutes":
                    length = readTag(parser, "LengthInMinutes");
                    break;
                case "dtLocalRelease":
                    localRelease = readTag(parser, "dtLocalRelease");
                    break;
                case "Genres":
                    genres = readTag(parser, "Genres");
                    break;
                case "Synopsis":
                    summary = readTag(parser, "Synopsis");
                    break;
                case "EventURL":
                    link = readTag(parser, "EventURL");
                    break;
                case "Images":
                    photo = readURL(parser, "EventSmallImagePortrait");
                    break;
                default:
                    skip(parser);
                    break;
            }
        }

        return new Event(title, original, length, localRelease, genres, summary, link, photo);
    }

    /**
     * Processes tags in the xml feed.
     * Returns the text content inside the tags.
     *
     * @param parser                    the parser used
     * @param name                      the tag name
     * @return                          text content inside the tags
     * @throws IOException              if an input or output exception occurred
     * @throws XmlPullParserException   if parser exception occurred
     */
    private String readTag(XmlPullParser parser, String name) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, name);
        String content = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, name);
        return content;
    }

    /**
     * Processes URL tags in the xml feed.
     * Returns the url of an image.
     *
     * @param parser                    the parser used
     * @param image                     the tag used
     * @return                          the url of an image in string form
     * @throws IOException              if an input or output exception occurred
     * @throws XmlPullParserException   if parser exception occurred
     */
    private String readURL(XmlPullParser parser, String image) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Images");
        String photo = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(image)) {
                parser.require(XmlPullParser.START_TAG, ns, image);
                photo = readText(parser);
                parser.require(XmlPullParser.END_TAG, ns, image);
            } else {
                skip(parser);
            }
        }
        return photo;
    }

    /**
     * Extracts the text value inside xml tags.
     *
     * @param parser                    the parser used
     * @return                          the text from xml feed
     * @throws IOException              if an input or output exception occurred
     * @throws XmlPullParserException   if parser exception occurred
     */
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    /**
     * Changes the parser depth depending on the following tag in xml feed.
     *
     * @param parser                    the parser used
     * @throws XmlPullParserException   if parser exception occurred
     * @throws IOException              if an input or output exception occurred
     */
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
                default:
                    break;
            }
        }
    }
}
