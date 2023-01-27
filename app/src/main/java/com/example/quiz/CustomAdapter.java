package com.example.quiz;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
//import com.firebase.ui.database.FirebaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>
{
    Context context;
    ArrayList<QuestionModel> List;

    public CustomAdapter(Context context, ArrayList<QuestionModel> list) {
        this.context = context;
        List = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_question_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
         QuestionModel model  = List.get(position);
        holder.Answer.setText(model.getAnswer());
        holder.OptionA.setText(model.getOptionA());
        holder.OptionB.setText(model.getOptionB());
        holder.OptionC.setText(model.getOptionC());
        holder.OptionD.setText(model.getOptionD());
        holder.Question.setText(model.getQuestion());
    }

    @Override
    public int getItemCount() {
        return List.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView Question,OptionA,OptionB,OptionC,OptionD,Answer;

        public MyViewHolder(@NonNull View iteamView){
            super(iteamView);
             Answer = iteamView.findViewById(R.id.Answer);
             OptionA= iteamView.findViewById(R.id.FirstOption);
             OptionB= iteamView.findViewById(R.id.SecondOption);
            OptionC= iteamView.findViewById(R.id.ThirdOption);
            OptionD= iteamView.findViewById(R.id.ForthOption);
            Question = iteamView.findViewById(R.id.Ques);

        }
    }
}
