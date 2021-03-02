package com.example.instagramcclone.Adapter;

import android.content.Context;
import android.nfc.Tag;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramcclone.R;

import java.util.List;

public  class TagAdapter extends  RecyclerView.Adapter<TagAdapter.ViewHolder> {
private Context mcontext;
private List<String> mTags;
    private List<String> mTagcount;

    public TagAdapter(Context mcontext, List<String> mTags, List<String> mTagcount) {
        this.mcontext = mcontext;
        this.mTags = mTags;
        this.mTagcount = mTagcount;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.tag_item,parent,false);
        return new TagAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
holder.tag.setText(mTags.get(position));
holder.noOfPosts.setText(mTags.get(position));
    }

    @Override
    public int getItemCount() {
        return mTags.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tag,noOfPosts;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tag=itemView.findViewById(R.id.tagdetail);
            noOfPosts=itemView.findViewById(R.id.nofpost);
        }
    }

}