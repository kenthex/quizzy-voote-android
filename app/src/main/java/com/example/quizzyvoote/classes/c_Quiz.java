package com.example.quizzyvoote.classes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class c_Quiz {

    private Integer quiz_ID;
    private Integer verified;
    private String quiz_name;
    private String quiz_creator;
    private String expireDate;

    public void setQuizName(String quiz_name) {
        this.quiz_name = quiz_name;
    }

    public String getQuizName() {
        return this.quiz_name;
    }

    public void setQuizID(Integer quiz_ID) {
        this.quiz_ID = quiz_ID;
    }

    public Integer getQuizID() {
        return this.quiz_ID;
    }

    public void setQuizCreator(String quiz_creator) {
        this.quiz_creator = quiz_creator;
    }

    public String getQuizCreator() {
        return this.quiz_creator;
    }

    public Integer getVerified() { return this.verified; }

    public String getExpireDate() {
        return this.expireDate;
    }

    public c_Quiz(Integer id, String name, String creator, long expireDate) {
        this.quiz_ID = id;
        this.quiz_name = name;
        this.quiz_creator = creator;
        Date date = new Date(expireDate);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = sdf.format(date);
        this.expireDate = formattedDate;
    }

    public c_Quiz(Integer id, String name, String creator, long expireDate, Integer verified) {
        this.quiz_ID = id;
        this.quiz_name = name;
        this.quiz_creator = creator;
        Date date = new Date(expireDate);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = sdf.format(date);
        this.expireDate = formattedDate;
        this.verified = verified;
    }
}
