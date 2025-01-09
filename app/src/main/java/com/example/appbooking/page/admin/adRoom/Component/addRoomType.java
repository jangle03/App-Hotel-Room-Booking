package com.example.appbooking.page.admin.adRoom.Component;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appbooking.Database.MySQLite;
import com.example.appbooking.R;

public class addRoomType extends AppCompatActivity {
    EditText edtTenLoaiPhong, edtGiaPhong, edtSoLuongNguoi, edtMoTaPhong, ad_rt_mttt;
    Button btnLuuLoaiPhong;
    MySQLite db1 = new MySQLite();
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_room_type);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Ánh xạ các view bằng findViewById
        edtTenLoaiPhong = findViewById(R.id.ad_rt_ten);
        edtGiaPhong = findViewById(R.id.ad_rt_gia);
        edtSoLuongNguoi = findViewById(R.id.ad_rt_sl);
        edtMoTaPhong = findViewById(R.id.ad_rt_mt);
        ad_rt_mttt = findViewById(R.id.ad_rt_mttt);
        btnLuuLoaiPhong = findViewById(R.id.rt_ad_btn);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnLuuLoaiPhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra tên loại phòng
                String tenLoaiPhong = edtTenLoaiPhong.getText().toString().trim();
                if (tenLoaiPhong.isEmpty()) {
                    Toast.makeText(addRoomType.this, "Hãy nhập tên loại phòng", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Kiểm tra giá phòng
                String giaPhongText = edtGiaPhong.getText().toString().trim();
                if (giaPhongText.isEmpty()) {
                    Toast.makeText(addRoomType.this, "Hãy nhập giá phòng", Toast.LENGTH_SHORT).show();
                    return;
                }
                int giaPhong = Integer.parseInt(giaPhongText);
                if (giaPhong < 0) {
                    Toast.makeText(addRoomType.this, "Giá phòng không được nhỏ hơn 0", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Kiểm tra số lượng người
                String soLuongText = edtSoLuongNguoi.getText().toString().trim();
                if (soLuongText.isEmpty()) {
                    Toast.makeText(addRoomType.this, "Hãy nhập số lượng người", Toast.LENGTH_SHORT).show();
                    return;
                }
                int soLuongNguoi = Integer.parseInt(soLuongText);
                if (soLuongNguoi < 0) {
                    Toast.makeText(addRoomType.this, "Số lượng người không được nhỏ hơn 0", Toast.LENGTH_SHORT).show();
                    return;
                }
                String moTaPhongc = ad_rt_mttt.getText().toString().trim();
                if (moTaPhongc.isEmpty()) {
                    Toast.makeText(addRoomType.this, "Hãy nhập mô tả phòng", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Kiểm tra mô tả phòng
                String moTaPhong = edtMoTaPhong.getText().toString().trim();
                if (moTaPhong.isEmpty()) {
                    Toast.makeText(addRoomType.this, "Hãy nhập mô tả chi tiết phòng", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    db1.insertDataLoaiPhong(tenLoaiPhong, giaPhong, soLuongNguoi, moTaPhongc, moTaPhong);
                    Toast.makeText(addRoomType.this, "Thêm loại phòng thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } catch (Exception e) {
                    Toast.makeText(addRoomType.this, "Thêm loại phòng không thành công!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}