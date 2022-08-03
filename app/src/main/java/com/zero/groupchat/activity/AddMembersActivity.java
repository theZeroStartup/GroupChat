package com.zero.groupchat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zero.groupchat.adapter.AddMemberAdapter;
import com.zero.groupchat.controller.UserController;
import com.zero.groupchat.databinding.ActivityAddMembersBinding;
import com.zero.groupchat.databinding.ActivityNewChatBinding;
import com.zero.groupchat.listener.ItemClickListener;
import com.zero.groupchat.model.User;

import java.util.ArrayList;
import java.util.List;

public class AddMembersActivity extends AppCompatActivity implements ItemClickListener {

    private ActivityAddMembersBinding binding;

    private AddMemberAdapter addMemberAdapter;
    private UserController userController = UserController.getInstance();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference usersReference = database.getReference("users");

    private List<User> addedMembers;
    private List<String> addedMemberNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddMembersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addedMembers = userController.getUserList();
        if (addedMembers == null) addedMembers = new ArrayList<>();
        addedMemberNames = new ArrayList<>();
        for (User user: addedMembers){
            addedMemberNames.add(user.getFullName());
        }

        binding.fabAdd.setOnClickListener(view -> {
            userController.setUserList(null);
            userController.setUserList(addedMembers);
            finish();
        });

        initRecyclerView();
        getUsers();
    }

    private void getUsers() {
        usersReference.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {

            }
            else {
                DataSnapshot dataSnapshot = task.getResult();

                List<User> usersList = new ArrayList<>();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);

                    user.setUserId(snapshot.getKey());

                    user.setAdded(addedMemberNames.contains(user.getFullName()));

                    usersList.add(user);
                }

                addMemberAdapter.updateData(usersList);
            }
        });
    }

    private void initRecyclerView() {
        binding.rvMembers.setLayoutManager(new LinearLayoutManager(this));
        binding.rvMembers.setHasFixedSize(true);
        addMemberAdapter = new AddMemberAdapter(this, new ArrayList<User>(), this);
        binding.rvMembers.setAdapter(addMemberAdapter);
    }

    @Override
    public void onItemClicked(int position, String type) {
        if (type.equals("Add")){
            addedMembers.add(addMemberAdapter.getMembersList().get(position));
        }
        else{
            int removeAtPosition = -1;
            for (int i = 0; i < addedMemberNames.size(); i++){
                if (addMemberAdapter.getMembersList().get(position).getFullName().equals(addedMemberNames.get(i))){
                    removeAtPosition = i;
                }
            }
            if (removeAtPosition != -1)
                addedMembers.remove(removeAtPosition);
        }
    }
}