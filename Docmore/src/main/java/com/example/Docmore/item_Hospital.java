package com.example.Docmore;

public class item_Hospital {
    private String Hospital_name;
    private String Hospital_time;
    private String Hospital_address;
    private String Hospital_spec;

    public item_Hospital(String hospital_name, String hospital_time, String hospital_address, String hospital_spec, String hospital_contact) {
        Hospital_name = hospital_name;
        Hospital_time = hospital_time;
        Hospital_address = hospital_address;
        Hospital_spec = hospital_spec;
        Hospital_contact = hospital_contact;
    }

    public String getHospital_name() {
        return Hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        Hospital_name = hospital_name;
    }

    public String getHospital_time() {
        return Hospital_time;
    }

    public void setHospital_time(String hospital_time) {
        Hospital_time = hospital_time;
    }

    public String getHospital_address() {
        return Hospital_address;
    }

    public void setHospital_address(String hospital_address) {
        Hospital_address = hospital_address;
    }

    public String getHospital_spec() {
        return Hospital_spec;
    }

    public void setHospital_spec(String hospital_spec) {
        Hospital_spec = hospital_spec;
    }

    public String getHospital_contact() {
        return Hospital_contact;
    }

    public void setHospital_contact(String hospital_contact) {
        Hospital_contact = hospital_contact;
    }

    private String Hospital_contact;


}
