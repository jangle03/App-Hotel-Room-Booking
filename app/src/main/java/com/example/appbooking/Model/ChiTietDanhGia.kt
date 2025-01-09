package com.example.appbooking.Model

import java.io.Serializable
import java.util.Date


class ChiTietDanhGia : Serializable {
    var maDanhGia: Int
    var maDon: Int
    var danhGiaChatLuongPhong: Int = 0
    var danhGiaSachSe: Int = 0
    var danhGiaNhanVienPhucVu: Int = 0
    var danhGiaTienNghi: Int = 0
    var moTaChiTiet: String = ""
    var ngayDanhGia: String = ""

    constructor() {
        this.maDanhGia = -1
        this.maDon = -1
    }

    constructor(
        maDanhGia: Int, maDon: Int, ngayDanhGia: String, danhGiaChatLuongPhong: Int,
        danhGiaSachSe: Int, danhGiaNhanVienPhucVu: Int,
        danhGiaTienNghi: Int, moTaChiTiet: String
    ) {
        this.maDanhGia = maDanhGia
        this.maDon = maDon
        this.ngayDanhGia = ngayDanhGia
        this.danhGiaChatLuongPhong = danhGiaChatLuongPhong

        this.danhGiaSachSe = danhGiaSachSe
        this.danhGiaNhanVienPhucVu = danhGiaNhanVienPhucVu
        this.danhGiaTienNghi = danhGiaTienNghi
        this.moTaChiTiet = moTaChiTiet
    }

    override fun toString(): String {
        return "ChiTietDanhGia{" +
                "maDanhGia=" + maDanhGia +
                ", maDon=" + maDon +
                ", ngayDanhGia=" + ngayDanhGia +
                ", danhGiaChatLuongPhong=" + danhGiaChatLuongPhong +

                ", danhGiaSachSe=" + danhGiaSachSe +
                ", danhGiaNhanVienPhucVu=" + danhGiaNhanVienPhucVu +
                ", danhGiaTienNghi=" + danhGiaTienNghi +
                ", moTaChiTiet='" + moTaChiTiet + '\'' +
                '}'
    }
}
