package com.zero.groupchat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.zero.groupchat.databinding.ActivityChatBinding;
import com.zero.groupchat.databinding.ActivityChatListBinding;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}