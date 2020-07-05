package com.technosaab.newdavai.Fragments;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.technosaab.newdavai.Activities.MainActivity;
import com.technosaab.newdavai.Activities.SelectLangActivity;
import com.technosaab.newdavai.Adapter.ClientReservationAdpter;
import com.technosaab.newdavai.Adapter.MyReservationAdapter;
import com.technosaab.newdavai.Models.ClientBooking;
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


public class MyReservationVendorFragment extends Fragment implements View.OnClickListener {
    private RecyclerView recyclerView;
    private List<UserReservResponse> myReservationList;
    private List<ClientBooking> clientBookings;
    private MyReservationAdapter myReservationAdapter;
//    private ImageView filter;
    private EditText search;
    private LinearLayout filterTypeLayout;
    TextView barTitle;
    private String userId;
    private String userType;
    private ProgressBar progressBar;
    private AlertDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = getLayoutInflater().inflate(R.layout.fragment_my_reservation, container, false);

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
        search = view.findViewById(R.id.my_reservation_search);
        progressBar =view.findViewById(R.id.my_reser_bar);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        myReservationList = new ArrayList<>();
        clientBookings = new ArrayList<>();
        //filter = view.findViewById(R.id.filter_icon);
        filterTypeLayout = view.findViewById(R.id.filter_type_layout);
        //filter.setOnClickListener(this);
        barTitle = getActivity().findViewById(R.id.bar_title);
        barTitle.setText(getString(R.string.my_reservation));
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
            Toast.makeText(getContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

        }else {
            getUserBooking();
        }
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(s!=null||!s.toString().isEmpty())
                    try {
                        myReservationAdapter.getFilter().filter(s.toString());
                    }catch (Exception e){

                    }

            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){

        }
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
                        if (myReservationList.size() >= 1) {
                            myReservationAdapter = new MyReservationAdapter(getContext(), myReservationList);
                            recyclerView.setAdapter(myReservationAdapter);
                            progressBar.setVisibility(View.GONE);
                        } else {
                            progressBar.setVisibility(View.GONE);
                            noReservationPopup();
                            //Toast.makeText(getContext(), getResources().getString(R.string.no_reservation), Toast.LENGTH_LONG).show();


                        }
                    }
                }

                @Override
                public void onFailure(Call<List<UserReservResponse>> call, Throwable t) {
                    Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        }else if (userType.equals("2")){
            ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
            Call<List<ClientBooking>> call = clientInterface.getClientBooking(userId);
            call.enqueue(new Callback<List<ClientBooking>>() {

                @Override
                public void onResponse(Call<List<ClientBooking>> call, Response<List<ClientBooking>> response) {
                    if (response.body()!=null){
                        clientBookings = response.body();
                        if (clientBookings.size()>=1) {
                            ClientReservationAdpter clientReservationAdpter = new ClientReservationAdpter(getContext(), clientBookings);
                            recyclerView.setAdapter(clientReservationAdpter);
                            progressBar.setVisibility(View.GONE);
                        }else {
                            progressBar.setVisibility(View.GONE);
                           // noReservationPopup();
                            Toast.makeText(getContext(), getResources().getString(R.string.no_reservation), Toast.LENGTH_LONG).show();

                        }
                    }
                }

                @Override
                public void onFailure(Call<List<ClientBooking>> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                }
            });




        }
    }

    private void noReservationPopup(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.no_reservation, null);
        Button yes = view.findViewById(R.id.yes_btn);
        Button no = view.findViewById(R.id.no_btn);
        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, new VendorFragment());
                //hbd
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                dialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
    }

}
