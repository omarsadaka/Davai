package com.technosaab.newdavai.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import com.technosaab.newdavai.Activities.MainActivity;
import com.technosaab.newdavai.Activities.SelectLangActivity;
import com.technosaab.newdavai.Models.AddFavResponse;
import com.technosaab.newdavai.Models.AddFavourite;
import com.technosaab.newdavai.Models.CategoryID;
import com.technosaab.newdavai.Models.CityID;
import com.technosaab.newdavai.Models.CountryID;
import com.technosaab.newdavai.Models.NumRes;
import com.technosaab.newdavai.Models.Ratting;
import com.technosaab.newdavai.Models.SettingResponse;
import com.technosaab.newdavai.Models.Vendor;
import com.technosaab.newdavai.R;
import com.technosaab.newdavai.rest.ApiClient;
import com.technosaab.newdavai.rest.ClientInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class AboutFragment extends Fragment implements View.OnClickListener {
    private TextView vendorName , description , phone , location , country , city , website , email , vendorCategory , star_text , num_fav;
    ImageView cover , logo , star_logo;
    ImageView add_Favourite , client_rat;
    private String clientId;
    private String userId;
    private String lang;
    private Vendor vendorList;
    private String userType;
    AlertDialog dialog;
    ImageView faceBook , twitter , google ,whatsApp;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = getLayoutInflater().inflate(R.layout.fragment_about, container, false);
        createView(view);
        SharedPreferences Prefs = Objects.requireNonNull(getActivity()).getSharedPreferences("ClientId", MODE_PRIVATE);
        String value = Prefs.getString("ClientId", null);
        if (value != null) {
            clientId = Prefs.getString("ClientId", null);
        }
        SharedPreferences Prefs2 = getActivity().getSharedPreferences("UserId", MODE_PRIVATE);
        String value2 = Prefs2.getString("Id", null);
        if (value2 != null) {
            userId = Prefs2.getString("Id", null);
        }
        SharedPreferences Prefs3 = getActivity().getSharedPreferences("Lang", MODE_PRIVATE);
        String value3 = Prefs3.getString("Local", null);
        if (value3 != null) {
            lang = Prefs3.getString("Local", null);
        }

        SharedPreferences prefs = getActivity().getSharedPreferences("checkUser", MODE_PRIVATE);
        String type = prefs.getString("userType", null);
        if (type != null) {
            userType = prefs.getString("userType", null);
        }
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }else {
            getAboutVendor();
            getNumFavourite();
        }



        return view;
    }
    private void createView(View view){
        vendorName = view.findViewById(R.id.vendor_name);
        description = view.findViewById(R.id.info_text);
        phone = view.findViewById(R.id.call_text);
        location = view.findViewById(R.id.vendor_address);
        country = view.findViewById(R.id.vendor_country);
        city = view.findViewById(R.id.vendor_city);
        website = view.findViewById(R.id.vendor_website);
        email = view.findViewById(R.id.vendor_email);
        cover = view.findViewById(R.id.vendor_cover);
        logo = view.findViewById(R.id.vendor_logo);
        faceBook = view.findViewById(R.id.facebook);
        twitter = view.findViewById(R.id.twitter);
        google = view.findViewById(R.id.google);
        whatsApp = view.findViewById(R.id.whatsApp);
        vendorCategory = view.findViewById(R.id.vendor_category);
        add_Favourite = view.findViewById(R.id.heart_logo);
        add_Favourite.setOnClickListener(this);
        star_text = view.findViewById(R.id.star_text);
        star_logo = view.findViewById(R.id.star_logo);
        num_fav = view.findViewById(R.id.num_fav);
        star_logo.setOnClickListener(this);
        phone.setOnClickListener(this);
        website.setOnClickListener(this);
        email.setOnClickListener(this);
        client_rat = view.findViewById(R.id.client_rat);
        client_rat.setOnClickListener(this);
        faceBook.setOnClickListener(this);
        twitter.setOnClickListener(this);
        google.setOnClickListener(this);
        whatsApp.setOnClickListener(this);
    }
   private void getAboutVendor(){
       ClientInterface aboutVendor = ApiClient.getClient().create(ClientInterface.class);
       Call<Vendor> call = aboutVendor.GetVendorAbout(clientId , userId);
       call.enqueue(new Callback<Vendor>() {
           @Override
           public void onResponse(@NonNull Call<Vendor> call, @NonNull Response<Vendor> response) {
               if (response.body()!=null){
                   vendorList = response.body();
                   vendorName.setText(response.body().getBrandName());
                   description.setText(response.body().getDescription());
                   phone.setText(response.body().getVendorMobile());
                   location.setText(response.body().getVendorAddress());
                   Picasso.get().load(response.body().getLogo()).into(logo);
                   Picasso.get().load(response.body().getCover()).into(cover);
                   website.setText(response.body().getWebsite());
                   email.setText(response.body().getEmail());
                   star_text.setText(response.body().getTotalRate());
                   CountryID countryID = vendorList.getCountryID();
                   CityID cityID = vendorList.getCityID();
                   CategoryID categoryID = vendorList.getCategoryID();
                   int heart = response.body().getV();
                   if (heart == 2){
                       add_Favourite.setImageDrawable(getResources().getDrawable(R.drawable.favourite));
                   }
                   if (lang.equals("english")){
                      country.setText(countryID.getTitleEN());
                      city.setText(cityID.getTitleEN());
                      vendorCategory.setText(categoryID.getTitleEN());
                   }else if (lang.equals("ar")) {
                       country.setText(countryID.getTitleAr());
                       city.setText(cityID.getTitleAr());
                       vendorCategory.setText(categoryID.getTitleAr());
                   }
                   ((MainActivity) getActivity()).barTitle.setText(response.body().getBrandName());
               }
           }

           @Override
           public void onFailure(@NonNull Call<Vendor> call, @NonNull Throwable t) {
               Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
           }
       });
   }

    @Override
    public void onClick(View v) {
        int id =v.getId();
        switch (id){
            case R.id.heart_logo:
                if (userType.equals("1")) {
                    if (add_Favourite.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.favourite).getConstantState()) {
                        ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
                        Call<ResponseBody> call = clientInterface.deleteFavourite(userId, clientId);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.body() != null) {
                                    Toast.makeText(getContext(), getResources().getString(R.string.delete_favourite), Toast.LENGTH_LONG).show();
                                    add_Favourite.setImageDrawable(getResources().getDrawable(R.drawable.h_heart));

                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(getContext(), getString(R.string.server_error), Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        AddFavourite addFavourite = new AddFavourite(clientId, userId);
                        ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
                        Call<AddFavResponse> call = clientInterface.addFavourite(addFavourite);
                        call.enqueue(new Callback<AddFavResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<AddFavResponse> call, @NonNull Response<AddFavResponse> response) {
                                if (response.body() != null) {
                                    Toast.makeText(getContext(), getResources().getString(R.string.add_favourite), Toast.LENGTH_LONG).show();
                                    add_Favourite.setImageDrawable(getResources().getDrawable(R.drawable.favourite));

                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<AddFavResponse> call, @NonNull Throwable t) {
                                Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                }

                break;
            case R.id.star_logo:
                if (userType.equals("1")) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                    View view = LayoutInflater.from(getContext()).inflate(R.layout.rate_layout, null);
                    Button rate = view.findViewById(R.id.rate);
                    final RatingBar ratingbar = view.findViewById(R.id.ratingBar);
                    final EditText comment_et = view.findViewById(R.id.comment);

                    dialogBuilder.setView(view);
                    dialog = dialogBuilder.create();
                    dialog.show();
                    rate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Float rating = ratingbar.getRating();
                            String comment = comment_et.getText().toString().trim();
                            //Toast.makeText(getContext(), rating, Toast.LENGTH_LONG).show();
                            if (!TextUtils.isEmpty(comment) && rating!=0) {
                            Ratting ratting = new Ratting(clientId, userId, rating, comment);
                            ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
                            Call<Object> call = clientInterface.addRatting(ratting);
                            call.enqueue(new Callback<Object>() {
                                @Override
                                public void onResponse(Call<Object> call, Response<Object> response) {
                                    if (response.body() != null) {
                                        Toast.makeText(getContext(), "Thank You", Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Object> call, Throwable t) {
                                    dialog.dismiss();
                                }
                            });
                            }else {
                                if (rating==0){
                                    Toast.makeText(getContext(), getResources().getString(R.string.your_rate), Toast.LENGTH_LONG).show();
                                }else if (TextUtils.isEmpty(comment)){
                                    Toast.makeText(getContext(), getResources().getString(R.string.your_comment), Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(getContext(), getResources().getString(R.string.your_info), Toast.LENGTH_LONG).show();
                                }

                            }


                        }

                    });
                }
                break;
            case R.id.call_text:
                String value = phone.getText().toString().trim();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+value));
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CALL_PHONE},
                            1);

                    // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                } else {
                    //You already have permission
                    try {
                        startActivity(callIntent);
                    } catch(SecurityException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.vendor_website:
                String websiteUrl = website.getText().toString().trim();
                if (!websiteUrl.startsWith("http://") && !websiteUrl.startsWith("https://")) {
                    websiteUrl = "http://" + websiteUrl;
                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl));
                startActivity(browserIntent);
                break;
            case R.id.vendor_email:
                String emailUrl = email.getText().toString().trim();
                if (!emailUrl.startsWith("http://") && !emailUrl.startsWith("https://")) {
                    emailUrl = "http://" + emailUrl;
                }
                Intent browserIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(emailUrl));
                startActivity(browserIntent2);
                break;
            case R.id.client_rat:
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, new ClientRateFragment());
                //hbd
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.facebook:
                setting("facebook");
                break;
            case R.id.twitter:
                setting("twitter");
                break;
            case R.id.google:
                setting("google");
                break;
            case R.id.whatsApp:
                String num = phone.getText().toString().trim();
                callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+num));
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CALL_PHONE},
                            1);

                    // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                } else {
                    //You already have permission
                    try {
                        startActivity(callIntent);
                    } catch(SecurityException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private void getNumFavourite(){
        ClientInterface aboutVendor = ApiClient.getClient().create(ClientInterface.class);
        Call<NumRes> call = aboutVendor.totalClientFav(clientId);
        call.enqueue(new Callback<NumRes>() {
            @Override
            public void onResponse(Call<NumRes> call, Response<NumRes> response) {
                if (response.body()!=null){
                    String num = String.valueOf(response.body().getMessage());
                    num_fav.setText(num);

                }
            }

            @Override
            public void onFailure(Call<NumRes> call, Throwable t) {

            }
        });
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
