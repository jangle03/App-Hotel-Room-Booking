package com.example.appbooking.page.admin.QuanLyDanhGia.Component

import android.os.Bundle
import android.widget.ImageView
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appbooking.Database.MySQLite
import com.example.appbooking.Model.ChiTietDanhGia
import com.example.appbooking.Model.RoomRating
import com.example.appbooking.R
import com.example.appbooking.page.admin.QuanLyDanhGia.Adapter.DanhGiaAdapter
import com.example.appbooking.page.admin.QuanLyDanhGia.Adapter.ThongKeAdapter

class ThongKeDanhGiaActivity : AppCompatActivity() {
    val db = MySQLite()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_thong_ke_danh_gia)
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
        var list = db.getThongKe()
        val adapterThongKe = ThongKeAdapter(this, R.layout.admin_thong_ke, list)
        lvDanhGia.adapter = adapterThongKe
    }

}
