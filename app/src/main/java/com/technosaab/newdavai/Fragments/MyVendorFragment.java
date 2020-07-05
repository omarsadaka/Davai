package com.technosaab.newdavai.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.technosaab.newdavai.Helper.GetCity;
import com.technosaab.newdavai.Helper.GetCountry;
import com.technosaab.newdavai.Models.CategoryID;
import com.technosaab.newdavai.Models.ClientInfo;
import com.technosaab.newdavai.Models.ClientResponse;
import com.technosaab.newdavai.Models.Vendor;
import com.technosaab.newdavai.R;
import com.technosaab.newdavai.rest.ApiClient;
import com.technosaab.newdavai.rest.ClientInterface;
import com.technosaab.newdavai.Activities.SignUpActivity;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class MyVendorFragment extends Fragment implements View.OnClickListener {
    private TextView vendorName , description , phone , location , website , email , vendorCategory;
    ImageView cover , logo , uploadCover , uploadLogo , edit , heart;
    private Button update;
    private Spinner sp_county , sp_city;
    private String clientId;
    private String lang;
    private Vendor vendorList;
    static String cover_url="",logo_url="";
    private String url ="";
    GetCountry getCountry;
    private String cityId;
    private String countryId;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = getLayoutInflater().inflate(R.layout.fragment_my_vendor, container, false);
        createView(view);
        createClicks();
        SharedPreferences Prefs = Objects.requireNonNull(getActivity()).getSharedPreferences("UserId", MODE_PRIVATE);
        String value = Prefs.getString("Id", null);
        if (value != null) {
            clientId = Prefs.getString("Id", null);
        }

        SharedPreferences Prefs3 = getActivity().getSharedPreferences("Lang", MODE_PRIVATE);
        String value3 = Prefs3.getString("Local", null);
        if (value3 != null) {
            lang = Prefs3.getString("Local", null);
        }

        progressDialog = new ProgressDialog(getContext());
        getCountry = new GetCountry(getContext());
        getCountry.getCountries2(sp_county  , R.layout.spinner_item);
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
            Toast.makeText(getContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

        }else {
            getClientById();
            spinnerItemClicked();
        }


        return view;
    }
    private void createView(View view){
        vendorName = view.findViewById(R.id.vendor_name);
        description = view.findViewById(R.id.info_text);
        phone = view.findViewById(R.id.call_text);
        location = view.findViewById(R.id.pin_text);
        sp_county = view.findViewById(R.id.sp_country);
        sp_city = view.findViewById(R.id.sp_city);
        website = view.findViewById(R.id.website_text);
        email = view.findViewById(R.id.mail_text);
        vendorCategory = view.findViewById(R.id.vendor_category);
        cover = view.findViewById(R.id.cover_logo);
        logo = view.findViewById(R.id.logo_image);
        uploadCover = view.findViewById(R.id.upload_cover);
        uploadLogo = view.findViewById(R.id.upload_logo);
        edit = view.findViewById(R.id.edit_data);
        heart = view.findViewById(R.id.heart_logo);
        update = view.findViewById(R.id.update);
    }
    private void createClicks(){
        uploadCover.setOnClickListener(this);
        uploadLogo.setOnClickListener(this);
        edit.setOnClickListener(this);
        heart.setOnClickListener(this);
        update.setOnClickListener(this);
    }
    private void getClientById(){
        ClientInterface aboutVendor = ApiClient.getClient().create(ClientInterface.class);
        Call<Vendor> call = aboutVendor.getVendorAbout(clientId);
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
                    CategoryID categoryID = vendorList.getCategoryID();
                    if (lang.equals("english")){

                        vendorCategory.setText(categoryID.getTitleEN());
                    }else if (lang.equals("ar")) {

                        vendorCategory.setText(categoryID.getTitleAr());
                    }

                    cover_url=response.body().getCover();
                    logo_url=response.body().getLogo();
                    countryId=response.body().getCountryID().getId();
                    cityId=response.body().getCityID().getId();

                    sp_county.setSelection(GetCountry.getId(countryId));
//                    sp_city.setSelection(GetCity.getId(response.body().getCityID().getId()));
//                    CountryID countryID = vendorList.getCountryID();
//                    CityID cityID = vendorList.getCityID();
//                    CategoryID categoryID = vendorList.getCategoryID();


//                    if (lang.equals("english")){
//                        country.setText(countryID.getTitleEN());
//                        city.setText(cityID.getTitleEN());
//                        sp_county.set;
//                        vendorCategory.setText(categoryID.getTitleEN());
//                    }else if (lang.equals("ar")) {
//                        country.setText(countryID.getTitleAr());
//                        city.setText(cityID.getTitleAr());
//                        vendorCategory.setText(categoryID.getTitleAr());
//                    }
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
        int id = v.getId();
        switch (id){
            case R.id.upload_cover:
                if (checkPermissionREAD_EXTERNAL_STORAGE(getContext())) {
                    // do your stuff..
                    Intent pickPhoto2 = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto2, 2);
                }
                break;
            case R.id.upload_logo:
                if (checkPermissionREAD_EXTERNAL_STORAGE(getContext())) {
                    // do your stuff..
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);
                }
                break;
            case R.id.edit_data:
                update.setVisibility(View.VISIBLE);
//                description.setText("");
//                phone.setText("");
//                location.setText("");
//                website.setText("");
//                email.setText("");
                break;
            case R.id.update:
                progressDialog.setMessage(getResources().getString(R.string.loading));
                String Description = description.getText().toString();
                String Phone = phone.getText().toString();
                String Address = location.getText().toString();
                String Website = website.getText().toString();
                String Email = email.getText().toString();
                if (!TextUtils.isEmpty(Description) && !TextUtils.isEmpty(Phone) && !TextUtils.isEmpty(Address) &&!TextUtils.isEmpty(Website) &&!TextUtils.isEmpty(Email)){
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                        if (!cover_url.isEmpty() && !logo_url.isEmpty()) {
                            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
                            if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
                                Toast.makeText(getContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                            } else {
                                updateVendor();
                            }
                        }else {
                            Toast.makeText(getContext(), getResources().getString(R.string.enter_logo_cover), Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(getContext(), getResources().getString(R.string.enter_valid_email), Toast.LENGTH_LONG).show();
                    }

                }else {
                    Toast.makeText(getContext(), getResources().getString(R.string.all_field_required), Toast.LENGTH_LONG).show();
                }

                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        File finalFile ;
        Uri selectedImage = imageReturnedIntent.getData();
        finalFile = new File(getRealPathFromURI(selectedImage));
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
            Toast.makeText(getContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

        }else {
            uploadPhoto(finalFile, requestCode);
        }

    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public void uploadPhoto(File file, final int id) {
        //File file = new File(imageUri);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        try {
            ClientInterface apiServiceTopUser = ApiClient.getClient().create(ClientInterface.class);
            Call<ResponseBody> ratevalueCallRequest = apiServiceTopUser.uploadFile(filePart);
            ratevalueCallRequest.enqueue(new Callback<ResponseBody>() {
                                             @Override
                                             public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                                 if (response.body() != null) {
                                                     // addplaceResponse.setTitle(Name);
                                                     try {
                                                         url = response.body().string();
                                                         Log.e("das2", url);
                                                     } catch (IOException e) {
                                                         e.printStackTrace();
                                                     }

                                                     Log.e("das3", url);
                                                     url.toString();
                                                     switch (id) {
                                                         case 1:
                                                             logo_url = url;
                                                             break;
                                                         case 2:
                                                             cover_url = url;
                                                             break;


                                                     }

                                                 }
                                             }

                                             @Override
                                             public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                 Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();


                                             }
                                         }
            );
        } catch (Exception e) {
            Log.e("das5", e.toString());
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case SignUpActivity.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(getContext(), "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }

    public boolean checkPermissionREAD_EXTERNAL_STORAGE(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                                    SignUpActivity.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public void showDialog(final String msg, final Context context, final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                SignUpActivity.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    private void spinnerItemClicked(){

        sp_county.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sp_county.setSelection(position);
                //if (position != 0) {
                    countryId = GetCountry.id(position);
                    GetCity getCity = new GetCity(getContext());
                    getCity.getCity2(countryId, sp_city, R.layout.spinner_item);
               // }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // if (position!=0) {
                    cityId = GetCity.id(position);

               // }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void updateVendor(){
        //getmore
        String Description = description.getText().toString();
        String Phone = phone.getText().toString();
        String Address = location.getText().toString();
        String Website = website.getText().toString();
        String Email = email.getText().toString();
        ClientInfo clientInfo = new ClientInfo( Phone ,  Description ,  Address ,Website ,
                Email ,  countryId ,   cityId , logo_url , cover_url );
        ClientInterface aboutVendor = ApiClient.getClient().create(ClientInterface.class);
        Call<ClientResponse> call = aboutVendor.updateClient(clientId , clientInfo);
        call.enqueue(new Callback<ClientResponse>() {
            @Override
            public void onResponse(Call<ClientResponse> call, Response<ClientResponse> response) {
                if (response.body()!= null){
                    Toast.makeText(getContext(), getResources().getString(R.string.data_update), Toast.LENGTH_LONG).show();
                    //update.setVisibility(View.GONE);
                    progressDialog.dismiss();
                    getClientById();
                }
            }

            @Override
            public void onFailure(Call<ClientResponse> call, Throwable t) {
                Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }
}
