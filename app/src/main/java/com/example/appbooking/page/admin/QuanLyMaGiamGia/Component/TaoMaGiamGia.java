package com.example.appbooking.page.admin.QuanLyMaGiamGia.Component;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appbooking.Database.MySQLite;
import com.example.appbooking.MainActivity;
import com.example.appbooking.R;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TaoMaGiamGia extends AppCompatActivity {

    private Button btnPickDateTime, btnPickEndDateTime, btnTaoMa;
    private long startTimeInMillis = -1;
    private long endTimeInMillis = -1;
    private int userId;
    private EditText tenUD, giamGia, dieuKien;
    private TextView tvNgayBatDau, tvNgayHetHan;
    private MySQLite db;
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkToken(); // Kiểm tra token trước khi xử lý
        setContentView(R.layout.activity_tao_ma_giam_gia);
        initViews();
        setupListeners();
    }

    // Ánh xạ view
    private void initViews() {
        imgBack = findViewById(R.id.imgBack);
        btnPickDateTime = findViewById(R.id.bntThoiGianBatDau);
        btnPickEndDateTime = findViewById(R.id.bntThoiGianKetThuc);
        btnTaoMa = findViewById(R.id.btnTaoMa);
        tenUD = findViewById(R.id.edtTenMaUuDai);
        giamGia = findViewById(R.id.edtGiamGia);
        dieuKien = findViewById(R.id.edtDieuKien);
        tvNgayBatDau = findViewById(R.id.tvNgayBatDau);
        tvNgayHetHan = findViewById(R.id.tvNgayHetHan);
        db = new MySQLite();
    }

    // Thiết lập sự kiện cho các nút
    private void setupListeners() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnPickDateTime.setOnClickListener(v -> pickDateTime(true));
        btnPickEndDateTime.setOnClickListener(v -> pickDateTime(false));
        btnTaoMa.setOnClickListener(v -> handleCreateDiscountCode());
    }

    // Hàm chọn ngày giờ bắt đầu hoặc kết thúc
    private void pickDateTime(boolean isStartTime) {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(isStartTime ? "Chọn ngày bắt đầu" : "Chọn ngày kết thúc")
                .setCalendarConstraints(new CalendarConstraints.Builder().build())
                .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(selection);

            MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(calendar.get(Calendar.HOUR_OF_DAY))
                    .setMinute(calendar.get(Calendar.MINUTE))
                    .setTitleText(isStartTime ? "Chọn giờ phút bắt đầu" : "Chọn giờ phút kết thúc")
                    .build();

            timePicker.addOnPositiveButtonClickListener(view -> {
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                calendar.set(Calendar.MINUTE, timePicker.getMinute());
                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
                String selectedDateTime = dateTimeFormat.format(calendar.getTime());

                if (isStartTime) {
                    startTimeInMillis = calendar.getTimeInMillis();
                    tvNgayBatDau.setText(selectedDateTime);
                    tvNgayBatDau.setTextColor(ContextCompat.getColor(this, R.color.brandBlue30));
                } else {
                    long selectedEndTime = calendar.getTimeInMillis();
                    if (startTimeInMillis != -1 && selectedEndTime <= startTimeInMillis) {
                        tvNgayHetHan.setText("Lỗi: Thời gian kết thúc phải lớn hơn thời gian bắt đầu!");
                        tvNgayHetHan.setTextColor(Color.RED);
                    } else {
                        endTimeInMillis = selectedEndTime;
                        tvNgayHetHan.setText(selectedDateTime);
                        tvNgayHetHan.setTextColor(ContextCompat.getColor(this, R.color.brandBlue30));
                    }
                }
            });

            timePicker.show(getSupportFragmentManager(), isStartTime ? "START_TIME_PICKER" : "END_TIME_PICKER");
        });

        datePicker.show(getSupportFragmentManager(), isStartTime ? "START_DATE_PICKER" : "END_DATE_PICKER");
    }

    // Xử lý logic tạo mã giảm giá
    private void handleCreateDiscountCode() {
        boolean isValid = true;
        String tenUuDai = tenUD.getText().toString().trim();
        String giamGiaValue = giamGia.getText().toString().trim();
        String dieuKienValue = dieuKien.getText().toString().trim();

        if (tenUuDai.isEmpty()) {
            tenUD.setError("Vui lòng nhập tên mã ưu đãi!");
            isValid = false;
        }

        int giamGiaInt = 0;
        try {
            giamGiaInt = Integer.parseInt(giamGiaValue);
            if (giamGiaInt <= 0 || giamGiaInt > 100) {
                giamGia.setError("Giảm giá phải từ 1% đến 100%!");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            giamGia.setError("Vui lòng nhập phần trăm giảm giá hợp lệ!");
            isValid = false;
        }

        int dieuKienInt = 0;
        try {
            dieuKienInt = Integer.parseInt(dieuKienValue);
            if (dieuKienInt < 0) {
                dieuKien.setError("Điều kiện phải là số nguyên dương!");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            dieuKien.setError("Điều kiện phải là số nguyên!");
            isValid = false;
        }

        if (startTimeInMillis == -1 || endTimeInMillis == -1) {
            Toast.makeText(this, "Vui lòng chọn thời gian bắt đầu và kết thúc!", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        if (isValid) {
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
            String start = dateTimeFormat.format(new Date(startTimeInMillis));
            String end = dateTimeFormat.format(new Date(endTimeInMillis));

            String result = db.insertDataUuDai(userId, tenUuDai, start, end, giamGiaInt / 100.0, dieuKienInt);

            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            tenUD.setText("");
            giamGia.setText("");
            dieuKien.setText("");
            tvNgayBatDau.setText("");
            tvNgayHetHan.setText("");
            startTimeInMillis = -1;
            endTimeInMillis = -1;
        } else {
            Toast.makeText(this, "Lỗi: không thể tạo mã giảm giá!", Toast.LENGTH_SHORT).show();
        }
    }




    // Hàm kiểm tra token
    private void checkToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);

        if (userId == -1) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
