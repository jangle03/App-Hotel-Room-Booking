package com.example.appbooking.Model

import java.io.Serializable

class QuanLy : Serializable {
    var maNhanVien: Int
    var maDon: Int
    var isThanhToan: Boolean = false
    var isDuyet: Boolean = false
    var isDatCoc: Boolean = false
    var check_in_thuc_te: String
    var check_out_thuc_te: String

    constructor() {
        this.maNhanVien = -1
        this.maDon = -1
        this.check_in_thuc_te = ""
        this.check_out_thuc_te = ""
    }

    constructor(
        maNhanVien: Int,
        maDon: Int,
        isThanhToan: Boolean,
        isDuyet: Boolean,
        isDatCoc: Boolean,
        check_in_thuc_te: String,
        check_out_thuc_te: String
    ) {
        this.maNhanVien = maNhanVien
        this.maDon = maDon
        this.isThanhToan = isThanhToan
        this.isDuyet = isDuyet
        this.isDatCoc = isDatCoc
        this.check_in_thuc_te = check_in_thuc_te
        this.check_out_thuc_te = check_out_thuc_te
    }

    override fun toString(): String {
        return "QuanLy{" +
                "maNhanVien=" + maNhanVien +
                ", maDon=" + maDon +
                ", isThanhToan=" + isThanhToan +
                ", isDuyet=" + isDuyet +
                ", isDatCoc=" + isDatCoc +
                ", check_in_thuc_te='" + check_in_thuc_te + '\'' +
                ", check_out_thuc_te='" + check_out_thuc_te + '\'' +
                '}'
    }
}
