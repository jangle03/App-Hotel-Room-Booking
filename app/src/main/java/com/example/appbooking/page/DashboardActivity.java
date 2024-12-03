package com.example.appbooking.page;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.appbooking.Database.MySQLite;
import com.example.appbooking.MainActivity;
import com.example.appbooking.R;
import com.example.appbooking.Utils.SharedPreferencesHelper;
import com.example.appbooking.page.customer.AccountFragment;
import com.example.appbooking.page.customer.HistoryFragment;
import com.example.appbooking.page.customer.HomeFragment;
import com.example.appbooking.page.customer.BillHistory;
import com.example.appbooking.page.customer.OrderHotelFragment;
import com.example.appbooking.page.customer.SettingFragment;
import com.example.appbooking.page.customer.PayMentHouse;
import com.google.android.material.navigation.NavigationView;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    ImageView imgAvt;
    TextView tvTenUser, tvEmail;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        tvTenUser = headerView.findViewById(R.id.tvTenUser);
        tvEmail = headerView.findViewById(R.id.tvEmail);
        imgAvt = headerView.findViewById(R.id.imgAvt);
//
        SharedPreferences sharedPreferences = this.getSharedPreferences("UserInfo", this.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);
        String ten = sharedPreferences.getString("ten", "");
        String email = sharedPreferences.getString("email", "");
        String hinh = sharedPreferences.getString("hinh", "ic_avt");
//
        tvTenUser.setText(ten);
        tvEmail.setText(email);
        Toast.makeText(this, hinh, Toast.LENGTH_SHORT).show();
        MySQLite db = new MySQLite();
        String anh = db.getDrawableResourceUrl(this, hinh);
        imgAvt.setImageURI(Uri.parse(anh));


        // Xử lý insets cho drawer layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Thiết lập Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Thiết lập Navigation Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Thay đổi fragment mặc định
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        // Tự động thêm khoảng trống bên dưới thanh trạng thái
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.setFitsSystemWindows(true);
    }

    // Menu
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
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
                    .commit();
        } else if (id == R.id.nav_account) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new AccountFragment())
                    .commit();
        } else if (id == R.id.nav_history_survey) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new HistoryFragment())
                    .commit();
        }else if (id == R.id.nav_logout) {
            SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
        }

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
