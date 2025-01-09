package com.example.appbooking.page.admin.adRoom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appbooking.Database.MySQLite;
import com.example.appbooking.Model.ChiTietLoaiPhong;
import com.example.appbooking.Model.LoaiPhong;
import com.example.appbooking.Model.Phong;
import com.example.appbooking.R;
import com.example.appbooking.page.admin.adRoom.Adapter.adApdaterPhong;
import com.example.appbooking.page.admin.adRoom.Adapter.detalPhong;
import com.example.appbooking.page.admin.adRoom.Component.AddRoom;
import com.example.appbooking.page.admin.adRoom.Component.addRoomType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class quanLyPhong extends AppCompatActivity {
    // Spinner
    Spinner ad_dkl1;
    Spinner ad_dkl2;
    ArrayList<detalPhong> ldPhong;
    ArrayAdapter<String> adapter;
    // ImageButton
    ImageButton ad_btnLoad, back;
    adApdaterPhong adapP;
    // ListView
    ListView ad_listViewRT;
    MySQLite db1 = new MySQLite();
    // Button
    Button ad_themP;
    Button ad_themLP;
    ArrayList<String> items;
    HashMap<String, ArrayList<String>> subCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quan_ly_phong);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Ánh xạ các View
        ad_dkl1 = findViewById(R.id.ad_dkl1);
        ad_dkl2 = findViewById(R.id.ad_dkl2);
        ad_btnLoad = findViewById(R.id.ad_btnLoad);
        ad_listViewRT = findViewById(R.id.ad_listViewRT);
        ad_themP = findViewById(R.id.ad_themP);
        ad_themLP = findViewById(R.id.ad_themLP);
        ldPhong = new ArrayList<>();
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ad_themLP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in1 = new Intent(quanLyPhong.this, addRoomType.class);
                startActivity(in1);
            }
        });
        ad_themP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in2 = new Intent(quanLyPhong.this, AddRoom.class);
                in2.putExtra("data", new detalPhong());
                startActivity(in2);
            }
        });
        items = new ArrayList<>();
        items.add("Chưa có bộ lọc");
        items.add("lọc theo loại phòng");
        items.add("lọc theo tên");
        subCategories = new HashMap<>();
        subCategories.put("Chưa có bộ lọc", new ArrayList<String>() {{
            add("Chưa có bộ lọc");
        }});
        subCategories.put("lọc theo loại phòng", getAllLP("select ten from LOAI_PHONG"));
        subCategories.put("lọc theo tên", new ArrayList<String>() {{
            add("A->");
            add("Z->");
        }});
        // Tạo adapter cho Spinner
        adapter = new ArrayAdapter<>(quanLyPhong.this, android.R.layout.simple_spinner_item,items);
        // Thiết lập layout khi thả xuống
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Gắn adapter vào Spinner
        ad_dkl1.setAdapter(adapter);
        // Xử lý sự kiện khi chọn mục
        // Xử lý khi chọn mục trong Spinner chính
        ad_dkl1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedMainCategory = items.get(position);
                // Lấy dữ liệu tương ứng cho Spinner phụ
                ArrayList<String> subList = subCategories.get(selectedMainCategory);
                // Tạo adapter cho Spinner phụ
                ArrayAdapter<String> subAdapter = new ArrayAdapter<>(quanLyPhong.this, android.R.layout.simple_spinner_item, subList);
                subAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ad_dkl2.setAdapter(subAdapter);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì nếu không có mục nào được chọn
            }
        });
        adapP = new adApdaterPhong(quanLyPhong.this, R.layout.ad_item_phong, ldPhong);
        ad_listViewRT.setAdapter(adapP);
        registerForContextMenu(ad_listViewRT);
        // Xử lý khi chọn mục trong Spinner phụ
        ad_dkl2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedMainCategory = ad_dkl1.getSelectedItem().toString();
                String selectedSubCategory = parent.getItemAtPosition(position).toString();
                String query = "";
                // Xây dựng câu truy vấn dựa trên lựa chọn của người dùng
                if (selectedMainCategory.equals("Chưa có bộ lọc")) {
                    query = "SELECT p.ma_phong, p.vi_tri, lp.ma_loai_phong, lp.ten, lp.gia, lp.so_nguoi_toi_da, lp.mo_ta, ct.id, ct.hinh , lp.mo_ta_chi_tiet " +
                            "FROM PHONG p " +
                            "JOIN LOAI_PHONG lp ON p.ma_loai_phong = lp.ma_loai_phong " +
                            "JOIN CHI_TIET_LOAI_PHONG ct ON lp.ma_loai_phong = ct.ma_loai_phong " +
                            "GROUP BY p.ma_phong;";
                } else if (selectedMainCategory.equals("lọc theo loại phòng")) {
                    query = "SELECT p.ma_phong, p.vi_tri, lp.ma_loai_phong, lp.ten, lp.gia, lp.so_nguoi_toi_da, lp.mo_ta, ct.id, ct.hinh , lp.mo_ta_chi_tiet " +
                            "FROM PHONG p " +
                            "JOIN LOAI_PHONG lp ON p.ma_loai_phong = lp.ma_loai_phong " +
                            "JOIN CHI_TIET_LOAI_PHONG ct ON lp.ma_loai_phong = ct.ma_loai_phong " +
                            "WHERE lp.ten = '" + selectedSubCategory + "' " +
                            "GROUP BY p.ma_phong;";
                } else if (selectedMainCategory.equals("lọc theo tên")) {
                    if (selectedSubCategory.equals("A->")) {
                        query = "SELECT p.ma_phong, p.vi_tri, lp.ma_loai_phong, lp.ten, lp.gia, lp.so_nguoi_toi_da, lp.mo_ta, ct.id, ct.hinh, lp.mo_ta_chi_tiet " +
                                "FROM PHONG p " +
                                "JOIN LOAI_PHONG lp ON p.ma_loai_phong = lp.ma_loai_phong " +
                                "JOIN CHI_TIET_LOAI_PHONG ct ON lp.ma_loai_phong = ct.ma_loai_phong " +
                                "ORDER BY lp.ten ASC;";
                    } else if (selectedSubCategory.equals("Z->")) {
                        query = "SELECT p.ma_phong, p.vi_tri, lp.ma_loai_phong, lp.ten, lp.gia, lp.so_nguoi_toi_da, lp.mo_ta, ct.id, ct.hinh, lp.mo_ta_chi_tiet " +
                                "FROM PHONG p " +
                                "JOIN LOAI_PHONG lp ON p.ma_loai_phong = lp.ma_loai_phong " +
                                "JOIN CHI_TIET_LOAI_PHONG ct ON lp.ma_loai_phong = ct.ma_loai_phong " +
                                "ORDER BY lp.ten DESC;";
                    }
                }
                // Thực hiện truy vấn và cập nhật dữ liệu
                ArrayList<detalPhong> data = getAllJ3(query);
                adapP.updateData(data);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì nếu không có mục nào được chọn
            }
        });
        ad_btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad_dkl1.setSelection(0);
            }
        });
    }
    public ArrayList<detalPhong> getAllJ3(String sql) {
        ArrayList<detalPhong> d = new ArrayList<>();
        try {
            List<List<Object>> row = db1.executeQuery(sql);
            for (List<Object> rows : row) {
                Phong p = new Phong(Integer.parseInt(rows.get(0).toString()), rows.get(1).toString(), Integer.parseInt(rows.get(2).toString())); // ma_phong, vi_tri, ma_loai_phong
                LoaiPhong lp = new LoaiPhong(Integer.parseInt(rows.get(2).toString()), rows.get(3).toString(), Integer.parseInt(rows.get(4).toString()), Integer.parseInt(rows.get(5).toString()), rows.get(6).toString(), rows.get(9).toString()); // ma_loai_phong, ten, gia, so_nguoi_toi_da, mo_ta
                ChiTietLoaiPhong ct = new ChiTietLoaiPhong(Integer.parseInt(rows.get(7).toString()), Integer.parseInt(rows.get(2).toString()), rows.get(8).toString()); // id, ma_loai_phong, hinh
                detalPhong dp = new detalPhong(p, lp, ct);
                d.add(dp);
            }
            return d;
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi nếu có
            return new ArrayList<>(); // Trả về danh sách rỗng nếu có lỗi
        }
    }
    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(contextMenu, v, menuInfo);
        try {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.ad_propup_delete, contextMenu); // Sử dụng file XML prop_up_menu
            contextMenu.setHeaderTitle("Thao tác");
        } catch (Exception e) {
            Log.e("ContextMenu", "Error inflating menu: " + e.getMessage());
        }
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        // Lấy thông tin vị trí item được chọn
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position; // Vị trí item trong ListView
        detalPhong selectedRoomType = ldPhong.get(position); // Lấy đối tượng LoaiPhong tại vị trí này
        // Kiểm tra mục được chọn
        if (item.getItemId() == R.id.delete) {
            // Xử lý xóa
            confirmAndDeleteRoomType(selectedRoomType, position);
            return true;
        }
        return super.onContextItemSelected(item);
    }
    private void confirmAndDeleteRoomType(detalPhong roomType, int position) {
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(quanLyPhong.this);
        dialogDelete.setTitle("Xóa");
        dialogDelete.setMessage("Bạn có chắc chắn muốn xóa phòng này?");
        dialogDelete.setPositiveButton("Xóa", (dialog, which) -> {
            // Xóa khỏi danh sách và cập nhật ListView
            try{
                db1.updateSQL("DELETE FROM PHONG WHERE ma_phong = "+ldPhong.get(position).getPhong().getMaPhong()+";");
                db1.updateSQL("DELETE FROM CHI_TIET_LOAI_PHONG WHERE id = "+ ldPhong.get(position).getChiTietLoaiPhong().getId() + ";");
                ldPhong.remove(position);
                adapP.notifyDataSetChanged();
                Toast.makeText(quanLyPhong.this, "Phòng đã được xóa", Toast.LENGTH_SHORT).show();
            }catch(Exception e){
                Toast.makeText(quanLyPhong.this, "Lỗi khi xóa hãy liên hệ với giám đốc sản xuất vũ đình huynh để sửa lỗi", Toast.LENGTH_SHORT).show();
            }
        });
        dialogDelete.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());
        dialogDelete.show();
    }
    public ArrayList<String> getAllLP(String sql) {
        ArrayList<String> d = new ArrayList<>();
        try {
            List<List<Object>> row = db1.executeQuery(sql);
            for (List<Object> rows : row) {
                d.add(rows.get(0).toString());
            }
            return d;
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi nếu có
            return new ArrayList<>(); // Trả về danh sách rỗng nếu có lỗi
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Làm mới dữ liệu ở đây
        String defaultQuery = "SELECT p.ma_phong, p.vi_tri, lp.ma_loai_phong, lp.ten, lp.gia, lp.so_nguoi_toi_da, lp.mo_ta, ct.id, ct.hinh, lp.mo_ta_chi_tiet " +
                "FROM PHONG p " +
                "JOIN LOAI_PHONG lp ON p.ma_loai_phong = lp.ma_loai_phong " +
                "JOIN CHI_TIET_LOAI_PHONG ct ON lp.ma_loai_phong = ct.ma_loai_phong " +
                "GROUP BY p.ma_phong;";
        ArrayList<detalPhong> data = getAllJ3(defaultQuery);
        adapP.updateData(data); // Cập nhật dữ liệu của adapter
    }
}