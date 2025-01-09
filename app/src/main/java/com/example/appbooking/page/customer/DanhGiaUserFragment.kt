package com.example.appbooking.page.customer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.appbooking.Database.MySQLite
import com.example.appbooking.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DanhGiaUserFragment : Fragment() {
    val db = MySQLite()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_danh_gia_user, container, false)

        val tvMaDon: TextView = view.findViewById(R.id.tvMaDon)
        val sbPhong: SeekBar = view.findViewById(R.id.sbPhong)
        val sbSachSe: SeekBar = view.findViewById(R.id.sbSachSe)
        val sbNhanVien: SeekBar = view.findViewById(R.id.sbNhanVien)
        val sbTienNghi: SeekBar = view.findViewById(R.id.sbTienNghi)
        val edtMoTaChiTiet: EditText = view.findViewById(R.id.sbMoTaChiTiet)
        val btnGuiDanhGia: Button = view.findViewById(R.id.btnGuiDanhGia)
        val orderId = arguments?.getString("maDon", "-1")?.toIntOrNull() ?: -1
        val lp = db.traVeLoaiPhongTuMaDon(orderId);
        tvMaDon.text = "Mã Đơn: ${orderId} - Loại Phòng: ${lp}"
        if(getDanhGiaTuMaDon(orderId)){
            Toast.makeText(context, "Bạn đã đánh giá rồi!", Toast.LENGTH_SHORT).show()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HistoryFragment())
                .commit()
        }

        btnGuiDanhGia.setOnClickListener {
            val feedbackDetails = db.insertDataChiTietDanhGia(orderId.toInt(), getCurrentDateTime(), sbPhong.progress, sbSachSe.progress, sbNhanVien.progress, sbTienNghi.progress, edtMoTaChiTiet.text.toString())
            Toast.makeText(context, "$feedbackDetails", Toast.LENGTH_SHORT).show()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HistoryFragment())
                .commit()
        }
        return view
    }
    fun getCurrentDateTime(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        return current.format(formatter)
    }
    fun getDanhGiaTuMaDon(maDon: Int): Boolean {
        var exists = false
        db.db.connect().use { conn ->
            val sql = "SELECT COUNT(*) FROM CHI_TIET_DANH_GIA WHERE ma_don=$maDon;"
            val rows = conn.query(sql)
            rows.forEach { row ->
                exists = row.get(0).toString().toInt() > 0
            }
        }
        return exists
    }


}
