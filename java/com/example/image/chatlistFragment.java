package com.example.image;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import Adapters.Adapter_chatlist;
import models.Model_Chat;
import models.Model_users;
import models.Modelchatlist;


public class chatlistFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    RecyclerView recyclerViewchat;

    List<Modelchatlist>  chatlistlist;
    List<Model_users>usersList;
    DatabaseReference reference;
    FirebaseUser currentuser;
    Adapter_chatlist adapter_chatlist;



    public chatlistFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chatlist , container, false);
        firebaseAuth= FirebaseAuth.getInstance();
        currentuser=FirebaseAuth.getInstance().getCurrentUser();
        recyclerViewchat  = (RecyclerView) view.findViewById(R.id.recyclyerviewchat);





        chatlistlist=new ArrayList<>();


        reference= FirebaseDatabase.getInstance().getReference("chatlist").child(currentuser.getUid());


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatlistlist.clear();

                for(DataSnapshot ds:snapshot.getChildren())
                {


                    Modelchatlist  chatlist=ds.getValue(Modelchatlist.class);

                    chatlistlist.add(chatlist);

                }


                loadchats();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return  view;
    }

    private void loadchats() {

        usersList=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for(DataSnapshot ds:snapshot.getChildren())
                {

                    Model_users users=ds.getValue(Model_users.class);

                    for(Modelchatlist chatlist:chatlistlist)
                    {

                        if(users.getUid()!=null&&users.getUid().equals(chatlist.getId()))

                        {

                            usersList.add(users);
                            break;
                        }
                    }
                    //     Toast.makeText(getContext(),""+usersList.size(),Toast.LENGTH_LONG).show();
                    adapter_chatlist=new Adapter_chatlist(getContext(),usersList);
                    LinearLayoutManager manager = new LinearLayoutManager(getContext());
                    recyclerViewchat.setHasFixedSize(true);
                    recyclerViewchat.setLayoutManager(manager);
                    recyclerViewchat.setAdapter(adapter_chatlist);

                    for(int i=0;i<usersList.size();i++)
                    {
                        lastmessage(usersList.get(i).getUid());
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void lastmessage(final String uid) {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String thelastmessqage="default";
                boolean issseen=false;
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    Model_Chat chat =ds.getValue(Model_Chat.class);
                    if(chat==null)
                    {
                        continue;
                    }
                    String sender=chat.getSender();
                    String reciever=chat.getReceiver();

                    if(sender==null||reciever==null)
                    {
                        continue;
                    }
                    if (chat.getReceiver().equals(currentuser.getUid())&&chat.getSender().equals(uid)||
                            chat.getReceiver().equals(uid)&&chat.getSender().equals(currentuser.getUid()))
                    {
                        if(chat.getType().equals("image"))
                        {
                            thelastmessqage="Sent a Photo";
                        }
                        else {
                            thelastmessqage = chat.getMessage();
issseen=chat.isIsseen();

                        }
                    }
                }

                adapter_chatlist.setlastmessage(uid,thelastmessqage);
                adapter_chatlist.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    /*

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main,menu);
        menu.findItem(R.id.action_add_post).setVisible(false);

        super.onCreateOptionsMenu(menu,inflater);
    }
    public  boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        if(id==R.id.action_logout)
        {
            firebaseAuth.signOut();
            checkuserstatus();
        }
        else if(id==R.id.action_Settings)
        {
            startActivity(new Intent(getContext(),SettingActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public  void onCreate( @Nullable Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

     */
    private void checkuserstatus()
    {
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null)
        {

        }
        else {
            startActivity(new Intent(getActivity(),MainActivity.class));
            getActivity().finish();
        }
    }
}