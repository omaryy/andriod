package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.image.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import models.Model_Chat;

public class Adapter_Chat extends RecyclerView.Adapter<Adapter_Chat.MyHoldet> {
    private static  final int msg_tupe_left=0;
    private static  final int msg_tupe_Right=1;
    Context context;
    List<Model_Chat> chatList;
    String imageUrl;
    FirebaseUser firebaseUser;


    public Adapter_Chat(Context context, List<Model_Chat> chatList, String imageUrl) {
        this.context = context;
        this.chatList = chatList;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override

    public MyHoldet onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==msg_tupe_Right)
        {
            View view =LayoutInflater.from(context).inflate(R.layout.row_chat_right,parent,false);
            return  new MyHoldet(view);

        }
        else {
            View view =LayoutInflater.from(context).inflate(R.layout.row_chat_left,parent,false);
            return  new MyHoldet(view);
        }

    }
    public  int getItemViewType(int position)
    {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(chatList.get(position).getSender().equals(firebaseUser.getUid()))
        {
            return msg_tupe_Right;
        }
        else {
            return msg_tupe_left;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHoldet holder, int position) {
        String message=chatList.get(position).getMessage();
        String time=chatList.get(position).getTime();
        Calendar cal=Calendar.getInstance(Locale.ENGLISH);
        String type=chatList.get(position).getType();
        if(type.equals("text"))
        {
   holder.messagetv.setVisibility(View.VISIBLE);
   holder.messageiv.setVisibility(View.GONE);
holder.messagetv.setText(message);
        }
        else {
            holder.messagetv.setVisibility(View.GONE);
            holder.messageiv.setVisibility(View.VISIBLE);

Picasso.get().load(message).placeholder(R.drawable.ic_image_black).into(holder.messageiv);
        }


        try {
            Picasso.get().load(imageUrl).placeholder(R.drawable.ic_baseline_account_circle_24).into(holder.profileiv);

        }
        catch (Exception e)
        {

        }
        holder.messagetv.setText(message);



    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }


    class MyHoldet extends RecyclerView.ViewHolder{

        TextView messagetv,timetv;
        ImageView profileiv,messageiv;



        public MyHoldet(@NonNull View itemView) {
            super(itemView);
            messagetv=itemView.findViewById(R.id.messaagetv);
            messageiv=itemView.findViewById(R.id.messageiv);
            timetv=itemView.findViewById(R.id.timetv);

            profileiv=itemView.findViewById(R.id.profilrimageview);


        }
    }

}
