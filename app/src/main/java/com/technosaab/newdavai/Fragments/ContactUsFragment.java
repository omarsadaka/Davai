package com.technosaab.newdavai.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.technosaab.newdavai.Models.ContactUs;
import com.technosaab.newdavai.Models.SettingResponse;
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

public class ContactUsFragment extends Fragment implements View.OnClickListener {

    Spinner msgTypeSpinner;
    ArrayList<String> msgTypeList;
    private EditText subject;
    private Button send;
    private String userId;
    private String clientId;
    private int msgType;
    private String userType;
    private ImageView facebook , twitter , google;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = getLayoutInflater().inflate(R.layout.fragment_contact_us, container, false);
         createView(view);
        SharedPreferences Prefs2 = getActivity().getSharedPreferences("UserId", MODE_PRIVATE);
        String value2 = Prefs2.getString("Id", null);
        if (value2 != null) {
            userId = Prefs2.getString("Id", null);
        }

        SharedPreferences Prefs = Objects.requireNonNull(getActivity()).getSharedPreferences("ClientId", MODE_PRIVATE);
        String value = Prefs.getString("ClientId", null);
        if (value != null) {
            clientId = Prefs.getString("ClientId", null);
        }
        SharedPreferences pref = getActivity().getSharedPreferences("checkUser", MODE_PRIVATE);
        String type = pref.getString("userType", null);
        if (type != null) {
            userType = pref.getString("userType", null);
        }

        msgTypeList = new ArrayList<>();
        msgTypeList.add(getResources().getString(R.string.choose_msg_type));
        msgTypeList.add(getResources().getString(R.string.opinion));
        msgTypeList.add(getResources().getString(R.string.spam));
        msgTypeList.add(getResources().getString(R.string.other));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, msgTypeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        msgTypeSpinner.setAdapter(adapter);

        return view;
    }
     private void createView(View view){

         subject = view.findViewById(R.id.contact_us_et);
         msgTypeSpinner = view.findViewById(R.id.contact_us_spinner);
         send = view.findViewById(R.id.contact_us_send_btn);
         send.setOnClickListener(this);
       facebook = view.findViewById(R.id.facebook_icon);
       twitter = view.findViewById(R.id.twitter_icon);
       google = view.findViewById(R.id.google_icon);
       facebook.setOnClickListener(this);
       twitter.setOnClickListener(this);
       google.setOnClickListener(this);
     }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.contact_us_send_btn:
                ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
                if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
                    Toast.makeText(getContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                }else {
                    contactUs();
                }

                break;
            case R.id.facebook_icon:
               setting("facebook");
                break;
            case R.id.twitter_icon:
                setting("twitter");
                break;
            case R.id.google_icon:
                setting("google");
                break;

        }
    }
    private void contactUs(){

        //todo check if user or client
        String title = msgTypeSpinner.getSelectedItem().toString();
        if (title.equals(getResources().getString(R.string.opinion))){
            msgType = 1;
        }else if (title.equals(getResources().getString(R.string.spam))){
            msgType = 2;
        }else {
            msgType = 3;
        }

        if (userType.equals("1")){
            String msgBody = subject.getText().toString();
            if (TextUtils.isEmpty(msgBody)){
                Toast.makeText(getContext() ,getResources().getString(R.string.enter_your_msg)  , Toast.LENGTH_LONG).show();
            }else {
                ContactUs contactUs = new ContactUs(msgType , userId , msgBody);
                ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
                Call<UserContact> call = clientInterface.addContactUS(contactUs);
                call.enqueue(new Callback<UserContact>() {
                    @Override
                    public void onResponse(Call<UserContact> call, Response<UserContact> response) {
                        if (response.body()!=null){
                            Toast.makeText(getContext() , getResources().getString(R.string.msg_send) , Toast.LENGTH_LONG).show();
                            subject.setText("");
                            msgTypeSpinner.setSelection(0);
                        }
                    }

                    @Override
                    public void onFailure(Call<UserContact> call, Throwable t) {
                        Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }else if (userType.equals("2")){
            String msgBody = subject.getText().toString();
            if (TextUtils.isEmpty(msgBody)){
                Toast.makeText(getContext() ,getResources().getString(R.string.enter_your_msg)  , Toast.LENGTH_LONG).show();
            }else {
                ContactUs contactUs = new ContactUs(msgType , clientId , msgBody);
                ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
                Call<UserContact> call = clientInterface.addContactUS(contactUs);
                call.enqueue(new Callback<UserContact>() {
                    @Override
                    public void onResponse(Call<UserContact> call, Response<UserContact> response) {
                        if (response.body()!=null){
                            Toast.makeText(getContext() , getResources().getString(R.string.msg_send) , Toast.LENGTH_LONG).show();
                            subject.setText("");
                            msgTypeSpinner.setSelection(0);
                        }
                    }

                    @Override
                    public void onFailure(Call<UserContact> call, Throwable t) {
                        Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }




    }

    private void setting(final String Key){

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
            Toast.makeText(getContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }else {
            ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
            Call<List<SettingResponse>> call = clientInterface.setting();
            call.enqueue(new Callback<List<SettingResponse>>() {
                @Override
                public void onResponse(Call<List<SettingResponse>> call, Response<List<SettingResponse>> response) {
                    if (response.body() != null) {
                        for (int i = 0; i < response.body().size(); i++) {
                            String key = response.body().get(i).getKey();
                            if (key.equals(Key)) {
                                String value = response.body().get(i).getValue();
                                //Toast.makeText(getContext(), value, Toast.LENGTH_LONG).show();
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(value));
                                startActivity(browserIntent);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<SettingResponse>> call, Throwable t) {

                }
            });
        }
    }

}
