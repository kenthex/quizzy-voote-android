package com.example.quizzyvoote.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizzyvoote.R;
import com.example.quizzyvoote.classes.Storage;
import com.example.quizzyvoote.classes.api_Answers;
import com.example.quizzyvoote.classes.api_NetworkService;
import com.example.quizzyvoote.classes.api_Votes;
import com.example.quizzyvoote.classes.c_PassTheQuiz_Adapter;
import com.google.android.material.snackbar.Snackbar;
//import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassTheQuizActivity extends AppCompatActivity {

    public final static String ID = "ID";
    public final static String CREATOR = "CREATOR";
    public final static String NAME = "NAME";
    public final static String ANSWER = "ANSWER";

    public TextView id, creator, name, answer, someText;
    public RecyclerView rv_answer_list;
    List<api_Answers> answers = new ArrayList<>();

    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_the_quiz);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        id = findViewById(R.id.tv_quiz_id);
        creator = findViewById(R.id.tv_quiz_creator);
        answer = findViewById(R.id.tv_quiz_answer);
        name = findViewById(R.id.tv_label_name);
        someText = findViewById(R.id.sdhfj);

        id.setText(getIntent().getStringExtra("ID"));
        creator.setText(getIntent().getStringExtra("CREATOR"));
        name.setText(getIntent().getStringExtra("NAME"));

        actionBar.setTitle(name.getText());

        if(!Storage.getProperty("TITLE").equals("")) {
            answer.setText(Storage.getProperty("TITLE"));
        } else {
            answer.setText("");
            someText.setText("");
        }

        Storage.addProperty("CURRENT_QUESTION_ID", getIntent().getStringExtra("ID"));
        Storage.addProperty("CURRENT_ANSWER", "");
        String answersText = getIntent().getStringExtra("ANSWER");

        for (String retval : answersText.split("~~", 0)) {
            answers.add(new api_Answers(retval));
        }
        answers.remove(0);

        //Toast.makeText(PassTheQuizActivity.this, getIntent().getStringExtra("ANSWER"), Toast.LENGTH_SHORT).show();


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_answers);
        c_PassTheQuiz_Adapter adapter = new c_PassTheQuiz_Adapter(this, answers);
        recyclerView.setAdapter(adapter);

        btnSave = findViewById(R.id.btn_save);

        if(Storage.getProperty("VOTED").equals("TRUE")) {
            btnSave.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (!Storage.getProperty("CURRENT_ANSWER").equals("")) {

                    api_Votes vote = new api_Votes(Integer.parseInt(Storage.getProperty("USER_ID")), Integer.parseInt(Storage.getProperty("CURRENT_QUESTION_ID")), Storage.getProperty("CURRENT_ANSWER"));
                    api_NetworkService.getInstance()
                            .getJSONApi()
                            .createVote(vote)
                            .enqueue(new Callback<api_Votes>() {
                                @Override
                                public void onResponse(@NonNull Call<api_Votes> call, @NonNull Response<api_Votes> response) {
                                    api_Votes votes = response.body();
                                    //Toast.makeText(PassTheQuizActivity.this, "SEND: " + Storage.getProperty("CURRENT_ANSWER"), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(PassTheQuizActivity.this, "Успешно!", Toast.LENGTH_SHORT).show();
                                    //Snackbar.make(v, "Успешно!", Snackbar.LENGTH_SHORT).show();
                                    Intent intent = new Intent(PassTheQuizActivity.this, MainActionActivity.class);
                                    startActivity(intent);
                                }

                                @Override
                                public void onFailure(@NonNull Call<api_Votes> call, @NonNull Throwable t) {
                                    Log.d("RES", "ERROR");
                                    t.printStackTrace();
                                }
                            });
                } else Snackbar.make(v, "Ничего не выбрано", Snackbar.LENGTH_LONG).show();


            }
        });

    }

}
