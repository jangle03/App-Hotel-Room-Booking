package com.example.appbooking.Database


import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.appbooking.Model.Don
import com.example.appbooking.Model.HoaDon
import com.example.appbooking.Model.LoaiPhong
import com.example.appbooking.Model.Phong
import com.example.appbooking.Model.RoomRating
import com.example.appbooking.Model.TaiKhoan
import com.example.appbooking.Model.TienNghi

import tech.turso.libsql.Database
import tech.turso.libsql.Libsql
import tech.turso.libsql.Row
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale

class MySQLite {
    var db: Database
    init {
        db = Libsql.open(
            url = "libsql://booking-hotel-haitrn.turso.io",
            authToken = "eyJhbGciOiJFZERTQSIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE3MzMyMzE0ODQsImlkIjoiMDgzMTMzOTQtZmM5NS00NTlhLWI1YTktODQ0ODlhMzQ5OTg1In0.MrDrfaRdKQm6-ODnqMHKpsMWRPTQx3DWdvs2SFzJxj8lblklkjnz9Ppg9lbl4DNpY8hDn1eZaRC0Gz4p3yIcAQ"
        )
    }
    fun insertDataTaiKhoan(username: String, password: String, name: String, email: String, sdt: String, cccd: String, address: String, role: Int, tenAnh: String): String {
        val query = "Select * from TAI_KHOAN where username = '$username';"
        val rows = db.connect().query(query)
        if (rows.firstOrNull() == null) {
            return try {
                val sql = """
                    INSERT INTO TAI_KHOAN (id, username, password, name, email, sdt, cccd, address, role, hinh)
                    VALUES (NULL, '$username', '$password', '$name', '$email', '$sdt', '$cccd', '$address', $role, '$tenAnh');
                """
                db.connect().query(sql)
                "Thêm thành công"
            } catch (e: Exception) {
                "Thêm không thành công: ${e.message}"
            }
        }
        return "Tài khoản đã tồn tại"
    }

    fun insertCoTienNghi(maLoaiPhong: Int, maTienNghi: Int): String {
        return try {
            val sql = "INSERT INTO CO_TIEN_NGHI (ma_tien_nghi, ma_loai_phong) VALUES ($maTienNghi, $maLoaiPhong);"
            db.connect().query(sql)
            "Thêm thành công"
        } catch (e: Exception) {
            "Thêm không thành công: ${e.message}"
        }
    }
    fun insertDataUuDai(maNhanVien: Int, tenMa: String, ngayBatDau: String, ngayHetHan: String, giam: Double, dieuKienVeGia: Int): String {
        return try {
            val sql = """
        INSERT INTO UU_DAI (ma_nhan_vien, ma_uu_dai, ten_ma, ngay_bat_dau, ngay_het_han, giam, dieu_kien_ve_gia)
        VALUES ($maNhanVien, null, '$tenMa', '$ngayBatDau', '$ngayHetHan', $giam, $dieuKienVeGia);
        """
            db.connect().query(sql)
            "Thêm thành công"
        } catch (e: Exception) {
            "Thêm không thành công: ${e.message}"
        }
    }
    fun insertChiTietUuDai(maUuDai: Int, hinh: String): String {
        return try {
            val sql = "INSERT INTO CHI_TIET_UU_DAI (id, ma_uu_dai, hinh) VALUES (NULL, $maUuDai, '$hinh');"
            db.connect().execute(sql)
            "Thêm thành công"
        } catch (e: Exception) {
            "Thêm không thành công: ${e.message}"
        }
    }
    fun insertApMa(maDon: Int, maUuDai: Int): String {
        return try {
            val sql = "INSERT INTO AP_MA (ma_don, ma_uu_dai) VALUES ($maDon, $maUuDai);"
            db.connect().execute(sql)
            "Thêm thành công"
        } catch (e: Exception) {
            "Thêm không thành công: ${e.message}"
        }
    }
    fun insertTienNghi(tenTienNghi: String, ic_mo_ta: String): String {
        return try {
            val sql = """
            INSERT INTO TIEN_NGHI (ma_tien_nghi, ten_tien_nghi, ic_mo_ta)
            VALUES (null, '$tenTienNghi', '$ic_mo_ta');
        """
            db.connect().execute(sql)
            "Thêm thành công"
        } catch (e: Exception) {
            "Thêm không thành công: ${e.message}"
        }
    }
    fun insertDataLoaiPhong(ten: String, gia: Int, soNguoiToiDa: Int, moTa: String, moTaChiTiet: String): String {
        return try {
            val sql = """
            INSERT INTO LOAI_PHONG (ma_loai_phong, ten, gia, so_nguoi_toi_da, mo_ta, mo_ta_chi_tiet)
            VALUES (NULL, '$ten', $gia, $soNguoiToiDa, '$moTa', '$moTaChiTiet');
        """
            db.connect().execute(sql)
            "Thêm thành công"
        } catch (e: Exception) {
            "Thêm không thành công: ${e.message}"
        }
    }
    fun insertDataChiTietDanhGia(maDon: Int, ngayDanhGia: String, phong: Int, sachSe: Int, nVien: Int, tienNghi: Int, moTa: String): String {
        return try {
            val sql = """
            INSERT INTO CHI_TIET_DANH_GIA (ma_danh_gia, ma_don, ngay_danh_gia, danh_gia_chat_luong_phong, danh_gia_sach_se, danh_gia_nhan_vien_phuc_vu, danh_gia_tien_nghi, mo_ta_chi_tiet)
            VALUES (NULL, $maDon, '$ngayDanhGia', $phong, $sachSe, $nVien, $tienNghi ,'$moTa');
        """
            db.connect().execute(sql)
            "Đánh giá thành công"
        } catch (e: Exception) {
            "Thêm không thành công: ${e.message}"
        }
    }
    fun insertDataPhong(viTri: String, maLoaiPhong: Int): String {
        return try {
            val sql = """
            INSERT INTO PHONG (ma_phong, vi_tri, ma_loai_phong)
            VALUES (NULL, '$viTri', $maLoaiPhong);
        """
            db.connect().execute(sql)
            "Thêm thành công"
        } catch (e: Exception) {
            "Thêm không thành công: ${e.message}"
        }
    }
    fun insertDataDon(ma_nguoi_dat: Int, ngayLapPhieu: String, checkIn: String): String {
        return try {
            val sql = """  
        INSERT INTO DON (ma_don, check_in, ma_nguoi_dat, ngay_lap_phieu)
        VALUES (NULL, '$checkIn', $ma_nguoi_dat, '$ngayLapPhieu');
        """
            db.connect().execute(sql)

            var ma_don: Int? = null
            db.connect().use { conn ->
                val sql = "SELECT MAX(ma_don) AS max_ma_don FROM DON;"
                val rows = conn.query(sql)
                rows.forEach { row ->
                    ma_don = row.get(0).toString().toInt()
                }
            }
            ma_don?.let {
                "$it"
            } ?: "Thêm không thành công, không thể lấy mã đơn."
        } catch (e: Exception) {
            "Thêm không thành công: ${e.message}"
        }
    }

    fun insertDataThue(maDon: Int, maPhong: Int, checkOut: String): String {
        return try {
            val sql = "INSERT INTO THUE VALUES ($maDon, $maPhong, '$checkOut');"
            db.connect().execute(sql)
            "Thêm thành công"
        } catch (e: Exception) {
            "Thêm không thành công: ${e.message}"
        }
    }
    fun insertDataChiTietLoaiPhong(maLoaiPhong: Int, hinh: String): String {
        return try {
            val sql = """
            INSERT INTO CHI_TIET_LOAI_PHONG (id, ma_loai_phong, hinh)
            VALUES (NULL, $maLoaiPhong, '$hinh');
        """
            db.connect().execute(sql)
            "Thêm thành công"
        } catch (e: Exception) {
            "Thêm không thành công: ${e.message}"
        }
    }

    fun insertDataHoaDon_BanDau(ma_don: Int, ngay_thanh_toan: String, tien: Double): String {

        return try {
            val sql = """
            INSERT INTO HOA_DON (ma_hoa_don, ma_don, ngay_thanh_toan, so_tien_thanh_toan)
            VALUES (NULL, $ma_don, '$ngay_thanh_toan', $tien);
        """
            db.connect().execute(sql)
            "Thêm thành công"
        } catch (e: Exception) {
            "Thêm không thành công: ${e} }"
        }
    }

    fun kiemTraDangNhap(username: String, password: String): TaiKhoan{
        var taiKhoan = TaiKhoan()
        db.connect().use { conn ->
            val sql = "SELECT * FROM TAI_KHOAN WHERE username = '$username' AND password = '$password';"
            val rows = conn.query(sql)
            rows.forEach { row ->
                taiKhoan = TaiKhoan(
                    row.get(0).toString().toInt(),
                    row.get(8).toString().toInt(),
                    row.get(1).toString(),
                    row.get(2).toString(),
                    row.get(4).toString(),
                    row.get(7).toString(),
                    row.get(5).toString(),
                    row.get(6).toString(),
                    row.get(3).toString(),
                    row.get(9).toString()
                )
            }
        }
        return taiKhoan
    }
    fun getTaiKhoan(id: Int): TaiKhoan{
        var taiKhoan = TaiKhoan()
        db.connect().use { conn ->
            val sql = "SELECT * FROM TAI_KHOAN WHERE id='$id';"
            val rows = conn.query(sql)
            rows.forEach { row ->
                taiKhoan = TaiKhoan(
                    row.get(0).toString().toInt(),
                    row.get(8).toString().toInt(),
                    row.get(1).toString(),
                    row.get(2).toString(),
                    row.get(4).toString(),
                    row.get(7).toString(),
                    row.get(5).toString(),
                    row.get(6).toString(),
                    row.get(3).toString(),
                    row.get(9).toString()
                )
            }
        }
        return taiKhoan
    }
    fun getTaiKhoantheoCCCD(cccd: String): TaiKhoan{
        var taiKhoan = TaiKhoan()
        db.connect().use { conn ->
            val sql = "SELECT * FROM TAI_KHOAN WHERE cccd='$cccd';"
            val rows = conn.query(sql)
            rows.forEach { row ->
                taiKhoan = TaiKhoan(
                    row.get(0).toString().toInt(),
                    row.get(8).toString().toInt(),
                    row.get(1).toString(),
                    row.get(2).toString(),
                    row.get(4).toString(),
                    row.get(7).toString(),
                    row.get(5).toString(),
                    row.get(6).toString(),
                    row.get(3).toString(),
                    row.get(9).toString()
                )
            }
        }
        return taiKhoan
    }

    fun getTaiKhoantheoSDT(sdt: String): TaiKhoan {
        var taiKhoan = TaiKhoan()
        db.connect().use { conn ->
            val sql = "SELECT * FROM TAI_KHOAN WHERE cccd='$sdt';"
            val rows = conn.query(sql)
            rows.forEach { row ->
                taiKhoan = TaiKhoan(
                    row.get(0).toString().toInt(),
                    row.get(8).toString().toInt(),
                    row.get(1).toString(),
                    row.get(2).toString(),
                    row.get(4).toString(),
                    row.get(7).toString(),
                    row.get(5).toString(),
                    row.get(6).toString(),
                    row.get(3).toString(),
                    row.get(9).toString()
                )
            }
        }
        return taiKhoan
    }

    fun layDanhSachTaiKhoan(): ArrayList<TaiKhoan>{
        var list = ArrayList<TaiKhoan>()
        db.connect().use { conn ->
            val sql = "SELECT * FROM TAI_KHOAN;"
            val rows = conn.query(sql)
            rows.forEach { row ->
                var taiKhoan : TaiKhoan = TaiKhoan(
                    row.get(0).toString().toInt(),
                    row.get(8).toString().toInt(),
                    row.get(1).toString(),
                    row.get(2).toString(),
                    row.get(4).toString(),
                    row.get(7).toString(),
                    row.get(5).toString(),
                    row.get(6).toString(),
                    row.get(3).toString(),
                    row.get(9).toString()
                )
                list.add(TaiKhoan())
            }
        }
        return list
    }
    fun executeQuery(sql: String): List<Row> {
        val resultList = mutableListOf<Row>()
        db.connect().use { conn ->
            val rows = conn.query(sql)
            rows.forEach { row ->
                resultList.add(row)
            }
        }
        return resultList
    }
    fun updateSQL(sql: String) {
        db.connect().execute(sql)
    }
    fun getDrawableResourceUrl(context: Context, imageName: String): String? {
        val resourceId = context.resources.getIdentifier(imageName, "drawable", context.packageName)
        return if (resourceId != 0) {
            "android.resource://${context.packageName}/$resourceId"
        } else {
            null
        }
    }
    fun formatDateTime(dateTime: String): String {   // dinh dang H -> HH
        val input = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm")
        val output = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val parsedDateTime = LocalDateTime.parse(dateTime, input)
        return parsedDateTime.format(output)
    }

    fun formatDateTime2(dateTime: String): String {
        val inputFormatter = DateTimeFormatterBuilder()
            .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm"))
            .toFormatter()

        return LocalDateTime.parse(dateTime, inputFormatter)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    }

    fun tinhGiaPhong(ma_don: Int): HashMap<String, Any> {
        var map = HashMap<String, Any>()
        var giaThuePhong: Double = 0.0
        var query = """
        SELECT D.ma_don, D.check_in, T.check_out, LP.gia, U.ngay_bat_dau, U.ngay_het_han, U.giam, U.dieu_kien_ve_gia
        FROM DON AS D 
            JOIN THUE AS T ON D.ma_don = T.ma_don 
            JOIN PHONG AS P ON P.ma_phong = T.ma_phong 
            JOIN LOAI_PHONG AS LP ON LP.ma_loai_phong = P.ma_loai_phong 
            LEFT JOIN AP_MA AS A ON A.ma_don = D.ma_don 
            LEFT JOIN UU_DAI AS U ON U.ma_uu_dai = A.ma_uu_dai
            WHERE D.ma_don = $ma_don;
    """
        var checkIn:String = ""
        var checkOut:String = ""
        var giaPhong = 0
        var ngayHetHan: String? = null
        var ngayBatDau: String? = null
        var giam: Double? = null
        var dieuKienVeGia: Int? = null

        db.connect().use { conn ->
            val rows = conn.query(query)
            rows.forEach { row ->
                checkIn = row.get(1).toString()
                checkOut = row.get(2).toString()
                giaPhong = row.get(3).toString().toInt()
                ngayHetHan = row.get(5)?.toString()
                ngayBatDau = row.get(4)?.toString()
                giam = row.get(6)?.toString()?.toDouble() ?: 0.0
                dieuKienVeGia = row.get(7)?.toString()?.toInt() ?: 0

            }
        }

        giaThuePhong = giaPhong * tinhSoDem(checkIn, checkOut).toDouble()
        var thueVAT = giaThuePhong*0.1 // thue vat 10%
        var tienGiam:Double = 0.0


        var tongTien = giaThuePhong + thueVAT - tienGiam
        map["giaThuePhong"] = giaThuePhong.toInt()
        map["thueVAT"] = thueVAT.toInt()
        map["tienGiam"] = tienGiam.toInt()
        map["tongTien"] = tongTien.toInt()
        map["soDem"] = tinhSoDem(checkIn, checkOut)
        return map
    }
    fun tinhSoDem(checkIn: String, checkOut: String): Long {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val checkInDate = LocalDate.parse(formatDateTime2(checkIn), formatter)
        val checkOutDate = LocalDate.parse(formatDateTime2(checkOut), formatter)
        return ChronoUnit.DAYS.between(checkInDate, checkOutDate)
    }

    fun layDuLieuLoaiPhong(): ArrayList<LoaiPhong> {
        var ds = ArrayList<LoaiPhong>()
        db.connect().use { conn ->
            var sql = """
                Select * from LOAI_PHONG;
            """
            var rows = conn.query(sql)

            rows.forEach{ row ->
                ds.add(LoaiPhong(
                    row.get(0).toString().toInt(),
                    row.get(1).toString(),
                    row.get(2).toString().toInt(),
                    row.get(3).toString().toInt(),
                    row.get(4).toString(),
                    row.get(5).toString()
                ))
            }

        }
        return  ds
    }

    fun layDuLieuPhongKhongCoNguoiDat(checkIn: String, checkOut: String, maLoaiPhong: Int): ArrayList<Phong> {
        var ds = ArrayList<Phong>()
        db.connect().use {conn ->
            var sql = """
                select * from PHONG
                    where ma_phong not in 
                        (SELECT P.ma_phong
                            FROM PHONG AS P
                            LEFT JOIN THUE AS T ON P.ma_phong = T.ma_phong
                            LEFT JOIN DON AS D ON D.ma_don = T.ma_don
                            LEFT JOIN QUAN_LY AS Q ON Q.ma_don = D.ma_don
                            WHERE (
                                (D.check_in <= '$checkOut' AND T.check_out >= '$checkIn')
                                OR
                                (Q.check_in_thuc_te <= '$checkOut' AND Q.check_out_thuc_te >= '$checkIn')
                                ) 
                            group by P.ma_phong)
                        And ma_loai_phong = $maLoaiPhong;
            """
            var rows = conn.query(sql).forEach{ row ->
                ds.add(Phong(
                    row.get(0).toString().toInt(),
                    row.get(1).toString(),
                    row.get(2).toString().toInt()
                ))
            }
        }
        return ds
    }

    fun layDuLieuCacAnhCuaLoaiPhong(maLoaiPhong: Int): ArrayList<String>{
        var ds = ArrayList<String>()
        db.connect().use {conn ->
            var sql = """
                        SELECT hinh from LOAI_PHONG AS L
                            JOIN CHI_TIET_LOAI_PHONG AS C ON L.ma_loai_phong = C.ma_loai_phong
                            WHERE L.ma_loai_phong = $maLoaiPhong;
            """
            var rows = conn.query(sql).forEach{ row ->
                ds.add(
                    row.get(0).toString()
                )
            }
        }
        return ds
    }

    fun traVeLoaiPhongTuMaDon(ma_don: Int): String {
        var ds = ""
        db.connect().use {conn ->
            var sql = """
                select ten from PHONG as P
                    join LOAI_PHONG as L on P.ma_loai_phong = L.ma_loai_phong
                    join THUE as D on D.ma_phong = P.ma_phong
                    where D.ma_don = $ma_don
            """
            var rows = conn.query(sql).forEach{ row ->
                ds = row.get(0).toString()
            }
        }
        return ds
    }
    fun layDuLieuDonCuaUser(id: Int): ArrayList<Don> {
        var ds = ArrayList<Don>()
        db.connect().use {conn ->
            var sql = """
                    SELECT * from DON
                        WHERE ma_nguoi_dat = $id
            """
            var rows = conn.query(sql).forEach{ row ->
                ds.add(Don(
                    row.get(0).toString().toInt(),
                    parseDate(row.get(1).toString()),
                    row.get(2).toString().toInt(),
                    parseDate(row.get(3).toString())
                ))
            }
        }
        return ds
    }


    fun parseDate(dateStr: String): Date? {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return dateFormat.parse(dateStr)
    }
    fun getThongKe(): ArrayList<RoomRating> {
        var arr = ArrayList<RoomRating>()
        db.connect().use { conn ->
            val sql = """
                SELECT 
                    L.ma_loai_phong, 
                    L.ten, 
                    COALESCE(AVG(DG.danh_gia_sach_se), 0) AS avg_danh_gia_sach_se,
                    COALESCE(AVG(DG.danh_gia_chat_luong_phong), 0) AS avg_danh_gia_chat_luong_phong,
                    COALESCE(AVG(DG.danh_gia_nhan_vien_phuc_vu), 0) AS avg_danh_gia_nhan_vien_phuc_vu,
                    COALESCE(AVG(DG.danh_gia_tien_nghi), 0) AS avg_danh_gia_tien_nghi
                FROM CHI_TIET_DANH_GIA AS DG
                RIGHT JOIN DON AS D ON D.ma_don = DG.ma_don
                JOIN THUE AS T ON D.ma_don = T.ma_don
                JOIN PHONG AS P ON P.ma_phong = T.ma_phong
                Right JOIN LOAI_PHONG AS L ON L.ma_loai_phong = P.ma_loai_phong
                GROUP BY L.ma_loai_phong, L.ten;
            """
            val rows = conn.query(sql)
            rows.forEach { row ->
                arr.add(
                    RoomRating(
                        row.get(0).toString(),
                        row.get(1).toString(),
                        row.get(2).toString().toFloat(),
                        row.get(3).toString().toFloat(),
                        row.get(4).toString().toFloat(),
                        row.get(5).toString().toFloat()
                    )
                )
            }
        }
        return arr
    }

    fun layDuLieuTienNghi(maLoaiPhong: Int): ArrayList<TienNghi> {
        val dsTienNghi = ArrayList<TienNghi>()
        db.connect().use { conn ->
            val sql = """
            SELECT TN.ma_tien_nghi, TN.ten_tien_nghi, TN.ic_mo_ta
            FROM LOAI_PHONG AS L
            JOIN CO_TIEN_NGHI AS C ON L.ma_loai_phong = C.ma_loai_phong
            JOIN TIEN_NGHI AS TN ON C.ma_tien_nghi = TN.ma_tien_nghi
            WHERE L.ma_loai_phong = $maLoaiPhong;
        """
            val rows = conn.query(sql)
            rows.forEach { row ->
                dsTienNghi.add(
                    TienNghi(
                        row.get(2).toString(),//hinh
                        row.get(1).toString(),// ten
                        row.get(0).toString().toInt() //ma
                    )
                )
            }
        }
        return dsTienNghi
    }

    
fun layDuLieuPhongCoNguoiDatTuMaPhong( maLoaiPhong: Int): ArrayList<HashMap<String, Any>> {
    val ds = ArrayList<HashMap<String, Any>>()

    db.connect().use { conn ->
        val sql = """
       SELECT 
            P.ma_phong, 
            P.vi_tri, 
            P.ma_loai_phong, 
            D.ma_don, 
            D.check_in, 
            T.check_out, 
            N.name, 
            N.id,
            N.sdt,
            N.cccd
        FROM PHONG AS P
        LEFT JOIN THUE AS T ON P.ma_phong = T.ma_phong
        LEFT JOIN DON AS D ON D.ma_don = T.ma_don
        LEFT JOIN TAI_KHOAN AS N ON N.id = D.ma_nguoi_dat
        LEFT JOIN QUAN_LY AS QL ON QL.ma_don = D.ma_don 
        WHERE 
            T.ma_don IS NOT NULL
            AND D.ma_don IS NOT NULL
            AND (QL.ma_don IS NULL OR QL.is_thanh_toan = 0)
        AND P.ma_loai_phong = $maLoaiPhong
        GROUP BY P.ma_phong;

        """

        val rows = conn.query(sql)
        rows.forEach { row ->
            val resultMap = HashMap<String, Any>()

            val maPhong = row.get(0).toString().toInt()
            val viTri = row.get(1).toString()
            val maLoaiPhong = row.get(2).toString().toInt()
            val maDon = row.get(3).toString()
            val checkInDb = row.get(4).toString()
            val checkOutDb = row.get(5).toString()
            val name = row.get(6).toString()
            val id = row.get(7).toString()
            val sdt = row.get(8).toString()
            val cccd = row.get(9).toString()

            resultMap["ma_phong"] = maPhong
            resultMap["vi_tri"] = viTri
            resultMap["ma_loai_phong"] = maLoaiPhong
            resultMap["ma_don"] = maDon
            resultMap["check_in"] = checkInDb
            resultMap["check_out"] = checkOutDb
            resultMap["name"] = name
            resultMap["id"] = id
            resultMap["sdt"] = sdt
            resultMap["cccd"] = cccd

            ds.add(resultMap)
        }
    }

    return ds
}


fun layDuLieuPhongCoNguoiDat(): ArrayList<HashMap<String, Any>> {
    val ds = ArrayList<HashMap<String, Any>>()

    db.connect().use { conn ->
        val sql = """
        SELECT 
            P.ma_phong, 
            P.vi_tri, 
            P.ma_loai_phong, 
            D.ma_don, 
            D.check_in,
            T.check_out,
            N.name, 
            N.id,
            N.sdt,
            N.cccd
        FROM PHONG AS P
        LEFT JOIN THUE AS T ON P.ma_phong = T.ma_phong
        LEFT JOIN DON AS D ON D.ma_don = T.ma_don
        LEFT JOIN TAI_KHOAN AS N ON N.id = D.ma_nguoi_dat
        LEFT JOIN QUAN_LY AS QL ON QL.ma_don = D.ma_don 
        WHERE 
        T.ma_don IS NOT NULL
        AND D.ma_don IS NOT NULL
        AND (QL.ma_don IS NULL OR QL.is_thanh_toan = 0)
        GROUP BY P.ma_phong;
        """

        conn.query(sql).forEach { row ->
            val maPhong = row.get(0).toString().toInt()
            val viTri = row.get(1).toString()
            val maLoaiPhong = row.get(2).toString().toInt()
            val maDon = row.get(3).toString()
            val checkInDb = row.get(4).toString()
            val checkOutDb = row.get(5).toString()
            val name = row.get(6).toString()
            val id = row.get(7).toString()
            val sdt = row.get(8).toString()
            val cccd = row.get(9).toString()

            val resultMap = HashMap<String, Any>().apply {
                put("ma_phong", maPhong)
                put("vi_tri", viTri)
                put("ma_loai_phong", maLoaiPhong)
                put("ma_don", maDon)
                put("check_in", checkInDb)
                put("check_out", checkOutDb)
                put("name", name)
                put("id", id)
                put("sdt", sdt)
                put("cccd", cccd)
            }

            ds.add(resultMap)
        }
    }

    return ds
}

    fun layDuLieuPhongDaThanhToan(): ArrayList<HashMap<String, Any>> {
        val ds = ArrayList<HashMap<String, Any>>()

        db.connect().use { conn ->
            val sql = """
        SELECT 
            P.ma_phong, 
            P.vi_tri, 
            P.ma_loai_phong, 
            D.ma_don, 
            D.check_in,
            T.check_out,
            N.name, 
            N.id,
            N.sdt,
            N.cccd
        FROM PHONG AS P
        LEFT JOIN THUE AS T ON P.ma_phong = T.ma_phong
        LEFT JOIN DON AS D ON D.ma_don = T.ma_don
        LEFT JOIN TAI_KHOAN AS N ON N.id = D.ma_nguoi_dat
        LEFT JOIN QUAN_LY AS QL ON QL.ma_don = D.ma_don 
        WHERE 
        T.ma_don IS NOT NULL
        AND D.ma_don IS NOT NULL
        AND QL.is_thanh_toan = 1
        GROUP BY P.ma_phong;
        """

            conn.query(sql).forEach { row ->
                val maPhong = row.get(0).toString().toInt()
                val viTri = row.get(1).toString()
                val maLoaiPhong = row.get(2).toString().toInt()
                val maDon = row.get(3).toString()
                val checkInDb = row.get(4).toString()
                val checkOutDb = row.get(5).toString()
                val name = row.get(6).toString()
                val id = row.get(7).toString()
                val sdt = row.get(8).toString()
                val cccd = row.get(9).toString()

                val resultMap = HashMap<String, Any>().apply {
                    put("ma_phong", maPhong)
                    put("vi_tri", viTri)
                    put("ma_loai_phong", maLoaiPhong)
                    put("ma_don", maDon)
                    put("check_in", checkInDb)
                    put("check_out", checkOutDb)
                    put("name", name)
                    put("id", id)
                    put("sdt", sdt)
                    put("cccd", cccd)
                }

                ds.add(resultMap)
            }
        }

        return ds
    }

    fun layDuLieuTienCuaPhongDo(ma_don: Int): String {
        var ds = ""
        db.connect().use {conn ->
            var sql = """
                    SELECT * from HOA_DON
                        WHERE ma_don = $ma_don
            """
            var rows = conn.query(sql).forEach{ row ->
                ds = row.get(3).toString()
            }
        }
        return ds
    }

    fun updateDataHoaDon(ma_hoa_don:Int, ma_don: Int, ngay_thanh_toan: String): String {
        return try {
            var tien: Any? = tinhGiaPhong(ma_don).get("tongTien")
            val sql = """
            UPDATE HOA_DON
            SET ngay_thanh_toan = '$ngay_thanh_toan', so_tien_thanh_toan = $tien
            WHERE ma_hoa_don = $ma_hoa_don;
        """
            db.connect().execute(sql)
            "Thêm thành công"
        } catch (e: Exception) {
            "No 1: ${e} }"
        }
    }

    fun insertDataQuanLyNgayBatDau(ma_nhan_vien: Int, ma_don: Int, check_in_thuc_te: String, dat_phong_fee: String, tra_phong_fee: String): String {
        return try {

            var sql = """
            INSERT INTO QUAN_LY (ma_nhan_vien, ma_don, is_thanh_toan, is_duyet,check_in_thuc_te, check_in_fee, check_out_fee)
            VALUES ($ma_nhan_vien, '$ma_don', '0', '0', '$check_in_thuc_te', '$dat_phong_fee', '$tra_phong_fee');
        """
            db.connect().execute(sql)
            "Thêm thành công"
        } catch (e: Exception) {
            "No 2: ${e.message}"
        }
    }

    fun insertDataQuanLyNCaHai(ma_nhan_vien: Int, ma_don: Int, check_in_thuc_te: String, check_out_thuc_te:String, dat_phong_fee: String, tra_phong_fee: String): String {
        return try {
            var sql = """
            INSERT INTO QUAN_LY (ma_nhan_vien, ma_don, is_thanh_toan, is_duyet,check_in_thuc_te,check_out_thuc_te, check_in_fee, check_out_fee)
            VALUES ($ma_nhan_vien, '$ma_don', '1', '1', '$check_in_thuc_te' , '$check_out_thuc_te', '$dat_phong_fee', '$tra_phong_fee');
        """
            db.connect().execute(sql)
            "Thêm thành công"
        } catch (e: Exception) {
            "No 3: ${e.message}"
        }
    }

    fun updateThoigianCheckInThuc(ma_don: Int, check_in_thuc_te: String): String {
        return try {
            val sql = """
            UPDATE QUAN_LY
            SET check_in_thuc_te = '$check_in_thuc_te',
                is_thanh_toan = '0',
                is_duyet = '0'
            WHERE ma_don = $ma_don;
        """
            db.connect().execute(sql)
            "Cập nhật thành công"
        } catch (e: Exception) {
            "No 4: ${e.message}"
        }
    }


    fun insertDataCheckOutThuc(ma_don: Int, check_in_thuc_te: String, check_out_thuc_te: String): String {
        return try {
            val sql = """
            UPDATE QUAN_LY
            SET is_thanh_toan = '1',
                is_duyet = '1',
                check_in_thuc_te = '$check_in_thuc_te',
                check_out_thuc_te = '$check_out_thuc_te'
            WHERE ma_don = $ma_don;
        """
            db.connect().execute(sql)
            "Thêm thành công"
        } catch (e: Exception) {
            "No 5: ${e.message}"
        }
    }

    fun kiemTraThanhToan(ma_don: Int) : String {
        return try {
            var hoa_don = -1
            db.connect().use { conn ->
                val sql = """
            SELECT is_thanh_toan FROM QUAN_LY
            WHERE ma_don = $ma_don
            """
                conn.query(sql).forEach { row ->
                    val rowMaDon = row.get(0).toString()
                    if (rowMaDon.isNotEmpty()) {
                        hoa_don = rowMaDon.toIntOrNull() ?: 0
                    }
                }
            }
            hoa_don.toString()
        } catch (e: Exception) {
            "Lỗi B: ${e.message}"
        }
    }

    fun hamlaymadon(ma_don: Int): String {
        return try {
            var ma_hoa_don = -1
            db.connect().use { conn ->
                val sql = """
            SELECT is_duyet FROM QUAN_LY
            WHERE ma_don = $ma_don
            """
                conn.query(sql).forEach { row ->
                    val rowMaDon = row.get(0).toString()
                    if (rowMaDon.isNotEmpty()) {
                        ma_hoa_don = rowMaDon.toIntOrNull() ?: -1
                    }
                }
            }
            ma_hoa_don.toString()
        } catch (e: Exception) {
            "Lỗi B: ${e.message}"
        }
    }


    fun hamlaycheckinthuc(ma_don: Int): String {
        return try {
            var check_in_thuc_te = "-1"
            db.connect().use { conn ->
                val sql = """
                SELECT check_in_thuc_te FROM QUAN_LY
                WHERE ma_don = $ma_don
            """
                conn.query(sql).forEach { row ->
                    val rowCheckIn = row.get(0).toString()
                    if (rowCheckIn.isNotEmpty()) {
                        check_in_thuc_te = rowCheckIn
                    }
                }
            }
            check_in_thuc_te
        } catch (e: Exception) {
            "No 6: ${e.message}"
        }
    }


    fun hamlaycheckoutthuc(ma_don: Int): String {
        return try {
            var check_out_thuc_te = "-1" // Giá trị mặc định nếu không tìm thấy
            db.connect().use { conn ->
                val sql = """
                SELECT check_out_thuc_te FROM QUAN_LY
                WHERE ma_don = $ma_don
            """
                conn.query(sql).forEach { row ->
                    // Kiểm tra dữ liệu tồn tại và lấy giá trị cột đúng
                    val rowValue = row.get(0)?.toString()
                    if (!rowValue.isNullOrEmpty()) {
                        check_out_thuc_te = rowValue
                    }
                }
            }
            check_out_thuc_te // Trả về giá trị lấy được
        } catch (e: Exception) {
            "No 7: ${e.message}" // Trả về thông báo lỗi nếu có ngoại lệ
        }
    }

    fun layDuLieuPhong(ma_don: Int): ArrayList<HashMap<String, Any>> {
        val ds = ArrayList<HashMap<String, Any>>()

        db.connect().use { conn ->
            val sql = """
        SELECT 
            P.ma_phong, 
            P.vi_tri, 
            P.ma_loai_phong, 
            D.ma_don, 
            D.check_in,
            T.check_out,
            N.name, 
            N.id,
            N.sdt,
            N.cccd
        FROM PHONG AS P
        LEFT JOIN THUE AS T ON P.ma_phong = T.ma_phong
        LEFT JOIN DON AS D ON D.ma_don = T.ma_don
        LEFT JOIN TAI_KHOAN AS N ON N.id = D.ma_nguoi_dat
        LEFT JOIN QUAN_LY AS QL ON QL.ma_don = D.ma_don 
        WHERE 
        T.ma_don IS NOT NULL
        AND D.ma_don IS NOT NULL
        AND (QL.ma_don IS NULL)
        AND D.ma_don = $ma_don
        GROUP BY P.ma_phong;
        """

            conn.query(sql).forEach { row ->
                val maPhong = row.get(0).toString().toInt()
                val viTri = row.get(1).toString()
                val maLoaiPhong = row.get(2).toString().toInt()
                val maDon = row.get(3).toString()
                val checkInDb = row.get(4).toString()
                val checkOutDb = row.get(5).toString()
                val name = row.get(6).toString()
                val id = row.get(7).toString()
                val sdt = row.get(8).toString()
                val cccd = row.get(9).toString()

                val resultMap = HashMap<String, Any>().apply {
                    put("ma_phong", maPhong)
                    put("vi_tri", viTri)
                    put("ma_loai_phong", maLoaiPhong)
                    put("ma_don", maDon)
                    put("check_in", checkInDb)
                    put("check_out", checkOutDb)
                    put("name", name)
                    put("id", id)
                    put("sdt", sdt)
                    put("cccd", cccd)
                }

                ds.add(resultMap)
            }
        }

        return ds
    }


}
