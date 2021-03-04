package com.example.instagramcclone.Model;

public class Post {
private String description,postid,imageurl,publisher;

    public Post() {
    }

    public Post(String description, String postid, String imageurl, String publisher) {
        this.description = description;
        this.postid = postid;
        this.imageurl = imageurl;
        this.publisher = publisher;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}

