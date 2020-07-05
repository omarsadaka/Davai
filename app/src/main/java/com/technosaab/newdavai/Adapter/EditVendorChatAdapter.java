package com.technosaab.newdavai.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.technosaab.newdavai.Fragments.VendorChatListFragment;
import com.technosaab.newdavai.Models.ClientChat;
import com.technosaab.newdavai.R;
import com.technosaab.newdavai.Util.ImageConverter;


import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class EditVendorChatAdapter extends RecyclerView.Adapter<EditVendorChatAdapter.viewHolder> {

    List<ClientChat> chatLists=new ArrayList<>();
    Context context;
    private SharedPreferences.Editor editor;

    public EditVendorChatAdapter(List<ClientChat> chatLists, Context context) {
        this.chatLists = chatLists;
        this.context = context;
    }

    @NonNull
    @Override
    public EditVendorChatAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_chat_list_item , parent , false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditVendorChatAdapter.viewHolder holder, int position) {
        holder.userName.setText(chatLists.get(position).getUserID().getFirstName());
        try {
            Picasso.get().load(chatLists.get(position).getUserID().getPersonalImg()).transform(new ImageConverter()).into(holder.userImage);
        }catch (Exception e){

        }

    }

    @Override
    public int getItemCount() {
        return chatLists.size();
    }
    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        TextView userName;
        public viewHolder(View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.image_user);
            userName = itemView.findViewById(R.id.user_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClientChat clientChat = chatLists.get(getAdapterPosition());
                    String chatId = clientChat.getId();
                    String user_Id = clientChat.getUserID().getId();
                    editor = context.getSharedPreferences("getChatId", MODE_PRIVATE).edit();
                    editor.putString("chatId", chatId);
                    editor.putString("user_Id", user_Id);
                    editor.apply();
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame, new VendorChatListFragment());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
        }
    }
}
