package com.example.esport3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    EditText username,email,password;
    Button btn_register;
    TextView t1;

    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        t1=findViewById(R.id.editText);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });
        auth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.player_progressbar);

        username=findViewById(R.id.nameid);
        email=findViewById(R.id.emailid);
        password=findViewById(R.id.passid);
        btn_register=findViewById(R.id.Registerbtn);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                btn_register.setText("REGISTERING USER....");
                String txt_username=username.getText().toString();
                String txt_email=email.getText().toString();
                String txt_password=password.getText().toString();

                if(TextUtils.isEmpty(txt_username)){
                    username.setError("Username is required");
                    return;
                }
                if(TextUtils.isEmpty(txt_email)){
                    email.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(txt_password)){
                    password.setError("Password is required");
                    return;
                }
                if(txt_password.length()<6){
                    password.setError("Must have atleast 6 character");
                    return;
                }
                register(txt_username,txt_email,txt_password);

            }
        });
    }
    private void register(final String username, String email, String password){
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    FirebaseUser user=auth.getCurrentUser();
                    assert user != null;
                    String userid=user.getUid();

                    reference=FirebaseDatabase.getInstance().getReference().child("users").child(userid);

                    HashMap<String,String > map=new HashMap<>();
                    map.put("id",userid);
                    map.put("username",username);
                    map.put("ImageURL","deafult");
                    map.put("status","offline");
                    map.put("search",username.toLowerCase());

                    reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                btn_register.setText("REGISTER");
                                Intent i=new Intent(getApplicationContext(),Category.class);
                                i.addFlags(i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();
                            }
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            HashMap<String ,Object> hashMap=new HashMap<>();
                            hashMap.put("registered","no");
                            reference.updateChildren(hashMap);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }
                else {
                    btn_register.setText("REGISTER");
                    Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}
