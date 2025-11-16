package com.longg.gky.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.longg.gky.R;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_USER = 0;
    private static final int TYPE_BOT = 1;

    private final List<ChatMessage> items = new ArrayList<>();

    public void addMessage(ChatMessage msg) {
        items.add(msg);
        notifyItemInserted(items.size() - 1);
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).fromUser ? TYPE_USER : TYPE_BOT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_USER) {
            View v = inflater.inflate(R.layout.item_message_sent, parent, false);
            return new UserVH(v);
        } else {
            View v = inflater.inflate(R.layout.item_message_received, parent, false);
            return new BotVH(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage msg = items.get(position);
        if (holder instanceof UserVH) {
            ((UserVH) holder).tv.setText(msg.text);
        } else if (holder instanceof BotVH) {
            ((BotVH) holder).tv.setText(msg.text);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class UserVH extends RecyclerView.ViewHolder {
        TextView tv;

        UserVH(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv_message);
        }
    }

    static class BotVH extends RecyclerView.ViewHolder {
        TextView tv;

        BotVH(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv_message);
        }
    }
}
