package com.zero.groupchat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.zero.groupchat.databinding.ItemChatsBinding;
import com.zero.groupchat.listener.ItemClickListener;
import com.zero.groupchat.model.Chat;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    private Context context;
    private List<Chat> chatList;
    private ItemClickListener listener;

    public ChatListAdapter(Context context, List<Chat> chatList, ItemClickListener listener) {
        this.context = context;
        this.chatList = chatList;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemChatsBinding binding;
        public ViewHolder(@NonNull ItemChatsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChatsBinding binding = ItemChatsBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = chatList.get(position);

        holder.binding.tvUsername.setText(chat.getUsername());
        holder.binding.tvMessageCount.setText(chat.getMessageCount());

        Glide.with(context).load(chat.getUserImage()).into(holder.binding.civUserImage);

        holder.binding.getRoot().setOnClickListener(view -> {
            listener.onItemClicked(position, "Chat");
        });
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public List<Chat> getChatList(){
        return chatList;
    }

}
