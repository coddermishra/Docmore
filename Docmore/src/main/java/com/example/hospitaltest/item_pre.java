package com.example.hospitaltest;

public class item_pre {
    private String Doctor_name;
    private String Time;
    private String Prescription;

    public item_pre(String doctor_name, String time, String prescription) {
        Doctor_name = doctor_name;
        Time = time;
        Prescription = prescription;
    }

    public String getDoctor_name() {
        return Doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        Doctor_name = doctor_name;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getPrescription() {
        return Prescription;
    }

    public void setPrescription(String prescription) {
        Prescription = prescription;
    }
}
