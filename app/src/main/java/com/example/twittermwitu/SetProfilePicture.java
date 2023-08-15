package com.example.twittermwitu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class SetProfilePicture extends AppCompatActivity {
    ImageView imageView15;
    Button button, button3, button4;
    int SELECT_PICTURE = 200;

    String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile_picture);

        imageView15 = findViewById(R.id.imageView15);
        button = findViewById(R.id.button);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            UserID = user.getUid();
        } else {
            // No user is signed in
        }
        // Retrieving and displaying the image in your Android application

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("images").document(UserID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String imageUrl = documentSnapshot.getString("imageUrl");
                        // Use Picasso or Glide to load and display the image
                        Picasso.get().load(imageUrl).into(imageView15);
                    }
                })
                .addOnFailureListener(exception -> {
                    // Handle any errors during image retrieval
                });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();

            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoreProfilepicture();
                Intent ProfilePage = new Intent(SetProfilePicture.this, ProfilePage.class);
                startActivity(ProfilePage);
            }
        });

    }

    //choose image from gallery
    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    imageView15.setImageURI(selectedImageUri);
                }
            }
        }
    }

    //Store Profile Picture in Firestore
    public void StoreProfilepicture() {
        FirebaseFirestore db1 = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        // Create a child reference
        // imagesRef now points to "images"
        StorageReference imagesRef = storageRef.child("images");
        // Child references can also take paths
        // spaceRef now points to "images/space.jpg
        // imagesRef still points to "images"
        StorageReference imageRef = storageRef.child("images/space.jpg");
        CollectionReference Imageref = db1.collection("images");


        // Get the data from an ImageView as bytes
        imageView15.setDrawingCacheEnabled(true);
        imageView15.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView15.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();


        UploadTask uploadTask = imagesRef.putBytes(data);

        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Image uploaded successfully, now save the image URL to Firestore
            imagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                // Save the image URL to Firestore
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> dataa = new HashMap<>();
                dataa.put("imageUrl", uri.toString());
                db.collection("images").document(UserID).set(dataa);

                Toast.makeText(this, "Saving New Profile Picture", Toast.LENGTH_SHORT).show();


            });
        }).addOnFailureListener(exception -> {
            // Handle any errors during image upload
        });

    }
}