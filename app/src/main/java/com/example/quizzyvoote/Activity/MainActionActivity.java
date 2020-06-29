package com.example.quizzyvoote.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizzyvoote.R;
import com.example.quizzyvoote.classes.Storage;
import com.example.quizzyvoote.classes.api_NetworkService;
import com.example.quizzyvoote.classes.api_Tokens;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActionActivity extends AppCompatActivity {

    Button btn_create_quiz, btn_pass_quiz, btn_exit;
    TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_action_screen);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Главное меню");

        username = (TextView) findViewById(R.id.tv_username);
        username.setText(Storage.getProperty("USERNAME"));

        btn_create_quiz = (Button) findViewById(R.id.btn_create_quiz);
        btn_create_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActionActivity.this, CreateQuizActivity.class);
                startActivity(intent);
            }
        });

        btn_pass_quiz = (Button) findViewById(R.id.btn_pass_quiz);
        btn_pass_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActionActivity.this, ShowQuizzesActivity.class);
                startActivity(intent);
            }
        });

        btn_exit = (Button) findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                api_NetworkService.getInstance()
                        .getJSONApi()
                        .delToken(Storage.getProperty("TOKEN"))
                        .enqueue(new Callback<api_Tokens>() {
                            @Override
                            public void onResponse(@NonNull Call<api_Tokens> call, @NonNull Response<api_Tokens> response) {
                                api_Tokens post = response.body();
                                Storage.addProperty("USER_ID", "");
                                Storage.addProperty("TOKEN", "");

                                Intent intent = new Intent(MainActionActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            @Override
                            public void onFailure(@NonNull Call<api_Tokens> call, @NonNull Throwable t) {
                                Log.d("RES", "ERROR_1");
                                t.printStackTrace();
                            }
                        });
            }
        });
    }

    private void resetValues() {

    }
}
