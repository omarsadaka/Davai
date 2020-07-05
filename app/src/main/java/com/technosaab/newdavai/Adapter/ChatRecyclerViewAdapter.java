package com.technosaab.newdavai.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.technosaab.newdavai.R;
import com.technosaab.newdavai.Models.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.viewHolder> {
    Context context;
    List<Message> messageList=new ArrayList<>();
    int type = -1;

    public ChatRecyclerViewAdapter(Context context, List<Message> messageList) {
        this.context = context;
        this.messageList = messageList;

    }

    @NonNull
    @Override
    public ChatRecyclerViewAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        int layout = -1;
        switch (viewType) {
            case Message.TYPE_MESSAGE_USER:
                layout = R.layout.user_chat_item_row;
                type = 1;
                break;
            case Message.TYPE_MESSAGE_VENDOR:
                layout = R.layout.vendor_chat_item_row;
                type = 2;
                break;
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(layout , parent ,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRecyclerViewAdapter.viewHolder holder, int position) {
        if (messageList.get(position).getType()==1)
        {
            holder.userMessage.setText(messageList.get(position).getUserMessage());
        }else {

            holder.vendorMessage.setText(messageList.get(position).getVendorMessage());
        }
    }
    @Override
    public int getItemViewType(int position) {
        return messageList.get(position).getType();
    }
    @Override
    public int getItemCount() {

        return messageList.size();
    }
//    @Override
//    public int getItemViewType(int position) {
//        return messageList.get(position).getType();
//    }
    public class viewHolder extends RecyclerView.ViewHolder {

         ImageView userImage;
         TextView userMessage;
         TextView vendorMessage;

        public viewHolder(View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.user_image);
            userMessage = itemView.findViewById(R.id.user_msg_chat);
            vendorMessage = itemView.findViewById(R.id.vendor_msg_chat);
        }
    }
}
