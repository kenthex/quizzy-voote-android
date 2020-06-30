package com.example.quizzyvoote.classes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizzyvoote.Activity.PassTheQuizActivity;
import com.example.quizzyvoote.R;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class c_Quiz_List_Adapter extends RecyclerView.Adapter<c_Quiz_List_Adapter.ListViewHolder> implements View.OnClickListener {

    private ArrayList<c_Quiz> quiz_list;

    @Override
    public void onClick(View v) { }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View listView = inflater.inflate(R.layout.extra_list_item_show_quizzes, parent, false);

        ListViewHolder viewHolder = new ListViewHolder(listView);
        return viewHolder;
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {

        TextView quiz_id, quiz_creator, quiz_name, quiz_expireDate;
        CardView card_quiz;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            card_quiz = itemView.findViewById(R.id.card_quiz);
            quiz_id = itemView.findViewById(R.id.tv_quiz_id);
            quiz_creator = itemView.findViewById(R.id.tv_quiz_creator_name);
            quiz_name = itemView.findViewById(R.id.tv_label_name);
            quiz_expireDate = itemView.findViewById(R.id.tv_date);
        }
    }

    public c_Quiz_List_Adapter(ArrayList<c_Quiz> quiz_list) {
        this.quiz_list = quiz_list;
    }

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, final int position) {
        final c_Quiz currentQuiz = quiz_list.get(position);

        holder.quiz_id.setText(currentQuiz.getQuizID().toString());
        holder.quiz_creator.setText(currentQuiz.getQuizCreator());
        holder.quiz_name.setText(currentQuiz.getQuizName());
        holder.quiz_expireDate.setText(currentQuiz.getExpireDate().toString());

        long currentDate, expireDate;
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        Date expired = null;
        Date current = null;
        try {
            expired = format.parse(currentQuiz.getExpireDate());
            current = new Date();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        expireDate = expired.getTime();
        currentDate = current.getTime();

        if(currentDate > expireDate) {
            holder.quiz_expireDate.setTextColor(Color.parseColor("#FF4444"));
        } else holder.quiz_expireDate.setTextColor(Color.parseColor("#99CC00"));

        holder.card_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                final Context context = v.getContext();
                if(holder.quiz_expireDate.getCurrentTextColor() != Color.parseColor("#FF4444")) {

                api_NetworkService.getInstance()
                        .getJSONApi()
                        .getAnswersForQuestion(currentQuiz.getQuizID().toString())
                        .enqueue(new Callback<api_Answers>() {
                            @Override
                            public void onResponse(@NonNull Call<api_Answers> call, @NonNull Response<api_Answers> response) {
                                List<api_Answers> answers_list;
                                answers_list = response.body().getAnswers();

                                ArrayList<String> answers = new ArrayList<String>();
                                String anuswers = "";
                                for(api_Answers answer : response.body().getAnswers()) {
                                    answers.add(answer.getAnswer());
                                    anuswers += "~~" + answer.getAnswer();
                                }
                                final Intent intent = new Intent(context, PassTheQuizActivity.class);
                                intent.putExtra(PassTheQuizActivity.ID, currentQuiz.getQuizID().toString());
                                intent.putExtra(PassTheQuizActivity.CREATOR, currentQuiz.getQuizCreator());
                                intent.putExtra(PassTheQuizActivity.NAME, currentQuiz.getQuizName());
                                intent.putExtra(PassTheQuizActivity.ANSWER, anuswers);

                                api_NetworkService.getInstance()
                                        .getJSONApi()
                                        .checkVote(Integer.parseInt(Storage.getProperty("USER_ID")), currentQuiz.getQuizID())
                                        .enqueue(new Callback<api_Votes>() {
                                            @Override
                                            public void onResponse(@NonNull Call<api_Votes> call, @NonNull Response<api_Votes> response) {
                                                api_Votes post = response.body();
                                                //Storage.addProperty("CURRENT_ANSWER", "");
                                                if(post.getStatus().equals("voted")) {
                                                    Storage.addProperty("VOTED", "TRUE");
                                                    Storage.addProperty("TITLE", post.getTitle());
                                                } else { Storage.addProperty("VOTED", "FALSE"); Storage.addProperty("TITLE", ""); }
                                            }
                                            @Override
                                            public void onFailure(@NonNull Call<api_Votes> call, @NonNull Throwable t) {
                                                Log.d("RES", "ERROR");
                                                t.printStackTrace();
                                            }
                                        });

                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                context.startActivity(intent);
                            }
                            @Override
                            public void onFailure(@NonNull Call<api_Answers> call, @NonNull Throwable t) {
                                Log.d("RES", "ERROR");
                                t.printStackTrace();
                            }
                        });
                } else Snackbar.make(v, "Голосование завершено", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return quiz_list.size();
    }
}
