package com.example.loginregisterfire.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.loginregisterfire.Common.Common;
import com.example.loginregisterfire.Model.BookingInformation;
import com.example.loginregisterfire.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class BookingStep4Fragment extends Fragment {

    SimpleDateFormat simpleDateFormat;
    LocalBroadcastManager localBroadcastManager;

    Unbinder unbinder;

    @BindView(R.id.txt_booking_section_text)
    TextView txt_booking_section_text;
    @BindView(R.id.txt_booking_time_text)
    TextView txt_booking_time_text;
    @BindView(R.id.txt_hospital_address)
    TextView txt_hospital_address;
    @BindView(R.id.txt_hospital_name)
    TextView txt_hospital_name;
    @BindView(R.id.txt_hospital_open_hours)
    TextView txt_hospital_open_hours;

    @OnClick(R.id.btn_confirm)
            void confirmBooking(){

        //Create booking information
        BookingInformation bookingInformation = new BookingInformation();

        bookingInformation.setSectionId(Common.currentSection.getSectionId());
        bookingInformation.setSectionName(Common.currentSection.getName());
        bookingInformation.setHospitalId(Common.currentHospital.getHospitalId());
        bookingInformation.setHospitalName(Common.currentHospital.getName());
        bookingInformation.setHospitalAddress(Common.currentHospital.getAddress());
        bookingInformation.setTime(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
                .append(" at ")
                .append(simpleDateFormat.format(Common.currentDate.getTime())).toString());
        bookingInformation.setSlot(Long.valueOf(Common.currentTimeSlot));

        //Submit to Section document
        DocumentReference bookingDate = FirebaseFirestore.getInstance()
                .collection("AllHospital")
                .document(Common.city)
                .collection("Branch")
                .document(Common.currentHospital.getHospitalId())
                .collection("Section")
                .document(Common.currentSection.getSectionId())
                .collection(Common.simpleDateFormat.format(Common.currentDate.getTime()))
        .document(String.valueOf(Common.currentTimeSlot));

        // Write data
        bookingDate. set(bookingInformation)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        resetStaticData();
                        getActivity().finish(); //Close activity
                        Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), ""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetStaticData() {
        Common.step = 0;
        Common.currentTimeSlot = -1;
        Common.currentHospital = null;
        Common.currentSection = null;
        Common.currentDate.add(Calendar.DATE, 0);
    }

    BroadcastReceiver confirmBookingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setData();
        }
    };

    private void setData() {
        txt_booking_section_text.setText(Common.currentSection.getName());
        txt_booking_time_text.setText(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
        .append(" at ")
        .append(simpleDateFormat.format(Common.currentDate.getTime())));

        txt_hospital_address.setText(Common.currentHospital.getAddress());
        txt_hospital_name.setText(Common.currentHospital.getName());
        txt_hospital_open_hours.setText(Common.currentHospital.getOpenHours());
    }

    static BookingStep4Fragment instance;

    public static BookingStep4Fragment getInstance() {
        if (instance == null)
            instance = new BookingStep4Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(confirmBookingReceiver, new IntentFilter(Common.KEY_CONFIRM_BOOKING));
    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(confirmBookingReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View itemView = inflater.inflate(R.layout.fragment_booking_step_four,container, false);
        unbinder = ButterKnife.bind(this, itemView);

        return itemView;
    }
}
