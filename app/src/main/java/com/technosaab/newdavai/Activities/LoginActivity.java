package com.technosaab.newdavai.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import com.technosaab.newdavai.Models.UserByIdResponse;
import com.technosaab.newdavai.Util.GetUserData;
import com.technosaab.newdavai.rest.ApiClient;
import com.technosaab.newdavai.rest.ClientInterface;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.technosaab.newdavai.R;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.AccountService;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener  {
    private Button signIn , signUp;
    private TextView skipLogin;
    private LoginButton loginButton;
    private SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private ImageView fbIcon , googleIcon , twitterIcon;
    private CallbackManager callbackManager;
    private static final String EMAIL = "email";
    private GoogleSignInOptions gso;
    private TwitterLoginButton twitterLoginButton;
    private SharedPreferences.Editor editor ;
    private SharedPreferences prefs;
    static String email;
    static String twitterEmail;
    String googleEmail;
    TwitterAuthClient mTwitterAuthClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))//enable logging when app is in debug mode
                .twitterAuthConfig(new TwitterAuthConfig(getResources().getString(R.string.twitter_app_key), getResources().getString(R.string.twitter_secret)))//pass the created app Consumer KEY and Secret also called API Key and Secret
                .debug(true)//enable debug mode
                .build();
        Twitter.initialize(config);
        setContentView(R.layout.activity_login);
        keyHash();
             createView();
             createClick();
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
         gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mTwitterAuthClient = new TwitterAuthClient();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        callbackManager = CallbackManager.Factory.create();
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        LoginWithFacebook();
        loginWithTwitter();
        loginButton.setReadPermissions(Arrays.asList(EMAIL));

    }
    private void createView(){
        signIn = findViewById(R.id.sign_in_btn);
        signUp = findViewById(R.id.sign_up_btn);
        fbIcon = findViewById(R.id.fb_login);
        loginButton = findViewById(R.id.login_button);
        signInButton = findViewById(R.id.sign_in_button);
        googleIcon = findViewById(R.id.google_login);
        twitterIcon = findViewById(R.id.tw_login);
        skipLogin = findViewById(R.id.skip_login);
    }
    private void createClick(){
        signIn.setOnClickListener(this);
        signUp.setOnClickListener(this);
        fbIcon.setOnClickListener(this);
        signInButton.setOnClickListener(this);
        googleIcon.setOnClickListener(this);
        twitterIcon.setOnClickListener(this);
        skipLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id  = v.getId();
        switch (id){
            case R.id.sign_in_btn:
                startActivity(new Intent(LoginActivity.this , SignInActivity.class));
                LoginActivity.this.finish();
                break;
            case R.id.sign_up_btn:
                startActivity(new Intent(LoginActivity.this , SignUpActivity.class));
                LoginActivity.this.finish();

                break;
            case R.id.fb_login:
                prefs = getSharedPreferences("checkSideMenu",MODE_PRIVATE);
                prefs.edit().clear().apply();
                loginButton.performClick();
                editor = getSharedPreferences("checkLogin", MODE_PRIVATE).edit();
                editor.putString("loginType","facebook");
                editor.apply();
                break;
            case R.id.sign_in_button:
                googleSignIn();
                break;
            case R.id.google_login:
                prefs = getSharedPreferences("checkSideMenu",MODE_PRIVATE);
                prefs.edit().clear().apply();
                googleSignIn();
                signInButton.performClick();
                editor = getSharedPreferences("checkLogin", MODE_PRIVATE).edit();
                editor.putString("loginType","google");
                editor.apply();
                break;
            case R.id.tw_login:
                prefs = getSharedPreferences("checkSideMenu",MODE_PRIVATE);
                prefs.edit().clear().apply();
                twitterLoginButton.performClick();
                editor = getSharedPreferences("checkLogin", MODE_PRIVATE).edit();
                editor.putString("loginType","twitter");
                editor.apply();
                break;
            case R.id.skip_login:
                Intent intent = new Intent(LoginActivity.this , MainActivity.class);
                intent.putExtra("check" , "home");
                startActivity(intent);
                LoginActivity.this.finish();
                editor = getSharedPreferences("checkSideMenu", MODE_PRIVATE).edit();
                editor.putString("key","skipLogin");
                editor.apply();
                break;
        }
    }

    protected void keyHash() {
        PackageInfo info;
        try {
            info = this.getPackageManager().getPackageInfo("com.technosaab.newdavai", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("KeyHash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("KeyHash name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("KeyHashnosuchalgorithm", e.toString());
        } catch (Exception e) {
            Log.e("KeyHash exception", e.toString());
        }
    }

    private void LoginWithFacebook() {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                Log.d ( "Sucses!" , loginResult.toString () );

                final GraphRequest request = GraphRequest.newMeRequest (
                        AccessToken.getCurrentAccessToken ( ), new GraphRequest.GraphJSONObjectCallback ( ) {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    String first_name = object.getString ( "first_name" );
                                    String last_name = object.getString ( "last_name" );
                                    String id = object.getString ( "id" );
                                    String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

                                    //todo check if there is an email
                                    if (object.has ( "email" )) {
                                         email = object.getString ( "email" );
                                        Log.d ( "object.toString()", email );

                                        ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
                                        Call<UserByIdResponse> call = clientInterface.getUserByEmail(email );
                                        call.enqueue(new retrofit2.Callback<UserByIdResponse>() {
                                            @Override
                                            public void onResponse(Call<UserByIdResponse> call, Response<UserByIdResponse> response) {
                                                if (response.body()!=null){
                                                    Intent intent = new Intent(LoginActivity.this, SignInActivity.class);
                                                    startActivity(intent);
                                                    LoginActivity.this.finish();
                                                } else{
                                                    Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                                                    startActivity(intent);
                                                    LoginActivity.this.finish();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<UserByIdResponse> call, Throwable t) {

                                            }
                                        });
                                        //use UserData class with that email
                                    } else {
                                        // create a dialog to get valid mail!!
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace ( );
                                }

                            }
                        } );

                Bundle parameters = new Bundle ( );
                parameters.putString ( "fields", "first_name,last_name,email,id" );
                request.setParameters ( parameters );
                request.executeAsync ( );

            }

            @Override
            public void onCancel() {
                Toast.makeText(getBaseContext(), "Login onCancel Failed!", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getBaseContext(), "Facebook Login Failed!\n"+error.toString (), Toast.LENGTH_LONG).show();
                Log.e("failed" , error.toString());
            }
        });
    }

    private void loginWithTwitter() {

        twitterLoginButton = findViewById(R.id.twitter_login_button);

        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
                mTwitterAuthClient.requestEmail(twitterSession, new Callback<String>() {
                    @Override
                    public void success(Result<String> result) {
                        Log.d("email", result.data);
                        twitterEmail = result.data;
                        // Do something with the result, which provides the email address

                        ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
                        Call<UserByIdResponse> call = clientInterface.getUserByEmail(twitterEmail);
                        call.enqueue(new retrofit2.Callback<UserByIdResponse>() {
                            @Override
                            public void onResponse(Call<UserByIdResponse> call, Response<UserByIdResponse> response) {
                                if (response.body() != null) {
                                    Intent intent = new Intent(LoginActivity.this, SignInActivity.class);
                                    // intent.putExtra("check" , "home");
                                    startActivity(intent);
                                    LoginActivity.this.finish();
                                } else {
                                    Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                                    // intent.putExtra("check" , "home");
                                    startActivity(intent);
                                    LoginActivity.this.finish();
                                }
                            }

                            @Override
                            public void onFailure(Call<UserByIdResponse> call, Throwable t) {

                            }
                        });


                    }

                    @Override
                    public void failure(TwitterException exception) {
                        // Do something on failure
                    }
                });

                //twitterEmail = GetUserData.gettwitterEmail(twitterSession);

//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                intent.putExtra("check" , "home");
//                startActivity(intent);
//                LoginActivity.this.finish();
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                Toast.makeText(getBaseContext(), "Twitter Login Failed!", Toast.LENGTH_LONG).show();

            }
        });
    }



            private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 1);

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            if (completedTask.isSuccessful()) {
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
                if (acct != null) {
                     googleEmail = acct.getEmail();
                    ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
                    Call<UserByIdResponse> call = clientInterface.getUserByEmail(googleEmail);
                    call.enqueue(new retrofit2.Callback<UserByIdResponse>() {
                        @Override
                        public void onResponse(Call<UserByIdResponse> call, Response<UserByIdResponse> response) {
                            if (response.body()!=null){
                                Intent intent = new Intent(LoginActivity.this, SignInActivity.class);
                                // intent.putExtra("check" , "home");
                                startActivity(intent);
                                LoginActivity.this.finish();
                            } else{
                                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                                // intent.putExtra("check" , "home");
                                startActivity(intent);
                                LoginActivity.this.finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserByIdResponse> call, Throwable t) {

                        }
                    });

                }


            }
            else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
            }

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("failed", "signInResult:failed code=" + e.getStatusCode());

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 ){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }



}
