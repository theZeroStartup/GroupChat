package com.zero.groupchat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.zero.groupchat.R;
import com.zero.groupchat.databinding.ActivityChatListBinding;
import com.zero.groupchat.databinding.ActivityMainBinding;

public class ChatListActivity extends AppCompatActivity {

    private ActivityChatListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }




}