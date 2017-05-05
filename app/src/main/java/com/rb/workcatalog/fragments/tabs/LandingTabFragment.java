package com.rb.workcatalog.fragments.tabs;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rb.workcatalog.MainActivity;
import com.rb.workcatalog.R;
import com.rb.workcatalog.firebaseauth.FireAuth;
import com.rb.workcatalog.fragments.main.BasicFragment;
import com.rb.workcatalog.models.TabItem;
import com.rb.workcatalog.utils.AppConfig;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LandingTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class LandingTabFragment extends BasicFragment {
    public static final int TITLE = R.string.tab_landing_title;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    AppBarLayout appBarLayoutl;

    public LandingTabFragment() {
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LandingTabFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LandingTabFragment newInstance(String param1, String param2) {
        LandingTabFragment fragment = new LandingTabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    MainActivity currentActivity;
    FireAuth fireAuth;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        currentActivity=(MainActivity)getActivity();
        fireAuth= FireAuth.getInstance();

        Log.e("path: ",this.getClass().getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_landing_tab, container, false);
    }
     Button tstBtn;
    String ttiel = "";

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);





        appBarLayoutl=(AppBarLayout)view.findViewById(R.id.appBarLayout);
        Button tstBtn2=(Button)view.findViewById(R.id.tstBtn2);
        tstBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentActivity.logged=false;
                fireAuth.signOutUser();
            }
        });

        tstBtn=(Button)view.findViewById(R.id.tstBtn);
        tstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appBarLayoutl.setExpanded(true,true);
                //tstBtn.setVisibility(View.GONE);
               // ((EditText)view.findViewById(R.id.tstEt)).setVisibility(View.VISIBLE);
            }
        });



        appBarLayoutl.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                    

                        Log.e("offset:  ",""+verticalOffset);
                       if (isBetween(-138,-277,verticalOffset)){
                            ttiel = "1 * 4";

                            Log.e("ttl:  ",""+ttiel);
                            updateTitle();

                        }else if (isBetween(-277,-416,verticalOffset)){
                            ttiel = "1 * 3 * 4";
                            Log.e("ttl:  ",""+ttiel);
                            updateTitle();

                        }else if(isBetween(-416,-552,verticalOffset)){
                            ttiel = "1 * 2 * 3 * 4";
                            Log.e("ttl:  ",""+ttiel);
                            updateTitle();


                        }else if(isBetween(-1,-137,verticalOffset)){
                            ttiel = "1";
                            Log.e("ttl:  ",""+ttiel);
                            updateTitle();


                        }



            }

        });




        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager)view. findViewById(R.id.landing_viewpager);
        viewPager.setOffscreenPageLimit(10);
        // Create an adapter that knows which fragment should be shown on each page
        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(getFragmentManager(), AppConfig.getInstance().getLandingPages());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);
        viewPager.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });
        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.landing_tabs);
        tabLayout.setupWithViewPager(viewPager);



    }
    public static boolean isBetween(int a, int b, int c) {
        return b > a ? c > a && c < b : c > b && c < a;
    }
    void updateTitle (){

        tstBtn.post(new Runnable() {
                          public void run() {
                              appBarLayoutl.invalidate();
                              tstBtn.setText(ttiel);
                          }
                      });


    }
    public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

        ArrayList<TabItem>pages;

        public SimpleFragmentPagerAdapter(FragmentManager fm, ArrayList<TabItem> landingPages) {
            super(fm);
            pages=landingPages;
        }

        // This determines the fragment for each tab
        @Override
        public BasicFragment getItem(int position) {
            return pages.get(position).getFragment();

        }

        // This determines the number of tabs
        @Override
        public int getCount() {


            return pages.size();

        }

        // This determines the title for each tab
        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return getString(pages.get(position).getTitle());

        }

    }
}
