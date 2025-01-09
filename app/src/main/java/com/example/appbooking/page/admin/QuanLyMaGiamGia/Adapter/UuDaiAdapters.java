package com.example.appbooking.page.admin.QuanLyMaGiamGia.Adapter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.appbooking.Database.MySQLite;
import com.example.appbooking.Model.UuDai;
import com.example.appbooking.R;
import com.example.appbooking.page.admin.QuanLyMaGiamGia.Component.Chitietmauudai;
import com.example.appbooking.page.admin.QuanLyMaGiamGia.Component.SuaMaGiamGia;
import com.example.appbooking.page.admin.QuanLyMaGiamGia.Component.TaoMaGiamGia;
import com.example.appbooking.page.admin.QuanLyMaGiamGia.Component.ThongTinMaGiamGia;
import com.example.appbooking.page.admin.QuanLyMaGiamGia.QuanLyMaGiamGia;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UuDaiAdapters extends ArrayAdapter {
    MySQLite db = new MySQLite();
    Activity context;
    int resource;
    ArrayList<UuDai> listMaUuDai;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    public UuDaiAdapters(Activity context, int resource, ArrayList<UuDai> listMaUuDai) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.listMaUuDai = listMaUuDai;
    }

    @Override
    public int getCount() {
        return listMaUuDai.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View customView = inflater.inflate(this.resource, null);

        TextView tvTenMa = customView.findViewById(R.id.tvTenMaGG);
        TextView tvGiam = customView.findViewById(R.id.tvGiamGia);
        TextView tvMaGiamGia = customView.findViewById(R.id.tvMaGiamGia);
        TextView tvTrangThai = customView.findViewById(R.id.tvTrangThai);
        ImageView imEdit = customView.findViewById(R.id.ivEdit);
        ImageView imCopy = customView.findViewById(R.id.ivCopy);

        UuDai uuDai = this.listMaUuDai.get(position);

        tvTenMa.setText(uuDai.getTenMa());
        tvGiam.setText(uuDai.getGiam() + "");
        tvMaGiamGia.setText(uuDai.getMaUuDai() +"");

        if (uuDai.getNgayHetHan() != null) {
            Date now = new Date();
            if (uuDai.getNgayHetHan().compareTo(now) <= 0) {
                tvTrangThai.setText("Hết Hạn");
                tvTrangThai.setTextColor(Color.RED);
            } else {
                tvTrangThai.setText("Còn Hạn");
                tvTrangThai.setTextColor(Color.GREEN);
            }
        } else {
            tvTrangThai.setText("Không xác định");
            tvTrangThai.setTextColor(Color.GRAY);
        }

        // Xử lý sự kiện khi nhấn vào toàn bộ item
        customView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent view = new Intent(context, Chitietmauudai.class);
                view.putExtra("tenMa", uuDai.getTenMa());
                view.putExtra("maGiamGia", uuDai.getMaUuDai());
                view.putExtra("giamGia", uuDai.getGiam());
                view.putExtra("ngayBatDau", uuDai.getNgayBatDau() != null ? uuDai.getNgayBatDau().getTime() : null);
                view.putExtra("ngayHetHan", uuDai.getNgayHetHan() != null ? uuDai.getNgayHetHan().getTime() : null);
                view.putExtra("trangThai", uuDai.getNgayHetHan() != null && uuDai.getNgayHetHan().compareTo(new Date()) > 0 ? "Còn Hạn" : "Hết Hạn");
                context.startActivity(view);
            }
        });



        // Xử lý edit
        imEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent suaMa = new Intent(context, SuaMaGiamGia.class);
                suaMa.putExtra("id", uuDai.getMaUuDai());
                context.startActivity(suaMa);
            }
        });

        // Xử lý Copy
        imCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy tên mã giảm giá từ đối tượng UuDai
                String maGiamGia = uuDai.getMaUuDai() + "";

                // Tạo ClipboardManager
                ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

                // Tạo ClipData chứa mã giảm giá
                ClipData clipData = ClipData.newPlainText("Mã Giảm Giá", maGiamGia);
                // Đưa dữ liệu vào Clipboard
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(context, "Đã sao chép mã: " + maGiamGia, Toast.LENGTH_SHORT).show();
            }
        });
        return  customView;
    }

//    ArrayList<UuDai> getall(String sql){
//        ArrayList<UuDai> listMa = new ArrayList<>();
//        try{
//            List<List<Object>> list = db.executeQuery(sql);
//            for (List<Object> row : list) {
//                UuDai uuDai = new UuDai();
//                uuDai.setMaNhanVien(Integer.parseInt(row.get(0).toString()));
//                uuDai.setMaUuDai(Integer.parseInt(row.get(1).toString()));
//                uuDai.setTenMa(row.get(2).toString());
//                try {
//                    Date ngayBatDau = row.get(3) != null ? dateFormat.parse(row.get(3).toString()) : null;
//                    uuDai.setNgayBatDau(ngayBatDau);
//                } catch (ParseException e) {
//                    uuDai.setNgayBatDau(null);
//                }
//                try {
//                    Date ngayBatDau = row.get(4) != null ? dateFormat.parse(row.get(4).toString()) : null;
//                    uuDai.setNgayHetHan(ngayBatDau);
//                } catch (ParseException e) {
//                    uuDai.setNgayHetHan(null);
//                }
//                uuDai.setGiam(Double.parseDouble(row.get(5).toString()));
//                uuDai.setDieuKienVeGia(Integer.parseInt(row.get(6).toString()));
//                listMa.add(uuDai);
//            }
//            return listMa;
//        }catch(Exception e){
//            return null;
//        }
//    }

//    public void updateData(ArrayList<UuDai> newList) {
//        if (newList != null && !newList.equals(this.listMaUuDai)) {
//            this.listMaUuDai.clear();
//            this.listMaUuDai.addAll(newList);
//            notifyDataSetChanged();
//        }
//    }


}
