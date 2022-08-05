package com.zero.groupchat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.zero.groupchat.controller.UserController;
import com.zero.groupchat.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        new Handler(getMainLooper()).postDelayed(() -> {
            UserController.getInstance().setUserId("HiCukX3oejYkmLq0gIZiBWTsPSv2");
            redirectToChatList();
        }, 2000);

    }

    private void redirectToChatList() {
        Intent intent = new Intent(this, ChatListActivity.class);
        startActivity(intent);
        finish();
    }
}