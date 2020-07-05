package com.technosaab.newdavai.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.technosaab.newdavai.Models.TermsModel;
import com.technosaab.newdavai.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class TermsConditionAdapter extends RecyclerView.Adapter<TermsConditionAdapter.viewHolder> {

    private Context context;
    private List<TermsModel> termsModels;
    private SharedPreferences prefs;
    private String lang;

    public TermsConditionAdapter(Context context, List<TermsModel> termsModels) {
        this.context = context;
        this.termsModels = termsModels;
        prefs = context.getSharedPreferences("Lang", MODE_PRIVATE);
        String value = prefs.getString("Local", null);
        if (value != null) {
            lang = prefs.getString("Local", null);
        }
    }

    @NonNull
    @Override
    public TermsConditionAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.terms_item_raw , parent , false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TermsConditionAdapter.viewHolder holder, int position) {
        if (lang.equals("english")){
            holder.termsText.setText(termsModels.get(position).getTitleEN());
        }else if (lang.equals("ar")) {
            holder.termsText.setText(termsModels.get(position).getTitleAr());
        }
    }

    @Override
    public int getItemCount() {
        return termsModels.size();
    }
    public class viewHolder extends RecyclerView.ViewHolder {
        TextView termsText;
        public viewHolder(View itemView) {
            super(itemView);
            termsText = itemView.findViewById(R.id.terms_text);
        }
    }
}
