package com.zero.groupchat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.zero.groupchat.adapter.AddMemberAdapter;
import com.zero.groupchat.controller.UserController;
import com.zero.groupchat.databinding.ActivityAddMembersBinding;
import com.zero.groupchat.databinding.ActivityNewChatBinding;
import com.zero.groupchat.listener.ItemClickListener;
import com.zero.groupchat.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddMembersActivity extends AppCompatActivity implements ItemClickListener {

    private ActivityAddMembersBinding binding;

    private AddMemberAdapter addMemberAdapter;
    private UserController userController = UserController.getInstance();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference usersReference = database.getReference("users");

    private List<User> addedMembers;
    private List<String> addedMemberNames;

    private List<User> usersList;

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

                usersList = new ArrayList<>();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);

                    user.setUserId(snapshot.getKey());

                    if (Objects.equals(user.getUserId(), UserController.getInstance().getUserId()))
                        UserController.getInstance().setUser(user);

                    user.setAdded(addedMemberNames.contains(user.getFullName()));
                    usersList.add(user);

                    downloadImageUsingGlideGetLink(user.getUserName(), usersList.size() - 1);
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
        addMemberAdapter = new AddMemberAdapter(this, new ArrayList<User>(), this);
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
}