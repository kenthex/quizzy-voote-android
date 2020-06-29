package com.example.quizzyvoote.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.quizzyvoote.R;
import com.example.quizzyvoote.classes.Storage;
import com.example.quizzyvoote.classes.api_NetworkService;
import com.example.quizzyvoote.classes.api_Tokens;
import com.example.quizzyvoote.classes.api_Users;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    Button btn_signin;
    Button btn_signup;
    TextView textView;
    SharedPreferences data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_signin = (Button) findViewById(R.id.btn_signin);
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SigninActivity.class);
                startActivity(intent);
            }
        });

        btn_signup = (Button) findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        Storage.init(MainActivity.this);
        String token = Storage.getProperty("TOKEN");

        //Toast.makeText(MainActivity.this, "TOKEN: " + token, Toast.LENGTH_SHORT).show();

        if(token != "" && token != null) {

            api_NetworkService.getInstance()
                    .getJSONApi()
                    .getToken(token)
                    .enqueue(new Callback<api_Tokens>() {
                        @Override
                        public void onResponse(@NonNull Call<api_Tokens> call, @NonNull Response<api_Tokens> response) {
                            api_Tokens post = response.body();

                            long currentDate = new Date().getTime();
                            long expiredAt = post.getExpiredAt();

                            Storage.addProperty("TOKEN", post.getToken());
                            //
                            if(expiredAt > currentDate) {

                                //Toast.makeText(MainActivity.this, "TOKEN OK: " + Storage.getProperty("TOKEN"), Toast.LENGTH_SHORT).show();
                                /// УДАЛЯЕМ ТОКЕН
                                api_NetworkService.getInstance()
                                        .getJSONApi()
                                        .delToken(post.getToken())
                                        .enqueue(new Callback<api_Tokens>() {
                                            @Override
                                            public void onResponse(@NonNull Call<api_Tokens> call, @NonNull Response<api_Tokens> response) {
                                                api_Tokens post = response.body();
                                            }
                                            @Override
                                            public void onFailure(@NonNull Call<api_Tokens> call, @NonNull Throwable t) {
                                                Log.d("RES", "ERROR_1");
                                                t.printStackTrace();
                                            }
                                        });
//                                /// СОЗДАЕМ НОВЫЙ
                                /// ЛОГИНИМ
                                api_Tokens token = new api_Tokens(post.getUserID(), "", new Date().getTime());
                                api_NetworkService.getInstance()
                                        .getJSONApi()
                                        .authUser(token)
                                        .enqueue(new Callback<api_Tokens>() {
                                            @Override
                                            public void onResponse(@NonNull Call<api_Tokens> call, @NonNull Response<api_Tokens> response) {
                                                api_Tokens post = response.body();

                                                Storage.init(MainActivity.this);
                                                Storage.addProperty("TOKEN", post.getToken());
                                                Storage.addProperty("USER_ID", post.getUserID().toString());


                                                api_NetworkService.getInstance()
                                                        .getJSONApi()
                                                        .getUserByID(post.getUserID())
                                                        .enqueue(new Callback<api_Users>() {
                                                            @Override
                                                            public void onResponse(@NonNull Call<api_Users> call, @NonNull Response<api_Users> response) {
                                                                api_Users post = response.body();
                                                                Storage.addProperty("USERNAME", post.getUser_login());
                                                            }
                                                            @Override
                                                            public void onFailure(@NonNull Call<api_Users> call, @NonNull Throwable t) {
                                                                Log.d("RES", "ERROR_1");
                                                                t.printStackTrace();
                                                            }
                                                        });

                                                //Toast.makeText(MainActivity.this, "TOKEN OK: " + Storage.getProperty("TOKEN"), Toast.LENGTH_SHORT).show();
                                                //Toast.makeText(MainActivity.this, "ID: " + Storage.getProperty("USER_ID"), Toast.LENGTH_SHORT).show();
                                                 Intent intent = new Intent(MainActivity.this, MainActionActivity.class);
                                                 startActivity(intent);
                                            }
                                            @Override
                                            public void onFailure(@NonNull Call<api_Tokens> call, @NonNull Throwable t) {
                                                Log.d("RES", "ERROR");
                                                t.printStackTrace();
                                            }
                                        });

                            } else {
                                /// ВРЕМЯ ТОКЕНА ВЫШЛО, УДАЛЯЕМ
                                //Toast.makeText(MainActivity.this, "TOKEN EXPIRED: " + Storage.getProperty("TOKEN"), Toast.LENGTH_SHORT).show();
                                api_NetworkService.getInstance()
                                        .getJSONApi()
                                        .delToken(post.getToken())
                                        .enqueue(new Callback<api_Tokens>() {
                                            @Override
                                            public void onResponse(@NonNull Call<api_Tokens> call, @NonNull Response<api_Tokens> response) {
                                                api_Tokens post = response.body();
                                                Storage.addProperty("TOKEN", null);
                                            }
                                            @Override
                                            public void onFailure(@NonNull Call<api_Tokens> call, @NonNull Throwable t) {
                                                Log.d("RES", "ERROR_2");
                                                t.printStackTrace();
                                            }
                                        });
                            }
                        }
                        @Override
                        public void onFailure(@NonNull Call<api_Tokens> call, @NonNull Throwable t) {
                            Log.d("RES", "ERROR_3");
                            t.printStackTrace();
                        }
                    });
        } else { /*Toast.makeText(MainActivity.this, "NO TOKEN", Toast.LENGTH_SHORT).show();*/ }

    }
}
