package com.example.fasterr;

public class BookDataObject {
    private String bookName;
    private String author;
    private String description;
    private String time;
    private String draw;
    private String category;

    BookDataObject(){}
    public BookDataObject(String bookName, String author, String description) {
        this.bookName = bookName;
        this.author = author;
        this.description = description;
    }

    public BookDataObject(String bookName, String author, String description, String time) {
        this.bookName = bookName;
        this.author = author;
        this.description = description;
        this.time = time;
    }



    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    private int num;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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