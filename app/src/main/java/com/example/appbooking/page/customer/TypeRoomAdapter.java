package com.example.appbooking.page.customer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbooking.Database.MySQLite;
import com.example.appbooking.Model.ChiTietLoaiPhong;
import com.example.appbooking.R;
import com.example.appbooking.Model.LoaiPhong;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class TypeRoomAdapter extends RecyclerView.Adapter<TypeRoomAdapter.LoaiPhongViewHolder> {
    MySQLite db1 = new MySQLite();

    private final List<LoaiPhong> loaiPhongList;
    private final Context context;  // Thêm Context vào Adapter

    // Cập nhật constructor để nhận vào Context
    public TypeRoomAdapter(List<LoaiPhong> loaiPhongList, Context context) {
        this.loaiPhongList = loaiPhongList;
        this.context = context;
    }

    @NonNull
    @Override
    public LoaiPhongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_loai_phong, parent, false);
        return new LoaiPhongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoaiPhongViewHolder holder, int position) {
        LoaiPhong loaiPhong = loaiPhongList.get(position);
        // Định dạng số thành dạng 7.000.000
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedGia = formatter.format(loaiPhong.getGia());

        holder.tvLoaiPhong.setText(loaiPhong.getTen());
//        holder.tvGiaPhong.setText("Giá: " + loaiPhong.getGia() + " VND");
        holder.tvGiaPhong.setText("Giá: " + formattedGia + " VND");
        holder.tvSoNguoiToiDa.setText("Tối đa: " + loaiPhong.getSoNguoiToiDa() + " người");
        holder.tvMoTa.setText(loaiPhong.getMoTa());

        // Thêm sự kiện click cho từng item
        holder.itemView.setOnClickListener(v -> {
            // Tạo Intent để chuyển tới ChiTietLoaiPhongActivity
            Intent intent = new Intent(context, DetailsTypeRoomActivity.class);

            // Truyền dữ liệu chi tiết về loại phòng
//            intent.putExtra("checkIn", )

            Date checkIn = new Date();
            Date checkOut = new Date(System.currentTimeMillis() + 86400000);
            intent.putExtra("checkIn", checkIn.getTime());
            intent.putExtra("checkOut", checkOut.getTime());
            intent.putExtra("tenPhong", loaiPhong.getTen());
            intent.putExtra("giaPhong", "Giá Phòng: " + formattedGia + " VND");
//            intent.putExtra("giaPhong", "Giá Phòng: " + String.valueOf(loaiPhong.getGia()) +"VND");
            intent.putExtra("soNguoi","Tối đa " + loaiPhong.getSoNguoiToiDa() + " người");
            intent.putExtra("moTaPhong", "Loại Phòng: " + loaiPhong.getMoTa() );
            intent.putExtra("imageResource", getchiTiet(loaiPhong.getMaLoaiPhong())); // intent gửi uri ảnh
            intent.putExtra("moTaChiTiet", "Chi Tiết: " + getMoTaChiTiet(loaiPhong.getMaLoaiPhong()));
            intent.putExtra("maloaiphong", loaiPhong.getMaLoaiPhong());
// Lấy danh sách tiện nghi từ cơ sở dữ liệu hoặc tạo danh sách tiện nghi

            List<String> tienNghiList = getTienNghi(loaiPhong.getMaLoaiPhong());
            intent.putExtra("tienNghi", new ArrayList<>(tienNghiList));  // Truyền danh sách vào Intent



            // Khởi động ChiTietLoaiPhongActivity
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return loaiPhongList.size();
    }

    public static class LoaiPhongViewHolder extends RecyclerView.ViewHolder {
        TextView tvLoaiPhong;
        TextView tvGiaPhong;
        TextView tvSoNguoiToiDa;
        TextView tvMoTa;

        public LoaiPhongViewHolder(View itemView) {
            super(itemView);
            tvLoaiPhong = itemView.findViewById(R.id.tvLoaiPhong);
            tvGiaPhong = itemView.findViewById(R.id.tvGiaPhong);
            tvSoNguoiToiDa = itemView.findViewById(R.id.tvSoNguoiToiDa);
            tvMoTa = itemView.findViewById(R.id.tvMoTa);
        }
    }

    public String getchiTiet(int id){ // hàm lấy ảnh theo mã loại phòng chuyền mã loại phòng vào id
        String str = "";
        try{
            List<List<Object>> anh = db1.executeQuery("select hinh from CHI_TIET_LOAI_PHONG where ma_loai_phong = " + id + " limit 1;");
            for(List<Object> row: anh){
                str = row.get(0).toString();
            }
        }catch (Exception e){
            e.getMessage();
        }
        if(!str.isEmpty()){
            return db1.getDrawableResourceUrl(context, str);
        }else{
            return db1.getDrawableResourceUrl(context, "ad_anh_clone_phong");
        }
    }
    public String getMoTaChiTiet(int id) { // Lấy mô tả chi tiết theo mã loại phòng
        String moTaChiTiet = "";
        try {
            List<List<Object>> result = db1.executeQuery("SELECT mo_ta_chi_tiet FROM LOAI_PHONG WHERE ma_loai_phong = " + id + " LIMIT 1;");
            for (List<Object> row : result) {
                moTaChiTiet = row.get(0).toString();
            }
        } catch (Exception e) {
            e.printStackTrace(); // Hiển thị lỗi nếu có
        }
        return moTaChiTiet.isEmpty() ? "Mô tả chi tiết không có sẵn" : moTaChiTiet;
    }


    public List<String> getTienNghi(int maLoaiPhong) {
        List<String> tienNghiList = new ArrayList<>();
        try {
            List<List<Object>> result = db1.executeQuery("SELECT ten_tien_nghi FROM TIEN_NGHI " +
                    "JOIN CO_TIEN_NGHI ON TIEN_NGHI.ma_tien_nghi = CO_TIEN_NGHI.ma_tien_nghi " +
                    "WHERE ma_loai_phong = " + maLoaiPhong + ";");
            for (List<Object> row : result) {
                tienNghiList.add(row.get(0).toString());  // Lấy tên tiện nghi
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tienNghiList;
    }

}