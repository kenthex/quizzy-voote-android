package com.example.quizzyvoote.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizzyvoote.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class c_Question_Adapter extends RecyclerView.Adapter<c_Question_Adapter.QuestionViewHolder> implements View.OnClickListener {

    private ArrayList<String> mQuestions = new ArrayList<String>();
    private Button delAnswer;

    public c_Question_Adapter(ArrayList<String> mQuestions) {
        this.mQuestions = mQuestions;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View questionView = inflater.inflate(R.layout.extra_list_item_add_question, parent, false);

        questionView.setOnClickListener(this);

        QuestionViewHolder viewHolder = new QuestionViewHolder(questionView);
        return viewHolder;
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {

        TextView question;
        Button del_question;
        Button del_all_questions;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.tw_question);
            del_question = itemView.findViewById(R.id.btn_delete_question);
            del_all_questions = itemView.findViewById(R.id.btn_delete_all_answers);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, final int position) {
        String currentQuestion = mQuestions.get(position);
        holder.question.setText(currentQuestion);

        holder.question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                String itemLabel = mQuestions.get(position);
                //Toast.makeText(context, itemLabel, Toast.LENGTH_SHORT).show();
            }
        });

        holder.del_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemLabel = mQuestions.get(position);
                Context context = v.getContext();
                mQuestions.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mQuestions.size());
                //Toast.makeText(context, "Удалено: " + itemLabel, Toast.LENGTH_SHORT).show();
                Snackbar.make(v, "Удалён вариант ответа: " + itemLabel, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mQuestions.size();
    }

    @Override
    public void onClick(View v) { }

}
