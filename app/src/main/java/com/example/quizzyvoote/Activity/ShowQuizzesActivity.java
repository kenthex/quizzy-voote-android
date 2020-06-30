package com.example.quizzyvoote.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizzyvoote.R;
import com.example.quizzyvoote.classes.Storage;
import com.example.quizzyvoote.classes.api_NetworkService;
import com.example.quizzyvoote.classes.api_Questions;
import com.example.quizzyvoote.classes.c_Quiz;
import com.example.quizzyvoote.classes.c_Quiz_List_Adapter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowQuizzesActivity extends AppCompatActivity {

    RecyclerView rv_quiz_list;
    Button       button;
    List<api_Questions> questions;
    TextView msg_empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_quiz_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Список опросов");

        msg_empty = findViewById(R.id.empty_list_msg);

        final ArrayList<c_Quiz> quizzes = new ArrayList<>();
        api_NetworkService.getInstance()
                .getJSONApi()
                .getQuestions()
                .enqueue(new Callback<api_Questions>() {
                    @Override
                    public void onResponse(@NonNull Call<api_Questions> call, @NonNull Response<api_Questions> response) {
                        questions = response.body().getQuestions();

                        if(questions.isEmpty())
                            msg_empty.setVisibility(View.VISIBLE);
                        else msg_empty.setVisibility(View.GONE);

                        for(api_Questions question : questions) {
                            if(question.getVerified() == 1) {
                                quizzes.add(new c_Quiz(Integer.parseInt(question.getID()), question.getTitle(), question.getCreator(), question.getExpiredAt()));
                                msg_empty.setVisibility(View.GONE);
                            }
                        }

                        rv_quiz_list = findViewById(R.id.rv_quiz_list);
                        c_Quiz_List_Adapter quizAdapter = new c_Quiz_List_Adapter(quizzes);
                        rv_quiz_list.setAdapter(quizAdapter);
                        rv_quiz_list.setLayoutManager(new LinearLayoutManager(ShowQuizzesActivity.this));
                    }
                    @Override
                    public void onFailure(@NonNull Call<api_Questions> call, @NonNull Throwable t) {
                        Log.d("RES", "ERROR");
                        t.printStackTrace();
                    }
                });
        Storage.addProperty("TITLE", "");
//        button = findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

    }
}
