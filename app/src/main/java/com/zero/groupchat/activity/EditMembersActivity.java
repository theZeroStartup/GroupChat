package com.zero.groupchat.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.zero.groupchat.adapter.AddMemberAdapter;
import com.zero.groupchat.controller.UserController;
import com.zero.groupchat.databinding.ActivityAddMembersBinding;
import com.zero.groupchat.listener.ItemClickListener;
import com.zero.groupchat.model.UnreadMessageCount;
import com.zero.groupchat.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class EditMembersActivity extends AppCompatActivity implements ItemClickListener {

    private ActivityAddMembersBinding binding;

    private AddMemberAdapter addMemberAdapter;
    private final UserController userController = UserController.getInstance();

    private String chatId;
    private String chatName;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference usersReference = database.getReference("users");
    DatabaseReference groupChatsReference;
    DatabaseReference unreadMessageCountReference;

    private List<User> addedMembers;
    private List<String> addedMemberNames;

    private List<User> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddMembersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chatId = getIntent().getStringExtra("chatId");
        chatName = getIntent().getStringExtra("chatName");
        groupChatsReference = database.getReference("chats/" + chatId);

        addedMembers = userController.getUserList();
        if (addedMembers == null) addedMembers = new ArrayList<>();
        addedMemberNames = new ArrayList<>();
        for (User user: addedMembers){
            addedMemberNames.add(user.getFullName());
        }

        binding.fabAdd.setOnClickListener(view -> {
            userController.setUserList(null);
            userController.setUserList(addedMembers);
            createGroupChat();
        });

        binding.etUserSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                showSearchResults(editable.toString().trim());
            }
        });

        initRecyclerView();
        getUsers();
    }

    private void showSearchResults(String regex) {
        if (regex.isEmpty()){
            addMemberAdapter.updateData(usersList);
        }
        else if (addMemberAdapter != null && usersList != null){
            if (!usersList.isEmpty()){
                List<User> filteredUserList = new ArrayList<>();

                for (User user: usersList){
                    if (user.getFullName().toLowerCase(Locale.ROOT).contains(regex.toLowerCase(Locale.ROOT)))
                        filteredUserList.add(user);
                }

                addMemberAdapter.updateData(filteredUserList);
            }
        }
    }

    private void getUsers() {
        usersReference.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {

            }
            else {
                DataSnapshot dataSnapshot = task.getResult();

                usersList = new ArrayList<>();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);

                    if (!UserController.getInstance().getGroupMembers().contains(snapshot.getKey())) {
                        user.setUserId(snapshot.getKey());

                        user.setAdded(addedMemberNames.contains(user.getFullName()));

                        if (!Objects.equals(snapshot.getKey(), userController.getUserId())) {
                            usersList.add(user);
                        } else if (!addedMemberNames.contains(user.getFullName())) {
                            addedMembers.add(user);
                        }

                        downloadImageUsingGlideGetLink(user.getUserName(), usersList.size() - 1);
                    }
                }


                addMemberAdapter.updateData(usersList);
            }
        });
    }

    public void downloadImageUsingGlideGetLink(String url, int position) {
        Context context = this;
        if(url ==null){
            return;
        }
        FirebaseDatabase.getInstance().getReference("users").orderByChild("userName").equalTo(url)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                System.out.println(dataSnapshot.child("imgProfileUri").getValue().toString() + "<snappy");
                                if (dataSnapshot.child("imgProfileUri").getValue().toString().contains("DefaultProfilePicture")) {
                                    //System.out.println("i'm default");
                                    String imageUrl = "https://firebasestorage.googleapis.com" +
                                            "/v0/b/tiktokforsmarties.appspot.com/o/DefaultProfilePicture.jpg?" +
                                            "alt=media&token=e92a7a2d-6b23-4c23-ad7f-aed3d58a4e5c";
                                    usersList.get(position).setImgProfileUri(imageUrl);
                                    addMemberAdapter.updateItem(position);

                                    if (url.equals(UserController.getInstance().getUsername())) UserController.getInstance().getUser().setImgProfileUri(imageUrl);
                                } else {
                                    if (dataSnapshot.child("userName").getValue().toString().equals(url)) {
                                        FirebaseStorage.getInstance().getReference().child("ProfileImages/" + dataSnapshot.child("imgProfileUri").getValue().toString()).
                                                getDownloadUrl().addOnSuccessListener(uri -> {
                                                    //System.out.println(uri);
                                                    if(isValidContextForGlide(context,url)){
                                                        usersList.get(position).setImgProfileUri(uri.toString());
                                                        addMemberAdapter.updateItem(position);
                                                        if (url.equals(UserController.getInstance().getUsername()))
                                                            UserController.getInstance().getUser().setImgProfileUri(uri.toString());

                                                    }
                                                });

                                    }
                                }
                            }

                        } else {
                            if (isValidContextForGlide(context,url)) {
                                String imageUrl = "https://firebasestorage.googleapis.com" +
                                        "/v0/b/tiktokforsmarties.appspot.com/o/DefaultProfilePicture.jpg" +
                                        "?alt=media&token=e92a7a2d-6b23-4c23-ad7f-aed3d58a4e5c";
                                usersList.get(position).setImgProfileUri(imageUrl);
                                addMemberAdapter.updateItem(position);

                                if (url.equals(UserController.getInstance().getUsername()))
                                    UserController.getInstance().getUser().setImgProfileUri(imageUrl);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

    }

    public static boolean isValidContextForGlide(final Context context,String url) {
        if(url == null || context==null)
            return false;

        else if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isDestroyed() || activity.isFinishing()) {
                return false;
            }
        }
        return true;
    }

    private void initRecyclerView() {
        binding.rvMembers.setLayoutManager(new LinearLayoutManager(this));
        binding.rvMembers.setHasFixedSize(true);
        addMemberAdapter = new AddMemberAdapter(this, new ArrayList<>(), this);
        binding.rvMembers.setAdapter(addMemberAdapter);
    }

    @Override
    public void onItemClicked(int position, String type) {
        if (type.equals("Add") && !addedMembers.contains(addMemberAdapter.getMembersList().get(position))){
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


    private void createGroupChat() {
        if (validate()){
            List<String> userIds = new ArrayList<>();
            for (User user: userController.getUserList()){
                userIds.add(user.getUserId());
            }

            HashMap<String, Object> groupDataMap = new HashMap<>();
            userIds.addAll(UserController.getInstance().getGroupMembers());
            groupDataMap.put("members", userIds);

            groupChatsReference.updateChildren(groupDataMap, (error, ref) -> {

                HashMap<String, Object> dataMap = new HashMap<>();
                HashMap<String, Object> unreadMessageMap = new HashMap<>();
                for (String user: userIds){
                    dataMap.put(user + "/myChats/" + chatId + "/groupName", chatName);
                    dataMap.put(user + "/myChats/" + chatId + "/chatId", chatId);

                    UnreadMessageCount unreadMessageCount = new UnreadMessageCount(user, 0);
                    unreadMessageMap.put(user, unreadMessageCount);
                }

                usersReference.updateChildren(dataMap, (e, r) -> {
                    unreadMessageCountReference = database.getReference("chats/" + chatId + "/unreadMessageCount");

                    unreadMessageCountReference.updateChildren(unreadMessageMap, (err, reference) -> {
                        Toast.makeText(EditMembersActivity.this, "Members added successfully!", Toast.LENGTH_LONG).show();
                        finish();
                    });
                });
            });
        }
    }

    private boolean validate() {
        if (userController.getUserList() != null) {
            if (!userController.getUserList().isEmpty()){
                return true;
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
}