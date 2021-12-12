package com.example.esport3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.esport3.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class UploadActivity extends AppCompatActivity {
    VideoView videoView;
    EditText title;
    Button btnUpload,btnChoose;
    Uri videouri;
    MediaController mediaController;
    StorageReference storageReference;
    String uridownload,username,imageUrl;
    FirebaseUser firebaseUser;
    ProgressBar progressBar;
    TextView show_progrss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        videoView=findViewById(R.id.uploadVideo);
        btnChoose=findViewById(R.id.button_choose);
        btnUpload=findViewById(R.id.button_upload);
        title=findViewById(R.id.video_title);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        progressBar=findViewById(R.id.progress);
        show_progrss=findViewById(R.id.show_progress);

        storageReference= FirebaseStorage.getInstance().getReference("Videos/").child(firebaseUser.getUid());

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        mediaController=new MediaController(UploadActivity.this);
                        videoView.setMediaController(mediaController);
                        mediaController.setAnchorView(videoView);
                    }
                });
            }
        });


        videoView.start();

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseVideo();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadVideos();
            }
        });
    }
    private void chooseVideo() {
        Intent intent=new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Video"),1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            videouri=data.getData();
            videoView.setVideoURI(videouri);
        }
    }
    private  void uploadVideos(){
        String txt_title=title.getText().toString();
        if(TextUtils.isEmpty(txt_title)){
            title.setError("Video title is required");
            return;
        }

        final LinearLayout layout=findViewById(R.id.layout_progress);
        layout.setVisibility(View.VISIBLE);
        final StorageReference riversRef = storageReference.child(title.getText().toString());
        final DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());
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
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(false);

        riversRef.putFile(videouri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress=(100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                progressBar.setProgress((int)progress);
                show_progrss.setText(progress+"%");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        show_progrss.append("Video Uploaded");
                        Toast.makeText(getApplicationContext(), "uploaded successfully", Toast.LENGTH_SHORT).show();
                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                 uridownload=uri.toString();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                DatabaseReference reference= FirebaseDatabase.getInstance()
                                        .getReference().child("videos");
                                String videoid=reference.push().getKey();
                                HashMap<String,Object> hashMap=new HashMap<>();
                                if(title.getText().toString().equals("")){
                                    String default_title="No title";
                                    hashMap.put("title",default_title);
                                }
                                else
                                {
                                    hashMap.put("title",title.getText().toString());
                                }
                                hashMap.put("videoUrl",uridownload);
                                hashMap.put("userid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                                hashMap.put("Channel",username);
                                hashMap.put("videoId",videoid);
                                hashMap.put("imageURL",imageUrl);
                                assert videoid != null;
                                reference.child(videoid).setValue(hashMap);


                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                Toast.makeText(UploadActivity.this, "Video Uploaded", Toast.LENGTH_SHORT).show();
                                layout.setVisibility(View.GONE);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(UploadActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        layout.setVisibility(View.GONE);
                    }
                });
    }
}
