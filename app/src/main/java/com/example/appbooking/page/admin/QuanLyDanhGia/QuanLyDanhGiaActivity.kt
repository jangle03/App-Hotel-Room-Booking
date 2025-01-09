package com.example.appbooking.page.admin.QuanLyDanhGia

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.appbooking.R
import com.example.appbooking.page.admin.QuanLyDanhGia.Adapter.ThongKeAdapter
import com.example.appbooking.page.admin.QuanLyDanhGia.Component.ThongKeDanhGiaActivity
import com.example.appbooking.page.admin.QuanLyDanhGia.Component.XemCacDanhGiaActivity
import com.example.appbooking.page.admin.QuanLyMaGiamGia.Component.TaoMaGiamGia

class QuanLyDanhGiaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quan_ly_danh_gia2)

        val xemcacdanhgia = findViewById<LinearLayout>(R.id.lnTaoMa)
        val thongke = findViewById<LinearLayout>(R.id.lnQuanLy)


        val imageBack = findViewById<ImageView>(R.id.arrow_back_ios)
        imageBack.setOnClickListener { finish() }


        xemcacdanhgia.setOnClickListener {
            val taoMaIntent = Intent(
                this@QuanLyDanhGiaActivity,
                XemCacDanhGiaActivity::class.java
            )
            startActivity(taoMaIntent)
        }
        thongke.setOnClickListener {
            val taoMaIntent = Intent(
                this@QuanLyDanhGiaActivity,
                ThongKeDanhGiaActivity::class.java
            )
            startActivity(taoMaIntent)
        }
    }
}
