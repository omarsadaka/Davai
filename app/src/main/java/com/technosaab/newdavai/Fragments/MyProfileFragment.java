package com.technosaab.newdavai.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;
import com.technosaab.newdavai.Activities.SignUpActivity;
import com.technosaab.newdavai.Adapter.ChatListRecyclerAdapter;
import com.technosaab.newdavai.Adapter.NotificationRecyclerAdapter;
import com.technosaab.newdavai.Helper.GetCity;
import com.technosaab.newdavai.Helper.GetCountry;
import com.technosaab.newdavai.Models.Notification;
import com.technosaab.newdavai.Models.NotificationResponse;
import com.technosaab.newdavai.Models.UserByIdResponse;
import com.technosaab.newdavai.Models.UserChatHistory;
import com.technosaab.newdavai.Models.userInfo;
import com.technosaab.newdavai.R;
import com.technosaab.newdavai.Util.ImageConverter;
import com.technosaab.newdavai.rest.ApiClient;
import com.technosaab.newdavai.rest.ClientInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.technosaab.newdavai.Activities.SignUpActivity.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;

public class MyProfileFragment extends Fragment implements View.OnClickListener {
    private Spinner country , city;
    private Button security , save , save2;
    private LinearLayout securityLayout , chatListLayout ,notificationLayout , messageLayout;
    NestedScrollView notificationScroll , chatListScrollView;
    private RecyclerView chatListRecycler , notificationRecycler;
    private List<UserChatHistory> chatLists;

    private ChatListRecyclerAdapter chatListRecyclerAdapter;
    private List<NotificationResponse> notificationList;
    private NotificationRecyclerAdapter notificationRecyclerAdapter;
    private String userId;
    private String email , newEmail , userName , mobile , newMobile , password , newPassword , currentPwd , repeatPwd;
    private EditText mobile_et , email_et ,currentPwd_et , newPassword_et , repeatPwd_et , forgetPwdEdit;
    private String countryId;
    private String cityId;
    GetCountry getCountry;
    private TextView UserName , forgetPassword;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private String userType;
    boolean flag=true;
    public static boolean flag2=false;
    private ImageView imgProfile;
    private static final int RESULT_LOAD_IMG = 1;
    private String url ="";
    static String cover_url="";
    private String cityID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = getLayoutInflater().inflate(R.layout.fragment_my_profile, container, false);
        createView(view);
        chatListRecycler.setHasFixedSize(true);
        GridLayoutManager gridlm = new GridLayoutManager(getContext(), 3);
        gridlm.setOrientation(GridLayoutManager.VERTICAL);
        chatListRecycler.setLayoutManager(gridlm);
        notificationList = new ArrayList<>();
        notificationRecycler.setHasFixedSize(true);
        notificationRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationRecycler.setNestedScrollingEnabled(false);




        SharedPreferences Prefs = getActivity().getSharedPreferences("UserId", MODE_PRIVATE);
        String value = Prefs.getString("Id", null);
        if (value != null) {
            userId = Prefs.getString("Id", null);
        }
        SharedPreferences prefs = getActivity().getSharedPreferences("checkUser", MODE_PRIVATE);
        String type = prefs.getString("userType", null);
        if (type != null) {
            userType = prefs.getString("userType", null);
        }

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
            Toast.makeText(getContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }else {
            getCountry = new GetCountry(getContext());
            getCountry.getCountries2(country  , R.layout.spinner_item2);
            getUserById();
            getUserChat();
            spinnerItemClicked();
            getNotification();
        }
//        SharedPreferences Pref = getActivity().getSharedPreferences("userImageProfile", MODE_PRIVATE);
//        String img = Pref.getString("userImage", null);
//        if (img != null) {
//            String image  = Pref.getString("userImage", null);
//            imgProfile.setImageBitmap(image);
//        }
        return view;
    }

    private void createView(View view){
        country = view.findViewById(R.id.country_spinner);
        city = view.findViewById(R.id.City_spinner);
        security = view.findViewById(R.id.security_btn);
        securityLayout = view.findViewById(R.id.security_layout);
        chatListLayout = view.findViewById(R.id.linear_chat_list);
        messageLayout = view.findViewById(R.id.msg_layout);
        chatListScrollView = view.findViewById(R.id.scroll);
        notificationScroll = view.findViewById(R.id.notification_scroll);
        chatListRecycler = view.findViewById(R.id.chat_list_recycler);
        notificationRecycler = view.findViewById(R.id.notification_recycler);
        notificationLayout = view.findViewById(R.id.notify_layout);
        UserName = view.findViewById(R.id.user_name);
        forgetPassword = view.findViewById(R.id.forget_pass);
        save = view.findViewById(R.id.save_btn);
        mobile_et = view.findViewById(R.id.mobile);
        email_et = view.findViewById(R.id.email);
        currentPwd_et = view.findViewById(R.id.current_pwd);
        newPassword_et = view.findViewById(R.id.new_pwd);
        repeatPwd_et = view.findViewById(R.id.repeat_pwd);
        security.setOnClickListener(this);
        messageLayout.setOnClickListener(this);
        notificationLayout.setOnClickListener(this);
        save.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
        save2 = view.findViewById(R.id.save1_btn);
        save2.setOnClickListener(this);
        imgProfile = view.findViewById(R.id.user_profile);
        imgProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
       int id = v.getId();
       switch (id){
           case R.id.security_btn:

               if (flag)securityLayout.setVisibility(View.VISIBLE);
               else securityLayout.setVisibility(View.GONE);
               flag=!flag;

//               if (flag)
//                   securityLayout.setVisibility(View.GONE);
//               else
//               {
//                   securityLayout.setVisibility(View.VISIBLE);
//                   flag=true;
//               }
               //securityLayout.setVisibility(View.VISIBLE);
               break;
           case R.id.msg_layout:
                flag2 = true;
               chatListScrollView.setVisibility(View.GONE);
               chatListRecycler.setVisibility(View.VISIBLE);
               chatListLayout.setVisibility(View.VISIBLE);
               notificationScroll.setVisibility(View.GONE);
               break;
           case R.id.notify_layout:
               flag2 = true;
               chatListScrollView.setVisibility(View.GONE);
               chatListRecycler.setVisibility(View.GONE);
               chatListLayout.setVisibility(View.GONE);
               notificationScroll.setVisibility(View.VISIBLE);
               break;
           case R.id.save_btn:
               currentPwd = currentPwd_et.getText().toString();
               repeatPwd = repeatPwd_et.getText().toString();
//               newEmail = email_et.getText().toString();
//               newMobile = mobile_et.getText().toString();
               newPassword = newPassword_et.getText().toString();
               if (!TextUtils.isEmpty(currentPwd) && !TextUtils.isEmpty(newPassword) && !TextUtils.isEmpty(repeatPwd) && currentPwd.equals(password)) {
                       if ( newPassword.equals(repeatPwd)) {

                           ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
                           if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
                               Toast.makeText(getContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                           } else {
                               security();
                           }
                       } else {
                           Toast.makeText(getContext(), getResources().getString(R.string.incorrect_data), Toast.LENGTH_LONG).show();
                       }

               }else {
                   checkEmptyField();
               }
               break;
           case R.id.forget_pass:
               dialogBuilder = new AlertDialog.Builder(getActivity());
               View view = getLayoutInflater().inflate(R.layout.forget_pwd_popup , null);
               forgetPwdEdit = view.findViewById(R.id.forget_pwd_et);
               Button send = view.findViewById(R.id.send);
               Button cancel = view.findViewById(R.id.cancel);
               dialogBuilder.setView(view);
               dialog = dialogBuilder.create();
               dialog.show();
               send.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
                       if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
                           Toast.makeText(getContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                       }else {
                           sendEmail();
                       }

                   }
               });
               cancel.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       dialog.dismiss();
                   }
               });
               break;
           case R.id.save1_btn:
               newEmail = email_et.getText().toString();
               newMobile = mobile_et.getText().toString();
               int countryId = country.getSelectedItemPosition();
               int cityId = city.getSelectedItemPosition();

               if (!TextUtils.isEmpty(newMobile)&&!TextUtils.isEmpty(newEmail)) {
                   if (android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                           ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
                           if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
                               Toast.makeText(getContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                           } else {
                               updateUser();
                           }

                   } else {

                       Toast.makeText(getActivity(), getResources().getString(R.string.enter_valid_email), Toast.LENGTH_LONG).show();
                   }
               }else {
                   checkEmptyField2();
               }

               break;
           case R.id.user_profile:

               if (checkPermissionREAD_EXTERNAL_STORAGE(getContext())) {
                   // do your stuff..
                   Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                           android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                   startActivityForResult(pickPhoto, 1);
               }
//               Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//               photoPickerIntent.setType("image/*");
//               startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
               break;
       }
    }
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        File finalFile ;
        try {
            Uri selectedImage = data.getData();
            if (selectedImage!=null) {
                finalFile = new File(getRealPathFromURI(selectedImage));
                uploadPhoto(finalFile , reqCode);
            }else {
                Toast.makeText(getContext(), getResources().getString(R.string.no_select_image), Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){

        }

//        if (resultCode == RESULT_OK) {
//            try {
//                final Uri imageUri = data.getData();
//                final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
//                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                imgProfile.setImageBitmap(selectedImage);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
//            }
//
//        }else {
//            Toast.makeText(getContext(), "You haven't picked Image",Toast.LENGTH_LONG).show();
//        }
    }
    public void getUserById(){
        ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
        Call<UserByIdResponse> call = clientInterface.getUserById(userId);
        call.enqueue(new Callback<UserByIdResponse>() {
            @Override
            public void onResponse(Call<UserByIdResponse> call, Response<UserByIdResponse> response) {
                     if (response.body()!=null){
                         email = response.body().getEmail();
                         userName = response.body().getFirstName();
                         password = response.body().getPassword();
                         mobile = response.body().getMobile();
                         UserName.setText(userName);
                         mobile_et.setText(mobile);
                         email_et.setText(email);
                         cover_url = response.body().getPersonalImg();
                         if (response.body().getPersonalImg()!= null){
                             Picasso.get().load(response.body().getPersonalImg()).transform(new ImageConverter()).into(imgProfile);
                         }else imgProfile.setBackgroundResource(R.drawable.user_name);
                         country.setSelection(GetCountry.getId(response.body().getCountryID().getId()));

                         //city.setSelection(GetCity.getId(response.body().getCityID().getId()));
//                         cityID = response.body().getCityID().getId();
//                         GetCity getCity = new GetCity(getContext());
//                         getCity.getCity2(response.body().getCountryID().getId(), city, R.layout.spinner_item2);
//                         city.setSelection(GetCity.getId(cityID));

                     }
            }

            @Override
            public void onFailure(Call<UserByIdResponse> call, Throwable t) {
                Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateUser(){
       // UpdateUser updateUser = new UpdateUser(newPassword , newMobile ,newEmail ,countryId , cityId);
        userInfo updateUser = new userInfo(newEmail ,  newMobile ,  countryId , cityId , cover_url);
        ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
        Call<ResponseBody> call = clientInterface.updateUser( userId, updateUser);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body()!=null){
                    Toast.makeText(getContext(), getResources().getString(R.string.user_update), Toast.LENGTH_LONG).show();
                    mobile_et.setText("");
                    email_et.setText("");
//                    currentPwd_et.setText("");
//                    newPassword_et.setText("");
//                    repeatPwd_et.setText("");

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void security(){
        // UpdateUser updateUser = new UpdateUser(newPassword , newMobile ,newEmail ,countryId , cityId);
        userInfo updateUser = new userInfo(newPassword );
        ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
        Call<ResponseBody> call = clientInterface.updateUser( userId, updateUser);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body()!=null){
                    Toast.makeText(getContext(), getResources().getString(R.string.security_update), Toast.LENGTH_LONG).show();
                    currentPwd_et.setText("");
                    newPassword_et.setText("");
                    repeatPwd_et.setText("");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void spinnerItemClicked(){

        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                country.setSelection(position);
                //if (position != 0) {
                    countryId = GetCountry.id(position );
                    GetCity getCity = new GetCity(getContext());
                    getCity.getCity2(countryId, city, R.layout.spinner_item2);
                    //city.setSelection(GetCity.getId(cityID));
               // }
//                countryId= GetCountry.id(position);
//                GetCity getCity = new GetCity(getContext());
//                getCity.getCity(countryId, city  , R.layout.spinner_item2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // if (position!=0) {
                    cityId = GetCity.id(position);

             //   }
                //cityId= GetCity.id(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void sendEmail(){
        String email = forgetPwdEdit.getText().toString();
        if (!TextUtils.isEmpty(email)){
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
                Call<ResponseBody> call = clientInterface.forgetPassword(email);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.body() != null) {
                            Toast.makeText(getContext(), getResources().getString(R.string.pwd_send), Toast.LENGTH_LONG).show();
                            forgetPwdEdit.setText("");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                    }
                });
            }else {
                Toast.makeText(getActivity(), getResources().getString(R.string.enter_valid_email), Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(getActivity(), getResources().getString(R.string.enter_your_email), Toast.LENGTH_LONG).show();
        }
    }

    private void getUserChat(){
        ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
        Call<List<UserChatHistory>> call = clientInterface.getUserChat(userId);
        call.enqueue(new Callback<List<UserChatHistory>>() {
            @Override
            public void onResponse(Call<List<UserChatHistory>> call, Response<List<UserChatHistory>> response) {
                if (response.body()!=null){
                    chatLists = new ArrayList<>();
                    chatLists = response.body();
                    chatListRecyclerAdapter = new ChatListRecyclerAdapter(getContext() , chatLists);
                    chatListRecycler.setAdapter(chatListRecyclerAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<UserChatHistory>> call, Throwable t) {
                Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();

            }
        });
    }

    public void checkEmptyField() {
        currentPwd = currentPwd_et.getText().toString();
        repeatPwd = repeatPwd_et.getText().toString();
        newPassword = newPassword_et.getText().toString();

        if (TextUtils.isEmpty(currentPwd)) {
            currentPwd_et.setError(getResources().getString(R.string.not_empity));
        } else if (TextUtils.isEmpty(newPassword)) {
            newPassword_et.setError(getResources().getString(R.string.not_empity));

        } else if (TextUtils.isEmpty(repeatPwd)) {
            repeatPwd_et.setError(getResources().getString(R.string.not_empity));
        }else if (!currentPwd.equals(password)){
            Toast.makeText(getContext(), getResources().getString(R.string.current_pwd_error), Toast.LENGTH_LONG).show();
        }
    }

    public void checkEmptyField2() {
        newEmail = email_et.getText().toString();
        newMobile = mobile_et.getText().toString();
        if (TextUtils.isEmpty(newMobile)) {
            mobile_et.setError(getResources().getString(R.string.not_empity));
        } else if (TextUtils.isEmpty(newEmail)) {
            email_et.setError(getResources().getString(R.string.not_empity));

        }
//        else if (country.getSelectedItemPosition()==0) {
//            Toast.makeText(getContext(), getResources().getString(R.string.select_country), Toast.LENGTH_LONG).show();
//
//        } else if (city.getSelectedItemPosition()==0) {
//            Toast.makeText(getContext(), getResources().getString(R.string.select_city), Toast.LENGTH_LONG).show();
//        }

    }


    private void getNotification () {

        if (userType.equals("1")){
            ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
            Call<List<NotificationResponse>> call = clientInterface.getUserNotify(userId);
            call.enqueue(new Callback<List<NotificationResponse>>() {
                @Override
                public void onResponse(Call<List<NotificationResponse>> call, Response<List<NotificationResponse>> response) {
                    if (response.body()!=null){
                        notificationList = response.body();
                        notificationRecyclerAdapter = new NotificationRecyclerAdapter(notificationList , getContext());
                        notificationRecycler.setAdapter(notificationRecyclerAdapter);
                    }
                }

                @Override
                public void onFailure(Call<List<NotificationResponse>> call, Throwable t) {

                }
            });


        }

    }


    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public void uploadPhoto(File file , final int id) {
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
                                                     cover_url = url;
                                                     Picasso.get().load(cover_url).transform(new ImageConverter()).into(imgProfile);

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
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
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
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
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
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }
}
