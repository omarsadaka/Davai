package com.technosaab.newdavai.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.squareup.picasso.Picasso;
import com.technosaab.newdavai.Fragments.AboutDevaiFragment;
import com.technosaab.newdavai.Fragments.AboutFragment;
import com.technosaab.newdavai.Fragments.ContactUsFragment;
import com.technosaab.newdavai.Fragments.CreateAdsFragment;
import com.technosaab.newdavai.Fragments.EditChatFragment;
import com.technosaab.newdavai.Fragments.EditServicesFragment;
import com.technosaab.newdavai.Fragments.FavouriteFragment;
import com.technosaab.newdavai.Fragments.HomeDetailsFragment;
import com.technosaab.newdavai.Fragments.HomeFragment;
import com.technosaab.newdavai.Fragments.LiveChatFragment;
import com.technosaab.newdavai.Fragments.MyProfileFragment;
import com.technosaab.newdavai.Fragments.MyVendorFragment;
import com.technosaab.newdavai.Fragments.ServicesFragment;
import com.technosaab.newdavai.Fragments.TermsConditionFragment;
import com.technosaab.newdavai.Fragments.VendorFragment;
import com.technosaab.newdavai.Fragments.VendorNotificationFragment;
import com.technosaab.newdavai.Helper.SkipLoginDialoge;
import com.technosaab.newdavai.Models.UserByIdResponse;
import com.technosaab.newdavai.Models.Vendor;

import com.technosaab.newdavai.R;
import com.technosaab.newdavai.Util.GetUserData;
import com.technosaab.newdavai.Util.ImageConverter;
import com.technosaab.newdavai.rest.ApiClient;
import com.technosaab.newdavai.rest.ClientInterface;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView back ;
    private CircleImageView userProfile;
    private LinearLayout home , reservation , favourite , menu ;
     public LinearLayout topButton;
    private LinearLayout myProfile ,share,rate, termsCondition , contactUs , aboutDavai , createAds , myVendor , signOut , signInLayout , vendorNotifivation;
    private Button about , services , liveChat , chat_btn;
    private ImageView menuIcon , favouriteIcon , reservationIcon ,homeIcon;
    private TextView menuTitle , favouriteTitle , reservationTitle ,homeTitle , userName;
    public TextView barTitle;
    private DrawerLayout drawerLayout;
    private String value;
    private AccessToken accessToken;
    private TwitterSession twitterSession;
    private SharedPreferences prefs , mPrefs , Mpref ,nPrefs,sPrefs ,Pref , mprefs;
    private String restoredText;
    private String userId;
    private String userType;
    String Type;
    AlertDialog dialog;
    private static final int RESULT_LOAD_IMG = 2;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))//enable logging when app is in debug mode
                .twitterAuthConfig(new TwitterAuthConfig(getResources().getString(R.string.twitter_app_key), getResources().getString(R.string.twitter_secret)))//pass the created app Consumer KEY and Secret also called API Key and Secret
                .debug(true)//enable debug mode
                .build();
        Twitter.initialize(config);
        setContentView(R.layout.activity_main);
        createView();
        createClick();

        SharedPreferences Pref =getSharedPreferences("UserId", MODE_PRIVATE);
        String key = Pref.getString("Id", null);
        if (key != null) {
            userId = Pref.getString("Id", null);
        }
        Mpref = getSharedPreferences("checkUser", MODE_PRIVATE);
        Type = Mpref.getString("userType", null);
        if (Type != null) {
            userType = Mpref.getString("userType", null);
        }
        value = getIntent().getStringExtra("check");
        switch (value) {
            case "home":
                startFragment(new HomeFragment());
                showClickedItem(homeTitle);
                homeIcon.setImageDrawable(getResources().getDrawable(R.drawable.gray_home));
                break;
            case "myVendorEdit":
                startFragment(new MyVendorFragment());
                barTitle.setText(getResources().getString(R.string.my_vendor));
                topButton.setVisibility(View.VISIBLE);
                topClickButton(about);
                showClickedItem(menuTitle);
                menuIcon.setImageDrawable(getResources().getDrawable(R.drawable.gray_menu));
                break;
            default:
                if (userType.equals("2")){
                    liveChat.setVisibility(View.GONE);

                }
                topButton.setVisibility(View.VISIBLE);
                startFragment(new AboutFragment());
                topClickButton(about);
                showClickedItem(homeTitle);
                homeIcon.setImageDrawable(getResources().getDrawable(R.drawable.gray_home));
                barTitle.setText("Vendor Name");
                break;
        }
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
            Toast.makeText(this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }else {
           // setUserData();
            getUserById();
        }
        mPrefs = getSharedPreferences("checkSideMenu", MODE_PRIVATE);
        restoredText = mPrefs.getString("key", null);
        if (restoredText != null) {
            String type = mPrefs.getString("key", "No name defined");//"No name defined" is the default value.
            if (type.equals("skipLogin")){
                myVendor.setVisibility(View.GONE);
                myProfile.setVisibility(View.GONE);
                contactUs.setVisibility(View.GONE);
                createAds.setVisibility(View.GONE);
                signOut.setVisibility(View.GONE);
                vendorNotifivation.setVisibility(View.GONE);
                signInLayout.setVisibility(View.VISIBLE);

            }
        }
//        prefs = getSharedPreferences("checkUser" , MODE_PRIVATE);
//        String type = prefs.getString("userType" , null);
        if ( Type!= null){
            if (userType.equals("1")){
                myVendor.setVisibility(View.GONE);
                createAds.setVisibility(View.GONE);
                vendorNotifivation.setVisibility(View.GONE);
            }else if (userType.equals("2")){
                myProfile.setVisibility(View.GONE);
                myVendor.setVisibility(View.VISIBLE);
                createAds.setVisibility(View.VISIBLE);
                favourite.setVisibility(View.GONE);
                vendorNotifivation.setVisibility(View.VISIBLE);
                //reservation.setVisibility(View.GONE);

            }
        }
    }
    private void createView(){
        back = findViewById(R.id.back);
        home = findViewById(R.id.home);
        reservation = findViewById(R.id.reservation);
        favourite = findViewById(R.id.favorite);
        menu = findViewById(R.id.menu);
        menuIcon = findViewById(R.id.menu_icon);
        favouriteIcon = findViewById(R.id.favorite_icon);
        reservationIcon = findViewById(R.id.reservation_icon);
        homeIcon = findViewById(R.id.home_icon);
        menuTitle = findViewById(R.id.menu_title);
        favouriteTitle = findViewById(R.id.favorite_title);
        reservationTitle = findViewById(R.id.reservation_title);
        homeTitle = findViewById(R.id.home_title);
        drawerLayout = findViewById(R.id.deawer_layout);
        topButton = findViewById(R.id.top_button);
        about = findViewById(R.id.about_btn);
        services = findViewById(R.id.service_btn);
        liveChat = findViewById(R.id.chat_btn);
        barTitle = findViewById(R.id.bar_title);
        myProfile = findViewById(R.id.my_profile_layout);
        share = findViewById(R.id.share_layout);
        rate = findViewById(R.id.rate_app_layout);
        termsCondition = findViewById(R.id.termsCondition_layout);
        contactUs = findViewById(R.id.contact_us_layout);
        aboutDavai = findViewById(R.id.about_layout);
        createAds = findViewById(R.id.creare_ads_layout);
        myVendor = findViewById(R.id.my_vendor_layout);
        userProfile = findViewById(R.id.image_profile);
        userName = findViewById(R.id.user_name);
        signOut = findViewById(R.id.sign_out_layout);
        signInLayout = findViewById(R.id.sign_in_layout);
        vendorNotifivation = findViewById(R.id.my_notify_layout);
        //chat_btn = findViewById(R.id.chat_btn);
    }
    private void createClick(){
        back.setOnClickListener(this);
        home.setOnClickListener(this);
        reservation.setOnClickListener(this);
        favourite.setOnClickListener(this);
        menu.setOnClickListener(this);
        about.setOnClickListener(this);
        services.setOnClickListener(this);
        liveChat.setOnClickListener(this);
        myProfile.setOnClickListener(this);
        termsCondition.setOnClickListener(this);
        contactUs.setOnClickListener(this);
        aboutDavai.setOnClickListener(this);
        createAds.setOnClickListener(this);
        myVendor.setOnClickListener(this);
        signOut.setOnClickListener(this);
        signInLayout.setOnClickListener(this);
        share.setOnClickListener(this);
        rate.setOnClickListener(this);
        userProfile.setOnClickListener(this);
        vendorNotifivation.setOnClickListener(this);
    }


    private void startFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        //hbd
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


    }
    private void showClickedItem(TextView clickedItem ) {
        menuTitle.setTextColor(getResources().getColor(R.color.colorBlue));
        favouriteTitle.setTextColor(getResources().getColor(R.color.colorBlue));
        reservationTitle.setTextColor(getResources().getColor(R.color.colorBlue));
        homeTitle.setTextColor(getResources().getColor(R.color.colorBlue));
        menuIcon.setImageDrawable(getResources().getDrawable(R.drawable.menu));
        favouriteIcon.setImageDrawable(getResources().getDrawable(R.drawable.favourite));
        reservationIcon.setImageDrawable(getResources().getDrawable(R.drawable.reservation));
        homeIcon.setImageDrawable(getResources().getDrawable(R.drawable.home));
        clickedItem.setTextColor(getResources().getColor(R.color.colorBrown));
    }
    private void topClickButton(Button button){
        about.setBackground(getResources().getDrawable(R.drawable.top_btn_bg));
        about.setTextColor(getResources().getColor(R.color.colorBlue));
        services.setBackground(getResources().getDrawable(R.drawable.top_btn_bg));
        services.setTextColor(getResources().getColor(R.color.colorBlue));
        liveChat.setBackground(getResources().getDrawable(R.drawable.top_btn_bg));
        liveChat.setTextColor(getResources().getColor(R.color.colorBlue));
        button.setBackground(getResources().getDrawable(R.drawable.pressed_top_buttob_bg));
        button.setTextColor(getResources().getColor(R.color.colorWhite));

    }
    @Override
    public void onClick(View v) {
       int id = v.getId();
       switch (id){
           case R.id.back:
               onBackPressed();
               break;
           case R.id.home:
               startFragment(new HomeFragment());
               showClickedItem(homeTitle);
               topButton.setVisibility(View.GONE);
               barTitle.setText(getResources().getString(R.string.home));
               homeIcon.setImageDrawable(getResources().getDrawable(R.drawable.gray_home));
               break;
           case R.id.reservation:
               if (restoredText != null) {
                   String value = mPrefs.getString("key", "No name defined");//"No name defined" is the default value.
                   if (value.equals("skipLogin")){
                       //Toast.makeText(MainActivity.this,getResources().getString(R.string.must_login), Toast.LENGTH_LONG).show();

                       SkipLoginDialoge.createDialoge(MainActivity.this);
                   }
               }else {
                   startFragment(new VendorFragment());
                   showClickedItem(reservationTitle);
                   topButton.setVisibility(View.GONE);
                   barTitle.setText(getResources().getString(R.string.new_reservation));
                   reservationIcon.setImageDrawable(getResources().getDrawable(R.drawable.gray_reservation));
               }
               break;
           case R.id.favorite:
               if (restoredText != null) {
                   String value = mPrefs.getString("key", "No name defined");//"No name defined" is the default value.
                   if (value.equals("skipLogin")){
                       //Toast.makeText(MainActivity.this,getResources().getString(R.string.must_login), Toast.LENGTH_LONG).show();

                       SkipLoginDialoge.createDialoge(MainActivity.this);
                   }
               }else {
                   startFragment(new FavouriteFragment());
                   showClickedItem(favouriteTitle);
                   topButton.setVisibility(View.GONE);
                   barTitle.setText(getResources().getString(R.string.favorite));
                   favouriteIcon.setImageDrawable(getResources().getDrawable(R.drawable.gray_favourite));
               }

               break;
           case R.id.menu:
               drawerLayout.openDrawer(GravityCompat.START);
               showClickedItem(menuTitle);
               //topButton.setVisibility(View.GONE);
               menuIcon.setImageDrawable(getResources().getDrawable(R.drawable.gray_menu));
               break;
           case R.id.about_btn:
               if (value.equals("myVendorEdit")){
                   startFragment(new MyVendorFragment());
                   topClickButton(about);
                   showClickedItem(menuTitle);
                   menuIcon.setImageDrawable(getResources().getDrawable(R.drawable.gray_menu));
               }else {
                   startFragment(new AboutFragment());
                   topClickButton(about);
                   showClickedItem(homeTitle);
                   homeIcon.setImageDrawable(getResources().getDrawable(R.drawable.gray_home));
                   barTitle.setText("Vendor Name");
               }

               break;
           case R.id.service_btn:

               if (value.equals("myVendorEdit")){
                   startFragment(new EditServicesFragment());
                   topClickButton(services);
                   showClickedItem(menuTitle);
                   menuIcon.setImageDrawable(getResources().getDrawable(R.drawable.gray_menu));
               }else {
                   startFragment(new ServicesFragment());
                   topClickButton(services);
                   showClickedItem(homeTitle);
                   homeIcon.setImageDrawable(getResources().getDrawable(R.drawable.gray_home));
                   barTitle.setText("Vendor Name");
               }

               break;
           case R.id.chat_btn:
               if (value.equals("myVendorEdit")){
                   startFragment(new EditChatFragment());
                   topClickButton(liveChat);
                   showClickedItem(menuTitle);
                   menuIcon.setImageDrawable(getResources().getDrawable(R.drawable.gray_menu));
               }else {
                   startFragment(new LiveChatFragment());
                   topClickButton(liveChat);
                   showClickedItem(homeTitle);
                   homeIcon.setImageDrawable(getResources().getDrawable(R.drawable.gray_home));
                   barTitle.setText("Vendor Name");
               }

               break;
           case R.id.share_layout:
               //topButton.setVisibility(View.GONE);
               Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
               sharingIntent.setType("text/plain");
               String shareBody = "http://play.google.com/store/apps/details?id=" +getPackageName();
               sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
               sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
               startActivity(Intent.createChooser(sharingIntent,getResources().getString(R.string.sharevia)));
               break;
           case R.id.rate_app_layout:
               Uri uri = Uri.parse("market://details?id=" + getPackageName());
               Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
               // To count with Play market backstack, After pressing back button,
               // to taken back to our application, we need to add following flags to intent.
               goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                       Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                       Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
               try {
                   startActivity(goToMarket);
               } catch (ActivityNotFoundException e) {
                   startActivity(new Intent(Intent.ACTION_VIEW,
                           Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
               }
               break;
           case R.id.my_profile_layout:
               drawerLayout.closeDrawer(GravityCompat.START);
               topButton.setVisibility(View.GONE);
               startFragment(new MyProfileFragment());

               barTitle.setText(getResources().getString(R.string.my_profile));
               break;
           case R.id.termsCondition_layout:
               drawerLayout.closeDrawer(GravityCompat.START);
               topButton.setVisibility(View.GONE);
               startFragment(new TermsConditionFragment());

               barTitle.setText(getResources().getString(R.string.terms));
               break;
           case R.id.contact_us_layout:
               drawerLayout.closeDrawer(GravityCompat.START);
               topButton.setVisibility(View.GONE);
               startFragment(new ContactUsFragment());

               barTitle.setText(getResources().getString(R.string.contact_us));
               break;
           case R.id.about_layout:
               drawerLayout.closeDrawer(GravityCompat.START);
               topButton.setVisibility(View.GONE);
               startFragment(new AboutDevaiFragment());

               barTitle.setText(getResources().getString(R.string.about_devai));
               break;
           case R.id.creare_ads_layout:
               drawerLayout.closeDrawer(GravityCompat.START);
               topButton.setVisibility(View.GONE);

               startFragment(new CreateAdsFragment());

               barTitle.setText(getResources().getString(R.string.create_ads));
               break;
           case R.id.my_vendor_layout:
               Intent i = new Intent(this, MainActivity.class);
               i.putExtra("check", "myVendorEdit");
               startActivity(i);
               break;
           case R.id.sign_out_layout:
               logout();
               break;
           case R.id.sign_in_layout:
               startActivity(new Intent(MainActivity.this , LoginActivity.class));
               MainActivity.this.finish();
               break;
           case R.id.my_notify_layout:
               drawerLayout.closeDrawer(GravityCompat.START);
               topButton.setVisibility(View.GONE);
               startFragment(new VendorNotificationFragment());
               barTitle.setText(getResources().getString(R.string.my_notification));
               break;
       }
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                userProfile.setImageBitmap(selectedImage);
                editor = getSharedPreferences("userImageProfile", MODE_PRIVATE).edit();
                editor.putString("userImage", String.valueOf(selectedImage));
                editor.apply();
//                RoundedBitmapDrawable roundedBitmapDrawable= RoundedBitmapDrawableFactory.create(getResources(), selectedImage);
//
//         //setting radius
//                roundedBitmapDrawable.setCornerRadius(50.0f);
//                roundedBitmapDrawable.setAntiAlias(true);
//                userProfile.setImageDrawable(roundedBitmapDrawable);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(MainActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    private void setUserData(){

         prefs = getSharedPreferences("checkLogin", MODE_PRIVATE);
        String restoredText = prefs.getString("loginType", null);
        if (restoredText != null) {
            String type = prefs.getString("loginType", "No name defined");//"No name defined" is the default value.
            if (type.equals("facebook")){
                GetUserData.facebookLoginInformation(accessToken , userProfile , userName);
            }else if (type.equals("twitter")){
                twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
                GetUserData.twitterInformation(twitterSession , userProfile , userName);
            }else if (type.equals("google")){
                GetUserData.googleInformation( this , userProfile , userName);
            }else if (type.equals("loginAsUser")){
               getUserById();
            }
        }

        }

        private void logout(){
             prefs = getSharedPreferences("checkLogin", MODE_PRIVATE);
            String restoredText = prefs.getString("loginType", null);
            if (restoredText != null) {
                String type = prefs.getString("loginType", "No name defined");//"No name defined" is the default value.
                if (type.equals("facebook")){
                    GetUserData.facebookSignOut(MainActivity.this);
                    prefs = getSharedPreferences("checkLogin",MODE_PRIVATE);
                    prefs.edit().clear().apply();
                }else if (type.equals("twitter")){
                    GetUserData.twitterSignOut(MainActivity.this);
                    prefs = getSharedPreferences("checkLogin",MODE_PRIVATE);
                    prefs.edit().clear().apply();
                }else if (type.equals("google")){
                    GetUserData.googleSignOut(this);
                    prefs = getSharedPreferences("checkLogin",MODE_PRIVATE);
                    prefs.edit().clear().apply();
                }else if (type.equals("loginAsUser")){
                    if (AccessToken.getCurrentAccessToken()!=null){
                        GetUserData.facebookSignOut(MainActivity.this);
                        prefs = getSharedPreferences("checkLogin",MODE_PRIVATE);
                        prefs.edit().clear().apply();
                        mprefs = getSharedPreferences("ClientLogo",MODE_PRIVATE);
                        mprefs.edit().clear().apply();

                    }else {
                        nPrefs = getSharedPreferences("loginType", MODE_PRIVATE);
                        Pref = getSharedPreferences("checkUser", MODE_PRIVATE);
                        sPrefs = getSharedPreferences("checkSideMenu", MODE_PRIVATE);
                        prefs = getSharedPreferences("checkLogin", MODE_PRIVATE);
                        //mprefs = getSharedPreferences("Lang", MODE_PRIVATE);
                        mprefs = getSharedPreferences("ClientLogo",MODE_PRIVATE);
                        mprefs.edit().clear().apply();
                        prefs.edit().clear().apply();
                        Pref.edit().clear().apply();
                        sPrefs.edit().clear().apply();
                        nPrefs.edit().clear().apply();
                       // mprefs.edit().clear().apply();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        MainActivity.this.finish();
                    }
                }else{
                    sPrefs=getSharedPreferences("checkSideMenu", MODE_PRIVATE);
                    Pref=getSharedPreferences("checkUser",MODE_PRIVATE);
                    nPrefs=getSharedPreferences("loginType",MODE_PRIVATE);
                    prefs = getSharedPreferences("checkLogin",MODE_PRIVATE);
                    //mprefs = getSharedPreferences("Lang", MODE_PRIVATE);
                    prefs.edit().clear().apply();
                    Pref.edit().clear().apply();
                    nPrefs.edit().clear().apply();
                    sPrefs.edit().clear().apply();
                    //mprefs.edit().clear().apply();
                    mprefs = getSharedPreferences("ClientLogo",MODE_PRIVATE);
                    mprefs.edit().clear().apply();
                    startActivity(new Intent(MainActivity.this , LoginActivity.class));
                    MainActivity.this.finish();

                }
            }
        }

    public void getUserById(){

        try {

            if (userType.equals("1")) {
                ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
                Call<UserByIdResponse> call = clientInterface.getUserById(userId);
                call.enqueue(new Callback<UserByIdResponse>() {
                    @Override
                    public void onResponse(Call<UserByIdResponse> call, Response<UserByIdResponse> response) {
                        if (response.body() != null) {
                            String Name = response.body().getFirstName();
                            userName.setText(Name);
                            try {
                                Picasso.get().load(response.body().getPersonalImg()).transform(new ImageConverter()).into(userProfile);
                            }catch (Exception e){

                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<UserByIdResponse> call, Throwable t) {
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                    }
                });

            } else if (userType.equals("2")) {
                ClientInterface aboutVendor = ApiClient.getClient().create(ClientInterface.class);
                Call<Vendor> call = aboutVendor.getVendorAbout(userId);
                call.enqueue(new Callback<Vendor>() {
                    @Override
                    public void onResponse(@NonNull Call<Vendor> call, @NonNull Response<Vendor> response) {
                        if (response.body() != null) {
                            userName.setText(response.body().getBrandName());
                            Picasso.get().load(response.body().getLogo()).transform(new ImageConverter()).into(userProfile);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Vendor> call, @NonNull Throwable t) {
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }catch (Exception e){

        }


    }

//    @Override
//    public void onBackPressed() {
//        int m=    getSupportFragmentManager().getBackStackEntryCount();
//        List fragmentList = getSupportFragmentManager().getFragments();
//
////        boolean handled = false;
////        for(Fragment f : fragmentList) {
////            if(f instanceof BaseFragment) {
////                handled = ((BaseFragment)f).onBackPressed();
////
////                if(handled) {
////                    break;
////                }
////            }
////        }
////
////        if(!handled) {
////            super.onBackPressed();
////        }
//
//
//
//        if(getFragmentManager().getBackStackEntryCount()!=0) {
//            getFragmentManager().popBackStack();
//
//        }else {
//          super.onBackPressed();
//        }
//    }
@Override
public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            tellFragments2();
            super.onBackPressed();
            tellFragments();
        }

}

    private void tellFragments(){
        int m=    getSupportFragmentManager().getBackStackEntryCount();
        List fragmentList = getSupportFragmentManager().getFragments();

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for(Fragment f : fragments){
            if(f != null && f instanceof HomeFragment){
                showClickedItem(homeTitle);
                barTitle.setText(getResources().getString(R.string.home));
                homeIcon.setImageDrawable(getResources().getDrawable(R.drawable.gray_home));

            }else if(f != null && f instanceof VendorFragment){
                showClickedItem(reservationTitle);

                barTitle.setText(getResources().getString(R.string.new_reservation));
                reservationIcon.setImageDrawable(getResources().getDrawable(R.drawable.gray_reservation));
            }else if(f != null && f instanceof FavouriteFragment){
                showClickedItem(favouriteTitle);

                barTitle.setText(getResources().getString(R.string.favorite));
                favouriteIcon.setImageDrawable(getResources().getDrawable(R.drawable.gray_favourite));

            }else if(f != null && f instanceof HomeDetailsFragment){


                showClickedItem(homeTitle);
                barTitle.setText(getResources().getString(R.string.home));
                homeIcon.setImageDrawable(getResources().getDrawable(R.drawable.gray_home));

            }else if(f != null && f instanceof AboutFragment){
                topClickButton(about);
                showClickedItem(homeTitle);
                homeIcon.setImageDrawable(getResources().getDrawable(R.drawable.gray_home));
                barTitle.setText("Vendor Name");
                topButton.setVisibility(View.VISIBLE);

            }else if(f != null && f instanceof ServicesFragment){
                topClickButton(services);
                showClickedItem(homeTitle);
                homeIcon.setImageDrawable(getResources().getDrawable(R.drawable.gray_home));
                barTitle.setText("Vendor Name");
                topButton.setVisibility(View.VISIBLE);
            }else if(f != null && f instanceof LiveChatFragment){
                topClickButton(liveChat);
                showClickedItem(homeTitle);
                homeIcon.setImageDrawable(getResources().getDrawable(R.drawable.gray_home));
               // barTitle.setText("Vendor Name");
                topButton.setVisibility(View.VISIBLE);
            }else if(f != null && f instanceof EditChatFragment){
                topClickButton(liveChat);
                showClickedItem(menuTitle);
                menuIcon.setImageDrawable(getResources().getDrawable(R.drawable.gray_menu));
            }else if(f != null && f instanceof EditServicesFragment){
                topClickButton(services);
                showClickedItem(menuTitle);
                menuIcon.setImageDrawable(getResources().getDrawable(R.drawable.gray_menu));

            }else if(f != null && f instanceof MyVendorFragment){
                barTitle.setText(getResources().getString(R.string.my_vendor));
                topButton.setVisibility(View.VISIBLE);
                topClickButton(about);
                showClickedItem(menuTitle);
                menuIcon.setImageDrawable(getResources().getDrawable(R.drawable.gray_menu));
            }else if(f != null && f instanceof VendorNotificationFragment){
                barTitle.setText(getResources().getString(R.string.my_notification));
                //topButton.setVisibility(View.VISIBLE);
                //topClickButton(about);
                showClickedItem(menuTitle);
                menuIcon.setImageDrawable(getResources().getDrawable(R.drawable.gray_menu));
            }

        }
    }
    private void tellFragments2(){
        int m=    getSupportFragmentManager().getBackStackEntryCount();
        List fragmentList = getSupportFragmentManager().getFragments();

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for(Fragment f : fragments){
            if(f != null && f instanceof HomeFragment){

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                View view = LayoutInflater.from(this).inflate(R.layout.finish_app, null);
                Button yes = view.findViewById(R.id.yes_btn);
                Button no = view.findViewById(R.id.no_btn);
                dialogBuilder.setView(view);
                 dialog = dialogBuilder.create();
                dialog.show();
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.this.finish();
                        if(SelectLangActivity.instance != null) {
                            try {
                                SelectLangActivity.instance.finish();
                            } catch (Exception e) {}
                        }
                        //getSupportFragmentManager().popBackStack();

                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        startFragment(new HomeFragment());
                    }
                });

            } else if(f != null && f instanceof AboutFragment){

               getSupportFragmentManager().popBackStack();

            }else if(f != null && f instanceof MyVendorFragment){
                getSupportFragmentManager().popBackStack();

            }else if(f != null && f instanceof MyProfileFragment){
             // MyProfileFragment fragment = new MyProfileFragment();
              if (MyProfileFragment.flag2){
//                  MyProfileFragment.chatListLayout.setVisibility(View.GONE);
//                  MyProfileFragment.chatListRecycler.setVisibility(View.GONE);
                  startFragment(new MyProfileFragment());
                  MyProfileFragment.flag2 = false;
              }


            }

        }
    }
}
