package com.technosaab.newdavai.Fragments;

import android.app.ProgressDialog;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.technosaab.newdavai.Activities.MainActivity;
import com.technosaab.newdavai.Adapter.ClientRateAdapter;
import com.technosaab.newdavai.Models.ClientRate;
import com.technosaab.newdavai.Models.UserContact;
import com.technosaab.newdavai.R;
import com.technosaab.newdavai.rest.ApiClient;
import com.technosaab.newdavai.rest.ClientInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class ClientRateFragment extends Fragment {
      private RecyclerView recyclerView;
      private List<ClientRate> clientRateList;
      private ClientRateAdapter clientRateAdapter;
      private String clientId;
      private ProgressBar progress;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = getLayoutInflater().inflate(R.layout.fragment_client_rate, container, false);

        SharedPreferences Prefs = Objects.requireNonNull(getActivity()).getSharedPreferences("ClientId", MODE_PRIVATE);
        String value = Prefs.getString("ClientId", null);
        if (value != null) {
            clientId = Prefs.getString("ClientId", null);
        }

        ((MainActivity)this.getActivity()).topButton.setVisibility(View.GONE);
        ((MainActivity)this.getActivity()).barTitle.setText(getString(R.string.rate_history));
        recyclerView = view.findViewById(R.id.rate_recyclerView);
        progress = view.findViewById(R.id.rate_bar);
        progress.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        clientRateList = new ArrayList<>();
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
            Toast.makeText(getContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }else {
            getClientRate();
        }

        return view;
    }

    private void getClientRate(){
        ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
        Call<List<ClientRate>> call = clientInterface.getClientRate(clientId);
        call.enqueue(new Callback<List<ClientRate>>() {
            @Override
            public void onResponse(Call<List<ClientRate>> call, Response<List<ClientRate>> response) {
                if (response.body()!=null){
                    clientRateList = response.body();
                    if (clientRateList.size()>=1) {
                        clientRateAdapter = new ClientRateAdapter(clientRateList, getContext());
                        recyclerView.setAdapter(clientRateAdapter);
                        progress.setVisibility(View.GONE);
                    }else {
                        progress.setVisibility(View.GONE);
                        Toast.makeText(getContext(), getResources().getString(R.string.no_rate), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ClientRate>> call, Throwable t) {
                progress.setVisibility(View.GONE);
                Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
            }
        });
    }
}
