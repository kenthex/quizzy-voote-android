package com.example.quizzyvoote.classes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class api_Tokens {
    @SerializedName("id")
    @Expose
    private int token_id;

    @SerializedName("user_id")
    @Expose
    private int user_id;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("expired_at")
    @Expose
    private long expired_at;

    @SerializedName("createdAt")
    @Expose
    private long createdAt;

//    @SerializedName("status")
//    @Expose
//    private String status;

    public api_Tokens(Integer user_id, String token, long expired_at) {
        this.user_id = user_id;
        this.token = token;
        this.expired_at = expired_at;
    }

    public api_Tokens(Integer user_id, String token, long expired_at, long createdAt) {
        this.user_id = user_id;
        this.token = token;
        this.expired_at = expired_at;
        this.createdAt = createdAt;
    }

    public Integer getTokenID() {
        return token_id;
    }

    public Integer getUserID() { return user_id; }

    public String getToken() {
        return token;
    }

    public long getExpiredAt() { return expired_at; }

    public long getCreatedAt() { return createdAt; }

}
