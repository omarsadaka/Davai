package com.technosaab.newdavai.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.technosaab.newdavai.Activities.SignUpActivity;
import com.technosaab.newdavai.Adapter.ViewPagerAdapter;
import com.technosaab.newdavai.Models.AdsImgModel;
import com.technosaab.newdavai.Models.AdsResponse;
import com.technosaab.newdavai.Models.ClientAds;
import com.technosaab.newdavai.R;
import com.technosaab.newdavai.rest.ApiClient;
import com.technosaab.newdavai.rest.ClientInterface;
import com.technosaab.newdavai.Helper.CustomVolleyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class CreateAdsFragment extends Fragment implements View.OnClickListener {
    LinearLayout uploadAdsImage;
    ImageView payPal , visa , masterCard , americanExpress;
    PaymentTypeFragment  paymentTypeFragment;
    Bundle bundle;
    public static final int PICK_IMAGE = 1;
    private EditText ads_description;
    private Button send;
    private String url ="";
    static String cover_url="";
    private String clientId;

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = getLayoutInflater().inflate(R.layout.fragment_create_ads , container , false);
        SharedPreferences Prefs2 = getActivity().getSharedPreferences("UserId", MODE_PRIVATE);
        String value2 = Prefs2.getString("Id", null);
        if (value2 != null) {
            clientId = Prefs2.getString("Id", null);
        }
        createView(view);
        rq = CustomVolleyRequest.getInstance(getActivity()).getRequestQueue();
        sliderImg = new ArrayList<>();
        viewPager = view. findViewById(R.id.pager);
        getRequest();
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
        return view;
    }
    private void createView(View view){
        paymentTypeFragment = new PaymentTypeFragment();
        bundle  = new Bundle();
        uploadAdsImage = view.findViewById(R.id.upload_ads_image);
        ads_description = view.findViewById(R.id.ads_description);
        send = view.findViewById(R.id.pay_via);
        payPal = view.findViewById(R.id.payPal);
        visa = view.findViewById(R.id.visa);
        masterCard = view.findViewById(R.id.master_card);
        americanExpress = view.findViewById(R.id.am_express);
        uploadAdsImage.setOnClickListener(this);
        payPal.setOnClickListener(this);
        visa.setOnClickListener(this);
        masterCard.setOnClickListener(this);
        americanExpress.setOnClickListener(this);
        send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.upload_ads_image:
                if (checkPermissionREAD_EXTERNAL_STORAGE(getContext())) {
                    // do your stuff..
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);
                }
                break;
            case R.id.payPal:
                bundle.putString("paymentType","payPal");
                paymentTypeFragment.setArguments(bundle);
//                assert getFragmentManager() != null;
//                //fragmentTransaction.addToBackStack(null);
//                getFragmentManager().beginTransaction()
//                        .replace(R.id.frame, paymentTypeFragment).commit();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, paymentTypeFragment);
                //hbd
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                break;
            case R.id.visa:
                bundle.putString("paymentType","visa");
                paymentTypeFragment.setArguments(bundle);
                 fragmentManager = getFragmentManager();
                 fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, paymentTypeFragment);
                //hbd
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
//                assert getFragmentManager() != null;
//                getFragmentManager().beginTransaction()
//                        .replace(R.id.frame, paymentTypeFragment).commit();
                break;
            case R.id.master_card:
                bundle.putString("paymentType","masterCard");
                paymentTypeFragment.setArguments(bundle);
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, paymentTypeFragment);
                //hbd
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
//                assert getFragmentManager() != null;
//                getFragmentManager().beginTransaction()
//                        .replace(R.id.frame, paymentTypeFragment).commit();
                break;
            case R.id.am_express:
                bundle.putString("paymentType","amExpress");
                paymentTypeFragment.setArguments(bundle);
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, paymentTypeFragment);
                //hbd
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
//                assert getFragmentManager() != null;
//                getFragmentManager().beginTransaction()
//                        .replace(R.id.frame, paymentTypeFragment).commit();
                break;
            case R.id.pay_via:
                ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
                if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
                    Toast.makeText(getContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                }else {
                    sendAds();
                }

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);


        File finalFile ;
        try {
            Uri selectedImage = imageReturnedIntent.getData();
            if (selectedImage!=null) {
                finalFile = new File(getRealPathFromURI(selectedImage));
                ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
                if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
                    Toast.makeText(getContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                } else {
                    uploadPhoto(finalFile, requestCode);
                }
            }else {
                Toast.makeText(getContext(), getResources().getString(R.string.no_select_image), Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){

        }




    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
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
                                                     cover_url = url;

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
            case SignUpActivity.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
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
                                SignUpActivity.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
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
                                    SignUpActivity.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    private void sendAds() {
        String description = ads_description.getText().toString();
        if (cover_url != null && !TextUtils.isEmpty(description)) {
            ClientAds clientAds = new ClientAds(description , clientId , cover_url);
            ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
            Call<AdsResponse> call = clientInterface.clientAds(clientAds);
            call.enqueue(new Callback<AdsResponse>() {
                @Override
                public void onResponse(Call<AdsResponse> call, Response<AdsResponse> response) {
                          if (response.body()!=null){
                              Toast.makeText(getContext(), getResources().getString(R.string.successfully_created), Toast.LENGTH_LONG).show();
                              ads_description.setText("");
                          }
                }

                @Override
                public void onFailure(Call<AdsResponse> call, Throwable t) {

                }
            });
        }else {
            Toast.makeText(getContext(), getResources().getString(R.string.ads_post), Toast.LENGTH_LONG).show();
        }

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
