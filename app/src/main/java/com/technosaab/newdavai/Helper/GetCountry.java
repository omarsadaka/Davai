package com.technosaab.newdavai.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.technosaab.newdavai.Models.CountryModel;
import com.technosaab.newdavai.R;
import com.technosaab.newdavai.rest.ApiClient;
import com.technosaab.newdavai.rest.ClientInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class GetCountry {
    private static final String URL = "http://davai.online/api/client/GetAllCountry";
    private static String countryName;
    private static Context context;
    private static String lang;
    private static List<CountryModel> countryList;

    public GetCountry(Context context) {
        GetCountry.context = context;
        SharedPreferences Prefs = context.getSharedPreferences("Lang", MODE_PRIVATE);
        String value = Prefs.getString("Local", null);
        if (value != null) {
            lang = Prefs.getString("Local", null);
        }
        countryList = new ArrayList<>();
        //countryList.add(context.getResources().getString(R.string.country));

    }

    public  void getCountries(final Spinner userCountry , final int Layout ) {

        ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
        Call<List<CountryModel>> call = clientInterface.getCountries();
        call.enqueue(new Callback<List<CountryModel>>() {
            @Override
            public void onResponse(Call<List<CountryModel>> call, Response<List<CountryModel>> response) {
                if (response.body()!=null){
                    countryList = response.body();
                    List<String> list=new ArrayList<>();
                    list.add(context.getResources().getString(R.string.select_country));
                    for (CountryModel c:countryList
                         ) {
                        if (lang.equals("english")) {
                            list.add(c.getTitleEN());
                        }else if (lang.equals("ar")){

                            list.add(c.getTitleAr());
                        }

                    }
                    ArrayAdapter<String> userCountryAdapter = new ArrayAdapter<String>(context, Layout, list);
                        userCountryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        userCountry.setAdapter(userCountryAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<CountryModel>> call, Throwable t) {
                Toast.makeText(context, context.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();

            }
        });
    }

    public  void getCountries2(final Spinner userCountry , final int Layout ) {

        ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
        Call<List<CountryModel>> call = clientInterface.getCountries();
        call.enqueue(new Callback<List<CountryModel>>() {
            @Override
            public void onResponse(Call<List<CountryModel>> call, Response<List<CountryModel>> response) {
                if (response.body()!=null){
                    countryList = response.body();
                    List<String> list=new ArrayList<>();
                    //list.add(context.getResources().getString(R.string.select_country));
                    for (CountryModel c:countryList
                            ) {
                        if (lang.equals("english")) {
                            list.add(c.getTitleEN());
                        }else if (lang.equals("ar")){

                            list.add(c.getTitleAr());
                        }

                    }
                    ArrayAdapter<String> userCountryAdapter = new ArrayAdapter<String>(context, Layout, list);
                    userCountryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    userCountry.setAdapter(userCountryAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<CountryModel>> call, Throwable t) {
                Toast.makeText(context, context.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();

            }
        });
    }

    public static String id(int pos){
        return countryList.get(pos).getId();
    }

    public static int getId(String id){
      for (int i=0;i<countryList.size();i++){
          if (id== countryList.get(i).getId() || countryList.get(i).getId().equals(id)){
              return i;
          }
      }

       return 0;
    }
}
