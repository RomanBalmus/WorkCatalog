package com.rb.workcatalog.firebaseauth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rb.workcatalog.utils.AppConfig;

/**
 * Created by Programmer on 13/04/17.
 */

public class FireAuth {




    private static FireAuth _instance= null;

    public static FireAuth getInstance()
    {
        if (_instance == null)
        {
            _instance = new FireAuth();
        }
        return _instance;
    }



    public void setUser(FirebaseUser user) {
        this.user = user;
    }

    public FirebaseUser getUser() {
        return user;
    }

    FirebaseUser user;
    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    private FirebaseAuth mAuth;
    private FireAuth() {
        mAuth = FirebaseAuth.getInstance();


    }



    public void signOutUser() {

        AppConfig.getInstance().clearUp();
        setUser(null);
        mAuth.signOut();
    }
}
