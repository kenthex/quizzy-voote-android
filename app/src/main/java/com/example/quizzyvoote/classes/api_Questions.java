package com.example.quizzyvoote.classes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class api_Questions {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("creator")
    @Expose
    private String creator;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("expired_at")
    @Expose
    private long expired_at;

    @SerializedName("verified")
    @Expose
    private int verified;

    @SerializedName("error")
    @Expose
    private String error;

    @SerializedName("questions")
    @Expose
    private List<api_Questions> questions;

    public api_Questions(String id, String creator, String title, long expired_at, int verified) {
        this.id = id;
        this.creator = creator;
        this.title = title;
        this.expired_at = expired_at;
        this.verified = verified;
    }

    public api_Questions(String creator, String title, long expired_at, int verified) {
        this.creator = creator;
        this.title = title;
        this.expired_at = expired_at;
        this.verified = verified;
    }

    public String getID() { return this.id; };

    public String getCreator() { return this.creator; };

    public String getTitle() { return this.title; };

    public long getExpiredAt() { return this.expired_at; };

    public int getVerified() { return this.verified; };

    public String getError() { return this.error; };

    public List<api_Questions> getQuestions() { return this.questions; };


}
