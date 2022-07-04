package com.example.calculator.DTO;

public class History {
    private int id;
    private String phepTinh;
    private String ketQua;

    public History(int id, String phepTinh, String ketQua) {
        this.id = id;
        this.phepTinh = phepTinh;
        this.ketQua = ketQua;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhepTinh() {
        return phepTinh;
    }

    public void setPhepTinh(String phepTinh) {
        this.phepTinh = phepTinh;
    }

    public String getKetQua() {
        return ketQua;
    }

    public void setKetQua(String ketQua) {
        this.ketQua = ketQua;
    }

}
