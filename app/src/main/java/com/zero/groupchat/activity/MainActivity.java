package com.zero.groupchat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.zero.groupchat.controller.UserController;
import com.zero.groupchat.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        UserController.getInstance().setUserId("HiCukX3oejYkmLq0gIZiBWTsPSv2");
        downloadImageUsingGlideGetLink(UserController.getInstance().getUserId());

        new Handler(getMainLooper()).postDelayed(this::redirectToChatList, 2000);

    }

    private void redirectToChatList() {
        Intent intent = new Intent(this, ChatListActivity.class);
        startActivity(intent);
        finish();
    }

    private void downloadImageUsingGlideGetLink(String url) {
        Context context = this;
        if(url ==null){
            return;
        }
        FirebaseDatabase.getInstance().getReference("users/" + url)
                .get().addOnCompleteListener(this, task -> {
                    DataSnapshot snapshot = task.getResult();
                    if (snapshot.exists()) {
                        UserController.getInstance().setUsername(snapshot.child("userName").getValue().toString());
                        if (snapshot.child("imgProfileUri").getValue().toString().contains("DefaultProfilePicture")) {

                            String imageUrl = "https://firebasestorage.googleapis.com" +
                                    "/v0/b/tiktokforsmarties.appspot.com/o/DefaultProfilePicture.jpg?" +
                                    "alt=media&token=e92a7a2d-6b23-4c23-ad7f-aed3d58a4e5c";

                            UserController.getInstance().setImageUrl(imageUrl);
                        } else {
                            FirebaseStorage.getInstance().getReference().child("ProfileImages/" + snapshot.child("imgProfileUri").getValue().toString()).
                                    getDownloadUrl().addOnSuccessListener(uri -> {

                                        Log.d("TAG", "downloadImageUsingGlideGetLink: " + uri);

                                        UserController.getInstance().setImageUrl(uri.toString());
                                    });                        }
                    } else {
                        if (isValidContextForGlide(context,url)) {
                            String imageUrl = "https://firebasestorage.googleapis.com" +
                                    "/v0/b/tiktokforsmarties.appspot.com/o/DefaultProfilePicture.jpg" +
                                    "?alt=media&token=e92a7a2d-6b23-4c23-ad7f-aed3d58a4e5c";

                            UserController.getInstance().setImageUrl(imageUrl);
                        }
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
}