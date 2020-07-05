package com.technosaab.newdavai.Helper;

import android.widget.Filter;

import com.technosaab.newdavai.Adapter.VendorRecyclerAdapter;
import com.technosaab.newdavai.Models.ClientResponse;

import java.util.ArrayList;

/**
 * Created by moataz on 5/9/2018.
 */

public class VendorSearchFilter extends Filter {

    ArrayList<ClientResponse> filterList;
    VendorRecyclerAdapter adapter;

    public VendorSearchFilter(ArrayList<ClientResponse> filterList, VendorRecyclerAdapter adapter) {
        this.filterList = filterList;
        this.adapter = adapter;
    }

    //FILTERING
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        final String[] parts = constraint.toString().split(",");
        if (parts.length<3)
            constraint=null;
        else constraint=parts[2];

        String cityId=parts[1];
        String catId=parts[0];
        //RESULTS
        FilterResults results=new FilterResults();

        //VALIDATION
        if(constraint != null && constraint.length()>0&&cityId.equals("0") &&!catId.equals("0"))
        {

//            //CHANGE TO UPPER FOR CONSISTENCY
//            constraint=constraint.toString().toUpperCase();
//
//            ArrayList<ClientResponse> filteredPlaces=new ArrayList<>();
//
//            //LOOP THRU FILTER LIST
//            for(int i=0;i<filterList.size();i++)
//            {
//                //FILTER
//                if(filterList.get(i).getBrandName().toUpperCase().contains(constraint))
//                {
//                    filteredPlaces.add(filterList.get(i));
//                }
//            }
//
//            results.count=filteredPlaces.size();
//            results.values=filteredPlaces;
            //CHANGE TO UPPER FOR CONSISTENCY
            constraint=constraint.toString().toUpperCase();
            catId=catId.toString().toUpperCase();

            ArrayList<ClientResponse> filteredPlaces=new ArrayList<>();

            //LOOP THRU FILTER LIST
            for(int i=0;i<filterList.size();i++)
            {
                //FILTER
                    if(filterList.get(i).getBrandName().toUpperCase().contains(constraint)&&filterList.get(i).getCategoryID().toUpperCase().contains(catId))
                {
                    filteredPlaces.add(filterList.get(i));
                }
            }

            results.count=filteredPlaces.size();
            results.values=filteredPlaces;
        }else if (constraint != null && constraint.length()>0 && !cityId.equals("0") &&!catId.equals("0")) {
            //CHANGE TO UPPER FOR CONSISTENCY
            constraint=constraint.toString().toUpperCase();
            cityId=cityId.toString().toUpperCase();
            catId=catId.toString().toUpperCase();

            ArrayList<ClientResponse> filteredPlaces=new ArrayList<>();

            //LOOP THRU FILTER LIST
            for(int i=0;i<filterList.size();i++)
            {
                //FILTER
                if(filterList.get(i).getBrandName().toUpperCase().contains(constraint)&&
                        filterList.get(i).getCityID().toUpperCase().contains(cityId)&&
                        filterList.get(i).getCategoryID().toUpperCase().contains(catId))
                {
                    filteredPlaces.add(filterList.get(i));
                }
            }

            results.count=filteredPlaces.size();
            results.values=filteredPlaces;

        }else if (constraint == null && !cityId.equals("0") &&!catId.equals("0")) {
            cityId=cityId.toString().toUpperCase();
            catId=catId.toString().toUpperCase();

            ArrayList<ClientResponse> filteredPlaces=new ArrayList<>();

            //LOOP THRU FILTER LIST
            for(int i=0;i<filterList.size();i++)
            {
                //FILTER
                if (filterList.get(i).getCityID()!=null&&filterList.get(i).getCategoryID()!=null)
                if(filterList.get(i).getCityID().toUpperCase().contains(cityId)&&filterList.get(i).getCategoryID().toUpperCase().contains(catId))
                {
                    filteredPlaces.add(filterList.get(i));
                }
            }
            results.count=filteredPlaces.size();
            results.values=filteredPlaces;
        }
        else if (constraint == null && cityId.equals("0") &&!catId.equals("0")){
            catId=catId.toString().toUpperCase();

            ArrayList<ClientResponse> filteredPlaces=new ArrayList<>();

            //LOOP THRU FILTER LIST
            for(int i=0;i<filterList.size();i++)
            {
                //FILTER
                if (filterList.get(i).getCategoryID()!=null)
                    if(filterList.get(i).getCategoryID().toUpperCase().contains(catId))
                    {
                        filteredPlaces.add(filterList.get(i));
                    }
            }
            results.count=filteredPlaces.size();
            results.values=filteredPlaces;
        } else
            if(constraint != null && constraint.length()>0&&cityId.equals("0") &&catId.equals("0"))
        {

            //CHANGE TO UPPER FOR CONSISTENCY
            constraint=constraint.toString().toUpperCase();

            ArrayList<ClientResponse> filteredPlaces=new ArrayList<>();

            //LOOP THRU FILTER LIST
            for(int i=0;i<filterList.size();i++)
            {
                //FILTER
                if(filterList.get(i).getBrandName().toUpperCase().contains(constraint))
                {
                    filteredPlaces.add(filterList.get(i));
                }
            }

            results.count=filteredPlaces.size();
            results.values=filteredPlaces;
        }///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        else if (constraint != null && constraint.length()>0 && !cityId.equals("0") &&catId.equals("0")) {
            //CHANGE TO UPPER FOR CONSISTENCY
            constraint=constraint.toString().toUpperCase();
            cityId=cityId.toString().toUpperCase();

            ArrayList<ClientResponse> filteredPlaces=new ArrayList<>();

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

        }else if (constraint == null && !cityId.equals("0") &&catId.equals("0")) {
            cityId=cityId.toString().toUpperCase();

            ArrayList<ClientResponse> filteredPlaces=new ArrayList<>();

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
        else {
            results.count=filterList.size();
            results.values=filterList;
        }

        return results;
    }

//PUBLISH RESULTS

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adapter.filterClientList= (ArrayList<ClientResponse>) results.values;
        adapter.notifyDataSetChanged();

    }
}