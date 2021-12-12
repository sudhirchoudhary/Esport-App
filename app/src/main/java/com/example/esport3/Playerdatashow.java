package com.example.esport3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Playerdatashow extends AppCompatActivity {
    Intent intent;
   Button b;
    TextView textTitle,tcharname,tcharid,tphone,tadress,tserver,tTier,tkd,tstate,tExpi,tassualtSkill,tsnipe,tTact,tIgl,tSupport,tEntry,tSniperole,tseason;
    TextView t,t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12,t13,t14,t15,t16;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playerdatashow);

        textTitle=findViewById(R.id.name_he);
        tcharname=findViewById(R.id.textView13);
        tcharid=findViewById(R.id.textView15);
        tphone=findViewById(R.id.textView17);
        tserver=findViewById(R.id.textView19);
        tTier=findViewById(R.id.textView21);
        tkd=findViewById(R.id.textView23);
        tstate=findViewById(R.id.textView25);
        tadress=findViewById(R.id.adress_id);
        tseason=findViewById(R.id.textView29);
        tassualtSkill=findViewById(R.id.textView31);
        tsnipe=findViewById(R.id.textView33);
        tTact=findViewById(R.id.textView35);
        tEntry=findViewById(R.id.textView41);
        tIgl=findViewById(R.id.textView37);
        tSniperole=findViewById(R.id.textView39);
        tSupport=findViewById(R.id.textView43);
        tExpi=findViewById(R.id.textView45);

        t1=findViewById(R.id.textView14);
        t2=findViewById(R.id.textView16);
        t3=findViewById(R.id.textView18);
        t4=findViewById(R.id.textView20);
        t5=findViewById(R.id.textView22);
        t6=findViewById(R.id.textView24);
        t7=findViewById(R.id.textView26);
        t8=findViewById(R.id.textView30);
        t9=findViewById(R.id.textView32);
        t10=findViewById(R.id.textView34);
        t11=findViewById(R.id.textView36);
        t12=findViewById(R.id.textView38);
        t13=findViewById(R.id.textView40);
        t14=findViewById(R.id.textView42);
        t15=findViewById(R.id.textView44);
        t16=findViewById(R.id.textView46);
        t=findViewById(R.id.adress2_id);
        intent=getIntent();
        final String userid=intent.getStringExtra("userid");

        assert userid != null;
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                t1.setText(dataSnapshot.child("Charater Name").getValue(String.class));
                t2.setText(dataSnapshot.child("Character ID").getValue(String.class));
                t3.setText(dataSnapshot.child("Phone Number").getValue(String.class));
                t4.setText(dataSnapshot.child("server").getValue(String.class));
                Double kdt=dataSnapshot.child("kd").getValue(Double.class);
                if(kdt!=null){
                    kdt=kdt*(-1);
                    String kds=kdt.toString();
                    t6.setText(kds);
                }
                else{
                    t6.setText("Not Updated");
                }
                t.setText(dataSnapshot.child("Address").getValue(String.class));
                t8.setText(dataSnapshot.child("Season").getValue(String.class));
                t7.setText(dataSnapshot.child("State").getValue(String.class));
                if(dataSnapshot.child("IGL Role").getValue(String.class)!=null){
                    t12.setText(dataSnapshot.child("IGL Role").getValue(String.class));
                }
                else{
                    t12.setText("No");
                }
                t10.setText(dataSnapshot.child("Sniping Skill").getValue(String.class));
                t13.setText(dataSnapshot.child("Sniper Role").getValue(String.class));
                t14.setText(dataSnapshot.child("Entry Fragger Role").getValue(String.class));
                t15.setText(dataSnapshot.child("Support Role").getValue(String.class));
                t11.setText(dataSnapshot.child("Tactical Skill").getValue(String.class));
                t9.setText(dataSnapshot.child("Assault skill").getValue(String.class));
                t16.setText(dataSnapshot.child("Experience").getValue(String.class));
                t5.setText(dataSnapshot.child("tier").getValue(String.class));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        b=findViewById(R.id.btn_chat);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(Playerdatashow.this,MessageActivity.class);
                intent2.putExtra("userid",userid);
                Playerdatashow.this.startActivity(intent2);
            }
        });
    }
}
