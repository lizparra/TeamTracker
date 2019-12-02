package edu.fsu.cs.drunkmode_new;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class ProfileDisplay extends AppCompatActivity {

    TextView usersName;
    TextView usersEmail;

    String memberid;

    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_profile_display);

        Bundle bundle = getIntent().getExtras();
        memberid = bundle.getString("memberid");

        usersEmail = findViewById(R.id.memberEmail);
        usersName = findViewById(R.id.memberName);

        database = FirebaseDatabase.getInstance();

        Query query = database.getReference().child("users").child(memberid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals("fullName")) {
                        usersName.setText(snapshot.getValue().toString());
                    }
                    else if (snapshot.getKey().equals("email")) {
                        usersEmail.setText(snapshot.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void openMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("userid", memberid);
        startActivity(intent);
    }
}
