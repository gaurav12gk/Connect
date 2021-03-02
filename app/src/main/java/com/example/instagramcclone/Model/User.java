package com.example.instagramcclone.Model;

public class User {
    private String Name,Email,Username,bio,imageurl,ID;
public User(){

}
    public User(String Name, String email, String username, String bio, String imageurl, String ID) {
        this.Name = Name;
        Email = email;
        Username = username;
        this.bio = bio;
        this.imageurl = imageurl;
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
