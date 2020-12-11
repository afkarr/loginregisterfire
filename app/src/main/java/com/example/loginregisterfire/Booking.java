package com.example.loginregisterfire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import com.example.loginregisterfire.Adapter.MyViewPagerAdapter;
import com.example.loginregisterfire.Common.Common;
import com.example.loginregisterfire.Common.NonSwipeViewPager;
import com.example.loginregisterfire.Model.Section;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

public class Booking extends AppCompatActivity {

    LocalBroadcastManager localBroadcastManager;
    AlertDialog dialog;
    CollectionReference sectionRef;

    @BindView(R.id.step_view)
    StepView stepView;
    @BindView(R.id.view_pager)
    NonSwipeViewPager viewPager;
    @BindView(R.id.btn_previous_step)
    Button btn_previous_step;
    @BindView(R.id.btn_next_step)
    Button btn_next_step;

    //Event
    @OnClick(R.id.btn_previous_step)
    void previousStep(){
        if(Common.step == 3 || Common.step > 0)
        {
            Common.step--;
            viewPager.setCurrentItem(Common.step);
            if(Common.step < 3)
            {
                btn_next_step.setEnabled(true);
                setColorButton();
            }
        }
    }
    @OnClick(R.id.btn_next_step)
    void nextClick(){
        if(Common.step < 3 || Common.step == 0)
        {
            Common.step++; //int
            if(Common.step == 1) //After chosen hospital
            {
                if(Common.currentHospital != null)
                    loadSectionByHospital(Common.currentHospital.getHospitalId());
            }
            else if(Common.step == 2) // Pick time slot
            {
                if(Common.currentSection != null)
                    loadTimeSlotOfSection(Common.currentSection.getSectionId());
            }
            else if(Common.step == 3) // Confirm
            {
                if(Common.currentTimeSlot != -1)
                    confirmBooking();
            }
            viewPager.setCurrentItem(Common.step);
        }
    }

    private void confirmBooking() {
        //Send broadcast to fragment step four
        Intent intent = new Intent(Common.KEY_CONFIRM_BOOKING);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void loadTimeSlotOfSection(String sectionId) {
         //Send Local Broadcast to Fragment step 3
        Intent intent = new Intent(Common.KEY_DISPLAY_TIME_SLOT);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void loadSectionByHospital(String hospitalId) {
        dialog.show();

        //load sections
        //  /AllHospital/Seremban/Branch/kf6hWUYu73mQOrNscxrf/Section
        if(!TextUtils.isEmpty(Common.city))
        {
            sectionRef = FirebaseFirestore.getInstance()
                    .collection("AllHospital")
                    .document(Common.city)
                    .collection("Branch")
                    .document(hospitalId)
                    .collection("Section");

            sectionRef.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            ArrayList<Section> sections = new ArrayList<>();
                            for(QueryDocumentSnapshot sectionSnapshot:task.getResult())
                            {
                                Section section = sectionSnapshot.toObject(Section.class);
                                section.setPassword(""); // Remove password
                                section.setSectionId(sectionSnapshot.getId());

                                sections.add(section);
                            }
                            //Send Broadcast to BookingStep2Fragment to load Fragment
                            Intent intent = new Intent(Common.KEY_SECTION_LOAD_DONE);
                            intent.putParcelableArrayListExtra(Common.KEY_SECTION_LOAD_DONE, sections);
                            localBroadcastManager.sendBroadcast(intent);

                            dialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                }
            });
        }
    }

    //Broadcast Receiver
    private BroadcastReceiver buttonNextReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int step = intent.getIntExtra(Common.KEY_STEP, 0);
            if(step == 1)
                Common.currentHospital = intent.getParcelableExtra(Common.KEY_HOSPITAL_STORE);
            else if (step == 2)
                Common.currentSection = intent.getParcelableExtra(Common.KEY_SECTION_SELECTED);
            else if (step == 3)
                Common.currentTimeSlot = intent.getIntExtra(Common.KEY_TIME_SLOT, -1);

            btn_next_step.setEnabled(true);
            setColorButton();
        }
    };


    @Override
    protected void onDestroy() {
        localBroadcastManager.unregisterReceiver(buttonNextReceiver);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        ButterKnife.bind(Booking.this);

        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(buttonNextReceiver, new IntentFilter(Common.KEY_ENABLE_BUTTON_NEXT));

        setupStepView();
        setColorButton();

        //View
        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(4); //4 fragments
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int i) {

                //step animation
                stepView.go(i, true);
                if (i == 0)
                    btn_previous_step.setEnabled(false);
                else
                    btn_previous_step.setEnabled(true);

                btn_next_step.setEnabled(false);
                setColorButton();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setColorButton() {
        if(btn_next_step.isEnabled())
        {
            btn_next_step.setBackgroundResource(R.color.colorButton);
        }
        else
        {
            btn_next_step.setBackgroundResource(android.R.color.darker_gray);
        }
        if(btn_previous_step.isEnabled())
        {
            btn_previous_step.setBackgroundResource(R.color.colorButton);
        }
        else
        {
            btn_previous_step.setBackgroundResource(android.R.color.darker_gray);
        }
    }


    private void setupStepView() {
        List<String> stepList = new ArrayList<>();
        stepList.add("Hospital");
        stepList.add("Section");
        stepList.add("Date");
        stepList.add("Confirm");
        stepView.setSteps(stepList);
    }
}