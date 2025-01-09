package com.example.appbooking;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appbooking.Activities.SignUpActivity;
import com.example.appbooking.Database.MySQLite;
import com.example.appbooking.Model.TaiKhoan;
import com.example.appbooking.page.DashboardActivity;
import com.example.appbooking.page.admin.homeAdmin;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Khai báo các biến
    EditText edtUsername, edtPassword;
    Button btnLogIn;
    ImageView ivTogglePassword;
    TextView txvSignUp;
    MySQLite db;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean isPasswordVisible = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setContentView(R.layout.activity_main);


        // Khởi tạo các view
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogIn = findViewById(R.id.btnLogIn);
        ivTogglePassword = findViewById(R.id.ivTogglePassword);
        txvSignUp = findViewById(R.id.txvSignUp);

        // Khởi tạo đối tượng cơ sở dữ liệu và SharedPreferences
        db = new MySQLite();
        sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        // Chuyen trang khi da danh nhap truoc do
        int userId_kt = sharedPreferences.getInt("userId", -1);
        String username_kt = sharedPreferences.getString("username", "Guest");
        int role_kt = sharedPreferences.getInt("role", -1);

        if(userId_kt != -1){
            String sql = "Select username from TAI_KHOAN where id = " + userId_kt + ";";
            List<List<Object>> list = db.executeQuery(sql); // Execute SQL query
            String tk_name = "";
            for (List<Object> row : list) {
                tk_name = row.get(0).toString();
            }
            if(tk_name.equals(username_kt)){
                if (role_kt == 0) {
                    Intent intentAdmin = new Intent(MainActivity.this, homeAdmin.class);
                    startActivity(intentAdmin);
                    finish(); // Đảm bảo đóng màn hình này
                } else if (role_kt == 1) {
                    Intent intentUser = new Intent(MainActivity.this, DashboardActivity.class);
                    startActivity(intentUser);
                    finish(); // Đảm bảo đóng màn hình này
                }
            }
        }

        // Định dạng TextView "Đăng ký"
        String fullText = "Bạn chưa có tài khoản? Đăng ký";
        String highlightText = "Đăng ký";
        int startIndex = fullText.indexOf(highlightText);
        int endIndex = startIndex + highlightText.length();
        SpannableString spannableString = new SpannableString(fullText);
        // Gạch chân
        spannableString.setSpan(new UnderlineSpan(), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Đổi màu
        spannableString.setSpan(new ForegroundColorSpan(Color.RED), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Gán chuỗi vào TextView
        txvSignUp.setText(spannableString);

        // Sự kiện ẩn/hiện mật khẩu
        ivTogglePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivTogglePassword.setImageResource(R.drawable.hidden_password);
                } else {
                    edtPassword.setTransformationMethod(null); // Hiển thị mật khẩu
                    ivTogglePassword.setImageResource(R.drawable.show_password);
                }
                isPasswordVisible = !isPasswordVisible; // Chuyển trạng thái hiển thị
            }
        });

//        btnLogIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String username = edtUsername.getText().toString().trim();
//                String password = edtPassword.getText().toString().trim();
//
//                // Xử lý Validate
//                if (username.isEmpty() && password.isEmpty()) {
//                    edtUsername.setError("Vui lòng nhập tên đăng nhập!");
//                    edtPassword.setError("Vui lòng nhập mật khẩu!");
//                } else if (username.isEmpty()) {
//                    edtUsername.setError("Vui lòng nhập tên đăng nhập!");
//                    edtPassword.setError(null);
//                } else if (password.isEmpty()) {
//                    edtPassword.setError("Vui lòng nhập mật khẩu!");
//                    edtUsername.setError(null);
//                } else {
//                    edtUsername.setError(null);
//                    edtPassword.setError(null);
//
//                    // Kiểm tra đăng nhập trong CSDL
//                    TaiKhoan taiKhoan = db.kiemTraDangNhap(username, password);
//
//                    if (taiKhoan != null && taiKhoan.getId() >= 0) {
//                        // Lưu thông tin vào SharedPreferences
//                        editor.putInt("userId", taiKhoan.getId());
//                        editor.putString("username", username);
//                        editor.putString("email", taiKhoan.getEmail());
//                        editor.putString("ten", taiKhoan.getName());
//                        editor.putString("hinh", taiKhoan.getHinh());
//                        editor.putInt("role", taiKhoan.getRole());
//                        editor.apply();
//
//                        // Điều hướng đến màn hình tiếp theo dựa trên role
//                        if (taiKhoan.getRole() == 0) {
//                            // Nếu là Admin
//                            Intent intentAdmin = new Intent(MainActivity.this, homeAdmin.class);
//                            startActivity(intentAdmin);
//                            finish(); // Đảm bảo đóng màn hình này
//                        } else if (taiKhoan.getRole() == 1) {
//                            // Nếu là User
//                            Intent intentUser = new Intent(MainActivity.this, DashboardActivity.class);
//                            startActivity(intentUser);
//                            finish(); // Đảm bảo đóng màn hình này
//                        }
//                    } else {
//                        // Nếu thông tin đăng nhập sai
//                        Toast.makeText(MainActivity.this, "Tên đăng nhập hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                // Xử lý Validate
                if (username.isEmpty() && password.isEmpty()) {
                    edtUsername.setError("Vui lòng nhập tên đăng nhập!");
                    edtPassword.setError("Vui lòng nhập mật khẩu!");
                } else if (username.isEmpty()) {
                    edtUsername.setError("Vui lòng nhập tên đăng nhập!");
                    edtPassword.setError(null);
                } else if (password.isEmpty()) {
                    edtPassword.setError("Vui lòng nhập mật khẩu!");
                    edtUsername.setError(null);
                } else {
                    edtUsername.setError(null);
                    edtPassword.setError(null);

                    // Kiểm tra đăng nhập trong CSDL
                    TaiKhoan taiKhoan = db.kiemTraDangNhap(username, password);

                    if (taiKhoan != null && taiKhoan.getId() >= 0) {
                        // Lưu thông tin vào SharedPreferences
                        editor.putInt("userId", taiKhoan.getId());
                        editor.putString("username", username);
                        editor.putString("email", taiKhoan.getEmail());
                        editor.putString("name", taiKhoan.getName());
                        editor.putString("hinh", taiKhoan.getHinh());
                        editor.putString("address", taiKhoan.getAddress());
                        editor.putString("cccd", taiKhoan.getCccd());
                        editor.putString("password", password); // Lưu mật khẩu vào SharedPreferences
                        editor.putInt("role", taiKhoan.getRole());
                        editor.apply();

                        // Điều hướng đến màn hình tiếp theo dựa trên role
//                        if (taiKhoan.getRole() == 0) {
//                            // Nếu là Admin
//                            Intent intentAdmin = new Intent(MainActivity.this, homeAdmin.class);
//                            startActivity(intentAdmin);
//                            finish(); // Đảm bảo đóng màn hình này
//                        } else if (taiKhoan.getRole() == 1) {
//                            // Nếu là User
//                            Intent intentUser = new Intent(MainActivity.this, DashboardActivity.class);
//                            startActivity(intentUser);
//                            finish(); // Đảm bảo đóng màn hình này
//                        }
//                        if (taiKhoan.getRole() == 0) {
//                            // Nếu là Admin
//                            Intent intentAdmin = new Intent(MainActivity.this, homeAdmin.class);
//                            startActivity(intentAdmin);
//                            finish(); // Đảm bảo đóng màn hình này
//                        } else if (taiKhoan.getRole() == 1) {
//                            // Nếu là User
//                            Intent intentUser = new Intent(MainActivity.this, DashboardActivity.class);
//                            startActivity(intentUser);
//                            finish(); // Đảm bảo đóng màn hình này
//                        }
                        if (taiKhoan.getRole() == 0) {
                            // Nếu là Admin
                            Intent intentAdmin = new Intent(MainActivity.this, homeAdmin.class);
                            startActivity(intentAdmin);
                            finish(); // Đảm bảo đóng màn hình này
                        } else if (taiKhoan.getRole() == 1) {
                            // Nếu là User
                            Intent intentUser = new Intent(MainActivity.this, DashboardActivity.class);
                            startActivity(intentUser);
                            finish(); // Đảm bảo đóng màn hình này
                        }
                    } else {
                        // Nếu thông tin đăng nhập sai
                        Toast.makeText(MainActivity.this, "Tên đăng nhập hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                    }
                }
//                // Chuyeen man tam thoi
//                Intent intentSignUp = new Intent(MainActivity.this, QuanLyMaGiamGia.class);
//                startActivity(intentSignUp);
            }
        });


        // Sự kiện chuyển tới màn hình Đăng ký
        txvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSignUp = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intentSignUp);
            }
        });
    }
}

