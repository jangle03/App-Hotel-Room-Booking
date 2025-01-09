package com.example.appbooking.page.customer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.appbooking.Database.MySQLite;
import com.example.appbooking.Model.LoaiPhong;
import com.example.appbooking.R;

import java.util.List;

public class HomeFragment extends Fragment {
    MySQLite db;
    RecyclerView recyclerViewLoaiPhong;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize RecyclerView
        recyclerViewLoaiPhong = view.findViewById(R.id.recyclerViewLoaiPhong);
        recyclerViewLoaiPhong.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Lấy danh sách loại phòng từ cơ sở dữ liệu
        db = new MySQLite();  // Giới thiệu đối tượng MySQLite
        List<LoaiPhong> loaiPhongList = db.layDuLieuLoaiPhong();

        if (loaiPhongList != null && !loaiPhongList.isEmpty()) {
            // Set up RecyclerView adapter
            TypeRoomAdapter adapter = new TypeRoomAdapter(loaiPhongList, getActivity());
            recyclerViewLoaiPhong.setAdapter(adapter);
        } else {
            // Hiển thị thông báo nếu không có dữ liệu
            Toast.makeText(getContext(), "Không có loại phòng nào trong cơ sở dữ liệu", Toast.LENGTH_SHORT).show();
        }

        return view;
    }
}