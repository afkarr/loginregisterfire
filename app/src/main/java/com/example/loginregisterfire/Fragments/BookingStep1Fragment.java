package com.example.loginregisterfire.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginregisterfire.Adapter.MyHospitalAdapter;
import com.example.loginregisterfire.Common.Common;
import com.example.loginregisterfire.Common.SpacesItemDecoration;
import com.example.loginregisterfire.Interface.IAllHospitalLoadListener;
import com.example.loginregisterfire.Interface.IBranchLoadListener;
import com.example.loginregisterfire.Model.Hospital;
import com.example.loginregisterfire.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class BookingStep1Fragment extends Fragment implements IAllHospitalLoadListener, IBranchLoadListener {

        //Variable
        CollectionReference allHospitalRef;
        CollectionReference branchRef;

        IAllHospitalLoadListener iAllHospitalLoadListener;
        IBranchLoadListener iBranchLoadListener;

        @BindView(R.id.spinner)
        MaterialSpinner spinner;
        @BindView(R.id.recycler_hospital)
        RecyclerView recycler_hospital;

        Unbinder unbinder;

        AlertDialog dialog;

        static BookingStep1Fragment instance;

         public static BookingStep1Fragment getInstance() {
             if(instance == null)
                 instance = new BookingStep1Fragment();
            return instance;
        }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        allHospitalRef = FirebaseFirestore.getInstance().collection("AllHospital");
        iAllHospitalLoadListener = this;
        iBranchLoadListener = this;

        dialog = new SpotsDialog.Builder().setContext(getActivity()).setCancelable(false).build();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);

         View itemView = inflater.inflate(R.layout.fragment_booking_step_one,container, false);
         unbinder = ButterKnife.bind(this, itemView);

         initView();
         loadAllHospital();

         return itemView;
    }

    private void initView() {
             recycler_hospital.setHasFixedSize(true);
             recycler_hospital.setLayoutManager(new GridLayoutManager(getActivity(), 2));
             recycler_hospital.addItemDecoration(new SpacesItemDecoration(4));
    }

    private void loadAllHospital() {
        allHospitalRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            List<String> list = new ArrayList<>();
                            list.add("Please choose city");
                            for(QueryDocumentSnapshot documentSnapshot:task.getResult())
                                list.add(documentSnapshot.getId());
                            iAllHospitalLoadListener.onAllHospitalLoadSuccess(list);

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iAllHospitalLoadListener.onAllHospitalLoadFailed(e.getMessage());
            }
        });
    }

    @Override
    public void onAllHospitalLoadSuccess(List<String> areaNameList) {
        spinner.setItems(areaNameList);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if(position > 0)
                {
                    loadBranchOfCity(item.toString());
                }
                else
                    recycler_hospital.setVisibility(view.GONE);
            }
        });
    }

    private void loadBranchOfCity(String cityName) {
        dialog.show();

        Common.city = cityName;

        branchRef = FirebaseFirestore.getInstance()
                .collection("AllHospital")
                        .document(cityName)
                .collection("Branch");

        branchRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Hospital> list = new ArrayList<>();
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot documentSnapshot:task.getResult())
                    {
                        Hospital hospital = documentSnapshot.toObject(Hospital.class);
                        hospital.setHospitalId(documentSnapshot.getId());
                        list.add(hospital);
                    }
                    iBranchLoadListener.onBranchLoadSuccess(list);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iBranchLoadListener.onBranchLoadFailed(e.getMessage());
            }
        });
    }

    @Override
    public void onAllHospitalLoadFailed(String message) {
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBranchLoadSuccess(List<Hospital> hospitalList) {
        MyHospitalAdapter adapter = new MyHospitalAdapter(getActivity(), hospitalList);
        recycler_hospital.setAdapter(adapter);
        recycler_hospital.setVisibility(View.VISIBLE);

        dialog.dismiss();
    }

    @Override
    public void onBranchLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }
}
