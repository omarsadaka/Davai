package com.technosaab.newdavai.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.technosaab.newdavai.Models.ClientServiceResponse2;
import com.technosaab.newdavai.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class EditVendorServicesAdapter extends RecyclerView.Adapter<EditVendorServicesAdapter.viewHolder> {

    private List<ClientServiceResponse2> servicesList;
    Context context;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private String lang;
    TextView brandName;
    ImageView logo , cover;
    SharedPreferences Prefs;

    public EditVendorServicesAdapter(List<ClientServiceResponse2> servicesList, Context context , TextView brandName ,ImageView logo , ImageView cover) {
        this.servicesList = servicesList;
        this.context = context;
        this.brandName = brandName;
        this.logo = logo;
        this.cover = cover;
        try {
            Prefs = context.getSharedPreferences("Lang", MODE_PRIVATE);
            String value = Prefs.getString("Local", null);
            if (value != null) {
                lang = Prefs.getString("Local", null);
            }
        }catch (Exception e){

        }

    }

    @NonNull
    @Override
    public EditVendorServicesAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_services_item_row , parent , false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditVendorServicesAdapter.viewHolder holder, int position) {
        if (lang.equals("english")){
            holder.serviceName.setText(servicesList.get(position).getServicesEN());
        }else if (lang.equals("ar")) {
            holder.serviceName.setText(servicesList.get(position).getServicesAr());
        }
        holder.serviceCoast.setText(servicesList.get(position).getPrice());
       brandName.setText(servicesList.get(0).getBrandName());
        Picasso.get().load(servicesList.get(0).getLogo()).into(logo);
        Picasso.get().load(servicesList.get(0).getCover()).into(cover);
    }

    @Override
    public int getItemCount() {
        return servicesList.size();
    }
    public class viewHolder extends RecyclerView.ViewHolder {
        TextView serviceName;
        TextView serviceCoast;
        TextView serviceRate;
        ImageView empIcon;
        public viewHolder(View itemView) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.face_mask);
            serviceCoast = itemView.findViewById(R.id.service_price);
            //serviceRate = itemView.findViewById(R.id.rate_num);
            empIcon = itemView.findViewById(R.id.emp_icon);
            empIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder = new AlertDialog.Builder(context);
                    View view = LayoutInflater.from(context).inflate(R.layout.employee_popup, null);
                    ImageView Close = view.findViewById(R.id.emp_close);
                    ListView list = view.findViewById(R.id.emp_list);
                    List<String> stringList = new ArrayList<>();
                    for (int i = 0 ; i<servicesList.get(getAdapterPosition()).getEmployees().size() ;i++){
                        String empName = servicesList.get(getAdapterPosition()).getEmployees().get(i).getFullname();
                        stringList.add(empName);
                    }
                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                            (context, android.R.layout.simple_list_item_1, stringList)


                    {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent){
                            // Get the current item from ListView
                            View view = super.getView(position,convertView,parent);

                            // Get the Layout Parameters for ListView Current Item View
                            ViewGroup.LayoutParams params = view.getLayoutParams();

                            // Set the height of the Item View
                            params.height = 80;
                            view.setLayoutParams(params);
                            return view;
                        }
                    };
                    // DataBind ListView with items from ArrayAdapter
                    list.setAdapter(arrayAdapter);
                    dialogBuilder.setView(view);
                    dialog = dialogBuilder.create();
                    dialog.show();
                    Objects.requireNonNull(dialog.getWindow()).setLayout(600, 700);
                    Close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            });


        }
    }
}
