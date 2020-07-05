package com.technosaab.newdavai.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


import com.technosaab.newdavai.Activities.MainActivity;
import com.technosaab.newdavai.Helper.SearchFilter;
import com.technosaab.newdavai.Models.CategoryClientResponse;
import com.technosaab.newdavai.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class HomeDetailRecyclerAdapter extends RecyclerView.Adapter<HomeDetailRecyclerAdapter.viewHolder> implements Filterable {

    Context context;
    ArrayList<CategoryClientResponse> categoryClientResponses=new ArrayList<>();

    public List<CategoryClientResponse> filterCategoryClientResponses=new ArrayList<>();
    private String lang ;
    private SharedPreferences.Editor editor;
    private SearchFilter filter;

    public HomeDetailRecyclerAdapter(Context context, List<CategoryClientResponse> categoriesList) {
        this.context = context;
        this.categoryClientResponses = (ArrayList<CategoryClientResponse>)  categoriesList;
        this.filterCategoryClientResponses = categoriesList;
        try {
            SharedPreferences Prefs = context.getSharedPreferences("Lang", MODE_PRIVATE);
            String value = Prefs.getString("Local", null);
            if (value != null) {
                lang = Prefs.getString("Local", null);
            }
        }catch (Exception e){

        }

    }

    @NonNull
    @Override
    public HomeDetailRecyclerAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_detail_item_row , parent , false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeDetailRecyclerAdapter.viewHolder holder, int position) {
        Picasso.get().load(filterCategoryClientResponses.get(position).getLogo()).into(holder.serviceImage);
        holder.numRate.setText(filterCategoryClientResponses.get(position).getTotalRate().substring(0,1));
        if (lang.equals("english")){
            holder.serviceTitle.setText(filterCategoryClientResponses.get(position).getBrandName());
            //holder.numRate.setText(vendorList.get(position).getFrom1());
        }else if (lang.equals("ar")) {
            holder.serviceTitle.setText(filterCategoryClientResponses.get(position).getBrandName());
            //holder.numRate.setText(vendorList.get(position).getFrom1());
        }
    }

    @Override
    public int getItemCount() {
        return filterCategoryClientResponses.size();
    }
//
//    @Override
//    public Filter getFilter() {
//        return new Filter() {
//            @Override
//            protected FilterResults performFiltering(CharSequence constraint) {
//                String charString=constraint.toString();
//                if(charString.isEmpty()){
//                    filterCategoryClientResponses = categoryClientResponses;
//                }else {
//                    List<CategoryClientResponse> filterdList = new ArrayList<>();
//                    for (CategoryClientResponse resultsBean:categoryClientResponses) {
//                        if(resultsBean.getBrandName().toLowerCase().contains(charString.toLowerCase())){
//                            filterdList.add(resultsBean);
//                        }
//                    }
//                    filterCategoryClientResponses = filterdList;
//                }
//                FilterResults filterResults = new FilterResults();
//                filterResults.values = filterCategoryClientResponses;
//                return filterResults;
//            }
//
//            @Override
//            protected void publishResults(CharSequence constraint, FilterResults results) {
//                filterCategoryClientResponses = (ArrayList<CategoryClientResponse>) results.values;
//                notifyDataSetChanged();
//            }
//        };
//    }

    @Override
    public Filter getFilter() {
        if(filter==null)
        {
            filter=new SearchFilter(categoryClientResponses,this , context);
        }

        return filter;
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
                    CategoryClientResponse client = filterCategoryClientResponses.get(getAdapterPosition());
                    String ClientId = client.getId();
                    String ClientName = client.getBrandName();
                    String ClientLogo = client.getLogo();
                    editor = context.getSharedPreferences("ClientId", MODE_PRIVATE).edit();
                    editor.putString("ClientId", ClientId);
                    editor.apply();
                    editor = context.getSharedPreferences("ClientName", MODE_PRIVATE).edit();
                    editor.putString("ClientName", ClientName);
                    editor.apply();
                    editor = context.getSharedPreferences("ClientLogo", MODE_PRIVATE).edit();
                    editor.putString("ClientLogo", ClientLogo);
                    editor.apply();

                    Intent intent = new Intent(context , MainActivity.class);
                    intent.putExtra("check" , "about");
                    context.startActivity(intent);
                }
            });
        }
    }
}
