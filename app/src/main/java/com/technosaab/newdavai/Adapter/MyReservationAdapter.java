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

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MyReservationAdapter extends RecyclerView.Adapter<MyReservationAdapter.viewHolder> implements Filterable {

    private List<UserReservResponse> myReservationList;
    private List<UserReservResponse> filterMyReservationList;
    private Context context;
    private List<ClientBooking> clientBookings;
    private List<ClientBooking> filterClientBookings;
    private SharedPreferences prefs;
    private String userType;
    private SharedPreferences.Editor editor;

    public MyReservationAdapter(Context context, List<UserReservResponse> myReservationList) {
        this.context = context;
        this.myReservationList = myReservationList;
        this.filterMyReservationList = myReservationList;

        prefs = context.getSharedPreferences("checkUser" , MODE_PRIVATE);
        String type = prefs.getString("userType" , null);
        if (type != null){
            userType = prefs.getString("userType" , null);
        }
    }

    @NonNull
    @Override
    public MyReservationAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //todo check if user or vendor to use layout
        View view = null;

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_edit_item_row , parent , false);


        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyReservationAdapter.viewHolder holder, int position) {

            UserReservResponse response = filterMyReservationList.get(position);

//            holder.date.setText(response.getDateTime().substring(0,response.getDateTime().indexOf("T")));
          holder.date.setText(response.getDateTime());


        ReservClientId reservClientId = response.getReservClientId();
            Picasso.get().load(reservClientId.getLogo()).into ( holder.image );
            holder.name.setText(reservClientId.getBrandName());



    }

    @Override
    public int getItemCount() {
        return filterMyReservationList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                    String charString = constraint.toString();
                    if (charString.isEmpty()) {
                        filterMyReservationList = myReservationList;
                    } else {
                        List<UserReservResponse> filterdList = new ArrayList<>();
                        for (UserReservResponse resultsBean : myReservationList) {
                            if (resultsBean.getReservClientId().getBrandName().toLowerCase().contains(charString.toLowerCase())) {
                                filterdList.add(resultsBean);
                            }
                        }
                        filterMyReservationList = filterdList;
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filterMyReservationList;

                    return filterResults;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filterMyReservationList = (ArrayList<UserReservResponse>) results.values;
                notifyDataSetChanged();
            }
        };
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

                        UserReservResponse response = filterMyReservationList.get(getAdapterPosition());
                        String bookingId = response.getId();
                        ReservClientId reservClientId = response.getReservClientId();
                        String client_id = reservClientId.getId();
                        editor = context.getSharedPreferences("BookingID", MODE_PRIVATE).edit();
                        editor.putString("bookingId",bookingId);
                        editor.putString("ClientId",client_id);
                        editor.apply();

                        EditVendorFragment fragment = new EditVendorFragment();
                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        FragmentManager fragmentManager = activity.getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        Bundle args = new Bundle();
                        args.putString("Client_Id", client_id);
                        fragment.setArguments(args);
                        fragmentTransaction.replace(R.id.frame, fragment);
                        //hbd
                       fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }

            });
        }
    }
}
