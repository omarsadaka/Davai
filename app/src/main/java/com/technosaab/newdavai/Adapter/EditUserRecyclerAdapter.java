package com.technosaab.newdavai.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.technosaab.newdavai.R;
import com.technosaab.newdavai.Models.MyReservation;


import java.util.List;

public class EditUserRecyclerAdapter extends RecyclerView.Adapter<EditUserRecyclerAdapter.viewHolder> {
    private Context context;
    private List<MyReservation> myReservationList;

    public EditUserRecyclerAdapter(Context context, List<MyReservation> myReservationList) {
        this.context = context;
        this.myReservationList = myReservationList;
    }

    @NonNull
    @Override
    public EditUserRecyclerAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_edit_item , parent , false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditUserRecyclerAdapter.viewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView serviceName;
        TextView employee_name;
        public viewHolder(View itemView) {
            super(itemView);
        }
    }
}
