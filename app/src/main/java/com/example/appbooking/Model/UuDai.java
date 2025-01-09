package com.example.appbooking.Model;

import java.io.Serializable;
import java.util.Date;

public class UuDai implements Serializable {
    private int maNhanVien = -1;
    private int maUuDai = -1;
    private String tenMa = "";
    private Date ngayBatDau = null;
    private Date ngayHetHan = null;
    private double giam = 0.0;
    private int dieuKienVeGia = 0;

    // Constructor mặc định
    public UuDai() {}

    // Constructor với tất cả các tham số
    public UuDai(int maNhanVien, int maUuDai, String tenMa, Date ngayBatDau, Date ngayHetHan, double giam, int dieuKienVeGia) {
        setMaNhanVien(maNhanVien);
        setMaUuDai(maUuDai);
        setTenMa(tenMa);
        setNgayBatDau(ngayBatDau);
        setNgayHetHan(ngayHetHan);
        setGiam(giam);
        setDieuKienVeGia(dieuKienVeGia);
    }

    public int getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(int maNhanVien) {
        if (maNhanVien > 0) {
            this.maNhanVien = maNhanVien;
        } else {
            throw new IllegalArgumentException("Mã nhân viên phải lớn hơn 0");
        }
    }

    public int getMaUuDai() {
        return maUuDai;
    }

    public void setMaUuDai(int maUuDai) {
        if (maUuDai > 0) {
            this.maUuDai = maUuDai;
        } else {
            throw new IllegalArgumentException("Mã ưu đãi phải lớn hơn 0");
        }
    }

    public String getTenMa() {
        return tenMa;
    }

    public void setTenMa(String tenMa) {
        if (tenMa == null || tenMa.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên mã không được để trống");
        }
        this.tenMa = tenMa;
    }

    public Date getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(Date ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public Date getNgayHetHan() {
        return ngayHetHan;
    }

    public void setNgayHetHan(Date ngayHetHan) {
        if (ngayBatDau != null && ngayHetHan != null && ngayHetHan.before(ngayBatDau)) {
            throw new IllegalArgumentException("Ngày hết hạn phải sau ngày bắt đầu");
        }
        this.ngayHetHan = ngayHetHan;
    }

    public double getGiam() {
        return giam;
    }

    public void setGiam(double giam) {
        if (giam >= 0.0 && giam <= 100.0) {
            this.giam = giam;
        } else {
            throw new IllegalArgumentException("Mức giảm phải từ 0.0 đến 100.0");
        }
    }

    public int getDieuKienVeGia() {
        return dieuKienVeGia;
    }

    public void setDieuKienVeGia(int dieuKienVeGia) {
        if (dieuKienVeGia >= 0) {
            this.dieuKienVeGia = dieuKienVeGia;
        } else {
            throw new IllegalArgumentException("Điều kiện giá phải lớn hơn hoặc bằng 0");
        }
    }

    @Override
    public String toString() {
        return "UuDai{" +
                "maNhanVien=" + maNhanVien +
                ", maUuDai=" + maUuDai +
                ", tenMa='" + tenMa + '\'' +
                ", ngayBatDau=" + ngayBatDau +
                ", ngayHetHan=" + ngayHetHan +
                ", giam=" + giam +
                ", dieuKienVeGia=" + dieuKienVeGia +
                '}';
    }
}
