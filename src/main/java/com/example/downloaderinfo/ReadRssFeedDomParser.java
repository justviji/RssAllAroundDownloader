package com.example.downloaderinfo;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class ReadRssFeedDomParser {

    public static void main(String[] args){
        parse("https://media.rss.com/billionsofatoms/feed.xml");
        ArrayList<PodcastEntry> pc = readCSV(Downloader.csvFileForSavingRss.getAbsolutePath());
        pc.forEach((entry)->{
            System.out.println(entry.toString());
        });
    }



    public static void parse(String FEED_URL) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new URL(FEED_URL).openStream());
            doc.getDocumentElement().normalize();

            System.out.println("Root Element: " + doc.getDocumentElement().getNodeName());
            System.out.println("------");

            NodeList itemNodes = doc.getElementsByTagName("item");

            for (int i = 0; i < itemNodes.getLength(); i++) {
                Node itemNode = itemNodes.item(i);

                if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element itemElement = (Element) itemNode;

                    String title = getTextContent(itemElement, "title");
                    String mp3Url = getAttribute(itemElement, "enclosure", "url");
                    String imageUrl = getAttribute(itemElement, "itunes:image", "href");
                    String author = getTextContent(itemElement, "itunes:author");

                    if (author.isEmpty()) {
                        author = getTextContent(itemElement, "dc:creator");
                    }
                    String description = getTextContent(itemElement, "description");

                    System.out.println("Title: " + title);
                    System.out.println("MP3 URL: " + mp3Url);
                    System.out.println("Image URL: " + imageUrl);
                    System.out.println("Author: " + author);
                    System.out.println("Description: " + description);
                    System.out.println("------");
                }
            }

            writeToCSV(Downloader.csvFileForSavingRss.getAbsolutePath(), itemNodes);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }


    private static String getTextContent(Element parentElement, String tagName) {
        NodeList nodeList = parentElement.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            return node.getTextContent();
        }
        return "";
    }

    private static String getAttribute(Element parentElement, String tagName, String attributeName) {
        NodeList nodeList = parentElement.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            Element element = (Element) nodeList.item(0);
            return element.getAttribute(attributeName);
        }
        return "";
    }
    private static void writeToCSV(String filePath, NodeList itemNodes) {
        try {
            FileWriter writer = new FileWriter(filePath);

            // Write CSV header
            writer.append("Title,MP3 URL,Image URL,Author,Description\n");

            for (int i = 0; i < itemNodes.getLength(); i++) {
                Node itemNode = itemNodes.item(i);

                if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element itemElement = (Element) itemNode;

                    String title = getTextContent(itemElement, "title");
                    String mp3Url = getAttribute(itemElement, "enclosure", "url");
                    String imageUrl = getAttribute(itemElement, "itunes:image", "href");
                    String author = getTextContent(itemElement, "itunes:author");

                    if (author.isEmpty()) {
                        author = getTextContent(itemElement, "dc:creator");
                    }
                    String description = getTextContent(itemElement, "description");

                    // Write entry to CSV file
                    writer.append(escapeSpecialCharacters(title)).append(",");
                    writer.append(escapeSpecialCharacters(mp3Url)).append(",");
                    writer.append(escapeSpecialCharacters(imageUrl)).append(",");
                    writer.append(escapeSpecialCharacters(author)).append(",");
                    writer.append(escapeSpecialCharacters(description)).append("\n");
                }
            }

            writer.flush();
            writer.close();

            System.out.println("CSV file has been created successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String escapeSpecialCharacters(String data) {
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            data = "\"" + data + "\"";
        }
        return data;
    }
    public static ArrayList<PodcastEntry> readCSV(String filePath) {
        ArrayList<PodcastEntry> entries = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 5) {
                    PodcastEntry entry = new PodcastEntry();
                    entry.title = data[0];
                    entry.mp3Url = data[1];
                    entry.imageUrl = data[2];
                    entry.author = data[3];
                    entry.description = data[4];
                    entries.add(entry);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return entries;
    }

}

