package com.sample.firebasetutorial.fragments;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sample.firebasetutorial.R;
import com.sample.firebasetutorial.adapters.HouseAdapter;
import com.sample.firebasetutorial.models.House;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class LandLord extends Fragment {

    private Button btnAdd;
    private RecyclerView rv_houses;
    private List<House> houses;
    private HouseAdapter houseAdapter;
    private FirebaseAuth mAuth;
    private String landlordID;
    private TextView txt_nothing;
    //database reference
    private DatabaseReference databaseReference;

    public LandLord() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_landlord,container,false);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        houses = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        landlordID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("firebase-sample");

        //load interface items
        initUI(view);

    }

    private void initUI(final View mView){
        btnAdd = mView.findViewById(R.id.btnAdd);
        rv_houses = mView.findViewById(R.id.rv_houses);
        txt_nothing = mView.findViewById(R.id.txt_no_houses);
        rv_houses.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        loadData();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(mView);
            }
        });

    }

    //load data
    private void loadData(){
        databaseReference.child("landlords")
                .child(mAuth.getCurrentUser().getUid()).child("buildings")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    houses.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        House house =  snapshot1.getValue(House.class);
                        houses.add(house);
                    }
                    houseAdapter = new HouseAdapter(houses, getActivity());
                    rv_houses.setAdapter(houseAdapter);
                }
                if(houses.size() == 0){
                   txt_nothing.setVisibility(View.VISIBLE);
                }else {
                    txt_nothing.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if(houses.size() == 0){
                    txt_nothing.setVisibility(View.VISIBLE);
                }else {
                    txt_nothing.setVisibility(View.GONE);
                }
            }
        });
    }

    //show dialog
    private void showDialog(View mView){

        View dialogLayout = LayoutInflater.from(getActivity()).inflate(R.layout.landlord_dialog,null,false);
        final EditText edt_name = dialogLayout.findViewById(R.id.edt_building_name);
        final EditText edt_estate = dialogLayout.findViewById(R.id.edt_estate_name);
        Button btn_add = dialogLayout.findViewById(R.id.button);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity())
                .setView(dialogLayout)
                .setTitle("ADD HOUSE")
                .setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(edt_estate.getText()) && !TextUtils.isEmpty(edt_name.getText()) ){
                    //get id
                    String id = databaseReference.child("buildings").push().getKey();
                    House house = new House(edt_name.getText().toString(),
                            id,
                            edt_estate.getText().toString(),
                            mAuth.getCurrentUser().getUid(),
                            "https://source.unsplash.com/random");
                    databaseReference.child("buildings").child(id).setValue(house);
                    //add estate if non-existent
                    databaseReference.child("estates").child(edt_estate.getText().toString()).child("buildings").push().setValue(id);
                    //add building to estate
                    //databaseReference.child("estates").child("buildings").child(id);
                    //add actual building to specific landlord
                    databaseReference.child("landlords").child(mAuth.getCurrentUser().getUid()).child("buildings").child(id).setValue(house);
                    //add/update landlord details
                    HashMap<String,String> landlordDetails = new HashMap<>();
                    landlordDetails.put("name", mAuth.getCurrentUser().getDisplayName());
                    landlordDetails.put("email", mAuth.getCurrentUser().getEmail());
                    databaseReference.child("landlords").child(mAuth.getCurrentUser().getUid()).child("info").setValue(landlordDetails);
                    edt_name.setText("");
                    edt_name.setText("");
                    dialog.dismiss();

                }
            }
        });


    }


}
