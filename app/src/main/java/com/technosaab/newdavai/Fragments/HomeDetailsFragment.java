package com.technosaab.newdavai.Fragments;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.technosaab.newdavai.Adapter.HomeDetailRecyclerAdapter;
import com.technosaab.newdavai.Helper.GetCity2;
import com.technosaab.newdavai.Models.CategoryClientResponse;
import com.technosaab.newdavai.Models.UserByIdResponse;
import com.technosaab.newdavai.Models.Vendor;
import com.technosaab.newdavai.R;
import com.technosaab.newdavai.rest.ApiClient;
import com.technosaab.newdavai.rest.ClientInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class HomeDetailsFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<CategoryClientResponse> vendorList;
    private HomeDetailRecyclerAdapter homeDetailRecyclerAdapter;
    private ArrayList<String> city;
    private String id;
    private EditText search;
    private SharedPreferences Pref;
    private String userType;
    private String userId;
    private  Spinner citySpinner;
    String st_query="";
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = getLayoutInflater().inflate(R.layout.fragment_home_details, container, false);
        id = getArguments().getString("ID");

        Pref = getActivity().getSharedPreferences("checkUser", MODE_PRIVATE);
        String type = Pref.getString("userType", null);
        if (type != null) {
            userType = Pref.getString("userType", null);
        }
        Pref = getActivity().getSharedPreferences("UserId", MODE_PRIVATE);
        String value = Pref.getString("Id", null);
        if (value != null) {
            userId = Pref.getString("Id", null);
        }
        recyclerView = view.findViewById(R.id.home_detail_recycler);
        search = view.findViewById(R.id.search_et);
        progressBar = view.findViewById(R.id.client_bar);
        citySpinner = view.findViewById(R.id.spinner);
        recyclerView.setHasFixedSize(true);
        progressBar.setVisibility(View.VISIBLE);
        GridLayoutManager gridlm = new GridLayoutManager(getContext(), 2);
        gridlm.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridlm);
        vendorList = new ArrayList<>();
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
            Toast.makeText(getContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }else {
            getCategoryClient();
            getUserById();
        }

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                    String query = "";
                    String cat_id = "";
                    int pos = citySpinner.getSelectedItemPosition();
                    if (pos > 0) {
                        cat_id = GetCity2.id(pos-1);
                        query=cat_id+","+st_query;
                    }else query="0"+","+st_query;
                    if (homeDetailRecyclerAdapter!=null)
                        homeDetailRecyclerAdapter.getFilter().filter(query);
                    }
                }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        city = new ArrayList<>();
//        city.add(getResources().getString(R.string.city));
//        city.add("Egypt");
//        city.add("Giza");


//        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, city);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        citySpinner.setAdapter(adapter);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(s!=null||!s.toString().isEmpty())
                {
                    String query=s.toString();
                    String cat_id="";
                    st_query=query;
                    int pos=citySpinner.getSelectedItemPosition();
                    if (pos>0) {
                        cat_id = GetCity2.id(pos-1);
                        query=cat_id+","+query;
                    }else query="0"+","+query;
                    if (homeDetailRecyclerAdapter!=null)
                        homeDetailRecyclerAdapter.getFilter().filter(query);
                }
                   // homeDetailRecyclerAdapter.getFilter().filter(s.toString()+""+citySpinner.getSelectedItem());
            }
        });

        return view;
    }
    private void getCategoryClient(){
        ClientInterface vendor = ApiClient.getClient().create(ClientInterface.class);
        Call<List<CategoryClientResponse>> call = vendor.getCategoryClient(id);
        call.enqueue(new Callback<List<CategoryClientResponse>>() {
            @Override
            public void onResponse(Call<List<CategoryClientResponse>> call, Response<List<CategoryClientResponse>> response) {
                if (response.body()!=null){
                    vendorList = response.body();
                    homeDetailRecyclerAdapter = new HomeDetailRecyclerAdapter(getContext() , vendorList);
                    recyclerView.setAdapter(homeDetailRecyclerAdapter);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<CategoryClientResponse>> call, Throwable t) {
                Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
     private void getUserById(){
// new line here
         if (userType!=null) {
             if (userType.equals("1")) {
                 ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
                 Call<UserByIdResponse> call = clientInterface.getUserById(userId);
                 call.enqueue(new Callback<UserByIdResponse>() {
                     @Override
                     public void onResponse(Call<UserByIdResponse> call, Response<UserByIdResponse> response) {
                         if (response.body() != null) {
                             String countryId = response.body().getCountryID().getId();
                             GetCity2 getCity = new GetCity2(getContext());
                             getCity.getCity(countryId, citySpinner, R.layout.spinner_item , getContext());
                         }
                     }

                     @Override
                     public void onFailure(Call<UserByIdResponse> call, Throwable t) {
                         Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                     }
                 });
             } else if (userType.equals("2")) {
                 ClientInterface aboutVendor = ApiClient.getClient().create(ClientInterface.class);
                 Call<Vendor> call = aboutVendor.getVendorAbout(userId);
                 call.enqueue(new Callback<Vendor>() {
                     @Override
                     public void onResponse(@NonNull Call<Vendor> call, @NonNull Response<Vendor> response) {
                         if (response.body() != null) {
                             String countryId = response.body().getCountryID().getId();
                             GetCity2 getCity = new GetCity2(getContext());
                             getCity.getCity(countryId, citySpinner, R.layout.spinner_item , getContext());
                         }
                     }

                     @Override
                     public void onFailure(@NonNull Call<Vendor> call, @NonNull Throwable t) {
                         Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                     }
                 });
             }
         }
         else{
             Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();

         }
         }
     }


