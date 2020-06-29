package com.example.quizzyvoote.classes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class api_Users {
    @SerializedName("id")
    @Expose
    private int user_id;

    @SerializedName("login")
    @Expose
    private String user_login;

    @SerializedName("email")
    @Expose
    private String user_email;

    @SerializedName("password")
    @Expose
    private String user_password;

    @SerializedName("error")
    @Expose
    private String error;

    @SerializedName("repass")
    @Expose
    private String user_repass;

    public api_Users(String user_login, String user_email, String user_password) {
        this.user_login = user_login;
        this.user_email = user_email;
        this.user_password = user_password;
    }

    public api_Users(String user_login, String user_email, String user_password, String user_repass) {
        this.user_login = user_login;
        this.user_email = user_email;
        this.user_password = user_password;
        this.user_repass = user_repass;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getUser_login() {
        return user_login;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_password() {
        return user_password;
    }

    public String getError() {
        return error;
    }
}
