package com.technosaab.newdavai.Adapter;

import android.content.Context;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import android.text.TextUtils;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.technosaab.newdavai.Models.CategoryService;
import com.technosaab.newdavai.Models.ServiceModel;
import com.technosaab.newdavai.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class OnClickCategoryAdapter extends RecyclerView.Adapter<OnClickCategoryAdapter.viewHolder> {

    static Context context;
    List<CategoryService> categoryServices;
    private String lang;
    private SharedPreferences.Editor editor;
    TextView reList, info;
    ServiceModel serviceModel;
    static List<ServiceModel> serviceModels=new ArrayList<ServiceModel>();
    public OnClickCategoryAdapter(Context context, List<CategoryService> categoryServices ) {
        this.context = context;
        serviceModels.clear();
        this.categoryServices = categoryServices;
        SharedPreferences Prefs = context.getSharedPreferences("Lang", MODE_PRIVATE);
        String value = Prefs.getString("Local", null);
        if (value != null) {
            lang = Prefs.getString("Local", null);
        }
    }


    @NonNull
    @Override
    public OnClickCategoryAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.onclick_category_popup_item , parent , false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnClickCategoryAdapter.viewHolder holder, int position) {
        if (lang.equals("english")){
            holder.vendorService.setText(categoryServices.get(position).getTitleEN());
        }else if (lang.equals("ar")) {
            holder.vendorService.setText(categoryServices.get(position).getTitleAr());
        }

    }


    @Override
    public int getItemCount() {
        return categoryServices.size();
    }
    protected class viewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView  vendorService;
        EditText addEmployees , servicePrice;
        Button buttonAdd;
        LinearLayout container;
        // LinearLayout container;
        public viewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
            servicePrice = itemView.findViewById(R.id.servicePrice_et);
            addEmployees = itemView.findViewById(R.id.addEmployee_et);
//            servicePrice.requestFocus();
            vendorService = itemView.findViewById(R.id.vendor_service);
            buttonAdd = itemView.findViewById(R.id.add);
            container = itemView.findViewById(R.id.container);
            reList = itemView.findViewById(R.id.relist);
            reList.setMovementMethod(new ScrollingMovementMethod());
            info = itemView.findViewById(R.id.info);
            info.setMovementMethod(new ScrollingMovementMethod());
            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String emp = addEmployees.getText().toString();
                    String price = servicePrice.getText().toString();
                    if (checkBox.isChecked()){
                    if (!emp.isEmpty() && !price.isEmpty()){
                        LayoutInflater layoutInflater =
                                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View addView = layoutInflater.inflate(R.layout.layout_row, null);
                        EditText textOut = addView.findViewById(R.id.textout);
                        // textOut.setAdapter(adapter);
                        textOut.setText(addEmployees.getText().toString());
                        addEmployees.setText("");
                        Button buttonRemove = addView.findViewById(R.id.remove);

                        final View.OnClickListener thisListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                info.append("thisListener called:\t" + this + "\n");
                                info.append("Remove addView: " + addView + "\n\n");
                                ((LinearLayout) addView.getParent()).removeView(addView);

                                listAllAddView();

                            }
                        };
                        buttonRemove.setOnClickListener(thisListener);
                        container.addView(addView);
                        info.append(
                                "thisListener:\t" + thisListener + "\n"
                                        + "addView:\t" + addView + "\n\n"
                        );

                        listAllAddView();
                        List<String> strings = new ArrayList<>();
                        int childCount = container.getChildCount();
                        for (int i = 0; i < childCount; i++) {
                            View thisChild = container.getChildAt(i);
                            EditText childTextView = thisChild.findViewById(R.id.textout);
                            String childTextViewValue = childTextView.getText().toString();
                            strings.add(childTextViewValue);
//                            serviceModel.setServiceEmployees(strings);

                        }
                        serviceModels.get(getAdapterPosition()).setServiceEmployees(strings);

                    }else {
                        Toast.makeText(context, context.getResources().getString(R.string.add_one_emp), Toast.LENGTH_SHORT).show();
                    }
                    }else {
                        Toast.makeText(context, context.getResources().getString(R.string.check_service), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        CategoryService categoryService = categoryServices.get(getAdapterPosition());
                        //Toast.makeText(context,categoryService.getTitleEN(),Toast.LENGTH_LONG).show();
                        String id = categoryService.getId();
                        serviceModel=new ServiceModel();
                        serviceModel.setServiceId(id);
//                        serviceModels.get(getAdapterPosition()).setServiceId(id);
//                       serviceModels.add(serviceModel);
//                        serviceModels.get(getAdapterPosition()).setServiceName(name);

                        servicePrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (!hasFocus) {
                                    if (!TextUtils.isEmpty(servicePrice.getText())) {
                                        int price = Integer.parseInt(servicePrice.getText().toString());
//                                        serviceModel = new ServiceModel();
                                        serviceModel.setServicePrice(price);

//                         serviceModels.get(getAdapterPosition()).setServicePrice(price);
                                    }
                                }
                            }
                        });
                        serviceModels.add(serviceModel);

                    }else {
                        // delete
                        serviceModels.remove(getAdapterPosition());
                        checkBox.setChecked(false);
                    }
                }
            });


        }

        private void listAllAddView() {
            reList.setText("");
            int childCount = container.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View thisChild = container.getChildAt(i);
                reList.append(thisChild + "\n");
                EditText childTextView = thisChild.findViewById(R.id.textout);
                String childTextViewValue = childTextView.getText().toString();
                reList.append("= " + childTextViewValue + "\n");
            }
        }
    }
    public static List<ServiceModel> getServiceModels() {
        return serviceModels;
    }



}
