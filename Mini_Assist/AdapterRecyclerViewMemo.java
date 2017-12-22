package com.sherlock.shoppingassist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

public class AdapterRecyclerViewMemo extends RecyclerView.Adapter < AdapterRecyclerViewMemo.ViewHolder > {
    ArrayList <MemoData> arrayList = new ArrayList<MemoData>();
    Context context;
    static int number;
    LayoutInflater layoutInflater;

    public AdapterRecyclerViewMemo(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void notifyIt(ArrayList<MemoData> newArrayList){
        arrayList = newArrayList;
        //Toast.makeText(context,position+"",Toast.LENGTH_SHORT).show();
        notifyDataSetChanged();
        notifyItemRangeChanged(0,arrayList.size());
        //notifyItemChanged(4);
    }


    public void setList(ArrayList < MemoData > arrayList) {
        this.arrayList = arrayList;
        notifyItemRangeChanged(0, arrayList.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_recycler_view_memo, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MemoData memoData = arrayList.get(position);
        holder.setValues(memoData);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public RelativeLayout relativeLayout;
        public Button deleteButton;
        public void setValues(MemoData memoData) {
            textView.setText(memoData.productName);
            if(memoData.added == 1) {
                //Toast.makeText(context, "Already",Toast.LENGTH_SHORT).show();
                textView.setTextColor(Color.BLACK); //green
                textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            else{
                textView.setTextColor(Color.BLACK);
            }
        }

        public ViewHolder(final View itemView, final Context context) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.memoName1);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);
            deleteButton= (Button) itemView.findViewById(R.id.deleteButton2);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int n = getAdapterPosition();
                    //Toast.makeText(context, n + "", Toast.LENGTH_LONG).show();
                    arrayList.remove(n);
                    notifyItemRemoved(n);

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(arrayList);
                    if(arrayList.size() != 0)
                        editor.putString("memoList", json);
                    else{
                        editor.putString("memoList", null);
                    }
                    editor.commit();
                }
            });

        }
    }
}