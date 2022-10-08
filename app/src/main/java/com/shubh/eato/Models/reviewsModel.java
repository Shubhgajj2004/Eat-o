package com.shubh.eato.Models;

public class reviewsModel {

    String publisherImg, publishedStar, publishedDate, publisherName;

    public reviewsModel(){}

    public reviewsModel(String publisherImg, String publishedStar, String publishedDate, String publisherName) {
        this.publisherImg = publisherImg;
        this.publishedStar = publishedStar;
        this.publishedDate = publishedDate;
        this.publisherName = publisherName;
    }

    public String getPublisherImg() {
        return publisherImg;
    }

    public void setPublisherImg(String publisherImg) {
        this.publisherImg = publisherImg;
    }

    public String getPublishedStar() {
        return publishedStar;
    }

    public void setPublishedStar(String publishedStar) {
        this.publishedStar = publishedStar;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }
}
