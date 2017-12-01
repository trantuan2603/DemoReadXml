package com.landsoft.demoreadxml.Model;

import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TRANTUAN on 29-Nov-17.
 */

public class DownloadXml extends AsyncTask<String, Void, List> {
    static final String PUB_DATE = "pubDate";
    static final String DESCRIPTION = "description";
    static final String CHANNEL = "channel";
    static final String LINK = "link";
    static final String TITLE = "title";
    static final String ITEM = "item";
    static final String IMAGE = "image";
    @Override
    protected List doInBackground(String... strings) {
        List<News> list = new ArrayList<>();
        String urlString = strings[0];
        InputStream inputStream = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            int conn = connection.getResponseCode();
            if (conn == HttpURLConnection.HTTP_OK) {
                 inputStream = connection.getInputStream();
//                InputStreamReader reader = new InputStreamReader(inputStream);
//                BufferedReader bufferedReader = new BufferedReader(reader);
//                StringBuilder builder = new StringBuilder();
//                String line = "";
//                while ((line = bufferedReader.readLine()) != null) {
//                    builder.append(line);
//                }
//                Log.d("du lieu", builder.toString());
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(inputStream,null);               
                int eventType = parser.getEventType();
                boolean done = false;
                News item = null;
                while (eventType != XmlPullParser.END_DOCUMENT && !done) {
                    String name = null;
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            name = parser.getName();
                            if (name.equalsIgnoreCase(ITEM)) {
//                                Log.i("new item", "Create new item");
                                item = new News();
                            } else if (item != null) {
                                if (name.equalsIgnoreCase(LINK)) {
//                                    Log.i("Attribute", "setLink");
                                    item.setLink(parser.nextText());
                                } else if (name.equalsIgnoreCase(DESCRIPTION)) {
//                                    Log.i("Attribute", "description");
                                    item.setDescription(parser.nextText().trim());
                                } else if (name.equalsIgnoreCase(PUB_DATE)) {
//                                    Log.i("Attribute", "date");
                                    item.setPubDate(parser.nextText());
                                } else if (name.equalsIgnoreCase(TITLE)) {
//                                    Log.i("Attribute", "title");
                                    item.setTitle(parser.nextText().trim());
                                } else if (name.equalsIgnoreCase(IMAGE)){
//                                    Log.d("Attribute", "image");
                                    item.setImage(parser.nextText().trim());
                                }
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            name = parser.getName();
//                            Log.i("End tag", name);
                            if (name.equalsIgnoreCase(ITEM) && item != null) {
//                                Log.i("Added", item.toString());
                                list.add(item);
                            } else if (name.equalsIgnoreCase(CHANNEL)) {
                                done = true;
                            }
                            break;
                    }
                    eventType = parser.next();
                }               
                return list;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return null;
    }
}
