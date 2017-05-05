package com.rb.workcatalog.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.rb.workcatalog.services.UploadService;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Created by Programmer on 13/04/17.
 */

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences upPref;
    SharedPreferences.Editor editor;

    SharedPreferences.Editor uEditor;
    Context _context;
    private static final String PREF_NAME = "work-catalog-pref";
    private static final String U_PREF_NAME = "u_work-catalog-pref";

    // shared pref mode
    int PRIVATE_MODE = 0;
    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        upPref = _context.getSharedPreferences(U_PREF_NAME, PRIVATE_MODE);
        uEditor = upPref.edit();

        editor = pref.edit();
    }

    public void saveObjectForKey(ArrayList<String> paths,String key_uid) {
        Gson gson = new Gson();
        String json = gson.toJson(paths);
        uEditor.putString(key_uid, json);
        uEditor.commit();
    }


    public void getAllKeys(){
        Map<String, ?> allPrefs = upPref.getAll(); //your sharedPreference
        Set<String> set = allPrefs.keySet();
        for(String key_uid : set){
            Gson gson = new Gson();
            ArrayList<String> paths = gson.fromJson(allPrefs.get(key_uid).toString(), ArrayList.class);
            Log.e("Pref:", key_uid + " : "+ paths.toString());

        }
    }

    public void searchKeysAndUpload() {
        Map<String, ?> allPrefs = upPref.getAll(); //your sharedPreference
        Set<String> set = allPrefs.keySet();
        for(String key_uid : set){
            Gson gson = new Gson();
            ArrayList<String> paths = gson.fromJson(allPrefs.get(key_uid).toString(), ArrayList.class);
            Log.e("Pref:", key_uid + " : "+ paths.toString());


            if (paths.size()==0){
                uEditor.remove(key_uid).commit();
            }

            for (String path :
                    paths) {
                _context.startService(new Intent(_context, UploadService.class)
                        .putExtra(UploadService.EXTRA_FILE_URI, path)
                        .putExtra(UploadService.EXTRA_UID_KEY, key_uid)
                        .setAction(UploadService.ACTION_UPLOAD));

                break;
            }


            break;
        }


    }

    public void removePathFromKey(String file, String key_uid) {

        Map<String, ?> allPrefs = upPref.getAll(); //your sharedPreference

        if (allPrefs.get(key_uid)!=null) {
            Gson gson = new Gson();
            ArrayList<String> paths = gson.fromJson(allPrefs.get(key_uid).toString(), ArrayList.class);
            if (paths.size() > 0) {
                paths.remove(file);
                saveObjectForKey(paths, key_uid);
            } else {
                uEditor.remove(key_uid).commit();

            }
        }



    }



    /*public  UserModel getUserModel(){

        String userString = pref.getString(USER_MODEL_STRING,"");
        if (TextUtils.isEmpty(userString)){
            return  null;
        }
        Gson gson = new Gson();
        UserModel user = gson.fromJson(userString, UserModel.class);
        return user;
    }*/
}
