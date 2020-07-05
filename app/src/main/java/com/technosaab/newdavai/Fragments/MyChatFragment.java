package com.technosaab.newdavai.Fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.technosaab.newdavai.Adapter.ChatRecyclerViewAdapter;
import com.technosaab.newdavai.Models.ChatHistoryItem;
import com.technosaab.newdavai.Models.UserByIdResponse;
import com.technosaab.newdavai.R;
import com.technosaab.newdavai.Models.Message;
import com.technosaab.newdavai.Util.ImageConverter;
import com.technosaab.newdavai.rest.ApiClient;
import com.technosaab.newdavai.rest.ClientInterface;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.socket.client.IO;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class MyChatFragment extends Fragment {


    private io.socket.client.Socket mSocket;
    private RecyclerView recyclerView;
    private List<Message> messageList=new ArrayList<>();
    private ChatRecyclerViewAdapter chatRecyclerViewAdapter;
    private TextView user_name;
    Button chat_send_btn;
    EditText input_text;
    String chatId;
    private String userId;
    private String clientId;
    private LinearLayout notify_layout , msg_layout;
    private ImageView user_profile;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = getLayoutInflater().inflate(R.layout.fragment_my_chat, container, false);

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
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        if ((cm != null ? cm.getActiveNetworkInfo() : null) == null) {
            Toast.makeText(getContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }else {
            getChats();
            getUserById();
        }

        recyclerView = view.findViewById(R.id.my_chat_recycler);
        user_name = view.findViewById(R.id.user_name);
        notify_layout = view.findViewById(R.id.notify_layout);
        msg_layout = view.findViewById(R.id.msg_layout);
        user_profile = view.findViewById(R.id.user_profile);
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

                onStartChat();
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
                    chatId=chatHistoryItems.get(0).getChatID().getId();
                    for (ChatHistoryItem item:chatHistoryItems) {
                        Message message=new Message(item.getMsg(),item.getMsg(),item.getFrom());
                        messageList.add(message);
                    }
                    if (messageList!=null&&messageList.size()>0)
                        chatRecyclerViewAdapter.notifyDataSetChanged();
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
                mSocket.emit("startChat",chatId ,title);
                messageList.add(new Message(title, "", 1));
                chatRecyclerViewAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(chatRecyclerViewAdapter.getItemCount() - 1);
                input_text.setText("");
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
                }
            });

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();

    }
    public void getUserById(){
        ClientInterface clientInterface = ApiClient.getClient().create(ClientInterface.class);
        Call<UserByIdResponse> call = clientInterface.getUserById(userId);
        call.enqueue(new Callback<UserByIdResponse>() {
            @Override
            public void onResponse(Call<UserByIdResponse> call, Response<UserByIdResponse> response) {
                if (response.body()!=null){

                    String userName = response.body().getFirstName();
                   user_name.setText(userName);
                    Picasso.get().load(response.body().getPersonalImg()).transform(new ImageConverter()).into(user_profile);
                }
            }

            @Override
            public void onFailure(Call<UserByIdResponse> call, Throwable t) {
                Toast.makeText(getContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
            }
        });
    }

}
