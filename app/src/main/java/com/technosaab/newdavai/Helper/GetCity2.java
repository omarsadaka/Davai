package com.technosaab.newdavai.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.technosaab.newdavai.Models.CityModel;
import com.technosaab.newdavai.R;
import com.technosaab.newdavai.rest.ApiClient;
import com.technosaab.newdavai.rest.ClientInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class GetCity2 {
    private Context context;
    private static String lang;
    private static List<CityModel> cityList;
    private static String cityName;
    private static final String getCityURL = "http://davai.online/api/client/GetCitiesByID?id=";

    public GetCity2(Context context) {
        this.context = context;
        try {
            SharedPreferences Prefs = context.getSharedPreferences("Lang", MODE_PRIVATE);
            String value = Prefs.getString("Local", null);
            if (value != null) {
                lang = Prefs.getString("Local", null);
            }
        }catch (Exception e){

        }

        cityList = new ArrayList<>();

    }


    public void getCity(String id, final Spinner userCity, final int layout , final Context ctx) {
        cityList.clear();
        ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
        Call<List<CityModel>> call = clientInterface.getCities(id);
        call.enqueue(new Callback<List<CityModel>>() {
            @Override
            public void onResponse(Call<List<CityModel>> call, Response<List<CityModel>> response) {
                if (response.body() != null) {
                    cityList = response.body();
                    List<String> list = new ArrayList<>();
                    if (lang.equals("english")) {
                        list.add(ctx.getResources().getString(R.string.all_cities));
                    } else if (lang.equals("ar")) {
                        list.add(ctx.getResources().getString(R.string.all_cities));
                    }

                    for (CityModel c : cityList
                            ) {
                        if (lang.equals("english")) {
                            list.add(c.getTitleEN());
                        } else if (lang.equals("ar")) {
                            list.add(c.getTitleAr());
                        }
                        ArrayAdapter<String> userCityAdapter = new ArrayAdapter<>(context, layout, list);
                        userCityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        userCity.setAdapter(userCityAdapter);

                    }
                }

            }

            @Override
            public void onFailure(Call<List<CityModel>> call, Throwable t) {
                Toast.makeText(context, context.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();

            }

        });
    }
    public static String id(int pos){
        return cityList.get(pos).getId();
    }
}
