package Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;


import com.example.image.ADDPOSTSActivity;
import com.example.image.FavouriteFragment;
import com.example.image.R;
import com.example.image.ThereprofileActivity;
import com.example.image.chatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.zip.DataFormatException;

import models.ModelPosts;

public class Adapterposts  extends  RecyclerView.Adapter<Adapterposts.MyHolder> {
    Context context;
    List<ModelPosts>postsList;
    String myuid;
int position1;

    public Adapterposts(Context context, List<ModelPosts> postsList) {
        this.context = context;
        this.postsList = postsList;
        myuid= FirebaseAuth.getInstance().getCurrentUser().getUid();

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_posts,parent,false);


        return new MyHolder(view);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        final String uid=postsList.get(position).getUid();
        final String uemail=postsList.get(position).getUemail();
        final String uname=postsList.get(position).getUname();
        final String uDp=postsList.get(position).getuDp();
        final String pid=postsList.get(position).getPid();
        final String ptitle=postsList.get(position).getPtitle();
        final String city=postsList.get(position).getCity();
        final String pimage=postsList.get(position).getPimage();
        final String ptime=postsList.get(position).getPtime();
        Calendar calendar=Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(ptime));
position1=position;
        final String time= DateFormat.format("dd/MM/yyyy " + System.lineSeparator()+"hh:mm aa",calendar).toString();
        holder.unameTv.setText(uname);
        holder.PtimeTv.setText(time);

        holder.PtitleTv.setText(ptitle);

        holder.PdescriptionTv.setText(city);
        if(pimage.equals("noimage"))
        {
            holder.PimageIv.setVisibility(View.GONE);
        }

        try {
            Picasso.get().load(uDp).into(holder.upictureIv);

        } catch (Exception e) {

        }


        try {
            Picasso.get().load(pimage).into(holder.PimageIv);

        } catch (Exception e) {

        }

        holder.morebtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                    showmoreoptions(holder.morebtn,uid,myuid,pid,pimage,uemail,uname,uDp,ptitle,city,ptime);
                }
            }
        });


        holder.commentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uid.equals(myuid))
                {
                    Toast.makeText(context,"Can Not Chating With Your Self",Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent = new Intent(context, chatActivity.class);
                    intent.putExtra("uid", uid);
                    context.startActivity(intent);
                }

            }
        });
        holder.sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,ptitle);
                sendIntent.setType("text/plain");
                context.startActivity(sendIntent);

            }
        });
        holder.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        holder.profilelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ThereprofileActivity.class);
                intent.putExtra("uid",uid);
                context.startActivity(intent);


            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void showmoreoptions(ImageButton morebtn, final String uid, final String myuid, final String pid, final String pimage, final String uemail, final String uname, final String uDp, final String ptitle, final String city, String ptime) {

        PopupMenu popupMenu=new PopupMenu(context,morebtn, Gravity.END);
        if(uid.equals(myuid)) {

            popupMenu.getMenu().add(Menu.NONE, 1, 1, "Edit");



        }
        if(!uid.equals(myuid))
        {
            popupMenu.getMenu().add(Menu.NONE, 2, 2, "Add to favourite");
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id=menuItem.getItemId();

                  if (id==1)
                {
                    Intent intent=new Intent(context, ADDPOSTSActivity.class);
                    intent.putExtra("name","editpost");
                    intent.putExtra("editpostid",pid);
                    context.startActivity(intent);
                }
                else if(id==2)
                {

                    String filepathandname="";
                    final String time1=String.valueOf(System.currentTimeMillis());
                    String data1="";
                    filepathandname="Postsfavourite/"+"Postfavourite_"+time1;
                    data1="posts"+"favourite";
                    Toast.makeText(context,pimage,Toast.LENGTH_LONG).show();
                    if(!pimage.equals("noimage"))
                    {

                        HashMap<Object,String> hashMap=new HashMap<>();
                        hashMap.put("city",city);
                        hashMap.put("pid",time1);
                        hashMap.put("pimage","noimage");
                        hashMap.put("ptime",time1);
                        hashMap.put("ptitle",ptitle);
                        hashMap.put("uName",uname);
                        hashMap.put("udp",uDp);
                        hashMap.put("uemail",uemail);
                        hashMap.put("uid",uid);
                        hashMap.put("myuid",myuid);







                        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(data1);
                        databaseReference.child(time1).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {



                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                    else {


                        HashMap<Object,String> hashMap=new HashMap<>();
                        hashMap.put("city",city);
                        hashMap.put("pid",time1);
                        hashMap.put("pimage","noimage");
                        hashMap.put("ptime",time1);
                        hashMap.put("ptitle",ptitle);
                        hashMap.put("uName",uname);
                        hashMap.put("udp",uDp);
                        hashMap.put("uemail",uemail);
                        hashMap.put("uid",uid);
                        hashMap.put("myuid",myuid);







                        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(data1);
                        databaseReference.child(time1).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {



                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();

                            }
                        });
                    }


                }
                return false;
            }
        });
        popupMenu.show();

    }

    private void beginDelete(String pid, String pimage,String uname) {
        if (pimage.equals("noimage"))
        {
           // Deletewithoutimage(pid,uname);
        }
        else {
            //Deletewithimage(pid,pimage,uname);
        }

    }
/*
    private void Deletewithimage(final String pid, String pimage,String uname) {
        final ProgressDialog pd=new ProgressDialog(context);

        pd.setMessage("Deleting........");
        StorageReference storageReference= FirebaseStorage.getInstance().getReferenceFromUrl(pimage);
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Query query=FirebaseDatabase.getInstance().getReference("postsChildren").orderByChild("pid").equalTo(pid);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds:snapshot.getChildren())
                        {
                            ds.getRef().removeValue();

                        }
                        Toast.makeText(context,"Deleted successfully",Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void Deletewithoutimage(String pid,String uname) {
        final ProgressDialog pd=new ProgressDialog(context);

        pd.setMessage("Deleting........");
        Query query=FirebaseDatabase.getInstance().getReference("postsChildren").orderByChild("pid").equalTo(pid);



        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren())
                {
                    ds.getRef().removeValue();

                }
                Toast.makeText(context,"Deleted successfully",Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


 */
    @Override
    public int getItemCount() {
        return postsList.size();
    }

    class  MyHolder extends RecyclerView.ViewHolder{
        ImageView upictureIv,PimageIv;
        TextView unameTv,PtimeTv,PtitleTv,PdescriptionTv,plikeTv;
        ImageButton morebtn;
        Button favourite,commentbtn,sharebtn;
        LinearLayout profilelayout;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            upictureIv=itemView.findViewById(R.id.postpictureiv);
            PimageIv=itemView.findViewById(R.id.pimageviewiv);
            unameTv=itemView.findViewById(R.id.postnametv);
            PtimeTv=itemView.findViewById(R.id.posttimetv);
            PtitleTv=itemView.findViewById(R.id.ptitletv);
            PdescriptionTv=itemView.findViewById(R.id.pdescription);

            morebtn=itemView.findViewById(R.id.morebtn);

            commentbtn=itemView.findViewById(R.id.commentbtn);
            sharebtn=itemView.findViewById(R.id.sharebtn);
            favourite=itemView.findViewById(R.id.favourite);
            profilelayout=itemView.findViewById(R.id.profilelayout);




        }
    }
}
