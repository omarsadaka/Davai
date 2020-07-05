package com.technosaab.newdavai.Fragments;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.technosaab.newdavai.Adapter.MyReservationAdapter;
import com.technosaab.newdavai.Models.UserReservResponse;
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

public class MyReservationUserFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<UserReservResponse> myReservationList;
    private MyReservationAdapter myReservationAdapter;
    TextView barTitle;
    private String userId;
    private String userType;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = getLayoutInflater().inflate(R.layout.fragment_my_reservation_user, container, false);

        SharedPreferences Prefs2 = getActivity().getSharedPreferences("UserId", MODE_PRIVATE);
        String value2 = Prefs2.getString("Id", null);
        if (value2 != null) {
            userId = Prefs2.getString("Id", null);
        }
        SharedPreferences prefs = getActivity().getSharedPreferences("checkUser" , MODE_PRIVATE);
        String type = prefs.getString("userType" , null);
        if (type != null){
            userType = prefs.getString("userType" , null);
        }
        recyclerView = view.findViewById(R.id.my_reservation_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(myReservationAdapter);
        myReservationList = new ArrayList<>();
        barTitle = getActivity().findViewById(R.id.bar_title);
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
            Toast.makeText(getContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

        }else {
            getUserBooking();
        }

        barTitle.setText(getString(R.string.my_reservation));
        return view;
    }

    private void getUserBooking(){
        if (userType.equals("1")) {
            ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
            Call<List<UserReservResponse>> call = clientInterface.getUserBooking(userId);
            call.enqueue(new Callback<List<UserReservResponse>>() {
                @Override
                public void onResponse(Call<List<UserReservResponse>> call, Response<List<UserReservResponse>> response) {
                    if (response.body() != null) {
                        myReservationList = response.body();
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        myReservationAdapter = new MyReservationAdapter(getContext(), myReservationList);
                    }
                }

                @Override
                public void onFailure(Call<List<UserReservResponse>> call, Throwable t) {
                    Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();

                }
            });
        }else if (userType.equals("2")){
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            myReservationAdapter = new MyReservationAdapter(getContext(), myReservationList);
        }
    }

}
