package com.example.appbooking.page.customer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.appbooking.R;

public class SettingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        LinearLayout call = view.findViewById(R.id.call);
        LinearLayout email = view.findViewById(R.id.email);
        LinearLayout face  = view.findViewById(R.id.face);

        // Xử lý sự kiện click vào "call"
        call.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:0999999999")); // Thay "123456789" bằng số điện thoại
            startActivity(intent);
        });

        // Xử lý sự kiện click vào "email"
        email.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:stu725105093@hnue.edu,vn")); // Thay "example@gmail.com" bằng email cần gửi
            intent.putExtra(Intent.EXTRA_SUBJECT, "Báo Lỗi"); // Tiêu đề email
            intent.putExtra(Intent.EXTRA_TEXT, "có lỗi"); // Nội dung email
            startActivity(intent);
        });

        // Xử lý sự kiện click vào "face"
        face.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.facebook.com/inhhuynhvu.156796")); // Thay "example" bằng đường dẫn Facebook
            startActivity(intent);
        });


        return view;
    }
}