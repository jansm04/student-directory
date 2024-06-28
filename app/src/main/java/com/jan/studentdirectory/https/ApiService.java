package com.jan.studentdirectory.https;

import com.jan.studentdirectory.model.Student;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @GET("api3.php")
    Call<List<Student>> getStudents();

    @POST("api2.php")
    Call<Void> postStudents(@Body List<Student> students);
}
