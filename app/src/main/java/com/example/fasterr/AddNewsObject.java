package com.example.fasterr;


public class AddNewsObject {
    public AddNewsObject(String heading, String content, String img, String type) {
        this.heading = heading;
        this.content = content;
        this.img = img;
        this.type = type;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String heading,content,img,type;

    AddNewsObject(){}

}