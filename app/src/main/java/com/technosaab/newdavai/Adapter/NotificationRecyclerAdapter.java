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
import com.technosaab.newdavai.Fragments.EditUSerFragment;
import com.technosaab.newdavai.Fragments.MyReservationVendorFragment;
import com.technosaab.newdavai.Models.Notification;
import com.technosaab.newdavai.Models.NotificationResponse;
import com.technosaab.newdavai.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class NotificationRecyclerAdapter extends RecyclerView.Adapter<NotificationRecyclerAdapter.viewHolder> {
    List<NotificationResponse> notificationList;
    Context context;
    private String userType;

    public NotificationRecyclerAdapter(List<NotificationResponse> notificationList, Context context) {
        this.notificationList = notificationList;
        this.context = context;
        try {
            SharedPreferences prefs = context.getSharedPreferences("checkUser", MODE_PRIVATE);
            String type = prefs.getString("userType", null);
            if (type != null) {
                userType = prefs.getString("userType", null);
            }
        }catch (Exception e){

        }

    }

    @NonNull
    @Override
    public NotificationRecyclerAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notify_list_item , parent , false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationRecyclerAdapter.viewHolder holder, int position) {

        if (userType.equals("1")) {
            holder.notifyDate.setText(notificationList.get(position).getCreatedAt().substring(0, notificationList.get(position).getCreatedAt().indexOf("T")));
            holder.msg.setText(notificationList.get(position).getMsg());
            holder.vendorName.setText(notificationList.get(position).getClientID().getBrandName());
            Picasso.get().load(notificationList.get(position).getClientID().getLogo()).into(holder.logo);
        }else if (userType.equals("2")){
            holder.notifyDate.setText(notificationList.get(position).getCreatedAt().substring(0, notificationList.get(position).getCreatedAt().indexOf("T")));
            holder.msg.setText(notificationList.get(position).getMsg());
            holder.vendorName.setText(notificationList.get(position).getUserID().getFirstName());
            Picasso.get().load(notificationList.get(position).getUserID().getPersonalImg()).into(holder.logo);
        }

    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }
    public class viewHolder extends RecyclerView.ViewHolder {
        TextView notifyDate;
        TextView msg;
        TextView vendorName;
        ImageView logo;
        public viewHolder(View itemView) {
            super(itemView);
            notifyDate = itemView.findViewById(R.id.date);
            msg = itemView.findViewById(R.id.msg_status);
            vendorName = itemView.findViewById(R.id.vendor_name);
            logo = itemView.findViewById(R.id.logo);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
//                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.frame, new MyReservationVendorFragment());
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();
                }
            });
        }
    }
}
