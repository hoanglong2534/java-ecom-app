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

// LƯU Ý: Đã đổi tên class và logic để hoạt động với OpenRouter
public class OpenAiApiClient {

    private final OkHttpClient client = new OkHttpClient();
    private final String apiKey;
    private final String model;

    public interface ResultCallback {
        void onSuccess(String text);
        void onError(String error);
    }

    public OpenAiApiClient(String apiKey, String model) {
        this.apiKey = apiKey;
        // Đặt model mặc định là của OpenRouter (ví dụ: gpt-3.5-turbo miễn phí của họ)
        this.model = model == null ? "openai/gpt-3.5-turbo" : model;
    }

    public void generateText(String prompt, ResultCallback cb) {
        // SỬA LỖI DỨT ĐIỂM: Dùng đúng URL của OpenRouter
        String url = "https://openrouter.ai/api/v1/chat/completions";

        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        try {
            // Cấu trúc JSON body cho OpenRouter (tương tự OpenAI)
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("model", this.model);
            JSONArray messages = new JSONArray();
            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", prompt);
            messages.put(userMessage);
            jsonBody.put("messages", messages);

            RequestBody body = RequestBody.create(jsonBody.toString(), JSON);

            // OpenRouter yêu cầu key trong Header
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    // OpenRouter yêu cầu thêm header này để họ biết ứng dụng của bạn là gì
                    .addHeader("HTTP-Referer", "https://github.com/long2534/BTL_GKI") // Thay bằng link repo của bạn
                    .addHeader("X-Title", "BTL_GKI") // Thay bằng tên app của bạn
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

    // Hàm phân tích JSON cho OpenRouter (giống OpenAI)
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
            return "Lỗi từ OpenRouter: " + obj.getJSONObject("error").getString("message");
        }
        throw new Exception("Không tìm thấy nội dung văn bản trong phản hồi.");
    }
}
