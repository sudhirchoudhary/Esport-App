package com.example.esport3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.esport3.Fragments.ChannelFagment;
import com.example.esport3.Fragments.ChatFragment;
import com.example.esport3.Fragments.HomeFragment;
import com.example.esport3.Fragments.UsersFragment;
import com.example.esport3.Model.User;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class StartActivity extends AppCompatActivity {
    CircleImageView profile;
    TextView username;

    FirebaseUser user;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Toolbar toolbar=findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        TabLayout tabLayout=findViewById(R.id.myTablayout);
        ViewPager viewPager=findViewById(R.id.myVFiewpager);
        viewpagerAdapter viewpagerAdapter=new viewpagerAdapter(getSupportFragmentManager());
        viewpagerAdapter.addFragment(new HomeFragment(),"Home");
        viewpagerAdapter.addFragment(new UsersFragment(),"Player");
        viewpagerAdapter.addFragment(new ChatFragment(),"Chats");
        viewpagerAdapter.addFragment(new ChannelFagment(),"My Channel");
        viewPager.setAdapter(viewpagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        profile=findViewById(R.id.profile_image);
        username=findViewById(R.id.username);

        user=FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   User user=dataSnapshot.getValue(User.class);
                assert user != null;
                username.setText(user.getUsername());

                    if (user.getImageURL().equals("default")){
                        profile.setImageResource(R.mipmap.ic_launcher);
                    }else{
                        Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile);
                    }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater =getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_logout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            return true;
        }
        if (id==R.id.action_addAcc){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),RegisterActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            return true;
        }
        if (id==R.id.action_bio){
            startActivity(new Intent(getApplicationContext(),BioData.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            return true;
        }
        if (id==R.id.action_myprofile){
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class viewpagerAdapter extends FragmentPagerAdapter {

        private  ArrayList<Fragment> fragmentlist;
        private  ArrayList<String> fragTitle;
        public viewpagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragmentlist=new ArrayList<>();
            this.fragTitle=new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentlist.get(position);
        }

        @Override
        public int getCount() {
            return fragmentlist.size();
        }
        public void addFragment(Fragment fragment,String title)
        {
            fragmentlist.add(fragment);
            fragTitle.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragTitle.get(position);
        }
    }

    private void status(String status){
        DatabaseReference reference1= FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("status",status);
        reference1.updateChildren(hashMap);

    }
    @Override
    protected void onResume() {
        super.onResume();
        status("online");

    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

}
