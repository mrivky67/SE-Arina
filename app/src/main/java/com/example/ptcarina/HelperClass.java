package com.example.ptcarina;

public class HelperClass {
    String sensor, umur;

    public HelperClass(String sensor, String umur) {
        this.sensor = sensor;
        this.umur = umur;
    }

    public String getUmur() {
        return umur;
    }

    public void setUmur(String umur) {
        this.umur = umur;
    }

    public String getSensor() {
        return sensor;
    }

    public void getsSenos(String nama) {
        this.sensor = nama;
    }
}