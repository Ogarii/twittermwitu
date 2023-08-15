package com.example.twittermwitu;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class HomePage extends AppCompatActivity {

    EditText editTextTextPersonName3;
    Button oval,oval2;
    ImageView imageView7;
    String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_home_page);

        oval = findViewById(R.id.oval);
        oval2 = findViewById(R.id.oval2);
        imageView7 = findViewById(R.id.imageView7);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            UserID =   user.getUid();
        } else {
            // No user is signed in
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("images").document(UserID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String imageUrl = documentSnapshot.getString("imageUrl");
                        // Use Picasso or Glide to load and display the image
                        Picasso.get().load(imageUrl).into(imageView7);
                    }
                })
                .addOnFailureListener(exception -> {
                    // Handle any errors during image retrieval
                });

        imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent Profpage = new Intent(HomePage.this,ProfilePage.class);
                startActivity(Profpage);
            }
        });

        oval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Tweet = new Intent(HomePage.this,TweetPage.class);
                startActivity(Tweet);

            }
        });
        oval2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent PullTweets = new Intent(HomePage.this,showtweets.class);
                startActivity(PullTweets);
            }
        });
    }
}