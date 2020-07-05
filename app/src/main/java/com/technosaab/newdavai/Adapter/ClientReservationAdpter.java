package com.technosaab.newdavai.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.technosaab.newdavai.Fragments.EditUSerFragment;
import com.technosaab.newdavai.Fragments.EditVendorFragment;
import com.technosaab.newdavai.Models.ClientBooking;
import com.technosaab.newdavai.Models.ReservClientId;
import com.technosaab.newdavai.Models.UserReservResponse;
import com.technosaab.newdavai.R;
import com.technosaab.newdavai.Util.ImageConverter;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ClientReservationAdpter extends RecyclerView.Adapter<ClientReservationAdpter.viewHolder> implements Filterable {

    private Context context;
    private List<ClientBooking> clientBookings;
    private List<ClientBooking> filterClientBookings;
    private SharedPreferences prefs;
    private String userType;
    private SharedPreferences.Editor editor;


    public ClientReservationAdpter(Context context, List<ClientBooking> clientBookings) {
        this.context = context;
        this.clientBookings = clientBookings;
        this.filterClientBookings = clientBookings;
        prefs = context.getSharedPreferences("checkUser" , MODE_PRIVATE);
        String type = prefs.getString("userType" , null);
        if (type != null){
            userType = prefs.getString("userType" , null);
        }
    }

    @NonNull
    @Override
    public ClientReservationAdpter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_edit_item_row , parent , false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        ClientBooking booking = filterClientBookings.get(position);
        //String Date = booking.getDateTime();
        holder.date.setText(booking.getDateTime().substring(0 ,booking.getDateTime().indexOf("T")));
        holder.name.setText(booking.getUserID().getFirstName());
        try {
            Picasso.get().load(booking.getUserID().getPersonalImg()).transform(new ImageConverter()).into(holder.image);
        }catch (Exception e){
            Picasso.get().load(String.valueOf(context.getResources().getDrawable(R.drawable.user_name))).transform(new ImageConverter()).into(holder.image);
        }

    }


    @Override
    public int getItemCount() {
        return  filterClientBookings.size();
    }


    public class viewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView time;
        ImageView image;
        TextView name;
        TextView serviceName;
        public viewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date_text);
            //time = itemView.findViewById(R.id.time_text);
            image = itemView.findViewById(R.id.card_image);
            name = itemView.findViewById(R.id.vendor_name);
            serviceName = itemView.findViewById(R.id.face_mask);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClientBooking clientBooking = filterClientBookings.get(getAdapterPosition());
                    String bookingId = clientBooking.getId();
                    String userId = clientBooking.getUserID().getId();
                    editor = context.getSharedPreferences("BookingID", MODE_PRIVATE).edit();
                    editor.putString("bookingId",bookingId);
                    editor.putString("USERID",userId);
                    editor.apply();
                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        FragmentManager fragmentManager = activity.getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, new EditUSerFragment());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();


                }
            });
        }
    }


    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    filterClientBookings = clientBookings;
                } else {
                    List<ClientBooking> filterdList = new ArrayList<>();
                    for (ClientBooking resultsBean : clientBookings) {
                        if (resultsBean.getUserID().getFirstName().toLowerCase().contains(charString.toLowerCase())) {
                            filterdList.add(resultsBean);
                        }
                    }
                    filterClientBookings = filterdList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filterClientBookings;

                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filterClientBookings = (ArrayList<ClientBooking>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
