package com.example.mdaij.ccc;

import java.io.Serializable;

/**
 * Created by mdaij on 12/23/2016.
 */
public class Notification implements Serializable{
    private String content;
    private String tag;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;

    public Notification() {
    }

    public Notification(String content, String tag, String title) {
        this.content = content;
        this.tag = tag;
        this.title = title;
    }

    public String getContent() {

        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
