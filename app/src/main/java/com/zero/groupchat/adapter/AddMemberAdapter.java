package com.zero.groupchat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.zero.groupchat.R;
import com.zero.groupchat.databinding.ItemAddMembersBinding;
import com.zero.groupchat.listener.ItemClickListener;
import com.zero.groupchat.model.User;

import java.util.List;

public class AddMemberAdapter extends RecyclerView.Adapter<AddMemberAdapter.ViewHolder> {

    private Context context;
    private List<User> membersList;
    private ItemClickListener listener;

    public AddMemberAdapter(Context context, List<User> chatList, ItemClickListener listener) {
        this.context = context;
        this.membersList = chatList;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemAddMembersBinding binding;
        public ViewHolder(@NonNull ItemAddMembersBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAddMembersBinding binding = ItemAddMembersBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = membersList.get(position);

        holder.binding.cbAddMember.setChecked(user.isAdded());

        holder.binding.cbAddMember.setText(user.getFullName());

        Glide.with(context).load(user.getImgProfileUri()).placeholder(R.drawable.user).into(holder.binding.civUserImage);

        holder.binding.cbAddMember.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                listener.onItemClicked(position, "Add");
            }
            else{
                listener.onItemClicked(position, "Remove");
            }
        });
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
