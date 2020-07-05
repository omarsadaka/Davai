package com.technosaab.newdavai.Fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.technosaab.newdavai.Adapter.ReservationRecyclerAdapter;
import com.technosaab.newdavai.Models.ClientResponse;
import com.technosaab.newdavai.R;
import com.technosaab.newdavai.rest.ApiClient;
import com.technosaab.newdavai.rest.ClientInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationFragment extends Fragment implements View.OnClickListener {
    private RecyclerView recyclerView;
    private List<ClientResponse> clientList;
    private ReservationRecyclerAdapter reservationRecyclerAdapter;
    private ImageView filter;
    private TextView selectDate , selectTime;
    private LinearLayout filterLayout;
    private Spinner city , category;
    ArrayList<String> categoryList;
    ArrayList<String> cityList;
    private TextView myReservation , barTitle;
    Calendar myCalendar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = getLayoutInflater().inflate(R.layout.fragment_reservation, container, false);
        createView(view);
        createClicks();
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridlm = new GridLayoutManager(getContext(), 2);
        gridlm.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridlm);
        clientList = new ArrayList<>();
       // getAllClient();
        categoryList = new ArrayList<>();
        cityList = new ArrayList<>();
        categoryList.add(getResources().getString(R.string.category));
        cityList.add(getResources().getString(R.string.city));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, categoryList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter);

        ArrayAdapter<String> adapteer = new ArrayAdapter<>(getContext(), R.layout.spinner_item, cityList);
        adapteer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city.setAdapter(adapteer);
        return view;
    }
    public void createView(View view){
        recyclerView = view.findViewById(R.id.reservation_recycler);
        filter = view.findViewById(R.id.filter_icon);
        filterLayout = view.findViewById(R.id.filter_type_layout);
        city = view.findViewById(R.id.spinner_city);
        category = view.findViewById(R.id.spinner_category);
        myReservation = view.findViewById(R.id.my_reservation);
        barTitle = view.findViewById(R.id.bar_title);
        selectDate = view.findViewById(R.id.select_date);
        selectTime = view.findViewById(R.id.select_time);

    }
    private void createClicks(){
        filter.setOnClickListener(this);
        myReservation.setOnClickListener(this);
        selectDate.setOnClickListener(this);
        selectTime.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.filter_icon:
                filterLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.my_reservation:
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, new MyReservationVendorFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.select_date:
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

                DatePickerDialog datePickerDialog=    new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();

                 break;
            case R.id.select_time:
                myCalendar = Calendar.getInstance();
                int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = myCalendar.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        selectTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                break;
        }
    }
    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        selectDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void getAllClient(){
        ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
        Call<List<ClientResponse>> call = clientInterface.getAllClient();
        call.enqueue(new Callback<List<ClientResponse>>() {
            @Override
            public void onResponse(Call<List<ClientResponse>> call, Response<List<ClientResponse>> response) {
                if (response.body()!=null){
                    clientList = response.body();
                    reservationRecyclerAdapter = new ReservationRecyclerAdapter(clientList ,getContext() );
                    recyclerView.setAdapter(reservationRecyclerAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<ClientResponse>> call, Throwable t) {
                Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();

            }
        });
    }
}
