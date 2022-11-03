package com.jnu.booklistmainactivity.data;

import java.io.Serializable;

public class Book implements Serializable {
    private String title;
    private int resourceId;

    public Book(String title, int resourceId) {
        this.title=title;
        this.resourceId=resourceId;
    }

    public String getTitle(){
        return title;
    }
    public void setTitle(String title1){title=title1;}

    public int getCoverResourceId(){
        return resourceId;
    }
    public void setCoverResourceId(int coverResourceId){resourceId=coverResourceId;}
}
