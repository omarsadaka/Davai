package com.technosaab.newdavai.Helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.technosaab.newdavai.Activities.LoginActivity;
import com.technosaab.newdavai.R;

public class SkipLoginDialoge {


    static AlertDialog.Builder dialogBuilder;
    static AlertDialog dialog;

    public static void createDialoge(final Context context){

        dialogBuilder = new AlertDialog.Builder(context);
         View view = LayoutInflater.from(context).inflate(R.layout.skip_login, null);
         Button login = view.findViewById(R.id.login_btn);
         Button cancel = view.findViewById(R.id.cancel_btn);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               context.startActivity(new Intent(context , LoginActivity.class));
                ((Activity) context).finish();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              dialog.dismiss();
            }
        });

    }
}
