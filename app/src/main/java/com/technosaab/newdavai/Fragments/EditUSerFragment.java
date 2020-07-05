package com.technosaab.newdavai.Fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.technosaab.newdavai.Adapter.ClientReservationAdpter;
import com.technosaab.newdavai.Adapter.EditUserRecyclerAdapter;
import com.technosaab.newdavai.Adapter.EditVendorServicesAdapter2;
import com.technosaab.newdavai.Models.BookingServiceResponse;
import com.technosaab.newdavai.Models.ClientBooking;
import com.technosaab.newdavai.Models.ClientServiceResponse2;
import com.technosaab.newdavai.Models.ServiceInfo;
import com.technosaab.newdavai.Models.UpdateBookingInfo;
import com.technosaab.newdavai.Models.UserByIdResponse;
import com.technosaab.newdavai.R;
import com.technosaab.newdavai.Models.MyReservation;
import com.technosaab.newdavai.Util.ImageConverter;
import com.technosaab.newdavai.rest.ApiClient;
import com.technosaab.newdavai.rest.ClientInterface;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;


public class EditUSerFragment extends Fragment implements View.OnClickListener {
    private RecyclerView recyclerView;
    private List<MyReservation> myReservationList;
    private EditUserRecyclerAdapter editUserRecyclerAdapter;
    TextView barTitle;
    private String clientId;
    private String bookingId;
    private String userId;
    private String lang;
    private List<BookingServiceResponse> servicesList;
    private List<ClientServiceResponse2> services;
    private ListView serviceList;
    List<String> strings;
    private TextView date , userName;
    private Button done , cancel , reserved;
    private ImageView chat , user_image;
    private TextView select_service;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    List<ServiceInfo> serviceInfos;
    TextView dateList , timeList;
    Calendar myCalendar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = getLayoutInflater().inflate(R.layout.fragment_edit_u, container, false);

        SharedPreferences Prefs2 = getActivity().getSharedPreferences("UserId", MODE_PRIVATE);
        String value2 = Prefs2.getString("Id", null);
        if (value2 != null) {
            clientId = Prefs2.getString("Id", null);
        }
        SharedPreferences Prefs = getActivity().getSharedPreferences("Lang", MODE_PRIVATE);
        String value = Prefs.getString("Local", null);
        if (value != null) {
            lang = Prefs.getString("Local", null);
        }
        SharedPreferences Pref = getActivity().getSharedPreferences("BookingID", MODE_PRIVATE);
        String valueKey = Pref.getString("bookingId", null);
        if (valueKey != null) {
            bookingId = Pref.getString("bookingId", null);
            userId = Pref.getString("USERID", null);
        }
        createView(view);
        servicesList = new ArrayList<>();
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setNestedScrollingEnabled(false);
//        editUserRecyclerAdapter = new EditUserRecyclerAdapter( getContext() , myReservationList);
//        recyclerView.setAdapter(editUserRecyclerAdapter);
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
            Toast.makeText(getContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }else {
            getClientServiceName();
            getUserById();
        }
        barTitle.setText(getString(R.string.edit_reservation));
        showDate();
        return view;
    }

    private void createView(View view){
       // recyclerView = view.findViewById(R.id.user_edit_recycler);
        barTitle = getActivity().findViewById(R.id.bar_title);
        serviceList = view.findViewById(R.id.list);
        date = view.findViewById(R.id.date_text);
        userName = view.findViewById(R.id.user_name);
        chat = view.findViewById(R.id.chat_icon);
        select_service = view.findViewById(R.id.select_service);
        cancel = view.findViewById(R.id.cancelled_btn);
        reserved = view.findViewById(R.id.reserved_btn);
        chat.setOnClickListener(this);
        select_service.setOnClickListener(this);
        cancel.setOnClickListener(this);
        reserved.setOnClickListener(this);
        user_image = view.findViewById(R.id.user_image);
        dateList = view.findViewById(R.id.date);
        timeList = view.findViewById(R.id.time);
        timeList.setOnClickListener(this);
    }
    private void getClientServiceName(){

        ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
        Call<List<BookingServiceResponse>> call = clientInterface.getBookingService(bookingId);
        call.enqueue(new Callback<List<BookingServiceResponse>>() {
            @Override
            public void onResponse(Call<List<BookingServiceResponse>> call, Response<List<BookingServiceResponse>> response) {
                if (response.body()!=null){
                    servicesList = response.body();
                    date.setText(response.body().get(0).getBookingID().getDateTime().substring(0 ,response.body().get(0).getBookingID().getDateTime().indexOf("T")));
                    userName.setText(response.body().get(0).getBookingID().getUserID().getFirstName());
                    strings = new ArrayList<>();
                    for (int i = 0; i < response.body().size(); i++) {
                        if (lang.equals("english")){
                            String serviceName = response.body().get(i).getServicesID().getTitleEN();
                            strings.add(serviceName);
                        }else if (lang.equals("ar")){
                            String serviceName = response.body().get(i).getServicesID().getTitleAr();
                            strings.add(serviceName);
                        }

                    }

                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                            (getContext(), android.R.layout.simple_list_item_1, strings)

                    {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            // Get the current item from ListView
                            View view = super.getView(position, convertView, parent);

                            // Get the Layout Parameters for ListView Current Item View
                            ViewGroup.LayoutParams params = view.getLayoutParams();

                            // Set the height of the Item View
                            params.height = 80;
                            view.setLayoutParams(params);
                            return view;
                        }
                    };
                    // DataBind ListView with items from ArrayAdapter
                    serviceList.setAdapter(arrayAdapter);

                }
            }

            @Override
            public void onFailure(Call<List<BookingServiceResponse>> call, Throwable t) {
                Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                Log.d("Error" , t.toString());
            }
        });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.chat_icon:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, new EditChatFragment());
                //hbd
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.time:
                showTime();
                break;
            case R.id.select_service:
                dialogBuilder = new AlertDialog.Builder(getContext());
                View views = getLayoutInflater().inflate(R.layout.onclick_category_popup , null);
                ImageView closee = views.findViewById(R.id.onclick_popup_close);
                TextView service = views.findViewById(R.id.onclick_category);
                Button done = views.findViewById(R.id.category_btn_done);
                RecyclerView recyclerView = views.findViewById(R.id.onclick_category_recycler);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
                if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
                    Toast.makeText(getContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }else {
                    getClientService(recyclerView , service);
                }

                dialogBuilder.setView(views);
                dialog = dialogBuilder.create();
                dialog.show();
                closee.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        serviceInfos = EditVendorServicesAdapter2.getServiceModels();
                        // Toast.makeText(getContext(), ""+serviceInfos.size(), Toast.LENGTH_SHORT).show();

                        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
                            if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
                                Toast.makeText(getContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                            }else {
                                clientUpdateReserve();
                            }


                    }
                });
                break;
            case R.id.cancelled_btn:
                cancel.setBackground(getResources().getDrawable(R.drawable.pressed_top_buttob_bg));
                reserved.setBackground(getResources().getDrawable(R.drawable.tv_bg));
                cancel.setTextColor(getResources().getColor(R.color.colorWhite));
                reserved.setTextColor(getResources().getColor(R.color.colorBrown));

                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setMessage(getResources().getString(R.string.delete_reservation));
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
                                if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
                                    Toast.makeText(getContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                                }else {
                                    deleteReservation();
                                }
                            }
                        });

                builder1.setNegativeButton(
                        getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
                break;
            case R.id.reserved_btn:
                cancel.setBackground(getResources().getDrawable(R.drawable.tv_bg));
                reserved.setBackground(getResources().getDrawable(R.drawable.pressed_top_buttob_bg));
                cancel.setTextColor(getResources().getColor(R.color.colorBrown));
                reserved.setTextColor(getResources().getColor(R.color.colorWhite));
                break;
        }
    }

    private void getClientService(final RecyclerView recyclerView , final TextView textView){
        ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
        Call<List<ClientServiceResponse2>> call = clientInterface.getClientService(clientId);
        call.enqueue(new Callback<List<ClientServiceResponse2>>() {
            @Override
            public void onResponse(Call<List<ClientServiceResponse2>> call, Response<List<ClientServiceResponse2>> response) {
                if (response.body()!=null){
                    services = new ArrayList<>();
                    services = response.body();
                    EditVendorServicesAdapter2 editVenderServicesAdapter2 = new EditVendorServicesAdapter2(getContext() , services ,textView , strings);
                    recyclerView.setAdapter(editVenderServicesAdapter2);
                }
            }

            @Override
            public void onFailure(Call<List<ClientServiceResponse2>> call, Throwable t) {
                Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                Log.d("Error" , t.toString());
            }
        });
    }

    private void clientUpdateReserve(){
        String dateTime = dateList.getText().toString();//+","+selectTime.getText().toString();
        UpdateBookingInfo bookingInfo = new UpdateBookingInfo(bookingId , dateTime , serviceInfos );
        ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
        Call<ResponseBody> call = clientInterface.updateClientBooking(bookingInfo);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body()!=null) {
                    Toast.makeText(getContext(), "Successfully UpdatingBooking , Thank You", Toast.LENGTH_LONG).show();
                    serviceInfos.clear();
                    dialog.dismiss();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame, new MyReservationVendorFragment());
                    //hbd
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void deleteReservation(){
        ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
        Call<ResponseBody> call = clientInterface.clientCancelBooking(bookingId , clientId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body()==null){
                    Toast.makeText(getContext(), getResources().getString(R.string.deleleted), Toast.LENGTH_LONG).show();

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame, new MyReservationVendorFragment());
                    //hbd
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void getUserById(){
        ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
        Call<UserByIdResponse> call = clientInterface.getUserById(userId);
        call.enqueue(new Callback<UserByIdResponse>() {
            @Override
            public void onResponse(Call<UserByIdResponse> call, Response<UserByIdResponse> response) {
                if (response.body()!=null){
                    String img = response.body().getPersonalImg();
                    if (img!=null) {
                        Picasso.get().load(response.body().getPersonalImg()).transform(new ImageConverter()).into(user_image);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserByIdResponse> call, Throwable t) {
                Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
            }
        });
    }


    public void showDate(){
        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };


        dateList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog datePickerDialog= new DatePickerDialog(getContext(),R.style.DialogTheme , date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
    }
    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateList.setText(sdf.format(myCalendar.getTime()));
    }
    public void showTime(){

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getContext(), R.style.DialogTheme ,new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                timeList.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }
}
