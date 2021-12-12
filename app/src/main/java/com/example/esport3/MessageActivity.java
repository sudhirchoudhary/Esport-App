package com.example.esport3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.esport3.Adapter.MessageAdapter;
import com.example.esport3.Fragments.APIService;
import com.example.esport3.Model.Chat;
import com.example.esport3.Model.User;
import com.example.esport3.Notifications.Client;
import com.example.esport3.Notifications.Data;
import com.example.esport3.Notifications.MyResponse;
import com.example.esport3.Notifications.Sender;
import com.example.esport3.Notifications.Token;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {
    CircleImageView profileImage;
    TextView username;

    FirebaseUser firebaseUser;
    DatabaseReference reference;


    Intent intent;

    ImageButton send_btn;
    EditText msg_txt;

    MessageAdapter adapter;
    List<Chat>  mChat;

    RecyclerView recyclerView;

    ValueEventListener seenListner;
    String userid;

    APIService apiService;
    boolean notify=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar=findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessageActivity.this,StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });


        recyclerView=findViewById(R.id.chat_recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        send_btn=findViewById(R.id.btn_send);
        msg_txt=findViewById(R.id.mdg_text);

        profileImage=findViewById(R.id.profile_image);
        username=findViewById(R.id.username);

        intent=getIntent();
        userid=intent.getStringExtra("userid");
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        assert userid != null;
        reference= FirebaseDatabase.getInstance().getReference().child("users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                assert user != null;
                username.setText(user.getUsername());
                if (user.getImageURL().equals("deafault")){
                    profileImage.setImageResource(R.drawable.profile);
                }
                else
                {
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profileImage);
                }
                readMessage(firebaseUser.getUid(),userid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify=true;
                String msg=msg_txt.getText().toString();
                if (!msg.equals("")){
                    onSendMessage(FirebaseAuth.getInstance().getCurrentUser().getUid(),userid,msg);

                }else {
                    Toast.makeText(MessageActivity.this, "Can't send empty message", Toast.LENGTH_SHORT).show();
                }
                msg_txt.setText("");
            }
        });
        seenMessage(userid);
    }

    private void seenMessage(final String userid){
        reference=FirebaseDatabase.getInstance().getReference().child("Chats");
        seenListner=reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                   Chat chat=snapshot.getValue(Chat.class);
                   if (chat.getReceiver().equals(firebaseUser.getUid())&&chat.getSender().equals(userid)){
                       HashMap<String,Object> map=new HashMap<>();
                       map.put("isseen",true);
                       snapshot.getRef().updateChildren(map);
                   }
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void onSendMessage(String sender, final String receiver, String message){
        DatabaseReference newref=FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> map=new HashMap<>();
        map.put("sender",sender);
        map.put("receiver",receiver);
        map.put("message",message);
        map.put("isseen",false);

        newref.child("Chats").push().setValue(map);

    }

    private void sendNotification(String receiver, final String username, final String message){
        DatabaseReference tokens=FirebaseDatabase.getInstance().getReference("Tokens");
        Query query=tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Token token=snapshot.getValue(Token.class);
                    Data data=new Data(firebaseUser.getUid(),R.mipmap.ic_launcher,username+": "+message,"New Message",userid);
                    Sender sender=new Sender(data,token.getToken());
                    apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if (response.code()==200){
                                if (response.body().success!=1){
                                    Toast.makeText(MessageActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void readMessage(final String myid, final String userid){
        mChat=new ArrayList<>();

        reference=FirebaseDatabase.getInstance().getReference().child("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chat chatManage=snapshot.getValue(Chat.class);
                    if(chatManage.getReceiver().equals(myid)&&chatManage.getSender().equals(userid)||
                            chatManage.getReceiver().equals(userid)&&chatManage.getSender().equals(myid)){
                        mChat.add(chatManage);
                    }
                    adapter=new MessageAdapter(MessageActivity.this,mChat);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void status(String status){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("status",status);
        reference.updateChildren(hashMap);

    }
    @Override
    protected void onResume() {
        super.onResume();
        status("online");

    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListner);
        status("offline");
    }


}
