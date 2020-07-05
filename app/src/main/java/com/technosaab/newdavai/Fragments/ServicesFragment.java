package com.technosaab.newdavai.Fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.squareup.picasso.Picasso;
import com.technosaab.newdavai.Activities.MainActivity;
import com.technosaab.newdavai.Adapter.HomeDetailRecyclerAdapter;
import com.technosaab.newdavai.Adapter.ServicesRecyclerAdapter;
import com.technosaab.newdavai.Helper.GetCountry;
import com.technosaab.newdavai.Models.AddFavResponse;
import com.technosaab.newdavai.Models.AddFavourite;
import com.technosaab.newdavai.Models.BookingInfo;
import com.technosaab.newdavai.Models.CategoryClientResponse;
import com.technosaab.newdavai.Models.CategoryID;
import com.technosaab.newdavai.Models.CityID;
import com.technosaab.newdavai.Models.ClientServiceResponse2;
import com.technosaab.newdavai.Models.CountryID;
import com.technosaab.newdavai.Models.NumRes;
import com.technosaab.newdavai.Models.Ratting;
import com.technosaab.newdavai.Models.ServiceInfo;
import com.technosaab.newdavai.Models.Vendor;
import com.technosaab.newdavai.R;
import com.technosaab.newdavai.rest.ApiClient;
import com.technosaab.newdavai.rest.ClientInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;


public class ServicesFragment extends Fragment implements View.OnClickListener {
    private RecyclerView recyclerView;
    private List<ClientServiceResponse2> servicesList;
    private ServicesRecyclerAdapter servicesRecyclerAdapter;
    private Button reserve;
    private AlertDialog.Builder dialogBulder;
    private AlertDialog dialog;
    private TextView selectDate , selectTime;
    private TextView brandName , star_text , num_fav ;
    private ImageView logo , cover , star_logo , heart_logo , client_rat;
    private Calendar myCalendar;
    private ArrayList<String> employee;
    private String clientId;
    private String userId;
    private List<ServiceInfo> serviceInfos;
    private Vendor vendorList;
    private int startTime , endTime;
    private int offDay =0;
    private ProgressBar progressBar;
    private String userType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = getLayoutInflater().inflate(R.layout.fragment_services, container, false);

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
        SharedPreferences prefs = getActivity().getSharedPreferences("checkUser" , MODE_PRIVATE);
        String type = prefs.getString("userType" , null);
        if (type != null){
            userType = prefs.getString("userType" , null);
        }
        createView(view);
        createClicks();
        servicesList = new ArrayList<>();
        progressBar .setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
            Toast.makeText(getContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

        }else {
            getClientService();
            getClientRate();
            getAboutVendor();
            getNumFavourite();
        }
        return view;
    }
   private void createView(View view){
       recyclerView = view.findViewById(R.id.services_recycler);
       reserve = view.findViewById(R.id.reserve);
       brandName = view.findViewById(R.id.brand_name);
       cover = view.findViewById(R.id.cover);
       logo = view.findViewById(R.id.logo_image);
       star_text = view.findViewById(R.id.star_text);
       star_logo = view.findViewById(R.id.star_logo);
       star_logo.setOnClickListener(this);
       heart_logo = view.findViewById(R.id.heart_logo);
       heart_logo.setOnClickListener(this);
       num_fav = view.findViewById(R.id.num_fav);
       progressBar = view.findViewById(R.id.service_bar);
       client_rat = view.findViewById(R.id.client_rat);
       client_rat.setOnClickListener(this);
   }
   private void createClicks(){
        reserve.setOnClickListener(this);
   }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.reserve:
                if (userType.equals("1")) {
                    serviceInfos = ServicesRecyclerAdapter.getServiceModels();
                    //Toast.makeText(getContext(), ""+serviceInfos.size(), Toast.LENGTH_LONG).show();
                    if (serviceInfos.size() >= 1) {
                        createPopup();
                    }
                }else {
                    reserve.setEnabled(false);
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
            case R.id.client_rat:
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, new ClientRateFragment());
                //hbd
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.heart_logo:
                if (userType.equals("1")) {
                    if (heart_logo.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.favourite).getConstantState()) {
                        ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
                        Call<ResponseBody> call = clientInterface.deleteFavourite(userId, clientId);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.body() != null) {
                                    Toast.makeText(getContext(), getResources().getString(R.string.delete_favourite), Toast.LENGTH_LONG).show();
                                    heart_logo.setImageDrawable(getResources().getDrawable(R.drawable.h_heart));

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
                                    heart_logo.setImageDrawable(getResources().getDrawable(R.drawable.favourite));

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
        }
    }
    private void createPopup(){

        dialogBulder = new AlertDialog.Builder(getContext());
                    View view = LayoutInflater.from(getContext()).inflate(R.layout.popup_services , null);
                    ImageView close = view.findViewById(R.id.close);
                    //Spinner selectEmployee = view.findViewById(R.id.spinner);
                     final Button Reserve = view.findViewById(R.id.reserve_btn);
                     selectDate = view.findViewById(R.id.select_date);
                     selectTime = view.findViewById(R.id.select_time);

                    myCalendar = Calendar.getInstance();
                    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth ) {
                            // TODO Auto-generated method stub
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            updateLabel();
                        }

                    };
                    selectDate.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.DialogTheme ,date, myCalendar
                                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                    myCalendar.get(Calendar.DAY_OF_MONTH));
                            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                            datePickerDialog.show();
                        }
                    });

                    selectTime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myCalendar = Calendar.getInstance();
                            int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                            int minute = myCalendar.get(Calendar.MINUTE);
                            TimePickerDialog mTimePicker;
                            mTimePicker = new TimePickerDialog(getContext(), R.style.DialogTheme ,new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                    selectTime.setText( selectedHour + ":" + selectedMinute);
                                }
                            }, hour, minute, true);//Yes 24 hour time
                            mTimePicker.setTitle("Select Time");
                            mTimePicker.show();

                            Reserve.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    // todo check data and time
                                    String dateTime = selectDate.getText().toString();//+","+selectTime.getText().toString();

                                    //Calendar c = Calendar.getInstance();
                                    //int dayOfWeek = myCalendar.get(Calendar.DAY_OF_MONTH);
                                    if (Calendar.MONDAY == offDay) {
                                        Toast.makeText(getContext(), getResources().getString(R.string.wrong_day), Toast.LENGTH_SHORT).show();
                                    }else if (Calendar.TUESDAY == offDay) {
                                        Toast.makeText(getContext(), getResources().getString(R.string.wrong_day), Toast.LENGTH_SHORT).show();
                                    } else if (Calendar.WEDNESDAY == offDay) {
                                        Toast.makeText(getContext(), getResources().getString(R.string.wrong_day), Toast.LENGTH_SHORT).show();
                                    } else if (Calendar.THURSDAY == offDay) {
                                        Toast.makeText(getContext(), getResources().getString(R.string.wrong_day), Toast.LENGTH_SHORT).show();
                                    } else if (Calendar.FRIDAY == offDay) {
                                        Toast.makeText(getContext(), getResources().getString(R.string.wrong_day), Toast.LENGTH_SHORT).show();
                                    } else if (Calendar.SATURDAY == offDay) {
                                        Toast.makeText(getContext(), getResources().getString(R.string.wrong_day), Toast.LENGTH_SHORT).show();
                                    } else if (Calendar.SUNDAY == offDay) {
                                        Toast.makeText(getContext(), getResources().getString(R.string.wrong_day), Toast.LENGTH_SHORT).show();
                                    }else booking(dateTime);


                                   //booking(dateTime);

                                    //Toast.makeText(getContext(), getResources().getString(R.string.wrong_day), Toast.LENGTH_SHORT).show();

                                }
                            });

                        }
                    });
//        employee = new ArrayList<>();
//        employee.add(getResources().getString(R.string.select_employee));
//        employee.add("Mohamed Samy");
//        employee.add("Omar");
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, employee);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        selectEmployee.setAdapter(adapter);
        dialogBulder.setView(view);
                    dialog = dialogBulder.create();
                    dialog.show();
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
    }

    private void updateLabel() {
        //String myFormat = "dd-MM-yyyy";
        String myFormat = "yyyy-MM-dd";//In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        selectDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void getClientService(){
        ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
        Call<List<ClientServiceResponse2>> call = clientInterface.getClientService(clientId);
        call.enqueue(new Callback<List<ClientServiceResponse2>>() {
            @Override
            public void onResponse(Call<List<ClientServiceResponse2>> call, Response<List<ClientServiceResponse2>> response) {
                if (response.body()!=null){
                    servicesList = response.body();
                    servicesRecyclerAdapter = new ServicesRecyclerAdapter(servicesList , getContext() , brandName , logo , cover);
                    recyclerView.setAdapter(servicesRecyclerAdapter);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<ClientServiceResponse2>> call, Throwable t) {
                Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                Log.d("Error" , t.toString());
            }
        });
    }

    private void getClientRate(){
        ClientInterface aboutVendor = ApiClient.getClient().create(ClientInterface.class);
        Call<Vendor> call = aboutVendor.getVendorAbout(clientId);
        call.enqueue(new Callback<Vendor>() {
            @Override
            public void onResponse(@NonNull Call<Vendor> call, @NonNull Response<Vendor> response) {
                if (response.body()!=null){
                    vendorList = new Vendor();
                    star_text.setText(response.body().getTotalRate());

                }
            }
            @Override
            public void onFailure(@NonNull Call<Vendor> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getAboutVendor(){
        ClientInterface aboutVendor = ApiClient.getClient().create(ClientInterface.class);
        Call<Vendor> call = aboutVendor.GetVendorAbout(clientId , userId);
        call.enqueue(new Callback<Vendor>() {
            @Override
            public void onResponse(@NonNull Call<Vendor> call, @NonNull Response<Vendor> response) {
                if (response.body()!=null){
                    vendorList = response.body();
                    int heart = response.body().getV();
                    if (heart == 2){
                        try {
                            heart_logo.setImageDrawable(getResources().getDrawable(R.drawable.favourite));
                        }catch (Exception e){

                        }

                    }
                    //todo temporarily
//                    startTime = response.body().getFrom();
//                    endTime = response.body().getTo();
//                    offDay = response.body().getOffDay();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Vendor> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
            }
        });
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

    private void booking(String date){
        BookingInfo bookingInfo = new BookingInfo(clientId , date , userId , serviceInfos );
        ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
        Call<ResponseBody> call = clientInterface.booking(bookingInfo);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body()!=null) {
                    Toast.makeText(getContext(), R.string.done_reserve, Toast.LENGTH_LONG).show();
                    serviceInfos.clear();
                    getClientService();
                    dialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
            }
        });
    }

}
