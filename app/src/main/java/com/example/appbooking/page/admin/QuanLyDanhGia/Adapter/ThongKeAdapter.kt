package com.example.appbooking.page.admin.QuanLyDanhGia.Adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.example.appbooking.Database.MySQLite
import com.example.appbooking.Model.ChiTietDanhGia
import com.example.appbooking.Model.Don
import com.example.appbooking.Model.RoomRating
import com.example.appbooking.R
import com.example.appbooking.page.customer.DanhGiaUserFragment

class ThongKeAdapter (
    context: Context,
    private val resource: Int,
    private var listDanhGia: ArrayList<RoomRating>
) : ArrayAdapter<RoomRating>(context, resource, listDanhGia) {
    val mySQLite = MySQLite()

    override fun getCount(): Int {
        return listDanhGia.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)


        val tvLoaiPhong = view.findViewById<TextView>(R.id.tvLoaiPhong)
        val imgAvt = view.findViewById<ImageView>(R.id.imgAvt)
        val tvPhong = view.findViewById<TextView>(R.id.tvPhong)
        val tvSachSe = view.findViewById<TextView>(R.id.tvSachSe)
        val tvNhanVien = view.findViewById<TextView>(R.id.tvNhanVien)
        val tvTienNghi = view.findViewById<TextView>(R.id.tvTienNghi)

        val dg = listDanhGia[position]

        tvLoaiPhong.text = dg.ten
        tvPhong.text = "Chất lượng phòng: ${dg.avgDanhGiaChatLuongPhong}/10"
        tvSachSe.text = "Sạch sẽ: ${dg.avgDanhGiaSachSe}/10"
        tvNhanVien.text = "Nhân viên: ${dg.avgDanhGiaNhanVienPhucVu}/10"
        tvTienNghi.text = "Tiện nghi: ${dg.avgDanhGiaTienNghi}/10"

//        imgAvt.setImageResource(R.drawable.default_room_image)

        return view
    }

}

