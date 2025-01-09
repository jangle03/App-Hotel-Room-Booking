package com.example.appbooking.page.admin.QuanLyMaGiamGia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appbooking.R;
import com.example.appbooking.page.admin.QuanLyMaGiamGia.Component.TaoMaGiamGia;
import com.example.appbooking.page.admin.QuanLyMaGiamGia.Component.ThongTinMaGiamGia;

public class QuanLyMaGiamGia extends AppCompatActivity {

    LinearLayout taoMaGiamGia, quanLyMaGiamGia;
    ImageView imageBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quan_ly_ma_giam_gia);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imageBack = findViewById(R.id.arrow_back_ios);
        taoMaGiamGia = findViewById(R.id.lnTaoMa);
        quanLyMaGiamGia = findViewById(R.id.lnQuanLy);

        taoMaGiamGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent taoMa = new Intent(QuanLyMaGiamGia.this, TaoMaGiamGia.class);
                startActivity(taoMa);
            }
        });

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        quanLyMaGiamGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent quanLy = new Intent(QuanLyMaGiamGia.this, ThongTinMaGiamGia.class);
                startActivity(quanLy);
            }
        });
    }
}