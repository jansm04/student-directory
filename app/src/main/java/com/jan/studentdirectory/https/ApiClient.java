package com.jan.studentdirectory.https;

import android.text.TextUtils;

import com.jan.studentdirectory.properties.Properties;
import com.jan.studentdirectory.exceptions.InvalidCredentialsException;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;

public class ApiClient {

    private static final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static ApiService apiService;

    public static ApiService createService(String username, String password) throws InvalidCredentialsException {
        if (TextUtils.isEmpty(username)) {
            throw new InvalidCredentialsException("Username must not be empty.");
        }
        if (TextUtils.isEmpty(password)) {
            throw new InvalidCredentialsException("Password must not be empty");
        }
        String authToken = Credentials.basic(username, password);
        return createService(authToken);
    }

    public static ApiService createService(final String authToken) {
        if (!TextUtils.isEmpty(authToken)) {
            AuthenticationInterceptor interceptor = new AuthenticationInterceptor(authToken);
            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);
                if (apiService == null) {
                    apiService = RetrofitClient.getClient(Properties.BASE_URL, httpClient.build()).create(ApiService.class);
                }
            }
        }
        return apiService;
    }
}
