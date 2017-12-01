package com.landsoft.demoreadxml.Model;

import android.util.Log;

/**
 * Created by TRANTUAN on 29-Nov-17.
 */

public class News {
    private static final String TAG = "News" ;
    String title;
    String description;
    String link;
    String pubDate;
    String image;

    public News() {
    }

    public News(String title, String description, String link, String pubDate, String image) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.pubDate = pubDate;
        this.image = image;
    }

    public void echoNews(){
        Log.d(TAG   , "echoNews: " + title +"/n" +  description +"/n" +  link +"/n" +  pubDate +"/n" +  image +"/n" );
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
