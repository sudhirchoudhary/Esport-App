package com.example.esport3.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.esport3.MessageActivity;
import com.example.esport3.Model.User;
import com.example.esport3.Model.video;
import com.example.esport3.Playerdatashow;
import com.example.esport3.R;
import com.example.esport3.VideoActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private Context mContext;
    private List<video> mUsers;


    public VideoAdapter(Context mContext, List<video> mUsers) {
        this.mContext = mContext;
        this.mUsers = mUsers;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.video_recycler,parent,false);
        return new VideoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final video user=mUsers.get(position);
        holder.username.setText(user.getChannel());
        holder.title.setText(user.getTitle());
        Glide.with(mContext).load(user.getImageURL()).into(holder.profileImage);
        Glide.with(mContext).load(user.getImageURL()).into(holder.thumbnail);
        holder.likeShow.setText(user.getLikes());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext, VideoActivity.class);
                    intent.putExtra("videoid",user.getVideoId());
                    intent.putExtra("channelid",user.getUserid());
                    mContext.startActivity(intent);
                }
            });
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Followers").child(user.getUserid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long y=dataSnapshot.getChildrenCount();
                holder.followerShow.setText(String.valueOf(y));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private ImageView profileImage,thumbnail;
        private TextView username;
        private TextView likeShow;
        private TextView followerShow;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.name_he);
            username=itemView.findViewById(R.id.username);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            profileImage=itemView.findViewById(R.id.profile_image);
            likeShow=itemView.findViewById(R.id.likeShow);
            followerShow=itemView.findViewById(R.id.follow_Count);
        }
    }
}
