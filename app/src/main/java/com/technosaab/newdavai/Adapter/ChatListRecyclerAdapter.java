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
import com.technosaab.newdavai.Fragments.MyChatFragment;
import com.technosaab.newdavai.Models.UserChatHistory;
import com.technosaab.newdavai.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ChatListRecyclerAdapter extends RecyclerView.Adapter<ChatListRecyclerAdapter.viewHolder> {

    Context context;
    List<UserChatHistory> chatLists;
    private SharedPreferences.Editor editor;

    public ChatListRecyclerAdapter(Context context, List<UserChatHistory> chatLists) {
        this.context = context;
        this.chatLists = chatLists;
    }

    @NonNull
    @Override
    public ChatListRecyclerAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_item_row , parent , false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListRecyclerAdapter.viewHolder holder, int position) {
        holder.name.setText(chatLists.get(position).getClientID().getBrandName());
        Picasso.get().load(chatLists.get(position).getClientID().getLogo()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return chatLists.size();
    }
    public class viewHolder extends RecyclerView.ViewHolder {
        TextView chatNum;
        TextView name;
        ImageView image;
        public viewHolder(View itemView) {
            super(itemView);
            chatNum = itemView.findViewById(R.id.num_msg);
            name = itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserChatHistory chatHistory = chatLists.get(getAdapterPosition());
                    String client_Id = chatHistory.getClientID().getId();
                    editor = context.getSharedPreferences("ClientId", MODE_PRIVATE).edit();
                    editor.putString("ClientId", client_Id);
                    editor.apply();
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame, new MyChatFragment());

                    //hbd

                  fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
        }
    }
}
