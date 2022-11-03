package com.jnu.booklistmainactivity.data;

import java.io.Serializable;

public class Book implements Serializable {
    public int id;
    public String title;

    public Book(String title, int resourceId) {
        this.title=title;
        this.id=resourceId;
    }

    public String getTitle(){
        return this.title;
    }
    public void setTitle(String title1){title=title1;}

    public int getCoverResourceId(){
        return this.id;
    }
    public void setCoverResourceId(int coverResourceId){
        id=coverResourceId;
    }
}
