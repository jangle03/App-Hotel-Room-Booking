package com.example.appbooking.page.admin.QuanLyDon;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbooking.Database.MySQLite;
import com.example.appbooking.R;

import java.util.ArrayList;
import java.util.HashMap;

public class PhongAdapter extends ArrayAdapter<HashMap<String, String>> {

    Activity context;
    int resource;
    ArrayList<HashMap<String, String>> dataList;
    MySQLite db;
    public PhongAdapter(Activity context, int resource, ArrayList<HashMap<String, String>> dataList) {
        super(context, resource, dataList);
        this.context = context;
        this.resource = resource;
        this.dataList = dataList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(resource, parent, false);

            // Ánh xạ các view
            holder.tenPhong = convertView.findViewById(R.id.tenPhong);
            holder.username = convertView.findViewById(R.id.username); // User ID
            holder.ad_btnEditRoom = convertView.findViewById(R.id.ad_btnEditRoom); // Ánh xạ nút "Xem Chi Tiết"

            convertView.setTag(holder);
        } else {
            // Tái sử dụng view
            holder = (ViewHolder) convertView.getTag();
        }
        // Lấy dữ liệu từ danh sách
        try {
            HashMap<String, String> item = dataList.get(position);
            String tenPhong = item.get("vi_tri");
            String username = item.get("name");
            String sdt = item.get("sdt");
            String cccd = item.get("cccd");

            String ma_loai_phong = item.get("ma_loai_phong");
            String ma_don = item.get("ma_don");
            String check_in = item.get("check_in");
            String check_out = item.get("check_out");
            final String maPhong = item.get("ma_phong");

            holder.tenPhong.setText(tenPhong);
            holder.username.setText(username + " - SDT: " + sdt);

            holder.ad_btnEditRoom.setOnClickListener(v -> {
                Intent intent = new Intent(context, RoomDetailDon.class);
                intent.putExtra("MA_PHONG", maPhong);
                intent.putExtra("tenPhong", tenPhong);
                intent.putExtra("username", username);
                intent.putExtra("sdt", sdt);
                intent.putExtra("cccd", cccd);
                intent.putExtra("ma_loai_phong", ma_loai_phong);
                intent.putExtra("ma_don", ma_don);
                intent.putExtra("check_in", check_in);
                intent.putExtra("check_out", check_out);
                context.startActivity(intent);
            });

        } catch (Exception e) {
            Toast.makeText(context, "Lỗi hiển thị phòng", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return convertView;
    }

    // Phương thức cập nhật dữ liệu và làm mới ListView
    public void updateData(ArrayList<HashMap<String, String>> newData) {
        dataList.clear();
        dataList.addAll(newData);
        notifyDataSetChanged();
    }

    // ViewHolder để lưu trữ các view con
    private static class ViewHolder {
        TextView tenPhong;
        TextView username;
        Button ad_btnEditRoom; // Khai báo Button cho nút "Xem Chi Tiết"
    }
}
