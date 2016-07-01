package com.example.pavlo.messanger.models;

/**
 * Created by pavlo on 30.06.16.
 */
public class Message {

    private String messageText;
    private String phoneNumber;
    private int messageId;
    private boolean messageSendStatus;
    private boolean messageStoredStatus;

    private Message() {

    }

    public String getMessageText() {
        return messageText;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getMessageId() {
        return messageId;
    }

    public boolean isMessageSendStatus() {
        return messageSendStatus;
    }

    public boolean isMessageStoredStatus() {
        return messageStoredStatus;
    }

    public static Builder newBuilder() {
        return new Message().new Builder();
    }

    public class Builder {

        private Builder() {

        }

        public Builder setMessageText(String messageText) {
            Message.this.messageText = messageText;

            return this;
        }

        public Builder setPhoneNumber(String phoneNumber) {
            Message.this.phoneNumber = phoneNumber;

            return this;
        }

        public Builder setMessageId(int messageId) {
            Message.this.messageId = messageId;

            return this;
        }

        public Builder setMessageSendStatus(boolean messageSendStatus) {
            Message.this.messageSendStatus = messageSendStatus;

            return this;
        }

        public Builder setMessageStoredStatus(boolean messageStoredStatus) {
            Message.this.messageStoredStatus = messageStoredStatus;

            return this;
        }

        public Message build() {
            return Message.this;
        }
    }
}
