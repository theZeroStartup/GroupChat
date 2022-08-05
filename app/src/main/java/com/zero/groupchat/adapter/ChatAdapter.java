package com.zero.groupchat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.zero.groupchat.databinding.ItemChatsBinding;
import com.zero.groupchat.databinding.ItemMessageBinding;
import com.zero.groupchat.listener.ItemClickListener;
import com.zero.groupchat.model.Chat;
import com.zero.groupchat.model.User;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private Context context;
    private List<Chat> chatList;

    public ChatAdapter(Context context, List<Chat> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemMessageBinding binding;
        public ViewHolder(@NonNull ItemMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMessageBinding binding = ItemMessageBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = chatList.get(position);

        holder.binding.tvMessage.setText(chat.getMessage());
        holder.binding.tvTimestamp.setText(chat.getTimestamp());

        Glide.with(context).load(chat.getSenderImg()).into(holder.binding.messageUserImage);
    }

    public void updateData(List<Chat> chatList){
        this.chatList = chatList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public List<Chat> getChatList(){
        return chatList;
    }

}
