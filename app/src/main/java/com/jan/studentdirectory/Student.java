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
}
