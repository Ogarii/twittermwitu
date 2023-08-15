package com.example.twittermwitu;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class showtweets extends AppCompatActivity {

    private RecyclerView recyclerview;

    private TwitAdapter twitAdapter;

    private List<Tweet>tweetlist;

    private FirebaseFirestore db;

    String UserID;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_showtweets);

        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(showtweets.this));

        db = FirebaseFirestore.getInstance();
        tweetlist = new ArrayList<>();
        twitAdapter = new TwitAdapter(showtweets.this, tweetlist);

        recyclerview.setAdapter(twitAdapter);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            UserID = user.getUid();
        } else {
            // No user is signed in
        }


        fetchTweets();



    }

    private void fetchTweets() {
        db.collection("Twit")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Tweet user = documentSnapshot.toObject(Tweet.class);
                        tweetlist.add(user);
                    }
                    twitAdapter.notifyDataSetChanged();
                    Log.d(TAG, "pulling tweet");
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "Error fetching users: " + e.getMessage());
                });
    }
  
    public class TwitAdapter extends RecyclerView.Adapter<TwitAdapter.UserViewHolder> {
        private List<Tweet> userList;

        public TwitAdapter(Context context, List<Tweet> userList) {
            this.userList = userList;
        }




        @NonNull
        @Override
        public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(showtweets.this).inflate(R.layout.tweet, parent, false);
            return new UserViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
            Tweet user = userList.get(position);
            holder.bind(user);
        }

        @Override
        public int getItemCount() {
            return userList.size();
        }

        public class UserViewHolder extends RecyclerView.ViewHolder{
            private TextView thetweet, textView8;
            private CardView tweetCardView;
            private ImageView imageView16;

            private String Username;


            UserViewHolder(@NonNull View itemView) {
                super(itemView);

                thetweet = itemView.findViewById(R.id.textView7);
                tweetCardView = itemView.findViewById(R.id.tweetCardView);
                imageView16 = itemView.findViewById(R.id.imageView16);
                textView8 = itemView.findViewById(R.id.textView8);
            }

            void bind(Tweet user) {

                thetweet.setText(user.getTwit());
                Log.d(TAG, "pulling tweet" + user.getTwit());

                // Set profile picture
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("images").document(user.getUserID()).get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                String imageUrl = documentSnapshot.getString("imageUrl");
                                // Use Picasso or Glide to load and display the image
                                Picasso.get().load(imageUrl).into(imageView16);
                            }
                        })
                        .addOnFailureListener(exception -> {
                            // Handle any errors during image retrieval
                        });

                // Set username
                FirebaseFirestore db2 = FirebaseFirestore.getInstance();
                db2.collection("UserDetails").document(user.getUserID())
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String username = documentSnapshot.getString("username");
                                textView8.setText(username);


                                // OnClickListener for tweetCardView
                                tweetCardView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent tweetInfoIntent = new Intent(itemView.getContext(), TweetDetails.class);
                                        tweetInfoIntent.putExtra("twit", user.getDocumentId());
                                        tweetInfoIntent.putExtra("Username", username);


                                        itemView.getContext().startActivity(tweetInfoIntent);
                                    }
                                });
                            }
                        });

            }

        }
    }
}