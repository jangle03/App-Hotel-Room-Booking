package com.example.appbooking.Model

import java.io.Serializable
import java.sql.Date


class HoaDon : Serializable {
    var maHoaDon: Int
    var maDon: Int
    var soTienThanhToan: Int
    var ngayThanhToan: Date? = null

    constructor() {
        this.maHoaDon = -1
        this.maDon = -1
        this.soTienThanhToan = 0
    }

    constructor(maHoaDon: Int, maDon: Int, ngayThanhToan: Date?, soTienThanhToan: Int) {
        this.maHoaDon = maHoaDon
        this.maDon = maDon
        this.ngayThanhToan = ngayThanhToan
        this.soTienThanhToan = soTienThanhToan
    }

    override fun toString(): String {
        return "HoaDon{" +
                "maHoaDon=" + maHoaDon +
                ", maDon=" + maDon +
                ", ngayThanhToan=" + ngayThanhToan +
                ", soTienThanhToan=" + soTienThanhToan +
                '}'
    }
}
//        createTable("HOA_DON", """
//            ma_hoa_don INTEGER PRIMARY KEY AUTOINCREMENT,
//            ma_don INTEGER UNIQUE,
//            ngay_thanh_toan DATE,
//            so_tien_thanh_toan INTEGER,
//            FOREIGN KEY (ma_don) REFERENCES DON(ma_don)
//        """)