package com.javathlon.rss;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Vector;

public class AudioRssHandler extends DefaultHandler {


    public Vector itemListHolder = new Vector();
    public static AudioRssParseClass item = null;
    //	private boolean itemDataFilled = true;
    private boolean titleFound = false, subtitleFound = false, authorFound = false, dateFound = false, summaryFound = false, otherDataFound = false;
    private boolean titleInserted = false, subtitleInserted = false, authorInserted = false, dateInserted = false, summaryInserted = false, otherDataInserted = false;
    private boolean itemFound = false;
    private int itemNumber = 0;

    //	Handler hh;
    public AudioRssHandler() {
        super();
        itemNumber = 1;
//		hh = 
    }


    ////////////////////////////////////////////////////////////////////
    // Event handlers.
    ////////////////////////////////////////////////////////////////////


    public void startDocument() {
        System.out.println("Start document");
        itemFound = false;
    }


    public void endDocument() throws SAXException {
//	    	boolean a = true;
        System.out.println("End document");
        throw new SAXException("terminate");
//	    	if(a){	    		
//	    		throw new SAXException("terminate");
//	    	}
    }


    public void startElement(String uri, String name,
                             String qName, Attributes atts) {
//		if ("".equals (uri))
//		    System.out.println("Start element: " + qName);
//		else
//		    System.out.println("Start element: {" + uri + "}" + name);


        if (qName.equalsIgnoreCase("item")) {

            itemFound = true;
            if (item != null) {

                titleInserted = false;
                subtitleInserted = false;
                authorInserted = false;
                dateInserted = false;
                summaryInserted = false;
                otherDataInserted = false;

                itemNumber++;
                itemListHolder.add(item);

            }
            //Log.e("item number","item "+itemNumber);

            item = new AudioRssParseClass();
        }
        if (itemFound) {
            if (qName.equalsIgnoreCase("title")) {
                titleFound = true;
            } else if (name.equalsIgnoreCase("subtitle")) {
                subtitleFound = true;
            } else if (name.equalsIgnoreCase("author")) {
                authorFound = true;
            } else if (qName.equalsIgnoreCase("pubDate")) {
                dateFound = true;
            } else if (name.equalsIgnoreCase("summary")) {
                summaryFound = true;
            } else if (qName.equalsIgnoreCase("enclosure") || name.equalsIgnoreCase("content")) {
                otherDataFound = true;
                int length = atts.getLength();
                for (int i = 0; i < length; i++) {
                    String attrName = atts.getQName(i);
                    if (attrName.equalsIgnoreCase("url")) {
                        String value = atts.getValue(i);
                        if (value.contains(".mp4"))
                            item.setUrl(value);
                        //Log.e("Url ",""+value);
                    } else if (attrName.equalsIgnoreCase("length") || attrName.equalsIgnoreCase("fileSize")) {
                        String value = atts.getValue(i);
                        item.setFileSize(value);
                        //Log.e("FileSize ",""+value);
                    } else if (attrName.equalsIgnoreCase("type")) {
                        String value = atts.getValue(i);
                        item.setType(value);
                        //Log.e("Type ",""+value);
                    }
                }
                otherDataFound = false;
                otherDataInserted = true;
            }
        }
    }


    public void endElement(String uri, String name, String qName) {
//		if ("".equals (uri))
//		    System.out.println("End element: " + qName);
//		else
//		    System.out.println("End element:   {" + uri + "}" + name);


    }


    public void characters(char ch[], int start, int length) {
        if (itemFound) {

            if (titleFound) {
                String value = new String(ch, start, length);
                //Log.e("Title ",""+value);
                item.setTitle(value);
                titleFound = false;
                titleInserted = true;
            } else if (subtitleFound) {
                String value = new String(ch, start, length);
                //Log.e("Subtitle ",""+value);
                item.setSubtitle(value);
                subtitleFound = false;
                subtitleInserted = true;
            } else if (authorFound) {
                String value = new String(ch, start, length);
                //Log.e("Author ",""+value);
                item.setAuthor(value);
                authorFound = false;
                authorInserted = true;
            } else if (dateFound) {
                String value = new String(ch, start, length);
                //Log.e("Date ",""+value);
                item.setDate(value);
                dateFound = false;
                dateInserted = true;
            } else if (summaryFound) {
                String value = new String(ch, start, length);
                //Log.e("Summary ",""+value);
                item.setSummary(value);
                summaryFound = false;
                summaryInserted = true;
//		    		itemDataFilled = true;
//		    		itemFound = false;
            }

        }
//		System.out.print("Characters:    \"");
//		for (int i = start; i < start + length; i++) {
//		    switch (ch[i]) {
//		    case '\\':
//			System.out.print("\\\\");
//			break;
//		    case '"':
//			System.out.print("\\\"");
//			break;
//		    case '\n':
//			System.out.print("\\n");
//			break;
//		    case '\r':
//			System.out.print("\\r");
//			break;
//		    case '\t':
//			System.out.print("\\t");
//			break;
//		    default:
//			System.out.print(ch[i]);
//			break;
//		    }
//		}
//		System.out.print("\"\n");
    }


//	Boolean currentElement = false;
//	String currentValue = null;
//	public Vector v = new Vector();
//	public static AudioRssParseClass itemList = null;
//	private String inPage;
//	private boolean mediaContentTagStarted = false;
//
//
//	/** Called when tag starts ( ex:- <name>AndroidPeople</name>
//	 * -- <name> )*/
//	@Override
//	public void startElement(String uri, String localName, String qName,
//			Attributes attributes) throws SAXException {
//		
//		currentElement = true;
//		currentValue = "";
//		if (localName.equals("media:content"))
//		{
//			/** Start */
//			mediaContentTagStarted = true;
//			itemInitialized = true;
//			itemList = new AudioRssParseClass();
//            int length = attributes.getLength();
//            for (int i=0; i<length; i++) {
//                String name = attributes.getQName(i);
//                System.out.print(name + ": ");
//                if(name.equalsIgnoreCase("url")){                	
//                	String value = attributes.getValue(i);
//                	itemList.setUrl(value);
//                	System.out.println(value);            
//                }else if(name.equalsIgnoreCase("fileSize")){
//                	String value = attributes.getValue(i);
//                	itemList.setFileSize(value);
//                	System.out.println(value); 
//                }else if(name.equalsIgnoreCase("type")){
//                	String value = attributes.getValue(i);
//                	itemList.setType(value);
//                	System.out.println(value); 
//                }
//            }
//		}else if (localName.equals("itunes:subtitle"))
//		{
//			
//		}else if (localName.equals("itunes:author"))
//		{
//			
//		}
//
//	}
//
//	/** Called when tag closing ( ex:- <name>AndroidPeople</name>
//	 * -- </name> )*/
//	@Override
//	public void endElement(String uri, String localName, String qName)
//			throws SAXException {
//		
//		currentElement = false;
//		currentValue = currentValue.trim();
//		if (localName.equalsIgnoreCase("guid"))
//		{	
//			v.add(itemList);
//			itemInitialized = false;
//		}
//		if (localName.equals("itunes:explicit"))
//		{
//			
//		}
//
//		if (localName.equals("itunes:subtitle"))
//		{
//			itemList.setSubtitle(currentValue);
//			Log.v("itunes:subtitle:", currentValue);
//		}
//		if (localName.equals("itunes:author"))
//		{
//			itemList.setAuthor(currentValue);
//			Log.v("itunes:author:", currentValue);
//		}
//		
//	}
//
//	/** Called to get tag characters ( ex:- <name>AndroidPeople</name>
//	 * -- to get AndroidPeople Character ) */
//	@Override
//	public void characters(char[] ch, int start, int length)
//			throws SAXException {
//		if(itemInitialized){			
//			if(mediaContentTagStarted){
//				if(new String(ch, start, length).equalsIgnoreCase("/")){
//					continue;
//					if(new String(ch, start, length).equalsIgnoreCase(">")){
//						mediaContentTagStarted = false;
//						continue;
//					}
//				}
//			}
//			if (currentElement) {
//				currentValue+= new String(ch, start, length);
//			}
//		}
//
//	}

}
