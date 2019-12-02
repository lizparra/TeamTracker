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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class GroupDisplayActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Dialog dialog;
    EditText editText;
    Button cancel;
    Button confirm;
    ArrayAdapter adapter;
    ArrayList<String> grouplist= new ArrayList<String>();
    ArrayList<String> namedGroupList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_group_display);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, namedGroupList);

        ListView listView = (ListView) findViewById(R.id.group_list);
        listView.setAdapter(adapter);

        Query query = database.getReference("users/" + mAuth.getUid() +"/groups");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                grouplist.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    if ((boolean) snapshot.getValue()){
                        grouplist.add((String) snapshot.getKey());
                    }
                }
                Query query1 = database.getReference("GroupDetail");

                query1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        namedGroupList.clear();
                        HashMap<String, String> map = new HashMap<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren())
                        {
                            map.put(snapshot.getKey(), snapshot.getValue().toString());
                        }
                        for (String id : grouplist){
                            if (map.containsKey(id)){
                                namedGroupList.add(dataSnapshot.child(id).child("name").getValue().toString());
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
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String string = grouplist.get(i);
                Intent intent = new Intent(getApplicationContext(), GroupMessageDisplay.class);
                intent.putExtra("groupid", string);
                startActivity(intent);
            }
        });

    }

    public void createGroupDialog(View view) {
        showDialog(1);
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.creategroupdialog);
        dialog.setTitle("Custom Dialog");

        cancel = (Button) dialog.findViewById(R.id.cancelButton);
        confirm = (Button) dialog.findViewById(R.id.confirmButton);
        editText = (EditText) dialog.findViewById(R.id.groupNameInput);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeDialog(1);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref = database.getReference().child("GroupDetail").push();
                ref.child("name").setValue(editText.getText().toString());
                ref.child("ownerid").setValue(mAuth.getCurrentUser().getUid());
                ref.child("lastMessage").child("text").setValue("Group Created!");
                ref.child("lastMessage").child("sentBy").setValue(mAuth.getCurrentUser().getUid());
                DatabaseReference membersref = ref.child("members").push();

                membersref.setValue(mAuth.getCurrentUser().getUid());

                String currentDateandTime = new Date().toGMTString();

                DatabaseReference ref2 = database.getReference().child("messages").child(ref.getKey()).push();
                ref2.child("text").setValue("Group Created!");
                ref2.child("sentBy").setValue(mAuth.getCurrentUser().getUid());
                ref2.child("timesent").setValue(currentDateandTime);

                DatabaseReference usergroup = database.getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("groups").child(ref.getKey());
                usergroup.setValue(true);
                removeDialog(1);
                updateList();
            }
        });

        return dialog;
    }

    private void updateList() {
        adapter.notifyDataSetChanged();
    }

    public void displayProfile(View view) {
        Intent intent = new Intent(this, ProfileDisplay.class);
        intent.putExtra("memberid", mAuth.getCurrentUser().getUid().toString());
        startActivity(intent);
    }

}
