package com.longg.gky.network;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

// LƯU Ý: Tên lớp được giữ nguyên nhưng logic bên trong đã được thay đổi cho OpenAI.
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
        // Đặt model mặc định là gpt-3.5-turbo của OpenAI
        this.model = model == null ? "gpt-3.5-turbo" : model;
    }

    public void generateText(String prompt, ResultCallback cb) {
        // Endpoint của OpenAI
        String url = "https://api.openai.com/v1/chat/completions";

        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        try {
            // Cấu trúc JSON body cho OpenAI
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("model", this.model);
            JSONArray messages = new JSONArray();
            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", prompt);
            messages.put(userMessage);
            jsonBody.put("messages", messages);
            jsonBody.put("max_tokens", 256); // Giới hạn độ dài câu trả lời

            RequestBody body = RequestBody.create(jsonBody.toString(), JSON);

            // OpenAI yêu cầu key trong Header, không phải trong URL
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    cb.onError(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseBodyString = response.body() != null ? response.body().string() : "";
                    if (!response.isSuccessful()) {
                        cb.onError("HTTP " + response.code() + ": " + responseBodyString);
                        return;
                    }

                    try {
                        String extracted = extractTextFromOpenAiResponse(responseBodyString);
                        cb.onSuccess(extracted);
                    } catch (Exception ex) {
                        cb.onError("Lỗi phân tích JSON: " + ex.getMessage());
                    }
                }
            });
        } catch (org.json.JSONException e) {
            cb.onError("Lỗi tạo JSON: " + e.getMessage());
        }
    }

    // Hàm phân tích JSON cho phản hồi của OpenAI
    private String extractTextFromOpenAiResponse(String json) throws Exception {
        JSONObject obj = new JSONObject(json);
        JSONArray choices = obj.getJSONArray("choices");
        if (choices.length() > 0) {
            JSONObject firstChoice = choices.getJSONObject(0);
            JSONObject message = firstChoice.getJSONObject("message");
            if (message.has("content")) {
                return message.getString("content");
            }
        }
        if (obj.has("error")) {
            return "Lỗi từ OpenAI: " + obj.getJSONObject("error").getString("message");
        }
        throw new Exception("Không tìm thấy nội dung văn bản trong phản hồi.");
    }
}
