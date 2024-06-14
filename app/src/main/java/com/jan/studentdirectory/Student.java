package com.jan.studentdirectory;

import com.google.gson.annotations.SerializedName;

public class Student {

    @SerializedName("name")
    private final String name;
    @SerializedName("studentID")
    private final int studentId;
    @SerializedName("address")
    private final String address;
    @SerializedName("latitude")
    private final double latitude;
    @SerializedName("longitude")
    private final double longitude;
    @SerializedName("phone")
    private final String phone;
    @SerializedName("image")
    private final String image;
    @SerializedName("timestamp")
    private final String timestamp;

    public Student(String name, int studentId, String address, double latitude, double longitude, String phone, String image, String timestamp) {
        this.name = name;
        this.studentId = studentId;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
        this.image = image;
        this.timestamp = timestamp;
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
    public String getImage() {
        return image;
    }
}
