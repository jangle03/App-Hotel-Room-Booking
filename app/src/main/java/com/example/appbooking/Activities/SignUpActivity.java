package com.example.appbooking.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appbooking.Database.MySQLite;
import com.example.appbooking.MainActivity;
import com.example.appbooking.R;

public class SignUpActivity extends AppCompatActivity {
    EditText edtName, edtUsername, edtPassword, edtConfirmPassword;
    ImageView ivTogglePassword, ivToggleConfirmPassword;
    TextView txvSignIn;
    Button btnSignUp;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean isPasswordVisible = false;
    MySQLite db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        //Khởi tạo
        edtName = findViewById(R.id.edtName);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
//        imgLogout = findViewById(R.id.imgLogout);
        ivTogglePassword = findViewById(R.id.ivTogglePassword);
        ivToggleConfirmPassword = findViewById(R.id.ivToggleConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        txvSignIn = findViewById(R.id.txvSignIn);

        // Khởi tạo SharedPreferences
        db = new MySQLite();
        sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Định dạng TextView "Đăng ký"
        String fullText = "Bạn đã có tài khoản? Đăng nhập.";
        String highlightText = "Đăng nhập";
        int startIndex = fullText.indexOf(highlightText);
        int endIndex = startIndex + highlightText.length();
        SpannableString spannableString = new SpannableString(fullText);
        // Gạch chân
        spannableString.setSpan(new UnderlineSpan(), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Đổi màu
        spannableString.setSpan(new ForegroundColorSpan(Color.RED), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Gán chuỗi vào TextView
        txvSignIn.setText(spannableString);

        // Sự kiện chuyển tới màn hình Đăng ký
        txvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSignUp = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intentSignUp);
            }
        });

        // Sự kiện ẩn/hiện mật khẩu cho edtPassword
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

        // Sự kiện ẩn/hiện mật khẩu cho edtConfirmPassword
        ivToggleConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    edtConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivToggleConfirmPassword.setImageResource(R.drawable.hidden_password);
                } else {
                    edtConfirmPassword.setTransformationMethod(null);
                    ivToggleConfirmPassword.setImageResource(R.drawable.show_password);
                }
                isPasswordVisible = !isPasswordVisible;
            }
        });

//         Set sự kiện khi ấn Logout
//        imgLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                editor.clear().apply();
//                Intent intetLogIn = new Intent(SignUpActivity.this, MainActivity.class);
//                startActivity(intetLogIn);
//                finish();
//            }
//        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = edtName.getText().toString().trim();
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String confirmPassword = edtConfirmPassword.getText().toString().trim();

                // Các thông tin thêm khác
                String email = "example@gmail.com";
                String sdt = "0123456789";
                String cccd = "123456789012";
                String address = "Địa chỉ mặc định";
                String tenAnh = "ic_avt";
                int role = 1;

                // Kiểm tra hợp lệ
                boolean isValid = true;

                if (fullName.isEmpty()) {
                    edtName.setError("Vui lòng điền đầy đủ họ tên!");
                    isValid = false;
                }
                if (username.isEmpty()) {
                    edtUsername.setError("Vui lòng điền tên đăng nhập!");
                    isValid = false;
                } else if (username.length() < 2) {
                    edtUsername.setError("Tên đăng nhập phải có ít nhất 2 ký tự!");
                    isValid = false;
                }
                if (password.isEmpty()) {
                    edtPassword.setError("Vui lòng điền mật khẩu");
                    isValid = false;
                } else if (password.length() < 6) {
                    edtPassword.setError("Mật khẩu phải có độ dài tối thiểu là 6 ký tự");
                    isValid = false;
                }
                if (confirmPassword.isEmpty()) {
                    edtConfirmPassword.setError("Vui lòng nhập lại mật khẩu");
                    isValid = false;
                } else if (!confirmPassword.equals(password)) {
                    edtConfirmPassword.setError("Mật khẩu không khớp");
                    isValid = false;
                }

                if (isValid) {
                    // Gọi phương thức thêm tài khoản từ MySQLite
                    String result = db.insertDataTaiKhoan(username, password, fullName, email, sdt, cccd, address, role, tenAnh);

                    if (result.equals("Tài khoản đã tồn tại")) {
                        // Nếu tài khoản đã tồn tại, đặt con trỏ vào ô tên đăng nhập và hiển thị lỗi
                        edtUsername.requestFocus();  // Đặt con trỏ vào ô tên đăng nhập
                        edtUsername.setError("Tài khoản đã tồn tại. Vui lòng nhập tên đăng nhập khác!");
                    }
                    else if (result.equals("Thêm thành công")) {
                        // Nếu đăng ký thành công, hiển thị thông báo thành công
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                        LayoutInflater inflater = getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.dialog_register_success, null);

                        // Khởi tạo các thành phần trong dialog
                        TextView txtMessage = dialogView.findViewById(R.id.txtMessage);
                        Button btnOk = dialogView.findViewById(R.id.btnOk);

                        // Thiết lập nội dung
                        txtMessage.setText("Bạn đã đăng ký thành công. Hãy bổ sung thêm thông tin!");

                        // Khi người dùng nhấn OK
                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Chuyển đến AddInfoUserActivity và truyền thông tin
                                Intent intent = new Intent(SignUpActivity.this, AddInfoUserActivity.class);
                                intent.putExtra("USERNAME", username);
                                intent.putExtra("PASSWORD", password);
                                intent.putExtra("FULL_NAME", fullName);
                                startActivity(intent);
                                finish();
                            }
                        });

                        // Tạo và hiển thị AlertDialog
                        builder.setView(dialogView);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else {
                        // Xử lý lỗi thêm tài khoản
                        Toast.makeText(SignUpActivity.this, result, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    // Kiểm tra sự tồn tại của tài khoản, CCCD hoặc số điện thoại
    private boolean isAccountExists(String value, String field) {
        String existingValue = sharedPreferences.getString(field, null);
        return existingValue != null && existingValue.equals(value);
    }

    // Lưu thông tin tài khoản vào SharedPreferences
    private void saveAccountToSharedPreferences(String username, String fullName, String password) {
        editor.putString("username", username);
        editor.putString("fullName", fullName);
        editor.putString("password", password);
        editor.apply();

        // Chuyển tới MainActivity sau khi đăng ký thành công
        Toast.makeText(SignUpActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
