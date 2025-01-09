package com.example.appbooking.page.customer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbooking.R;
import com.example.appbooking.Model.Phong;

import java.util.ArrayList;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    private final ArrayList<Phong> danhSachPhong;
    private final OnRoomClickListener listener;
    private int selectedPosition = -1; // Lưu vị trí phòng đã chọn

    public RoomAdapter(ArrayList<Phong> danhSachPhong, OnRoomClickListener listener) {
        this.danhSachPhong = danhSachPhong;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RoomViewHolder holder, int position) {
        Phong phong = danhSachPhong.get(position);
        holder.textViewTenPhong.setText("MSP: " + phong.getMaPhong());
        holder.textViewGiaPhong.setText("Phòng: " + phong.getViTri());

        // Đánh dấu phòng được chọn
        if (selectedPosition == position) {
            holder.itemView.setBackgroundColor(Color.LTGRAY); // Thay đổi màu nền của phòng đã chọn
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE); // Màu nền mặc định
        }

        holder.itemView.setOnClickListener(v -> {
            selectedPosition = position; // Cập nhật vị trí phòng được chọn
            notifyDataSetChanged(); // Cập nhật giao diện
            listener.onRoomClick(phong); // Gửi thông tin phòng đã chọn
        });
    }

    @Override
    public int getItemCount() {
        return danhSachPhong.size();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTenPhong, textViewGiaPhong;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTenPhong = itemView.findViewById(R.id.textViewTenPhong);
            textViewGiaPhong = itemView.findViewById(R.id.textViewGiaPhong);
        }
    }

    public interface OnRoomClickListener {
        void onRoomClick(Phong phong);
    }
}
