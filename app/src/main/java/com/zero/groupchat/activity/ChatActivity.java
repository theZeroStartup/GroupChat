package com.zero.groupchat.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zero.groupchat.adapter.ChatAdapter;
import com.zero.groupchat.adapter.ChatListAdapter;
import com.zero.groupchat.controller.UserController;
import com.zero.groupchat.databinding.ActivityChatBinding;
import com.zero.groupchat.databinding.ActivityChatListBinding;
import com.zero.groupchat.model.Chat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;

    private ChatAdapter chatAdapter;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myChatReference;
    private String chatId;

    private List<Chat> chatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initRecyclerView();
        chatId = getIntent().getStringExtra("chatId");
        myChatReference = database.getReference("chats/" + chatId + "/messages");

        getMessages();
        messageListener();

        binding.ibSend.setOnClickListener(view -> {
            if (!binding.etMessage.getText().toString().trim().isEmpty())
                sendAnswers(binding.etMessage.getText().toString().trim());
        });
    }

    private void messageListener() {
        myChatReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Chat chat = snapshot.getValue(Chat.class);

                if (chatList != null) {
                    chatList.add(chat);
                    chatAdapter.notifyItemInserted(chatList.size() - 1);
                    binding.rvChats.smoothScrollToPosition(chatList.size() - 1);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getMessages() {
        myChatReference.get().addOnCompleteListener(this, task -> {
            DataSnapshot snapshot = task.getResult();

            if (snapshot.exists()){
                chatList = new ArrayList<>();

                for (DataSnapshot s: snapshot.getChildren()){
                    Chat chat = s.getValue(Chat.class);

                    chatList.add(chat);
                }

                chatAdapter.updateData(chatList);
            }
        });
    }

    private void sendAnswers(String message) {
        final UserController controller = UserController.getInstance();
        binding.etMessage.setText("");

        if (!message.isEmpty()){
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
            String timestamp = sdf.format(new Date().getTime());
            Chat chat = new Chat(controller.getUsername(), controller.getUserId(), controller.getImageUrl(), message, timestamp);

            myChatReference.push().setValue(chat)
                    .addOnCompleteListener(this, task -> {
                        if (chatList == null){
                            getMessages();
                        }
                    });
        }
    }

    private void initRecyclerView() {
        binding.rvChats.setLayoutManager(new LinearLayoutManager(this));
        binding.rvChats.setHasFixedSize(true);
        chatAdapter = new ChatAdapter(this, new ArrayList<>());
        binding.rvChats.setAdapter(chatAdapter);
    }

}