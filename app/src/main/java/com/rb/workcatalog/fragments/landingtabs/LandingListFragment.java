package com.rb.workcatalog.fragments.landingtabs;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rb.workcatalog.MainActivity;
import com.rb.workcatalog.R;
import com.rb.workcatalog.adapters.TwoWaySectionAdapter;
import com.rb.workcatalog.firebaseauth.FireAuth;
import com.rb.workcatalog.firebasedatabase.FireDB;
import com.rb.workcatalog.fragments.main.BasicFragment;
import com.rb.workcatalog.models.ForegroundSection;
import com.rb.workcatalog.models.MappObject;

import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LandingListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LandingListFragment extends BasicFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public LandingListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LandingListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LandingListFragment newInstance(String param1, String param2) {
        LandingListFragment fragment = new LandingListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }
    ProgressBar recentProgress;
    MainActivity currentActivity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        currentActivity=(MainActivity)getActivity();
    }
    private Map<DatabaseReference, ValueEventListener> mValueEventListeners = new ArrayMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_landing_main_list, container, false);
    }
    FirebaseRecyclerAdapter adapter;
    DatabaseReference mRef;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recentProgress = (ProgressBar)view.findViewById(R.id.recentProgress);

        RecyclerView foreGroundRV = (RecyclerView)view. findViewById(R.id.foregroundRecycler);





        Query landingSectionList = FireDB.getInstance().getForegroundReference();

        mRef =FireDB.getInstance().getRootReference();


        adapter = new FirebaseRecyclerAdapter<ForegroundSection,SectionViewHolder> (ForegroundSection.class,R.layout.foreground_recycler_row, SectionViewHolder.class, landingSectionList.orderByKey()) {
            @Override
            protected void populateViewHolder(SectionViewHolder viewHolder, final ForegroundSection model, final int position) {
                if (recentProgress.getVisibility() == View.VISIBLE) {
                    recentProgress.setVisibility(View.GONE);
                }
                TextView textView = viewHolder.titleTextView;

                textView.setText(""+getStringResourceByName(model.getString_res()));
                String childPath=model.getSection_path();
                Button button = viewHolder.loadMoreButton;
                if (position > 0) {
                    button.setVisibility(View.VISIBLE);
                }else{
                    button.setVisibility(View.GONE);

                }


                final TwoWayView twoWay = (TwoWayView) viewHolder.twoWayView;
                final ArrayList<MappObject> mapps = new ArrayList<>();
                final TwoWaySectionAdapter adTwoWay = new TwoWaySectionAdapter(currentActivity, mapps);
                twoWay.setAdapter(adTwoWay);



                DatabaseReference databaseReference = mRef.child(childPath).child(FireAuth.getInstance().getUser().getUid());
                Log.e("dtt:",""+databaseReference.getRef());

                if (!mValueEventListeners.containsKey(databaseReference)) {
                    ValueEventListener valueEventListener = new ValueEventListener() {
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            adapter.notifyItemChanged(position,model);

                            if (dataSnapshot.exists()) {

                                for (DataSnapshot dtt :
                                        dataSnapshot.getChildren()) {

                                    MappObject mappObj = dtt.getValue(MappObject.class);

                                    mapps.add(mappObj);
                                }

                                Collections.reverse(mapps);

                                if (mapps.size()>0&&twoWay.getVisibility()==View.GONE){
                                    twoWay.setVisibility(View.VISIBLE);
                                }



                                adTwoWay.notifyDataSetChanged();



                            }else {
                                adTwoWay.notifyDataSetChanged();

                                if (mapps.size() > 0 && twoWay.getVisibility() == View.GONE) {
                                    twoWay.setVisibility(View.VISIBLE);
                                }
                            }

                        }

                        public void onCancelled(DatabaseError databaseError) {
                            adapter.notifyDataSetChanged();

                            adTwoWay.notifyDataSetChanged();

                            if (mapps.size()>0&&twoWay.getVisibility()==View.GONE){
                                twoWay.setVisibility(View.VISIBLE);
                            }
                        }
                    };

                    databaseReference.limitToLast(5).addValueEventListener(valueEventListener);
                    mValueEventListeners.put(databaseReference, valueEventListener);


                }

                databaseReference.limitToLast(5).addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onDataChange(DataSnapshot dataSnapshot) {



                        if (dataSnapshot.exists()) {

                            for (DataSnapshot dtt :
                                    dataSnapshot.getChildren()) {
                                //Log.e("dtt:",""+dataSnapshot.getValue());

                                MappObject mappObj = dtt.getValue(MappObject.class);

                                mapps.add(mappObj);
                            }

                            Collections.reverse(mapps);
                            adTwoWay.notifyDataSetChanged();

                            if (mapps.size()>0&&twoWay.getVisibility()==View.GONE){
                                twoWay.setVisibility(View.VISIBLE);
                            }



                        }else {
                            adTwoWay.notifyDataSetChanged();

                            if (mapps.size() > 0 && twoWay.getVisibility() == View.GONE) {
                                twoWay.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        adTwoWay.notifyDataSetChanged();

                        if (mapps.size()>0&&twoWay.getVisibility()==View.GONE){
                            twoWay.setVisibility(View.VISIBLE);
                        }
                    }


                });


            }




        };
        foreGroundRV.setHasFixedSize(true);

        foreGroundRV.setAdapter(adapter);
        foreGroundRV.setLayoutManager(new LinearLayoutManager(currentActivity));



    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adapter != null) {
            adapter.cleanup();
        }

        for (DatabaseReference databaseReference : mValueEventListeners.keySet()) {
            databaseReference.removeEventListener(mValueEventListeners.get(databaseReference));
        }
    }
    public static class SectionViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public Button loadMoreButton;
        public TwoWayView twoWayView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public SectionViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
            loadMoreButton = (Button) itemView.findViewById(R.id.loadMoreButton);
            twoWayView = (TwoWayView) itemView.findViewById(R.id.sectionTwoWayView);

        }


    }

    private String getStringResourceByName(String aString) {
        String packageName = currentActivity.getPackageName();
        int resId = getResources().getIdentifier(aString, "string", packageName);
        return getString(resId);
    }
}
