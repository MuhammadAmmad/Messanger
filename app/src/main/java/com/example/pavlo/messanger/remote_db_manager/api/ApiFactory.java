package com.example.pavlo.messanger.remote_db_manager.api;

import com.example.pavlo.messanger.remote_db_manager.models.Messages;
import com.example.pavlo.messanger.remote_db_manager.models.Response;
import com.example.pavlo.messanger.remote_db_manager.utilities.Util;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by pavlo on 30.06.16.
 */
public class ApiFactory {

    private static final OkHttpClient CLIENT = new OkHttpClient();

    private ApiService apiService;

    public ApiFactory() {
        apiService = getMessagesService();
    }

    public static ApiService getMessagesService() {
        return getRetrofit().create(ApiService.class);
    }

    private static Retrofit getRetrofit() {
        return new Retrofit.Builder().
                baseUrl(Util.SERVER_URL).
                addConverterFactory(GsonConverterFactory.create()).
                addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                client(CLIENT).
                build();
    }

    public Observable<Messages> getAllMessages() {
        Observable<Messages> observable = apiService.
                getAllMessages().
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                cache();

        return observable;
    }

    public Observable<Messages> getMessageById(int messageId) {
        Observable<Messages> observable = apiService.
                getMessageById(messageId).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                cache();

        return observable;
    }

    public Observable<Response> createMessage(String text, String phone) {
        Observable<Response> observable = apiService.
                createMessage(text, phone).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                cache();

        return observable;
    }

    public Observable<Response> createMessage(String text, String phone, String status) {
        Observable<Response> observable = apiService.
                createMessage(text, phone, status).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                cache();

        return observable;
    }

    public Observable<Response> changeStatusMessage(int messageId, String status) {
        Observable<Response> observable = apiService.
                changeStatusMessage(messageId, status).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                cache();

        return observable;
    }

    public Observable<Response> deleteMessage(int messageId) {
        Observable<Response> observable = apiService.
                deleteMessage(messageId).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                cache();

        return observable;
    }
}
