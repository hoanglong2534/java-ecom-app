package com.longg.gky.chat;

public class ChatMessage {
    public final String text;
    public final boolean fromUser;

    public ChatMessage(String text, boolean fromUser) {
        this.text = text;
        this.fromUser = fromUser;
    }
}
