package com.example.cram_.projectjedi;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by cram_ on 01/02/2016.
 */
public class MyCustomAdapter extends RecyclerView.Adapter<MyCustomAdapter.AdapterViewHolder> {

    ArrayList<String> users;
    ArrayList<Integer> scores;

    public MyCustomAdapter(ArrayList<String> data, ArrayList<Integer> dataScores){
        users = data;
        scores = dataScores;
    }
    @Override
    public MyCustomAdapter.AdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.rowlayout, viewGroup, false);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyCustomAdapter.AdapterViewHolder adapterViewHolder, int position) {
        adapterViewHolder.user.setText(scores.get(position).toString() + " " + users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {
        public TextView user;

        public AdapterViewHolder(View itemView) {
            super(itemView);
            this.user = (TextView) itemView.findViewById(R.id.textViewOp);
        }
    }
}
