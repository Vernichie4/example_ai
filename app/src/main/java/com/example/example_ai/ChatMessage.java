package com.example.example_ai;

public class ChatMessage {
    private final String message;
    private final boolean isUser;
    private boolean isLoading;

    // Constructor for user and AI messages
    public ChatMessage(String message, boolean isUser) {
        this.message = message;
        this.isUser = isUser;
        this.isLoading = false;
    }

    // Constructor for the loading indicator
    public ChatMessage(boolean isLoading) {
        this.message = ""; // Message is empty for loading state
        this.isUser = false; // Loading bubble appears on the AI's side
        this.isLoading = isLoading;
    }

    public String getMessage() {
        return message;
    }

    public boolean isUser() {
        return isUser;
    }

    public boolean isLoading() {
        return isLoading;
    }
}
