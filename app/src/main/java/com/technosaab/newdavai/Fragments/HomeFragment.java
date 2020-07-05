package com.technosaab.newdavai.Fragments;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.technosaab.newdavai.Adapter.HomeRecyclerAdapter;
import com.technosaab.newdavai.Adapter.ViewPagerAdapter;
import com.technosaab.newdavai.Models.AdsImgModel;
import com.technosaab.newdavai.Models.CategoryResponse;
import com.technosaab.newdavai.R;
import com.technosaab.newdavai.rest.ApiClient;
import com.technosaab.newdavai.rest.ClientInterface;
import com.technosaab.newdavai.Helper.CustomVolleyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;
public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<CategoryResponse> categoriesList;
    private HomeRecyclerAdapter homeRecyclerAdapter;
    private EditText search;
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 200;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 2000; // time in milliseconds between successive task executions.
    ViewPager viewPager;
    private int dotsCount;
    private ImageView[] dots;
    RequestQueue rq;
    List<AdsImgModel> sliderImg;
    ViewPagerAdapter viewPagerAdapter;
    String request_url = "http://104.248.175.110/api/user/ads";
    private ProgressBar progressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = getLayoutInflater().inflate(R.layout.fragment_home , container , false);
        rq = CustomVolleyRequest.getInstance(getActivity()).getRequestQueue();
        sliderImg = new ArrayList<>();
        viewPager = view. findViewById(R.id.pager);

        recyclerView = view.findViewById(R.id.home_recyclerView);

        recyclerView.setHasFixedSize(true);
        search = view.findViewById(R.id.search_et);
        progressBar = view.findViewById(R.id.home_bar);
        progressBar.setVisibility(View.VISIBLE);
        GridLayoutManager gridlm = new GridLayoutManager(getActivity(), 2);
        gridlm.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridlm);
        categoriesList = new ArrayList<>();
        ConnectivityManager cm = (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(CONNECTIVITY_SERVICE);
        if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }else {
            getAllCategories();
            getRequest();
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                for(int i = 0; i< dotsCount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_launcher_background));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_launcher_background));
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == sliderImg.size()) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };
        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(s!=null||!s.toString().isEmpty())
                    homeRecyclerAdapter.getFilter().filter(s.toString());
            }
        });
        return view;
    }
    private void getAllCategories(){
        ClientInterface home = ApiClient.getClient().create(ClientInterface.class);
        Call<List<CategoryResponse>> call = home.getAllCategories();
        call.enqueue(new Callback<List<CategoryResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<CategoryResponse>> call, @NonNull Response<List<CategoryResponse>> response) {
                if (response.body()!=null){
                    categoriesList = response.body();
                    homeRecyclerAdapter = new HomeRecyclerAdapter(categoriesList , Objects.requireNonNull(getContext()));
                    recyclerView.setAdapter(homeRecyclerAdapter);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CategoryResponse>> call, @NonNull Throwable t) {
                try {
                    Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }catch (Exception e){}
            }
        });
    }
    public void getRequest(){
        JsonArrayRequest jsonArrayRequest  = new JsonArrayRequest(Request.Method.GET, request_url, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response.length()>0) {
                    for (int i = 0; i < response.length(); i++) {
                        AdsImgModel sliderUtils = new AdsImgModel();
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            sliderUtils.setImgPath(jsonObject.getString("imgPath"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        sliderImg.add(sliderUtils);
                    }
                    viewPagerAdapter = new ViewPagerAdapter(sliderImg, getApplicationContext());
                    viewPager.setAdapter(viewPagerAdapter);
                    dotsCount = viewPagerAdapter.getCount();
                    dots = new ImageView[dotsCount];
                    for (int i = 0; i < dotsCount; i++) {
                        dots[i] = new ImageView(getApplicationContext());
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_launcher_background));
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(8, 0, 8, 0);
                    }
                    dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_launcher_background));
                }else {
                    Toast.makeText(getContext(), getResources().getString(R.string.no_img), Toast.LENGTH_LONG).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        CustomVolleyRequest.getInstance(getActivity()).addToRequestQueue(jsonArrayRequest);
    }
}
