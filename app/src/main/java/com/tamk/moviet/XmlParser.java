package com.tamk.moviet;

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
                Log.d("Event found", "Event found");
            } else {
                skip(parser);
            }
        }
        Log.d("entries size in parser: ", "" + entries.size());
        return entries;
    }

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
                    Log.d("XmlParser", "photo link was " + photo);
                    break;
                default:
                    skip(parser);
                    break;
            }
        }

        return new Event(title, original, length, localRelease, genres, summary, link, photo);
    }

    // Processes most tags in the feed.
    private String readTag(XmlPullParser parser, String name) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, name);
        String content = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, name);
        return content;
    }

    // Processes URL tags in the feed.
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
                default:
                    break;
            }
        }
    }
}
