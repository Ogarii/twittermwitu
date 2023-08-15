package com.example.twittermwitu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ProfilePage extends AppCompatActivity {
    String UserID;
    TextView Name, Age, editProfile, bio, Username;
    Button oval;

    ImageView imageView3;

    int SELECT_PICTURE = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        Age = findViewById(R.id.Age);
        Name = findViewById(R.id.Name);
        editProfile = findViewById(R.id.Edit);
        bio = findViewById(R.id.bio);
        Username = findViewById(R.id.Username);
        imageView3 = findViewById(R.id.imageView3);

        oval = findViewById(R.id.oval);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            UserID = user.getUid();
        } else {
            // No user is signed in
        }

        // get user Details

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("UserDetails").document(UserID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Toast.makeText(ProfilePage.this, "Loading Information", Toast.LENGTH_SHORT).show();

                    String FName = String.valueOf(document.get("FirstName"));
                    String SName = String.valueOf(document.get("SecondName"));
                    String fullName = FName + " " + SName;
                    String Agee = String.valueOf(document.get("Age"));
                    String Usernamee = String.valueOf(document.get("username"));


                    Name.setText(fullName);
                    Age.setText(Agee);
                    Username.setText(Usernamee);

                } else {
                    Toast.makeText(ProfilePage.this, "Error Retry", Toast.LENGTH_SHORT).show();
                }
            }
        });

        FirebaseFirestore db2 = FirebaseFirestore.getInstance();
        DocumentReference docRef2 = db2.collection("UpdatedDetails").document(UserID);
        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document1 = task.getResult();
                    Toast.makeText(ProfilePage.this, "Getting information", Toast.LENGTH_SHORT).show();

                    String Bio = String.valueOf(document1.get("Bio"));

                    bio.setText(Bio);
                }

            }
        });

        FirebaseFirestore db3 = FirebaseFirestore.getInstance();
        db3.collection("images").document(UserID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String imageUrl = documentSnapshot.getString("imageUrl");
                        // Use Picasso or Glide to load and display the image
                        Picasso.get().load(imageUrl).into(imageView3);
                    }
                })
                .addOnFailureListener(exception -> {
                    // Handle any errors during image retrieval
                });


        // sign Out
        oval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent SignIn = new Intent(ProfilePage.this, MainActivity.class);
                startActivity(SignIn);
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Edit = new Intent(ProfilePage.this, EditProfile.class);
                startActivity(Edit);
            }
        });
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ProfilePicture = new Intent(ProfilePage.this,SetProfilePicture.class);
                startActivity(ProfilePicture);

            }
        });

    }

}