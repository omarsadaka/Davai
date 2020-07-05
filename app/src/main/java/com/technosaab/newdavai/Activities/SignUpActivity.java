package com.technosaab.newdavai.Activities;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.technosaab.newdavai.Adapter.CategoryAdapter;
import com.technosaab.newdavai.Adapter.OnClickCategoryAdapter;
import com.technosaab.newdavai.Adapter.TermsConditionAdapter;
import com.technosaab.newdavai.Helper.GetCity;
import com.technosaab.newdavai.Helper.GetCountry;
import com.technosaab.newdavai.Models.CategoryResponse;
import com.technosaab.newdavai.Models.CategoryService;
import com.technosaab.newdavai.Models.ClientInfo;
import com.technosaab.newdavai.Models.ClientResponse;
import com.technosaab.newdavai.Models.ServiceModel;
import com.technosaab.newdavai.Models.TermsModel;
import com.technosaab.newdavai.Models.UserResponse;
import com.technosaab.newdavai.Models.userInfo;
import com.technosaab.newdavai.R;
import com.technosaab.newdavai.Util.GetUserData;
import com.technosaab.newdavai.Util.MultiSelectionSpinner;
import com.technosaab.newdavai.rest.ApiClient;
import com.technosaab.newdavai.rest.ClientInterface;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private Button user, vendor, userSignUp, vendorSignUp, next;
    private List<CategoryService> categoryServices;
    private LinearLayout userLayout, conditionLayout, vendorLayout, moreVendorLayout, typeLayout , select_time_layout;
    private RelativeLayout uploadLogo, uploadCover;
    private TextView category, service, barTitle , userTerms , vendorTerms , workingHour;
    private AlertDialog dialog;
    private ImageView back;
    private EditText userFirstName, userLastName, userMobile, userEmail, userPassword, userConfirmPassword;
    private EditText vendorName, vendorPhone, vendorWebsite, vendorEmail, vendorAddress  ,userName, mobile, email, password, confirmPwd, vendorDescription ;
    private Spinner vendorCountry, vendorCity, userCountry, userCity ;
    Spinner sp_workingDay;
    private RadioGroup radioGroup;
    private CheckBox userCondition, vendorCondition;
    Intent intent;
    String Gender;
    int genderType;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    RequestQueue queue;
    String lang;
    private ProgressDialog progressDialog;
    GetCountry getCountry;
    private List<CategoryResponse> categoryList;
    private String categoryId;
    private String cityId;
    private String countryId;
    static String cover_url="",logo_url="";
    private String url ="";
    List<ServiceModel> serviceModelList;
    private List<TermsModel> lists;
    private TermsConditionAdapter termsConditionAdapter;
    private AccessToken accessToken;
    private TwitterSession twitterSession;
    private SharedPreferences  prefs;
    String type;
    List<String> listDays=new ArrayList<>();
    private TextView from , to;
    private Calendar myCalendar;
    int hour1 ;
    int minute1;
    String time1;
    private ProgressBar categoryBar , serviceesBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        createView();
        createClick();

        prefs = getSharedPreferences("checkLogin", MODE_PRIVATE);
        String restoredText = prefs.getString("loginType", null);
        if (restoredText != null) {
            type = prefs.getString("loginType", "No name defined");//"No name defined" is the default value.
        }
        queue = Volley.newRequestQueue(this);
        getCountry = new GetCountry(this);
        categoryServices = new ArrayList<>();
        SharedPreferences Prefs = getSharedPreferences("Lang", MODE_PRIVATE);
        String value = Prefs.getString("Local", null);
        if (value != null) {
            lang = Prefs.getString("Local", null);
        }
        SharedPreferences Pref = getSharedPreferences("CategoryId", MODE_PRIVATE);
        String valueKey = Pref.getString("categoryId", null);
        if (valueKey != null) {
            categoryId = Pref.getString("categoryId", null);
        }
        categoryList = new ArrayList<>();
        progressDialog = new ProgressDialog(SignUpActivity.this);
        barTitle.setText(getResources().getString(R.string.sign_up));
        intent = new Intent();
        getCountry.getCountries(userCountry  , R.layout.spinner_item3);
        spinnerItemClicked();
        setUserData();

        listDays.add(getResources().getString(R.string.SelectOFDay));
        listDays.add(getResources().getString(R.string.non));
        listDays.add(getResources().getString(R.string.saterday));
        listDays.add(getResources().getString(R.string.sunday));
        listDays.add(getResources().getString(R.string.monday));
        listDays.add(getResources().getString(R.string.tuesday));
        listDays.add(getResources().getString(R.string.wednsday));
        listDays.add(getResources().getString(R.string.thursday));
        listDays.add(getResources().getString(R.string.friday));

        //sp_workingDay.setItems(listDays);
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item3, listDays);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_workingDay.setAdapter(dayAdapter);

    }

    private void createView() {
        user = findViewById(R.id.user_btn);
        vendor = findViewById(R.id.vendor_btn);
        userSignUp = findViewById(R.id.user_sign_up);
        vendorSignUp = findViewById(R.id.v_sign_up);
        userLayout = findViewById(R.id.user_layout);
        conditionLayout = findViewById(R.id.terms_layout);
        vendorLayout = findViewById(R.id.vendor_layout);
        moreVendorLayout = findViewById(R.id.v_more_layout);
        category = findViewById(R.id.v_category);
        service = findViewById(R.id.v_service);
        back = findViewById(R.id.back);
        next = findViewById(R.id.next_btn);
        vendorCountry = findViewById(R.id.Spinner_country);
        vendorCity = findViewById(R.id.Spinner_city);
        typeLayout = findViewById(R.id.linearLayout2);
        uploadLogo = findViewById(R.id.upload_logo);
        uploadCover = findViewById(R.id.upload_cover);
        barTitle = findViewById(R.id.titleBar);
        userCountry = findViewById(R.id.sp_country);
        userCity = findViewById(R.id.sp_city);
        userFirstName = findViewById(R.id.first_name);
        userLastName = findViewById(R.id.last_name);
        userMobile = findViewById(R.id.mobile);
        userEmail = findViewById(R.id.email);
        userPassword = findViewById(R.id.password);
        userConfirmPassword = findViewById(R.id.confirm_pwd);
        radioGroup = findViewById(R.id.radio_group);
        userCondition = findViewById(R.id.user_checkbox);
        vendorName = findViewById(R.id.vendor_name);
        vendorPhone = findViewById(R.id.vendor_phone);
        vendorWebsite = findViewById(R.id.vendor_website);
        vendorEmail = findViewById(R.id.vendor_email);
        userName = findViewById(R.id.vendor_user_name);
        mobile = findViewById(R.id.v_mobile);
        email = findViewById(R.id.v_email);
        password = findViewById(R.id.v_password);
        confirmPwd = findViewById(R.id.v_confirm_pwd);
        vendorDescription = findViewById(R.id.v_description);
        vendorSignUp = findViewById(R.id.v_sign_up);
        vendorCondition = findViewById(R.id.v_checkbox);
        vendorAddress = findViewById(R.id.vendor_address);
        sp_workingDay = findViewById(R.id.v_working_day);
        workingHour = findViewById(R.id.v_working_hours);
        userTerms = findViewById(R.id.user_terms);
        vendorTerms = findViewById(R.id.vendor_terms);
        select_time_layout = findViewById(R.id.select_time_layout);
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);



    }

    private void createClick() {
        user.setOnClickListener(this);
        vendor.setOnClickListener(this);
        next.setOnClickListener(this);
        category.setOnClickListener(this);
        service.setOnClickListener(this);
        back.setOnClickListener(this);
        uploadLogo.setOnClickListener(this);
        uploadCover.setOnClickListener(this);
        userSignUp.setOnClickListener(this);
        vendorSignUp.setOnClickListener(this);
        userTerms.setOnClickListener(this);
        vendorTerms.setOnClickListener(this);
        workingHour.setOnClickListener(this);
        from.setOnClickListener(this);
        to.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.user_btn:

                vendorLayout.setVisibility(View.GONE);
                next.setVisibility(View.GONE);
                moreVendorLayout.setVisibility(View.GONE);
                userLayout.setVisibility(View.VISIBLE);
                userSignUp.setVisibility(View.VISIBLE);
                radioGroup.setVisibility(View.VISIBLE);
                conditionLayout.setVisibility(View.VISIBLE);
                typeLayout.setVisibility(View.VISIBLE);
                user.setBackground(getResources().getDrawable(R.drawable.after_press_bg));
                user.setTextColor(getResources().getColor(R.color.colorBlue));
                vendor.setBackground(getResources().getDrawable(R.drawable.login_bg_btn));
                vendor.setTextColor(getResources().getColor(R.color.colorWhite));
                break;
            case R.id.vendor_btn:

                workingHour.setVisibility(View.VISIBLE);
                select_time_layout.setVisibility(View.GONE);

                getCountry.getCountries(vendorCountry  , R.layout.spinner_item3);
                vendorLayout.setVisibility(View.VISIBLE);
                typeLayout.setVisibility(View.VISIBLE);
                userLayout.setVisibility(View.GONE);
                userSignUp.setVisibility(View.GONE);
                radioGroup.setVisibility(View.GONE);
                moreVendorLayout.setVisibility(View.GONE);
                conditionLayout.setVisibility(View.GONE);
                next.setVisibility(View.VISIBLE);
                vendor.setBackground(getResources().getDrawable(R.drawable.after_press_bg));
                vendor.setTextColor(getResources().getColor(R.color.colorBlue));
                user.setBackground(getResources().getDrawable(R.drawable.login_bg_btn));
                user.setTextColor(getResources().getColor(R.color.colorWhite));
                setUserData2();
//                if (type!=null) {
//                    if (type.equals("facebook")) {
//                        GetUserData.facebookEmail(accessToken, vendorName, userName, vendorEmail);
//                    }
//                }
                break;
            case R.id.next_btn:
                String Vname = vendorName.getText().toString().trim();
                String Vphone = vendorPhone.getText().toString().trim();
                String Vwebsite = vendorWebsite.getText().toString().trim();
                String Vemail = vendorEmail.getText().toString().trim();
                int Vdays = sp_workingDay.getSelectedItemPosition();
               //todo check workingHours
                String timeFrom = from.getText().toString();
                String timeTo = to.getText().toString();
                String Vaddress = vendorAddress.getText().toString().trim();
                int Country = vendorCountry.getSelectedItemPosition();
                int City = vendorCity.getSelectedItemPosition();

                if (!TextUtils.isEmpty(Vname) && !TextUtils.isEmpty(Vphone) && Country!=0 && City!=0 && !category.getText().toString().equals(getString(R.string.category))
                &&!TextUtils.isEmpty(Vwebsite) && !TextUtils.isEmpty(Vemail) && !TextUtils.isEmpty(Vaddress) && Vdays!=0 && !cover_url.equals("") &&
                        !logo_url.equals("") && !timeFrom.equals(getResources().getString(R.string.from)) && !timeTo.equals(getResources().getString(R.string.to))) {
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(Vemail).matches()){
                        if (android.util.Patterns.PHONE.matcher(Vphone).matches()) {
                            moreVendorLayout.setVisibility(View.VISIBLE);
                            userLayout.setVisibility(View.GONE);
                            userSignUp.setVisibility(View.GONE);
                            radioGroup.setVisibility(View.GONE);
                            typeLayout.setVisibility(View.GONE);
                            conditionLayout.setVisibility(View.GONE);
                            vendorLayout.setVisibility(View.GONE);
                            next.setVisibility(View.GONE);
                        }else {
                            Toast.makeText(this, getResources().getString(R.string.enter_valid_phone), Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(this, getResources().getString(R.string.enter_valid_email), Toast.LENGTH_LONG).show();
                    }
                } else checkVendorEmptyField();


                break;
            case R.id.v_category:
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.category_popup, null);
                RecyclerView categoryRecycler = view.findViewById(R.id.category_recycler);
                ImageView close = view.findViewById(R.id.close);
                Button done = view.findViewById(R.id.category_btn_done);
                categoryBar = view.findViewById(R.id.category_bar);
                categoryRecycler.setHasFixedSize(true);
                categoryRecycler.setLayoutManager(new LinearLayoutManager(this));
                getCategory(categoryRecycler);
                dialogBuilder.setView(view);
                dialog = dialogBuilder.create();
                dialog.show();
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });
                break;
            case R.id.v_service:
                dialogBuilder = new AlertDialog.Builder(this);
                @SuppressLint("InflateParams") View views = getLayoutInflater().inflate(R.layout.onclick_category_popup, null);
                ImageView Close = views.findViewById(R.id.onclick_popup_close);
                TextView categoryName = views.findViewById(R.id.onclick_category);
                Button Done = views.findViewById(R.id.category_btn_done);
                serviceesBar = views.findViewById(R.id.services_bar);
                serviceesBar.setVisibility(View.VISIBLE);
                categoryName.setText(category.getText().toString());
                RecyclerView recyclerView = views.findViewById(R.id.onclick_category_recycler);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                getCategoryService(recyclerView);
                dialogBuilder.setView(views);
                dialog = dialogBuilder.create();

                recyclerView.post(new Runnable() {

                    @Override
                    public void run() {
                        dialog.getWindow().clearFlags(
                                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                                        WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                    }
                });
                dialog.show();
                Close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                Done.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View v) {
                        ServiceModel serviceModel = new ServiceModel();
                        int price = serviceModel.getServicePrice();
                        //serviceModelList = new ArrayList<>();
                        serviceModelList = OnClickCategoryAdapter.getServiceModels();
                        if (serviceModelList.size()<1){
                            Toast.makeText(SignUpActivity.this,getResources().getString(R.string.check_service), Toast.LENGTH_LONG).show();
                        }else {
                             for(ServiceModel c : serviceModelList){
                                 if(c.getServicePrice()==0 || c.getServiceEmployees().size()<1 ){
                                     Toast.makeText(SignUpActivity.this, getResources().getString(R.string.enter_price) , Toast.LENGTH_LONG).show();
                                 }else {
                                     dialog.dismiss();
                                 }
                             }
//                             if(serviceModel.getServicePrice()==0){
//                                 Toast.makeText(SignUpActivity.this, getResources().getString(R.string.enter_price) , Toast.LENGTH_LONG).show();
//                             }else
//                                 if(serviceModel.getServiceEmployees().size()==0){
//                                 Toast.makeText(SignUpActivity.this, getResources().getString(R.string.one_employee) , Toast.LENGTH_LONG).show();
//                             }else {
//                                 dialog.dismiss();
//                             }
                        }
                    }
                });
                break;
            case R.id.back:
                onBackPressed();
                SignUpActivity.this.finish();
                break;
            case R.id.upload_logo:
                if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {
                    // do your stuff..
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);
                }

                break;
            case R.id.upload_cover:
                if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {
                    // do your stuff..
                    Intent pickPhoto2 = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto2, 2);
                }

                break;
            case R.id.user_sign_up:
                String Fname = userFirstName.getText().toString();
                String Lname = userLastName.getText().toString();
                String Mobile = userMobile.getText().toString();
                String Email = userEmail.getText().toString().trim();
                String Password = userPassword.getText().toString();
                String Confirm_pwd = userConfirmPassword.getText().toString();
                int country = userCountry.getSelectedItemPosition();
                int city = userCity.getSelectedItemPosition();
                if (!Fname.isEmpty() && !Lname.isEmpty() && !Mobile.isEmpty() && !Email.isEmpty() && !Password.isEmpty() &&
                        !Confirm_pwd.isEmpty() && country!=0 && city!=0 && radioGroup.getCheckedRadioButtonId() != -1 && userCondition.isChecked()) {
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                        if (android.util.Patterns.PHONE.matcher(Mobile).matches()) {
                            if (Password.equals(Confirm_pwd)) {
                                progressDialog.setMessage(getResources().getString(R.string.loading));
                                progressDialog.show();
                                ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                                if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
                                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                } else {
                                    userSignUp();
                                }
                            } else {
                                Toast.makeText(this, getResources().getString(R.string.incorrect_data), Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(this, getResources().getString(R.string.enter_valid_phone), Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(this, getResources().getString(R.string.enter_valid_email), Toast.LENGTH_LONG).show();
                    }

                } else checkUserEmptyField();
                break;
            case R.id.v_sign_up:
                String Uname = userName.getText().toString();
                String Umobile = mobile.getText().toString();
                String Uemail = email.getText().toString().trim();
                String Upasssword = password.getText().toString();
                String UconfurmPwd = confirmPwd.getText().toString();
                String Vdescription = vendorDescription.getText().toString();
                if (!TextUtils.isEmpty(Uname) && !TextUtils.isEmpty(Umobile) && !TextUtils.isEmpty(Uemail) && !TextUtils.isEmpty(Upasssword) &&
                        !TextUtils.isEmpty(UconfurmPwd) && !TextUtils.isEmpty(Vdescription)  && vendorCondition.isChecked() ) {
                    if ( android.util.Patterns.EMAIL_ADDRESS.matcher(Uemail).matches()){
                        if (android.util.Patterns.PHONE.matcher(Umobile).matches()) {
                            if (Upasssword.equals(UconfurmPwd)) {
                                ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                                if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
                                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                } else {
                                    progressDialog.setMessage(getResources().getString(R.string.loading));
                                    progressDialog.show();
                                    vendorSignUp();
                                }

                            } else {
                                Toast.makeText(this, getResources().getString(R.string.incorrect_data), Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(this, getResources().getString(R.string.enter_valid_phone), Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(this, getResources().getString(R.string.enter_valid_email), Toast.LENGTH_LONG).show();
                    }
                } else checkVendorEmptyField2();
                break;
            case R.id.user_terms:
                dialogBuilder = new AlertDialog.Builder(this);
                @SuppressLint("InflateParams") View userTerms = getLayoutInflater().inflate(R.layout.terms_layout, null);
                ImageView termsUserClose = userTerms.findViewById(R.id.terms_close);
                RecyclerView termsUserRecyclerView = userTerms.findViewById(R.id.terms_recycler);
                TextView userTerm = userTerms.findViewById(R.id.terms);
                userTerm.setText(getResources().getString(R.string.user_terms));
                termsUserRecyclerView.setHasFixedSize(true);
                termsUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                } else {
                    getUserTerms(termsUserRecyclerView);
                }

                dialogBuilder.setView(userTerms);
                dialog = dialogBuilder.create();
                dialog.show();
                termsUserClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                break;
            case R.id.vendor_terms:
                dialogBuilder = new AlertDialog.Builder(this);
                @SuppressLint("InflateParams") View vendorTerms = getLayoutInflater().inflate(R.layout.terms_layout, null);
                ImageView termsVendorClose = vendorTerms.findViewById(R.id.terms_close);
                RecyclerView termsVendorRecyclerView = vendorTerms.findViewById(R.id.terms_recycler);
                TextView vendorTerm = vendorTerms.findViewById(R.id.terms);
                vendorTerm.setText(getResources().getString(R.string.vendor_terms));
                termsVendorRecyclerView.setHasFixedSize(true);
                termsVendorRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                } else {
                    getVendorTerms(termsVendorRecyclerView);
                }

                dialogBuilder.setView(vendorTerms);
                dialog = dialogBuilder.create();
                dialog.show();
                termsVendorClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.v_working_hours:
                workingHour.setVisibility(View.GONE);
                select_time_layout.setVisibility(View.VISIBLE);
                from.setText(getResources().getString(R.string.from));
                to.setText(getResources().getString(R.string.to));
                break;
            case R.id.from:
                myCalendar = Calendar.getInstance();
                 hour1 = myCalendar.get(Calendar.HOUR_OF_DAY);
                 minute1 = myCalendar.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(this, R.style.DialogTheme ,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                         time1 = selectedHour + ":" + selectedMinute;
                        from.setText( time1);
                    }
                }, hour1, minute1, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                break;
            case R.id.to:
                myCalendar = Calendar.getInstance();
                final int hour2 = myCalendar.get(Calendar.HOUR_OF_DAY);
                int minute2 = myCalendar.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker2;
                mTimePicker2 = new TimePickerDialog(this, R.style.DialogTheme ,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        to.setText(selectedHour + ":" + selectedMinute);

//                        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
//                        try {
//                            if (dateFormat.parse(time1).after(dateFormat.parse(selectedHour + ":" + selectedMinute))){
//                                Toast.makeText(SignUpActivity.this, "error time", Toast.LENGTH_LONG).show();
//                                to.setText(getResources().getString(R.string.to));
//                            }else {
//                                to.setText(selectedHour + ":" + selectedMinute);
//                            }
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }


                    }
                }, hour2, minute2, true);//Yes 24 hour time
                mTimePicker2.setTitle("Select Time");
                mTimePicker2.show();
                break;
        }

    }

    private void userSignUp() {
        String Fname = userFirstName.getText().toString().trim();
        String Lname = userLastName.getText().toString().trim();
        String Mobile = userMobile.getText().toString().trim();
        String Email = userEmail.getText().toString().trim();
        String Password = userPassword.getText().toString().trim();
       // int CountryId = userCountry.getSelectedItemPosition()+1;
        int id = radioGroup.getCheckedRadioButtonId();
        View radioButton = radioGroup.findViewById(id);
        int radioId = radioGroup.indexOfChild(radioButton);
        RadioButton gender = (RadioButton) radioGroup.getChildAt(radioId);
        Gender = (String) gender.getText();
        if (Gender.equals("Male")){
            genderType = 1;
        }else if (Gender.equals("Female")){
            genderType = 2;
        }
        userInfo userInfo = new userInfo(Email , Fname , Lname , Mobile , Password , genderType ,countryId , cityId );
        ClientInterface addNewUser = ApiClient.getClient().create(ClientInterface.class);
        Call<UserResponse> call = addNewUser.addNewUser(userInfo);
        call.enqueue(new Callback<UserResponse>(){

            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                if (response.body() != null) {
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.successfully_signUp), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                    SignUpActivity.this.finish();
                }else if(response.errorBody()!=null){
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.email_exist), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                Toast.makeText(SignUpActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    private void vendorSignUp() {
        String Vname = vendorName.getText().toString().trim();
        String Vphone = vendorPhone.getText().toString().trim();
        String Vwibsite = vendorWebsite.getText().toString().trim();
        String Vemail = vendorEmail.getText().toString().trim();
        String Vaddress = vendorAddress.getText().toString().trim();
        String Uname = userName.getText().toString().trim();
        String Umobile = mobile.getText().toString().trim();
        int  pos = sp_workingDay.getSelectedItemPosition();
        String time = from.getText().toString();
        String time2 = to.getText().toString();
        int Vdays = dayName(pos);
        int timeFrom = Integer.parseInt(time.substring(0,time.indexOf(":")));
        int timeTo = Integer.parseInt(time2.substring(0,time2.indexOf(":")));

        //todo check workingHours
        String Uemail = email.getText().toString().trim();
        String Password = password.getText().toString().trim();
        String Description = vendorDescription.getText().toString().trim();

        ClientInfo clientInfo = new ClientInfo(Vname , Uname , Vphone , Vdays , Description , timeFrom , timeTo , Vaddress , Vwibsite , Umobile ,
                Vemail ,Uemail , Password ,countryId , categoryId,cityId ,logo_url , cover_url , serviceModelList);

        ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
        Call<ClientResponse> call = clientInterface.addNewVendor(clientInfo);
        call.enqueue(new Callback<ClientResponse>() {
            @Override
            public void onResponse(@NonNull Call<ClientResponse> call, @NonNull Response<ClientResponse> response) {
                       if (response.body()!=null){
                           Toast.makeText(SignUpActivity.this, getResources().getString(R.string.successfully_signUp), Toast.LENGTH_LONG).show();
                           progressDialog.dismiss();
                           serviceModelList.clear();
                           startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                           SignUpActivity.this.finish();
                       }else if(response.errorBody()!=null){
                           progressDialog.dismiss();
                           Toast.makeText(SignUpActivity.this, getResources().getString(R.string.email_exist), Toast.LENGTH_LONG).show();
                       }
            }

            @Override
            public void onFailure(@NonNull Call<ClientResponse> call, @NonNull Throwable t) {
                Toast.makeText(SignUpActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void checkUserEmptyField() {
        String Fname = userFirstName.getText().toString();
        String Lname = userLastName.getText().toString();
        String Mobile = userMobile.getText().toString();
        String Email = userEmail.getText().toString().trim();
        int countryId = userCountry.getSelectedItemPosition();
        int cityId = userCity.getSelectedItemPosition();
        String Password = userPassword.getText().toString();
        String Confirm_pwd = userConfirmPassword.getText().toString();
        if (TextUtils.isEmpty(Fname)) {
            userFirstName.setError(getResources().getString(R.string.not_empity));
        } else if (TextUtils.isEmpty(Lname)) {
            userLastName.setError(getResources().getString(R.string.not_empity));

        } else if (TextUtils.isEmpty(Mobile)) {
            userMobile.setError(getResources().getString(R.string.not_empity));

        } else if (TextUtils.isEmpty(Email)) {
            userEmail.setError(getResources().getString(R.string.not_empity));

        }else if (countryId ==0) {
            Toast.makeText(this, getResources().getString(R.string.select_country), Toast.LENGTH_LONG).show();

        }else if (cityId ==0) {
            Toast.makeText(this, getResources().getString(R.string.select_city), Toast.LENGTH_LONG).show();

        }
        else if (TextUtils.isEmpty(Password)) {
            userPassword.setError(getResources().getString(R.string.not_empity));

        } else if (TextUtils.isEmpty(Confirm_pwd)) {
            userConfirmPassword.setError(getResources().getString(R.string.not_empity));

        } else if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, getResources().getString(R.string.choose_gender), Toast.LENGTH_LONG).show();

        } else if (!userCondition.isChecked()) {
            Toast.makeText(this, getResources().getString(R.string.check_condition), Toast.LENGTH_LONG).show();

        }

    }

    private void checkVendorEmptyField2() {
        String Uname = userName.getText().toString();
        String Umobile = mobile.getText().toString();
        String Uemail = email.getText().toString().trim();
        String Upasssword = password.getText().toString();
        String UconfurmPwd = confirmPwd.getText().toString();
        String Vdescription = vendorDescription.getText().toString();
        if (TextUtils.isEmpty(Uname)) {
            userName.setError(getResources().getString(R.string.not_empity));
        } else if (TextUtils.isEmpty(Umobile)) {
            mobile.setError(getResources().getString(R.string.not_empity));

        } else if (TextUtils.isEmpty(Uemail)) {
            email.setError(getResources().getString(R.string.not_empity));
        } else if (TextUtils.isEmpty(Upasssword)) {
            password.setError(getResources().getString(R.string.not_empity));
        } else if (TextUtils.isEmpty(UconfurmPwd)) {
            confirmPwd.setError(getResources().getString(R.string.not_empity));

        } else if (TextUtils.isEmpty(Vdescription)) {
            vendorDescription.setError(getResources().getString(R.string.not_empity));

        } else if (!vendorCondition.isChecked()) {
            Toast.makeText(this, getResources().getString(R.string.check_condition), Toast.LENGTH_LONG).show();

        }

    }

    private void checkVendorEmptyField() {
        String Vname = vendorName.getText().toString();
        String Vphone = vendorPhone.getText().toString();
        int countryId = vendorCountry.getSelectedItemPosition();
        int cityId = vendorCity.getSelectedItemPosition();
        String Vwebsite = vendorWebsite.getText().toString();
        String Vemail = vendorEmail.getText().toString().trim();
        String Vaddress = vendorAddress.getText().toString();
        int Vdays = sp_workingDay.getSelectedItemPosition();
        //todo check workingHours
        String timeFrom = from.getText().toString();
        String timeTo = to.getText().toString();

        if (TextUtils.isEmpty(Vname)) {
            vendorName.setError(getResources().getString(R.string.not_empity));
        } else if (TextUtils.isEmpty(Vphone)) {
            vendorPhone.setError(getResources().getString(R.string.not_empity));

        } else if (countryId ==0) {
            Toast.makeText(this, getResources().getString(R.string.select_country), Toast.LENGTH_LONG).show();
        }else if (cityId ==0) {
            Toast.makeText(this, getResources().getString(R.string.select_city), Toast.LENGTH_LONG).show();
        }
        else if (category.getText().toString().equals(getString(R.string.category))){
            Toast.makeText(this, getResources().getString(R.string.select_category), Toast.LENGTH_LONG).show();
        } else if (timeFrom.equals(getString(R.string.from))){
            Toast.makeText(this, getResources().getString(R.string.select_time), Toast.LENGTH_LONG).show();
        }else if (timeTo.equals(getString(R.string.to))){
            Toast.makeText(this, getResources().getString(R.string.select_time), Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(Vwebsite)) {
            vendorWebsite.setError(getResources().getString(R.string.not_empity));

        } else if (TextUtils.isEmpty(Vemail)) {
            vendorEmail.setError(getResources().getString(R.string.not_empity));

        }else if (TextUtils.isEmpty(Vaddress)) {
            vendorAddress.setError(getResources().getString(R.string.not_empity));

        } else if (Vdays==0) {
            Toast.makeText(this, getResources().getString(R.string.select_workingDays), Toast.LENGTH_LONG).show();

        }
//        else if (Vhours==0) {
//            Toast.makeText(this, getResources().getString(R.string.select_workingHour), Toast.LENGTH_LONG).show();
//
//        }
        else if (cover_url.isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.select_cover), Toast.LENGTH_LONG).show();

        } else if (logo_url.isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.select_logo), Toast.LENGTH_LONG).show();


        }

    }

    private void getCategory(final RecyclerView recyclerView){
        ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
        Call<List<CategoryResponse>> call = clientInterface.getAllCategories();
        call.enqueue(new Callback<List<CategoryResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<CategoryResponse>> call, @NonNull Response<List<CategoryResponse>> response) {
                if (response.body()!=null){
                    categoryList = response.body();
                    CategoryAdapter categoryAdapter = new CategoryAdapter( SignUpActivity.this ,categoryList , category);
                    recyclerView.setAdapter(categoryAdapter);
                    categoryBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CategoryResponse>> call, @NonNull Throwable t) {
                Toast.makeText(SignUpActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                categoryBar.setVisibility(View.GONE);
            }
        });
    }

    private void spinnerItemClicked(){

        userCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //userCountry.setSelection(position);
                if (position!=0){
                    countryId= GetCountry.id(position-1);
                    //Toast.makeText(SignUpActivity.this, "id =" + countryId, Toast.LENGTH_SHORT).show();
                    GetCity getCity = new GetCity(SignUpActivity.this);
                    getCity.getCity(countryId, userCity  , R.layout.spinner_item3);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        vendorCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //vendorCountry.setSelection(position);
                if (position!=0) {
                    countryId = GetCountry.id(position-1);
                    GetCity getCity = new GetCity(SignUpActivity.this);
                    getCity.getCity(countryId, vendorCity, R.layout.spinner_item3);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        vendorCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position!=0) {
                    cityId = GetCity.id(position - 1);

                }
               // Toast.makeText(SignUpActivity.this, "id =" + cityId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        userCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position!=0) {
                    cityId = GetCity.id(position-1);
                    //Toast.makeText(SignUpActivity.this, "id =" + cityId, Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(SignUpActivity.this, "id =" + cityId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);


        File finalFile ;
        try {
            Uri selectedImage = imageReturnedIntent.getData();
            if (selectedImage!=null) {
                finalFile = new File(getRealPathFromURI(selectedImage));
                uploadPhoto(finalFile, requestCode);
            }else {
                Toast.makeText(this, getResources().getString(R.string.no_select_image), Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){

        }


    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
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
                                                 Toast.makeText(SignUpActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();


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
                    Toast.makeText(SignUpActivity.this, "GET_ACCOUNTS Denied",
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

    private void getCategoryService(final RecyclerView recyclerView){
        ClientInterface vendor = ApiClient.getClient().create(ClientInterface.class);
        Call<List<CategoryService>> call = vendor.getCategoryService(categoryId);
        call.enqueue(new Callback<List<CategoryService>>() {
            @Override
            public void onResponse(Call<List<CategoryService>> call, Response<List<CategoryService>> response) {
                if (response.body()!=null){
                    categoryServices = response.body();
                    OnClickCategoryAdapter onClickCategoryAdapter = new OnClickCategoryAdapter(SignUpActivity.this , categoryServices);
                    recyclerView.setAdapter(onClickCategoryAdapter);
                    serviceesBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<CategoryService>> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                serviceesBar.setVisibility(View.GONE);
            }
        });
    }

    private void getUserTerms(final RecyclerView recyclerView){
        ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
        Call<List<TermsModel>> call = clientInterface.getUserTerms();
        call.enqueue(new Callback<List<TermsModel>>() {
            @Override
            public void onResponse(Call<List<TermsModel>> call, Response<List<TermsModel>> response) {
                if (response.body() != null) {
                    lists = response.body();
                    termsConditionAdapter = new TermsConditionAdapter(SignUpActivity.this, lists);
                    recyclerView.setAdapter(termsConditionAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<TermsModel>> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void getVendorTerms(final RecyclerView recyclerView){
        ClientInterface  clientInterface = ApiClient.getClient().create(ClientInterface.class);
        Call<List<TermsModel>> call = clientInterface.getClientTerms();
        call.enqueue(new Callback<List<TermsModel>>() {
            @Override
            public void onResponse(Call<List<TermsModel>> call, Response<List<TermsModel>> response) {
                if (response.body() != null) {
                    lists = response.body();
                    termsConditionAdapter = new TermsConditionAdapter(SignUpActivity.this, lists);
                    recyclerView.setAdapter(termsConditionAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<TermsModel>> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setUserData(){
        if (type!=null) {

            if (type.equals("facebook")) {
                GetUserData.facebookEmail(accessToken, userFirstName, userLastName, userEmail);
            } else if (type.equals("twitter")) {
                twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
                GetUserData.twitterEmail(twitterSession, userEmail , userFirstName );
            } else if (type.equals("google")) {
                GetUserData.googleEmail2(this, userFirstName , userEmail ,userLastName );
            } else if (type.equals("loginAsUser")) {
                //getUserById();
            }
        }
        }

    private void setUserData2(){
        if (type!=null) {

            if (type.equals("facebook")) {
                GetUserData.facebookEmail(accessToken, vendorName, userName, vendorEmail);
            } else if (type.equals("twitter")) {
                twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
                GetUserData.twitterEmail(twitterSession, vendorEmail , vendorName );
            } else if (type.equals("google")) {
                GetUserData.googleEmail2(this, vendorName ,vendorEmail , userName);
            } else if (type.equals("loginAsUser")) {
                //getUserById();
            }
        }
    }

    private int dayName(int pos){
        int Vdays = 0;
        switch (pos){
            case 1:
                Vdays = 0;
                break;
            case 2:
                Vdays = 7;
                break;
            case 3:
                Vdays = 1;
                break;
            case 4:
                Vdays = 2;
                break;
            case 5:
                Vdays = 3;
                break;
            case 6:
                Vdays = 4;
                break;
            case 7:
                Vdays = 5;
                break;
            case 8:
                Vdays = 6;
                break;
        }

        return Vdays;
    }

}
