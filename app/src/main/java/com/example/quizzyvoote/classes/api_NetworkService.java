package com.example.quizzyvoote.classes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class api_NetworkService {
    private static api_NetworkService mInstance;
    //private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private static final String BASE_URL = "http://quizzy-voote.herokuapp.com/";
    private Retrofit mRetrofit;
    private Gson gson;

    private api_NetworkService() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(interceptor);

        gson = new GsonBuilder()
                .setLenient()
                .create();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static api_NetworkService getInstance() {
        if (mInstance == null) {
            mInstance = new api_NetworkService();
        }
        return mInstance;
    }

    public api_JSONPlaceHolder getJSONApi() {
        return mRetrofit.create(api_JSONPlaceHolder.class);
    }

}
