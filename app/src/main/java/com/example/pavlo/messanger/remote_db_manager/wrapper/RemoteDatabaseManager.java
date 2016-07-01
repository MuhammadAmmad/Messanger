package com.example.pavlo.messanger.remote_db_manager.wrapper;

import android.content.Context;
import android.widget.Toast;

import com.example.pavlo.messanger.remote_db_manager.api.ApiFactory;
import com.example.pavlo.messanger.remote_db_manager.models.Message;
import com.example.pavlo.messanger.remote_db_manager.models.Messages;
import com.example.pavlo.messanger.remote_db_manager.models.Response;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by pavlo on 30.06.16.
 */
public class RemoteDatabaseManager {

    private Subscription subscription;
    private ApiFactory factory;

    private Context context;

    private ArrayList<Message> listMessages;
    private int messageId;

    public RemoteDatabaseManager(Context context) {
        this.context = context;
        factory = new ApiFactory();
        listMessages = new ArrayList<Message>();
    }

    public ArrayList<Message> getAllMessages() {
        //final ArrayList<Message> listMessages = new ArrayList<Message>();
        Observable<Messages> getMessages = factory.getAllMessages();
        getMessages.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Messages>() {
            @Override
            public void call(Messages messages) {
                listMessages.addAll(messages.getResponse());
            }
        });
        subscription = getMessages.subscribe();

        return listMessages;
    }

    public Message getMessageById(int id) {
        final ArrayList<Message> message = new ArrayList<Message>();
        Observable<Messages> getMessage = factory.getMessageById(id);
        getMessage.subscribe(new Action1<Messages>() {
            @Override
            public void call(Messages messages) {
                message.addAll(messages.getResponse());
            }
        });
        subscription = getMessage.subscribe();

        return message.get(0);
    }

    public int createMessage(String text, String phone) {
        Observable<Response> createMessage = factory.createMessage(text, phone);
        createMessage.subscribe(new Action1<Response>() {
            @Override
            public void call(Response response) {
                messageId = response.getMesageId();
                Toast.makeText(context, response.getResponse(), Toast.LENGTH_SHORT).show();
            }
        });
        subscription = createMessage.subscribe();

        return messageId;
    }

    public int createMessage(String text, String phone, String status) {
        Observable<Response> createMessage = factory.createMessage(text, phone, status);
        createMessage.subscribe(new Action1<Response>() {
            @Override
            public void call(Response response) {
                messageId = response.getMesageId();
                Toast.makeText(context, response.getResponse(), Toast.LENGTH_SHORT).show();
            }
        });
        subscription = createMessage.subscribe();

        return messageId;
    }

    public void changeStatusMessage(int id, String status) {
        Observable<Response> changeStatus = factory.changeStatusMessage(id, status);
        changeStatus.subscribe(new Action1<Response>() {
            @Override
            public void call(Response response) {
                Toast.makeText(context, response.getResponse(), Toast.LENGTH_SHORT).show();
            }
        });
        subscription = changeStatus.subscribe();
    }

    public void deleteMessage(int id) {
        Observable<Response> deleteMessage = factory.deleteMessage(id);
        deleteMessage.subscribe(new Action1<Response>() {
            @Override
            public void call(Response response) {
                Toast.makeText(context, response.getResponse(), Toast.LENGTH_SHORT).show();
            }
        });
        subscription = deleteMessage.subscribe();
    }

    public void unsubscribe() {
        if (this.subscription != null) {
            subscription.unsubscribe();
        }
    }
}
