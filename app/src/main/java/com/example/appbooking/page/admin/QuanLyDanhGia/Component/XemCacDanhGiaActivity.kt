package com.example.appbooking.page.admin.QuanLyDanhGia.Component

import android.os.Bundle
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appbooking.Database.MySQLite
import com.example.appbooking.Model.ChiTietDanhGia
import com.example.appbooking.R
import com.example.appbooking.page.admin.QuanLyDanhGia.Adapter.DanhGiaAdapter
import com.example.appbooking.page.customer.Adapter.DonAdapter

class XemCacDanhGiaActivity : AppCompatActivity() {
    val db = MySQLite()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_xem_cac_danh_gia)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val lvDanhGia = findViewById<ListView>(R.id.lvDanhGia)

        val sharedPreferences = this.getSharedPreferences("UserInfo", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("userId", -1)
        val username = sharedPreferences.getString("username", "Guest")
        val role = sharedPreferences.getInt("role", -1)
        val imageBack = findViewById<ImageView>(R.id.arrow_back_ios)
        imageBack.setOnClickListener { finish() }
        var list = getDanhGiaTuMaDon()
        val adapterDanhGia = DanhGiaAdapter(this, R.layout.user_danh_gia, list)
        lvDanhGia.adapter = adapterDanhGia
    }

    fun getDanhGiaTuMaDon(): ArrayList<ChiTietDanhGia> {
        var arr = ArrayList<ChiTietDanhGia>()
        db.db.connect().use { conn ->
            val sql = "SELECT * FROM CHI_TIET_DANH_GIA;"
            val rows = conn.query(sql)
            rows.forEach { row ->
                arr.add(ChiTietDanhGia(
                    row.get(0).toString().toInt(),
                    row.get(1).toString().toInt(),
                    row.get(2).toString(),
                    row.get(3).toString().toInt(),
                    row.get(4).toString().toInt(),
                    row.get(5).toString().toInt(),
                    row.get(6).toString().toInt(),
                    row.get(7).toString()
                ))
            }
        }
        return arr
    }

}