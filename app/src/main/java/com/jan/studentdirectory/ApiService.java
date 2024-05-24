package com.jan.studentdirectory;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("api.txt")
    Call<List<Student>> getStudents();
}
