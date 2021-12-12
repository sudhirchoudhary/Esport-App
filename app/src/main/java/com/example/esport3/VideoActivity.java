package com.example.esport3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import com.bumptech.glide.Glide;
import com.example.esport3.Adapter.CommentAdapter;
import com.example.esport3.Model.Comment;
import com.example.esport3.Model.video;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class VideoActivity extends AppCompatActivity {
    VideoView videoView;
    String url;
    Intent intent;
    String videoid,channelid;
    ImageButton like,dislike,comment_btn;
    String isliked;
    FirebaseUser fuser;
    TextView likeShow,dislikeShow,Channel,Title;
    Button follow_btn;
    int t;
    long y;
    CircleImageView profile;
    EditText comment;
    RecyclerView recyclerView;
    List<Comment> mComments;
    String username,imageUrl;
    CommentAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        videoView=findViewById(R.id.video);
        fuser=FirebaseAuth.getInstance().getCurrentUser();

        like=findViewById(R.id.like);
        dislike=findViewById(R.id.dislike);
        follow_btn=findViewById(R.id.follow_btn);
        Channel=findViewById(R.id.username);
        Title=findViewById(R.id.name_he);
        profile=findViewById(R.id.profile_image);
        recyclerView=findViewById(R.id.comment_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        mComments=new ArrayList<>();

        intent=getIntent();
        videoid=intent.getStringExtra("videoid");
        channelid=intent.getStringExtra("channelid");
        likeShow=findViewById(R.id.likeShow);
        dislikeShow=findViewById(R.id.dislikeShow);
        comment=findViewById(R.id.comment);
        comment_btn=findViewById(R.id.send_comment);

        final DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("users").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                imageUrl=dataSnapshot.child("ImageURL").getValue(String.class);
                username=dataSnapshot.child("username").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference commentRef=FirebaseDatabase.getInstance().getReference("Comments").child(videoid);

        comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment_btn.setBackgroundColor(R.drawable.ic_action_send_comment);
                final String commentID=commentRef.push().getKey();
                HashMap<String,Object>hashMap=new HashMap<>();
                hashMap.put("commentId",commentID);
                hashMap.put("receiver",channelid);
                hashMap.put("sender",fuser.getUid());
                hashMap.put("comment",comment.getText().toString());
                hashMap.put("ImageURL",imageUrl);
                hashMap.put("username",username);
                assert commentID != null;
                commentRef.child(commentID).setValue(hashMap);
            }
        });
        DatabaseReference xref=FirebaseDatabase.getInstance().getReference("videos").child(videoid);
        xref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Channel.setText(dataSnapshot.child("Channel").getValue(String.class));
                Title.setText(dataSnapshot.child("title").getValue(String.class));
                if (dataSnapshot.child("imageURL").getValue(String.class).equals("default")){
                    profile.setImageResource(R.drawable.profile);
                }else{
                    Glide.with(getApplicationContext()).load(dataSnapshot.child("imageURL").getValue(String.class)).into(profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        videoView.setMediaController(new MediaController(this){
            public boolean dispatchKeyEvent(KeyEvent event){
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)
                    finish();
                return super.dispatchKeyEvent(event);
            }
        });
        final DatabaseReference ref=FirebaseDatabase.getInstance().getReference("videos").child(videoid);
        Query likeCount=FirebaseDatabase.getInstance().getReference("Likes").child(videoid).orderByChild("info").equalTo("like");
        likeCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                likeShow.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                y=dataSnapshot.getChildrenCount();
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put("likes",String.valueOf(y));
                ref.updateChildren(hashMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Query likeCount2=FirebaseDatabase.getInstance().getReference("Likes").child(videoid).orderByChild("info").equalTo("dislike");
        likeCount2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dislikeShow.setText(String.valueOf(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference reference2=FirebaseDatabase.getInstance().getReference("Followers")
                .child(channelid).child(fuser.getUid());
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String timepassID=dataSnapshot.child("follower").getValue(String.class);
                if(fuser.getUid().equals(timepassID)&&timepassID!=null){
                    follow_btn.setBackgroundResource(R.drawable.custom_following);
                    follow_btn.setTextColor(getResources().getColor(R.color.following));
                    follow_btn.setText("Following");
                }
                else{
                    Toast.makeText(VideoActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final DatabaseReference ref2=FirebaseDatabase.getInstance().getReference("videos").child(videoid);
        Query followerCount=FirebaseDatabase.getInstance().getReference("Followers").child(channelid).orderByChild("follow").equalTo("yes");
        followerCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                y=dataSnapshot.getChildrenCount();
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put("follower",String.valueOf(y));
                ref2.updateChildren(hashMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference reference6=FirebaseDatabase.getInstance().getReference("Likes")
                .child(videoid).child(fuser.getUid());
        reference6.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                isliked=dataSnapshot.child("info").getValue(String.class);
                if(isliked!=null){
                    if(isliked.equals("like")){
                        like.setBackgroundResource(R.drawable.ic_action_like_t);
                        dislike.setBackgroundResource(R.drawable.ic_action_dislike_ut);
                    }
                    else{
                        like.setBackgroundResource(R.drawable.ic_action_like_ut);
                        dislike.setBackgroundResource(R.drawable.ic_action_dislike_t);
                    }
                }
                else
                {
                    Toast.makeText(VideoActivity.this, "Error he mere bhai", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        startVideo(videoid);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageLike(t=1);
            }
        });
        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             manageLike(t=2);
            }
        });
        DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Followers").child(channelid)
                .child(fuser.getUid());
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String follow=dataSnapshot.child("follow").getValue(String.class);
                follow_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        assert follow != null;
                        if(follow.equals("yes")){
                            int t=1;
                            followManage(t);
                        }
                        if(follow.equals("")){
                            int t=2;
                            followManage(t);
                        }

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        showComments();

    }

    private void showComments() {
        Query reference= FirebaseDatabase.getInstance().getReference().child("Comments").child(videoid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mComments.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Comment user = snapshot.getValue(Comment.class);
                    mComments.add(user);
                }
                commentAdapter = new CommentAdapter(getApplicationContext(), mComments);
                recyclerView.setAdapter(commentAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void followManage(int follow) {

        final DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Followers")
                .child(channelid);
                if(follow==2){
                        follow_btn.setTextColor(getResources().getColor(R.color.following));
                        follow_btn.setBackgroundResource(R.drawable.custom_following);
                        follow_btn.setText("Following");
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("follower",fuser.getUid());
                        hashMap.put("follow","yes");
                        reference.child(fuser.getUid()).updateChildren(hashMap);
                }
                if(follow==1){
                        follow_btn.setTextColor(getResources().getColor(R.color.follow));
                        follow_btn.setBackgroundColor(getResources().getColor(R.color.following));
                        follow_btn.setText("Follow");
                        reference.child(fuser.getUid()).removeValue();
                }

            }

    public void manageLike(int x){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Likes")
                .child(videoid).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        if (x==1){
            like.setBackgroundResource(R.drawable.ic_action_like_t);
            dislike.setBackgroundResource(R.drawable.ic_action_dislike_ut);
            HashMap<String,Object>hashMap=new HashMap<>();
            hashMap.put("info","like");
            reference.updateChildren(hashMap);
        }
        if(x==2){
            like.setBackgroundResource(R.drawable.ic_action_like_ut);
            dislike.setBackgroundResource(R.drawable.ic_action_dislike_t);
            HashMap<String,Object>hashMap=new HashMap<>();
            hashMap.put("info","dislike");
            reference.updateChildren(hashMap);
        }
    }

    private void startVideo(String id) {
        DatabaseReference query= FirebaseDatabase.getInstance().getReference("videos").child(id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                video user=dataSnapshot.getValue(video.class);
                assert user != null;
                url=user.getVideoUrl();
                videoView.requestFocus();
                videoView.setVideoURI(Uri.parse(url));
                videoView.start();
                MediaController mediaController=new MediaController( VideoActivity.this);
                videoView.setMediaController(mediaController);
                mediaController.setAnchorView(videoView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



}
