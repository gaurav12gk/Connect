package com.example.instagramcclone.Model;

public class Comment {
    private String comments,publisher;

    public Comment() {
    }

    public Comment(String comments, String publisher) {
        this.comments = comments;
        this.publisher = publisher;
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
