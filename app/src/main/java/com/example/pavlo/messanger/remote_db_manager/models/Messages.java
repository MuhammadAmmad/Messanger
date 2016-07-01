package com.example.pavlo.messanger.remote_db_manager.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by pavlo on 30.06.16.
 */
public class Messages {

    @SerializedName("error")
    private String error;

    @SerializedName("response")
    private ArrayList<Message> response;

    public String getError() {
        return error;
    }

    public ArrayList<Message> getResponse() {
        return response;
    }
}
