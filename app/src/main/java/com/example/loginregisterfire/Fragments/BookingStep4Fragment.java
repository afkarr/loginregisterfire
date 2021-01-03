package com.example.loginregisterfire.Fragments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
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
import com.example.loginregisterfire.Interface.MyCallBack;
import com.example.loginregisterfire.Model.BookingInformation;
import com.example.loginregisterfire.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class BookingStep4Fragment extends Fragment {

    private static final String TAG = "DB";
    SimpleDateFormat simpleDateFormat;
    LocalBroadcastManager localBroadcastManager;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference storageReference;
    String donorFullName;
    String donorEmail;

    Unbinder unbinder;

    AlertDialog dialog;

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

        dialog.show();

        String startTime = Common.convertTimeSlotToString(Common.currentTimeSlot);
        String[] convertTime = startTime.split("-");

        String[] startTimeConvert = convertTime[0].split(":");
        int startHourInt = Integer.parseInt(startTimeConvert[0].trim());
        int startMinInt = Integer.parseInt(startTimeConvert[1].trim());

        Calendar bookingDateWithourHouse = Calendar.getInstance();
        bookingDateWithourHouse.setTimeInMillis(Common.bookingDate.getTimeInMillis());
        bookingDateWithourHouse.set(Calendar.HOUR_OF_DAY, startHourInt);
        bookingDateWithourHouse.set(Calendar.MINUTE, startMinInt);

        Timestamp timestamp = new Timestamp(bookingDateWithourHouse.getTime());

        //Create booking information
        BookingInformation bookingInformation = new BookingInformation();

        callUserCredential(new MyCallBack() {
            @Override
            public void onCallFullName(String fullName) {
                bookingInformation.setDonorName(fullName);
                //Log for successfully calling the interface
                Log.d(TAG, "donorFullName bookingInformation: " + fullName);
            }

            @Override
            public void onCallEmail(String Email) {
                bookingInformation.setDonorEmail(Email);
                //Log for successfully calling the interface
                Log.d(TAG, "donorEmail bookingInformation: " + Email);
            }
        });

        //store donor UID
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        String donorUID = fAuth.getCurrentUser().getUid();

        bookingInformation.setDonorUID(donorUID);
        bookingInformation.setTimestamp(timestamp);
        bookingInformation.setDone(false);
        bookingInformation.setSectionId(Common.currentSection.getSectionId());
        bookingInformation.setSectionName(Common.currentSection.getName());
        bookingInformation.setHospitalId(Common.currentHospital.getHospitalId());
        bookingInformation.setHospitalName(Common.currentHospital.getName());
        bookingInformation.setHospitalAddress(Common.currentHospital.getAddress());
        bookingInformation.setTime(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
                .append(" at ")
                .append(simpleDateFormat.format(bookingDateWithourHouse.getTime())).toString());
        bookingInformation.setSlot(Long.valueOf(Common.currentTimeSlot));

        //Submit to Section document
        DocumentReference bookingDate = FirebaseFirestore.getInstance()
                .collection("AllHospital")
                .document(Common.city)
                .collection("Branch")
                .document(Common.currentHospital.getHospitalId())
                .collection("Section")
                .document(Common.currentSection.getSectionId())
                .collection(Common.simpleDateFormat.format(Common.bookingDate.getTime()))
        .document(String.valueOf(Common.currentTimeSlot));

        // Write data
        bookingDate. set(bookingInformation)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        addToUserBooking(bookingInformation);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), ""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callUserCredential(MyCallBack myCallBack) {
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        String userID = fAuth.getCurrentUser().getUid();


        DocumentReference docRef = fStore
                .collection("users")
                .document(userID);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){

                        //Log for successfully fetching data from firebase
                        Log.d(TAG,"User full name: " + document.getString("FullName"));
                        Log.d(TAG, "User email: " + document.getString("Email"));

                        String fullName = document.getString("FullName");
                        String eMail = document.getString("Email");

                        //Log for successfully setting the data into the variables
                        Log.d(TAG, "donorFullName set: " + fullName);
                        Log.d(TAG, "donorEmail set: " + eMail);

                        //Interface callback
                        myCallBack.onCallFullName(fullName);
                        myCallBack.onCallEmail(eMail);

                    }
                }
            }
        });
    }


    // add Scoring method
//    private void addScore() {
//        fAuth = FirebaseAuth.getInstance();
//        fStore = FirebaseFirestore.getInstance();
//        String uId;
//        uId = fAuth.getCurrentUser().getUid();
//
//         DocumentReference documentReference = fStore
//                .collection("users")
//                .document(uId);
//
//         documentReference.update("Score", FieldValue.increment(10));
//         documentReference.update("BloodDonated", FieldValue.increment(1));
//    }

    private void addToUserBooking(BookingInformation bookingInformation) {

        fAuth = FirebaseAuth.getInstance();
        String userID;
        userID = fAuth.getCurrentUser().getUid();

        //New collection
        CollectionReference userBooking = FirebaseFirestore.getInstance()
                .collection("UserBooking") //change this later if there's an error
                .document(userID)
                .collection("Booking");

        userBooking.whereEqualTo("done", false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.getResult().isEmpty())
                        {
                            //Set Data
                            userBooking.document()
                                    .set(bookingInformation)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            //addScore();

                                            if(dialog.isShowing())
                                                dialog.dismiss();

                                            addToCalendar(Common.bookingDate,
                                                    Common.convertTimeSlotToString(Common.currentTimeSlot));

                                            resetStaticData();
                                            getActivity().finish(); //Close activity
                                            Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            if(dialog.isShowing())
                                                dialog.dismiss();
                                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        else
                        {
                            if(dialog.isShowing())
                                dialog.dismiss();

                            resetStaticData();
                            getActivity().finish(); //Close activity
                            Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void addToCalendar(Calendar bookingDate, String startDate) {
        String startTime = Common.convertTimeSlotToString(Common.currentTimeSlot);
        String[] convertTime = startTime.split("-");

        String[] startTimeConvert = convertTime[0].split(":");
        int startHourInt = Integer.parseInt(startTimeConvert[0].trim());
        int startMinInt = Integer.parseInt(startTimeConvert[1].trim());

        String[] endTimeConvert = convertTime[1].split(":");
        int endHourInt = Integer.parseInt(endTimeConvert[0].trim());
        int endMinInt = Integer.parseInt(endTimeConvert[1].trim());

        Calendar startEvent = Calendar.getInstance();
        startEvent.setTimeInMillis(bookingDate.getTimeInMillis());
        startEvent.set(Calendar.HOUR_OF_DAY,startHourInt);
        startEvent.set(Calendar.MINUTE,startMinInt);

        Calendar endEvent = Calendar.getInstance();
        endEvent.setTimeInMillis(bookingDate.getTimeInMillis());
        endEvent.set(Calendar.HOUR_OF_DAY,endHourInt);
        endEvent.set(Calendar.MINUTE,endMinInt);

        SimpleDateFormat calendarDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String startEventTime = calendarDateFormat.format(startEvent.getTime());
        String endEventTime = calendarDateFormat.format(endEvent.getTime());

        addToDeviceCalendar(startEventTime, endEventTime, "Donation Booking",
                new StringBuilder("Donation from")
        .append(startTime)
        .append(" with ")
        .append(Common.currentSection.getName())
        .append(" at ")
        .append(Common.currentHospital.getName()).toString(),
                new StringBuilder("Address: ").append(Common.currentHospital.getAddress()).toString());
    }

    private void addToDeviceCalendar(String startEventTime, String endEventTime, String title, String description, String location) {

        SimpleDateFormat calendarDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        try{
            Date start = calendarDateFormat.parse(startEventTime);
            Date end = calendarDateFormat.parse(endEventTime);

            ContentValues event = new ContentValues();


            //Put
            event.put(CalendarContract.Events.CALENDAR_ID, getCalendar(getContext()));
            event.put(CalendarContract.Events.TITLE,title);
            event.put(CalendarContract.Events.DESCRIPTION, description);
            event.put(CalendarContract.Events.EVENT_LOCATION, location);

            //Time
            event.put(CalendarContract.Events.DTSTART, start.getTime());
            event.put(CalendarContract.Events.DTEND, end.getTime());
            event.put(CalendarContract.Events.ALL_DAY, 0);
            event.put(CalendarContract.Events.HAS_ALARM, 1);

            String timeZone = TimeZone.getDefault().getID();
            event.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone);


            Uri calendars;
                if(Build.VERSION.SDK_INT >= 8)
                    calendars = Uri.parse("content://com.android.calendar/events");
                else
                    calendars = Uri.parse("content://calendar/events");


            getActivity().getContentResolver().insert(calendars, event);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getCalendar(Context context) {
        String gmailIdCalendar = "";
        String projection[]={"_id", "calendar_displayName"};
        Uri calendars = Uri.parse("content://com.android.calendar/calendars");

        ContentResolver contentResolver = context.getContentResolver();
        Cursor managedCursor = contentResolver.query(calendars, projection, null, null, null);
                if(managedCursor.moveToFirst())
                {
                    String calName;
                    int nameCol = managedCursor.getColumnIndex(projection[1]);
                    int idCol = managedCursor.getColumnIndex(projection[0]);
                    do {
                        calName = managedCursor.getString(nameCol);
                        if(calName.contains("@gmail.com"))
                        {
                            gmailIdCalendar = managedCursor.getString(idCol);
                            break;
                        }
                    }while (managedCursor.moveToNext());
                    managedCursor.close();
                }
                return gmailIdCalendar;
    }

    private void resetStaticData() {
        Common.step = 0;
        Common.currentTimeSlot = -1;
        Common.currentHospital = null;
        Common.currentSection = null;
        Common.bookingDate.add(Calendar.DATE, 0);
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
        .append(simpleDateFormat.format(Common.bookingDate.getTime())));

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

        dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false)
                .build();
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
