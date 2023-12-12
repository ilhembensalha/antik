package com.example.antik;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionStateChange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class messagerie extends Fragment {

    private List<Message> messages = new ArrayList<>();
    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private EditText etMessage;

    private String myUserId, receiverUserId;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public messagerie() {
        // Required empty public constructor
    }

    public static messagerie newInstance(String param1, String param2) {
        messagerie fragment = new messagerie();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messagerie, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        etMessage = view.findViewById(R.id.etMessage);
        SharedPreferences preferences = requireContext().getSharedPreferences("AppSettings", Context.MODE_PRIVATE);

        // Retrieve user details from SharedPreferences
        myUserId = preferences.getString("id", "");
        receiverUserId = preferences.getString("iduser", "");

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
        messageAdapter = new MessageAdapter(messages, myUserId, receiverUserId);
        recyclerView.setAdapter(messageAdapter);

        // Initialize btnSend
        Button btnSend = view.findViewById(R.id.btnSend);

        // Set up click listener for btnSend
        btnSend.setOnClickListener(this::sendMessage);

       /* ApiService apiService = ApiClient.getApiService();

        // Retrieve old messages
        Call<List<Message>> call = apiService.getMessages(Integer.parseInt(myUserId), Integer.parseInt(receiverUserId));
        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (response.isSuccessful()) {
                    messages.addAll(response.body());
                    messageAdapter.notifyDataSetChanged();
                } else {
                    // Handle request errors
                    Log.e("API Error", "Failed to retrieve old messages: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                // Handle network errors
                Log.e("Network Error", "Failed to retrieve old messages: " + t.getMessage());
            }
        });
*/
        // Laravel Echo setup
        PusherOptions options = new PusherOptions();
        options.setCluster("mt1");

        Pusher pusher = new Pusher("b2b07fb3e046b7cc73c3", options);

        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                Log.i("Pusher", "State changed from " + change.getPreviousState() +
                        " to " + change.getCurrentState());
            }

            @Override
            public void onError(String message, String code, Exception e) {
                Log.e("Pusher", "Error connecting to Pusher" +
                        "\nCode: " + code +
                        "\nMessage: " + message, e);
            }
        }, ConnectionState.ALL);

        Channel channel = pusher.subscribe("my-channel");

        channel.bind("my-event", new SubscriptionEventListener() {
            @Override
            public void onEvent(PusherEvent event) {
                requireActivity().runOnUiThread(() -> {
                    Message newMessage = parseNewMessageEvent(event);
                    if (newMessage != null) {
                        messages.add(newMessage);
                        messageAdapter.notifyItemInserted(messages.size() - 1);
                    } else {
                        Log.e("Pusher Event Error", "Failed to parse new message event");
                    }
                });
            }
        });

        return view;
    }

    private Message parseNewMessageEvent(PusherEvent event) {
        try {
            Log.i("Pusher Event", "Raw event data: " + event.getData());
            // Attempt to parse event data
            JsonObject eventData = new JsonParser().parse(event.getData()).getAsJsonObject();

            // Log eventData for debugging
            Log.i("Pusher Event", "Event data: " + eventData);

            // Check if eventData is not null
            if (eventData != null && eventData.has("message")) {
                // Extract the message object
                JsonObject messageObject = eventData.getAsJsonObject("message");

                // Create a new message
                Message newMessage = new Message();

                // Update message with necessary fields
               // newMessage.setId(messageObject.get("id").getAsInt());
                newMessage.setSenderId(messageObject.get("sender_id").getAsInt());
                newMessage.setReceiverId(messageObject.get("receiver_id").getAsInt());
                newMessage.setContent(messageObject.get("content").getAsString());
                newMessage.setFormattedDate(messageObject.get("formatted_date").getAsString());

                return newMessage;
            } else {
                Log.e("Pusher Event Error", "Missing required fields in eventData");
            }
        } catch (Exception e) {
            // Handle exceptions or log errors during parsing
            e.printStackTrace();
            Log.e("Pusher Event Error", "Error parsing event data: " + e.getMessage());
        }

        return null;
    }



    public void sendMessage(View view) {
        SharedPreferences preferences = requireContext().getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        // Retrieve user details from SharedPreferences
        myUserId = preferences.getString("id", "");
        receiverUserId = preferences.getString("iduser", "");
        String messageContent = etMessage.getText().toString().trim();
        if (!messageContent.isEmpty()) {
            Message message = new Message();
            message.setSenderId(Integer.parseInt(myUserId));
            message.setReceiverId(Integer.parseInt(receiverUserId));
            message.setContent(messageContent);

            // Retrofit - Send message
            RequestBody myUser = RequestBody.create(MediaType.parse("multipart/form-data"), myUserId);
            RequestBody receiverId = RequestBody.create(MediaType.parse("multipart/form-data"), receiverUserId);
            RequestBody cont = RequestBody.create(MediaType.parse("multipart/form-data"), messageContent);
            ApiService apiService = ApiClient.getApiService();
            Call<ResponseBody> call = apiService.sendMessage(
                    receiverId,
                    myUser,
                    cont);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            // Extract the event from the response body
                            String eventJsonString = response.body().string();

                            Log.e("ok", " to send message: " + response.body().string());

                            // Parse the event string into a PusherEvent
                            PusherEvent event = new Gson().fromJson(eventJsonString, PusherEvent.class);
                            Log.e(" Event Error", "event"+String.valueOf(event));
                            // Message sent successfully

                            // The real-time message event will be received and updated in the UI
                            Message newMessage = parseNewMessageEvent(event);

                            Log.e("Pusher Event Error", "message"+String.valueOf(newMessage));

                            messages.add(newMessage);
                            messageAdapter.notifyItemInserted(messages.size() - 1);
                            etMessage.getText().clear();
                            // Récupérer le nom et l'image du sender et du receiver
                           /* loadSenderDetails( Integer.parseInt(myUserId), Integer.parseInt(receiverUserId) );*/
                        } catch (IOException e) {
                            // Handle IO exception
                        }
                    } else {
                        Log.e("API Error", "Failed to send message: " + response.message());                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // Handle network errors
                    Log.e("Network Error", "Failed to send message: " + t.getMessage());
                }
            });

        }
    }
  /*  private void loadSenderDetails(int senderId, int receiverId) {
        ApiService apiService = ApiClient.getApiService();

        // Récupérer les détails du sender
        Call<User> senderCall = apiService.getUserProfile(senderId);
        senderCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User sender = response.body();
                    // Mettre à jour les détails du sender dans le message
                    updateSenderDetails(sender);
                } else {
                    // Gérer les erreurs de requête
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Gérer les erreurs réseau
            }
        });
    }

    private void updateSenderDetails(User sender) {
        // Mettre à jour les détails du sender dans le message
        if (sender != null && !messages.isEmpty()) {
            Message lastMessage = messages.get(messages.size() - 1);
            if (lastMessage != null) {
                lastMessage.setSenderName(sender.getName());
                lastMessage.setSenderImageUrl(sender.getAvatar());
                messageAdapter.notifyItemChanged(messages.size() - 1);
            }
        }
    }

*/

}