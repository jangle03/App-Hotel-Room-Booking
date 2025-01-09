//package com.example.appbooking.Activities;
//
//import android.content.Intent;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.text.TextUtils;
//import android.util.Base64;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.appbooking.Database.MySQLite;
//import com.example.appbooking.MainActivity;
//import com.example.appbooking.Model.TaiKhoan;
//import com.example.appbooking.R;
//import com.example.appbooking.page.DashboardActivity;
//
//import java.io.ByteArrayOutputStream;
//
//public class AddInfoUserActivity extends AppCompatActivity {
//
//    private static final int PICK_IMAGE_REQUEST = 1;
//
//    EditText edtName, edtUsername, edtPassword, edtEmail, edtSDT, edtCCCD, edtAddress;
//    Button btnSave;
//    ImageView imgAvatar, ivTogglePassword;
//
//    MySQLite db;
//    String username, password, fullName, selectedImagePath = "default.jpg";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_info_user);
//
//        // Khởi tạo cơ sở dữ liệu
//        db = new MySQLite();
//
//        // Nhận dữ liệu từ Intent
//        Intent intent = getIntent();
//        username = intent.getStringExtra("USERNAME");
//        password = intent.getStringExtra("PASSWORD");
//        fullName = intent.getStringExtra("FULL_NAME");
//
//        // Khởi tạo các view
//        edtName = findViewById(R.id.edtName);
//        edtUsername = findViewById(R.id.edtUsername);
//        edtPassword = findViewById(R.id.edtPassword);
//        edtEmail = findViewById(R.id.edtEmail);
//        edtSDT = findViewById(R.id.edtSDT);
//        edtCCCD = findViewById(R.id.edtCCCD);
//        edtAddress = findViewById(R.id.edtAddress);
//        btnSave = findViewById(R.id.btnSave);
//        imgAvatar = findViewById(R.id.imgAvatar);
//        ivTogglePassword = findViewById(R.id.ivTogglePassword);
//
//        // Hiển thị thông tin mặc định
//        edtUsername.setText(username);
//        edtPassword.setText(password);
//        edtName.setText(fullName);
//
//        // Vô hiệu hóa các trường chỉnh sửa cho tên, tên đăng nhập và mật khẩu
//        edtUsername.setEnabled(false);
//        edtPassword.setEnabled(false);
//        edtName.setEnabled(false);
//
//        // Chọn ảnh từ thư viện
//        imgAvatar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(pickImageIntent, PICK_IMAGE_REQUEST);
//            }
//        });
//
//        // Sự kiện ẩn/hiện mật khẩu
//        ivTogglePassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (edtPassword.getTransformationMethod() instanceof android.text.method.PasswordTransformationMethod) {
//                    edtPassword.setTransformationMethod(null);
//                    ivTogglePassword.setImageResource(R.drawable.show_password);
//                } else {
//                    edtPassword.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
//                    ivTogglePassword.setImageResource(R.drawable.hidden_password);
//                }
//            }
//        });
//
//        // Khi người dùng nhấn "Lưu Thay Đổi"
//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Lấy thông tin từ các trường nhập liệu
//                String name = edtName.getText().toString().trim();
//                String username = edtUsername.getText().toString().trim();
//                String password = edtPassword.getText().toString().trim();
//                String email = edtEmail.getText().toString().trim();
//                String phone = edtSDT.getText().toString().trim();
//                String cccd = edtCCCD.getText().toString().trim();
//                String address = edtAddress.getText().toString().trim();
//
//                // Kiểm tra xem có trường nào bị bỏ trống
//                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(username) || TextUtils.isEmpty(password) ||
//                        TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(cccd) ||
//                        TextUtils.isEmpty(address)) {
//                    Toast.makeText(AddInfoUserActivity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                // Kiểm tra định dạng email
//                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//                    Toast.makeText(AddInfoUserActivity.this, "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                // Kiểm tra số điện thoại (đảm bảo chỉ có số và đủ độ dài)
//                if (!phone.matches("\\d{10,11}")) {
//                    Toast.makeText(AddInfoUserActivity.this, "Số điện thoại phải có 10-11 chữ số!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                // Kiểm tra nếu CCCD không hợp lệ
//                if (!cccd.matches("\\d{12}")) {
//                    Toast.makeText(AddInfoUserActivity.this, "CCCD phải có đúng 12 chữ số!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                // Lưu thông tin vào cơ sở dữ liệu
//                try {
//                    String updateSQL = "UPDATE TAI_KHOAN SET name = '" + name + "', email = '" + email + "', " +
//                            "sdt = '" + phone + "', cccd = '" + cccd + "', address = '" + address + "', " +
//                            "hinh = '" + selectedImagePath + "' WHERE username = '" + username + "'";
//                    db.updateSQL(updateSQL);
//
//                    Toast.makeText(AddInfoUserActivity.this, "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
//
//                    // Chuyển dữ liệu sang InfoUserActivity
//                    Intent toInfoUser = new Intent(AddInfoUserActivity.this, InfoUserActivity.class);
//                    toInfoUser.putExtra("username", username);
//                    toInfoUser.putExtra("name", name);
//                    toInfoUser.putExtra("email", email);
//                    toInfoUser.putExtra("phone", phone);
//                    toInfoUser.putExtra("cccd", cccd);
//                    toInfoUser.putExtra("address", address);
//                    toInfoUser.putExtra("hinh", encodeImageToBase64(selectedImagePath)); // Đưa ảnh dạng base64
//                    startActivity(toInfoUser);
//
//                    // Chuyển dữ liệu sang DashboardActivity
//                    Intent toDashboard = new Intent(AddInfoUserActivity.this, DashboardActivity.class);
//                    toDashboard.putExtra("name", name);
//                    toDashboard.putExtra("email", email);
//                    toDashboard.putExtra("hinh", encodeImageToBase64(selectedImagePath));
//                    startActivity(toDashboard);
//
//                    // Quay lại màn hình chính
//                    Intent backToMain = new Intent(AddInfoUserActivity.this, MainActivity.class);
//                    startActivity(backToMain);
//                    finish();
//                } catch (Exception e) {
//                    Toast.makeText(AddInfoUserActivity.this, "Lỗi khi cập nhật dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//    }
//
//    private String encodeImageToBase64(String hinh) {
//        Bitmap bitmap = BitmapFactory.decodeFile(hinh);
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//        byte[] byteArray = byteArrayOutputStream.toByteArray();
//        return Base64.encodeToString(byteArray, Base64.DEFAULT);
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            Uri selectedImageUri = data.getData();
//            selectedImagePath = getPathFromUri(selectedImageUri);
//
//            // Hiển thị hình ảnh được chọn
//            Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
//            imgAvatar.setImageBitmap(bitmap);
//        }
//    }
//
//    // Lấy đường dẫn thực tế từ URI
//    private String getPathFromUri(Uri uri) {
//        String[] projection = {MediaStore.Images.Media.DATA};
//        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
//        if (cursor != null) {
//            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//            String path = cursor.getString(columnIndex);
//            cursor.close();
//            return path;
//        }
//        return null;
//    }
//}
//

package com.example.appbooking.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appbooking.Database.MySQLite;
import com.example.appbooking.MainActivity;
import com.example.appbooking.R;
import com.example.appbooking.page.DashboardActivity;

public class AddInfoUserActivity extends AppCompatActivity {

    EditText edtName, edtUsername, edtPassword, edtEmail, edtSDT, edtCCCD, edtAddress;
    Button btnSave;
    ImageView imgAvatar, ivTogglePassword;

    MySQLite db;
    String username, password, fullName;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info_user);

        // Khởi tạo cơ sở dữ liệu
        db = new MySQLite();

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        username = intent.getStringExtra("USERNAME");
        password = intent.getStringExtra("PASSWORD");
        fullName = intent.getStringExtra("FULL_NAME");

        // Khởi tạo các view
        edtName = findViewById(R.id.edtName);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtEmail = findViewById(R.id.edtEmail);
        edtSDT = findViewById(R.id.edtSDT);
        edtCCCD = findViewById(R.id.edtCCCD);
        edtAddress = findViewById(R.id.edtAddress);
        btnSave = findViewById(R.id.btnSave);
        imgAvatar = findViewById(R.id.imgAvatar);
        ivTogglePassword = findViewById(R.id.ivTogglePassword);

        // Hiển thị thông tin mặc định
        edtUsername.setText(username);
        edtPassword.setText(password);
        edtName.setText(fullName);

        // Đặt avatar cố định
        imgAvatar.setImageResource(R.drawable.ic_avt);

        // Vô hiệu hóa các trường chỉnh sửa cho tên, tên đăng nhập và mật khẩu
        edtUsername.setEnabled(false);
        edtPassword.setEnabled(false);
        edtName.setEnabled(false);

        // Sự kiện ẩn/hiện mật khẩu
        ivTogglePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtPassword.getTransformationMethod() instanceof android.text.method.PasswordTransformationMethod) {
                    edtPassword.setTransformationMethod(null);
                    ivTogglePassword.setImageResource(R.drawable.show_password);
                } else {
                    edtPassword.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
                    ivTogglePassword.setImageResource(R.drawable.hidden_password);
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy thông tin từ các trường nhập liệu
                String name = edtName.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String phone = edtSDT.getText().toString().trim();
                String cccd = edtCCCD.getText().toString().trim();
                String address = edtAddress.getText().toString().trim();

//                // Kiểm tra xem có trường nào bị bỏ trống
//                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) ||
//                        TextUtils.isEmpty(phone) || TextUtils.isEmpty(cccd) ||
//                        TextUtils.isEmpty(address)) {
//                    Toast.makeText(AddInfoUserActivity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                // Kiểm tra định dạng email
//                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//                    Toast.makeText(AddInfoUserActivity.this, "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                // Kiểm tra số điện thoại (đảm bảo chỉ có số và đủ độ dài)
//                if (!phone.matches("\\d{10,11}")) {
//                    Toast.makeText(AddInfoUserActivity.this, "Số điện thoại phải có 10-11 chữ số!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                // Kiểm tra nếu CCCD không hợp lệ
//                if (!cccd.matches("\\d{12}")) {
//                    Toast.makeText(AddInfoUserActivity.this, "CCCD phải có đúng 12 chữ số!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                // Biến trạng thái để kiểm tra xem có trường nào bị bỏ trống
                boolean hasEmptyField = false;

                // Kiểm tra xem có trường nào bị bỏ trống
                if (TextUtils.isEmpty(name)) {
                    edtName.setError("Vui lòng nhập tên đầy đủ!");
                    hasEmptyField = true;
                }
                if (TextUtils.isEmpty(email)) {
                    edtEmail.setError("Vui lòng nhập email!");
                    hasEmptyField = true;
                }
                if (TextUtils.isEmpty(phone)) {
                    edtSDT.setError("Vui lòng nhập số điện thoại!");
                    hasEmptyField = true;
                }
                if (TextUtils.isEmpty(cccd)) {
                    edtCCCD.setError("Vui lòng nhập CCCD!");
                    hasEmptyField = true;
                }
                if (TextUtils.isEmpty(address)) {
                    edtAddress.setError("Vui lòng nhập địa chỉ!");
                    hasEmptyField = true;
                }

                // Nếu phát hiện bất kỳ trường nào trống, dừng lại và thông báo lỗi
                if (hasEmptyField) {
                    return;
                }

                // Kiểm tra định dạng email
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    edtEmail.setError("Email không hợp lệ!");
                    return;
                }

                // Kiểm tra số điện thoại (đảm bảo chỉ có số và đủ độ dài)
                if (!phone.matches("\\d{10,11}")) {
                    edtSDT.setError("Số điện thoại phải có 10-11 chữ số!");
                    return;
                }

                // Kiểm tra nếu CCCD không hợp lệ
                if (!cccd.matches("\\d{12}")) {
                    edtCCCD.setError("CCCD phải có đúng 12 chữ số!");
                    return;
                }


                // Nếu tất cả thông tin hợp lệ, xoá lỗi
                edtName.setError(null);
                edtEmail.setError(null);
                edtSDT.setError(null);
                edtCCCD.setError(null);
                edtAddress.setError(null);


                // Lưu thông tin vào SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name", name);
                editor.putString("username", username);
                editor.putString("email", email);
                editor.putString("phone", phone);
                editor.putString("cccd", cccd);
                editor.putString("address", address);
                editor.putString("avatar", "ic_avt");
                editor.apply(); // Áp dụng thay đổi

                // Lưu thông tin vào cơ sở dữ liệu
                try {
                    // Cập nhật avatar cố định (giả sử tên file là "ic_avt")
                    String updateSQL = "UPDATE TAI_KHOAN SET name = '" + name + "', email = '" + email + "', " +
                            "sdt = '" + phone + "', cccd = '" + cccd + "', address = '" + address + "', " +
                            "hinh = 'ic_avt' WHERE username = '" + username + "'";
                    db.updateSQL(updateSQL);

                    Toast.makeText(AddInfoUserActivity.this, "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();

                    // Chuyển dữ liệu sang InfoUserActivity
                    Intent toInfoUser = new Intent(AddInfoUserActivity.this, InfoUserActivity.class);
                    toInfoUser.putExtra("username", username);
                    toInfoUser.putExtra("name", name);
                    toInfoUser.putExtra("email", email);
                    toInfoUser.putExtra("phone", phone);
                    toInfoUser.putExtra("cccd", cccd);
                    toInfoUser.putExtra("address", address);
                    toInfoUser.putExtra("hinh", "ic_avt");
                    startActivity(toInfoUser);

                    // Chuyển dữ liệu sang DashboardActivity
                    Intent toDashboard = new Intent(AddInfoUserActivity.this, DashboardActivity.class);
                    toDashboard.putExtra("name", name);
                    toDashboard.putExtra("email", email);
                    toDashboard.putExtra("hinh", "ic_avt");
                    startActivity(toDashboard);

                    // Quay lại màn hình chính
                    Intent backToMain = new Intent(AddInfoUserActivity.this, MainActivity.class);
                    startActivity(backToMain);
                    finish();
                } catch (Exception e) {
                    Toast.makeText(AddInfoUserActivity.this, "Lỗi khi cập nhật dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}

