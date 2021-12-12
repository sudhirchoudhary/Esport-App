package com.example.esport3.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.esport3.Model.Comment;
import com.example.esport3.Model.video;
import com.example.esport3.R;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter <CommentAdapter.ViewHolder>{
    private Context mContext;
    private List<Comment> mUsers;

    public CommentAdapter(Context mContext, List<Comment> mUsers) {
        this.mContext = mContext;
        this.mUsers = mUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.comment_recycler,parent,false);
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Comment comment=mUsers.get(position);
        holder.username.setText(comment.getUsername());
        holder.comment.setText(comment.getComment());
        Glide.with(mContext).load(comment.getImageURL()).into(holder.profileImage);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView comment;
        private ImageView profileImage;
        private TextView username;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            comment=itemView.findViewById(R.id.comment_show);
            username=itemView.findViewById(R.id.username);
            profileImage=itemView.findViewById(R.id.profile_images);

        }
    }
}
