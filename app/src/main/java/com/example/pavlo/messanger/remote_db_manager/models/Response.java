package com.example.pavlo.messanger.remote_db_manager.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by pavlo on 30.06.16.
 */
public class Response {

    @SerializedName("error")
    private String error;

    @SerializedName("response")
    private String response;

    @SerializedName("message_id")
    private int mesageId;

    public String getError() {
        return error;
    }

    public String getResponse() {
        return response;
    }

    public int getMesageId() {
        return mesageId;
    }
}
