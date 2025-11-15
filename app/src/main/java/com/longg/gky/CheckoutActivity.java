package com.longg.gky;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.longg.gky.utils.CartManager;

import java.text.DecimalFormat;

public class CheckoutActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tvSummary;
    private Button btnConfirm;
    private EditText etFullName;
    private EditText etAddress;
    private EditText etPhoneNumber;

    private CartManager cartManager;
    private DecimalFormat priceFormat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        toolbar = findViewById(R.id.toolbar);
        tvSummary = findViewById(R.id.tvSummary);
        btnConfirm = findViewById(R.id.btnConfirm);
        etFullName = findViewById(R.id.etFullName);
        etAddress = findViewById(R.id.etAddress);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        cartManager = CartManager.getInstance();
        priceFormat = new DecimalFormat("#,##0 ₫");

        updateSummary();

        btnConfirm.setOnClickListener(v -> {
            if (validateInput()) {
                cartManager.clearCart();
                Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_LONG).show();
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private boolean validateInput() {
        String fullName = etFullName.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();

        if (TextUtils.isEmpty(fullName)) {
            etFullName.setError("Vui lòng nhập họ và tên");
            etFullName.requestFocus();
            return false;
        }
        if (fullName.split("\\s+").length < 2) {
            etFullName.setError("Họ và tên phải có ít nhất 2 từ");
            etFullName.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(address)) {
            etAddress.setError("Vui lòng nhập địa chỉ");
            etAddress.requestFocus();
            return false;
        }
        if (address.length() < 10) {
            etAddress.setError("Địa chỉ phải có ít nhất 10 ký tự");
            etAddress.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(phoneNumber)) {
            etPhoneNumber.setError("Vui lòng nhập số điện thoại");
            etPhoneNumber.requestFocus();
            return false;
        }
        if (!phoneNumber.startsWith("0")) {
            etPhoneNumber.setError("Số điện thoại phải bắt đầu bằng 0");
            etPhoneNumber.requestFocus();
            return false;
        }
        if (phoneNumber.length() < 10 || phoneNumber.length() > 11) {
            etPhoneNumber.setError("Số điện thoại phải có 10 hoặc 11 chữ số");
            etPhoneNumber.requestFocus();
            return false;
        }
        if (!TextUtils.isDigitsOnly(phoneNumber)) {
            etPhoneNumber.setError("Số điện thoại chỉ được chứa các chữ số");
            etPhoneNumber.requestFocus();
            return false;
        }

        return true;
    }

    private void updateSummary() {
        double subtotal = cartManager.getTotalPrice();
        double tax = subtotal * 0.1; // 10% tax
        double total = subtotal + tax;
        String summaryText = String.format(
                "Tổng phụ: %s\nThuế (10%%): %s\nTổng cộng: %s",
                priceFormat.format(subtotal),
                priceFormat.format(tax),
                priceFormat.format(total)
        );
        tvSummary.setText(summaryText);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
