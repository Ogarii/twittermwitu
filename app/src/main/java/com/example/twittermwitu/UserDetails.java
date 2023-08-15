package com.example.twittermwitu;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserDetails extends AppCompatActivity {

    EditText editTextTextPersonName7,editTextTextPersonName6,editTextTextPersonName5,editTextTextPersonName4,Username;
    Button button2;

    String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_user_details);

        editTextTextPersonName4 =findViewById(R.id.editTextTextPersonName4);
        editTextTextPersonName5 =findViewById(R.id.editTextTextPersonName5);
        editTextTextPersonName6=findViewById(R.id.editTextTextPersonName6);
        editTextTextPersonName7 =findViewById(R.id.editTextTextPersonName7);
        Username = findViewById(R.id.Username);

        button2 = findViewById(R.id.button2);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDetailsdb();

            }
        });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            UserID =   user.getUid();
        } else {
            // No user is signed in
        }
    }

    public void UserDetailsdb(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String FirstName = editTextTextPersonName4.getText().toString();
        String SecondName = editTextTextPersonName5.getText().toString();
        String Age = editTextTextPersonName6.getText().toString();
        String Gender = editTextTextPersonName7.getText().toString();
        String UserName = Username.getText().toString();

        Map<String, Object>UserDetail = new HashMap<>();

        UserDetail.put("FirstName", FirstName);
        UserDetail.put("SecondName", SecondName);
        UserDetail.put("username",UserName);
        UserDetail.put("Age", Age);
        UserDetail.put("Gender",Gender);
        UserDetail.put("UserId", UserID);


        db.collection("UserDetails").document(UserID).set(UserDetail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(UserDetails.this, "Success", Toast.LENGTH_SHORT).show();
                Intent Homepage = new Intent(UserDetails.this,HomePage.class);
                startActivity(Homepage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserDetails.this, "Error Retry", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
