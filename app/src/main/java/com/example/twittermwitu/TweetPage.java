package com.example.twittermwitu;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TweetPage extends AppCompatActivity {

    EditText editTextTextPersonName8;

    String documentId;
    Button oval;
    String UserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_tweet_page);

        editTextTextPersonName8 = findViewById(R.id.editTextTextPersonName8);
        oval = findViewById(R.id.oval);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            UserID =   user.getUid();
        } else {
            // No user is signed in
        }



        oval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Database();

            }
        });

    }
    private void Database(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference twitRef = db.collection("Twit");
        DocumentReference documentReference = db.collection("twit").document(UserID);

        Map<String, Object> Twit = new HashMap<>();
        String Twitt = editTextTextPersonName8.getText().toString();
        Twit.put("twit", Twitt);
        Twit.put("UserID", UserID);


        db.collection("Twits").document().set(Twit).addOnSuccessListener(new OnSuccessListener<Void>() {

            @Override
            public void onSuccess(Void unused) {
                twitRef.add(Twit)
                        .addOnSuccessListener(documentReference -> {
                            // The document was added successfully, and documentReference.getId()
                            // will give you the dynamically generated document ID.
                            String documentId = documentReference.getId();
                            Map<String, Object> updateData = new HashMap<>();
                            updateData.put("documentId", documentId);

                            documentReference.update(updateData).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("Firestore", "Document Displayed with ID: " + documentId);
                                }
                            });

                            // You can use the documentId as needed
                            Log.d("Firestore", "Document added with ID: " + documentId);
                        })
                        .addOnFailureListener(e -> {
                            // Error adding document
                            Log.e("Firestore", "Error adding document: " );

                        });




                Toast.makeText(TweetPage.this, "Tweet successful", Toast.LENGTH_SHORT).show();
                Intent Homepage = new Intent(TweetPage.this,HomePage.class);
                startActivity(Homepage);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TweetPage.this, "Error Occurred", Toast.LENGTH_SHORT).show();

            }


        });




    }

}