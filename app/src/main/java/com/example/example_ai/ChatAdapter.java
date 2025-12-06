package com.example.example_ai;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {

    private final List<ChatMessage> messageList;

    public ChatAdapter(List<ChatMessage> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ChatMessage message = messageList.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView messageTextView;
        private final LinearLayout rootLayout; // The parent layout of the TextView

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            // We get the root layout to adjust its gravity
            rootLayout = (LinearLayout) itemView;
        }

        void bind(ChatMessage message) {
            Context context = itemView.getContext();

            // Handle the loading state
            if (message.isLoading()) {
                messageTextView.setText("● ● ●");
                messageTextView.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_ai_bubble));
                messageTextView.setTextColor(ContextCompat.getColor(context, android.R.color.black));
                rootLayout.setGravity(Gravity.START);
                return;
            }

            // Set the message text
            messageTextView.setText(message.getMessage());

            // Adjust gravity and background based on who sent the message
            if (message.isUser()) {
                rootLayout.setGravity(Gravity.END);
                messageTextView.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_user_bubble));
                messageTextView.setTextColor(ContextCompat.getColor(context, android.R.color.white));
            } else {
                rootLayout.setGravity(Gravity.START);
                messageTextView.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_ai_bubble));
                messageTextView.setTextColor(ContextCompat.getColor(context, android.R.color.black));
            }
        }
    }
}
