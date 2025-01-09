package com.example.appbooking.page.admin.QuanLyMaGiamGia.Component;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appbooking.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Chitietmauudai extends AppCompatActivity {

    TextView tenMGG, maGG, giaTriGG, ngayBatDauGG, ngayKetThucGG, trangThaiGG;
    Button bntBack, bntShare;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chitietmauudai);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ
        tenMGG = findViewById(R.id.tvTenMa);
        maGG = findViewById(R.id.tvMa);
        giaTriGG = findViewById(R.id.tvGiaTri);
        ngayBatDauGG = findViewById(R.id.tvThoigianbatdau);
        ngayKetThucGG = findViewById(R.id.tvThoigianketthuc);
        trangThaiGG = findViewById(R.id.tvTrangThai);
        bntBack = findViewById(R.id.btnBack);
        bntShare = findViewById(R.id.btnShare);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String tenMa = intent.getStringExtra("tenMa");
        int maGiamGia = intent.getIntExtra("maGiamGia", -1);
        double giaTriGiam = intent.getDoubleExtra("giamGia", 0.0);
        long ngayBatDauMillis = intent.getLongExtra("ngayBatDau", -1);
        long ngayKetThucMillis = intent.getLongExtra("ngayHetHan", -1);
        String trangThai = intent.getStringExtra("trangThai");

        // Chuyển đổi thời gian từ millis sang định dạng chuỗi
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String ngayBatDau = ngayBatDauMillis > 0 ? dateFormat.format(new Date(ngayBatDauMillis)) : "Không xác định";
        String ngayKetThuc = ngayKetThucMillis > 0 ? dateFormat.format(new Date(ngayKetThucMillis)) : "Không xác định";

        // Set text lên các TextView
        tenMGG.setText(tenMa != null ? tenMa : "Không xác định");
        maGG.setText(String.valueOf(maGiamGia));
        giaTriGG.setText( (giaTriGiam * 100) + "%");
        ngayBatDauGG.setText(ngayBatDau);
        ngayKetThucGG.setText(ngayKetThuc);
        trangThaiGG.setText(trangThai != null ? trangThai : "Không xác định");
        trangThaiGG.setTextColor(
                trangThai != null && trangThai.equals("Còn Hạn")
                        ? getResources().getColor(R.color.green60, null)
                        : getResources().getColor(R.color.red50, null)
        );

        bntBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bntShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // gửi ứng dụng này sang ứng dụng khác
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");

                String status = trangThai != null && trangThai.equals("Còn Hạn")
                        ? "Hãy tải app HotelBooking và đặt phòng, sau đó nhập mã " + maGG.getText() + " để cùng nhận những sự kiện hời bạn nhen!"
                        : "Nhưng hiện tại mã đã hết hạn rồi, bạn mau nhanh tay tải để nhận hàng ngàn ưu đãi khác nhen !";

                String shareText = "Bạn ơi, khách sạn chúng tớ có mã giảm giá << " + tenMGG.getText() + " >> siêu xịn xò cho các bạn đây! \n" + status
                        ;

                shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

                Intent chooserIntent = Intent.createChooser(shareIntent, "Chia sẻ qua");

                startActivity(chooserIntent);
            }
        });

    }

}