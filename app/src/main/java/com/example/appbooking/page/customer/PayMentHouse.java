package com.example.appbooking.page.customer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appbooking.Database.MySQLite;
import com.example.appbooking.MainActivity;
import com.example.appbooking.Model.TaiKhoan;
import com.example.appbooking.Model.UuDai;
import com.example.appbooking.R;
import com.example.appbooking.page.DashboardActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PayMentHouse extends AppCompatActivity {
    MySQLite db;
    TextView tvCustomerName,tvCustomerSdt, tvCustomerCCCD, tvCustomerContact, tvSubtotal, tvDiscount, tvTotal, roomContainer;
    Button btnEditCustomerInfo, btnPay;
    Spinner spnMgg;
    int userId;
    ArrayList<UuDai> listMaUD;
    ArrayAdapter<String> discountAdapter;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    double total = 0;

    int maUuDai;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkToken();
        setContentView(R.layout.activity_payment);

        // Bind views
        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvCustomerCCCD = findViewById(R.id.tvCustomerCCCD);
        tvCustomerSdt = findViewById(R.id.tvCustomerSdt);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvTotal = findViewById(R.id.tvTotal);
        roomContainer = findViewById(R.id.roomContainer);
//        btnEditCustomerInfo = findViewById(R.id.btnEditCustomerInfo);
        btnPay = findViewById(R.id.btnPay);
        spnMgg = findViewById(R.id.spnMgg);

        // Get data from Intent
        Intent intent = getIntent();
        db = new MySQLite();
        TaiKhoan taiKhoan = db.getTaiKhoan(userId);

        String tenKhachHang = taiKhoan.getName() ;
        String cccd = taiKhoan.getCccd();
        String sdt = taiKhoan.getSdt();
        String tenPhong = intent.getStringExtra("tenPhong");
        String giaPhong = intent.getStringExtra("giaPhong");
        String maPhong = intent.getStringExtra("maPhong");
        String timeCheckIn = intent.getStringExtra("timeCheckIn");
        String timeCheckOut = intent.getStringExtra("timeCheckOut");

        // Display customer and room information
        tvCustomerName.setText("Tên khách hàng: " + (tenKhachHang != null ? tenKhachHang : "Không rõ"));
        tvCustomerCCCD.setText("CCCD: "+  cccd);
        roomContainer.setText(tenPhong + "\n\n" + "Nhận phòng: " + timeCheckIn + "\n\n" + "Trả phòng: " + timeCheckOut + "\n\n" + giaPhong);
        tvCustomerSdt.setText("Số điện thoại: " + sdt);

        // Fetch active discount data
        listMaUD = new ArrayList<>();
        listMaUD = getall("SELECT * FROM UU_DAI WHERE ngay_het_han >= strftime('%Y/%m/%d %H:%M', datetime('now', '+7 hours')) AND dieu_kien_ve_gia <" + parsePriceString(giaPhong));

        // Populate Spinner with active discount codes
        List<String> discountNames = new ArrayList<>();
        for (UuDai discount : listMaUD) {
            discountNames.add(discount.getTenMa());
        }

        discountAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, discountNames);
        discountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMgg.setAdapter(discountAdapter);

        double subtotal = parsePriceString(giaPhong);
        tvDiscount.setText("không có mã giảm%");
        total = subtotal; // Tính giảm giá dựa trên phần trăm
        tvTotal.setText("Tổng thanh toán: " + total + " VND");
        spnMgg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!listMaUD.isEmpty()) {
                    UuDai selectedDiscount = listMaUD.get(position);
                        tvDiscount.setText("Giảm giá: " + selectedDiscount.getGiam() + "%");
                        double subtotal = parsePriceString(giaPhong);
                        double discount = selectedDiscount.getGiam();
                        maUuDai = selectedDiscount.getMaUuDai();
                        total = subtotal - (subtotal * discount / 100); // Tính giảm giá dựa trên phần trăm
                        tvTotal.setText("Tổng thanh toán: " + total + " VND");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tvDiscount.setText("Giảm giá: 0 VND");
            }
        });

//        btnEditCustomerInfo.setOnClickListener(view -> {
//            Intent editIntent = new Intent(PayMentHouse.this, EditCustomerInfoActivity.class);
//            editIntent.putExtra("customerName", tenKhachHang);
//            editIntent.putExtra("customerContact", cccd);
//            startActivityForResult(editIntent, 1);
//        });

        btnPay.setOnClickListener(view -> {
            Toast.makeText(PayMentHouse.this, "Đang xử lý thanh toán...", Toast.LENGTH_SHORT).show();
            showPaymentConfirmationDialog(userId, timeCheckIn, tenKhachHang, maPhong, timeCheckOut, total);
        });

    }

    private void checkToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);

        if (userId == -1) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String updatedName = data.getStringExtra("customerName");
            String updatedContact = data.getStringExtra("customerContact");
            tvCustomerName.setText("Tên khách hàng: " + updatedName);
            tvCustomerContact.setText("CCCD: " + updatedContact);
        }
    }

    private double parsePriceString(String priceString) {
        // Loại bỏ tất cả các ký tự không phải số
        String numericString = priceString.replaceAll("[^0-9]", "");
        try {
            // Chuyển đổi chuỗi số thành double
            return Double.parseDouble(numericString);
        } catch (NumberFormatException e) {
            // Nếu không thể chuyển đổi, trả về 0.0
            return 0.0;
        }
    }

    private void showPaymentConfirmationDialog(int maUser, String checkIn, String tenKhachHang, String maPhong, String checkOut, double totalAmount) {
        if (checkIn == null || checkOut == null || tenKhachHang == null || maPhong == null || totalAmount <= 0) {
            Toast.makeText(this, "Thông tin thanh toán không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Xác nhận thanh toán")
                .setMessage("Bạn chắc chắn muốn thanh toán không?\n\n" +
                        "Tổng thanh toán: " + totalAmount + " VND")
                .setPositiveButton("Đồng ý", (dialog, which) -> {
                    Toast.makeText(PayMentHouse.this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
                    showPaymentDetails(maUser, checkIn, tenKhachHang, maPhong, checkOut, totalAmount, maUuDai);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private String getCurrentTimeFormatted() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return sdf.format(Calendar.getInstance().getTime());
    }

    private void showPaymentDetails(int maUser, String checkIn, String tenKhachHang, String maPhong, String checkOut, double totalAmount, int maUuDai) {
        String message =
                "Khách hàng: " + tenKhachHang + "\n" +
                "Phòng: " + maPhong + "\n" +
                "Check-in: " + checkIn + "\n" +
                "Check-out: " + checkOut + "\n" +
                "Tổng tiền: " + totalAmount + " VND\n\n" +
                "Xin hãy chuyển khoản tới tài khoản: xxxxxx VietComBank.\n" +
                "Chúng tôi sẽ liên hệ với bạn sớm. Xin hãy giữ liên lạc qua điện thoại.";

        new AlertDialog.Builder(this)
                .setTitle("Cảm ơn bạn đã thanh toán")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    // After the user closes the dialog, proceed with the database operations
                    processPayment(maUser, checkIn, maPhong, checkOut, totalAmount, maUuDai);
                })
                .show();
    }

    private void processPayment(int maUser, String checkIn, String maPhong, String checkOut, double totalAmount, int MaUuDai) {
        String currentTime = getCurrentTimeFormatted();

        try {
            // Insert the payment into the database
            Integer result = Integer.valueOf(db.insertDataDon(maUser, currentTime, checkIn));
            int maDon = 0;

            // Validate the order ID (maDon)
            try {
                maDon = Integer.parseInt(String.valueOf(result));
            } catch (NumberFormatException e) {
                showErrorToast("Lỗi: Mã đơn không hợp lệ!");
                return;
            }

            // Convert room number (maPhong) to integer
            int maPhongInt = 0;
            try {
                maPhongInt = Integer.parseInt(maPhong);
            } catch (NumberFormatException e) {
                showErrorToast("Lỗi: Mã phòng không hợp lệ!");
                return;
            }
            String ApMa = db.insertApMa(maDon, maUuDai);
            if (!"Thêm thành công".equals(ApMa)) {
                showErrorToast("Lỗi khi thêm dữ liệu vào bảng ApMa!");
                return;
            }
            // Insert the room rental information into the database
            String insertResult = db.insertDataThue(maDon, maPhongInt, checkOut);
            if (!"Thêm thành công".equals(insertResult)) {
                showErrorToast("Lỗi khi thêm dữ liệu vào bảng THUE!");
                return;
            }

            String inserstDtaHOaDon = db.insertDataHoaDon_BanDau(maDon, checkOut, totalAmount);
            if (!"Thêm thành công".equals(inserstDtaHOaDon)) {
                showErrorToast("Lỗi khi thêm dữ liệu vào bảng HOA DON!");
                return;
            }

            // Show a success message after successful payment processing
            Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();

            // Navigate to the DashboardActivity
            Intent intent = new Intent(PayMentHouse.this, DashboardActivity.class);
            startActivity(intent);

        } catch (Exception e) {
            showErrorToast("Lỗi: " + e.getMessage());
        }
    }


    private void showErrorToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    ArrayList<UuDai> getall(String sql) {
        ArrayList<UuDai> listMa = new ArrayList<>();
        try {
            List<List<Object>> list = db.executeQuery(sql);
            for (List<Object> row : list) {
                UuDai uuDai = new UuDai();
                uuDai.setMaNhanVien(Integer.parseInt(row.get(0).toString()));
                uuDai.setMaUuDai(Integer.parseInt(row.get(1).toString()));
                uuDai.setTenMa(row.get(2).toString());
                try {
                    Date ngayBatDau = row.get(3) != null ? dateFormat.parse(row.get(3).toString()) : null;
                    uuDai.setNgayBatDau(ngayBatDau);
                } catch (ParseException e) {
                    uuDai.setNgayBatDau(null);
                }
                try {
                    Date ngayHetHan = row.get(4) != null ? dateFormat.parse(row.get(4).toString()) : null;
                    uuDai.setNgayHetHan(ngayHetHan);
                } catch (ParseException e) {
                    uuDai.setNgayHetHan(null);
                }
                uuDai.setGiam(Double.parseDouble(row.get(5).toString()));
                uuDai.setDieuKienVeGia(Integer.parseInt(row.get(6).toString()));
                listMa.add(uuDai);
            }
            return listMa;
        } catch (Exception e) {
            return null;
        }
    }
}
