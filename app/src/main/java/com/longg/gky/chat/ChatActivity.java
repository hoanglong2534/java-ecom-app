package com.longg.gky.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.longg.gky.R;
import com.longg.gky.network.OpenAiApiClient; // Đã đổi sang OpenAiApiClient

public class ChatActivity extends AppCompatActivity {

    private ChatAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        RecyclerView rv = findViewById(R.id.rv_chat);
        adapter = new ChatAdapter();
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        ProgressBar pb = findViewById(R.id.pb_loading);

        EditText et = findViewById(R.id.et_message);
        Button btn = findViewById(R.id.btn_send);


        String apiKey = "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";

        if (apiKey == null || apiKey.isEmpty()) {
            Toast.makeText(this, "API key rỗng!", Toast.LENGTH_LONG).show();
            return;
        }

        // Sử dụng class OpenAiApiClient mới
        OpenAiApiClient client = new OpenAiApiClient(apiKey, null);

        btn.setOnClickListener(v -> {
            String text = et.getText().toString().trim();
            if (text.isEmpty()) return;
            // show user message and scroll
            adapter.addMessage(new ChatMessage(text, true));
            rv.scrollToPosition(adapter.getItemCount() - 1);
            et.setText("");

            // show loading
            pb.setVisibility(View.VISIBLE);
            btn.setEnabled(false);

            client.generateText(text, new OpenAiApiClient.ResultCallback() {
                @Override
                public void onSuccess(String botText) {
                    runOnUiThread(() -> {
                        pb.setVisibility(View.GONE);
                        btn.setEnabled(true);
                        adapter.addMessage(new ChatMessage(botText, false));
                        rv.scrollToPosition(adapter.getItemCount() - 1);
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        pb.setVisibility(View.GONE);
                        btn.setEnabled(true);
                        Toast.makeText(ChatActivity.this, "Lỗi: " + error, Toast.LENGTH_LONG).show();
                        adapter.addMessage(new ChatMessage("Xin lỗi, đã xảy ra lỗi: " + error, false));
                        rv.scrollToPosition(adapter.getItemCount() - 1);
                    });
                }
            });
        });
    }
}
