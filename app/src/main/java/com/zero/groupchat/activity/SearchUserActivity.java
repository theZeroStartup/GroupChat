package com.zero.groupchat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.zero.groupchat.databinding.ActivitySearchUserBinding;

public class SearchUserActivity extends AppCompatActivity {

    private ActivitySearchUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}