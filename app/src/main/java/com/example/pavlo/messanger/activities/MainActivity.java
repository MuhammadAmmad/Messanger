package com.example.pavlo.messanger.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pavlo.messanger.R;
import com.example.pavlo.messanger.adapters.MessagesAdapter;
import com.example.pavlo.messanger.adapters.OnItemClickListener;
import com.example.pavlo.messanger.local_db_manager.LocalDatabaseHelper;
import com.example.pavlo.messanger.local_db_manager.LocalDatabaseManager;
import com.example.pavlo.messanger.permissions_manager.PermissionsManager;
import com.example.pavlo.messanger.remote_db_manager.api.ApiFactory;
import com.example.pavlo.messanger.remote_db_manager.models.Message;
import com.example.pavlo.messanger.remote_db_manager.models.Messages;
import com.example.pavlo.messanger.remote_db_manager.models.Response;
import com.example.pavlo.messanger.remote_db_manager.wrapper.RemoteDatabaseManager;
import com.example.pavlo.messanger.util.Config;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LOG_TAG = "Main activity log ";

    private EditText messageEditText;
    private EditText phonNumberEditText;

    private Button saveMessageButton;
    private Button sendMessageButton;
    private Button downloadMessagesButton;

    private ProgressBar progressBar;

    private RecyclerView messagesRecyclerView;
    private MessagesAdapter adapter;

    private LocalDatabaseManager localDatabaseManager;
    private RemoteDatabaseManager remoteDatabaseManager;

    private String textMessage;
    private String phoneNumber;

    private Subscription subscription;
    private ApiFactory factory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();

        PermissionsManager.requestPermissions(this);
        localDatabaseManager = new LocalDatabaseManager(this);
        remoteDatabaseManager = new RemoteDatabaseManager(this);

        factory = new ApiFactory();
    }

    private void initComponents() {
        messageEditText = (EditText) findViewById(R.id.messageEditText);
        phonNumberEditText = (EditText) findViewById(R.id.phonNumberEditText);

        saveMessageButton = (Button) findViewById(R.id.saveMessageButton);
        sendMessageButton = (Button) findViewById(R.id.sendMessageButton);
        downloadMessagesButton = (Button) findViewById(R.id.downloadMessagesButton);

        saveMessageButton.setOnClickListener(this);
        sendMessageButton.setOnClickListener(this);
        downloadMessagesButton.setOnClickListener(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        messagesRecyclerView = (RecyclerView) findViewById(R.id.messagesRecyclerView);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    private boolean sendSms(String textMessage, String phoneNumber) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, textMessage, null, null);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG, e.getMessage());
            return false;
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    private void setAdapter(ArrayList<Message> messages) {
        adapter = new MessagesAdapter(MainActivity.this, messages, new OnItemClickListener() {
            @Override
            public void onItemClick(final Message message) {
                new AlertDialog.Builder(MainActivity.this).
                        setTitle("Message action.").
                        setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (isNetworkConnected()) {
                                    String text = message.getText();
                                    String phone = message.getPhone();
                                    int id = message.getId();
                                    boolean isSent = sendSms(text, phone);
                                    if (isSent) {
                                        localDatabaseManager.deleteMessage(id);
                                        if (!message.getStatus().equals("sent")) {
                                            changeStatusMessage(id, "sent");

                                        }
                                    } else {
                                        Toast.makeText(MainActivity.this, "Message did not send!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, "No access to internet!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "Action has canceled!", Toast.LENGTH_SHORT).show();
                            }
                        }).
                        show();
            }
        });
        messagesRecyclerView.setAdapter(adapter);
    }

    public void createMessage(String text, String phone, final String status) {
        Observable<Response> createMessage = (status == null) ?
                factory.createMessage(text, phone) :
                factory.createMessage(text, phone, status);
        createMessage.subscribe(new Action1<Response>() {
            @Override
            public void call(Response response) {
                int messageId = response.getMesageId();
                if (status == null) {
                    localDatabaseManager.createMessage(textMessage, phoneNumber, messageId);
                }
                Toast.makeText(MainActivity.this, response.getResponse(), Toast.LENGTH_SHORT).show();
            }
        });
        subscription = createMessage.subscribe();
    }

    public void changeStatusMessage(int id, String status) {
        Observable<Response> changeStatus = factory.changeStatusMessage(id, status);
        changeStatus.subscribe(new Action1<Response>() {
            @Override
            public void call(Response response) {
                Toast.makeText(MainActivity.this, response.getResponse(), Toast.LENGTH_SHORT).show();
            }
        });
        subscription = changeStatus.subscribe();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveMessageButton:
                textMessage = messageEditText.getText().toString();
                phoneNumber = phonNumberEditText.getText().toString();
                if (!textMessage.isEmpty() && !phoneNumber.isEmpty()) {
                    createMessage(textMessage, phoneNumber, null);
                }
                break;
            case R.id.sendMessageButton:
                textMessage = messageEditText.getText().toString();
                phoneNumber = phonNumberEditText.getText().toString();
                if (!textMessage.isEmpty() && !phoneNumber.isEmpty()) {
                    boolean isSent = sendSms(textMessage, phoneNumber);
                    if (isSent) {
                        createMessage(textMessage, phoneNumber, "sent");
                    } else {
                        Toast.makeText(MainActivity.this, "Message did not send!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Wrong! Empty text or phone number!", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.downloadMessagesButton:
                if (isNetworkConnected()) {
                    Observable<Messages> getMessages = factory.getAllMessages();
                    getMessages.subscribe(new Action1<Messages>() {
                        @Override
                        public void call(Messages messages) {
                            setAdapter(messages.getResponse());
                        }
                    });
                    subscription = getMessages.subscribe();
                } else {
                    setAdapter(localDatabaseManager.getAllMessages());
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResult);

        String requestMessage = (requestCode == Config.PERMISSION_REQUEST_CODE) ?
                getString(R.string.allowed_send_message) : getString(R.string.not_allowed_send_message);

        Log.d(LOG_TAG, requestMessage);
        Toast.makeText(MainActivity.this, requestMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        if (this.subscription != null) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }
}
