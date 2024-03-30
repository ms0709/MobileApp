package com.example.shc_notice;

public class adminUploadPDF {


    public String title;
    public String url;
    public String dateTime;



    public adminUploadPDF(String title, String url, String dateTime) {
        this.title = title;
        this.url = url;
        this.dateTime=dateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public adminUploadPDF() {

    }
}
