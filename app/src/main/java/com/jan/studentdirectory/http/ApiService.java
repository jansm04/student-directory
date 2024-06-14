package com.jan.studentdirectory.http;

import com.jan.studentdirectory.Student;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @GET("api.php")
    Call<List<Student>> getStudents();

    @POST("api2.php")
    Call<Void> postData(@Body List<Student> students);
}
