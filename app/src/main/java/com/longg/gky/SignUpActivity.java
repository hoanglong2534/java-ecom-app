package com.longg.gky;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.longg.gky.utils.AuthManager;

public class SignUpActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etAge;
    private EditText etAddress;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private Button btnSignUp;
    private Button btnGoToLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etUsername = findViewById(R.id.etUsername);
        etAge = findViewById(R.id.etAge);
        etAddress = findViewById(R.id.etAddress);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnGoToLogin = findViewById(R.id.btnGoToLogin);

        btnSignUp.setOnClickListener(v -> {
            if (validateInput()) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String age = etAge.getText().toString().trim();
                String address = etAddress.getText().toString().trim();

                if (AuthManager.saveUserWithRole(this, username, password, AuthManager.ROLE_USER, age, address)) {
                    Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    finish(); // Quay lại màn hình đăng nhập
                } else {
                    etUsername.setError("Tên đăng nhập đã tồn tại");
                    Toast.makeText(this, "Tên đăng nhập đã tồn tại.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnGoToLogin.setOnClickListener(v -> {
            finish(); // Đóng màn hình này để quay lại
        });
    }

    private boolean validateInput() {
        if (TextUtils.isEmpty(etUsername.getText())) {
            etUsername.setError("Tên đăng nhập không được để trống");
            return false;
        }
        if (TextUtils.isEmpty(etAge.getText())) {
            etAge.setError("Tuổi không được để trống");
            return false;
        }
        if (TextUtils.isEmpty(etAddress.getText())) {
            etAddress.setError("Địa chỉ không được để trống");
            return false;
        }
        if (TextUtils.isEmpty(etPassword.getText())) {
            etPassword.setError("Mật khẩu không được để trống");
            return false;
        }
        if (TextUtils.isEmpty(etConfirmPassword.getText())) {
            etConfirmPassword.setError("Vui lòng xác nhận mật khẩu");
            return false;
        }
        if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
            etConfirmPassword.setError("Mật khẩu không khớp");
            return false;
        }
        return true;
    }
}
