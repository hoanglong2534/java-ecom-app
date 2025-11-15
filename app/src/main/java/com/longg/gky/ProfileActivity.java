package com.longg.gky;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.longg.gky.data.DBRepository;
import com.longg.gky.data.entities.ProductEntity;
import com.longg.gky.utils.AuthManager;
import com.longg.gky.utils.CartManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvUserName, tvUserAge, tvUserAddress;
    private Button btnAddProduct, btnLogout, btnLogin, btnSignUp;
    private View adminControls, guestControls;
    private Uri selectedImageUri;
    private ImageView dialogProductImage;

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    if (dialogProductImage != null) {
                        Glide.with(this).load(selectedImageUri).into(dialogProductImage);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initViews();
        setupBottomNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void initViews() {
        tvUserName = findViewById(R.id.tvUserName);
        tvUserAge = findViewById(R.id.tvUserAge);
        tvUserAddress = findViewById(R.id.tvUserAddress);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnLogout = findViewById(R.id.btnLogout);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);
        adminControls = findViewById(R.id.adminControls);
        guestControls = findViewById(R.id.guestControls);

        setupClickListeners();
    }

    private void updateUI() {
        if (AuthManager.isLoggedIn(this)) {
            guestControls.setVisibility(View.GONE);
            tvUserName.setVisibility(View.VISIBLE);
            tvUserAge.setVisibility(View.VISIBLE);
            tvUserAddress.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.VISIBLE);

            tvUserName.setText(AuthManager.getUserName(this));
            tvUserAge.setText("Tuổi: " + AuthManager.getUserAge(this));
            tvUserAddress.setText("Địa chỉ: " + AuthManager.getUserAddress(this));

            if (AuthManager.getRole(this).equals(AuthManager.ROLE_ADMIN)) {
                adminControls.setVisibility(View.VISIBLE);
            } else {
                adminControls.setVisibility(View.GONE);
            }
        } else {
            guestControls.setVisibility(View.VISIBLE);
            adminControls.setVisibility(View.GONE);
            tvUserName.setVisibility(View.GONE);
            tvUserAge.setVisibility(View.GONE);
            tvUserAddress.setVisibility(View.GONE);
            btnLogout.setVisibility(View.GONE);
        }
    }

    private void setupClickListeners() {
        btnAddProduct.setOnClickListener(v -> showAddProductDialog());
        btnLogout.setOnClickListener(v -> logout());
        btnLogin.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));
        btnSignUp.setOnClickListener(v -> startActivity(new Intent(this, SignUpActivity.class)));
    }

    private void logout() {
        AuthManager.logout(this);
        CartManager.getInstance().invalidate();
        updateUI();
        Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
    }

    private void showAddProductDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_add_product, null);
        builder.setView(dialogView);

        dialogProductImage = dialogView.findViewById(R.id.ivProductImage);
        Button btnSelectImage = dialogView.findViewById(R.id.btnSelectImage);
        EditText etProductName = dialogView.findViewById(R.id.etProductName);
        EditText etProductBrand = dialogView.findViewById(R.id.etProductBrand);
        EditText etProductPrice = dialogView.findViewById(R.id.etProductPrice);
        EditText etDiscount = dialogView.findViewById(R.id.etDiscount);
        EditText etProductDescription = dialogView.findViewById(R.id.etProductDescription);
        Button btnAddProductDialog = dialogView.findViewById(R.id.btnAddProduct);

        final AlertDialog dialog = builder.create();

        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        btnAddProductDialog.setOnClickListener(v -> {
            saveProduct(dialog, etProductName, etProductBrand, etProductPrice, etDiscount, etProductDescription);
        });
        selectedImageUri = null;
        dialog.show();
    }

    private String saveImageToInternalStorage(Uri uri) {
        if (uri == null) return null;
        File internalDir = getDir("product_images", Context.MODE_PRIVATE);
        String fileExtension = getFileExtension(uri);
        File imageFile = new File(internalDir, System.currentTimeMillis() + "." + fileExtension);

        try (InputStream in = getContentResolver().openInputStream(uri);
             OutputStream out = new FileOutputStream(imageFile)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            return imageFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void saveProduct(AlertDialog dialog, EditText name, EditText brand, EditText price, EditText discount, EditText description) {
        String nameStr = name.getText().toString().trim();
        String brandStr = brand.getText().toString().trim();
        String priceStr = price.getText().toString().trim();

        if (TextUtils.isEmpty(nameStr) || TextUtils.isEmpty(brandStr) || TextUtils.isEmpty(priceStr)) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin sản phẩm.", Toast.LENGTH_SHORT).show();
            return;
        }

        ProductEntity product = new ProductEntity();
        product.name = nameStr;
        product.brand = brandStr;
        product.originalPrice = Double.parseDouble(priceStr);
        String discountStr = discount.getText().toString().trim();
        int discountPercent = TextUtils.isEmpty(discountStr) ? 0 : Integer.parseInt(discountStr);
        product.price = product.originalPrice * (100 - discountPercent) / 100.0;
        product.description = description.getText().toString().trim();

        if (selectedImageUri != null) {
            String imagePath = saveImageToInternalStorage(selectedImageUri);
            product.imageUrl = imagePath;
        } else {
            product.imageUrl = "";
        }

        DBRepository.get().insertProductAsync(product, () -> {
            runOnUiThread(() -> {
                Toast.makeText(ProfileActivity.this, "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });
        });
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_profile) {
                return true;
            } else if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }
}
