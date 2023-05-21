package com.example.downloaderinfo;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class RSSFeedParser {
    static final String TITLE = "title";
    static final String DESCRIPTION = "description";
    static final String CHANNEL = "channel";
    static final String LANGUAGE = "language";
    static final String COPYRIGHT = "copyright";
    static final String LINK = "link";
    static final String AUTHOR = "author";
    static final String ITEM = "item";
    static final String PUB_DATE = "pubDate";
    static final String GUID = "guid";

    static URL url = null;

    public RSSFeedParser(String feedUrl) {
        try {
            url = new URL(feedUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Feed readFeed() {
        Feed feed = null;
        try {
            boolean isFeedHeader = true;
            // Set header values intial to the empty string
            String description = "";
            String title = "";
            String link = "";
            String language = "";
            String copyright = "";
            String author = "";
            String pubdate = "";
            String guid = "";

            // First create a new XMLInputFactory
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            // Setup a new eventReader
            InputStream in = read();
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            // read the XML document
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    String localPart = event.asStartElement().getName()
                            .getLocalPart();
                    switch (localPart) {
                        case ITEM -> {
                            if (isFeedHeader) {
                                isFeedHeader = false;
                                feed = new Feed(title, link, description, language,
                                        copyright, pubdate);
                            }
                            event = eventReader.nextEvent();
                        }
                        case TITLE -> title = getCharacterData(event, eventReader);
                        case DESCRIPTION -> description = getCharacterData(event, eventReader);
                        case LINK -> link = getCharacterData(event, eventReader);
                        case GUID -> guid = getCharacterData(event, eventReader);
                        case LANGUAGE -> language = getCharacterData(event, eventReader);
                        case AUTHOR -> author = getCharacterData(event, eventReader);
                        case PUB_DATE -> pubdate = getCharacterData(event, eventReader);
                        case COPYRIGHT -> copyright = getCharacterData(event, eventReader);
                    }
                } else if (event.isEndElement()) {
                    if (Objects.equals(event.asEndElement().getName().getLocalPart(), ITEM)) {
                        FeedMessage message = new FeedMessage();
                        message.setAuthor(author);
                        message.setDescription(description);
                        message.setGuid(guid);
                        message.setLink(link);
                        message.setTitle(title);
                        assert feed != null;
                        feed.getMessages().add(message);
                        event = eventReader.nextEvent();
                        continue;
                    }
                }
            }
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
        return feed;
    }

    private static String getCharacterData(XMLEvent event, XMLEventReader eventReader)
            throws XMLStreamException {
        String result = "";
        event = eventReader.nextEvent();
        if (event instanceof Characters) {
            result = event.asCharacters().getData();
        }
        return result;
    }

    public static URL getUrl() {
        return url;
    }

    public static void setUrl(URL url) {
        RSSFeedParser.url = url;
    }

    public static InputStream read() {
        //TODO: change to the file read
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static InputStream read(URL urlExternal) {
        try {
            return urlExternal.openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
