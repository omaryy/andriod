package Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.image.R;
import com.example.image.chatActivity;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import models.Model_users;

public class Adapter_chatlist extends  RecyclerView.Adapter<Adapter_chatlist.MyHolder> {
    Context context;
    List<Model_users> usersList;
    private HashMap<String,String > lastmessagemap;

    public Adapter_chatlist(Context context, List<Model_users> usersList) {
        this.context = context;
        this.usersList = usersList;
        lastmessagemap = new HashMap<>();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_chatlist,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        final String hisuid=usersList.get(position).getUid();
        String username=usersList.get(position).getName();
        String lastmessage=lastmessagemap.get(hisuid);
        String userimage=usersList.get(position).getImage();
        holder.nametv.setText(username);
        try {
            Picasso.get().load(userimage).placeholder(R.drawable.ic_baseline_account_circle_24).into(holder.profileiv);

        }
        catch (Exception e)
        {

        }
        if(lastmessage==null||lastmessage.equals("default"))
        {
            holder.lastmessagetv.setVisibility(View.GONE);
        }
        else {
            holder.lastmessagetv.setVisibility(View.VISIBLE);
            holder.lastmessagetv.setText(lastmessage);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, chatActivity.class);
                intent.putExtra("hisuid",hisuid);
                context.startActivity(intent);
            }
        });

    }
    public  void setlastmessage(String userid,String lastmessage)
    {
        lastmessagemap.put(userid,lastmessage);
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    class MyHolder  extends RecyclerView.ViewHolder{
        ImageView profileiv;
        TextView nametv,lastmessagetv;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            profileiv=itemView.findViewById(R.id.profileivchatlist);
            nametv=itemView.findViewById(R.id.nameTvchatlist);
            lastmessagetv=itemView.findViewById(R.id.lastmessageTv);




        }
    }
}
