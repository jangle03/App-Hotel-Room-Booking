package com.example.appbooking.page.admin.quanLyUser.Adapter;

import static com.example.appbooking.R.*;
import static com.example.appbooking.R.drawable.anh_user;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appbooking.Database.MySQLite;
import com.example.appbooking.MainActivity;
import com.example.appbooking.Model.TaiKhoan;
import com.example.appbooking.R;
import com.example.appbooking.page.admin.quanLyUser.Component.editUser;
import com.example.appbooking.page.admin.quanLyUser.quanLyUser;

import java.util.ArrayList;
import java.util.List;

public class adAdpterUser extends ArrayAdapter {
    Context context;
    int resource;
    MySQLite db1 = new MySQLite();
    ArrayList<TaiKhoan> listTK;
    public adAdpterUser(@NonNull Context context, int resource, ArrayList<TaiKhoan> listTK) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.listTK = listTK;
    }

    public int getCount(){
        return this.listTK.size();
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Sử dụng convertView để tái sử dụng View
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(resource, parent, false);
        }
        // Ánh xạ các thành phần trong layout
        ImageView ad_img1 = convertView.findViewById(R.id.ad_img1);
        Button ad_btnEdit = convertView.findViewById(R.id.ad_btnEdit);
        Button ad_btnDel = convertView.findViewById(R.id.ad_btnDel);
        TextView ad_tv1 = convertView.findViewById(R.id.ad_tv1);
        TextView ad_tv2 = convertView.findViewById(R.id.ad_tv2);
        TextView ad_tv3 = convertView.findViewById(R.id.ad_tv3);

        // Gán dữ liệu cho các thành phần
        TaiKhoan tk = listTK.get(position);
        ad_tv1.setText(tk.getName());
        ad_tv2.setText(tk.getEmail()+ " --- " + tk.getSdt());
        ad_tv3.setText(tk.getRole() == 0 ? "Admin" : "User");
        try{
            if (tk.getHinh() != null && !tk.getHinh().isEmpty()) {
                Uri imageUri = Uri.parse(db1.getDrawableResourceUrl(getContext(), tk.getHinh())); // Chuyển chuỗi URI thành đối tượng Uri
                ad_img1.setImageURI(imageUri); // Hiển thị ảnh từ URI
            }
        }catch (Exception e){
            ad_img1.setImageURI(Uri.parse(db1.getDrawableResourceUrl(getContext(), "anh_user")));
        }

        ad_btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(getContext(), editUser.class);
                newIntent.putExtra("data", tk);
                ((AppCompatActivity) getContext()).startActivityForResult(newIntent, 1);
            }
        });

        ad_btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialogthoat = new AlertDialog.Builder(getContext());
                dialogthoat.setTitle("Xóa người dùng ");
                dialogthoat.setMessage("Lựa chọn");
                dialogthoat.setPositiveButton("có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try{
                            db1.updateSQL("DELETE FROM TAI_KHOAN WHERE id = " + tk.getId() + ";");
                            ArrayList<TaiKhoan> list = getall("SELECT * FROM TAI_KHOAN;");
                            updateData(list);
                            Toast.makeText(context, "Xóa "+ tk.getName()+ " thành công", Toast.LENGTH_SHORT).show();
                        }catch(Exception e){
                            Toast.makeText(context, "Lỗi khi xóa dữ liệu", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialogthoat.setNegativeButton("không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                dialogthoat.create().show();
            }
        });

        return convertView;
    }

    public void updateData(ArrayList<TaiKhoan> newList) {
        this.listTK.clear(); // Xóa danh sách hiện tại
        this.listTK.addAll(newList); // Thêm dữ liệu mới
        notifyDataSetChanged(); // Thông báo adapter cập nhật lại giao diện
    }

    ArrayList<TaiKhoan> getall(String sql){
        ArrayList<TaiKhoan> t = new ArrayList<>();
        try{
            List<List<Object>> list = db1.executeQuery(sql);
            for (List<Object> row : list) {
                TaiKhoan tk1 = new TaiKhoan(Integer.parseInt(row.get(0).toString()),
                        Integer.parseInt(row.get(8).toString()),
                        row.get(1).toString(),
                        row.get(2).toString(),
                        row.get(4).toString(),
                        row.get(7).toString(),
                        row.get(5).toString(),
                        row.get(6).toString(),
                        row.get(3).toString(),
                        row.get(9).toString());
                t.add(tk1);
            }
            return t;
        }catch(Exception e){
            return null;
        }
    }
}
