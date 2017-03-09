package com.javathlon.rss;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

public class MyXMLHandlerItems extends DefaultHandler {

    Boolean currentElement = false;
    boolean titleTrue = false;
    boolean imageTrue = false;
    boolean channelTrue = false;
    boolean summaryTrue = false;
    boolean authorTrue = false;
    String currentValue = null;
    public Vector v = new Vector();
    public static XmlParseClass itemList = null;
    private String inPage;

    public String podcastTitle;
    public String podcastSubtitle;
    public String podcastAuthor;
    public String podcastImage;

    public boolean enclosureTrue = false;
    public String enclosureUrl;

    public int itemCOunt = 0;

//	public static RSSParseClass getSitesList() {
//		return newsitemList;
//	}
//
//	public static void setSitesList(RSSParseClass itemList) {
//		MyXMLHandlerNewsItems.newsitemList = itemList;
//	}


    /**
     * Called when tag starts ( ex:- <name>AndroidPeople</name>
     * -- <name> )
     */
    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {

        currentElement = true;
        currentValue = "";
        if (localName.equals("result")) {
            /** Start */
            itemList = new XmlParseClass();
        }

        if (localName.equals("title")) {
            titleTrue = true;
        }

        if (localName.equals("item")) {
            channelTrue = false; // itemların üstündeki genel bilgileri almak için
            itemList = new XmlParseClass();
        }
        if (localName.equals("rss")) {
            //Log.e("new","item");
            itemList = new XmlParseClass();
        }
        if (localName.equals("channel")) {
            channelTrue = true;
        }
        if (localName.contains("subtitle")) {

        }
        if (localName.contains("summary")) {
            summaryTrue = true;

        }
        if (localName.contains("enclosure")) {
            enclosureTrue = true;
            enclosureUrl = attributes.getValue("url");
            if (null != attributes.getValue("length"))
                itemList.setSize(Long.parseLong(attributes.getValue("length")));
        }
        if (localName.contains("image")) {
            imageTrue = true;
            if (podcastImage == null)
                podcastImage = attributes.getValue("name");

        }
        if (localName.contains("author")) {
            authorTrue = true;

        }

    }

    /**
     * Called when tag closing ( ex:- <name>AndroidPeople</name>
     * -- </name> )
     */
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        currentElement = false;
        currentValue = currentValue.trim();
        if (localName.equalsIgnoreCase("result")) {
            //     v.add(itemList);
        } else if (localName.equalsIgnoreCase("item")) {
            v.add(itemList);
            itemCOunt++;

        } else if (localName.equals("rss")) {
            itemList.setIsEmpty(true);
            //    v.add(itemList);
        }
        if (localName.equals("channel")) {
            itemList.setIsEmpty(true);
            //    v.add(itemList);
            channelTrue = false;
        } else if (localName.equalsIgnoreCase("pid")) {
            itemList.setPid(currentValue);
            //Log.v("pid:", currentValue);
        } else if (localName.equalsIgnoreCase("path")) {

            itemList.setPath(currentValue);
            //Log.v("path:", currentValue);
        } else if (!channelTrue && localName.equalsIgnoreCase("title")) {

            itemList.setTitle(currentValue);
            //Log.v("title:", currentValue);
        } else if (!channelTrue && localName.equalsIgnoreCase("summary")) {

            summaryTrue = false;
        } else if (!channelTrue && localName.equalsIgnoreCase("author")) {

            authorTrue = false;
        } else if (imageTrue && localName.equalsIgnoreCase("url")) {
            podcastImage = currentValue;
        } else if (!channelTrue && localName.equalsIgnoreCase("image")) {

            imageTrue = false;
        } else if (localName.equalsIgnoreCase("link")) {

            itemList.setLink(currentValue);
            //Log.v("link:", currentValue);
        } else if (localName.equalsIgnoreCase("description")) {

            itemList.setDescription(currentValue);
            //Log.v("description:", currentValue);
        } else if (localName.equalsIgnoreCase("guid")) {

            itemList.setGuid(currentValue);
            //Log.v("guid:", currentValue);
        } else if (localName.equalsIgnoreCase("duration")) {

            itemList.setDurationString(currentValue);
            if (null != currentValue && !"".equals(currentValue)) {

                if (currentValue.contains(":")) {
                    String[] values = currentValue.split(":");
                    long second = 0;

                    for (int i = values.length - 1; i >= 0; i--) {
                        String val = values[i].replace("^0+", "");
                        second += Math.pow(60, values.length - i - 1) * Integer.parseInt(val);
                        itemList.setDuration(second);
                    }
                }
                //    continue;
                // itemList.setDuration(Long.parseLong(currentValue));
            }
            //Log.v("guid:", currentValue);
        } else if (localName.equalsIgnoreCase("pubDate")) {

            DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
            try {
                Date d = formatter.parse(currentValue);
                itemList.setPublishDateLong(d.getTime());

            } catch (Exception e) {
                e.printStackTrace();
            }

            //Log.v("pubDate:", currentValue);
        } else if (localName.equalsIgnoreCase("enclosure")) {
            itemList.setEnclosureUrl(new String(enclosureUrl));


        }
    }

    /**
     * Called to get tag characters ( ex:- <name>AndroidPeople</name>
     * -- to get AndroidPeople Character )
     */
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {

        if (channelTrue && titleTrue) {
            podcastTitle = new String(ch, start, length);
            titleTrue = false;
        }
        if (channelTrue && summaryTrue) {
            podcastSubtitle = new String(ch, start, length);
            summaryTrue = false;
        }
        if (channelTrue && authorTrue) {
            podcastAuthor = new String(ch, start, length);
            authorTrue = false;
        }
       /* if (channelTrue && imageTrue) {
            podcastImage = new String(ch, start, length);

        }
*/

        else if (currentElement) {
            currentValue += new String(ch, start, length);
//			currentElement = false;
        }

    }

}
