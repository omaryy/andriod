package Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import models.Model_users;

import com.example.image.R;
import com.example.image.ThereprofileActivity;
import com.example.image.chatActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_user extends  RecyclerView.Adapter<Adapter_user.Myholder>{
    Context context;

    List<Model_users> usersList;

    public Adapter_user(Context context, List<Model_users> usersList) {
        this.context = context;
        this.usersList = usersList;
    }


    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_users,parent,false);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        final String hisuid=usersList.get(position).getUid();
        String username=usersList.get(position).getName();
        final String useremail=usersList.get(position).getEmail();
        String userimage=usersList.get(position).getImage();
        holder.name.setText(username);
        holder.email.setText(useremail);
        try {
            Picasso.get().load(userimage).placeholder(R.drawable.ic_baseline_account_circle_24).into(holder.avatar);

        }
        catch (Exception e)
        {

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setItems(new String[]{"profile", "chat"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i==0)
                        {
                            Intent intent=new Intent(context, ThereprofileActivity.class);
                            intent.putExtra("uid",hisuid);
                            context.startActivity(intent);
                        }
                        if (i==1)
                        {
                            Intent intent=new Intent(context, chatActivity.class);
                            intent.putExtra("hisuid",hisuid);
                            context.startActivity(intent);
                        }

                    }

                });
                builder.create().show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    class Myholder extends RecyclerView.ViewHolder{
        TextView name,email;
        ImageView avatar;

        public Myholder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.nametextt);
            email=itemView.findViewById(R.id.emailtextt);
            avatar=itemView.findViewById(R.id.avatariv);

        }
    }
}
