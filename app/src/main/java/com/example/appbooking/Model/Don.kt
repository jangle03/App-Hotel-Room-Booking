package com.example.appbooking.Model

import java.io.Serializable
import java.util.Date




class Don : Serializable {
    var maDon: Int
    var ma_nguoi_dat: Int
    var checkIn: Date? = null
    var ngayLapPhieu: Date? = null

    constructor() {
        this.maDon = -1
        this.ma_nguoi_dat = -1
    }

    constructor(maDon: Int, checkIn: java.util.Date?, maNhanVienNhapPhieu: Int, ngayLapPhieu: java.util.Date?) {
        this.maDon = maDon
        this.checkIn = checkIn
        this.ma_nguoi_dat = maNhanVienNhapPhieu
        this.ngayLapPhieu = ngayLapPhieu
    }

    override fun toString(): String {
        return "Don{" +
                "maDon=" + maDon +
                ", checkIn=" + checkIn +
                ", maNhanVienNhapPhieu=" + ma_nguoi_dat +
                ", ngayLapPhieu=" + ngayLapPhieu +
                '}'
    }
}
