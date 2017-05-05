package com.rb.workcatalog.fragments.auth;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.rb.workcatalog.MainActivity;
import com.rb.workcatalog.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WelcomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WelcomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "WelcomeFragment";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public WelcomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WelcomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WelcomeFragment newInstance(String param1, String param2) {
        WelcomeFragment fragment = new WelcomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

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
    ImageButton closeBtn;
    Button welcome_sign_in_btn,welcome_continue_btn,welcome_create_btn,welcome_other_btn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        closeBtn=(ImageButton)view.findViewById(R.id.welcome_close_btn);
        welcome_continue_btn=(Button)view.findViewById(R.id.welcome_continue_with_fb_btn);
        welcome_create_btn=(Button)view.findViewById(R.id.welcome_create_acc_btn);
        welcome_sign_in_btn=(Button)view.findViewById(R.id.welcome_sign_in_btn);
        welcome_other_btn=(Button)view.findViewById(R.id.welcome_other_option_btn);


        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentActivity.onBackPressed();
            }
        });
        welcome_continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentActivity.doFacebook();
            }
        });
        welcome_create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentActivity.backStackSlideReplaceToFragment(new CreateAccountNameFragment());

            }
        });
        welcome_sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentActivity.backStackSlideReplaceToFragment(new EmailSignInFragment());
            }
        });
        welcome_other_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentActivity.backStackSlideAddFromBottomToFragment(new OtherOptionFragment());

            }
        });
    }





    /**
     * Detects and toggles immersive mode (also known as "hidey bar" mode).
     */

}
