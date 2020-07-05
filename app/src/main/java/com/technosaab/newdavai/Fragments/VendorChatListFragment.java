package com.technosaab.newdavai.Fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.technosaab.newdavai.Adapter.ChatRecyclerViewAdapter;
import com.technosaab.newdavai.Models.ChatHistoryItem;
import com.technosaab.newdavai.Models.Vendor;
import com.technosaab.newdavai.R;
import com.technosaab.newdavai.Models.Message;
import com.technosaab.newdavai.rest.ApiClient;
import com.technosaab.newdavai.rest.ClientInterface;


import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class VendorChatListFragment extends Fragment {
    private RecyclerView recyclerView;
    private io.socket.client.Socket mSocket;
    private EditText input_text;
    private Button send;
    private String chatId;
    private String clientId,userId;
    private ImageView cover;
    private Vendor vendorList;


    private List<Message> messageList=new ArrayList<>();
    private ChatRecyclerViewAdapter chatRecyclerViewAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = getLayoutInflater().inflate(R.layout.fragment_vendor_chat_list, container, false);

        SharedPreferences Prefs = getActivity().getSharedPreferences("getChatId", MODE_PRIVATE);
        String value = Prefs.getString("chatId", null);
        if (value != null) {
            chatId = Prefs.getString("chatId", null);
            userId = Prefs.getString("user_Id", null);
        }
//        SharedPreferences Pref = Objects.requireNonNull(getActivity()).getSharedPreferences("ClientId", MODE_PRIVATE);
//        String value1 = Pref.getString("ClientId", null);
//        if (value1 != null) {
//            clientId = Pref.getString("ClientId", null);
//        }
        SharedPreferences Prefs3 = getActivity().getSharedPreferences("UserId", MODE_PRIVATE);
        String value3 = Prefs3.getString("Id", null);
        if (value3 != null) {
            clientId = Prefs3.getString("Id", null);
        }
        recyclerView = view.findViewById(R.id.vendor_chat_list_recycler);
        input_text = view.findViewById(R.id.input_text);
        cover = view.findViewById(R.id.logo_icon);
        send = view.findViewById(R.id.chat_send_btn);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStartChat();
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
            Toast.makeText(getContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

        }else {
            getChats();
            getAboutVendor();
        }

        try {
            mSocket = IO.socket("http://104.248.175.110");

            mSocket.on("clientReceive", onClientReceive);
            mSocket.connect();
            mSocket.emit("joinRoom",chatId ,clientId);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return view;
    }
    private void getChats() {

        ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
        Call<List<ChatHistoryItem>> call = clientInterface.getChatHistory(userId, clientId);

        call.enqueue(new Callback<List<ChatHistoryItem>>() {
            @Override
            public void onResponse(Call<List<ChatHistoryItem>> call, Response<List<ChatHistoryItem>> response) {
                List<ChatHistoryItem> chatHistoryItems=new ArrayList<>();
                chatHistoryItems=response.body();
                if (chatHistoryItems!=null&&chatHistoryItems.size()>0)
                {
                    for (ChatHistoryItem item:chatHistoryItems) {
                        Message message=new Message(item.getMsg(),item.getMsg(),item.getFrom());
                        messageList.add(message);
                    }
                    if (messageList!=null&&messageList.size()>0)
                        chatRecyclerViewAdapter = new ChatRecyclerViewAdapter(getContext() , messageList);
                        recyclerView.setAdapter(chatRecyclerViewAdapter);
                    recyclerView.smoothScrollToPosition(chatRecyclerViewAdapter.getItemCount() - 1);

                }else {

                }
            }

            @Override
            public void onFailure(Call<List<ChatHistoryItem>> call, Throwable t) {
                //Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();

            }

        });


    }

    private void onStartChat() {
        final String title = input_text.getText().toString().trim();
        if (!TextUtils.isEmpty(title)) {
            if (!TextUtils.isEmpty(chatId)) {
                mSocket.emit("clientSend",title);
                messageList.add(new Message("",title, 2));
                if (chatRecyclerViewAdapter!=null) {
                    chatRecyclerViewAdapter.notifyDataSetChanged();
                }else {
                    chatRecyclerViewAdapter = new ChatRecyclerViewAdapter(getContext() , messageList);
                    recyclerView.setAdapter(chatRecyclerViewAdapter);
                }
                recyclerView.smoothScrollToPosition(chatRecyclerViewAdapter.getItemCount() - 1);
                input_text.setText("");
            }
        }
    }
    private Emitter.Listener onClientReceive = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            final String data = (String) args[0];
            ((Activity)getActivity()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    messageList.add(new Message(data, "", 1));
                    if (chatRecyclerViewAdapter != null) {
                        chatRecyclerViewAdapter.notifyDataSetChanged();
                    }else {
                        chatRecyclerViewAdapter = new ChatRecyclerViewAdapter(getContext() , messageList);
                        recyclerView.setAdapter(chatRecyclerViewAdapter);
                    }
                    recyclerView.smoothScrollToPosition(chatRecyclerViewAdapter.getItemCount() - 1);

                }
            });

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();

    }

    private void getAboutVendor(){
        ClientInterface aboutVendor = ApiClient.getClient().create(ClientInterface.class);
        Call<Vendor> call = aboutVendor.getVendorAbout(clientId );
        call.enqueue(new Callback<Vendor>() {
            @Override
            public void onResponse(@NonNull Call<Vendor> call, @NonNull Response<Vendor> response) {
                if (response.body()!=null){
                    vendorList = response.body();
                    Picasso.get().load(response.body().getCover()).into(cover);

                }
            }

            @Override
            public void onFailure(@NonNull Call<Vendor> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
            }
        });
    }
}
