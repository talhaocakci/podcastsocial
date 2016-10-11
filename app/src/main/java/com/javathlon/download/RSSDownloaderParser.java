package com.javathlon.download;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Message;
import android.widget.Toast;

import com.javathlon.ApplicationSettings;
import com.javathlon.CheckInternet;
import com.javathlon.PodcastData;
import com.javathlon.db.DBAccessor;
import com.javathlon.rss.MyXMLHandlerItems;
import com.javathlon.rss.XmlParseClass;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by talha on 10.07.2015.
 */
public class RSSDownloaderParser {

    ProgressDialog progressDialog;

    private ArrayList<String> titleList, linkList, descriptionList, guidList;
    private ArrayList<Long> pubDateList;
    private List<PodcastData> podcastDataList;
    public static final int XMLDOWNLOADFINISHED = 4, XMLDOWNLOADNOTFINISHED = 5;
    private DBAccessor dbHelper;
    private DownloadHandler handler;

    private Context context;
    private boolean isAsync;
    private MyXMLHandlerItems myXMLHandler;

    public RSSDownloaderParser(Context context, boolean isAsync, DownloadHandler handler, MyXMLHandlerItems itemHandler, List<PodcastData> dataList) {
        this.isAsync = isAsync;
        this.context = context;
        this.handler = handler;
        this.myXMLHandler = itemHandler;
        this.podcastDataList = dataList;

    }

    public List<PodcastData> getRSSListFromUrl(final String rssUrl, Context con) {
        if (CheckInternet.checkAnyConnectionExists(con)) {
            if (isAsync) {
                if (progressDialog != null) {
                    progressDialog = ProgressDialog.show(con, "", "");
                    progressDialog.setCancelable(true);
                }
            }
            loadList();
            if (isAsync) {
                new Thread() {
                    @Override
                    public void run() {

                        getData(rssUrl);

                    }
                }.start();
            } else {
                getData(rssUrl);
                return podcastDataList;
            }

        } else {
            Toast.makeText(con, "Internet is not available.", Toast.LENGTH_LONG).show();
        }
        return podcastDataList;
    }

    private void loadList() {
        // TODO Auto-generated method stub
        titleList = new ArrayList<String>();
        linkList = new ArrayList<String>();
        descriptionList = new ArrayList<String>();
        guidList = new ArrayList<String>();
        pubDateList = new ArrayList<Long>();
    }

    private void getData(String link) {

        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();
            URL sourceUrl = new URL(link);

            if(ApplicationSettings.isProxyOpen) {
                System.setProperty("http.proxyHost", ApplicationSettings.proxyAddress);
                System.setProperty("http.proxyPort", ApplicationSettings.proxyHost);
            }

            xr.setContentHandler(myXMLHandler);
            xr.parse(new InputSource(sourceUrl.openStream()));
            Vector vvv = myXMLHandler.v;
            // podcastLabel.setText(myXMLHandler.description);
            int length = vvv.size();
            if (length > 0) {
                for (int i = 0; i < length; i++) {
                    XmlParseClass listObj = (XmlParseClass) vvv
                            .elementAt(i);
                    if (!listObj.getIsEmpty())
                        titleList.add(listObj.getTitle());
                    linkList.add(listObj.getLink());
                    descriptionList.add(listObj.getDescription());
                    guidList.add(listObj.getGuid());
                    pubDateList.add(listObj.getPublishDateLong());
                    PodcastData data = new PodcastData();
                    data.catalogId = 0;
                    data.editionTitle = listObj.getTitle();
                    data.url = listObj.getGuid();
                    data.duration = listObj.getDuration();
                    data.publishDateLong = listObj.getPublishDateLong();
                    data.durationString = listObj.getDurationString();
                    data.size = listObj.getSize();
                    if (!data.url.contains(".mp3"))
                        data.url = listObj.getEnclosureUrl();
                    data.publishDateLong = listObj.getPublishDateLong();
                    podcastDataList.add(data);

                }
                if (isAsync) {
                    Message msg = new Message();
                    msg.what = XMLDOWNLOADFINISHED;
                    handler.sendMessage(msg);
                } else {

                }
            } else {
                if (isAsync) {
                    Message msg = new Message();
                    msg.what = XMLDOWNLOADNOTFINISHED;
                    handler.sendMessage(msg);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();

        }


    }
}
