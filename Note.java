package com.example.assignmentno04;

public class Note {
    private String title;
    private String content;
    private String noteid;
    public String getNoteid() {
        return noteid;
    }

    public void setNoteid(String noteid) {
        this.noteid = noteid;
    }

    public Note() {
    }

    @Override
    public String toString() {
        return "Note{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", noteid=" + noteid +
                '}';
    }

    public Note(String title, String content, String noteid) {
        this.title = title;
        this.content = content;
        this.noteid = noteid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
