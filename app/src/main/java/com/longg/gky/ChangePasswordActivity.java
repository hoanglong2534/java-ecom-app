package com.longg.gky;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.longg.gky.utils.AuthManager;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText etOld;
    private EditText etNew;
    private Button btnChange;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        etOld = findViewById(R.id.etOldPassword);
        etNew = findViewById(R.id.etNewPassword);
        btnChange = findViewById(R.id.btnChangePassword);

        btnChange.setOnClickListener(v -> {
            String oldP = etOld.getText() == null ? "" : etOld.getText().toString();
            String newP = etNew.getText() == null ? "" : etNew.getText().toString();
            if (oldP.isEmpty() || newP.isEmpty()) {
                Toast.makeText(this, "Please fill both fields", Toast.LENGTH_SHORT).show();
                return;
            }
            if (AuthManager.changePassword(this, oldP, newP)) {
                Toast.makeText(this, "Password changed", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Old password incorrect", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
