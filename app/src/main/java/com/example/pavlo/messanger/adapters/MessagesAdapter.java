package com.example.pavlo.messanger.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pavlo.messanger.R;
import com.example.pavlo.messanger.remote_db_manager.models.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pavlo on 30.06.16.
 */
public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

    private final OnItemClickListener listener;
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Message> messages;

    public MessagesAdapter(Context context, ArrayList<Message> messages, OnItemClickListener listener) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.messages = messages;
        this.listener = listener;
    }

    public void add(List<Message> data) {
        messages.addAll(data);
    }

    public void remove(int position) {
        messages.remove(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.messages_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(context, messages.get(position), listener);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout messageItemLayout;

        private TextView numberMessageTextView;
        private TextView messageTextView;
        private TextView phoneTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            numberMessageTextView = (TextView) itemView.findViewById(R.id.numberMessageTextView);
            messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            phoneTextView = (TextView) itemView.findViewById(R.id.phoneTextView);

            messageItemLayout = (LinearLayout) itemView.findViewById(R.id.messageItemLayout);
        }

        public void bind(Context context, final Message item, final OnItemClickListener listener) {

            if (item.getStatus().equals("stored")) {
                messageItemLayout.setBackgroundColor(Color.parseColor("#A8FFFF"));
            }

            numberMessageTextView.setText("" + item.getId());
            messageTextView.setText(item.getText());
            phoneTextView.setText(item.getPhone());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
