package com.example.appbooking.page.admin.adRoom.Component;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.style.AlignmentSpan;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appbooking.Database.MySQLite;
import com.example.appbooking.R;
import com.example.appbooking.page.admin.adRoom.Adapter.detalPhong;

import java.util.ArrayList;
import java.util.List;

public class AddRoom extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;  // Mã yêu cầu để nhận kết quả từ Intent
    ImageView imgRoom;
    EditText edtViTriPhong;
    Spinner spLoaiPhong;
    Button btnLuuPhong;
    MySQLite db1 = new MySQLite();
    ArrayAdapter<String> adap;
    ArrayList<String> items;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_room);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Ánh xạ các view từ layout
        imgRoom = findViewById(R.id.ad_r_img);
        edtViTriPhong = findViewById(R.id.ad_r_viTri);
        spLoaiPhong = findViewById(R.id.ad_r_sp);
        btnLuuPhong = findViewById(R.id.ad_r_btn);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent in = new Intent();
        detalPhong d = (detalPhong) getIntent().getSerializableExtra("data");
        // Thêm sự kiện nhấn vào ImageView để chọn ảnh
        imgRoom.setOnClickListener(v -> openGallery());
        items = getAllLP("select ten from LOAI_PHONG");
        adap = new ArrayAdapter<>(AddRoom.this, android.R.layout.simple_spinner_item,items);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLoaiPhong.setAdapter(adap);
        if (d != null && d.getPhong() != null && d.getLoaiPhong() != null && d.getChiTietLoaiPhong() != null) {
            try{
                if (d.chiTietLoaiPhong.getHinh().contains("/")) {
                    imgRoom.setImageURI(Uri.parse(d.chiTietLoaiPhong.getHinh()));
                } else {
                    imgRoom.setImageURI(Uri.parse(db1.getDrawableResourceUrl(AddRoom.this, d.chiTietLoaiPhong.getHinh())));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            edtViTriPhong.setText(d.getPhong().getViTri().toString().trim());
            String tenLoaiPhong = d.getLoaiPhong().getTen().toString().trim();
            int index = items.indexOf(tenLoaiPhong);
            if (index != -1) {
                spLoaiPhong.setSelection(index);
            } else {
                Toast.makeText(this, "Không tìm thấy loại phòng: " + tenLoaiPhong, Toast.LENGTH_SHORT).show();
            }
        }
        btnLuuPhong.setOnClickListener(v -> {
            String viTriPhong = edtViTriPhong.getText().toString().trim();
            String loaiPhong = spLoaiPhong.getSelectedItem().toString();
            String imageUri;
            if (imgRoom.getDrawable() == null || imgRoom.getTag() == null) {
                imageUri = "ad_anh_clone_phong"; // Ảnh mặc định
            } else {
                imageUri = imgRoom.getTag().toString();
            }
            valiateAndpush(imageUri, viTriPhong, loaiPhong, d);  // Gọi hàm lưu thông tin
            finish();  // Đóng activity sau khi lưu thành công
        });
    }

    // Mở thư viện ảnh
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();  // Lấy URI của ảnh được chọn
            if (imageUri != null) {
                imgRoom.setImageURI(imageUri);  // Hiển thị ảnh trong ImageView
                imgRoom.setTag(imageUri.toString());  // Lưu URI vào tag của ImageView để dùng sau
            }
        }
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
    public void valiateAndpush(String uri, String vtri, String lp, detalPhong p){
        if(uri.toString().isEmpty()){
            uri = p.chiTietLoaiPhong.getHinh();
        }
        if(vtri.toString().isEmpty()){
            Toast.makeText(this, "không để phần vị trí bị chống", Toast.LENGTH_SHORT).show();
            return;
        }
        int id = getID("Select ma_loai_phong from LOAI_PHONG where ten = '" +lp+"';");

        if(p != null && p.getPhong() != null && p.getLoaiPhong() != null && p.getChiTietLoaiPhong() != null){
            try{
                db1.updateSQL("UPDATE PHONG SET vi_tri = '"+vtri.toString().trim()+"', ma_loai_phong = "+ id +" WHERE ma_phong = "+p.getPhong().getMaPhong()+";");
                db1.updateSQL("UPDATE CHI_TIET_LOAI_PHONG SET hinh = '"+uri+"', ma_loai_phong = "+ id +" WHERE id = "+p.getChiTietLoaiPhong().getId()+";");
                Toast.makeText(this, "update thành công", Toast.LENGTH_SHORT).show();
            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(this, "Lỗi khi update", Toast.LENGTH_SHORT).show();
            }
        }else{
            try{
                db1.insertDataChiTietLoaiPhong(id, uri);
                Toast.makeText(AddRoom.this, "Lưu thông tin thành công!", Toast.LENGTH_SHORT).show();
            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(this, "Lỗi không thể thêm chi tiết loại phòng", Toast.LENGTH_SHORT).show();
            }
            try {
                db1.insertDataPhong(vtri.toString().trim(), id);
                Toast.makeText(AddRoom.this, "Lưu thông tin thành công!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Lỗi không thể thêm phòng", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public int getID(String sql){
        int u = 0;
        List<List<Object>> row = db1.executeQuery(sql);
        for(List<Object> rows : row){
            u = Integer.parseInt(rows.get(0).toString());
        }
        return u;
    }

}