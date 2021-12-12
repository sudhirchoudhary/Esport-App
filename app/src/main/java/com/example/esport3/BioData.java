package com.example.esport3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class BioData extends AppCompatActivity {
    private CheckBox assault,sniper,tactical,entry,sniperrole,igl,support;
    private String assaultCheck,snipeCheck,tactCheck,entryCheck,sniperoleCheck,iglCheck,supportCheck;
    private FirebaseAuth fAuth;
    private EditText mFullname,mCharctername,mCharaterid,mPhoneno,mKd,mAddress;
    Button registerBtn;
    private Spinner spinner,spinner2,spinner3,spinner4,spinner5;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio_data);
        fAuth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.player_progressbar);

        //for editTexts
        mFullname=findViewById(R.id.Fullname_id);
        mCharctername=findViewById(R.id.character_name_id);
        mCharaterid=findViewById(R.id.Character_id);
        mPhoneno=findViewById(R.id.phoneid);
        mKd=findViewById(R.id.KD_id);
        mAddress=findViewById(R.id.address_id);

        //for spinner
        spinner =  findViewById(R.id.spinner_state_id);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(getApplicationContext(),R.array.state,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        spinner2= findViewById(R.id.spinner_season_id);
        ArrayAdapter<CharSequence>  adapter2=ArrayAdapter.createFromResource(getApplicationContext(),R.array.Season,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner2.setAdapter(adapter2);

        spinner3= findViewById(R.id.spinner_Tier_id);
        ArrayAdapter<CharSequence>  adapter3=ArrayAdapter.createFromResource(getApplicationContext(),R.array.Tier,android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner3.setAdapter(adapter3);

        spinner4=  findViewById(R.id.spinner_Expi_id);
        ArrayAdapter<CharSequence>  adapter4=ArrayAdapter.createFromResource(getApplicationContext(),R.array.Expi,android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner4.setAdapter(adapter4);

        spinner5=  findViewById(R.id.spinner_server_id);
        ArrayAdapter<CharSequence>  adapter5=ArrayAdapter.createFromResource(getApplicationContext(),R.array.server,android.R.layout.simple_spinner_item);
        adapter5.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner5.setAdapter(adapter5);

        //FOR CHECK BOX
        assault=findViewById(R.id.checkBox_assault_id);
        sniper=findViewById(R.id.checkBox_sniping_id);
        tactical=findViewById(R.id.checkBox_tactical_id);
        entry=findViewById(R.id.checkBox_fragger_id);
        sniperrole=findViewById(R.id.checkBox_sniper_id);
        igl=findViewById(R.id.checkBox_igl_id);
        support=findViewById(R.id.checkBox_support_id);
        assault.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (assault.isChecked())
                {
                    assaultCheck="yes";
                }
                else{
                    assaultCheck="No";
                }
            }
        });
        sniper.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (sniper.isChecked())
                {
                    snipeCheck="yes";
                }
                else{
                    snipeCheck="No";
                }
            }
        });

        tactical.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (tactical.isChecked())
                {
                    tactCheck="yes";
                }
                else{
                    tactCheck="No";
                }
            }
        });
        entry.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (entry.isChecked())
                {
                    entryCheck="yes";
                }
                else{
                    entryCheck="No";
                }
            }
        });
        sniperrole.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (sniperrole.isChecked())
                {
                    sniperoleCheck="yes";
                }
                else{
                    sniperoleCheck="No";
                }
            }
        });
        igl.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (igl.isChecked())
                {
                    iglCheck="yes";
                }
                else{
                    iglCheck="No";
                }
            }
        });
        support.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (support.isChecked())
                {
                    supportCheck="yes";
                }
                else{
                    supportCheck="No";
                }
            }
        });
        registerBtn=findViewById(R.id.Registerbtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                registerBtn.setText("Saving Data.....");
                progressBar.setVisibility(View.VISIBLE);
                if(fAuth.getCurrentUser() !=null){
                    addPlayer();
                }
            }
        });
    }
    private void addPlayer() {
        final String userid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String fullname=mFullname.getText().toString();
        final String phone=mPhoneno.getText().toString();
        final String characterName=mCharctername.getText().toString();
        final String characterId=mCharaterid.getText().toString();
        final String KD=mKd.getText().toString();
        final String adsress=mAddress.getText().toString();
        final String server=spinner5.getSelectedItem().toString();
        final String state=spinner.getSelectedItem().toString();
        final String season=spinner2.getSelectedItem().toString();
        final String tier=spinner3.getSelectedItem().toString();
        final String expi=spinner4.getSelectedItem().toString();
        final String assault=assaultCheck;
        final String snipe=snipeCheck;
        final String tactical=tactCheck;
        final String fragger=entryCheck;
        final String sniperole=sniperoleCheck;
        final String igl=iglCheck;
        final String support=supportCheck;
        double kdi=Double.parseDouble(KD);
        kdi=kdi*(-1);

        HashMap<String,Object> user=new HashMap<>();
        user.put("name", fullname);
        user.put("Charater Name", characterName);
        user.put("Character ID",characterId);
        user.put("Phone Number",phone);
        user.put("kd",kdi);
        user.put("Address",adsress);
        user.put("server",server);
        user.put("State",state);
        user.put("Season",season);
        user.put("tier",tier);
        user.put("Experience",expi);
        user.put("Assault skill",assault);
        user.put("Sniping skill",snipe);
        user.put("Tactical skill",tactical);
        user.put("Entry Fragger Role",fragger);
        user.put("Sniper Role",sniperole);
        user.put("IGL Role",igl);
        user.put("Support Role",support);

        final DatabaseReference newref=FirebaseDatabase.getInstance().getReference().child("users").child(userid);
        newref.updateChildren(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(BioData.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                registerBtn.setText("REGISTER");
                HashMap<String ,Object> hashMap=new HashMap<>();
                hashMap.put("registered","yes");
                newref.updateChildren(hashMap);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                registerBtn.setText("REGISTER");
            }
        });
    }
}