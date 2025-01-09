package com.example.appbooking.page.customer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbooking.Model.TienNghi;
import com.example.appbooking.R;

import java.util.ArrayList;
import java.util.List;

public class TienNghiAdapter extends RecyclerView.Adapter<TienNghiAdapter.TienNghiViewHolder> {
    private final List<String> tienNghiList;

    public TienNghiAdapter(List<String> tienNghiList) {
        this.tienNghiList = tienNghiList;
    }

    @NonNull
    @Override
    public TienNghiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tien_nghi, parent, false);
        return new TienNghiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TienNghiViewHolder holder, int position) {
        holder.tvTienNghi.setText(tienNghiList.get(position));
    }

    @Override
    public int getItemCount() {
        return tienNghiList.size();
    }

    public static class TienNghiViewHolder extends RecyclerView.ViewHolder {
        TextView tvTienNghi;

        public TienNghiViewHolder(View itemView) {
            super(itemView);
            tvTienNghi = itemView.findViewById(R.id.tvTienNghi);
        }
    }
}
