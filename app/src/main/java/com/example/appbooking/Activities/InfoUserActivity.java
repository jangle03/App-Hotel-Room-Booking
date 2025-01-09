
package com.example.appbooking.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appbooking.MainActivity;
import com.example.appbooking.R;
import com.example.appbooking.page.admin.quanLyUser.Component.editUser;

public class InfoUserActivity extends AppCompatActivity {

    TextView tv_name, tv_username, tv_email, tv_sdt, tv_cccd, tv_address;
    ImageView imgAvatar, btnBack;
    Button btnEdit;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_user);

        // Ánh xạ các view
        tv_name = findViewById(R.id.tv_name);
        tv_username = findViewById(R.id.tv_username);
        tv_email = findViewById(R.id.tv_email);
        tv_cccd = findViewById(R.id.tv_cccd);
        tv_sdt = findViewById(R.id.tv_sdt);
        tv_address = findViewById(R.id.tv_address);
        imgAvatar = findViewById(R.id.imgAvatar);
        btnBack = findViewById(R.id.btnBack);
        btnEdit = findViewById(R.id.btnEdit);

        // Nhận dữ liệu từ Intent
        String username = getIntent().getStringExtra("username");
        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String sdt = getIntent().getStringExtra("phone");
        String hinh = getIntent().getStringExtra("hinh");
        String cccd = getIntent().getStringExtra("cccd");
        String address = getIntent().getStringExtra("address");


        // Hiển thị thông tin trên giao diện
        if (name != null) {
            tv_name.setText(name);
        } else {
            tv_name.setText("Chưa có tên");
        }

        if (username != null) {
            tv_username.setText(username);
        } else {
            tv_username.setText("Chưa có username");
        }

        if (email != null) {
            tv_email.setText(email);
        } else {
            tv_email.setText("Chưa có email");
        }

//        if (sdt != null) {
//            tv_sdt.setText(sdt);
//        } else {
//            tv_sdt.setText("Chưa có sdt");
//        }

        if (cccd != null) {
            tv_cccd.setText(cccd);
        } else {
            tv_cccd.setText("Căn cước công dân: Chưa có");
        }

        if (address != null) {
            tv_address.setText(address);
        } else {
            tv_address.setText("Địa chỉ: Chưa có");
        }

        if (hinh != null && !hinh.equals("ic_avt")) {
            // Nếu có đường dẫn hình hợp lệ, hiển thị hình từ URI
            imgAvatar.setImageURI(Uri.parse(hinh));
        } else {
            // Nếu không, hiển thị ảnh mặc định
            imgAvatar.setImageResource(R.drawable.ic_avt);
        }
        // Set sự kiện cho ic_back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Set sự kiện cho button Edit
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
                String password = sharedPreferences.getString("password", ""); //Lấy mật khâur từ share
                Intent intentEdit = new Intent(InfoUserActivity.this, EditInfoUserActivity.class);
                // Truyền dữ liệu người dùng sang EditInfoUser
                intentEdit.putExtra("username", tv_username.getText().toString());
                intentEdit.putExtra("name", tv_name.getText().toString());
                intentEdit.putExtra("email", tv_email.getText().toString());
                intentEdit.putExtra("sdt", tv_sdt.getText().toString());
                intentEdit.putExtra("cccd", tv_cccd.getText().toString());
                intentEdit.putExtra("address", tv_address.getText().toString());
                intentEdit.putExtra("password", password); // Truyền mật khẩu qua Intent
                // Truyền avatar (URI hoặc tên file avatar)
                String avatarUri = imgAvatar.getTag() != null ? imgAvatar.getTag().toString() : "ic_avt";
                intentEdit.putExtra("avatar", avatarUri);
                startActivity(intentEdit);
                //Truyền mật khẩu từ trang đăng nhập sang

            }
        });
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        loadUserInfo();  // Tải lại thông tin người dùng từ SharedPreferences
//    }
//
//    private void loadUserInfo() {
//        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
//
//        String name = sharedPreferences.getString("name", "Chưa có tên");
//        String username = sharedPreferences.getString("username", "Chưa có username");
//        String email = sharedPreferences.getString("email", "Chưa có email");
//        String sdt = sharedPreferences.getString("sdt", "Chưa có số điện thoại");  // Đảm bảo key là "phone"
//        String cccd = sharedPreferences.getString("cccd", "Chưa có căn cước công dân");
//        String address = sharedPreferences.getString("address", "Chưa có địa chỉ");
//        String avatarUri = sharedPreferences.getString("avatar", "ic_avt");
//
////        // Kiểm tra xem giá trị có hợp lệ không
////        Log.d("UserInfo", "Phone: " + phone);  // Log số điện thoại ra để kiểm tra
//
//        // Cập nhật UI
//        tv_name.setText(name);
//        tv_username.setText(username);
//        tv_email.setText(email);
//        tv_sdt.setText(sdt);  // Hiển thị số điện thoại
//        tv_cccd.setText(cccd);
//        tv_address.setText(address);
//
//        // Hiển thị avatar
//        if (!avatarUri.equals("ic_avt")) {
//            imgAvatar.setImageURI(Uri.parse(avatarUri));
//        } else {
//            imgAvatar.setImageResource(R.drawable.avatar);
//        }
//    }
}

