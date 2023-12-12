package com.example.antik;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<Message> messages;
    private String myUserId; // ID de l'utilisateur actuel (sender)
    private String receiverUserId; // ID du destinataire (receiver)

    public MessageAdapter(List<Message> messages, String myUserId, String receiverUserId) {
        this.messages = messages;
        this.myUserId = myUserId;
        this.receiverUserId = receiverUserId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messages.get(position);

     /*   if (message != null) {

                //holder.bindSender(message);

        }*/
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvContentSender, namesender, tvReceiverName,time;
        ImageView ivImageSender, ivImageReceiver, ivReceiverImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContentSender = itemView.findViewById(R.id.tvContent);
            ivImageSender = itemView.findViewById(R.id.ivImage);
            namesender = itemView.findViewById(R.id.namesender);
            time = itemView.findViewById(R.id.time);
           // ivImageReceiver = itemView.findViewById(R.id.ivImageReceiver);

        }

        public void bindSender(Message message) {
            //tvContentSender.setText(message.getContent());

          /*  if (message.getImageUrl() != null && !message.getImageUrl().isEmpty()) {
                Picasso.get().load(message.getImageUrl()).into(ivImageSender);
                ivImageSender.setVisibility(View.VISIBLE);
            } else {
                ivImageSender.setVisibility(View.GONE);
            }*/


        }

    }
}
