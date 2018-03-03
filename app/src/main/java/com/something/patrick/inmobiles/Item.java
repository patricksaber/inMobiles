package com.something.patrick.inmobiles;

/**
 * Created by patrick on 3/1/2018.
 * Item model class
 */

public class Item {
    private int Id;
    private String link;
    private String title;
    private String description;

    public Item(int id, String link, String title, String description) {
        Id = id;
        this.link = link;
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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
}
