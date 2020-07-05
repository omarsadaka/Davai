package com.technosaab.newdavai.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;
import com.technosaab.newdavai.Activities.LoginActivity;
import com.technosaab.newdavai.Activities.SignInActivity;
import com.technosaab.newdavai.Activities.SignUpActivity;
import com.technosaab.newdavai.Models.UserByIdResponse;
import com.technosaab.newdavai.rest.ApiClient;
import com.technosaab.newdavai.rest.ClientInterface;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;

import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.AccountService;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;

public class GetUserData {
    static Context context;
    private static GoogleSignInClient mGoogleSignInClient;
    private static GoogleSignInOptions gso;
    static TwitterAuthClient mTwitterAuthClient;

    public GetUserData(Context context) {
        this.context = context;
    }

    public static void facebookLoginInformation(AccessToken accessToken , final ImageView imageView , final TextView textView) {
        /**
         Creating the GraphRequest to fetch user details
         1st Param - AccessToken
         2nd Param - Callback (which will be invoked once the request is successful)
         **/
        GraphRequest request = GraphRequest.newMeRequest (
                AccessToken.getCurrentAccessToken ( ), new GraphRequest.GraphJSONObjectCallback ( ) {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String first_name = object.getString ( "first_name" );
                            String last_name = object.getString ( "last_name" );
                            String id = object.getString ( "id" );
                            String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

                            Picasso.get ( ).load ( image_url ).transform ( new ImageConverter( ) ).into ( imageView );
                            textView.setText ( first_name + last_name );
                            Log.d ( "user ", first_name + last_name );
                            Log.d ( "image  ", image_url );

                            //todo check if there is an email
                            if (object.has ( "email" )) {
                                String email = object.getString ( "email" );
                                Log.d ( "object.toString()", email );
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
    public static void twitterInformation( TwitterSession twitterSession , final ImageView imageView , final TextView textView) {

        TwitterCore.getInstance().getApiClient(twitterSession).getAccountService().verifyCredentials(true, false ,false).enqueue(new Callback<User>() {
            @Override
            public void success(Result<User> userResult) {
                try {
                    User user = userResult.data;
                    String fullName = user.name;
                    String userSocialProfile = user.profileImageUrl;
                    textView.setText(fullName);
                    Picasso.get ( ).load ( userSocialProfile ).transform ( new ImageConverter( ) ).into ( imageView );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void failure(TwitterException e) {
            }
        });

    }

    public static void twitterSignOut(Context context){
        TwitterCore.getInstance().getSessionManager().clearActiveSession();
        Intent intent = new Intent(context , LoginActivity.class);
        context.startActivity(intent);
        ((Activity)context).finish();
    }

    public static void facebookSignOut(Context context){
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(context , LoginActivity.class);
        context.startActivity(intent);
        ((Activity)context).finish();
    }
    public static void googleSignOut(final Activity activity){
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        Intent intent = new Intent(activity , LoginActivity.class);
                        activity.startActivity(intent);
                    }
                });
    }
    public static void googleInformation( Activity activity , ImageView imageView , TextView textView){
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(activity);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            textView.setText(personName);
            Picasso.get ( ).load ( personPhoto ).transform ( new ImageConverter( ) ).into ( imageView );

        }
    }

    public static void facebookEmail(AccessToken accessToken , final EditText firstName , final EditText lastName , final EditText e_mail ) {
        /**
         Creating the GraphRequest to fetch user details
         1st Param - AccessToken
         2nd Param - Callback (which will be invoked once the request is successful)
         **/
        GraphRequest request = GraphRequest.newMeRequest (
                AccessToken.getCurrentAccessToken ( ), new GraphRequest.GraphJSONObjectCallback ( ) {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String first_name = object.getString ( "first_name" );
                            String last_name = object.getString ( "last_name" );
                            String id = object.getString ( "id" );
                            String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

                            firstName.setText(first_name);
                            lastName.setText(last_name);
                            //todo check if there is an email
                            if (object.has ( "email" )) {
                                String email = object.getString ( "email" );
                                Log.d ( "object.toString()", email );
                                e_mail.setText(email);

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
    public static void facebookEmail2(AccessToken accessToken ,  final EditText e_mail ) {
        /**
         Creating the GraphRequest to fetch user details
         1st Param - AccessToken
         2nd Param - Callback (which will be invoked once the request is successful)
         **/
        GraphRequest request = GraphRequest.newMeRequest (
                AccessToken.getCurrentAccessToken ( ), new GraphRequest.GraphJSONObjectCallback ( ) {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
//                            String first_name = object.getString ( "first_name" );
//                            String last_name = object.getString ( "last_name" );
                            //String id = object.getString ( "id" );
                            //String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";


                            //todo check if there is an email
                            if (object.has ( "email" )) {
                                String email = object.getString ( "email" );
                                Log.d ( "object.toString()", email );
                                e_mail.setText(email);

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

    public static void twitterEmail(final TwitterSession twitterSession , final EditText email , final EditText name ) {

        TwitterCore.getInstance().getApiClient(twitterSession).getAccountService().verifyCredentials(true, false ,false).enqueue(new Callback<User>() {
            @Override
            public void success(Result<User> userResult) {
                try {
                    User user = userResult.data;
                    String fullName = user.name;
                    String userSocialProfile = user.profileImageUrl;
                    name.setText(fullName);

                    mTwitterAuthClient = new TwitterAuthClient();
                    mTwitterAuthClient.requestEmail(twitterSession, new Callback<String>() {
                        @Override
                        public void success(Result<String> result) {
                            Log.d("email", result.data);
                            String twitterEmail = result.data;
                            email.setText(twitterEmail);
                            // Do something with the result, which provides the email address

                        }

                        @Override
                        public void failure(TwitterException exception) {
                            // Do something on failure
                        }
                    });



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void failure(TwitterException e) {
            }
        });

    }

    public static void twitterEmail2(TwitterSession twitterSession , final EditText email  ) {

         mTwitterAuthClient = new TwitterAuthClient();
        twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
        mTwitterAuthClient.requestEmail(twitterSession, new Callback<String>() {
            @Override
            public void success(Result<String> result) {
                Log.d("email", result.data);
                String twitterEmail = result.data;
                email.setText(twitterEmail);
                // Do something with the result, which provides the email address

            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
            }
        });
    }

    public static void googleEmail( Activity activity , EditText editText){
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(activity);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            editText.setText(personEmail);
        }
    }

    public static void googleEmail2( Activity activity , EditText Fname ,EditText email , EditText Lname ){
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(activity);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            Fname.setText(personName);
            Lname.setText(personFamilyName);
            email.setText(personEmail);
        }
    }

}
