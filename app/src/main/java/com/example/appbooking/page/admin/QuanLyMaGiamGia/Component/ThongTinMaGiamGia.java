package com.example.appbooking.page.admin.QuanLyMaGiamGia.Component;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appbooking.Database.MySQLite;
import com.example.appbooking.Model.TaiKhoan;
import com.example.appbooking.Model.UuDai;
import com.example.appbooking.R;
import com.example.appbooking.page.admin.QuanLyMaGiamGia.Adapter.UuDaiAdapters;
import com.example.appbooking.page.admin.quanLyUser.Adapter.adAdpterUser;
import com.example.appbooking.page.admin.quanLyUser.quanLyUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ThongTinMaGiamGia extends AppCompatActivity {

    MySQLite db ;
    ImageButton imgLoad;
    ImageView imgBack;
    Spinner spnMgg;
    ListView lvMgg;
    UuDaiAdapters UuDaiAdapter;
    ArrayList<UuDai> listMaUD;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thong_tin_ma_giam_gia);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ view
        imgLoad = findViewById(R.id.imgLoad);
        spnMgg = findViewById(R.id.spnMgg);
        lvMgg = findViewById(R.id.lvMagg);
        imgBack = findViewById(R.id.imgBack);

        // Khởi tạo sql
        db = new MySQLite();

        // khởi tạo arrlist
        listMaUD = new ArrayList<>();
        listMaUD = getall("SELECT * FROM UU_DAI");


        // khởi tạo adapter
        UuDaiAdapter = new UuDaiAdapters(this, R.layout.lv_magiamgia, listMaUD);
        lvMgg.setAdapter(UuDaiAdapter);


        // back
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // refresh
        imgLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResume();
            }
        });

        // Tạo danh sách spinner
        List<String> filler = new ArrayList<>();
        filler.add("Tất cả");
        filler.add("Hoạt Động");
        filler.add("Hết Hạn");

        // Tạo ArrayAdapter cho Spinner
        ArrayAdapter<String> filterAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filler);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMgg.setAdapter(filterAdapter);

        // Thiết lập sự kiện cho Spinner
        spnMgg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: // "Tất cả"
                        listMaUD.clear();
                        listMaUD.addAll(getall("SELECT * FROM UU_DAI"));
                        break;

                    case 1:
                        listMaUD.clear();
                        listMaUD.addAll(getall("SELECT * FROM UU_DAI WHERE ngay_het_han >= strftime('%Y/%m/%d %H:%M', datetime('now', '+7 hours'))"));
                        break;

                    case 2:
                        listMaUD.clear();
                        listMaUD.addAll(getall("SELECT * FROM UU_DAI WHERE ngay_het_han < strftime('%Y/%m/%d %H:%M', datetime('now', '+7 hours'))"));
                        break;
                }
                UuDaiAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì nếu không có gì được chọn
            }

        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    private void refreshData() {
        listMaUD.clear();
        listMaUD.addAll(getall("SELECT * FROM UU_DAI"));
        UuDaiAdapter.notifyDataSetChanged();
    }



    // lay du lieu
    ArrayList<UuDai> getall(String sql){
        ArrayList<UuDai> listMa = new ArrayList<>();
        try{
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
                    Date ngayBatDau = row.get(4) != null ? dateFormat.parse(row.get(4).toString()) : null;
                    uuDai.setNgayHetHan(ngayBatDau);
                } catch (ParseException e) {
                    uuDai.setNgayHetHan(null);
                }
                uuDai.setGiam(Double.parseDouble(row.get(5).toString()));
                uuDai.setDieuKienVeGia(Integer.parseInt(row.get(6).toString()));
                listMa.add(uuDai);
            }
            return listMa;
        }catch(Exception e){
            return null;
        }
    }


}