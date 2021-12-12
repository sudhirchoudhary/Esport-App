package com.example.esport3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    static int SPLASH_TIME_OUI = 3000;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

    ImageView logo;
    TextView appName;
    Animation top,bottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));
        setContentView(R.layout.activity_main);

         logo=findViewById(R.id.logo);
         appName=findViewById(R.id.textView10);
         top= AnimationUtils.loadAnimation(this,R.anim.top);
         bottom= AnimationUtils.loadAnimation(this,R.anim.bottom);
         logo.setAnimation(top);
         appName.setAnimation(bottom);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(firebaseAuth.getCurrentUser()!=null)
                {
                    startActivity(new Intent(getApplicationContext(),StartActivity.class));
                }
                else
                {
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                }
                finish();


            }
        }, SPLASH_TIME_OUI);


    }
}
