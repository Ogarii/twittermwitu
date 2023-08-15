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
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    TextView editTextTextPersonName;
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
        setContentView(R.layout.activity_forgot_password);


        editTextTextPersonName = findViewById(R.id.editTextTextPersonName);
        oval = findViewById(R.id.oval);

        mAuth = FirebaseAuth.getInstance();


        oval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reAuth();


            }
        });

    }
    public void reAuth(){
        String Email = editTextTextPersonName.getText().toString();

      mAuth.sendPasswordResetEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
          @Override
          public void onComplete(@NonNull Task<Void> task) {
              if (task.isSuccessful()){
                  Intent Login = new Intent(ForgotPassword.this,MainActivity.class);
                  startActivity(Login);
                  Toast.makeText(ForgotPassword.this, "Email Sent", Toast.LENGTH_SHORT).show();
              }
              else{
                  Toast.makeText(ForgotPassword.this, "Incorrect Email", Toast.LENGTH_SHORT).show();
              }
              Intent Login = new Intent(ForgotPassword.this,MainActivity.class);
              startActivity(Login);


          }
      });


      

    }
}
