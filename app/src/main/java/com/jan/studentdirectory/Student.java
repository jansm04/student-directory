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
    @SerializedName("image")
    String image;

    public Student(String name, int studentId, String address, double latitude, double longitude, String phone, String image) {
        this.name = name;
        this.studentId = studentId;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
        this.image = image;
    }

    public String getName() {
        return name;
    }
    public int getStudentId() {
        return studentId;
    }
    public String getAddress() {
        return address;
    }
    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public String getPhone() {
        return phone;
    }
    public String getImage() { return image; }
    public void setName(String name) {
        this.name = name;
    }
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setImage(String image) { this.image = image; }
}
