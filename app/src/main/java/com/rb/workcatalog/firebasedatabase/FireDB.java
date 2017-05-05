package com.rb.workcatalog.firebasedatabase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rb.workcatalog.firebaseauth.FireAuth;
import com.rb.workcatalog.models.MappObject;
import com.rb.workcatalog.utils.AppConfig;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Programmer on 13/04/17.
 */

public class FireDB {

    private static FireDB _instance= null;

    public static FireDB getInstance()
    {
        if (_instance == null)
        {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            _instance = new FireDB();
        }
        return _instance;
    }

    public DatabaseReference getUserReference() {
        return userReference;
    }

    DatabaseReference userReference;

    public DatabaseReference getMappReference() {
        return mappReference;
    }

    DatabaseReference mappReference;

    public DatabaseReference getCfgTypesReference() {
        return cfgTypesReference;
    }

    DatabaseReference cfgTypesReference;

    public DatabaseReference getCfgLabelsReference() {
        return cfgLabelsReference;
    }

    DatabaseReference cfgLabelsReference;

    public DatabaseReference getObjectsReference() {
        return objectsReference;
    }

    DatabaseReference objectsReference;

    public DatabaseReference getPathsReference() {
        return pathsReference;
    }

    DatabaseReference pathsReference;

    public DatabaseReference getForegroundReference() {
        return foregroundSectionsReference;
    }

    DatabaseReference foregroundSectionsReference;

    public DatabaseReference getRootReference() {
        return rootReference;
    }

    DatabaseReference rootReference;

    public DatabaseReference getTabsReference() {
        return tabsReference;
    }

    DatabaseReference tabsReference;


    public FirebaseDatabase getDatabase() {
        return database;
    }

    FirebaseDatabase database;
    private FireDB() {

        database = FirebaseDatabase.getInstance();
        userReference = database.getReference("users").child(FireAuth.getInstance().getUser().getUid());
        mappReference = database.getReference("mapps").child(FireAuth.getInstance().getUser().getUid());
        objectsReference = database.getReference("objects").child(FireAuth.getInstance().getUser().getUid());
        pathsReference = database.getReference("paths").child(FireAuth.getInstance().getUser().getUid());
        foregroundSectionsReference = database.getReference("foreground");
        rootReference = database.getReference();
        String iso3loc = Locale.getDefault().getISO3Language();
        Log.e("ISO£Loc: ",iso3loc);
        cfgTypesReference = database.getReference("configurations_types").child(iso3loc);
        cfgLabelsReference = database.getReference("configurations_labels").child(iso3loc);
        tabsReference = database.getReference("app_tabs");

        mappReference.keepSynced(true);
        cfgTypesReference.keepSynced(true);
        cfgLabelsReference.keepSynced(true);
        objectsReference.keepSynced(true);
        pathsReference.keepSynced(true);
        foregroundSectionsReference.keepSynced(true);
        tabsReference.keepSynced(true);
        //parseConfiguration();

        /*UserModel user = new UserModel();
        database.getReference("users").push().setValue(user);*/

        /*database.getReference("users").child("-KjCY3Jxrh3bC639b5LR").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    UserModel user = dataSnapshot.getValue(UserModel.class);
                    Log.e("user: ",user.toString());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/





    }


    public void parseTabs(){
        AppConfig.getInstance().parseTabs(getTabsReference());

    }


    void parseConfiguration(){
        AppConfig.getInstance().setParsingLabel(true);

        AppConfig.getInstance().setParsingType(true);

        cfgLabelsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {


                    for (DataSnapshot data :
                            dataSnapshot.getChildren()) {
                        HashMap map = (HashMap)data.getValue();

                        Iterator it = map.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            AppConfig.getInstance().addLabel(pair.getValue().toString());
                            it.remove(); // avoids a ConcurrentModificationException
                        }
                    }

                    AppConfig.getInstance().setParsingLabel(false);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                AppConfig.getInstance().setParsingLabel(false);

            }
        });


        cfgTypesReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {


                    for (DataSnapshot data :
                            dataSnapshot.getChildren()) {
                        HashMap map = (HashMap)data.getValue();

                        Iterator it = map.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            AppConfig.getInstance().addType(pair.getValue().toString());
                            it.remove(); // avoids a ConcurrentModificationException
                        }
                    }

                    AppConfig.getInstance().setParsingType(false);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                AppConfig.getInstance().setParsingType(false);

            }
        });

    }



    public void saveMapp(MappObject mapp){
        mapp.setAuthor(FireAuth.getInstance().getUser().getDisplayName());
        mapp.setUser_uid(FireAuth.getInstance().getUser().getUid());
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm",Locale.getDefault());
        String date = df.format(Calendar.getInstance().getTime());
        mapp.setCreated_at(date);
        String key = mappReference.push().getKey();
        mapp.setKey_uid(key);
        mappReference.child(key).setValue(mapp);
    }


    public void rewriteMappForKey(final MappObject mapp){
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm",Locale.getDefault());
        String date = df.format(Calendar.getInstance().getTime());
        mapp.setCreated_at(date);

        String key = objectsReference.push().getKey();
        mapp.setObject_key_uid(key);
        objectsReference.child(key).setValue(mapp).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()){
                    if (transactionCompleteListener!=null){
                        transactionCompleteListener.onRewriteComplete(mapp);
                    }
                }
            }
        });
    }


    private TransactionCompleteListener transactionCompleteListener;
    public interface TransactionCompleteListener {
        abstract void onRewriteComplete(MappObject mapp);
    }
    // ALLOWS YOU TO SET LISTENER && INVOKE THE OVERIDING METHOD
    // FROM WITHIN ACTIVITY
    public void setOnTransactionCompleteListener(TransactionCompleteListener listener) {
        transactionCompleteListener = listener;
    }
    public void savePaths(final String donwloadPath, String key_uid, String nano) {

        final Map<String,Object>pat=new HashMap<>();
        pat.put(nano,donwloadPath);
        Log.e("nano:",nano);
        pathsReference.child(key_uid).updateChildren(pat);

        objectsReference.child(key_uid).child("gallery_image_path").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                }else{
                    dataSnapshot.getRef().setValue(donwloadPath);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void updateMappName(MappObject mapp) {
        Map<String,Object> child = new HashMap<>();
        child.put("name",mapp.getName());
        mappReference.child(mapp.getKey_uid()).updateChildren(child);

    }








}
