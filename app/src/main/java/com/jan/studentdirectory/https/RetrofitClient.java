package com.jan.studentdirectory.https;

import com.jan.studentdirectory.exceptions.InvalidUrlException;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl, OkHttpClient client) throws InvalidUrlException {
        if (retrofit == null) {
            try {
                retrofit = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)
                        .build();
            } catch (RuntimeException e) {
                throw new InvalidUrlException("Invalid URL provided to Retrofit Builder.");
            }
        }
        return retrofit;
    }
}
