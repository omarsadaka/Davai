package com.technosaab.newdavai.Fragments;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;
import com.technosaab.newdavai.Adapter.AboutAppRecyclerAdapter;
import com.technosaab.newdavai.Models.AboutApp;
import com.technosaab.newdavai.Models.GetVideo;
import com.technosaab.newdavai.R;
import com.technosaab.newdavai.rest.ApiClient;
import com.technosaab.newdavai.rest.ClientInterface;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;


public class AboutDevaiFragment extends Fragment {
    VideoView videoView;
    private RecyclerView recyclerView;
    private AboutAppRecyclerAdapter aboutAppRecyclerAdapter;
    private List<AboutApp> aboutAppList;
    private SharedPreferences prefs;
    private String userType;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = getLayoutInflater().inflate(R.layout.fragment_about_devai, container, false);
        prefs = getActivity().getSharedPreferences("checkUser", MODE_PRIVATE);
        String type = prefs.getString("userType", null);
        if (type != null) {
            userType = prefs.getString("userType", null);
        }
        videoView =  view.findViewById(R.id.video_view);
        recyclerView = view.findViewById(R.id.about_app_recycler);
        progressBar = view.findViewById(R.id.about_app_bar);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        aboutAppList = new ArrayList<>();
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
            Toast.makeText(getContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }else {
            getText();
            getVedio();
        }
        //videoView.setVideoURI(Uri.parse("android.resource://" + getActivity().getPackageName() +"/"+ R.raw.vedio));

        return view;
    }
      public void getText(){

        if (userType==null){
            ClientInterface aboutApp = ApiClient.getClient().create(ClientInterface.class);
            Call<List<AboutApp>> call = aboutApp.getUserAbout();
            call.enqueue(new Callback<List<AboutApp>>() {
                @Override
                public void onResponse(Call<List<AboutApp>> call, Response<List<AboutApp>> response) {
                    if (response.body()!=null){
                        aboutAppList=response.body();
                        aboutAppRecyclerAdapter = new AboutAppRecyclerAdapter(aboutAppList , getContext());
                        recyclerView.setAdapter(aboutAppRecyclerAdapter);
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<List<AboutApp>> call, Throwable t) {
                    Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        }else {
            if (userType.equals("1")) {
                ClientInterface aboutApp = ApiClient.getClient().create(ClientInterface.class);
                Call<List<AboutApp>> call = aboutApp.getUserAbout();
                call.enqueue(new Callback<List<AboutApp>>() {
                    @Override
                    public void onResponse(Call<List<AboutApp>> call, Response<List<AboutApp>> response) {
                        if (response.body() != null) {
                            aboutAppList = response.body();
                            aboutAppRecyclerAdapter = new AboutAppRecyclerAdapter(aboutAppList, getContext());
                            recyclerView.setAdapter(aboutAppRecyclerAdapter);
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<AboutApp>> call, Throwable t) {
                        Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            } else if (userType.equals("2")) {
                ClientInterface aboutApp = ApiClient.getClient().create(ClientInterface.class);
                Call<List<AboutApp>> call = aboutApp.getClientAbout();

                call.enqueue(new Callback<List<AboutApp>>() {
                    @Override
                    public void onResponse(Call<List<AboutApp>> call, Response<List<AboutApp>> response) {
                        if (response.body() != null) {
                            aboutAppList = response.body();
                            aboutAppRecyclerAdapter = new AboutAppRecyclerAdapter(aboutAppList, getContext());
                            recyclerView.setAdapter(aboutAppRecyclerAdapter);
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<AboutApp>> call, Throwable t) {
                        Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }
        //todo check if user or client

      }
      private void getVedio(){
          ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
          Call<List<GetVideo>> call = clientInterface.adsVideo();
          call.enqueue(new Callback<List<GetVideo>>() {
              @Override
              public void onResponse(Call<List<GetVideo>> call, Response<List<GetVideo>> response) {
                  if (response.body()!=null){
//                      videoView.setMediaController(new MediaController(getContext()));
//                      videoView.setVideoURI(Uri.parse(Url));
//                      videoView.requestFocus();
//                      videoView.start();

                      String LINK =response.body().get(0).getImgPath();
                      MediaController mc = new MediaController(getContext());
                      mc.setAnchorView(videoView);
                      mc.setMediaPlayer(videoView);
                      Uri video = Uri.parse(LINK);
                      videoView.setMediaController(mc);
                      videoView.setVideoURI(video);
                      videoView.start();
                  }
              }

              @Override
              public void onFailure(Call<List<GetVideo>> call, Throwable t) {
                  Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
              }
          });
      }
}
