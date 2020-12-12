package com.example.loginregisterfire;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginregisterfire.Adapter.HomeSliderAdapter;
import com.example.loginregisterfire.Adapter.LookbookAdapter;
import com.example.loginregisterfire.Interface.IBannerLoadListener;
import com.example.loginregisterfire.Interface.IBookingInfoLoadListener;
import com.example.loginregisterfire.Interface.ILookbookLoadListener;
import com.example.loginregisterfire.Model.Banner;
import com.example.loginregisterfire.Model.BookingInformation;
import com.example.loginregisterfire.Service.PicassoImageLoadingService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ss.com.bannerslider.Slider;

public class Home extends Fragment implements ILookbookLoadListener, IBannerLoadListener, IBookingInfoLoadListener {

    private Unbinder unbinder;

    ImageView home_userImage;
    TextView home_userName;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    StorageReference storageReference;

    @BindView(R.id.recycler_look_book)
    RecyclerView recycler_look_book;
    @BindView(R.id.banner_slider)
    Slider banner_slider;
    @OnClick(R.id.card_view_booking)
    void booking()
    {
        startActivity(new Intent(getActivity(), Booking.class));
    }

    @BindView(R.id.card_booking_info)
    CardView card_booking_info;
    @BindView(R.id.txt_hospital_address)
            TextView txt_hospital_address;
    @BindView(R.id.txt_hospital_section)
            TextView txt_hospital_section;
    @BindView(R.id.txt_time)
            TextView txt_time;
    @BindView(R.id.txt_time_remain)
            TextView txt_time_remain;



    //Firestore
    CollectionReference bannerRef,lookbookRef;

    //Interface
    IBannerLoadListener iBannerLoadListener;
    ILookbookLoadListener iLookbookLoadListener;
    IBookingInfoLoadListener iBookingInfoLoadListener;

    public Home() {
        bannerRef= FirebaseFirestore.getInstance().collection("Banner");
        lookbookRef=FirebaseFirestore.getInstance().collection("Lookbook");
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserBooking();
    }

    private void loadUserBooking() {

        fAuth = FirebaseAuth.getInstance();
        String userID;
        userID = fAuth.getCurrentUser().getUid();

        CollectionReference userBooking = FirebaseFirestore.getInstance()
                .collection("UserBooking") //change this later if there's an error
                .document(userID)
                .collection("Booking");

        //Get Current date
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

        Timestamp toDayTimeStamp = new Timestamp(calendar.getTime());

        userBooking
                .whereGreaterThanOrEqualTo("timestamp", toDayTimeStamp)
                .whereEqualTo("done", false)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful())
                        {
                            if(!task.getResult().isEmpty())
                            {
                                for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult())
                                {
                                    BookingInformation bookingInformation = queryDocumentSnapshot.toObject(BookingInformation.class);
                                    iBookingInfoLoadListener.onBookingInfoLoadSuccess(bookingInformation);
                                    break;
                                }
                            }
                            else
                                iBookingInfoLoadListener.onBookingInfoLoadEmpty();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);

        //Init
        Slider.init(new PicassoImageLoadingService());
        iBannerLoadListener  = this;
        iLookbookLoadListener = this;
        iBookingInfoLoadListener = this;

        //load banner
        loadBanner();
        loadLookBook();
        loadUserBooking();

        return view;


    }

    // Show user
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        home_userName = getActivity().findViewById(R.id.home_userName);
        home_userImage = getActivity().findViewById(R.id.home_userImage);
    }

    @Override
    public void onStart() {
        super.onStart();



        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();

        String userID;
        userID = fAuth.getCurrentUser().getUid();

        final DocumentReference documentReference = fStore.collection("users").document(userID);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isComplete()){
                    //Log.d("TAG", "onComplete: "+ task.getResult().getString("Username"));
                    home_userName.setText(task.getResult().getString("FullName"));
                }
            }
        });

        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(home_userImage);
            }
        });
    }

    private void loadLookBook() {
        lookbookRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Banner> lookbooks = new ArrayList<>();
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot bannerSnapShot:task.getResult()){

                        Banner banner = bannerSnapShot.toObject(Banner.class);
                        lookbooks.add(banner);
                    }
                    iLookbookLoadListener.onLookbookLoadSuccess(lookbooks);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iLookbookLoadListener.onLookbookLoadFailed(e.getMessage());
            }
        });
    }

    private void loadBanner() {
        bannerRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Banner> banners = new ArrayList<>();
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot bannerSnapShot:task.getResult()){

                        Banner banner = bannerSnapShot.toObject(Banner.class);
                        banners.add(banner);
                    }
                    iBannerLoadListener.onBannerLoadSuccess(banners);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iBannerLoadListener.onBannerLoadFailed(e.getMessage());
            }
        });
    }

    @Override
    public void onLookbookLoadSuccess(List<Banner> banners) {
        recycler_look_book.setHasFixedSize(true);
        recycler_look_book.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_look_book.setAdapter(new LookbookAdapter(getActivity(), banners));
    }

    @Override
    public void onLookbookLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBannerLoadSuccess(List<Banner> banners) {
        banner_slider.setAdapter(new HomeSliderAdapter(banners));
    }

    @Override
    public void onBannerLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBookingInfoLoadEmpty() {
        card_booking_info.setVisibility(View.GONE);
    }

    @Override
    public void onBookingInfoLoadSuccess(BookingInformation bookingInformation) {

        txt_hospital_address.setText(bookingInformation.getHospitalAddress());
        txt_hospital_section.setText(bookingInformation.getSectionName());
        txt_time.setText(bookingInformation.getTime());
        String dateRemain = DateUtils.getRelativeTimeSpanString(
                Long.valueOf(bookingInformation.getTimestamp().toDate().getTime()),
                Calendar.getInstance().getTimeInMillis(), 0).toString();

        txt_time_remain.setText(dateRemain);

        card_booking_info.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBookingInfoLoadFailed(String message) {

    }
}