package com.drunk.mode.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.drunk.mode.Models.AllMethods;
import com.drunk.mode.Models.Message;
import com.drunk.mode.R;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageAdapterViewHolder> {

    Context context;
    List<Message> messages;
    DatabaseReference messagedb;

    public MessageAdapter(Context context, List<Message> messages, DatabaseReference messagedb){
        this.context = context;
        this.messages = messages;
        this.messagedb = messagedb;
    }

    @NonNull
    @Override
    public MessageAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message, viewGroup, false);
        return new MessageAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapterViewHolder messageAdapterViewHolder, int i) {
        Message message = messages.get(i);

        if(message.getName().equals(AllMethods.name)){
            messageAdapterViewHolder.tvTittle.setText("You: " + message.getMessage());
            messageAdapterViewHolder.tvTittle.setGravity(Gravity.START);
            messageAdapterViewHolder.l1.setBackgroundColor(Color.parseColor("#EF9E73"));
        } else {
            messageAdapterViewHolder.tvTittle.setText(message.getName() + ":" + message.getMessage());
            messageAdapterViewHolder.ibDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessageAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView tvTittle;
        ImageButton ibDelete;
        LinearLayout l1;

        public MessageAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTittle = (TextView)itemView.findViewById(R.id.tvTitle);
            ibDelete = (ImageButton)itemView.findViewById(R.id.ibDelete);
            l1 = (LinearLayout)itemView.findViewById(R.id.l1Message);

            ibDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    messagedb.child(messages.get(getAdapterPosition()).getKey()).removeValue();

                }
            });
        }
    }
}
