package com.example.basics;

public class MyStory {
    String title;
    String story;
    String songLink;

    public MyStory() {
    }

    public MyStory(String title, String story, String songLink) {
        this.title = title;
        this.story = story;
        this.songLink = songLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getSongLink() {
        return songLink;
    }

    public void setSongLink(String songLink) {
        this.songLink = songLink;
    }
}
