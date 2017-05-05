package com.rb.workcatalog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.rb.workcatalog.firebaseauth.FireAuth;
import com.rb.workcatalog.firebasedatabase.FireDB;
import com.rb.workcatalog.fragments.auth.CreateAccountNameFragment;
import com.rb.workcatalog.fragments.auth.CreateAccountPasswordFragment;
import com.rb.workcatalog.fragments.auth.OtherOptionFragment;
import com.rb.workcatalog.fragments.auth.PasswordForgotFragment;
import com.rb.workcatalog.fragments.auth.WelcomeFragment;
import com.rb.workcatalog.fragments.main.BasicFragment;
import com.rb.workcatalog.models.AppAccount;
import com.rb.workcatalog.models.TabItem;
import com.rb.workcatalog.services.TimerService;
import com.rb.workcatalog.utils.AppConfig;
import com.rb.workcatalog.utils.PrefManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PasswordForgotFragment.PasswordForgotListener,OtherOptionFragment.OtherOptionListener,CreateAccountPasswordFragment.CreateAccountListener,GoogleApiClient.OnConnectionFailedListener,AppConfig.TabChangeListener {
    PrefManager prefManager;
    private FireAuth mAuth;
    private static final int RC_SIGN_IN = 9001;
    private CallbackManager mCallbackManager;
    private GoogleApiClient mGoogleApiClient;
    AppAccount account=null;
    public boolean logged=false;
// test dev deploy
    //////////////////////////////////
///
/// activity life
///
/// START
/// //////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        mainProgress = (ProgressBar)findViewById(R.id.mainProgress);



        showProgressDialog();
        prefManager = new PrefManager(this);
        mAuth = FireAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

       //checkSignedUSer();

        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.e("Main","onSuccess");

                        // App code
                        handleFacebookAccessToken(loginResult.getAccessToken());

                    }

                    @Override
                    public void onCancel() {
                        Log.e("Main","onCancel");

                        // App code
                        showLoginError(Resources.getSystem().getString(R.string.action_cancelled));


                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.e("Main","onError");

                        showLoginError(exception.getLocalizedMessage());



                    }
                });


    }


    FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            mAuth.setUser(user);
            if (!logged) {
                checkSignedUSer();
            }



        }
    };

    void checkSignedUSer(){
        Log.e("Main","checkSignedUSer");
        if (mAuth.getUser()!=null){
            //getSupportActionBar().show();
            //mAuth.signOutUser();
            enableApp();
            logged=true;
            Log.e("REPLACE","ENABLE");


        }else{



           // toggleHideyBar();
            Log.e("REPLACE","WELCOME");
            slideReplaceToWelcomeFragment(new WelcomeFragment());
            logged=false;

        }
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.getmAuth().addAuthStateListener(mAuthListener);
// Register receiver for uploads and downloads



    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
        AppConfig.getInstance().clearUp();

    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
        if (mAuthListener != null) {
            mAuth.getmAuth().removeAuthStateListener(mAuthListener);
        }

    }
    ProgressBar mainProgress;
    ArrayList<TabItem>tabsArray = new ArrayList<>();
    SimpleFragmentPagerAdapter adapter;



    void enableApp() {



        Fragment f = getSupportFragmentManager().findFragmentById(R.id.main_content);
        if (f instanceof WelcomeFragment){
            // do something with f
            removeFragment(f);
        }
        //replaceToFragment(new MainTabsFragment());
        ((View)findViewById(R.id.content_app)).setVisibility(View.VISIBLE);

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.content_viewpager);

        //tabsArray.addAll(AppConfig.getInstance().getTabBarPages());
        // Create an adapter that knows which fragment should be shown on each page
        adapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(),tabsArray);

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
        viewPager.setOffscreenPageLimit(10);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        hideProgressDialog();

        startService(new Intent(this, TimerService.class));

        Log.e("MAIN","enableApp(9:"+tabsArray);
        AppConfig.getInstance().setOnTabChangeListener(this);
        FireDB.getInstance().parseTabs();
    }

    @Override
    public void onChangeComplete(TabItem tab) {

            for (TabItem tItem :
                    tabsArray) {
                if (tItem.getKey_uid().equalsIgnoreCase(tab.getKey_uid())){

                    int index = tabsArray.indexOf(tItem);
                    tabsArray.set(index, tab);

                    onlyVisibleItems();

                    break;
                }
            }



    }

    @Override
    public void onListComplete(ArrayList<TabItem> tabs) {

            if (tabsArray.size()==0){

                tabsArray.addAll(tabs);

            }
        onlyVisibleItems();





    }

    @Override
    public void onAddComplete(TabItem tab) {

      /*  for (TabItem tItem :
                tabsArray) {
            if (!tItem.getKey_uid().equalsIgnoreCase(tab.getKey_uid())){

                Log.e("dont exist",""+tab);
                tabsArray.add(tab);



            }
        }*/
        Log.e("tabs1",""+tabsArray);

        boolean dontexists=true;
        for (TabItem innerTab :
                tabsArray) {

            if(TextUtils.equals(innerTab.getKey_uid(),tab.getKey_uid())){
                Log.e("is the same","tab "+innerTab.getKey_uid());
                Log.e("is the same","tab "+tab.getKey_uid());

                dontexists = false;
                break;
            }
        }


        if (dontexists){
            tabsArray.add(tab);
            onlyVisibleItems();
        }


    }

    @Override
    public void onRemoveComplete(TabItem tab) {

        for (TabItem tItem :
                tabsArray) {
            if (tItem.getKey_uid().equalsIgnoreCase(tab.getKey_uid())){

                int index = tabsArray.indexOf(tItem);
                tabsArray.remove(index);

                onlyVisibleItems();

                break;
            }
        }
        onlyVisibleItems();
    }


    void onlyVisibleItems(){


        adapter.notifyDataSetChanged();
        mainProgress.setVisibility(View.GONE);
        Log.e("tabs2",""+tabsArray);


    }





    @Override
    public void onBackPressed() {
        super.onBackPressed();
        PrefManager pref = new PrefManager(this);
        pref.getAllKeys();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                handleGoogleAccessToken(account);
            } else {
                showLoginError(result.getStatus().getStatusMessage());
            }

            return;
        }
        if(mCallbackManager.onActivityResult(requestCode, resultCode, data)) {
            return;
        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

       showLoginError( connectionResult.getErrorMessage());

    }

    //////////////////////////////////
///
/// progress dialog for loading
///
/// START
/// //////////////////////////////


    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }



//////////////////////////////////
///
/// fragment slide
///
/// START
/// //////////////////////////////
    private void removeFragment(Fragment fragment) {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.remove( fragment)
            .commit();
    hideProgressDialog();

    }
    public void backStackSlideReplaceToFragment (Fragment fragment){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment.setEnterTransition(new Slide(Gravity.END));
        fragment.setExitTransition(new Slide(Gravity.START));
        transaction.replace(R.id.main_content, fragment)
                .addToBackStack(fragment.getClass().getName())
                .commit();
        hideProgressDialog();

    }
    public void backStackSlideAddToFragment (Fragment fragment){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment.setEnterTransition(new Slide(Gravity.END));
        fragment.setExitTransition(new Slide(Gravity.START));
        transaction.add(R.id.main_content, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getName())
                .commit();
        hideProgressDialog();

    }
    public void slideAddToFragment (Fragment fragment){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment.setEnterTransition(new Slide(Gravity.END));
        fragment.setExitTransition(new Slide(Gravity.START));
        transaction.add(R.id.main_content, fragment, fragment.getClass().getSimpleName())
                .commit();
        hideProgressDialog();

    }

    public void replaceToFragment (Fragment fragment){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_content, fragment)
                .commit();
        hideProgressDialog();
    }

    public void slideReplaceToWelcomeFragment (Fragment fragment){
        List<Fragment> al = getSupportFragmentManager().getFragments();
        if (al == null) {
            // code that handles no existing fragments
            return;
        }

        for (Fragment frag : al)
        {
            // To save any of the fragments, add this check.
            // A tag can be added as a third parameter to the fragment when you commit it
            if (frag == null) {
                continue;
            }

            getSupportFragmentManager().beginTransaction().remove(frag).commit();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment.setEnterTransition(new Slide(Gravity.END));
        fragment.setExitTransition(new Slide(Gravity.START));
        transaction.replace(R.id.main_content, fragment)
                .commit();
        hideProgressDialog();
    }
    public void slideReplaceToFragment (Fragment fragment){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment.setEnterTransition(new Slide(Gravity.END));
        fragment.setExitTransition(new Slide(Gravity.START));
        transaction.replace(R.id.main_content, fragment)
                .commit();
        hideProgressDialog();
    }
    public void backStackSlideReplaceFromBottomToFragment (Fragment fragment){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment.setEnterTransition(new Slide(Gravity.BOTTOM));
        fragment.setExitTransition(new Slide(Gravity.TOP));
        transaction.replace(R.id.main_content, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getName())
                .commit();
        hideProgressDialog();

    }
    public void backStackSlideAddFromBottomToFragment (Fragment fragment){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment.setEnterTransition(new Slide(Gravity.BOTTOM));
        fragment.setExitTransition(new Slide(Gravity.TOP));
        transaction.add(R.id.main_content, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getName())
                .commit();
        hideProgressDialog();

    }

    public synchronized void createAccountFromOptions() {

        onBackPressed();

        backStackSlideReplaceToFragment(new CreateAccountNameFragment());
    }
//////////////////////////////////
///
/// fragment listeners
///
/// START
/// //////////////////////////////


    @Override
    public void askResetForEmail(String email) {

    }

    @Override
    public void tryToCreateAccount() {
        if (account!=null) {

           createUserWithData(account);
        }

    }

    @Override
    public void optionSelected(String option) {
        Log.e("optionSelected",option);

        if (option.equalsIgnoreCase(getString(R.string.facebook))){
            doFacebook();

        }else if (option.equalsIgnoreCase(getString(R.string.google))){
            doGoogle();

        }

    }

    public void doFacebook(){
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","email"));

    }
    public void doGoogle(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
//////////////////////////////////
///
/// FirAuthListener listeners
///
/// START
/// //////////////////////////////


    void showLoginError(String error){
        Log.e("userDidNotLogged",error);

    }





    public void handleFacebookAccessToken(AccessToken accessToken) {
        Log.e("FirAuth","handleFacebookAccessToken");

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        fSignInWithCredential(credential);
    }

    public void handleGoogleAccessToken(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        fSignInWithCredential(credential);

    }
    void fSignInWithCredential(AuthCredential credential){
        Log.e("FirAuth","fSignInWithCredential");

        mAuth.getmAuth().signInWithCredential(credential);
    }




    public void createUserWithData(final AppAccount userData){
        mAuth.getmAuth().createUserWithEmailAndPassword(userData.getEmail(),userData.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {



                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    showLoginError(task.getException().getLocalizedMessage());

                    return;
                }



                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(userData.getName()+" "+userData.getSurname())
                        .build();

                task.getResult().getUser().updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    mAuth.getmAuth().signInWithEmailAndPassword(userData.getEmail(),userData.getPassword());

                                }
                            }
                        });




            }
        });
    }

//////////////////////////////////
///
/// Account set
///
/// START
/// //////////////////////////////


    public void setNameSurname(String name,String surname){
        if (account==null){
            account = new AppAccount();
        }
        account.setName(name);
        account.setSurname(surname);
    }
    public void setEmail(String email){
        if (account==null){
            account = new AppAccount();
        }
        account.setEmail(email);
    }
    public void setPassword(String password){
        if (account==null){
            account = new AppAccount();
        }
        account.setPassword(password);
    }


//////////////////////////////////
///
/// Adapter set
///
/// START
/// //////////////////////////////

    private class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

        ArrayList<TabItem>pages;

        public SimpleFragmentPagerAdapter(FragmentManager fm, ArrayList<TabItem> tabBarPages) {
            super(fm);


            pages = tabBarPages;


        }

        // This determines the fragment for each tab
        @Override
        public Fragment getItem(int position) {

            BasicFragment o= null;
            try {
                o = (BasicFragment)Class.forName("com.rb.workcatalog.fragments.tabs."+pages.get(position).getClass_android()).newInstance();
                o.setTheTab(pages.get(position));
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            return  o;

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
            return ""+getStringResourceByName(pages.get(position).getString_res());

        }



    }

    private String getStringResourceByName(String aString) {
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(aString, "string", packageName);
        return getString(resId);
    }


//////////////////////////////////
///
/// Adapter Gallery set
///
/// START
/// //////////////////////////////





    public List<String> selectedImages;

    public List<String> getSelectedImages() {
        if (selectedImages==null){
            selectedImages=new ArrayList<>();
        }else{
            HashSet<String> hashSet = new HashSet<String>();
            hashSet.addAll(selectedImages);
            selectedImages.clear();
            selectedImages.addAll(hashSet);
        }
        return selectedImages;
    }

    public void setSelectedImages(List<String> selectedImages) {
        this.selectedImages = selectedImages;
    }

    public List<Boolean> getSelected() {
        if (selected==null){
            selected=new ArrayList<>();
        }
        return selected;
    }

    public void setSelected(List<Boolean> selected) {
        this.selected = selected;
    }

    public  List<Boolean> selected;
}
