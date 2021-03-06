package com.technosaab.newdavai.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.technosaab.newdavai.Activities.MainActivity;
import com.technosaab.newdavai.Models.ClientServiceResponse2;
import com.technosaab.newdavai.Models.Employee;
import com.technosaab.newdavai.Models.ServiceInfo;
import com.technosaab.newdavai.R;


import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ServicesRecyclerAdapter extends RecyclerView.Adapter<ServicesRecyclerAdapter.viewHolder> {
    private List<ClientServiceResponse2> servicesList;
    private static Context context;
    private TextView brandName;
    ImageView logo , cover;
    private ArrayList<String> st_employee;
    private List<Employee> emp;
    static List<ServiceInfo> serviceInfos = new ArrayList<>();
    private String lang;

    public ServicesRecyclerAdapter(List<ClientServiceResponse2> servicesList, Context context , TextView brandName ,ImageView logo , ImageView cover) {
        this.servicesList = servicesList;
        this.context = context;
        this.brandName = brandName;
        this.logo = logo;
        this.cover = cover;
        try {
            SharedPreferences Prefs = context.getSharedPreferences("Lang", MODE_PRIVATE);
            String value = Prefs.getString("Local", null);
            if (value != null) {
                lang = Prefs.getString("Local", null);
            }
        }catch (Exception e){

        }


    }

    @NonNull
    @Override
    public ServicesRecyclerAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.services_item_row , parent , false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesRecyclerAdapter.viewHolder holder, int position) {

        holder.serviceCoast.setText(servicesList.get(position).getPrice());
        holder.serviceCoast2.setText(servicesList.get(position).getPrice());
        if (lang.equals("english")){
              holder.serviceName.setText(servicesList.get(position).getServicesEN());
        }else if (lang.equals("ar")) {
            holder.serviceName.setText(servicesList.get(position).getServicesAr());
        }

        brandName.setText(servicesList.get(position).getBrandName());
        try {
            Picasso.get().load(servicesList.get(position).getLogo()).into(logo);
            Picasso.get().load(servicesList.get(position).getCover()).into(cover);
        }catch (Exception e){

        }

        ((MainActivity) context).barTitle.setText(servicesList.get(position).getBrandName());

        st_employee = new ArrayList<>();
        ClientServiceResponse2 serviceResponse2 = servicesList.get(position);
        emp = serviceResponse2.getEmployees();
         for (int i =0 ; i<emp.size() ; i++) {
             String s = emp.get(i).getFullname();
//             for (String s : emp.get(i).getFullname()) {
                st_employee.add(s);
//             }
         }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_item, st_employee);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.selectEmployee.setAdapter(adapter);
        //Toast.makeText(context, st_employee.size()+"",Toast.LENGTH_LONG).show();

    }

    @Override
    public int getItemCount() {
        return servicesList.size();
    }
    public class viewHolder extends RecyclerView.ViewHolder {
        TextView serviceName;
        TextView serviceCoast , serviceCoast2;
        TextView serviceRate;
        CheckBox checkBox;
        Spinner selectEmployee;
        RelativeLayout defaultLayout , afterCheckLayout;
        public viewHolder(View itemView) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.serviceName);
            serviceCoast = itemView.findViewById(R.id.serviceCoast);
            serviceCoast2 = itemView.findViewById(R.id.service_price2);
            //serviceRate = itemView.findViewById(R.id.rate_num);
            checkBox = itemView.findViewById(R.id.check_box);
            defaultLayout = itemView.findViewById(R.id.default_layout);
            afterCheckLayout = itemView.findViewById(R.id.after_check);
            selectEmployee = itemView.findViewById(R.id.spinner);
            selectEmployee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int pos = selectEmployee.getSelectedItemPosition();
                    String empId =  servicesList.get(getAdapterPosition()).getEmployees().get(pos).getId();
                    for (int i=0;i<serviceInfos.size();i++) {
                        if (serviceInfos.get(i).getServicesID()==servicesList.get(getAdapterPosition()).getServicesID())
                        {
                            serviceInfos.get(i).setEmployeeID(empId);
                            break;
                        }
                    }

//                    if (serviceInfos!=null)

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        defaultLayout.setVisibility(View.GONE);
                        afterCheckLayout.setVisibility(View.VISIBLE);
                        checkBox.setChecked(true);
                        ClientServiceResponse2 serviceResponse2 = servicesList.get(getAdapterPosition());
                        String serviceId = serviceResponse2.getServicesID();
                        String servicePrice = serviceResponse2.getPrice();
                        ServiceInfo info = new ServiceInfo();
                        info.setServicesID(serviceId);
                        info.setPrice(servicePrice);
                        serviceInfos.add(info);

                        //int pos = selectEmployee.getSelectedItemPosition();
                        try {
                            String empId =  servicesList.get(getAdapterPosition()).getEmployees().get(0).getId();
                            for (int i=0;i<serviceInfos.size();i++) {
                                if (serviceInfos.get(i).getServicesID()==servicesList.get(getAdapterPosition()).getServicesID())
                                {
                                    serviceInfos.get(i).setEmployeeID(empId);
                                    break;
                                }
                            }
                        }catch (Exception e){

                        }


                    }else {
                        for (int i=0;i<serviceInfos.size();i++) {
                            if (serviceInfos.get(i).getServicesID()==servicesList.get(getAdapterPosition()).getServicesID())
                            {
                                serviceInfos.remove(i);
                                break;
                            }
                        }
                        defaultLayout.setVisibility(View.VISIBLE);
                        afterCheckLayout.setVisibility(View.GONE);
                        checkBox.setChecked(false);
                    }
                }
            });

        }
    }

    public static List<ServiceInfo> getServiceModels() {
        if (serviceInfos.size()<1){
            Toast.makeText(context, context.getResources().getString(R.string.check_service), Toast.LENGTH_LONG).show();
        }
        return serviceInfos;
    }

}
