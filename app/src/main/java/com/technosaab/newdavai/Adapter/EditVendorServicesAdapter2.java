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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.technosaab.newdavai.Models.ClientServiceResponse2;
import com.technosaab.newdavai.Models.Employee;
import com.technosaab.newdavai.Models.ServiceInfo;
import com.technosaab.newdavai.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class EditVendorServicesAdapter2 extends RecyclerView.Adapter<EditVendorServicesAdapter2.viewHolder> {

    private static Context context;
    private List<ClientServiceResponse2> servicesList;
    private String lang;
    private ArrayList<String> st_employee;
    private List<Employee> emp;
    TextView vendorName;
    List<String> stringList;
    static List<ServiceInfo> serviceInfos = new ArrayList<>();


    public EditVendorServicesAdapter2(Context context, List<ClientServiceResponse2> servicesList ,TextView vendorName , List<String> stringList) {
        this.context = context;
        this.servicesList = servicesList;
        this.vendorName = vendorName;
        this.stringList = stringList;
        SharedPreferences Prefs = context.getSharedPreferences("Lang", MODE_PRIVATE);
        String value = Prefs.getString("Local", null);
        if (value != null) {
            lang = Prefs.getString("Local", null);
        }
    }

    @NonNull
    @Override
    public EditVendorServicesAdapter2.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_services_popup , parent , false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        if (lang.equals("english")){
            holder.serviceName.setText(servicesList.get(position).getServicesEN());
        }else if (lang.equals("ar")) {
            holder.serviceName.setText(servicesList.get(position).getServicesAr());
        }
        for (String n :stringList) {
            if (lang.equals("english")){
                if (servicesList.get(position).getServicesEN().equals(n)){
                    holder.checkBox.setChecked(true);
                }

            }else if (lang.equals("ar")) {
                if (n.equals(servicesList.get(position).getServicesAr())){
                    holder.checkBox.setChecked(true);
                }
            }

        }

        holder.servicePrice.setText(servicesList.get(position).getPrice());
        vendorName.setText(servicesList.get(position).getBrandName());


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
        holder.addEmployees.setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        return servicesList.size();
    }
    protected class viewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView servicePrice , serviceName;
        Spinner addEmployees;
        ArrayList<String> employee;
        public viewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
            servicePrice = itemView.findViewById(R.id.service_price_et);
            serviceName = itemView.findViewById(R.id.vendor_service);
            addEmployees = itemView.findViewById(R.id.add_employee_et);
            addEmployees.setPrompt(context.getResources().getString(R.string.select_employee));
            employee = new ArrayList<>();

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        ClientServiceResponse2 serviceResponse2 = servicesList.get(getAdapterPosition());
                        String serviceId = serviceResponse2.getServicesID();
                        String servicePrice = serviceResponse2.getPrice();
                        ServiceInfo info = new ServiceInfo();
                        info.setServicesID(serviceId);
                        info.setPrice(servicePrice);
                        serviceInfos.add(info);
                       // int pos = addEmployees.getSelectedItemPosition();
                        String empId =  servicesList.get(getAdapterPosition()).getEmployees().get(0).getId();

                        for (int i=0;i<serviceInfos.size();i++) {
                            if (serviceInfos.get(i).getServicesID()==servicesList.get(getAdapterPosition()).getServicesID())
                            {
                                serviceInfos.get(i).setEmployeeID(empId);
                                break;
                            }
                        }

                    }else {
                        for (int i=0;i<serviceInfos.size();i++) {
                            if (serviceInfos.get(i).getServicesID()==servicesList.get(getAdapterPosition()).getServicesID())
                            {
                                serviceInfos.remove(i);
                                break;
                            }
                        }
                        checkBox.setChecked(false);
                    }
                }
            });

            addEmployees.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int pos = addEmployees.getSelectedItemPosition();
                    String empId =  servicesList.get(getAdapterPosition()).getEmployees().get(pos).getId();

                    for (int i=0;i<serviceInfos.size();i++) {
                        if (serviceInfos.get(i).getServicesID()==servicesList.get(getAdapterPosition()).getServicesID())
                        {
                            serviceInfos.get(i).setEmployeeID(empId);
                            break;
                        }
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

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
