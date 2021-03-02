package com.example.instagramcclone.Adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramcclone.Model.User;
import com.example.instagramcclone.R;

import java.util.List;

public class TagAdapter extends RecyclerView.Adapter {
    private Context mcontext;
    private List<String> hastags;
    private List<String> nooftags;

    public TagAdapter(Context mcontext, List<String> hastags, List<String> nooftags) {
        this.mcontext = mcontext;
        this.hastags = hastags;
        this.nooftags = nooftags;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.tag_item,parent,false);
        return new TagAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return hastags.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tags,posts;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tags=itemView.findViewById(R.id.tagdetail);
            posts=itemView.findViewById(R.id.nofpost);
        }
    }
}
