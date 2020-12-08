package com.example.loginregisterfire.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginregisterfire.Adapter.MySectionAdapter;
import com.example.loginregisterfire.Common.Common;
import com.example.loginregisterfire.Common.SpacesItemDecoration;
import com.example.loginregisterfire.Model.Section;
import com.example.loginregisterfire.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BookingStep2Fragment extends Fragment {

    Unbinder unbinder;
    LocalBroadcastManager localBroadcastManager;

    @BindView(R.id.recycler_section)
    RecyclerView recycler_section;

    private BroadcastReceiver sectionDoneReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Section> sectionArrayList = intent.getParcelableArrayListExtra(Common.KEY_SECTION_LOAD_DONE);

            MySectionAdapter adapter = new MySectionAdapter(getContext(), sectionArrayList);
            recycler_section.setAdapter(adapter);

        }
    };

    static BookingStep2Fragment instance;
    public static BookingStep2Fragment getInstance() {
        if (instance == null)
            instance = new BookingStep2Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(sectionDoneReceiver, new IntentFilter(Common.KEY_SECTION_LOAD_DONE));
    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(sectionDoneReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);

         View itemView = inflater.inflate(R.layout.fragment_booking_step_two,container, false);

         unbinder = ButterKnife.bind(this, itemView);

         initView();

        return itemView;
    }

    private void initView() {
        recycler_section.setHasFixedSize(true);
        recycler_section.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recycler_section.addItemDecoration(new SpacesItemDecoration(4));
    }
}
