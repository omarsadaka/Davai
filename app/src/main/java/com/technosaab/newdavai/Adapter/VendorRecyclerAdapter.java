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
import com.technosaab.newdavai.Fragments.VendorServicesFragment;
import com.technosaab.newdavai.Helper.VendorSearchFilter;
import com.technosaab.newdavai.Models.ClientResponse;
import com.technosaab.newdavai.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class VendorRecyclerAdapter extends RecyclerView.Adapter<VendorRecyclerAdapter.viewHolder> implements Filterable {
    private Context context;
    private ArrayList<ClientResponse> clientList;
    public List<ClientResponse> filterClientList;
    private VendorSearchFilter filter;
    private String userType;

    public VendorRecyclerAdapter(Context context, List<ClientResponse> clientList) {
        this.context = context;
        this.clientList = (ArrayList<ClientResponse>) clientList;
        this.filterClientList = clientList;

        SharedPreferences Pref = context.getSharedPreferences("checkUser", MODE_PRIVATE);
        String type = Pref.getString("userType", null);
        if (type != null) {
            userType = Pref.getString("userType", null);
        }
    }

    @NonNull
    @Override
    public VendorRecyclerAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_detail_item_row , parent , false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VendorRecyclerAdapter.viewHolder holder, int position) {
        Picasso.get().load(filterClientList.get(position).getLogo()).into(holder.serviceImage);
        holder.serviceTitle.setText(filterClientList.get(position).getBrandName());
        holder.numRate.setText(filterClientList.get(position).getTotalRate());
    }

    @Override
    public int getItemCount() {
        return filterClientList.size();
    }

    @Override
    public Filter getFilter() {
        if(filter==null)
        {
            filter=new VendorSearchFilter(clientList,this);
        }

        return filter;
//        return new Filter() {
//            @Override
//            protected FilterResults performFiltering(CharSequence constraint) {
//                String charString=constraint.toString();
//                if(charString.isEmpty()){
//                    filterClientList = clientList;
//                }else {
//                    List<ClientResponse> filterdList = new ArrayList<>();
//                    for (ClientResponse resultsBean:clientList) {
//                        if(resultsBean.getBrandName().toLowerCase().contains(charString.toLowerCase())){
//                            filterdList.add(resultsBean);
//                        }
//                    }
//                    filterClientList = filterdList;
//                }
//                FilterResults filterResults = new FilterResults();
//                filterResults.values = filterClientList;
//                return filterResults;
//            }
//
//            @Override
//            protected void publishResults(CharSequence constraint, FilterResults results) {
//                filterClientList = (ArrayList<ClientResponse>) results.values;
//                notifyDataSetChanged();
//            }
//        };
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView numRate;
        private ImageView serviceImage;
        private TextView serviceTitle;
        public viewHolder(View itemView) {
            super(itemView);
            numRate = itemView.findViewById(R.id.num_rate);
            serviceImage = itemView.findViewById(R.id.home_detail_image);
            serviceTitle = itemView.findViewById(R.id.home_detail_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userType.equals("1")) {
                        ClientResponse clientResponse = filterClientList.get(getAdapterPosition());
                        String client_id = clientResponse.getId();
                        VendorServicesFragment fragment = new VendorServicesFragment();
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

                }
            });
        }
    }
}
