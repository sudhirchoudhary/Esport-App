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
import com.rengwuxian.materialedittext.MaterialEditText;

public class LoginActivity extends AppCompatActivity {
    EditText email,password;
    Button btn_login;
    TextView t1;

    FirebaseAuth auth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /**Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        t1=findViewById(R.id.textViewreg);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
                finish();
            }
        });
        progressBar=findViewById(R.id.player_progressbar);

        auth=FirebaseAuth.getInstance();

        email=findViewById(R.id.loginemail);
        password=findViewById(R.id.loginpass);

        btn_login=findViewById(R.id.buttonlogin);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                btn_login.setText("Logging In.....");
                String txt_email=email.getText().toString();
                String txt_password=password.getText().toString();

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

                auth.signInWithEmailAndPassword(txt_email,txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()){
                           progressBar.setVisibility(View.INVISIBLE);
                           btn_login.setText("LOGIN");
                           Intent i=new Intent(getApplicationContext(),StartActivity.class);
                           i.addFlags(i.FLAG_ACTIVITY_CLEAR_TASK|i.FLAG_ACTIVITY_NEW_TASK);
                           startActivity(i);
                           finish();
                       }
                       else{
                           btn_login.setText("LOGIN");
                           Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                           progressBar.setVisibility(View.INVISIBLE);
                       }
                    }
                });
            }
        });
    }
}
