package com.example.quizzyvoote.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizzyvoote.R;
import com.example.quizzyvoote.classes.Storage;
import com.example.quizzyvoote.classes.api_NetworkService;
import com.example.quizzyvoote.classes.api_Tokens;
import com.example.quizzyvoote.classes.api_Users;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SigninActivity extends AppCompatActivity {

    EditText userName;
    EditText userPassword;
    Button btn_signin;
    SharedPreferences data;

    public final static String LOGIN = "LOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        // Открываем кнопку "Назад" в ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Вход");

        userName = (EditText) findViewById(R.id.input_username);
        userPassword = (EditText) findViewById(R.id.input_password);
        // Меняем фокус на поле "Имя пользователя"
        userName.requestFocus();

        userName.setText(getIntent().getStringExtra("LOGIN"));

        btn_signin = (Button) findViewById(R.id.btn_signin);
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userName.getText().toString().trim().length() != 0) {
                    if(userPassword.getText().toString().length() != 0) {
                        api_NetworkService.getInstance()
                                .getJSONApi()
                                .getUserByName(userName.getText().toString())
                                .enqueue(new Callback<api_Users>() {
                                    @Override
                                    public void onResponse(@NonNull Call<api_Users> call, @NonNull Response<api_Users> response) {
                                        api_Users post = response.body();

                                        if(post.getUser_password() != null) {
                                            String inputPass = userPassword.getText().toString();
                                            String requestPass = post.getUser_password();
                                            if(inputPass.equals(requestPass)) {

                                                //////////////////////////////////////////// CREATE TOKEN
                                                Date currentDate = new Date();
                                                api_Tokens token = new api_Tokens(post.getUser_id(), "", currentDate.getTime());
                                                api_NetworkService.getInstance()
                                                        .getJSONApi()
                                                        .authUser(token)
                                                        .enqueue(new Callback<api_Tokens>() {
                                                            @Override
                                                            public void onResponse(@NonNull Call<api_Tokens> call, @NonNull Response<api_Tokens> response) {
                                                                api_Tokens post = response.body();

                                                                Storage.init(SigninActivity.this);
                                                                Storage.addProperty("USER_ID", post.getUserID().toString());
                                                                Storage.addProperty("TOKEN", post.getToken());
                                                                Storage.addProperty("USERNAME", userName.getText().toString());

                                                                Intent intent = new Intent(SigninActivity.this, MainActionActivity.class);
                                                                startActivity(intent);

                                                            }
                                                            @Override
                                                            public void onFailure(@NonNull Call<api_Tokens> call, @NonNull Throwable t) {
                                                                Log.d("RES", "ERROR");
                                                                //Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
                                                                //textView.setText("Error occurred while getting request!");
                                                                t.printStackTrace();
                                                            }
                                                        });
                                            } else { userPassword.setError("Неверный пароль"); userPassword.requestFocus(); /*Toast.makeText(SigninActivity.this, "Введенный пароль: " + inputPass + "\nПароль с базы: " + post.getUser_password(), Toast.LENGTH_SHORT).show(); */ }
                                        } else { userName.setError("Пользователь не найден"); userName.requestFocus(); }

                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<api_Users> call, @NonNull Throwable t) {
                                        Log.d("RES", "ERROR");
                                        t.printStackTrace();
                                    }
                                });
                    } else { userPassword.setError("Пустое поле"); userPassword.requestFocus(); }
                } else { userName.setError("Пустое поле"); userName.requestFocus(); }

            }
        });
    }

    // При нажатии на кнопку "Назад" в ActionBar закрываем текущее окно
    @Override
    public void onBackPressed() {
        finish();
    }
}
