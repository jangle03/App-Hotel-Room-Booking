package com.example.appbooking.page.admin.adRoom.Adapter;

import com.example.appbooking.Model.ChiTietLoaiPhong;
import com.example.appbooking.Model.LoaiPhong;
import com.example.appbooking.Model.Phong;

import java.io.Serializable;

public class detalPhong implements Serializable {
    public Phong phong;
    public LoaiPhong loaiPhong;
    public ChiTietLoaiPhong chiTietLoaiPhong;

    public detalPhong(Phong phong, LoaiPhong loaiPhong, ChiTietLoaiPhong chiTietLoaiPhong) {
        this.phong = phong;
        this.loaiPhong = loaiPhong;
        this.chiTietLoaiPhong = chiTietLoaiPhong;
    }

    public detalPhong() {
    }

    public Phong getPhong() {
        return phong;
    }

    public ChiTietLoaiPhong getChiTietLoaiPhong() {
        return chiTietLoaiPhong;
    }

    public LoaiPhong getLoaiPhong() {
        return loaiPhong;
    }
}
