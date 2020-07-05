package com.technosaab.newdavai.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.google.firebase.iid.FirebaseInstanceId;
import com.technosaab.newdavai.Models.User;
import com.technosaab.newdavai.R;
import com.technosaab.newdavai.Util.GetUserData;
import com.technosaab.newdavai.rest.ApiClient;
import com.technosaab.newdavai.rest.ClientInterface;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView back;
    private Button signIn;
    private EditText email , password;
    private TextView forgetPassword , barTitle;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private SharedPreferences.Editor editor;
    private SharedPreferences  prefs;
    private ProgressDialog progressDialog;
    private LinearLayout activateLayout;
    EditText forgetPwdEdit;
    String userkey="";
    private AccessToken accessToken;
    private TwitterSession twitterSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        createView();
        setUserData();
        barTitle.setText(getResources().getString(R.string.sign_in));
        progressDialog = new ProgressDialog(SignInActivity.this);

    }
    private void createView(){
        back = findViewById(R.id.back);
        signIn = findViewById(R.id.sign_in);
        forgetPassword = findViewById(R.id.forget_pwd);
        barTitle = findViewById(R.id.titleBar);
        email = findViewById(R.id.email_et);
        password = findViewById(R.id.password_et);
        activateLayout = findViewById(R.id.bottom_layout);
        email.setOnClickListener(this);
        password.setOnClickListener(this);
        back.setOnClickListener(this);
        signIn.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.back:
                onBackPressed();
                logout();
                SignInActivity.this.finish();
                break;
            case R.id.sign_in:
                String Email = email.getText().toString().trim();
                String Password = password.getText().toString();
                if (!TextUtils.isEmpty(Email) && !TextUtils.isEmpty(Password)){
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
                    prefs = getSharedPreferences("checkSideMenu",MODE_PRIVATE);
                    prefs.edit().clear().apply();
                    progressDialog.setMessage ( getResources().getString(R.string.loading) );
                    progressDialog.show ( );
                    ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                    if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
                        Toast.makeText(SignInActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }else {
                        signIn();
                    }
                    }else {
                        Toast.makeText(this, getResources().getString(R.string.enter_valid_email), Toast.LENGTH_LONG).show();
                    }

                }else checkEmptyField();

                break;
            case R.id.forget_pwd:
                dialogBuilder = new AlertDialog.Builder(this);
                View view = getLayoutInflater().inflate(R.layout.forget_pwd_popup , null);
                forgetPwdEdit = view.findViewById(R.id.forget_pwd_et);
                Button send = view.findViewById(R.id.send);
                Button cancel = view.findViewById(R.id.cancel);
                dialogBuilder.setView(view);
                dialog = dialogBuilder.create();
                dialog.show();
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                        if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
                            Toast.makeText(SignInActivity.this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
                        }else {
                            sendEmail();
                        }

                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                break;

        }
    }
    private void checkEmptyField(){
        String Email = email.getText().toString();
        String Password = password.getText().toString();
        if (TextUtils.isEmpty(Email)){
            email.setError(getResources().getString(R.string.not_empity));
        }else if (TextUtils.isEmpty(Password)){
            password.setError(getResources().getString(R.string.not_empity));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        logout();
    }

    private void signIn(){
        String Email = email.getText().toString().trim();
        String Password = password.getText().toString();
        userkey = FirebaseInstanceId.getInstance().getToken();
        //UserSignIn userSignIn = new UserSignIn(Email, Password);
        ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
        Call<User> call = clientInterface.userLogIn(Email , Password , userkey);
        call.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body() != null) {
                    progressDialog.dismiss();

                    String userId = response.body().getId();
                    int userType = response.body().getUserType();
                    Log.d("id" , userId);
                    editor = getSharedPreferences("UserId", MODE_PRIVATE).edit();
                    editor.putString("Id", userId);
                    editor.apply();

                    editor = getSharedPreferences("checkLogin", MODE_PRIVATE).edit();
                    editor.putString("loginType","loginAsUser");
                    editor.apply();

                    editor = getSharedPreferences("checkUser", MODE_PRIVATE).edit();
                    editor.putString("userType", String.valueOf(userType));
                    editor.apply();
                    Intent intent = new Intent(SignInActivity.this , MainActivity.class);
                    intent.putExtra("check" , "home");
                    startActivity(intent);
                    SignInActivity.this.finish();
                }else {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("Message");
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        String sv=response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String s=response.message();
                    if (s.equals("Unauthorized")){
                        Toast.makeText(SignInActivity.this, getResources().getString(R.string.not_signIn), Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(SignInActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }
        });
    }

    private void sendEmail(){
           String email = forgetPwdEdit.getText().toString();
           if (!TextUtils.isEmpty(email)){
               if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                   ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
                   Call<ResponseBody> call = clientInterface.forgetPassword(email);
                   call.enqueue(new Callback<ResponseBody>() {
                       @Override
                       public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                           if (response.body() != null) {
                               Toast.makeText(SignInActivity.this, getResources().getString(R.string.pwd_send), Toast.LENGTH_LONG).show();
                               forgetPwdEdit.setText("");
                           }else {
                               String s=response.message();
                               if (s.equals("Unauthorized")){
                                   Toast.makeText(SignInActivity.this, getResources().getString(R.string.not_signIn), Toast.LENGTH_LONG).show();
                               }
                           }
                       }

                       @Override
                       public void onFailure(Call<ResponseBody> call, Throwable t) {
                           Toast.makeText(SignInActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                       }
                   });
               }else {
                   Toast.makeText(this, getResources().getString(R.string.enter_valid_email), Toast.LENGTH_LONG).show();
               }
           }else {
               Toast.makeText(this, getResources().getString(R.string.enter_your_email), Toast.LENGTH_LONG).show();
           }
    }

    private void setUserData(){

        prefs = getSharedPreferences("checkLogin", MODE_PRIVATE);
        String restoredText = prefs.getString("loginType", null);
        if (restoredText != null) {
            String type = prefs.getString("loginType", "No name defined");//"No name defined" is the default value.
            if (type.equals("facebook")){
                GetUserData.facebookEmail2(accessToken , email );
            }else if (type.equals("twitter")){
                twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
                GetUserData.twitterEmail2(twitterSession , email );
            }else if (type.equals("google")){
                GetUserData.googleEmail( this , email );
            }else if (type.equals("loginAsUser")){
                //getUserById();
            }
        }

    }
    private void logout(){
        prefs = getSharedPreferences("checkLogin", MODE_PRIVATE);
        String restoredText = prefs.getString("loginType", null);
        if (restoredText != null) {
            String type = prefs.getString("loginType", "No name defined");//"No name defined" is the default value.
            if (type.equals("facebook")){
                GetUserData.facebookSignOut(this);
                prefs = getSharedPreferences("checkLogin",MODE_PRIVATE);
                prefs.edit().clear().apply();
            }else if (type.equals("twitter")){
                GetUserData.twitterSignOut(this);
                prefs = getSharedPreferences("checkLogin",MODE_PRIVATE);
                prefs.edit().clear().apply();
            }else if (type.equals("google")){
                GetUserData.googleSignOut(this);
                prefs = getSharedPreferences("checkLogin",MODE_PRIVATE);
                prefs.edit().clear().apply();
            }else if (type.equals("loginAsUser")) {
                if (AccessToken.getCurrentAccessToken() != null) {
                    GetUserData.facebookSignOut(this);
                    prefs = getSharedPreferences("checkLogin", MODE_PRIVATE);
                    prefs.edit().clear().apply();
                } else {

                }
            }

        }
    }
}
