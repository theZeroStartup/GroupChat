package com.zero.groupchat.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zero.groupchat.R;
import com.zero.groupchat.adapter.ChatAdapter;
import com.zero.groupchat.controller.UserController;
import com.zero.groupchat.databinding.ActivityChatBinding;
import com.zero.groupchat.model.Chat;
import com.zero.groupchat.model.UnreadMessageCount;

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
    DatabaseReference groupMembersReference;
    DatabaseReference myUnreadMessageCountReference;
    DatabaseReference unreadMessageCountReference;
    private String chatId;
    private String chatName;

    private List<Chat> chatList;
    private List<String> members;
    private List<UnreadMessageCount> unreadMessageCountList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initRecyclerView();
        chatId = getIntent().getStringExtra("chatId");
        chatName = getIntent().getStringExtra("chatName");
        groupMembersReference = database.getReference("chats/" + chatId + "/members");
        myChatReference = database.getReference("chats/" + chatId + "/messages");
        unreadMessageCountReference = database.getReference("chats/" + chatId + "/unreadMessageCount");
        myUnreadMessageCountReference = database.getReference("chats/" + chatId + "/unreadMessageCount/" + UserController.getInstance().getUserId());

        markAllAsRead();
        getUnreadMessageCount();
        messageListener();
        getMessages();

        binding.ibSend.setOnClickListener(view -> {
            if (!binding.etMessage.getText().toString().trim().isEmpty())
                sendMessage(binding.etMessage.getText().toString().trim());
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMembers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.addMember) {
            Intent intent = new Intent(ChatActivity.this, EditMembersActivity.class);
            intent.putExtra("chatId", chatId);
            intent.putExtra("chatName", chatName);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void markAllAsRead() {
        UnreadMessageCount userMessageCount = new UnreadMessageCount(UserController.getInstance().getUserId(), 0);
        myUnreadMessageCountReference.setValue(userMessageCount).addOnCompleteListener(this, t -> {
        });
    }

    private void getUnreadMessageCount() {
        unreadMessageCountReference.get()
                .addOnCompleteListener(ChatActivity.this, task -> {
                    DataSnapshot snapshot = task.getResult();

                    if (snapshot.exists()){
                        unreadMessageCountList = new ArrayList<>();

                        for (DataSnapshot s: snapshot.getChildren()){
                            UnreadMessageCount unreadMessageCount = s.getValue(UnreadMessageCount.class);

                            unreadMessageCountList.add(unreadMessageCount);
                        }
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        markAllAsRead();
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

    private void getMembers() {
        groupMembersReference.get().addOnCompleteListener(this, task -> {
            DataSnapshot snapshot = task.getResult();

            if (snapshot.exists()){
                members = new ArrayList<>();

                for (DataSnapshot s: snapshot.getChildren()){
                    String chat = s.getValue(String.class);

                    members.add(chat);
                }

                UserController.getInstance().setGroupMembers(members);
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
                binding.rvChats.smoothScrollToPosition(chatList.size() - 1);
            }
        });
    }

    private void sendMessage(String message) {
        final UserController controller = UserController.getInstance();
        binding.etMessage.setText("");

        if (!message.isEmpty()){
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
            String timestamp = sdf.format(new Date().getTime());
            Chat chat = new Chat(controller.getUsername(), controller.getUserId(), controller.getImageUrl(), message, timestamp);

            myChatReference.push().setValue(chat)
                    .addOnCompleteListener(this, task -> {
                        HashMap<String, Object> unreadMessageMap = new HashMap<>();

                        for (UnreadMessageCount unreadMessageCount: unreadMessageCountList){
                            if (!unreadMessageCount.getUserId().equals(UserController.getInstance().getUserId())){
                                int currentUnreadMessageCount = unreadMessageCount.getUnreadMessageCount();
                                unreadMessageCount.setUnreadMessageCount(++currentUnreadMessageCount);

                                unreadMessageMap.put(unreadMessageCount.getUserId(), unreadMessageCount);
                            }
                        }

                        unreadMessageCountReference.updateChildren(unreadMessageMap, (err, reference) -> {
                            if (chatList == null){
                                getMessages();
                            }
                        });
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