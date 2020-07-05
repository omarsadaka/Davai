package com.technosaab.newdavai.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.technosaab.newdavai.Adapter.TermsConditionAdapter;
import com.technosaab.newdavai.Models.TermsModel;
import com.technosaab.newdavai.R;
import com.technosaab.newdavai.rest.ApiClient;
import com.technosaab.newdavai.rest.ClientInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class TermsConditionFragment extends Fragment {
   // private TextView terms;
    private List<TermsModel> lists;
    private RecyclerView recyclerView;
    private TermsConditionAdapter termsConditionAdapter;
    private SharedPreferences prefs;
    private String userType;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = getLayoutInflater().inflate(R.layout.fragment_terms_condition, container, false);
        //terms = view.findViewById(R.id.terms_text);

        prefs = getActivity().getSharedPreferences("checkUser", MODE_PRIVATE);
        String type = prefs.getString("userType", null);
        if (type != null) {
             userType = prefs.getString("userType", null);
        }
            recyclerView = view.findViewById(R.id.terms_recycler);
        progressBar = view.findViewById(R.id.terms_bar);
        progressBar.setVisibility(View.VISIBLE);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            lists = new ArrayList<>();
            getTermsCondition();
            return view;
        }

        private void getTermsCondition () {
        if (userType ==null){
            ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
            Call<List<TermsModel>> call = clientInterface.getUserTerms();
            call.enqueue(new Callback<List<TermsModel>>() {
                @Override
                public void onResponse(Call<List<TermsModel>> call, Response<List<TermsModel>> response) {
                    if (response.body() != null) {
                        lists = response.body();
                        termsConditionAdapter = new TermsConditionAdapter(getActivity(), lists);
                        recyclerView.setAdapter(termsConditionAdapter);
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<List<TermsModel>> call, Throwable t) {
                    Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        }else {

            if (userType.equals("1")) {
                ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
                Call<List<TermsModel>> call = clientInterface.getUserTerms();
                call.enqueue(new Callback<List<TermsModel>>() {
                    @Override
                    public void onResponse(Call<List<TermsModel>> call, Response<List<TermsModel>> response) {
                        if (response.body() != null) {
                            lists = response.body();
                            termsConditionAdapter = new TermsConditionAdapter(getActivity(), lists);
                            recyclerView.setAdapter(termsConditionAdapter);
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<TermsModel>> call, Throwable t) {
                        Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            } else if (userType.equals("2")) {
                ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
                Call<List<TermsModel>> call = clientInterface.getClientTerms();
                call.enqueue(new Callback<List<TermsModel>>() {
                    @Override
                    public void onResponse(Call<List<TermsModel>> call, Response<List<TermsModel>> response) {
                        if (response.body() != null) {
                            lists = response.body();
                            termsConditionAdapter = new TermsConditionAdapter(getActivity(), lists);
                            recyclerView.setAdapter(termsConditionAdapter);
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<TermsModel>> call, Throwable t) {
                        Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }

        }

    }
