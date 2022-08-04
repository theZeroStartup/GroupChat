package com.zero.groupchat.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zero.groupchat.R;
import com.zero.groupchat.adapter.ChatListAdapter;
import com.zero.groupchat.adapter.MembersAdapter;
import com.zero.groupchat.controller.UserController;
import com.zero.groupchat.databinding.ActivityChatListBinding;
import com.zero.groupchat.databinding.ActivityMainBinding;
import com.zero.groupchat.listener.ItemClickListener;
import com.zero.groupchat.model.User;

import java.util.ArrayList;
import java.util.List;

public class ChatListActivity extends AppCompatActivity implements ItemClickListener {

    private ActivityChatListBinding binding;

    private ChatListAdapter chatListAdapter;

    private UserController userController = UserController.getInstance();

    private List<User> myChatsList;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myChatsReference = database.getReference("users/" + userController.getUserId() + "/myChats");
    DatabaseReference usersReference = database.getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.fabAdd.setOnClickListener(view -> startActivity(new Intent(this, NewChatActivity.class)));

        initRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMyChats();
    }

    private void getMyChats() {
        myChatsReference.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {

            }
            else {
                DataSnapshot dataSnapshot = task.getResult();

                myChatsList = new ArrayList<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String chatName = snapshot.getValue(String.class);

                    User user = new User();
                    user.setUserName(chatName);
                    user.setChatId(snapshot.getKey());
                    myChatsList.add(user);
                }

                chatListAdapter.updateData(myChatsList);
            }
        });
    }

    private void initRecyclerView() {
        binding.rvChats.setLayoutManager(new LinearLayoutManager(this));
        binding.rvChats.setHasFixedSize(true);
        chatListAdapter = new ChatListAdapter(this, new ArrayList<>(), this);
        binding.rvChats.setAdapter(chatListAdapter);
    }

    @Override
    public void onItemClicked(int position, String type) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("chatId", chatListAdapter.getChatList().get(position).getChatId());
        startActivity(intent);
    }
}