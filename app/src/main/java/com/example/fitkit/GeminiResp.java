package com.example.fitkit;

import android.os.Handler;
import android.os.Looper;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GeminiResp {

    // 1. Your API Key (Make sure this is correct)
    private static final String API_KEY = "AIzaSyCLRBRPTBvfe9OlvM_AXqJcp9T6IMKnIL0";

    // 2. The Method to call from Nutrition or Recipes
    public static void getResponse(String query, final GeminiCallback callback) {

        // 3. The URL (No spaces allowed!)
        // Try this URL instead:
        String url = "https://generativelanguage.googleapis.com/v1/models/gemini-2.5-flash:generateContent?key=" + API_KEY;
        // 4. Create the JSON Payload
        String json = "{ \"contents\": [ { \"parts\": [ { \"text\": \"" + query.replace("\"", "'") + "\" } ] } ] }";

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(json, mediaType);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        // 5. Send the Request
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Network Error
                sendBack(callback, "Internet Error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String rawData = response.body().string();

                try {
                    if (!response.isSuccessful()) {
                        sendBack(callback, "API Error: " + response.code() + " - " + rawData);
                        return;
                    }

                    // 6. Parse the Result
                    JSONObject jsonObject = new JSONObject(rawData);
                    String result = jsonObject
                            .getJSONArray("candidates")
                            .getJSONObject(0)
                            .getJSONObject("content")
                            .getJSONArray("parts")
                            .getJSONObject(0)
                            .getString("text");

                    sendBack(callback, result.trim());

                } catch (Exception e) {
                    sendBack(callback, "Parsing Error. Raw Data: " + rawData);
                }
            }
        });
    }

    // Helper to send data back to the main UI thread
    private static void sendBack(GeminiCallback callback, String text) {
        new Handler(Looper.getMainLooper()).post(() -> callback.onResult(text));
    }

    // Interface for callback
    public interface GeminiCallback {
        void onResult(String response);
    }
}