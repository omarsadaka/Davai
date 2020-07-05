package com.technosaab.newdavai.Fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.technosaab.newdavai.Adapter.EditVendorServicesAdapter;
import com.technosaab.newdavai.Adapter.UpdateClientServicesAdapter;
import com.technosaab.newdavai.Models.CategoryService;
import com.technosaab.newdavai.Models.ClientServiceResponse2;
import com.technosaab.newdavai.Models.ServiceModel;
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

public class EditServicesFragment extends Fragment implements View.OnClickListener {
    private RecyclerView recyclerView;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    List<ClientServiceResponse2> clientServiceResponse2;
    private EditVendorServicesAdapter editVendorServicesAdapter;
    private ImageView logo , cover;
    private TextView brandName;
    private ImageView editService;
    private String clientId;
    private String categoryId;
    private List<CategoryService> categoryServices;
    private List<ServiceModel> serviceModels;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = getLayoutInflater().inflate(R.layout.fragment_edit_services, container, false);
        createView(view);
        SharedPreferences Prefs2 = getActivity().getSharedPreferences("UserId", MODE_PRIVATE);
        String value2 = Prefs2.getString("Id", null);
        if (value2 != null) {
            clientId = Prefs2.getString("Id", null);
        }
        SharedPreferences Pref = getActivity().getSharedPreferences("CategoryId", MODE_PRIVATE);
        String valueKey = Pref.getString("categoryId", null);
        if (valueKey != null) {
            categoryId = Pref.getString("categoryId", null);
        }
        clientServiceResponse2 = new ArrayList<>();
        categoryServices = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
            Toast.makeText(getContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }else {
            getClientService();
        }

        return view;
    }
  private void createView(View view){
        editService = view.findViewById(R.id.edit_services);
        editService.setOnClickListener(this);
        recyclerView = view.findViewById(R.id.services_recycler);
        brandName = view.findViewById(R.id.brandName);
        logo = view.findViewById(R.id.logo_image);
        cover = view.findViewById(R.id.cover_image);
  }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.edit_services:
                dialogBuilder = new AlertDialog.Builder(getContext());
                @SuppressLint("InflateParams") View views = getLayoutInflater().inflate(R.layout.update_service_popup, null);
                ImageView Close = views.findViewById(R.id.onclick_popup_close);
                RecyclerView recyclerView = views.findViewById(R.id.onclick_category_recycler);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
                if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
                    Toast.makeText(getContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                }else {
                    updateClientService(recyclerView);
                }

                dialogBuilder.setView(views);
                dialog = dialogBuilder.create();

                recyclerView.post(new Runnable() {

                    @Override
                    public void run() {
                        dialog.getWindow().clearFlags(
                                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                                        WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                    }
                });
                dialog.show();
                Close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        getClientService();
                    }
                });
        }
    }
    private void getClientService(){
        ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
        Call<List<ClientServiceResponse2>> call = clientInterface.getClientService(clientId);
        call.enqueue(new Callback<List<ClientServiceResponse2>>() {
            @Override
            public void onResponse(Call<List<ClientServiceResponse2>> call, Response<List<ClientServiceResponse2>> response) {
                if (response.body()!=null){
                           clientServiceResponse2 = response.body();
                    editVendorServicesAdapter = new EditVendorServicesAdapter(clientServiceResponse2 , getActivity() ,brandName , logo , cover );
                    recyclerView.setAdapter(editVendorServicesAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<ClientServiceResponse2>> call, Throwable t) {
                Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                Log.d("Error" , t.toString());
            }
        });
    }

    private void updateClientService(final RecyclerView recyclerView){

        ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
        Call<List<ClientServiceResponse2>> call = clientInterface.getClientService(clientId);
        call.enqueue(new Callback<List<ClientServiceResponse2>>() {
            @Override
            public void onResponse(Call<List<ClientServiceResponse2>> call, Response<List<ClientServiceResponse2>> response) {
                if (response.body()!=null){
                    clientServiceResponse2 = response.body();
                    UpdateClientServicesAdapter onClickCategoryAdapter = new UpdateClientServicesAdapter(getActivity() , clientServiceResponse2);
                    recyclerView.setAdapter(onClickCategoryAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<ClientServiceResponse2>> call, Throwable t) {
                Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                Log.d("Error" , t.toString());
            }
        });
//        ClientInterface vendor = ApiClient.getClient().create(ClientInterface.class);
//        Call<List<CategoryService>> call = vendor.getCategoryService(clientId);
//        call.enqueue(new Callback<List<CategoryService>>() {
//            @Override
//            public void onResponse(Call<List<CategoryService>> call, Response<List<CategoryService>> response) {
//                if (response.body()!=null){
//                    categoryServices = response.body();
//                    UpdateClientServicesAdapter onClickCategoryAdapter = new UpdateClientServicesAdapter(getActivity() , categoryServices , clientServiceResponse2);
//                    recyclerView.setAdapter(onClickCategoryAdapter);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<CategoryService>> call, Throwable t) {
//                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_LONG).show();
//            }
//        });
    }

}
