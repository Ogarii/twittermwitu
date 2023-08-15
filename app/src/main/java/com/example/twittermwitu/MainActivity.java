package com.example.twittermwitu;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    String email , password;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
       // Make the status bar and navigation bar transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_main);




        EditText Email = findViewById(R.id.editTextTextEmailAddress);
        EditText Password = findViewById(R.id.editTextTextPassword);
        Button SignIn = findViewById(R.id.oval);
        TextView Forgotp = findViewById(R.id.textView);
        TextView Signup = findViewById(R.id.textView2);
        ImageView ShowPassword = findViewById(R.id.imageView6);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Intent i = new Intent(MainActivity.this, HomePage.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else {
            // User is signed out
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }




        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                mAuth = FirebaseAuth.getInstance();
                email = Email.getText().toString();
                password = Password.getText().toString();

                Login();

            }
        });


        Forgotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ForgotPassword = new Intent(MainActivity.this,ForgotPassword.class);
                startActivity(ForgotPassword);

            }
        });
        Signup. setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Signup = new Intent(MainActivity.this,Signup.class);
                startActivity(Signup);
            }
        });

        ShowPassword. setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Password.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    Password.setInputType( InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }else {
                    Password.setInputType( InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD );
                }
                Password.setSelection(Password.getText().length());
            }

        });

    }
    public void Login (){
        Intent Homepage = new Intent(MainActivity.this,HomePage.class);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override

            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    startActivity(Homepage);
                    overridePendingTransition(R.anim.slide_in_right,  R.anim.slide_out_left);
                    Toast.makeText(MainActivity.this, "Logging In",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, "Authentication failed",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

}