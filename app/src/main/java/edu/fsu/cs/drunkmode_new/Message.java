package edu.fsu.cs.drunkmode_new;

public class Message {
    private String sentBy;
    private String text;
    private String timesent;
    private String sentByName;

    public Message(String sentBy, String text, String timesent, String sentByName){
        this.sentBy = sentBy;
        this.text = text;
        this.timesent = timesent;
        this.sentByName = sentByName;
    }

    public Message(){}

    public String getSender() {
        return sentBy;
    }

    public void setSender(String sentBy) {
        this.sentBy = sentBy;
    }

    public String gettext() {
        return text;
    }

    public void settext(String text) {
        this.text = text;
    }

    public String getTimesent() {
        return timesent;
    }

    public void setTimesent(String timesent) {
        this.timesent = timesent;
    }

    public String getSentByName() {return  sentByName; }

    public void setSentByName(String sentByName) { this.sentByName = sentByName; }
}
