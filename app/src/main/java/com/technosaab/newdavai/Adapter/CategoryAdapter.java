package com.technosaab.newdavai.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.technosaab.newdavai.Models.CategoryResponse;
import com.technosaab.newdavai.R;


import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.viewHolder> {
    Context context;
    List<CategoryResponse> categoryList;
    private TextView textView;
    private AlertDialog.Builder dialogBulder;
    private AlertDialog dialog;
    private String lang;
    private SharedPreferences.Editor editor;
    private String categoryTitle;

    public CategoryAdapter(Context context, List<CategoryResponse> categoryList , TextView textView) {
        this.context = context;
        this.categoryList = categoryList;
        this.textView = textView;
        SharedPreferences Prefs = context.getSharedPreferences("Lang", MODE_PRIVATE);
        String value = Prefs.getString("Local", null);
        if (value != null) {
            lang = Prefs.getString("Local", null);
        }
    }

    @NonNull
    @Override
    public CategoryAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_pupup_item , parent ,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.viewHolder holder, int position) {

        if (lang.equals("english")){
            holder.title.setText(categoryList.get(position).getTitleEN());
        }else if (lang.equals("ar")) {
            holder.title.setText(categoryList.get(position).getTitleAr());
        }

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
    public class viewHolder extends RecyclerView.ViewHolder {
        TextView title;
        RelativeLayout relativeLayout;
        public viewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_item);
            relativeLayout = itemView.findViewById(R.id.RelativeLayout);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorBrown));
                    CategoryResponse category = categoryList.get(getAdapterPosition());
                    if (lang.equals("english")){
                         categoryTitle = category.getTitleEN();
                    }else if (lang.equals("ar")) {
                         categoryTitle = category.getTitleAr();
                    }

                    textView.setText(categoryTitle);
                    String categoryId = category.getId();
                    editor = context.getSharedPreferences("CategoryId", MODE_PRIVATE).edit();
                    editor.putString("categoryId", categoryId);
                    editor.apply();

                }
            });

        }
    }
}
