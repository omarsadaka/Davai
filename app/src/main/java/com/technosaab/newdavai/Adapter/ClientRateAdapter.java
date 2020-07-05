package com.technosaab.newdavai.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.technosaab.newdavai.Models.ClientRate;
import com.technosaab.newdavai.R;
import com.technosaab.newdavai.Util.ImageConverter;

import java.util.List;

public class ClientRateAdapter extends RecyclerView.Adapter<ClientRateAdapter.viewHolder> {

    private List<ClientRate> clientRateList;
    private Context context;

    public ClientRateAdapter(List<ClientRate> clientRateList, Context context) {
        this.clientRateList = clientRateList;
        this.context = context;
    }

    @NonNull
    @Override
    public ClientRateAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_rate_item , parent , false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientRateAdapter.viewHolder holder, int position) {

         if (clientRateList.get(position).getUserID().getPersonalImg()!=null) {
             Picasso.get().load(clientRateList.get(position).getUserID().getPersonalImg()).transform(new ImageConverter()).into(holder.userImage);
         }else holder.userImage.setBackgroundResource(R.drawable.user_name);
        holder.userName.setText(clientRateList.get(position).getUserID().getFirstName());
        holder.userComment.setText(clientRateList.get(position).getComment());
        float rate = clientRateList.get(position).getRate();
        holder.ratingBar.setRating(rate);
    }

    @Override
    public int getItemCount() {
        return clientRateList.size();
    }
    public class viewHolder extends RecyclerView.ViewHolder {
        private ImageView userImage;
        private TextView userName;
        private RatingBar ratingBar;
        private TextView userComment;
        public viewHolder(View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.image_user);
            userName = itemView.findViewById(R.id.name_user);
            ratingBar = itemView.findViewById(R.id.rate_user);
            userComment = itemView.findViewById(R.id.user_comment);
        }
    }
}
