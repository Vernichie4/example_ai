package com.example.example_ai;

import android.os.Bundle;

import androidx.annotation.NonNull;import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.example_ai.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final List<ChatMessage> messageList = new ArrayList<>();
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 1. Initialize the Adapter and set it to the RecyclerView
        chatAdapter = new ChatAdapter(messageList);
        binding.chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.chatRecyclerView.setAdapter(chatAdapter);

        // 2. Update the click listener
        binding.sendBtn.setOnClickListener(v -> {
            String message = binding.userInput.getText().toString().trim();
            if (!message.isEmpty()) {
                // Add user message and update the adapter
                addMessageToList(message, true);
                binding.userInput.setText("");

                // Show loading indicator and disable button
                showLoadingIndicator(true);
                binding.sendBtn.setEnabled(false);

                sendToChatGPT(message);
            }
        });
    }

    // 3. New method to add messages to the list and notify the adapter
    private void addMessageToList(String message, boolean isUser) {
        messageList.add(new ChatMessage(message, isUser));
        int newPosition = messageList.size() - 1;
        // Notify the adapter that a new item has been inserted
        chatAdapter.notifyItemInserted(newPosition);
        // Scroll to the new message
        binding.chatRecyclerView.scrollToPosition(newPosition);
    }

    // 4. New method to handle the loading indicator in the list
    private void showLoadingIndicator(boolean show) {
        if (show) {
            messageList.add(new ChatMessage(true)); // Add loading message
            chatAdapter.notifyItemInserted(messageList.size() - 1);
            binding.chatRecyclerView.scrollToPosition(messageList.size() - 1);
        } else {
            // Remove the last item if it's a loading indicator
            if (!messageList.isEmpty() && messageList.get(messageList.size() - 1).isLoading()) {
                int lastPosition = messageList.size() - 1;
                messageList.remove(lastPosition);
                chatAdapter.notifyItemRemoved(lastPosition);
            }
        }
    }

    // 5. Updated sendToChatGPT method
    private void sendToChatGPT(String message) {
        NetworkUtils.sendMessage(message, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> {
                    showLoadingIndicator(false);
                    binding.sendBtn.setEnabled(true);
                    addMessageToList("Network Error: " + e.getMessage(), false);
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                // Always hide loading and enable button on the main thread
                runOnUiThread(() -> {
                    showLoadingIndicator(false);
                    binding.sendBtn.setEnabled(true);
                });

                if (response.isSuccessful()) {
                    try {
                        String responseBody = Objects.requireNonNull(response.body()).string();
                        JSONObject jsonObject = new JSONObject(responseBody);
                        JSONArray choices = jsonObject.getJSONArray("choices");
                        String reply = choices.getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content");

                        runOnUiThread(() -> addMessageToList(reply.trim(), false));

                    } catch (Exception e) {
                        runOnUiThread(() -> addMessageToList("Error parsing response.", false));
                    }
                } else {
                    runOnUiThread(() -> addMessageToList("API Error: " + response.code(), false));
                }
            }
        });
    }

    // --- The old addMessageToChat and showLoadingIndicator methods are no longer needed ---
    // --- and should be deleted. This new code replaces their functionality. ---
}
