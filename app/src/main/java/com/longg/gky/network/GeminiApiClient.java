package com.longg.gky.network;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

public class GeminiApiClient {

    private final OkHttpClient client = new OkHttpClient();
    private final String apiKey;
    private final String model;

    public interface ResultCallback {
        void onSuccess(String text);
        void onError(String error);
    }

    public GeminiApiClient(String apiKey, String model) {
        this.apiKey = apiKey;
        this.model = model == null ? "text-bison-001" : model;
    }

    public void generateText(String prompt, ResultCallback cb) {
        // NOTE: This implementation uses the Google Generative Language endpoint pattern.
        // You must provide a valid API key and model; adjust endpoint or body if your API differs.
        String url = "https://generativelanguage.googleapis.com/v1beta2/models/" + model + ":generateText?key=" + apiKey;

        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        String bodyJson = "{\"prompt\":{\"text\":\"" + escapeJson(prompt) + "\"},\"maxOutputTokens\":256}";

        RequestBody body = RequestBody.create(bodyJson, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                cb.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    cb.onError("HTTP " + response.code() + ": " + response.message());
                    return;
                }
                String resp = response.body() != null ? response.body().string() : "";
                // Try to parse JSON and extract best candidate text (common fields: content, output, text)
                try {
                    String extracted = tryExtractTextFromGenerativeResponse(resp);
                    cb.onSuccess(extracted);
                } catch (Exception ex) {
                    // fallback to raw response
                    cb.onSuccess(resp);
                }
            }
        });
    }

    private static String escapeJson(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }

    private static String tryExtractTextFromGenerativeResponse(String json) throws Exception {
        // Use org.json for lightweight parsing to avoid adding dependencies
        org.json.JSONObject obj = new org.json.JSONObject(json);
        if (obj.has("candidates")) {
            org.json.JSONArray arr = obj.getJSONArray("candidates");
            if (arr.length() > 0) {
                org.json.JSONObject first = arr.getJSONObject(0);
                // common fields used by different generative APIs
                if (first.has("content")) return first.getString("content");
                if (first.has("output")) return first.getString("output");
                if (first.has("text")) return first.getString("text");
            }
        }
        // some responses use 'candidates' nested deeper or different names
        if (obj.has("output")) {
            return obj.getString("output");
        }
        if (obj.has("response")) {
            return obj.getString("response");
        }
        // If nothing matches, throw to indicate parsing failed
        throw new Exception("No candidate text found");
    }
}
