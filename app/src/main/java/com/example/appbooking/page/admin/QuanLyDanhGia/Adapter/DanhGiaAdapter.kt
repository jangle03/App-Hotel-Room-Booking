package com.example.appbooking.page.admin.QuanLyDanhGia.Adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.example.appbooking.Database.MySQLite
import com.example.appbooking.Model.ChiTietDanhGia
import com.example.appbooking.Model.Don
import com.example.appbooking.R
import com.example.appbooking.page.customer.DanhGiaUserFragment

class DanhGiaAdapter (
        context: Context,
        private val resource: Int,
        private var listDanhGia: ArrayList<ChiTietDanhGia>
    ) : ArrayAdapter<ChiTietDanhGia>(context, resource, listDanhGia) {
        val mySQLite = MySQLite()

        override fun getCount(): Int {
            return listDanhGia.size
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)
            val tvMaDon = view.findViewById<TextView>(R.id.tvMaDon)
            val tvDate = view.findViewById<TextView>(R.id.tvDate)
            val tvPhong = view.findViewById<TextView>(R.id.tvPhong)
            val tvSachSe = view.findViewById<TextView>(R.id.tvSachSe)
            val tvNhanVien = view.findViewById<TextView>(R.id.tvNhanVien)
            val tvTienNghi = view.findViewById<TextView>(R.id.tvTienNghi)
            val tvChiTiet = view.findViewById<TextView>(R.id.tvChiTiet)

            var dg = listDanhGia[position]
            tvMaDon.text = "Mã Đơn: ${dg.maDon}"
            tvDate.text = "Ngày gửi: ${dg.ngayDanhGia}"
            tvPhong.text = "Chất lượng phòng: ${dg.danhGiaChatLuongPhong}/10"
            tvSachSe.text = "Sạch sẽ: ${dg.danhGiaSachSe}/10"
            tvNhanVien.text = "Nhân viên: ${dg.danhGiaNhanVienPhucVu}/10"
            tvTienNghi.text = "Tiện nghi: ${dg.danhGiaTienNghi}/10"
            tvChiTiet.text = "Chi tiết: ${dg.moTaChiTiet}"
            return view
        }



    }


