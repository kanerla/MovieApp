package com.example.xmlparser;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlParser {
    private static final String ns = null;

    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            Log.d("MovieActivity", "done parsing");
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

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

    private Event readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Event");
        String title = null;
        String original = null;
        String genres = null;
        String summary = null;
        String link = null;
        String photo = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("Title")) {
                title = readTitle(parser);
            } else if (name.equals("OriginalTitle")) {
                original = readOriginal(parser);
            } else if (name.equals("Genres")) {
                genres = readGenres(parser);
            } else if (name.equals("ShortSynopsis")) {
                summary = readSummary(parser);
            } else if (name.equals("EventURL")) {
                link = readLink(parser);
            } else if (name.equals("Images")) {
                photo = readURL(parser);
                Log.d("XmlParser", "photo link was " + photo);
            } else {
                skip(parser);
            }
        }
        return new Event(title, original, genres, summary, link, photo);
    }

    // Processes title tags in the feed.
    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Title");
        return title;
    }

    // Processes original title tags in the feed.
    private String readOriginal(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "OriginalTitle");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "OriginalTitle");
        return title;
    }

    // Processes original title tags in the feed.
    private String readGenres(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Genres");
        String genres = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Genres");
        return genres;
    }

    // Processes link tags in the feed.
    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "EventURL");
        String link = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "EventURL");
        return link;
    }

    // Processes summary tags in the feed.
    private String readSummary(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "ShortSynopsis");
        String summary = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "ShortSynopsis");
        return summary;
    }

    // Processes URL tags in the feed.
    private String readURL(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Images");
        String photo = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("EventSmallImagePortrait")) {
                parser.require(XmlPullParser.START_TAG, ns, "EventSmallImagePortrait");
                photo = readText(parser);
                parser.require(XmlPullParser.END_TAG, ns, "EventSmallImagePortrait");
            } else {
                skip(parser);
            }
        }
        return photo;
    }

    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

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
            }
        }
    }
}
