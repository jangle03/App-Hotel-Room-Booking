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
import java.util.List;
import java.util.Locale;

public class SuaMaGiamGia extends AppCompatActivity {

    private Button btnStartPickDateTimeEdit, btnPickEndDateTimeEdit, btnSuaMa;
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
        setContentView(R.layout.activity_sua_ma_giam_gia);
        getData(getIntent().getIntExtra("id", -1));
        setupListeners();

    }

    // nhan du lieu
    private void getData(int maGiamGia) {
        imgBack = findViewById(R.id.imgBackSua);
        btnStartPickDateTimeEdit = findViewById(R.id.bntSuaThoiGianBatDau);
        btnPickEndDateTimeEdit = findViewById(R.id.bntSuaThoiGianKetThuc);
        btnSuaMa = findViewById(R.id.btnSuaCapNhatMa);
        tenUD = findViewById(R.id.edtSuaTenMaUuDai);
        giamGia = findViewById(R.id.edtSuaGiamGia);
        dieuKien = findViewById(R.id.edtSuaDieuKien);
        tvNgayBatDau = findViewById(R.id.tvSuaNgayBatDau);
        tvNgayHetHan = findViewById(R.id.tvSuaNgayHetHan);



        try {
            db = new MySQLite();
            List<List<Object>> list = db.executeQuery("SELECT * FROM UU_DAI WHERE ma_uu_dai = " + maGiamGia + ";");
            if (list.isEmpty()) {
                Toast.makeText(this, "Lỗi: không thể lấy dữ liệu !", Toast.LENGTH_SHORT).show();
            } else {

                List<Object> data = list.get(0);
                String tenUuDai = data.get(2).toString();
                String ngayBatDau = data.get(3).toString();
                String ngayHetHan = data.get(4).toString();
                double giamGiaValue = (double) data.get(5);
                long dieuKienValue = (long) data.get(6);

                // Hiển thị dữ liệu lên giao diện
                tenUD.setText(tenUuDai);
                dieuKien.setText(String.valueOf(dieuKienValue));
                tvNgayBatDau.setText(ngayBatDau);
                tvNgayHetHan.setText(ngayHetHan);
                giamGia.setText(String.valueOf((int) (giamGiaValue * 100)));
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi: không thể lấy dữ liệu !", Toast.LENGTH_SHORT).show();
            finish();
        }

    }



    // Thiết lập sự kiện cho các nút
    private void setupListeners() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnStartPickDateTimeEdit.setOnClickListener(v -> pickDateTime(true));
        btnPickEndDateTimeEdit.setOnClickListener(v -> pickDateTime(false));
        btnSuaMa.setOnClickListener(v -> handleEditDiscountCode());
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

    // Xử lý logic sửa mã giảm giá
    private void handleEditDiscountCode() {
        boolean isValid = true;
        String tenUuDai = tenUD.getText().toString().trim();
        String giamGiaValue = giamGia.getText().toString().trim();
        String dieuKienValue = dieuKien.getText().toString().trim();

        // Kiểm tra tên mã ưu đãi
        if (tenUuDai.isEmpty()) {
            tenUD.setError("Vui lòng nhập tên mã ưu đãi!");
            isValid = false;
        }

        // Kiểm tra giá trị giảm giá
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

        // Kiểm tra điều kiện
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

        // Kiểm tra và gán giá trị ngày giờ
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
        try {
            if (startTimeInMillis == -1) {
                startTimeInMillis = dateTimeFormat.parse(tvNgayBatDau.getText().toString()).getTime();
            }
            if (endTimeInMillis == -1) {
                endTimeInMillis = dateTimeFormat.parse(tvNgayHetHan.getText().toString()).getTime();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi xử lý thời gian!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (endTimeInMillis <= startTimeInMillis) {
            Toast.makeText(this, "Thời gian kết thúc phải lớn hơn thời gian bắt đầu!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Nếu tất cả dữ liệu hợp lệ, cập nhật mã giảm giá
        if (isValid) {
            String start = dateTimeFormat.format(new Date(startTimeInMillis));
            String end = dateTimeFormat.format(new Date(endTimeInMillis));

            try {
                db.updateSQL("UPDATE UU_DAI SET ten_ma = '" + tenUuDai + "', ngay_bat_dau = '" + start + "', ngay_het_han = '" + end + "', giam = " + giamGiaInt / 100.0 + ", dieu_kien_ve_gia = " + dieuKienInt + " WHERE ma_uu_dai = " + getIntent().getIntExtra("id", -1));

                Toast.makeText(this, "Cập nhật mã giảm giá thành công!", Toast.LENGTH_SHORT).show();
                resetForm();
                finish();
            } catch (Exception e) {
                Toast.makeText(this, "Lỗi: Không thể cập nhật mã giảm giá. Vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Hàm xóa dữ liệu trên form sau khi hoàn thành
    private void resetForm() {
        tenUD.setText("");
        giamGia.setText("");
        dieuKien.setText("");
        tvNgayBatDau.setText("");
        tvNgayHetHan.setText("");
        startTimeInMillis = -1;
        endTimeInMillis = -1;
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