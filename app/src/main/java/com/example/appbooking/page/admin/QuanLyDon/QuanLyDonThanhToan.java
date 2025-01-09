package com.example.appbooking.page.admin.QuanLyDon;

import androidx.appcompat.widget.SearchView;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appbooking.Database.MySQLite;
import com.example.appbooking.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuanLyDonThanhToan extends AppCompatActivity {

    MySQLite db;
    ListView lvDanhSachPhong;
    PhongAdapter phongAdapter;
    Spinner spinnerFilter;
    ImageButton btnReload;
    ImageView btnBack;
    ImageView btnSearchExpand;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_don_thanh_toan);

        // Initialize views
        btnReload = findViewById(R.id.btnReload);
        spinnerFilter = findViewById(R.id.ad_dkLoc);
        lvDanhSachPhong = findViewById(R.id.ad_listDanhSachThue);
        btnBack = findViewById(R.id.arrow_back_ios);
        btnSearchExpand = findViewById(R.id.btnSearchExpand);
        searchView = findViewById(R.id.ad_searchView);

        // Initialize database
        db = new MySQLite();

        // Initialize adapter with an empty list
        phongAdapter = new PhongAdapter(this, R.layout.ad_item_admin_quan_ly_don, new ArrayList<>());
        lvDanhSachPhong.setAdapter(phongAdapter);

        // Setup spinner for filtering
        setupFilterSpinner();

        // Reload button event
        btnReload.setOnClickListener(v -> reloadData());

        // Back button event
        btnBack.setOnClickListener(v -> finish());

        // Expand search on icon click
        btnSearchExpand.setOnClickListener(v -> {
            if (searchView.getVisibility() == View.GONE) {
                searchView.setVisibility(View.VISIBLE);  // Show SearchView
                searchView.requestFocus();  // Focus on the SearchView to type
            }
        });

        // Handle query text change in SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterRooms(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterRooms(newText);
                return false;
            }
        });
    }

    private void setupFilterSpinner() {
        // Options for the spinner
        List<String> filterOptions = new ArrayList<>();
        filterOptions.add("Đang Thuê");
        filterOptions.add("Standard");
        filterOptions.add("Superior");
        filterOptions.add("Deluxe");
        filterOptions.add("Suite");
        filterOptions.add("Đã Thuê");

        // Set up adapter for spinner
        ArrayAdapter<String> filterAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filterOptions);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(filterAdapter);

        // Handle spinner item selection
        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedItem = parentView.getItemAtPosition(position).toString();

                ArrayList<HashMap<String, Object>> filteredList;
                if ("Đang Thuê".equals(selectedItem)) {
                    filteredList = db.layDuLieuPhongCoNguoiDat();
                }else if("Đã Thuê".equals(selectedItem)){
                    filteredList = db.layDuLieuPhongDaThanhToan();
                } else {
                    int roomTypeCode = getRoomTypeCode(selectedItem);
                    filteredList = db.layDuLieuPhongCoNguoiDatTuMaPhong(roomTypeCode);
                }
                updateRoomList(filteredList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });
    }

    private void updateRoomList(ArrayList<HashMap<String, Object>> filteredList) {
        if (filteredList == null || filteredList.isEmpty()) {
            Toast.makeText(this, "Không có phòng nào phù hợp!", Toast.LENGTH_SHORT).show();
        } else {
            try {
                ArrayList<HashMap<String, String>> stringList = convertToStringMaps(filteredList);
                phongAdapter.updateData(stringList);
                Toast.makeText(this, "Đã cập nhật danh sách phòng!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Có lỗi xảy ra khi cập nhật dữ liệu", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    private void reloadData() {
        if (spinnerFilter.getAdapter() == null || spinnerFilter.getAdapter().getCount() == 0) {
            Toast.makeText(this, "Không có tùy chọn nào trong Spinner!", Toast.LENGTH_SHORT).show();
            return;
        }

        spinnerFilter.setSelection(0);

        ArrayList<HashMap<String, Object>> allRooms = db.layDuLieuPhongCoNguoiDat();
        updateRoomList(allRooms);

        Toast.makeText(this, "Đã load lại danh sách phòng", Toast.LENGTH_SHORT).show();
    }

    private void filterRooms(String query) {
        String selectedFilter = spinnerFilter.getSelectedItem().toString();

        ArrayList<HashMap<String, Object>> filteredList = new ArrayList<>();
        if ("Đang Thuê".equals(selectedFilter)) {
            filteredList = db.layDuLieuPhongCoNguoiDat();
        } else if("Đã Thuê".equals(selectedFilter)){
            filteredList = db.layDuLieuPhongDaThanhToan();
        }
        else {
            int roomTypeCode = getRoomTypeCode(selectedFilter);
            filteredList = db.layDuLieuPhongCoNguoiDatTuMaPhong(roomTypeCode);
        }

        ArrayList<HashMap<String, Object>> resultList = new ArrayList<>();
        for (HashMap<String, Object> room : filteredList) {
            Object viTri = room.get("vi_tri");
            if (viTri != null && viTri.toString().toLowerCase().contains(query.toLowerCase())) {
                resultList.add(room);
            }
        }

        updateRoomList(resultList);
    }

    private int getRoomTypeCode(String roomTypeName) {
        switch (roomTypeName) {
            case "Standard":
                return 1;
            case "Superior":
                return 2;
            case "Deluxe":
                return 3;
            case "Suite":
                return 4;
            default:
                return -1;
        }
    }

    private ArrayList<HashMap<String, String>> convertToStringMaps(ArrayList<HashMap<String, Object>> originalList) {
        ArrayList<HashMap<String, String>> convertedList = new ArrayList<>();

        for (HashMap<String, Object> originalMap : originalList) {
            HashMap<String, String> stringMap = new HashMap<>();
            for (String key : originalMap.keySet()) {
                Object value = originalMap.get(key);
                if (value != null) {
                    stringMap.put(key, String.valueOf(value));
                } else {
                    stringMap.put(key, "");
                }
            }
            convertedList.add(stringMap);
        }
        return convertedList;
    }
}
