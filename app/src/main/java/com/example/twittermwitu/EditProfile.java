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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {
    EditText name , Bio ,country,Phonenumber, Age, UserName;
    Button oval;
    String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        name = findViewById(R.id.name);
        Bio = findViewById(R.id.Bio);
        country = findViewById(R.id.country);
        Phonenumber = findViewById(R.id.Phonenumber);
        Age = findViewById(R.id.Age);
        UserName = findViewById(R.id.USernamee);


        oval = findViewById(R.id.oval);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            UserID =   user.getUid();
        } else {
            // No user is signed in
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef =  db.collection("UpdatedDetails").document(UserID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                Toast.makeText(EditProfile.this, "Setting Info", Toast.LENGTH_SHORT).show();

                String bio= String.valueOf(document.get("Bio"));
                String Country = String.valueOf(document.get("Country"));
                String phoneNumber= String.valueOf(document.get("PhoneNumber"));

                Bio.setText(bio);
                country.setText(Country);
                Phonenumber.setText(phoneNumber);

            }

        });
        FirebaseFirestore db1 = FirebaseFirestore.getInstance();
        DocumentReference docRef1 =  db1.collection("UserDetails").document(UserID);
        docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                Toast.makeText(EditProfile.this, "Setting Info", Toast.LENGTH_SHORT).show();

                String FName = String.valueOf(document.get("FirstName"));
                String SName = String.valueOf(document.get("SecondName"));
                String fullName = FName +" "+ SName;
                String Username = String.valueOf(document.get("username"));
                String age = String.valueOf(document.get("Age"));

                name.setText(fullName);
                Age.setText(age);
                UserName.setText(Username);

            }
        });



        oval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateInfoNew();
                UpdateExisting1();
                startActivity(new Intent(EditProfile.this,ProfilePage.class));
                overridePendingTransition(R.anim.slide_in_right,  R.anim.slide_out_left);
            }
        });


    }

    public void UpdateInfoNew(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String bio = Bio.getText().toString();
        String Country = country.getText().toString();
        String PhoneNumber = Phonenumber.getText().toString();

        Map<String, Object> NewDetails = new HashMap<>();

        NewDetails.put("Bio",bio);
        NewDetails.put("Country",Country);
        NewDetails.put("PhoneNumber", PhoneNumber);

        db.collection("UpdatedDetails").document(UserID).set(NewDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(EditProfile.this, "Success", Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(EditProfile.this, "Retry", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    // set Existing names to the fields
    //Existing Details
    public void SetExistingDetails(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef =  db.collection("UserDetails").document(UserID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                Toast.makeText(EditProfile.this, "Setting Info", Toast.LENGTH_SHORT).show();

                String FName = String.valueOf(document.get("FirstName"));
                String SName = String.valueOf(document.get("SecondName"));
                String fullName = FName +" "+ SName;
                String Username = String.valueOf(document.get("username"));
                String age = String.valueOf(document.get("Age"));

                name.setText(fullName);
                Age.setText(age);
                UserName.setText(Username);

            }
        });
    }
    //new Details
    public void setNewDetails(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef =  db.collection("UpdatedDetails").document(UserID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                Toast.makeText(EditProfile.this, "Setting Info", Toast.LENGTH_SHORT).show();

                String bio= String.valueOf(document.get("Bio"));
                String Country = String.valueOf(document.get("Country"));
                String phoneNumber= String.valueOf(document.get("PhoneNumber"));

                Bio.setText(bio);
                country.setText(Country);
                Phonenumber.setText(phoneNumber);

            }

        });

    }
    public void UpdateExisting1() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("UserDetails").document(UserID);
        String Name = name.getText().toString();

        Map<String, Object> updates = new HashMap<>();
        updates.put("FirstName", Name);

        docRef.update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditProfile.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors
                    }
                });




    }

}