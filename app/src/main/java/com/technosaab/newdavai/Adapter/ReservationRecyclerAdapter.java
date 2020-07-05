package com.technosaab.newdavai.Adapter;

import android.content.Context;
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
import com.technosaab.newdavai.Fragments.VendorFragment;
import com.technosaab.newdavai.Models.ClientResponse;
import com.technosaab.newdavai.R;



import java.util.List;

public class ReservationRecyclerAdapter extends RecyclerView.Adapter<ReservationRecyclerAdapter.viewHolder> {

    List<ClientResponse> clientList;
    Context context;

    public ReservationRecyclerAdapter(List<ClientResponse> clientList, Context context) {
        this.clientList = clientList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReservationRecyclerAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_row , parent ,false);
        return new ReservationRecyclerAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationRecyclerAdapter.viewHolder holder, int position) {
        Picasso.get().load(clientList.get(position).getLogo()).into(holder.serviceImage);
        holder.serviceTitle.setText(clientList.get(position).getBrandName());
    }

    @Override
    public int getItemCount() {
        return clientList.size();
    }
    public class viewHolder extends RecyclerView.ViewHolder {
        private ImageView serviceImage;
        private TextView serviceTitle;
        public viewHolder(View itemView) {
            super(itemView);
            serviceImage = itemView.findViewById(R.id.service_image);
            serviceTitle = itemView.findViewById(R.id.service_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame, new VendorFragment());
                    //hbd

                 fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
            });
        }
    }
}
