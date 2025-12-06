package com.example.example_ai;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import okhttp3.*;
// Include this if you ever use synchronous calls, good practice to keep.
public class NetworkUtils {
    // 1. GOOD PRACTICE: Create the OkHttpClient once as a static final field and reuse it.
    private static final OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    // CRITICAL FIX 1: Cleaned up the API Key string.
// The key should be a single continuous string without newlines or extra spaces.
    private static final String API_KEY = "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";
    // CRITICAL FIX 2: Corrected the API URL. Removed the incorrect "YOUR" prefix.
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    // 2. The method signature is already correct.
    public static void sendMessage(String message, Callback callback) {
        try {
            // 1. Create the main JSON object
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("model", "gpt-3.5-turbo");

            // 2. Create the "messages" array
            JSONArray messagesArray = new JSONArray();
            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", message); // No need to manually escape, the library handles it
            messagesArray.put(userMessage);

            jsonBody.put("messages", messagesArray);

            // 3. Create the request body from the JSONObject
            RequestBody body = RequestBody.create(jsonBody.toString(), JSON);

            Request request = new Request.Builder()
                    .url(API_URL)
                    .addHeader("Authorization", "Bearer " + API_KEY)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(callback);

        } catch (JSONException e) {
            // This would happen if you made a mistake with a "key" string, like using a null key.
            // It's good practice to log this, though it's rare to hit this with simple structures.
            e.printStackTrace();
            // Optionally, you could invoke the callback's onFailure here to notify the UI.
        }
    }
}