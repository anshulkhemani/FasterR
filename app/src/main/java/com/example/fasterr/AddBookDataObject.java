package com.example.fasterr;

import java.util.List;
import java.util.Map;

public class AddBookDataObject {
    private String bookName;
    private String author;
    private String description,about,whoFor,aboutAuthor;
    private String time;
    private String draw;
    private String pages;
    private String category;
    private Map<String, List<String>> mp;

    AddBookDataObject(){}
    public AddBookDataObject(String bookName, String author, String description, String about, String whoFor, String aboutAuthor, String time, String draw, String pages,String category, Map<String, List<String>> mp) {
        this.bookName = bookName;
        this.author = author;
        this.description = description;
        this.about = about;
        this.whoFor = whoFor;
        this.aboutAuthor = aboutAuthor;
        this.time = time;
        this.draw = draw;
        this.pages = pages;
        this.category=category;
        this.mp = mp;
    }
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getWhoFor() {
        return whoFor;
    }

    public void setWhoFor(String whoFor) {
        this.whoFor = whoFor;
    }

    public String getAboutAuthor() {
        return aboutAuthor;
    }

    public void setAboutAuthor(String aboutAuthor) {
        this.aboutAuthor = aboutAuthor;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public Map<String, List<String>> getMp() {
        return mp;
    }

    public void setMp(Map<String, List<String>> mp) {
        this.mp = mp;
    }

    public String getDraw() {
        return draw;
    }

    public String getBookName() {
        return bookName;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }
    public void setDraw(String dr) {
        this.draw = dr;
    }
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTime(String time) {
        this.time = time;
    }
}