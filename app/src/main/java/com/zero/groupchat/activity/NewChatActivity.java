package com.zero.groupchat.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.zero.groupchat.adapter.AddMemberAdapter;
import com.zero.groupchat.adapter.MembersAdapter;
import com.zero.groupchat.controller.UserController;
import com.zero.groupchat.databinding.ActivityChatBinding;
import com.zero.groupchat.databinding.ActivityNewChatBinding;
import com.zero.groupchat.model.User;

import java.util.ArrayList;
import java.util.List;

public class NewChatActivity extends AppCompatActivity {

    private ActivityNewChatBinding binding;

    private MembersAdapter membersAdapter;
    private List<User> addedMembers;
    private UserController userController = UserController.getInstance();

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