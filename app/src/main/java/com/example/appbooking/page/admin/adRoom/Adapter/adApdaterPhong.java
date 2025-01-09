package com.example.appbooking.page.admin.adRoom.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.appbooking.Database.MySQLite;
import com.example.appbooking.Model.TaiKhoan;
import com.example.appbooking.R;
import com.example.appbooking.page.admin.adRoom.Component.AddRoom;

import java.util.ArrayList;

public class adApdaterPhong extends ArrayAdapter {
    Context context;
    int resource;
    ArrayList<detalPhong> listDetal;
    MySQLite db1;
    // ImageView
    public adApdaterPhong(@NonNull Context context, int resource, ArrayList<detalPhong> listDetal) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.listDetal = listDetal;
    }

    public int getCount(){
        return this.listDetal.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(resource, parent, false);
        }
        TextView ad_phong_ten = convertView.findViewById(R.id.ad_phong_ten);
        TextView ad_gia_sl = convertView.findViewById(R.id.ad_gia_sl);
        TextView ad_mo_ta = convertView.findViewById(R.id.ad_mo_ta);
        Button ad_btnDetal = convertView.findViewById(R.id.ad_btnDetal);
        Button ad_btnFix = convertView.findViewById(R.id.ad_btnFix);
        ImageView rtImg = convertView.findViewById(R.id.rtImg);
        db1 = new MySQLite();
        detalPhong dphong = listDetal.get(position);
        ad_btnFix.setOnClickListener(v -> {
            Intent in1 = new Intent(getContext(), AddRoom.class);
            in1.putExtra("data", dphong);
            getContext().startActivity(in1);
        });
        ad_phong_ten.setText(dphong.getPhong().getViTri() + " ( " + dphong.getLoaiPhong().getTen() + " )");
        ad_gia_sl.setText(dphong.getLoaiPhong().getGia() + "VND / " + dphong.getLoaiPhong().getSoNguoiToiDa() + " Nguoi");
        ad_mo_ta.setText(dphong.getLoaiPhong().getMoTa().toString());
        try{
            if (dphong.chiTietLoaiPhong.getHinh().contains("/")) {
                rtImg.setImageURI(Uri.parse(dphong.chiTietLoaiPhong.getHinh()));
            } else {
                rtImg.setImageURI(Uri.parse(db1.getDrawableResourceUrl(getContext(), dphong.chiTietLoaiPhong.getHinh())));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        // Đăng ký menu ngữ cảnh nếu cần
        convertView.setOnLongClickListener(view -> {
            view.showContextMenu();
            return true; // Trả về true để sự kiện không lan tiếp
        });
        return convertView;
    }

    public void updateData(ArrayList<detalPhong> newList) {
        this.listDetal.clear(); // Xóa danh sách hiện tại
        this.listDetal.addAll(newList); // Thêm dữ liệu mới
        notifyDataSetChanged(); // Thông báo adapter cập nhật lại giao diện
    }
}
