package com.example.quizzyvoote.classes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class api_Votes {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("answer_id")
    @Expose
    private int answer_id;

    @SerializedName("user_id")
    @Expose
    private int user_id;

    @SerializedName("question_id")
    @Expose
    private int question_id;

    @SerializedName("answer_title")
    @Expose
    private String answer_title;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("title")
    @Expose
    private String title;

    public api_Votes(int user_id, int question_id, String answer_title) {
        this.user_id = user_id;
        this.question_id = question_id;
        this.answer_title = answer_title;
    }

    public String getStatus() { return this.status; }

    public String getTitle() { return this.title; }

}
