package com.example.twittermwitu;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Signup extends AppCompatActivity {

    TextView editTextTextPersonName2,editTextTextPassword2;
    Button oval;


    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_signup);
        editTextTextPersonName2 = findViewById(R.id.editTextTextPersonName2);
        editTextTextPassword2 = findViewById(R.id.editTextTextPassword2);
        oval = findViewById(R.id.oval);
        mAuth = FirebaseAuth.getInstance();

        oval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();

            }
        });
    }

    public void registerUser(){
        String Email = editTextTextPersonName2.getText().toString().trim();
        String Password = editTextTextPassword2.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Signup.this, "User Created", Toast.LENGTH_SHORT).show();
                    Intent Login = new Intent(Signup.this,UserDetails.class);
                    startActivity(Login);
                }
                else {
                    Toast.makeText(Signup.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}