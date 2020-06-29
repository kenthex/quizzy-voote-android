package com.example.quizzyvoote.classes;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizzyvoote.R;

import java.util.List;

public class c_PassTheQuiz_Adapter extends RecyclerView.Adapter<c_PassTheQuiz_Adapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<api_Answers> answers;
    private int lastSelectedPosition = -1;


    public c_PassTheQuiz_Adapter(Context context, List<api_Answers> answers) {
        this.answers = answers;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public c_PassTheQuiz_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.extra_list_item_show_answers, parent, false);
        return new ViewHolder(view);
    }
    private RadioButton lastCheckedRB = null;

    @Override
    public void onBindViewHolder(@NonNull final c_PassTheQuiz_Adapter.ViewHolder holder, final int position) {
        final api_Answers answers = this.answers.get(position);
        holder.title.setText(answers.getAnswer());
        holder.title.setChecked(lastSelectedPosition == position);

        if(holder.title.isChecked())
            holder.title.setBackgroundColor(Color.parseColor("#FFBB33"));
        else holder.title.setBackgroundColor(Color.parseColor("#E0E0E0"));
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    public void update(List<api_Answers> newlist) {
        answers.clear();
        answers.addAll(newlist);
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton title, lastTitle;
        RadioGroup radioGroup;
        CardView cardview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.rb_select_answer);
            cardview = itemView.findViewById(R.id.card_answer);
            radioGroup = itemView.findViewById(R.id.radioGrouppen);
            lastTitle = itemView.findViewById(R.id.rb_select_answer);


            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    answers.add(new api_Answers(" "));
                    answers.remove(( answers.size() -1 ));
                    notifyDataSetChanged();

                    Storage.addProperty("CURRENT_ANSWER", answers.get(lastSelectedPosition).getAnswer());
                    Context context = v.getContext();
                }
            });

        }
    }
}
