package com.zero.groupchat.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zero.groupchat.adapter.AddMemberAdapter;
import com.zero.groupchat.adapter.MembersAdapter;
import com.zero.groupchat.controller.UserController;
import com.zero.groupchat.databinding.ActivityChatBinding;
import com.zero.groupchat.databinding.ActivityNewChatBinding;
import com.zero.groupchat.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewChatActivity extends AppCompatActivity {

    private ActivityNewChatBinding binding;

    private MembersAdapter membersAdapter;
    private List<User> addedMembers;
    private UserController userController = UserController.getInstance();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference groupChatsReference = database.getReference("chats");
    DatabaseReference usersReference = database.getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userController.setUserList(null);

        binding.fabAdd.setOnClickListener(view -> startActivity(new Intent(this, AddMembersActivity.class)));

        binding.btnCreate.setOnClickListener(view -> {
            createGroupChat();
        });

        initRecyclerView();
    }

    private void createGroupChat() {
        if (validate()){
            List<String> userIds = new ArrayList<>();
            for (User user: userController.getUserList()){
                userIds.add(user.getUserId());
            }
            DatabaseReference newGroupReference = groupChatsReference.push();
            String chatId = newGroupReference.getKey();
            newGroupReference.child("members")
                    .setValue(userIds).addOnCompleteListener(this, task -> {

                        HashMap<String, Object> dataMap = new HashMap<>();
                        for (String user: userIds){
                            dataMap.put(user + "/myChats/" + chatId, chatId);
                        }


                        usersReference.updateChildren(dataMap, (error, ref) -> {
                            Toast.makeText(this, "Group Created Successfully!", Toast.LENGTH_LONG).show();
                            finish();
                        });
                    });
        }
    }

    private boolean validate() {
        if (userController.getUserList() != null) {
            if (!userController.getUserList().isEmpty()){
                if (!binding.etGroupName.getText().toString().trim().isEmpty()){
                    return true;
                }
                else{
                    Toast.makeText(this, "Group name required", Toast.LENGTH_LONG).show();
                    binding.etGroupName.setError("Group name required");
                    return false;
                }
            }
            else{
                Toast.makeText(this, "Members cannot be empty", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        else{
            Toast.makeText(this, "Members cannot be empty", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        addedMembers = userController.getUserList();
        if (addedMembers == null) addedMembers = new ArrayList<>();

        Log.d("TAG", "onResume: " + addedMembers);

        membersAdapter.updateData(addedMembers);
    }

    private void initRecyclerView() {
        binding.rvMembers.setLayoutManager(new LinearLayoutManager(this));
        binding.rvMembers.setHasFixedSize(true);
        membersAdapter = new MembersAdapter(this, new ArrayList<>());
        binding.rvMembers.setAdapter(membersAdapter);
    }
}