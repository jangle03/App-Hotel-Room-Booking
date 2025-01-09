package com.example.appbooking.page.admin.quanLyUser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appbooking.Database.MySQLite;
import com.example.appbooking.Model.TaiKhoan;
import com.example.appbooking.R;
import com.example.appbooking.page.admin.quanLyUser.Adapter.adAdpterUser;

import java.util.ArrayList;
import java.util.List;

import tech.turso.libsql.proto.Row;

public class quanLyUser extends AppCompatActivity {

    MySQLite db1;
    ImageButton ad_btnLoad, back;
    Spinner ad_dkLoc;
    ListView ad_listV;
    adAdpterUser taiKhoanAdapter;
    ArrayList<TaiKhoan> listTK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quan_ly_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Ánh xạ các thành phần giao diện
        ad_btnLoad = findViewById(R.id.ad_btnLoad);
        ad_dkLoc = findViewById(R.id.ad_dkLoc);
        ad_listV = findViewById(R.id.ad_listV);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Khởi tạo MySQLite
        db1 = new MySQLite();
//        db1.updateSQL("INSERT INTO TAI_KHOAN (username, password, name, email, sdt, cccd, address, role, hinh) " +
//                "VALUES ('huynh', '1', 'vu dinh huynh', 'huynh@gamil.com','0125646464' , '4839309535', 'haiDuong', 0, 'anh_user');");
//         Lấy dữ liệu ban đầu từ cơ sở dữ liệu
        listTK = new ArrayList<>();
//        Toast.makeText(this, listTK.size() + "", Toast.LENGTH_SHORT).show();
        // Khởi tạo adapter và gán vào ListView
        taiKhoanAdapter = new adAdpterUser(this, R.layout.ad_item_user, listTK);
        ad_listV.setAdapter(taiKhoanAdapter);

        // Tạo danh sách các lựa chọn cho Spinner
        List<String> filterOptions = new ArrayList<>();
        filterOptions.add("All");
        filterOptions.add("User");
        filterOptions.add("Admin");

        // Tạo ArrayAdapter cho Spinner
        ArrayAdapter<String> filterAdapter = new ArrayAdapter<>(quanLyUser.this, android.R.layout.simple_spinner_item, filterOptions);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ad_dkLoc.setAdapter(filterAdapter);

        // Thiết lập sự kiện cho Spinner
        ad_dkLoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedItem = parentView.getItemAtPosition(position).toString();
                // Lọc dữ liệu và cập nhật ListView
                ArrayList<TaiKhoan> filteredData = filterData(selectedItem);
                taiKhoanAdapter.updateData(filteredData);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Không làm gì nếu không chọn
            }
        });
        ad_btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad_dkLoc.setSelection(0); // Đặt Spinner về giá trị "All"
            }
        });
    }

    ArrayList<TaiKhoan> filterData(String filter) {
        ArrayList<TaiKhoan> filteredList = new ArrayList<>();
        String query = "SELECT * FROM TAI_KHOAN;";  // Default query for "All"

        if ("User".equals(filter)) {
            query = "SELECT * FROM TAI_KHOAN WHERE role = 1;";  // Only users
        } else if ("Admin".equals(filter)) {
            query = "SELECT * FROM TAI_KHOAN WHERE role = 0;";  // Only admins
        }

        try{
            filteredList = getall(query);
        }catch(Exception e){
            e.printStackTrace();
        }
        // Fetch the filtered list based on role

//        Toast.makeText(this, filter + " data: " + filteredList.size(), Toast.LENGTH_SHORT).show();

        return filteredList;
    }

    ArrayList<TaiKhoan> getall(String sql) {
        ArrayList<TaiKhoan> t = new ArrayList<>();
        try {
            List<List<Object>> list = db1.executeQuery(sql); // Execute SQL query
            for (List<Object> row : list) {
                TaiKhoan tk1 = new TaiKhoan(
                        Integer.parseInt(row.get(0).toString()),
                        Integer.parseInt(row.get(8).toString()),
                        row.get(1).toString(),
                        row.get(2).toString(),
                        row.get(4).toString(),
                        row.get(7).toString(),
                        row.get(5).toString(),
                        row.get(6).toString(),
                        row.get(3).toString(),
                        row.get(9).toString()
                );
                t.add(tk1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Kiểm tra nếu dữ liệu đã thay đổi
            if (data != null && data.getBooleanExtra("isUpdated", false)) {
                listTK = getall("SELECT * FROM TAI_KHOAN;"); // Lấy lại dữ liệu mới từ SQL
                taiKhoanAdapter.updateData(listTK); // Cập nhật lại ListView
            }
        }
    }
}