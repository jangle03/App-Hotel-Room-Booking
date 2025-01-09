package com.example.appbooking.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appbooking.Database.MySQLite;
import com.example.appbooking.MainActivity;
import com.example.appbooking.R;

import java.io.ByteArrayOutputStream;

public class EditInfoUserActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    EditText edtName, edtUsername, edtPassword, edtEmail, edtSDT, edtCCCD, edtAddress;
    Button btnSave;
    ImageView imgAvatar, ivTogglePassword, btnBack;
    boolean isPasswordVisible = false;

    MySQLite db;
    String username, password, fullName, selectedImagePath = "ic_avt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info_user);
        // Khởi tạo csdl
        db = new MySQLite();

        // Khởi tạo các view
        edtName = findViewById(R.id.edtName);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtEmail = findViewById(R.id.edtEmail);
        edtSDT = findViewById(R.id.edtSDT);
        edtCCCD = findViewById(R.id.edtCCCD);
        edtAddress = findViewById(R.id.edtAddress);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);
        imgAvatar = findViewById(R.id.imgAvatar);
        ivTogglePassword = findViewById(R.id.ivTogglePassword);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");
        String email = intent.getStringExtra("email");
        String sdt = intent.getStringExtra("sdt");
        String cccd = intent.getStringExtra("cccd");
        String address = intent.getStringExtra("address");
        String avatarUri = intent.getStringExtra("avatar");

        // Đổ dữ liệu vào các EditText
        if (name != null) {
            edtName.setText(name);
        }
        if (username != null) {
            edtUsername.setText(username);
        }
        if (password != null) {
            edtPassword.setText(password);
        }
        if (email != null) {
            edtEmail.setText(email);
        }
        if (sdt != null) {
            edtSDT.setText(sdt);
        }
        if (cccd != null) {
            edtCCCD.setText(cccd);
        }
        if (address != null) {
            edtAddress.setText(address);
        }
        // Đặt avatar cố định
        imgAvatar.setImageResource(R.drawable.ic_avt); // Sử dụng ảnh trong drawable

        // Set ẩn hiện mật khẩu
        ivTogglePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivTogglePassword.setImageResource(R.drawable.hidden_password);
                } else {
                    edtPassword.setTransformationMethod(null);
                    ivTogglePassword.setImageResource(R.drawable.show_password);
                }
                isPasswordVisible = !isPasswordVisible;
            }
        });
//        // Set sự kiện khi người dùng ấn Lưu
//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Lấy thông tin từ các trường nhập liệu
//                String name = edtName.getText().toString().trim();
//                String username = edtUsername.getText().toString().trim();
//                String password = edtPassword.getText().toString().trim();
//                String email = edtEmail.getText().toString().trim();
//                String sdt = edtSDT.getText().toString().trim();
//                String cccd = edtCCCD.getText().toString().trim();
//                String address = edtAddress.getText().toString().trim();
//
//                try {
//                    String updateSQL = "UPDATE TAI_KHOAN SET name = '" + name + "', email = '" + email + "', " +
//                            "sdt = '" + sdt + "', cccd = '" + cccd + "', address = '" + address + "' WHERE username = '" + username + "'";
//                    db.updateSQL(updateSQL);
//
//                    Toast.makeText(EditInfoUserActivity.this, "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
//                } catch (Exception e) {
//                    Toast.makeText(EditInfoUserActivity.this, "Lỗi khi cập nhật dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        });

        // Set sự kiện cho ic_back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị AlertDialog để xác nhận lưu thay đổi
                new AlertDialog.Builder(EditInfoUserActivity.this)
                        .setTitle("Xác nhận")
                        .setMessage("Bạn có chắc chắn lưu thông tin vừa thay đổi?")
                        .setPositiveButton("OK", (dialog, which) -> {
                            // Lấy thông tin từ các trường nhập liệu
                            String name = edtName.getText().toString().trim();
                            String username = edtUsername.getText().toString().trim();
                            String password = edtPassword.getText().toString().trim();
                            String email = edtEmail.getText().toString().trim();
                            String sdt = edtSDT.getText().toString().trim();
                            String cccd = edtCCCD.getText().toString().trim();
                            String address = edtAddress.getText().toString().trim();

                            try {
                                String updateSQL = "UPDATE TAI_KHOAN SET name = '" + name + "', email = '" + email + "', " +
                                        "sdt = '" + sdt + "', cccd = '" + cccd + "', address = '" + address + "' WHERE username = '" + username + "'";
                                db.updateSQL(updateSQL);

                                Toast.makeText(EditInfoUserActivity.this, "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
                                // Quay lại trang InfoUserActivity
                                Intent intent = new Intent(EditInfoUserActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish(); // Đóng trang hiện tại
                            } catch (Exception e) {
                                Toast.makeText(EditInfoUserActivity.this, "Lỗi khi cập nhật dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Huỷ", (dialog, which) -> {
                            // Đóng AlertDialog, giữ nguyên EditInfoUserActivity
                            dialog.dismiss();
                        })
                        .show();
            }
        });
    }

}