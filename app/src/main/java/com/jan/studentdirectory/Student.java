package com.jan.studentdirectory;

import com.google.gson.annotations.SerializedName;

public class Student {

    @SerializedName("name")
    String name;
    @SerializedName("studentID")
    int studentId;
    @SerializedName("address")
    String address;
    @SerializedName("latitude")
    double latitude;
    @SerializedName("longitude")
    double longitude;
    @SerializedName("phone")
    String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
