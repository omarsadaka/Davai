package com.technosaab.newdavai.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.technosaab.newdavai.Models.AboutApp;
import com.technosaab.newdavai.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class AboutAppRecyclerAdapter extends RecyclerView.Adapter<AboutAppRecyclerAdapter.viewHolder> {
    private List<AboutApp> aboutAppList;
    private Context context;
    private String lang;

    public AboutAppRecyclerAdapter(List<AboutApp> aboutAppList, Context context) {
        this.aboutAppList = aboutAppList;
        this.context = context;
        SharedPreferences Prefs = context.getSharedPreferences("Lang", MODE_PRIVATE);
        String value = Prefs.getString("Local", null);
        if (value != null) {
            lang = Prefs.getString("Local", null);
        }
    }

    @NonNull
    @Override
    public AboutAppRecyclerAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.about_app_item_raw , parent , false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AboutAppRecyclerAdapter.viewHolder holder, int position) {
        if (lang.equals("english")){
            holder.aboutText.setText(aboutAppList.get(position).getTitleEN());
        }else if (lang.equals("ar")) {
            holder.aboutText.setText(aboutAppList.get(position).getTitleAr());
        }

    }

    @Override
    public int getItemCount() {
        return aboutAppList.size();
    }
    public class viewHolder extends RecyclerView.ViewHolder {
        TextView aboutText;
        public viewHolder(View itemView) {
            super(itemView);
            aboutText = itemView.findViewById(R.id.about_text);
        }
    }
}
