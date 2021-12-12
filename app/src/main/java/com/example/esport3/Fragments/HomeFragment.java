package com.example.esport3.Fragments;



import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.esport3.Adapter.UserAdapter;
import com.example.esport3.Adapter.VideoAdapter;
import com.example.esport3.Model.User;
import com.example.esport3.Model.video;
import com.example.esport3.R;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    DatabaseReference reference;
    private VideoAdapter videoAdapter;
    private List<video> mUsers;
    ProgressBar progressBar;
    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView=v.findViewById(R.id.thumnail_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mUsers=new ArrayList<>();
        progressBar=v.findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);

        showVideos();
        return v;
    }

    private void showVideos() {
        final FirebaseUser cuser= FirebaseAuth.getInstance().getCurrentUser();
        Query reference= FirebaseDatabase.getInstance().getReference().child("videos");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mUsers.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        video user = snapshot.getValue(video.class);
                            mUsers.add(user);
                            progressBar.setVisibility(View.INVISIBLE);
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
