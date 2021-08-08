package com.example.Docmore;
public class ExampleItem {
    private int mImageResource;
    private String Doc_name;
    private String Doc_spec;
    private String Doc_time;
    private String Doc_num;
    private String Hospital_name;
    private String Hospital_address;
    private String Hospital_time;

    public String getDoc_uid() {
        return Doc_uid;
    }

    public void setDoc_uid(String doc_uid) {
        Doc_uid = doc_uid;
    }

    private String Doc_uid;

    public String getHospital_spec() {
        return Hospital_spec;
    }

    public void setHospital_spec(String hospital_spec) {
        Hospital_spec = hospital_spec;
    }

    private String Hospital_spec;

    public String getHospital_name() {
        return Hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        Hospital_name = hospital_name;
    }

    public String getHospital_address() {
        return Hospital_address;
    }

    public void setHospital_address(String hospital_address) {
        Hospital_address = hospital_address;
    }

    public String getHospital_time() {
        return Hospital_time;
    }

    public void setHospital_time(String hospital_time) {
        Hospital_time = hospital_time;
    }

    public String getHospital_number() {
        return Hospital_number;
    }

    public void setHospital_number(String hospital_number) {
        Hospital_number = hospital_number;
    }

    private String Hospital_number;

    public ExampleItem(String text1,String text2,String text3,String text4,String text5) {
        Doc_name=text1;
        Doc_spec=text2;
        Doc_time=text3;
        Doc_num=text4;
        Doc_uid=text5;
    }

    public int getmImageResource() {
        return mImageResource;
    }

    public void setmImageResource(int mImageResource) {
        this.mImageResource = mImageResource;
    }

    public String getDoc_name() {
        return Doc_name;
    }

    public void setDoc_name(String doc_name) {
        Doc_name = doc_name;
    }

    public String getDoc_spec() {
        return Doc_spec;
    }

    public void setDoc_spec(String doc_spec) {
        Doc_spec = doc_spec;
    }

    public String getDoc_time() {
        return Doc_time;
    }

    public void setDoc_time(String doc_time) {
        Doc_time = doc_time;
    }

    public String getDoc_num() {
        return Doc_num;
    }

    public void setDoc_num(String doc_num) {
        Doc_num = doc_num;
    }
}