package com.example.image;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapters.Adapter_user;
import models.Model_users;

public class usersFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView recyclerView;
    Adapter_user adapter_user;
    List<Model_users> usersList;

    RecyclerView.LayoutManager layoutManager;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public usersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment usersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static usersFragment newInstance(String param1, String param2) {
        usersFragment fragment = new usersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        firebaseAuth= FirebaseAuth.getInstance();
        recyclerView=view.findViewById(R.id.users_rcycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        usersList=new ArrayList<>();
        getallusers();

        return view;
    }

    private void getallusers() {
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for( DataSnapshot ds:snapshot.getChildren())
                {
                    Model_users model_user=ds.getValue(Model_users.class);
                    if(!model_user.getUid().equals(firebaseUser.getUid()))
                    {
                        usersList.add(model_user);
                    }
                    adapter_user=new Adapter_user(getActivity(),usersList);
                    layoutManager=new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter_user);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main,menu);

       /* menu.findItem(R.id.action_add_post).setVisible(false);

        MenuItem item=menu.findItem(R.id.action_search);
        SearchView searchView= (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(!TextUtils.isEmpty(s.trim()))
                {
                    searchusers(s);
                }
                else {
                    getallusers();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(!TextUtils.isEmpty(s.trim()))
                {
                    searchusers(s);
                }
                else {
                    getallusers();
                }
                return false;
            }
        });

to
        */
        super.onCreateOptionsMenu(menu,inflater);
    }

    private void searchusers(final String query) {
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Model_users model_user = ds.getValue(Model_users.class);
                    if (!model_user.getUid().equals(firebaseUser.getUid())) {
                        if (model_user.getName().toLowerCase().contains(query.toLowerCase()) || model_user.getEmail().toLowerCase().contains(query.toLowerCase())) {
                            usersList.add(model_user);

                        }
                    }
                    adapter_user = new Adapter_user(getActivity(), usersList);

                    layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter_user);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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