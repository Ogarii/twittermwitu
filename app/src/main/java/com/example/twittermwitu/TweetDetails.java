package com.example.twittermwitu;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class TweetDetails extends AppCompatActivity {

    TextView textView10, UserName;
    ImageView imageView18;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_tweet_details);

        textView10 = findViewById(R.id.textView10);
        UserName = findViewById(R.id.Username);
        imageView18 = findViewById(R.id.imageView18);

        Intent intent = getIntent();

        // Check if the Intent has extra data with the key "twit"
        if (intent != null && intent.hasExtra("twit")) {
            // Retrieve the document ID and username from the Intent's extras
            String documentId = intent.getStringExtra("twit");
            String username = intent.getStringExtra("Username");


            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("Twit").document(documentId);

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String tweet = document.getString("twit");

                            textView10.setText(tweet);
                            UserName.setText(username);


                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });

        } else {
            Toast.makeText(TweetDetails.this, "Error: No Document ID found.", Toast.LENGTH_SHORT).show();
        }
    }
}