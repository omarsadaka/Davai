package com.technosaab.newdavai.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.technosaab.newdavai.R;
import com.technosaab.newdavai.Util.LocalHelper;

public class SelectLangActivity extends AppCompatActivity implements View.OnClickListener {

    private Button english , arabic;
    private SharedPreferences prefs;
    String restoredText;
    private SharedPreferences.Editor editor;
    public static SelectLangActivity instance = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_lang);
        instance = this;
        english = findViewById(R.id.english_btn);
        arabic = findViewById(R.id.arabic_btn);
        english.setOnClickListener(this);
        arabic.setOnClickListener(this);

    }
// Lang null
@Override
public void finish() {
    super.finish();
    instance = null;
}

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.english_btn:
                LocalHelper.setLocale(SelectLangActivity.this, "English");
                prefs = getSharedPreferences("checkLogin", MODE_PRIVATE);
                 restoredText = prefs.getString("loginType", null);

                editor = getSharedPreferences("Lang", MODE_PRIVATE).edit();
                editor.putString("Local","english");
                editor.apply();
                if (restoredText == null) {
                        startActivity(new Intent(SelectLangActivity.this , LoginActivity.class));
                   //     SelectLangActivity.this.finish();
                    }
                    else if (restoredText.equals("loginAsUser")){
                    Intent intent = new Intent(SelectLangActivity.this , MainActivity.class);
                    intent.putExtra("check" , "home");
                        startActivity(intent);
                    }else {
                    startActivity(new Intent(SelectLangActivity.this , LoginActivity.class));
                }

                break;
            case R.id.arabic_btn:
                LocalHelper.setLocale(SelectLangActivity.this, "ar");
                prefs = getSharedPreferences("checkLogin", MODE_PRIVATE);
                restoredText = prefs.getString("loginType", null);
                editor = getSharedPreferences("Lang", MODE_PRIVATE).edit();
                editor.putString("Local","ar");
                editor.apply();
                if (restoredText == null) {
                    startActivity(new Intent(SelectLangActivity.this , LoginActivity.class));
                }
                else if (restoredText.equals("loginAsUser")){
                    Intent intent = new Intent(SelectLangActivity.this , MainActivity.class);
                    intent.putExtra("check" , "home");
                    startActivity(intent);
                }else {
                    startActivity(new Intent(SelectLangActivity.this , LoginActivity.class));
                }
                break;
        }
    }

}
