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
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.longg.gky.data.DBRepository;
import com.longg.gky.data.entities.ProductEntity;
import com.longg.gky.models.Product;
import com.longg.gky.utils.AuthManager;
import com.longg.gky.utils.CartManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView ivProductImage;
    private TextView tvProductName, tvProductBrand, tvProductPrice, tvOriginalPrice, tvProductDescription;
    private Button btnAddToCart, btnBuyNow, btnEditProduct;
    private Toolbar toolbar;
    private Product product;
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
        setContentView(R.layout.activity_product_detail);
        initViews();
        setupToolbar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProductData();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        ivProductImage = findViewById(R.id.ivProductImage);
        tvProductName = findViewById(R.id.tvProductName);
        tvProductBrand = findViewById(R.id.tvProductBrand);
        tvProductPrice = findViewById(R.id.tvProductPrice);
        tvOriginalPrice = findViewById(R.id.tvOriginalPrice);
        tvProductDescription = findViewById(R.id.tvProductDescription);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnBuyNow = findViewById(R.id.btnBuyNow);
        btnEditProduct = findViewById(R.id.btnEditProduct);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Chi tiết sản phẩm");
        }
    }

    private void loadProductData() {
        int productId = getIntent().getIntExtra("product_id", -1);
        if (productId == -1) {
            finish();
            return;
        }
        DBRepository.get().getProductByIdAsync(productId, productEntity -> {
            if (productEntity == null) {
                finish();
                return;
            }
            this.product = Product.fromEntity(productEntity);
            runOnUiThread(this::displayProductDetails);
        });
    }

    private void displayProductDetails() {
        tvProductName.setText(product.getName());
        tvProductBrand.setText(product.getBrand());
        tvProductDescription.setText(product.getDescription());

        DecimalFormat formatter = new DecimalFormat("#,##0 ₫");
        tvProductPrice.setText(formatter.format(product.getPrice()));

        if (product.hasDiscount()) {
            tvOriginalPrice.setVisibility(View.VISIBLE);
            tvOriginalPrice.setPaintFlags(tvOriginalPrice.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
            tvOriginalPrice.setText(formatter.format(product.getOriginalPrice()));
        } else {
            tvOriginalPrice.setVisibility(View.GONE);
        }

        String imageUrl = product.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            if (imageUrl.startsWith("/")) {
                Glide.with(this).load(new File(imageUrl)).placeholder(R.drawable.placeholder_product).error(R.drawable.placeholder_product).into(ivProductImage);
            } else {
                 try {
                    int resId = Integer.parseInt(imageUrl.trim());
                    Glide.with(this).load(resId).placeholder(R.drawable.placeholder_product).error(R.drawable.placeholder_product).into(ivProductImage);
                } catch (NumberFormatException e) {
                    Glide.with(this).load(Uri.parse(imageUrl)).placeholder(R.drawable.placeholder_product).error(R.drawable.placeholder_product).into(ivProductImage);
                }
            }
        } else {
            Glide.with(this).load(R.drawable.placeholder_product).into(ivProductImage);
        }


        if (AuthManager.getRole(this).equals(AuthManager.ROLE_ADMIN)) {
            btnEditProduct.setVisibility(View.VISIBLE);
        } else {
            btnEditProduct.setVisibility(View.GONE);
        }

        setupClickListeners();
    }

    private void setupClickListeners() {
        btnAddToCart.setOnClickListener(v -> {
            CartManager.getInstance().addToCart(product);
            Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        });
        btnBuyNow.setOnClickListener(v -> {
            CartManager.getInstance().addToCart(product);
            startActivity(new Intent(this, CartActivity.class));
        });
        btnEditProduct.setOnClickListener(v -> showEditProductDialog());
    }

    private void showEditProductDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.activity_add_product, null);
        builder.setView(dialogView);

        dialogProductImage = dialogView.findViewById(R.id.ivProductImage);
        Button btnSelectImage = dialogView.findViewById(R.id.btnSelectImage);
        EditText etProductName = dialogView.findViewById(R.id.etProductName);
        EditText etProductBrand = dialogView.findViewById(R.id.etProductBrand);
        EditText etProductPrice = dialogView.findViewById(R.id.etProductPrice);
        EditText etDiscount = dialogView.findViewById(R.id.etDiscount);
        EditText etProductDescription = dialogView.findViewById(R.id.etProductDescription);
        Button btnUpdate = dialogView.findViewById(R.id.btnAddProduct);

        etProductName.setText(product.getName());
        etProductBrand.setText(product.getBrand());
        etProductPrice.setText(String.valueOf((int)product.getOriginalPrice()));
        etDiscount.setText(String.valueOf(product.getDiscountPercentage()));
        etProductDescription.setText(product.getDescription());
        btnUpdate.setText("Lưu thay đổi");

        String imageUrl = product.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            if (imageUrl.startsWith("/")) {
                Glide.with(this).load(new File(imageUrl)).into(dialogProductImage);
            } else {
                try {
                    int resId = Integer.parseInt(imageUrl.trim());
                    Glide.with(this).load(resId).into(dialogProductImage);
                } catch (NumberFormatException e) {
                    Glide.with(this).load(Uri.parse(imageUrl)).into(dialogProductImage);
                }
            }
        }
        selectedImageUri = null;

        final AlertDialog dialog = builder.create();

        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        btnUpdate.setOnClickListener(v -> {
            updateProduct(dialog, etProductName, etProductBrand, etProductPrice, etDiscount, etProductDescription);
        });

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

    private void updateProduct(AlertDialog dialog, EditText name, EditText brand, EditText price, EditText discount, EditText description) {
        String nameString = name.getText().toString().trim();
        String brandString = brand.getText().toString().trim();
        String priceString = price.getText().toString();
        String discountString = discount.getText().toString();
        String descriptionString = description.getText().toString().trim();

        double originalPrice = 0.0;
        if (!TextUtils.isEmpty(priceString)) {
            try {
                originalPrice = Double.parseDouble(priceString);
            } catch (NumberFormatException e) {
                originalPrice = 0.0;
            }
        }

        int discountPercent = 0;
        if (!TextUtils.isEmpty(discountString)) {
            try {
                discountPercent = Integer.parseInt(discountString);
            } catch (NumberFormatException e) {
                discountPercent = 0;
            }
        }

        product.setName(nameString);
        product.setBrand(brandString);
        product.setOriginalPrice(originalPrice);
        product.setDescription(descriptionString);
        product.setPrice(originalPrice * (100 - discountPercent) / 100.0);

        if (selectedImageUri != null) {
            String imagePath = saveImageToInternalStorage(selectedImageUri);
            if (imagePath != null) {
                product.setImageUrl(imagePath);
            }
        }

        DBRepository.get().updateProductAsync(product.toEntity(), () -> {
            runOnUiThread(() -> {
                dialog.dismiss();
                Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                displayProductDetails();
            });
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
