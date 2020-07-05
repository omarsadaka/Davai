package com.technosaab.newdavai.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by manar on 1/15/2018.
 */

public class ApiClient {
    private static String BASE_URL_USER = "http://104.248.175.110/api/user/";
    private static String BASE_URL_CLIENT = "http://davai.online/api/client/";
    private static Retrofit retrofit = null;
    private static Retrofit retrofit2 = null;
    public static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder().readTimeout(500, TimeUnit.SECONDS)
                    .connectTimeout(500, TimeUnit.SECONDS)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            String v = bodyToString(request.body());

                            return chain.proceed(request);
                        }
                    });
    private static Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    public static Retrofit retrofitUser = new Retrofit.Builder()
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BASE_URL_USER)
            .build();
    public static Retrofit retrofitGoogle = new Retrofit.Builder()
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BASE_URL_USER)
            .build();

    private static String bodyToString(final RequestBody request) {
        try {
            final Buffer buffer = new Buffer();
            if (request != null)
                request.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL_USER)
                    .build();
        }
        return retrofit;
    }
    public static Retrofit getClient2() {
        if (retrofit2==null) {
            retrofit2 = new Retrofit.Builder()
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL_CLIENT)
                    .build();
        }
        return retrofit2;
    }
}
