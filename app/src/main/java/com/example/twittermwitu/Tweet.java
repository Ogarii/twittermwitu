package com.example.twittermwitu;

public class Tweet {


    private String twit;
    private String UserID;
    private String DocumentId;


    public Tweet(){}

    public Tweet(String twit, String userID, String documentId) {
        this.twit = twit;
        UserID = userID;
        DocumentId = documentId;
    }

    public String getTwit() {
        return twit;
    }

    public void setTwit(String twit) {
        this.twit = twit;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getDocumentId() {
        return DocumentId;
    }

    public void setDocumentId(String documentId) {
        DocumentId = documentId;
    }
}
