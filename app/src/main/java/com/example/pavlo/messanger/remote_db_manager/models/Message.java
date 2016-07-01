package com.example.pavlo.messanger.remote_db_manager.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by pavlo on 30.06.16.
 */
public class Message {

    @SerializedName("id")
    private int id;

    @SerializedName("text")
    private String text;

    @SerializedName("phone")
    private String phone;

    @SerializedName("status")
    private String status;

    public Message(String text, String phone, String status) {
        this.text = text;
        this.phone = phone;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getPhone() {
        return phone;
    }

    public String getStatus() {
        return status;
    }
}
