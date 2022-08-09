package com.zero.groupchat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.zero.groupchat.R;
import com.zero.groupchat.controller.UserController;
import com.zero.groupchat.databinding.ItemAddMembersBinding;
import com.zero.groupchat.databinding.ItemMembersBinding;
import com.zero.groupchat.listener.ItemClickListener;
import com.zero.groupchat.model.User;

import java.util.List;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.ViewHolder> {

    private Context context;
    private List<User> membersList;
    private ItemClickListener listener;

    public MembersAdapter(Context context, List<User> chatList) {
        this.context = context;
        this.membersList = chatList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemMembersBinding binding;
        public ViewHolder(@NonNull ItemMembersBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMembersBinding binding = ItemMembersBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = membersList.get(position);

        if (user.getUserId().equals(UserController.getInstance().getUserId()))
            holder.binding.tvUsername.setText("Me");
        else
            holder.binding.tvUsername.setText(user.getFullName());

        Glide.with(context).load(user.getImgProfileUri()).placeholder(R.drawable.user).into(holder.binding.civUserImage);
    }

    public void updateData(List<User> membersList){
        this.membersList = membersList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return membersList.size();
    }

    public List<User> getMembersList(){
        return membersList;
    }

}
