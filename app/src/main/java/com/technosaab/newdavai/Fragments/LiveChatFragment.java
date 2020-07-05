package com.technosaab.newdavai.Fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.technosaab.newdavai.Activities.MainActivity;
import com.technosaab.newdavai.Adapter.ChatRecyclerViewAdapter;
import com.technosaab.newdavai.Models.ChatHistoryItem;
import com.technosaab.newdavai.Models.Message;
import com.technosaab.newdavai.Models.StartChatMessage;
import com.technosaab.newdavai.Models.StartChatResponse;
import com.technosaab.newdavai.R;
import com.technosaab.newdavai.rest.ApiClient;
import com.technosaab.newdavai.rest.ClientInterface;


import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import io.socket.client.IO;
import io.socket.emitter.Emitter;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class LiveChatFragment extends Fragment {

    private io.socket.client.Socket mSocket;
    private RecyclerView recyclerView;
    private List<Message> messageList=new ArrayList<>();
    private ChatRecyclerViewAdapter chatRecyclerViewAdapter;
    Button chat_send_btn;
    EditText input_text;
    String chatId;
    private String userId;
    private String clientId;
    private String ClientName;
    private ImageView imageLogo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = getLayoutInflater().inflate(R.layout.fragment_live_chat, container, false);

        SharedPreferences Prefs = getActivity().getSharedPreferences("UserId", MODE_PRIVATE);
        String value = Prefs.getString("Id", null);
        if (value != null) {
            userId = Prefs.getString("Id", null);
        }
        SharedPreferences Pref = Objects.requireNonNull(getActivity()).getSharedPreferences("ClientId", MODE_PRIVATE);
        String value1 = Pref.getString("ClientId", null);
        if (value1 != null) {
            clientId = Pref.getString("ClientId", null);
        }
        SharedPreferences mPref = Objects.requireNonNull(getActivity()).getSharedPreferences("ClientName", MODE_PRIVATE);
        String name = mPref.getString("ClientName", null);
        if (name != null) {
            ClientName = mPref.getString("ClientName", null);
            ((MainActivity) getActivity()).barTitle.setText(ClientName);
        }
        imageLogo = view.findViewById(R.id.logo);
        SharedPreferences mPref1 = Objects.requireNonNull(getActivity()).getSharedPreferences("ClientLogo", MODE_PRIVATE);
        String logo = mPref1.getString("ClientLogo", null);
        if (logo != null) {
            ClientName = mPref1.getString("ClientLogo", null);
            Picasso.get().load(logo).into(imageLogo);
        }

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
            Toast.makeText(getContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

        }else {
            getChats();
        }

        recyclerView = view.findViewById(R.id.chat_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatRecyclerViewAdapter = new ChatRecyclerViewAdapter(getContext() , messageList);
        recyclerView.setAdapter(chatRecyclerViewAdapter);
        if (messageList!=null&&messageList.size()>0)
        recyclerView.smoothScrollToPosition(chatRecyclerViewAdapter.getItemCount() - 1);

        input_text=view.findViewById(R.id.input_text);
        chat_send_btn=view.findViewById(R.id.chat_send_btn);
        chat_send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
                if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
                    Toast.makeText(getContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();

                }else {
                    onStartChat();
                }

            }
        });

        try {
            mSocket = IO.socket("http://104.248.175.110");
            mSocket.on("userReceive", onUserReceive);
            mSocket.connect();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return view;
    }

    private void getChats(){
        ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
        Call<List<ChatHistoryItem>> call = clientInterface.getChatHistory(userId, clientId);
        call.enqueue(new Callback<List<ChatHistoryItem>>() {
            @Override
            public void onResponse(Call<List<ChatHistoryItem>> call, Response<List<ChatHistoryItem>> response) {
                List<ChatHistoryItem> chatHistoryItems=new ArrayList<>();
                chatHistoryItems=response.body();
                if (chatHistoryItems!=null&&chatHistoryItems.size()>0)
                {
                    messageList.clear();
                    chatId=chatHistoryItems.get(0).getChatID().getId();
                    for (ChatHistoryItem item:chatHistoryItems) {
                        Message message=new Message(item.getMsg(),item.getMsg(),item.getFrom());
                        messageList.add(message);
                    }
                    if (messageList!=null&&messageList.size()>0) {
                        chatRecyclerViewAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ChatHistoryItem>> call, Throwable t) {
               // Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                messageList.clear();
                messageList.add(new Message("", getResources().getString(R.string.defautt_msg), 2));
                chatRecyclerViewAdapter.notifyDataSetChanged();
            }

        });


    }

    private void onStartChat() {
        final String title = input_text.getText().toString().trim();
        if (!TextUtils.isEmpty(title)) {
            if (!TextUtils.isEmpty(chatId)) {
                mSocket.emit("startChat",chatId ,title);
                messageList.add(new Message(title, "", 1));
                chatRecyclerViewAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(chatRecyclerViewAdapter.getItemCount() - 1);
                input_text.setText("");
            } else {
                ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
                Call<StartChatResponse> call = clientInterface.startChat(new StartChatMessage(userId, clientId, title));
                call.enqueue(new Callback<StartChatResponse>() {
                    @Override
                    public void onResponse(Call<StartChatResponse> call, Response<StartChatResponse> response) {
                        StartChatResponse startChatResponse = response.body();
                        if (startChatResponse != null) {
                            chatId=startChatResponse.getId();
                            //messageList.add(new Message("", getResources().getString(R.string.defautt_msg), 2));
                            messageList.add(new Message(title, "", 1));
                            chatRecyclerViewAdapter.notifyDataSetChanged();
                            recyclerView.smoothScrollToPosition(chatRecyclerViewAdapter.getItemCount() - 1);
                            mSocket.emit("startChat" ,startChatResponse.getId(),title);
                            input_text.setText("");
                        }
                    }
                    @Override
                    public void onFailure(Call<StartChatResponse> call, Throwable t) {
                        //Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();

                    }
                });
            }
        }
    }
    private Emitter.Listener onUserReceive = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            final String data = (String) args[0];
            ((Activity)getActivity()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    messageList.add(new Message("",data,2));
                    chatRecyclerViewAdapter.notifyDataSetChanged();
                    recyclerView.smoothScrollToPosition(chatRecyclerViewAdapter.getItemCount() - 1);
                    Log.d("aaaaaaqaaa" , data);
                }
            });

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();

    }
}
