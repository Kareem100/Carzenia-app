package com.example.android.carzenia.SystemDatabase;

public class MessageModel {

    private String ID;
    private UserModel from;
    private MessageType type;
    private String subject;
    private String body;
    private boolean seen;
    private String response;

    public MessageModel() {
        // Left Blank For Firebase.
    }

    public MessageModel (String ID, UserModel from, MessageType messageType,
                         String subject, String body, boolean seen, String response) {
        this.ID = ID;
        this.from = from;
        this.type = messageType;
        this.subject = subject;
        this.body = body;
        this.seen = seen;
        this.response = response;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public UserModel getFrom() {
        return from;
    }

    public MessageType getType() {
        return type;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public boolean isSeen() {
        return seen;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
