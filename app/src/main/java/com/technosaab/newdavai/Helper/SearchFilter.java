package com.technosaab.newdavai.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Filter;


import com.technosaab.newdavai.Adapter.HomeDetailRecyclerAdapter;
import com.technosaab.newdavai.Models.CategoryClientResponse;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by moataz on 5/9/2018.
 */

public class SearchFilter extends Filter {

    ArrayList<CategoryClientResponse> filterList;
    HomeDetailRecyclerAdapter adapter;
    private String lang ;
    Context context;

    public SearchFilter(ArrayList<CategoryClientResponse> filterList, HomeDetailRecyclerAdapter adapter ,  Context context) {
        this.filterList = filterList;
        this.adapter = adapter;
        this.context = context;

        SharedPreferences Prefs = context.getSharedPreferences("Lang", MODE_PRIVATE);
        String value = Prefs.getString("Local", null);
        if (value != null) {
            lang = Prefs.getString("Local", null);
        }
    }

    //FILTERING
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        final String[] parts = constraint.toString().split(",");
        if (parts.length<2)
            constraint=null;
        else constraint=parts[1];
        String cityId=parts[0];
        //RESULTS
        FilterResults results=new FilterResults();

        //VALIDATION
        if(constraint != null && constraint.length()>0&&cityId.equals("0"))
        {

            //CHANGE TO UPPER FOR CONSISTENCY
            constraint=constraint.toString().toUpperCase();

            ArrayList<CategoryClientResponse> filteredPlaces=new ArrayList<>();

            //LOOP THRU FILTER LIST
            for(int i=0;i<filterList.size();i++)
            {
                //FILTER
                if (lang.equals("english")){
                    if(filterList.get(i).getBrandName().toUpperCase().contains(constraint))
                    {
                        filteredPlaces.add(filterList.get(i));
                    }
                }else if (lang.equals("ar")) {
                    if(filterList.get(i).getBrandName().toUpperCase().contains(constraint))
                    {
                        filteredPlaces.add(filterList.get(i));
                    }
                }


            }

            results.count=filteredPlaces.size();
            results.values=filteredPlaces;
        }else if (constraint != null && constraint.length()>0 && !cityId.equals("0")) {
            //CHANGE TO UPPER FOR CONSISTENCY
            constraint=constraint.toString().toUpperCase();
            cityId=cityId.toString().toUpperCase();

            ArrayList<CategoryClientResponse> filteredPlaces=new ArrayList<>();

            //LOOP THRU FILTER LIST
            for(int i=0;i<filterList.size();i++)
            {
                //FILTER
                if(filterList.get(i).getBrandName().toUpperCase().contains(constraint)&&filterList.get(i).getCityID().toUpperCase().contains(cityId))
                {
                    filteredPlaces.add(filterList.get(i));
                }
            }

            results.count=filteredPlaces.size();
            results.values=filteredPlaces;

        }else if (constraint == null && !cityId.equals("0")) {
            cityId=cityId.toString().toUpperCase();

            ArrayList<CategoryClientResponse> filteredPlaces=new ArrayList<>();

            //LOOP THRU FILTER LIST
            for(int i=0;i<filterList.size();i++)
            {
                //FILTER
                if (filterList.get(i).getCityID()!=null)
                if(filterList.get(i).getCityID().toUpperCase().contains(cityId))
                {
                    filteredPlaces.add(filterList.get(i));
                }
            }
            results.count=filteredPlaces.size();
            results.values=filteredPlaces;
        }
        else{
            results.count=filterList.size();
            results.values=filterList;
        }

        return results;
    }

//PUBLISH RESULTS

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adapter.filterCategoryClientResponses= (ArrayList<CategoryClientResponse>) results.values;
        adapter.notifyDataSetChanged();

    }
}