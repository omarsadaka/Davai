package com.technosaab.newdavai.Fragments;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.technosaab.newdavai.Adapter.EditVendorChatAdapter;
import com.technosaab.newdavai.Models.ClientChat;
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

public class EditChatFragment extends Fragment {

    private List<ClientChat> chatLists;
    private RecyclerView recyclerView;
    private EditVendorChatAdapter editVendorChatAdapter;
    private String clientId;
    private ImageView cover;
    private Vendor vendorList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = getLayoutInflater().inflate(R.layout.fragment_edit_chat, container, false);
        SharedPreferences Prefs3 = getActivity().getSharedPreferences("UserId", MODE_PRIVATE);
        String value3 = Prefs3.getString("Id", null);
        if (value3 != null) {
            clientId = Prefs3.getString("Id", null);
        }
        recyclerView = view.findViewById(R.id.edit_chat_recycler);
        cover = view.findViewById(R.id.logo_icon);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridlm = new GridLayoutManager(getContext(), 3);
        gridlm.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridlm);
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
            Toast.makeText(getContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

        }else {
            getChats();
            getAboutVendor();
        }

        return view;
    }

    private void getChats() {

        ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
        Call<List<ClientChat>> call = clientInterface.getClientChat( clientId);

        call.enqueue(new Callback<List<ClientChat>>() {
            @Override
            public void onResponse(Call<List<ClientChat>> call, Response<List<ClientChat>> response) {
                chatLists= new ArrayList<>();
                chatLists = response.body();
                if (chatLists != null && chatLists.size() > 0) {
                    editVendorChatAdapter = new EditVendorChatAdapter( chatLists ,getContext());
                    recyclerView.setAdapter(editVendorChatAdapter);

                }
            }
            @Override
            public void onFailure(Call<List<ClientChat>> call, Throwable t) {
                //Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();

            }

        });


    }

    private void getAboutVendor(){
        ClientInterface aboutVendor = ApiClient.getClient().create(ClientInterface.class);
        Call<Vendor> call = aboutVendor.getVendorAbout(clientId );
        call.enqueue(new Callback<Vendor>() {
            @Override
            public void onResponse(@NonNull Call<Vendor> call, @NonNull Response<Vendor> response) {
                if (response.body()!=null){
                    vendorList = response.body();
                    Picasso.get().load(response.body().getCover()).into(cover);

                }
            }

            @Override
            public void onFailure(@NonNull Call<Vendor> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
            }
        });
    }


}
