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
import com.technosaab.newdavai.Adapter.EditVendorServicesAdapter2;
import com.technosaab.newdavai.Adapter.VendorServicesAdapter;
import com.technosaab.newdavai.Models.BookingServiceResponse;
import com.technosaab.newdavai.Models.ClientServiceResponse2;
import com.technosaab.newdavai.Models.ServiceInfo;
import com.technosaab.newdavai.Models.UpdateBookingInfo;
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

public class EditVendorFragment extends Fragment implements View.OnClickListener {

    private TextView selectService , vendorName , date;
    private List<ClientServiceResponse2> servicesList;
    private ListView serviceList;
    List<String> strings;
    private ImageView vendorLogo , chat;
    private Button done , cancel , reserved;
    List<ServiceInfo> serviceInfos;
    private VendorServicesAdapter vendorServicesAdapter;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    TextView barTitle;
    TextView dateList , timeList;
    Calendar myCalendar;
    private String clientId;
    private String userId;
    private String bookingId;

    private String lang;
    private String categoryId;
    private List<BookingServiceResponse> bookingServiceResponses;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = getLayoutInflater().inflate(R.layout.fragment_edit_vendor, container, false);

        clientId = getArguments().getString("Client_Id");

        SharedPreferences Pref = getActivity().getSharedPreferences("BookingID", MODE_PRIVATE);
        String valueKey = Pref.getString("bookingId", null);
        if (valueKey != null) {
            bookingId = Pref.getString("bookingId", null);
        }
        SharedPreferences Prefs2 = getActivity().getSharedPreferences("UserId", MODE_PRIVATE);
        String value2 = Prefs2.getString("Id", null);
        if (value2 != null) {
            userId = Prefs2.getString("Id", null);
        }

        SharedPreferences Prefs = getActivity().getSharedPreferences("Lang", MODE_PRIVATE);
        String value = Prefs.getString("Local", null);
        if (value != null) {
            lang = Prefs.getString("Local", null);
        }
//        SharedPreferences Pref2 = getActivity().getSharedPreferences("CategoryId", MODE_PRIVATE);
//        String valueKey2 = Pref2.getString("categoryId", null);
//        if (valueKey2 != null) {
//            categoryId = Pref2.getString("categoryId", null);
//        }

        createView(view);
        createClick();
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
            Toast.makeText(getContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }else {
            getBookingService();
        }

        barTitle = getActivity().findViewById(R.id.bar_title);
        barTitle.setText(getString(R.string.edit_reservation));
        servicesList = new ArrayList<>();
        showDate();
        return view;
    }


    private void createView(View view){
        selectService = view.findViewById(R.id.select_service);
        dateList = view.findViewById(R.id.date);
        timeList = view.findViewById(R.id.time);
        vendorName = view.findViewById(R.id.vendor_name);
        date = view.findViewById(R.id.date_text);
        vendorLogo = view.findViewById(R.id.vendor_logo);
        serviceList = view.findViewById(R.id.list);
        done = view.findViewById(R.id.done);
        cancel = view.findViewById(R.id.cancelled_btn);
        reserved = view.findViewById(R.id.reserved_btn);
        chat = view.findViewById(R.id.support_icon);

    }

    private void createClick(){
        selectService.setOnClickListener(this);
        timeList.setOnClickListener(this);
        done.setOnClickListener(this);
        cancel.setOnClickListener(this);
        reserved.setOnClickListener(this);
        chat.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
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
                       dialog.dismiss();
                    }
                });
                break;
            case R.id.done:
                if (dateList.getText().toString().equals(getString(R.string.date))){
                    Toast.makeText(getContext(), getResources().getString(R.string.select_date_first), Toast.LENGTH_LONG).show();
                }else{
                     cm = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
                    if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
                        Toast.makeText(getContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                    }else {
                        updateReserve();
                    }
                }

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
            case R.id.support_icon:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, new LiveChatFragment());
                //hbd
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
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
                    servicesList = response.body();
                    EditVendorServicesAdapter2 editVenderServicesAdapter2 = new EditVendorServicesAdapter2(getContext() , servicesList ,textView , strings);
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

    private void getBookingService(){
        ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
        Call<List<BookingServiceResponse>> call = clientInterface.getBookingService(bookingId);
        call.enqueue(new Callback<List<BookingServiceResponse>>() {
            @Override
            public void onResponse(Call<List<BookingServiceResponse>> call, Response<List<BookingServiceResponse>> response) {
                if (response.body() != null) {
                    bookingServiceResponses = new ArrayList<>();
                    bookingServiceResponses = response.body();
                        try {
                            String dateTime = response.body().get(0).getBookingID().getDateTime();
                            String brandName = response.body().get(0).getBookingID().getClientID().getBrandName();
                            String brandLogo = response.body().get(0).getBookingID().getClientID().getLogo();
                            date.setText(dateTime.substring(0,dateTime.indexOf("T")));
                            //date.setText(dateTime);
                            vendorName.setText(brandName);
                            Picasso.get().load(brandLogo).into(vendorLogo);
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
                                    (Objects.requireNonNull(getContext()), android.R.layout.simple_list_item_1, strings)

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
                        }catch (Exception e){

                        }


            }
            }
            @Override
            public void onFailure(Call<List<BookingServiceResponse>> call, Throwable t) {
                Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void updateReserve(){
        String dateTime = dateList.getText().toString();//+","+selectTime.getText().toString();
        UpdateBookingInfo bookingInfo = new UpdateBookingInfo(bookingId , dateTime  , serviceInfos );
        ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
        Call<ResponseBody> call = clientInterface.updateBooking(bookingInfo);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body()!=null) {
                    Toast.makeText(getContext(), "Successfully UpdatingBooking , Thank You", Toast.LENGTH_LONG).show();
                    serviceInfos.clear();
                    dateList.setText(getString(R.string.date));
                    timeList.setText(getResources().getString(R.string.time));
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
        Call<ResponseBody> call = clientInterface.deleteBooking(bookingId , userId);
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

}
