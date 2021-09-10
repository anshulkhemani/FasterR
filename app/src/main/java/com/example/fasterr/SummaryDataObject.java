package com.example.fasterr;

public class SummaryDataObject {

    public void setSummaryHeading(String summaryHeading) {
        this.summaryHeading = summaryHeading;
    }

    public String getSummaryNo() {
        return summaryNo;
    }

    public void setSummaryNo(String summaryNo) {
        this.summaryNo = summaryNo;
    }

    public String getSummaryHeading() {
        return summaryHeading;
    }

    private String summaryHeading;
    private String summaryNo;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String content;
}
