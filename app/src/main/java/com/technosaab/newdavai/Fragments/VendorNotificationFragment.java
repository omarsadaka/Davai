package com.technosaab.newdavai.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.technosaab.newdavai.Adapter.NotificationRecyclerAdapter;
import com.technosaab.newdavai.Helper.GetCountry;
import com.technosaab.newdavai.Models.NotificationResponse;
import com.technosaab.newdavai.R;
import com.technosaab.newdavai.rest.ApiClient;
import com.technosaab.newdavai.rest.ClientInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;


public class VendorNotificationFragment extends Fragment {
           private RecyclerView recyclerView;
           private String userType;
           private String clientId;
    private List<NotificationResponse> notificationList;
    private NotificationRecyclerAdapter notificationRecyclerAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = getLayoutInflater().inflate(R.layout.fragment_vendor_notification, container, false);
        SharedPreferences prefs = getActivity().getSharedPreferences("checkUser", MODE_PRIVATE);
        String type = prefs.getString("userType", null);
        if (type != null) {
            userType = prefs.getString("userType", null);
        }
        SharedPreferences Prefs = getActivity().getSharedPreferences("UserId", MODE_PRIVATE);
        String value = Prefs.getString("Id", null);
        if (value != null) {
            clientId = Prefs.getString("Id", null);
        }
        recyclerView = view.findViewById(R.id.notification_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
            Toast.makeText(getContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }else {
            getNotification();
        }
        return view;
    }
    private void getNotification(){
        if (userType.equals("2")){
            ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
            Call<List<NotificationResponse>> call = clientInterface.getClientNotify(clientId);
            call.enqueue(new Callback<List<NotificationResponse>>() {
                @Override
                public void onResponse(Call<List<NotificationResponse>> call, Response<List<NotificationResponse>> response) {
                    if (response.body()!=null){
                        notificationList = response.body();
                        notificationRecyclerAdapter = new NotificationRecyclerAdapter(notificationList , getContext());
                        recyclerView.setAdapter(notificationRecyclerAdapter);
                    }
                }
                @Override
                public void onFailure(Call<List<NotificationResponse>> call, Throwable t) {

                }
            });
        }
    }
}
