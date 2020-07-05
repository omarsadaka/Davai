package com.technosaab.newdavai.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

import com.technosaab.newdavai.Models.ClientServiceResponse2;
import com.technosaab.newdavai.Models.EmployeeID;
import com.technosaab.newdavai.R;
import com.technosaab.newdavai.rest.ApiClient;
import com.technosaab.newdavai.rest.ClientInterface;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class UpdateClientServicesAdapter extends RecyclerView.Adapter<UpdateClientServicesAdapter.viewHolder> {

    static Context context;
    private String lang;
    TextView reList, info;
    static List<EmployeeID> serviceModels=new ArrayList<>();
    private List<ClientServiceResponse2> servicesList;
    private String clientId;
    private ProgressDialog dialog;

    public UpdateClientServicesAdapter(Context context, List<ClientServiceResponse2> servicesList) {
        this.context = context;
        this.servicesList = servicesList;
        //this.list = list;
        SharedPreferences Prefs = context.getSharedPreferences("Lang", MODE_PRIVATE);
        String value = Prefs.getString("Local", null);
        if (value != null) {
            lang = Prefs.getString("Local", null);
        }
        SharedPreferences Prefs2 = context.getSharedPreferences("UserId", MODE_PRIVATE);
        String value2 = Prefs2.getString("Id", null);
        if (value2 != null) {
            clientId = Prefs2.getString("Id", null);
        }
        dialog = new ProgressDialog(context);
        dialog.setMessage("Please Wait..");
    }

    @NonNull
    @Override
    public UpdateClientServicesAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.update_client_service_item , parent , false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpdateClientServicesAdapter.viewHolder holder, int position) {
        if (lang.equals("english")){
            holder.vendorService.setText(servicesList.get(position).getServicesEN());
        }else if (lang.equals("ar")) {
            holder.vendorService.setText(servicesList.get(position).getServicesAr());
        }
        holder.servicePrice.setText(servicesList.get(position).getPrice());

        //get emp id
        List<String> empId = new ArrayList<>();
        for (int i = 0 ; i<servicesList.get(position).getEmployees().size() ;i++){
            String id = servicesList.get(position).getEmployees().get(i).getId();
            empId.add(id);
            LayoutInflater layoutInflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View addView = layoutInflater.inflate(R.layout.layout_row, null);
            EditText textOut = addView.findViewById(R.id.textout);
            // textOut.setAdapter(adapter);
            textOut.setText(servicesList.get(position).getEmployees().get(i).getFullname());
            holder.container.addView(addView);
            holder.listAllAddView();

        }

    }

    @Override
    public int getItemCount() {
        return servicesList.size();
    }
    protected class viewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView  vendorService;
        EditText  servicePrice;
        Button buttonAdd , save;
        LinearLayout container;
        // LinearLayout container;
        public viewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
            servicePrice = itemView.findViewById(R.id.servicePrice_et);
           // addEmployees = itemView.findViewById(R.id.addEmployee_et);
            save = itemView.findViewById(R.id.update_service);
//            servicePrice.requestFocus();
            vendorService = itemView.findViewById(R.id.vendor_service);
            buttonAdd = itemView.findViewById(R.id.add);
            container = itemView.findViewById(R.id.container);
            reList = itemView.findViewById(R.id.relist);
            reList.setMovementMethod(new ScrollingMovementMethod());
            info = itemView.findViewById(R.id.info);
            info.setMovementMethod(new ScrollingMovementMethod());
//            buttonAdd.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String emp = addEmployees.getText().toString();
//                    String price = servicePrice.getText().toString();
//                    if (checkBox.isChecked()){
//                    if (!emp.isEmpty() && !price.isEmpty()){
//                        LayoutInflater layoutInflater =
//                                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                        final View addView = layoutInflater.inflate(R.layout.layout_row, null);
//                        EditText textOut = addView.findViewById(R.id.textout);
//                        // textOut.setAdapter(adapter);
//                        textOut.setText(addEmployees.getText().toString());
//                        addEmployees.setText("");
//                        Button buttonRemove = addView.findViewById(R.id.remove);
//
//                        final View.OnClickListener thisListener = new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                info.append("thisListener called:\t" + this + "\n");
//                                info.append("Remove addView: " + addView + "\n\n");
//                                ((LinearLayout) addView.getParent()).removeView(addView);
//
//                                listAllAddView();
//
//                            }
//                        };
//                        buttonRemove.setOnClickListener(thisListener);
//                        container.addView(addView);
//                        info.append(
//                                "thisListener:\t" + thisListener + "\n"
//                                        + "addView:\t" + addView + "\n\n"
//                        );
//
//                        listAllAddView();
//                        List<String> empName = new ArrayList<>();
//                        int childCount = container.getChildCount();
//                        for (int i = 0; i < childCount; i++) {
//                            View thisChild = container.getChildAt(i);
//                            EditText childTextView = thisChild.findViewById(R.id.textout);
//                            String childTextViewValue = childTextView.getText().toString();
//                            empName.add(childTextViewValue);
//
//
//                        }
//
//                    }else {
//                        Toast.makeText(context, context.getResources().getString(R.string.add_one_emp), Toast.LENGTH_SHORT).show();
//                    }
//                    }else {
//                        Toast.makeText(context, context.getResources().getString(R.string.check_service), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        ClientServiceResponse2 serviceResponse2 = servicesList.get(getAdapterPosition());
                        //Toast.makeText(context,serviceResponse2.getServicesEN(),Toast.LENGTH_LONG).show();
                        //String id = serviceResponse2.getEmployees().get(getAdapterPosition()).getId();


                    }else {

                        // delete
                        if (serviceModels.size()>=1) {
                            serviceModels.remove(getAdapterPosition());
                            checkBox.setChecked(false);
                        }
                    }
                }
            });
//            servicePrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    if (!hasFocus) {
//                        if (!TextUtils.isEmpty(servicePrice.getText())) {
//                            int price = Integer.parseInt(servicePrice.getText().toString());
//
//
//
//                        }
//                    }
//                }
//            });
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {
                        dialog.show();
                        servicesList.get(getAdapterPosition()).setPrice(servicePrice.getText().toString());
                        servicesList.get(getAdapterPosition()).setLogo(clientId);
                        listAllAddView();
                        ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
                        Call<ResponseBody> call = clientInterface.updateClientServices(servicesList.get(getAdapterPosition()));
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.body() != null) {
                                    Toast.makeText(context, context.getResources().getString(R.string.successfully_update), Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                    serviceModels.clear();
                                    checkBox.setChecked(false);
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(context, context.getString(R.string.server_error), Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        });
                    }else {
                        Toast.makeText(context, context.getString(R.string.check_service), Toast.LENGTH_LONG).show();

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
                servicesList.get(getAdapterPosition()).getEmployees().get(i).setFullname(childTextViewValue);
            }
        }
    }

}
