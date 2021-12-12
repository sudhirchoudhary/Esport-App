package com.example.esport3.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.esport3.Adapter.VideoAdapter;
import com.example.esport3.Model.User;
import com.example.esport3.Model.video;
import com.example.esport3.R;
import com.example.esport3.UploadActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChannelFagment extends Fragment {
    RecyclerView recyclerView;
    DatabaseReference reference;
    private VideoAdapter videoAdapter;
    private List<video> mUsers;
    TextView videoCount,Followers;
    CircleImageView profile;
    TextView Channel;

    public ChannelFagment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_channel, container, false);

        recyclerView=v.findViewById(R.id.channel_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mUsers=new ArrayList<>();
        videoCount=v.findViewById(R.id.textView27);
        Followers=v.findViewById(R.id.textView12);
        Channel=v.findViewById(R.id.textView11);
        profile=v.findViewById(R.id.image);

        FloatingActionButton floatingActionButton = v.findViewById(R.id.floating);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(getContext(), UploadActivity.class));
            }
        });
         FirebaseUser cuser= FirebaseAuth.getInstance().getCurrentUser();
        assert cuser != null;
        reference= FirebaseDatabase.getInstance().getReference().child("users").child(cuser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (getActivity() == null) {
                    return;
                }
                User user=dataSnapshot.getValue(User.class);
                assert user != null;
                Channel.setText(user.getUsername());

                if (user.getImageURL().equals("default")){
                    profile.setImageResource(R.drawable.profile);
                }else{
                    Glide.with(getActivity()).load(user.getImageURL()).into(profile);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        Query reference= FirebaseDatabase.getInstance().getReference().child("videos").orderByChild("userid").equalTo(cuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                videoCount.setText(dataSnapshot.getChildrenCount()+"  Videos");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Query reference2= FirebaseDatabase.getInstance().getReference().child("Followers").child(cuser.getUid());
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long y=dataSnapshot.getChildrenCount();
                Followers.setText(y+"  Followers");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        showVideos();
        return v;
    }
    private void showVideos() {
        final FirebaseUser cuser= FirebaseAuth.getInstance().getCurrentUser();
        Query reference= FirebaseDatabase.getInstance().getReference().child("videos").orderByChild("userid").equalTo(cuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    video user = snapshot.getValue(video.class);
                    mUsers.add(user);
                }
                videoAdapter = new VideoAdapter(getContext(), mUsers);
                recyclerView.setAdapter(videoAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
