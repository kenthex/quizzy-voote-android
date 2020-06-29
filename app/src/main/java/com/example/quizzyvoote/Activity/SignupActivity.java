package com.example.quizzyvoote.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizzyvoote.R;
import com.example.quizzyvoote.classes.api_NetworkService;
import com.example.quizzyvoote.classes.api_Users;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    EditText userName,
             userEmail,
             userPass,
             userRePass;
    Button signUP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Открываем кнопку "Назад" в ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setTitle("Регистрация");

        // Меняем фокус на поле "Имя пользователя"
        userName = (EditText) findViewById(R.id.input_username);
        userName.requestFocus();

        userEmail = (EditText) findViewById(R.id.input_email);
        userPass = (EditText) findViewById(R.id.input_password);
        userRePass = (EditText) findViewById(R.id.input_password_check);

        signUP = (Button) findViewById(R.id.btn_signin);
        signUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name, email, pass, repass;
                name = userName.getText().toString();
                email = userEmail.getText().toString();
                pass = userPass.getText().toString();
                repass = userRePass.getText().toString();

                api_Users newUser = new api_Users(name, email, pass, repass);
                api_NetworkService.getInstance()
                                .getJSONApi()
                                .createUser(newUser)
                                .enqueue(new Callback<api_Users>() {
                                    @Override
                                    public void onResponse(@NonNull Call<api_Users> call, @NonNull Response<api_Users> response) {
                                        api_Users post = response.body();

                                        if(post.getError() == null) {
                                            Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
                                            if(post.getUser_login() != null)
                                                intent.putExtra(SigninActivity.LOGIN, post.getUser_login());
                                            startActivity(intent);
                                        } else {
                                            String errorMes = post.getError();
                                            switch(errorMes) {
                                                case "Validation is on login failed":
                                                    //Toast.makeText(SignupActivity.this, "Неверный формат логина", Toast.LENGTH_SHORT).show();
                                                    userName.setError("Неверный формат");
                                                    userName.requestFocus();
                                                    break;
                                                case "Validation len on login failed":
                                                    //Toast.makeText(SignupActivity.this, "Неверная длина логина", Toast.LENGTH_SHORT).show();
                                                    userName.setError("Неверная длина");
                                                    userName.requestFocus();
                                                    break;
                                                case "login must be unique":
                                                    //Toast.makeText(SignupActivity.this, "Не уникальный логин", Toast.LENGTH_SHORT).show();
                                                    userName.setError("Не уникальный логин");
                                                    userName.requestFocus();
                                                    break;
                                                case "Validation isEmail on email failed":
                                                    //Toast.makeText(SignupActivity.this, "Неверный формат email", Toast.LENGTH_SHORT).show();
                                                    userEmail.setError("Неверный формат");
                                                    userEmail.requestFocus();
                                                    break;
                                                case "Validation len on email failed":
                                                    //Toast.makeText(SignupActivity.this, "Неверный формат email", Toast.LENGTH_SHORT).show();
                                                    userEmail.setError("Неверная длина");
                                                    userEmail.requestFocus();
                                                    break;
                                                case "email must be unique":
                                                    //Toast.makeText(SignupActivity.this, "Не уникальный email", Toast.LENGTH_SHORT).show();
                                                    userEmail.setError("Уже используется");
                                                    userEmail.requestFocus();
                                                    break;
                                                case "Validation len on password failed":
                                                    //Toast.makeText(SignupActivity.this, "Неверная длина логина", Toast.LENGTH_SHORT).show();
                                                    userPass.setError("Неверная длина");
                                                    userPass.requestFocus();
                                                    break;
                                                case "repass failed":
                                                    userRePass.setError("Пароли не совпадают");
                                                    userRePass.requestFocus();
                                                    break;
                                                default:
                                                    break;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<api_Users> call, @NonNull Throwable t) {
                                        //Log.d("RES", "123123");

                                        //Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
                                        //textView.setText("Error occurred while getting request!");
                                        t.printStackTrace();
                                    }
                                });
            }
        });

    }

    // При нажатии на кнопку "Назад" в ActionBar закрываем текущее окно
    @Override
    public void onBackPressed() {
        finish();
    }
}
