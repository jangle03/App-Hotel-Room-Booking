package com.example.appbooking.page.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.appbooking.R;
import com.example.appbooking.page.DashboardActivity;

public class BillHistory extends Fragment {
    Button btnNextPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout của Fragment
        View rootView = inflater.inflate(R.layout.list_history_booking, container, false);

        // Ánh xạ nút từ layout
        btnNextPage = rootView.findViewById(R.id.btnNextPage);

        // Xử lý sự kiện nhấn nút
        btnNextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang Activity mới
                Intent intent = new Intent(getActivity(), PayMentHouse.class);  // Sử dụng getActivity() để khởi tạo Intent
                startActivity(intent);  // Bắt đầu Activity mới
            }
        });

        return rootView;  // Trả về rootView đã được inflate
    }
}
