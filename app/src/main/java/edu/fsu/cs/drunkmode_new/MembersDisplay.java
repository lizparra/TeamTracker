package edu.fsu.cs.drunkmode_new;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MembersDisplay extends AppCompatActivity {

    String groupName;
    String groupID;
    FirebaseDatabase database;
    ArrayAdapter adapter;
    ArrayList<String> memberlist= new ArrayList<String>();
    ArrayList<String> namedMemberList = new ArrayList<String>();

    Dialog dialog;
    EditText editText;
    Button cancel;
    Button confirm;

    String memberemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_members_display);

        database = FirebaseDatabase.getInstance();
        Bundle bundle = getIntent().getExtras();
        groupName = bundle.getString("groupName");
        groupID = bundle.getString("groupID");
        TextView textView = findViewById(R.id.memberGroupTitle);
        textView.setText(groupName);


        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, namedMemberList);


        ListView listView = (ListView) findViewById(R.id.member_list);
        listView.setAdapter(adapter);

        Query query = database.getReference().child("GroupDetail").child(groupID).child("members");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                memberlist.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    memberlist.add(snapshot.getValue().toString());
                }

                Query query1 = database.getReference().child("users");
                query1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        namedMemberList.clear();
                        HashMap<String, String> map = new HashMap<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren())
                        {
                            map.put(snapshot.getKey(), snapshot.getValue().toString());
                        }
                        for (String id : memberlist){
                            if (map.containsKey(id)){
                                namedMemberList.add(dataSnapshot.child(id).child("fullName").getValue().toString());
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String string = memberlist.get(i);
                Intent intent = new Intent(getApplicationContext(), ProfileDisplay.class);
                intent.putExtra("memberid", string);
                startActivity(intent);
            }
        });
    }

    public void addMember(View view) {
        showDialog(1);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.addmemberdialog);
        dialog.setTitle("Custom Dialog");

        cancel = (Button) dialog.findViewById(R.id.cancelButton);
        confirm = (Button) dialog.findViewById(R.id.confirmButton);
        editText = (EditText) dialog.findViewById(R.id.memberEmailInput);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeDialog(1);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                memberemail = editText.getText().toString();
                Query query = database.getReference().child("users");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean memberadded = false;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            if (snapshot.child("email").getValue().toString().equals(memberemail)) {
                                DatabaseReference ref2 = database.getReference().child("users").child(snapshot.getKey().toString()).child("groups").child(groupID);
                                ref2.setValue(true);
                                memberadded = true;
                            }
                        }
                        if (memberadded == false) {
                            Toast.makeText(getApplicationContext(), "Member not found!", Toast.LENGTH_LONG).show();
                        }
                        removeDialog(1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            if (snapshot.child("email").getValue().toString().equals(memberemail)) {
                                DatabaseReference ref = database.getReference().child("GroupDetail").child(groupID).child("members").push();
                                ref.setValue(snapshot.getKey().toString());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        return dialog;
    }
}
