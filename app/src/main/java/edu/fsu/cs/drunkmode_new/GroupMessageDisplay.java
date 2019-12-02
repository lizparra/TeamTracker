package edu.fsu.cs.drunkmode_new;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GroupMessageDisplay extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    TextView textView;
    String groupid;

    String userName;

    RecyclerView recyclerView;
    List<Message> mchat;

    MessageAdapter messageAdapter;

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

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


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

        DatabaseReference reference = database.getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("fullName");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userName = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        readMessages();
    }

    public void sendMessage(View view) {
        EditText editText = findViewById(R.id.messageInput);
        String message = editText.getText().toString();
        if (!message.equals("")) {
            editText.setText("");
            DatabaseReference ref = database.getReference().child("messages").child(groupid).push();

            String currentDateandTime = new Date().toGMTString();

            ref.child("text").setValue(message);
            ref.child("sentBy").setValue(mAuth.getCurrentUser().getUid());
            ref.child("sentByName").setValue(userName);
            ref.child("timesent").setValue(currentDateandTime);

            DatabaseReference ref1 = database.getReference().child("GroupDetail").child(groupid).child("lastMessage");
            ref1.child("text").setValue(message);
            ref1.child("sentBy").setValue(mAuth.getCurrentUser().getUid());
            ref1.child("sentByName").setValue(userName);
            ref1.child("timesent").setValue(currentDateandTime);
        }


    }

    public void displayUsers(View view) {
        Intent intent = new Intent(this, MembersDisplay.class);
        intent.putExtra("groupName", textView.getText().toString());
        intent.putExtra("groupID", groupid);
        startActivity(intent);
    }

    private void readMessages(){
        mchat = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("messages").child(groupid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Message message = snapshot.getValue(Message.class);
                    mchat.add(message);
                    messageAdapter = new MessageAdapter(GroupMessageDisplay.this, mchat);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}