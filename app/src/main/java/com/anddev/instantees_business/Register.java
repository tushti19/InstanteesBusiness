package com.anddev.instantees_business;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class Register extends AppCompatActivity {

    FirebaseAuth Auth;
    DatabaseReference reference;

    MaterialEditText email,pass;
    Button btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        btn_save = findViewById(R.id.register);

        Auth = FirebaseAuth.getInstance();

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Email = email.getText().toString();
                String Password = pass.getText().toString();

                if(TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password)){
                    Toast.makeText(Register.this,"All Fields are required",Toast.LENGTH_SHORT).show();
                }
                else if(Password.length()<6){
                    Toast.makeText(Register.this, "Password should contain at least 6 characters", Toast.LENGTH_SHORT).show();
                }
                else{

                    Auth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isComplete()){
                                Log.d("User","Created");
                                FirebaseUser firebaseUser = Auth.getCurrentUser();
                                reference = FirebaseDatabase.getInstance().getReference("Retailers");

                                HashMap<String,String> hashMap = new HashMap<>();
                                hashMap.put("id",firebaseUser.getUid());
                                hashMap.put("Email" ,firebaseUser.getEmail());

                                reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isSuccessful()){
                                            Toast.makeText(Register.this, "User Registered", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(Register.this,home_page.class));
                                        }

                                        else
                                            Toast.makeText(Register.this, "Register Failed", Toast.LENGTH_SHORT).show();

                                    }
                                });



                            }
                            else
                                Log.d("User" , "Null");

                        }
                    });

                }



            }
        });
    }
}
