package com.technosaab.newdavai.Fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.technosaab.newdavai.Adapter.VendorServicesAdapter;
import com.technosaab.newdavai.Models.BookingInfo;
import com.technosaab.newdavai.Models.ClientServiceResponse2;
import com.technosaab.newdavai.Models.ServiceInfo;
import com.technosaab.newdavai.Models.UserByIdResponse;
import com.technosaab.newdavai.Models.Vendor;
import com.technosaab.newdavai.R;
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

public class VendorServicesFragment extends Fragment implements View.OnClickListener {
    private RecyclerView recyclerView;
    private List<ClientServiceResponse2> servicesList;
    private VendorServicesAdapter vendorServicesAdapter;
    private TextView myReservation , selectDate , selectTime , vendorName , userName;
    private Calendar myCalendar;
    private String clientId;
    private String userId;
    private Button reserve;
    private List<ServiceInfo> serviceInfos;
    private ProgressDialog dialog;
    private EditText search;
    private Vendor vendorList;
    private int startTime , endTime , offDay =0;
    private ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = getLayoutInflater().inflate(R.layout.fragment_vendor_services, container, false);
        createView(view);
        createClicks();
        clientId = getArguments().getString("Client_Id");
        SharedPreferences Prefs2 = getActivity().getSharedPreferences("UserId", MODE_PRIVATE);
        String value2 = Prefs2.getString("Id", null);
        if (value2 != null) {
            userId = Prefs2.getString("Id", null);
        }
        dialog = new ProgressDialog(getContext());
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        servicesList = new ArrayList<>();
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
            Toast.makeText(getContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

        }else {
            getUserById();
            getClientService();
            getAboutVendor();
        }
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s!=null||!s.toString().isEmpty())
                    vendorServicesAdapter.getFilter().filter(s.toString());
            }
        });

        return view;
    }

 private void createView(View view){
     recyclerView = view.findViewById(R.id.vendor_service_recycler);
     myReservation = view.findViewById(R.id.my_reservation);
     selectDate = view.findViewById(R.id.selectDate);
     selectTime = view.findViewById(R.id.selectTime);
     vendorName = view.findViewById(R.id.vendor_name);
     reserve = view.findViewById(R.id.reserve_button);
     userName = view.findViewById(R.id.user_name);
     search = view.findViewById(R.id.reserv_search);
     progressBar = view.findViewById(R.id.services_bar);
 }
 private void createClicks(){
        myReservation.setOnClickListener(this);
        selectDate.setOnClickListener(this);
        selectTime.setOnClickListener(this);
        reserve.setOnClickListener(this);

 }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.my_reservation:
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, new MyReservationVendorFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.selectDate:
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

                DatePickerDialog datePickerDialog=     new DatePickerDialog(getContext(), R.style.DialogTheme ,date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
                break;
            case R.id.selectTime:
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
                break;
            case R.id.reserve_button:
                serviceInfos = VendorServicesAdapter.getServiceModels();
                if (serviceInfos.size()<1){
                    Toast.makeText(getContext(), getResources().getString(R.string.check_service), Toast.LENGTH_LONG).show();
                }else {

                    if (selectDate.getText().toString().equals(getResources().getString(R.string.select_date))) {
                        Toast.makeText(getContext(), getResources().getString(R.string.select_date_first), Toast.LENGTH_SHORT).show();
                    } else {

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
                        }else {
                            dialog.setMessage("Please Wait...");
                            dialog.show();
                            reserveService(serviceInfos);
                        }
                    }
                }
                break;
        }
    }
    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
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
                    vendorServicesAdapter = new VendorServicesAdapter(getContext() , servicesList , vendorName);
                    recyclerView.setAdapter(vendorServicesAdapter);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<ClientServiceResponse2>> call, Throwable t) {
                Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void reserveService(List<ServiceInfo> infos){
          if(infos.size()>=1){
        String dateTime = selectDate.getText().toString();
        BookingInfo bookingInfo = new BookingInfo(clientId , dateTime , userId , infos );
        ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
        Call<ResponseBody> call = clientInterface.booking(bookingInfo);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body()!=null) {
                    Toast.makeText(getContext(), R.string.done_reserve, Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    serviceInfos.clear();
                    getClientService();
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


        }else {
    dialog.dismiss();

}
    }

    public void getUserById(){
        ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
        Call<UserByIdResponse> call = clientInterface.getUserById(userId);
        call.enqueue(new Callback<UserByIdResponse>() {
            @Override
            public void onResponse(Call<UserByIdResponse> call, Response<UserByIdResponse> response) {
                if (response.body()!=null){
                    userName.setText(response.body().getFirstName());
                }
            }

            @Override
            public void onFailure(Call<UserByIdResponse> call, Throwable t) {
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
}
