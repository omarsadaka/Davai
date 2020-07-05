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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.technosaab.newdavai.Adapter.VendorRecyclerAdapter;
import com.technosaab.newdavai.Helper.GetCity2;
import com.technosaab.newdavai.Models.CategoryResponse;
import com.technosaab.newdavai.Models.ClientResponse;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;


public class VendorFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private List<ClientResponse> clientList;
    private VendorRecyclerAdapter vendorRecyclerAdapter;
    private ImageView filter;
    private Button reserve;
    private EditText search;
    private LinearLayout filterLayout;
    private Spinner sp_city, sp_category;
    private String userType;
    List<String> categoryList;
//    ArrayList<String> cityList;
    private TextView myReservation , selectDate , selectTime , userName;
    Calendar myCalendar;
    private String userId;
    private List<CategoryResponse> categoriesList;
    private String lang;
    boolean flag=true;
    String st_query="";
    private ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = getLayoutInflater().inflate(R.layout.fragment_vendor, container, false);
        SharedPreferences Prefs = getActivity().getSharedPreferences("UserId", MODE_PRIVATE);
        String value2 = Prefs.getString("Id", null);
        if (value2 != null) {
            userId = Prefs.getString("Id", null);
        }
        SharedPreferences Pref = getActivity().getSharedPreferences("checkUser", MODE_PRIVATE);
        String type = Pref.getString("userType", null);
        if (type != null) {
            userType = Pref.getString("userType", null);
        }
        SharedPreferences mPref = getActivity().getSharedPreferences("Lang", MODE_PRIVATE);
        String value = mPref.getString("Local", null);
        if (value != null) {
            lang = mPref.getString("Local", null);
        }

        createView(view);
        createClicks();
        //filter.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridlm = new GridLayoutManager(getContext(), 2);
        gridlm.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridlm);
        clientList = new ArrayList<>();
        categoriesList = new ArrayList<>();
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
            Toast.makeText(getContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

        }else {
            getAllClient();
            getUserById();
            getAllCategories();
        }


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(s!=null||!s.toString().isEmpty())
                {
                    String query=s.toString();
                    String cat_id="";
                    String city_id="";
                    st_query=query;
                    int pos=sp_city.getSelectedItemPosition();
                    if (pos>0) {
                        city_id = GetCity2.id(pos-1);
                        query=city_id+","+query;
                    }else query="0"+","+query;
                    int pos2=sp_category.getSelectedItemPosition();
                    if (pos2>0) {
                        cat_id = categoriesList.get(pos2-1).getId();
                        query=cat_id+","+query;
                    }else query="0"+","+query;
                    if (vendorRecyclerAdapter!=null)
                        vendorRecyclerAdapter.getFilter().filter(query);
                }

            }
        });
        sp_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                    String query = "";
                    String cat_id = "",city_id="";
                    int pos=sp_city.getSelectedItemPosition();
                    if (pos>0) {
                        city_id = GetCity2.id(pos-1);
                        query=city_id+","+st_query;
                    }else query="0"+","+st_query;
                    int pos2=sp_category.getSelectedItemPosition();
                    if (pos2>0) {
                        cat_id = categoriesList.get(pos2-1).getId();
                        query=cat_id+","+query;
                    }else query="0"+","+query;
                    if (vendorRecyclerAdapter!=null)
                        vendorRecyclerAdapter.getFilter().filter(query);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                    String query = "";
                    String cat_id = "",city_id="";
                    int pos=sp_city.getSelectedItemPosition();
                    if (pos>0) {
                        city_id = GetCity2.id(pos-1);
                        query=city_id+","+st_query;
                    }else query="0"+","+st_query;
                    int pos2=sp_category.getSelectedItemPosition();
                    if (pos2>0) {
                        cat_id = categoriesList.get(pos2-1).getId();
                        query=cat_id+","+query;
                    }else query="0"+","+query;
                    if (vendorRecyclerAdapter!=null)
                        vendorRecyclerAdapter.getFilter().filter(query);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }
    public void createView(View view){
        recyclerView = view.findViewById(R.id.reservation_vendor_recycler);
        filter = view.findViewById(R.id.filter_image);
        filterLayout = view.findViewById(R.id.filter_type);
        sp_city = view.findViewById(R.id.city_spinner);
        sp_category = view.findViewById(R.id.categorySpinner);
        myReservation = view.findViewById(R.id.my_reservation);
        selectDate = view.findViewById(R.id.date);
        selectTime = view.findViewById(R.id.time);
        userName = view.findViewById(R.id.user_name);
        reserve = view.findViewById(R.id.reserve);
        search = view.findViewById(R.id.reserv_search);
        progressBar = view.findViewById(R.id.reservation_bar);

    }
    private void createClicks(){
        myReservation.setOnClickListener(this);
        filter.setOnClickListener(this);
        selectDate.setOnClickListener(this);
        selectTime.setOnClickListener(this);
        reserve.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.filter_image:

                if (flag)filterLayout.setVisibility(View.VISIBLE);
                else filterLayout.setVisibility(View.GONE);
                flag=!flag;


//                if (flag)
//                filterLayout.setVisibility(View.GONE);
//                else
//                {
//                    filterLayout.setVisibility(View.VISIBLE);
//                    flag=true;
//                }
                break;
            case R.id.my_reservation:
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, new MyReservationVendorFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.date:
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
//                new DatePickerDialog(getContext(), date, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.DialogTheme ,date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
                break;
            case R.id.time:
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
            case R.id.reserve:
                Toast.makeText(getContext(), getResources().getString(R.string.select_vendor_first), Toast.LENGTH_LONG).show();
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
                    vendorRecyclerAdapter = new VendorRecyclerAdapter( getContext(),clientList );
                    recyclerView.setAdapter(vendorRecyclerAdapter);
                    progressBar.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<List<ClientResponse>> call, Throwable t) {
                Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void getUserById(){
//        ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
//        Call<UserByIdResponse> call = clientInterface.getUserById(userId);
//        call.enqueue(new Callback<UserByIdResponse>() {
//            @Override
//            public void onResponse(Call<UserByIdResponse> call, Response<UserByIdResponse> response) {
//                if (response.body()!=null){
//                    userName.setText(response.body().getFirstName());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserByIdResponse> call, Throwable t) {
//                Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
//            }
//        });


        if (userType.equals("1")){
            ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
            Call<UserByIdResponse> call = clientInterface.getUserById(userId);
            call.enqueue(new Callback<UserByIdResponse>() {
                @Override
                public void onResponse(Call<UserByIdResponse> call, Response<UserByIdResponse> response) {
                    if (response.body()!=null){
                        userName.setText(response.body().getFirstName());
                        String countryId  = response.body().getCountryID().getId();
                        GetCity2 getCity = new GetCity2(getContext());
                        getCity.getCity(countryId, sp_city, R.layout.spinner_item , getContext());
                    }
                }

                @Override
                public void onFailure(Call<UserByIdResponse> call, Throwable t) {
                    Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                }
            });
        }else if (userType.equals("2")){
            ClientInterface aboutVendor = ApiClient.getClient().create(ClientInterface.class);
            Call<Vendor> call = aboutVendor.getVendorAbout(userId);
            call.enqueue(new Callback<Vendor>() {
                @Override
                public void onResponse(@NonNull Call<Vendor> call, @NonNull Response<Vendor> response) {
                    if (response.body()!=null){
                        //userName.setText(response.body().getFirstName());
                        String countryId  = response.body().getCountryID().getId();
                        GetCity2 getCity = new GetCity2(getContext());
                        getCity.getCity(countryId, sp_city, R.layout.spinner_item , getContext());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Vendor> call, @NonNull Throwable t) {
                    Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                }
            });
        }


    }

    private void getAllCategories(){
        ClientInterface home = ApiClient.getClient().create(ClientInterface.class);
        Call<List<CategoryResponse>> call = home.getAllCategories();
        call.enqueue(new Callback<List<CategoryResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<CategoryResponse>> call, @NonNull Response<List<CategoryResponse>> response) {
                if (response.body()!=null){
                    categoriesList = response.body();
                    categoryList  = new ArrayList<>();
                    categoryList.add(getContext().getResources().getString(R.string.all_categoyr));
                    for (CategoryResponse categoryResponse:categoriesList
                         ) {

                        if (lang.equals("english")) {
                            categoryList.add(categoryResponse.getTitleEN());
                        } else if (lang.equals("ar")) {
                            categoryList.add(categoryResponse.getTitleAr());
                        }

                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, categoryList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_category.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CategoryResponse>> call, @NonNull Throwable t) {
                try {
                    Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                }catch (Exception e){}
            }
        });
    }
}
