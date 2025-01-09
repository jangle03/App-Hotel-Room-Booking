package com.example.appbooking.Model

import java.io.Serializable

class LoaiPhong : Serializable {
    var maLoaiPhong: Int = 0
    var ten: String? = null
    var gia: Int = 0
    var soNguoiToiDa: Int = 0
    var moTa: String? = null
    var moTaChiTiet: String? = null

    constructor()

    constructor(maLoaiPhong: Int, ten: String?, gia: Int, soNguoiToiDa: Int, moTa: String?, moTaChiTiet: String?) {
        this.maLoaiPhong = maLoaiPhong
        this.ten = ten
        this.gia = gia
        this.soNguoiToiDa = soNguoiToiDa
        this.moTa = moTa
        this.moTaChiTiet = moTaChiTiet
    }

    override fun toString(): String {
        return "LoaiPhong{" +
                "maLoaiPhong=" + maLoaiPhong +
                ", ten='" + ten + '\'' +
                ", gia=" + gia +
                ", soNguoiToiDa=" + soNguoiToiDa +
                ", moTa='" + moTa + '\'' +
                ", moTaChiTiet='" + moTaChiTiet + '\'' +
                '}'
    }
}