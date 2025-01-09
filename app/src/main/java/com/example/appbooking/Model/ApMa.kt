package com.example.appbooking.Model

import java.io.Serializable



class ApMa : Serializable {
    var maDon: Int
    var maUuDai: Int

    constructor() {
        this.maDon = -1
        this.maUuDai = -1
    }

    constructor(maDon: Int, maUuDai: Int) {
        this.maDon = maDon
        this.maUuDai = maUuDai
    }

    override fun toString(): String {
        return "ApMa{" +
                "maDon=" + maDon +
                ", maUuDai=" + maUuDai +
                '}'
    }
}
//        createTable("AP_MA", """
//            ma_don INTEGER,
//            ma_uu_dai INTEGER,
//            PRIMARY KEY (ma_don, ma_uu_dai),
//            FOREIGN KEY (ma_don) REFERENCES DON(ma_don),
//            FOREIGN KEY (ma_uu_dai) REFERENCES UU_DAI(ma_uu_dai)
//        """)3