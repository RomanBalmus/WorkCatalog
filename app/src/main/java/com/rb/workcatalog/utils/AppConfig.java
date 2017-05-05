package com.rb.workcatalog.utils;

import android.util.ArrayMap;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.rb.workcatalog.R;
import com.rb.workcatalog.fragments.landingtabs.LandingListFragment;
import com.rb.workcatalog.fragments.landingtabs.LandingLocationsFragment;
import com.rb.workcatalog.models.TabItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

/**
 * Created by Programmer on 18/04/17.
 */

public class AppConfig {
    public boolean isParsingType() {
        return isParsingType;
    }

    public void setParsingType(boolean parsing) {
        isParsingType = parsing;
    }

    boolean isParsingType=false;
    public boolean isParsingLabel() {
        return isParsingLabel;
    }

    public void setParsingLabel(boolean parsing) {
        isParsingLabel = parsing;
    }

    boolean isParsingLabel=false;
    private static AppConfig _instance= null;

    public static AppConfig getInstance()
    {
        if (_instance == null)
        {
            _instance = new AppConfig();
        }
        return _instance;
    }


    ArrayList<TabItem>landingPages;

    private Map<DatabaseReference, ChildEventListener> mValueEventListeners = new ArrayMap<>();

    public ArrayList<String> getLabels() {
        Collections.sort(labels, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });
        return labels;
    }

    public ArrayList<String> getTypes() {
        Collections.sort(types, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });

        return types;
    }

    ArrayList<String>labels;

    ArrayList<String>types;

    private AppConfig() {
        types = new ArrayList<>();
        labels = new ArrayList<>();

        landingPages=new ArrayList<>();
        TabItem list=new TabItem();
        list.setTitle(R.string.foreground);
        list.setFragment(new LandingListFragment());
        landingPages.add(list);

        TabItem locations=new TabItem();
        locations.setTitle(R.string.locations);
        locations.setFragment(new LandingLocationsFragment());
        landingPages.add(locations);

/*
        tabBarPages=new ArrayList<>();
        TabItem landing=new TabItem();
        landing.setTitle(R.string.tab_landing_title);
        landing.setFragment(new LandingTabFragment());
        tabBarPages.add(landing);

        TabItem mapping=new TabItem();
        mapping.setTitle(R.string.tab_mapping_title);
        mapping.setFragment(new MappingListFragment());
        tabBarPages.add(mapping);

        TabItem userprofile=new TabItem();
        userprofile.setTitle(R.string.tab_user_profile_title);
        userprofile.setFragment(new UserProfileFragment());
        tabBarPages.add(userprofile);*/






    }



    public void parseTabs(final DatabaseReference databaseReference){



        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    ArrayList<TabItem>list = new ArrayList<TabItem>();
                    for (DataSnapshot dsnap :
                            dataSnapshot.getChildren()) {


                        Log.e("tab",""+dsnap.getValue());
                        TabItem tab = dsnap.getValue(TabItem.class);



                            list.add(tab);

                    }
                    if (tabChangeListener!=null){
                        tabChangeListener.onListComplete(list);


                    }
                   /* if (!mValueEventListeners.containsKey(databaseReference)) {
                        databaseReference.addChildEventListener(childEventListener);
                        mValueEventListeners.put(databaseReference, childEventListener);
                    }*/

                    if (!mValueEventListeners.containsKey(databaseReference)) {
                        databaseReference.addChildEventListener(childEventListener);
                        mValueEventListeners.put(databaseReference, childEventListener);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }




    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            if (dataSnapshot.exists()){
                Log.e("Add:",""+dataSnapshot.getValue());
                TabItem tab = dataSnapshot.getValue(TabItem.class);
                if (tabChangeListener!=null){
                    tabChangeListener.onAddComplete(tab);
                }
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            if (dataSnapshot.exists()){
                Log.e("Changed:",""+dataSnapshot.getValue());
                TabItem tab = dataSnapshot.getValue(TabItem.class);
                if (tabChangeListener!=null){
                    tabChangeListener.onChangeComplete(tab);
                }
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){
                Log.e("Remove:",""+dataSnapshot.getValue());
                TabItem tab = dataSnapshot.getValue(TabItem.class);
                if (tabChangeListener!=null){
                    tabChangeListener.onRemoveComplete(tab);
                }
            }
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    public void clearUp(){
        for (DatabaseReference databaseReference : mValueEventListeners.keySet()) {
            databaseReference.removeEventListener(mValueEventListeners.get(databaseReference));
        }
    }

    public void addLabel(String label){
        labels.add(label);
    }
    public void addType(String type){
        types.add(type);
    }

    public ArrayList<TabItem> getLandingPages(){
        return landingPages;
    }



    private TabChangeListener tabChangeListener;
    public interface TabChangeListener {
        abstract void onChangeComplete(TabItem tab);
        abstract void onAddComplete(TabItem tab);
        abstract void onListComplete(ArrayList<TabItem> tabs);
        abstract void onRemoveComplete(TabItem tab);
    }
    // ALLOWS YOU TO SET LISTENER && INVOKE THE OVERIDING METHOD
    // FROM WITHIN ACTIVITY
    public void setOnTabChangeListener(TabChangeListener listener) {
        tabChangeListener = listener;
    }
}
