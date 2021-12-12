package com.example.esport3.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.esport3.MessageActivity;
import com.example.esport3.Model.User;
import com.example.esport3.Playerdatashow;
import com.example.esport3.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUsers;
    private boolean ischat;

    public UserAdapter(Context mContext, List<User> mUsers,boolean ischat) {
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.ischat=ischat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.cuctom_recycler,parent,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final User user=mUsers.get(position);

        if (ischat){
            if (user.getStatus().equals("online")){
                holder.on.setVisibility(View.VISIBLE);
                holder.off.setVisibility(View.GONE);
            }
            else{
                holder.on.setVisibility(View.GONE);
                holder.off.setVisibility(View.VISIBLE);
            }
        }else {
            holder.on.setVisibility(View.GONE);
            holder.off.setVisibility(View.GONE);
        }

        holder.username.setText(user.getUsername());
        if(user.getImageURL()!=null){
            if(user.getImageURL().equals("default")){
                holder.profileImage.setImageResource(R.mipmap.ic_launcher);
            }else {
                Glide.with(mContext).load(user.getImageURL()).placeholder(android.R.drawable.progress_indeterminate_horizontal).error(android.R.drawable.stat_notify_error).into(holder.profileImage);
            }
        }

       if (ischat){
           holder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent=new Intent(mContext, MessageActivity.class);
                   intent.putExtra("userid",user.getId());
                   mContext.startActivity(intent);
               }
           });
       }else
       {
           holder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent=new Intent(mContext, Playerdatashow.class);
                   intent.putExtra("userid",user.getId());
                   mContext.startActivity(intent);
               }
           });
       }

    }
    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView username;
        private ImageView profileImage;
        private TextView on;
        private TextView off;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username=itemView.findViewById(R.id.name_he);
            profileImage=itemView.findViewById(R.id.profile_image);
            on=itemView.findViewById(R.id.img_on);
            off=itemView.findViewById(R.id.img_off);
        }
    }
}
