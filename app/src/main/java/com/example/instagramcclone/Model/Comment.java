package com.example.instagramcclone.Model;

public class Comment {
    private String comments,publisher;
    private String id;

    public Comment() {
    }


    public Comment(String comments, String publisher, String id) {
        this.comments = comments;
        this.publisher = publisher;
        this.id = id;
    }

    public Comment(String comments, String publisher) {
        this.comments = comments;
        this.publisher = publisher;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
