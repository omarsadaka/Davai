package com.technosaab.newdavai.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.technosaab.newdavai.Activities.MainActivity;
import com.technosaab.newdavai.Models.ClientID;
import com.technosaab.newdavai.R;
import com.technosaab.newdavai.rest.ApiClient;
import com.technosaab.newdavai.rest.ClientInterface;
import com.technosaab.newdavai.Models.FavouriteResponse;

import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static android.content.Context.MODE_PRIVATE;
public class FavouriteRecyclerAdapter extends RecyclerView.Adapter<FavouriteRecyclerAdapter.viewHolder> implements Filterable {

    Context context;
    List<FavouriteResponse> getFavourites;
    List<FavouriteResponse> filterGetFavourites;
    private SharedPreferences.Editor editor;
    private SharedPreferences Prefs;
    private String userId;



    public FavouriteRecyclerAdapter(Context context, List<FavouriteResponse> getFavourites) {
        this.context = context;
        this.getFavourites = getFavourites;
        this.filterGetFavourites = getFavourites;
        try {
            Prefs = context.getSharedPreferences("UserId", MODE_PRIVATE);
            String value = Prefs.getString("Id", null);
            if (value != null) {
                userId = Prefs.getString("Id", null);
            }
        }catch (Exception e){

        }

    }

    @NonNull
    @Override
    public FavouriteRecyclerAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_item_row , parent , false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteRecyclerAdapter.viewHolder holder, int position) {
                FavouriteResponse response = filterGetFavourites.get(position);
                ClientID clientID = response.getClientID();
                holder.serviceTitle.setText(clientID.getBrandName());
                holder.num_rate.setText(filterGetFavourites.get(position).getClientID().getTotalRate());
                Picasso.get().load(clientID.getLogo()).into(holder.serviceImage);
    }

    @Override
    public int getItemCount() {
        return filterGetFavourites.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString=constraint.toString();
                if(charString.isEmpty()){
                    filterGetFavourites = getFavourites;
                }else {
                    List<FavouriteResponse> filterdList = new ArrayList<>();
                    for (FavouriteResponse resultsBean:getFavourites) {
                        if(resultsBean.getClientID().getBrandName().toLowerCase().contains(charString.toLowerCase())){
                            filterdList.add(resultsBean);
                        }
                    }
                    filterGetFavourites = filterdList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filterGetFavourites;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filterGetFavourites = (ArrayList<FavouriteResponse>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class viewHolder extends RecyclerView.ViewHolder {



        private ImageView serviceImage , starImage , removeImage;
        private TextView serviceTitle , num_rate;
        public viewHolder(View itemView) {
            super(itemView);
            serviceImage = itemView.findViewById(R.id.home_detail_image);
            serviceTitle = itemView.findViewById(R.id.home_detail_title);
            starImage = itemView.findViewById(R.id.star_icon);
            removeImage = itemView.findViewById(R.id.remove_icon);
            num_rate = itemView.findViewById(R.id.num_rate);
            removeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FavouriteResponse favourite = getFavourites.get(getAdapterPosition());
                    ClientID clientID = favourite.getClientID();
                    String clientId = clientID.getId();
                    deleteFavourite(clientId,getAdapterPosition());

                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FavouriteResponse getFavourite = getFavourites.get(getAdapterPosition());
                    ClientID clientID = getFavourite.getClientID();
                        String ClientId = clientID.getId();
                        editor = context.getSharedPreferences("ClientId", MODE_PRIVATE).edit();
                        editor.putString("ClientId", String.valueOf(ClientId));
                        editor.apply();
                        Intent intent = new Intent(context , MainActivity.class);
                        intent.putExtra("check" , "about");
                        context.startActivity(intent);
                    }
            });

        }
    }
    private void deleteFavourite(String clientId, final int pos){
        ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
        Call<ResponseBody> call = clientInterface.deleteFavourite(userId , clientId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body()!=null){
                    Toast.makeText(context, context.getResources().getString(R.string.delete_favourite), Toast.LENGTH_LONG).show();
                    getFavourites.remove(pos);
                    notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, context.getString(R.string.server_error), Toast.LENGTH_LONG).show();
            }
        });
    }
}
