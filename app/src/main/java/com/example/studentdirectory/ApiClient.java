package com.example.studentdirectory;

public class ApiClient {
    private static final String BASE_URL = "https://static0app.z13.web.core.windows.net/";

    private static ApiService apiService;

    public static ApiService getApiService() {
        if (apiService == null) {
            apiService = RetrofitClient.getClient(BASE_URL).create(ApiService.class);
        }
        return apiService;
    }
}
