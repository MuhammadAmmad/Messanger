package com.example.pavlo.messanger.remote_db_manager.api;

import com.example.pavlo.messanger.remote_db_manager.models.Messages;
import com.example.pavlo.messanger.remote_db_manager.models.Response;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by pavlo on 30.06.16.
 */
public interface ApiService {

    @GET("m.messages")
    Observable<Messages> getAllMessages();

    @GET("m.message/{id}")
    Observable<Messages> getMessageById(@Path("id") int messageId);

    @POST("m.create")
    Observable<Response> createMessage(@Query("text") String text, @Query("phone") String phone);

    @POST("m.create")
    Observable<Response> createMessage(@Query("text") String text, @Query("phone") String phone, @Query("status") String status);

    @POST("m.change_status")
    Observable<Response> changeStatusMessage(@Query("message_id") int messageId, @Query("status") String status);

    @POST("m.delete_message/{id}")
    Observable<Response> deleteMessage(@Path("id") int messageId);
}
