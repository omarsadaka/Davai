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
import com.technosaab.newdavai.Fragments.HomeDetailsFragment;
import com.technosaab.newdavai.Helper.SkipLoginDialoge;
import com.technosaab.newdavai.Models.CategoryResponse;
import com.technosaab.newdavai.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.viewHolder> implements Filterable {
    List<CategoryResponse> categoriesList;
    List<CategoryResponse> filterCategoriesList;
    Context context;
    private String lang;
    String restoredText;
    SharedPreferences prefs , mprefs;

    public HomeRecyclerAdapter(List<CategoryResponse> categoriesList, Context context) {
        this.categoriesList = categoriesList;
        this.context = context;
        this.filterCategoriesList = categoriesList;
        mprefs = context.getSharedPreferences("Lang", MODE_PRIVATE);
        String value = mprefs.getString("Local", null);
        if (value != null) {
            lang = mprefs.getString("Local", null);
        }
        prefs = context.getSharedPreferences("checkSideMenu", MODE_PRIVATE);
        restoredText = prefs.getString("key", null);

    }

    @NonNull
    @Override
    public HomeRecyclerAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_row , parent ,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRecyclerAdapter.viewHolder holder, int position) {

        Picasso.get().load(filterCategoriesList.get(position).getImgPath()).into(holder.serviceImage);
        if (lang.equals("english")){
            holder.serviceTitle.setText(filterCategoriesList.get(position).getTitleEN());
        }else if (lang.equals("ar")) {
            holder.serviceTitle.setText(filterCategoriesList.get(position).getTitleAr());
        }

    }

    @Override
    public int getItemCount() {
        return filterCategoriesList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString=constraint.toString();
                if(charString.isEmpty()){
                    filterCategoriesList = categoriesList;
                }else {
                    List<CategoryResponse> filterdList = new ArrayList<>();
                    for (CategoryResponse resultsBean:categoriesList) {
                        if (lang.equals("english")){
                            if(resultsBean.getTitleEN().toLowerCase().contains(charString.toLowerCase())){
                                filterdList.add(resultsBean);
                            }
                        }else if (lang.equals("ar")) {
                            if(resultsBean.getTitleAr().toLowerCase().contains(charString.toLowerCase())){
                                filterdList.add(resultsBean);
                            }
                        }


                    }
                    filterCategoriesList = filterdList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filterCategoriesList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filterCategoriesList = (ArrayList<CategoryResponse>) results.values;
                notifyDataSetChanged();
            }
        };
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
                    if (restoredText != null) {
                        String type = prefs.getString("key", "No name defined");//"No name defined" is the default value.
                        if (type.equals("skipLogin")) {
                            //Toast.makeText(context, context.getResources().getString(R.string.must_login), Toast.LENGTH_LONG).show();
                            SkipLoginDialoge.createDialoge(context);
                        }
                    }else {
                            CategoryResponse category = categoriesList.get(getAdapterPosition());
                            String categoryId = category.getId();
                            HomeDetailsFragment fragment = new HomeDetailsFragment();
                            AppCompatActivity activity = (AppCompatActivity) v.getContext();
                            FragmentManager fragmentManager = activity.getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//hbd

                        Bundle args = new Bundle();
                            args.putString("ID", categoryId);
                            fragment.setArguments(args);
                            fragmentTransaction.replace(R.id.frame, fragment);
                     fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }

                }
            });
        }
    }
}
