package edu.fsu.cs.drunkmode_new;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class GroupMessageDisplay extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    TextView textView;
    String groupid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_group_message_display);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        Bundle bundle = getIntent().getExtras();
        groupid = bundle.getString("groupid");

        textView = findViewById(R.id.GroupName);

        DatabaseReference ref = database.getReference().child("GroupDetail").child(groupid).child("name");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getKey().equals("name"))
                {
                    textView.setText(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void displayUsers(View view) {
        Intent intent = new Intent(this, MembersDisplay.class);
        intent.putExtra("groupName", textView.getText().toString());
        intent.putExtra("groupID", groupid);
        startActivity(intent);
    }
}