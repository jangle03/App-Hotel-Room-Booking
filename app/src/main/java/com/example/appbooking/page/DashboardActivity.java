package com.example.appbooking.page;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.appbooking.Activities.InfoUserActivity;
import com.example.appbooking.MainActivity;
//import com.example.appbooking.Manifest;
import com.example.appbooking.R;
import com.example.appbooking.page.customer.AccountFragment;
import com.example.appbooking.page.customer.HistoryFragment;
import com.example.appbooking.page.customer.HomeFragment;
import com.example.appbooking.page.customer.OrderHotelFragment;
import com.example.appbooking.page.customer.SettingFragment;
import com.google.android.material.navigation.NavigationView;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        // Kiểm tra quyền truy cập bộ nhớ ngoài
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }


        // Lấy thông tin từ SharedPreferences
        SharedPreferences sharedPreferences = this.getSharedPreferences("UserInfo", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);
        String username = sharedPreferences.getString("username", "Chưa có tên");
        String name = sharedPreferences.getString("name", "Người dùng");
        String email = sharedPreferences.getString("email", "Chưa có email");
        String hinh = sharedPreferences.getString("hinh", null); // Đường dẫn ảnh người dùng
        String cccd = sharedPreferences.getString("cccd", "Chưa có căn cước công dân");
        String address = sharedPreferences.getString("address", "Chưa có địa chỉ");

        // Kiểm tra và hiển thị thông tin người dùng
        if (userId != -1) {
            Toast.makeText(this, "ID người dùng: " + userId, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Không nhận được ID người dùng.", Toast.LENGTH_SHORT).show();
        }

        // Cập nhật NavigationView với tên người dùng, email và ảnh đại diện
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.fullName);
        TextView emailTextView = headerView.findViewById(R.id.gmail);
        ImageView avatarImageView = headerView.findViewById(R.id.avatar);

        userNameTextView.setText(name);
        emailTextView.setText(email);

        if (hinh != null && !hinh.isEmpty()) {
            if (hinh.equals("ic_avt")) {
                // Nếu đường dẫn là "ic_avt", đặt ảnh mặc định
                avatarImageView.setImageResource(R.drawable.ic_avt);
            } else {
                // Nếu đường dẫn không phải "ic_avt", tải ảnh từ đường dẫn
                try {
                    Uri avatarUri = Uri.parse("file://" + hinh);  // Đảm bảo sử dụng "file://" cho đường dẫn bộ nhớ ngoài
                    Glide.with(this)
                            .load(avatarUri) // Glide sẽ tải ảnh từ đường dẫn
                            .into(avatarImageView); // Đặt ảnh vào ImageView

                } catch (Exception e) {
                    // Nếu có lỗi khi tải ảnh, đặt ảnh mặc định
                    avatarImageView.setImageResource(R.drawable.ic_avt);
                }
            }
        } else {
            // Nếu không có ảnh, đặt ảnh mặc định
            avatarImageView.setImageResource(R.drawable.ic_avt);
        }

//        if (hinh != null && !hinh.equals("default.jpg")) {
//            // Nếu có đường dẫn hình hợp lệ, hiển thị hình từ URI
//            avatarImageView.setImageURI(Uri.parse(hinh));
//        } else {
//            // Nếu không, hiển thị ảnh mặc định
//            avatarImageView.setImageResource(R.drawable.avatar);
//        }


        // Thiết lập Drawer Layout
        drawerLayout = findViewById(R.id.drawer_layout);

        // Thiết lập Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Thiết lập Navigation Drawer
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Thay đổi fragment mặc định
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OrderHotelFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_hotel);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Điều hướng đến các fragment
        if (id == R.id.nav_home) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
//                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        } else if (id == R.id.nav_settings) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new SettingFragment())
                    .commit();
        } else if (id == R.id.nav_hotel) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new OrderHotelFragment())

//                    .replace(R.id.fragment_container, new OrderHotelFragment())
                    .commit();
        } else if (id == R.id.nav_account) {
            // Kiểm tra thông tin người dùng từ SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
            String username = sharedPreferences.getString("username", null);
            String name = sharedPreferences.getString("name", "Chưa có tên");
            String email = sharedPreferences.getString("email", "Chưa có email");
            String hinh = sharedPreferences.getString("hinh", null);
            String cccd = sharedPreferences.getString("cccd", "Chưa có căn cước công dân");
            String address = sharedPreferences.getString("address", "Chưa có địa chỉ");

            if (username != null) {
                // Nếu người dùng đã đăng nhập, mở InfoUserActivity
                Intent toInfoUser = new Intent(this, InfoUserActivity.class);
                toInfoUser.putExtra("username", username);
                toInfoUser.putExtra("name", name);
                toInfoUser.putExtra("email", email);
                toInfoUser.putExtra("hinh", hinh);
                toInfoUser.putExtra("cccd", cccd);  // Căn cước công dân
                toInfoUser.putExtra("address", address);  // Địa chỉ
                startActivity(toInfoUser);

            } else {
                // Nếu không có thông tin người dùng, hiển thị thông báo hoặc chuyển về màn hình đăng nhập
                Toast.makeText(this, "Vui lòng đăng nhập trước", Toast.LENGTH_SHORT).show();
                Intent toLogin = new Intent(this, MainActivity.class); // Hoặc màn hình đăng nhập
                startActivity(toLogin);
            }
        } else if (id == R.id.nav_logout) {
            // Đăng xuất và xóa thông tin khỏi SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_history_survey) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new HistoryFragment())
                    .commit();
        }
        // Đóng Navigation Drawer khi chọn mục
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
