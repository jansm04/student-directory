package com.jan.studentdirectory;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("api.php")
    Call<List<Student>> getStudents();
}
