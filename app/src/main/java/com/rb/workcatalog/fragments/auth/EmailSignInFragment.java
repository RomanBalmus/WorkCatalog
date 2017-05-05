package com.rb.workcatalog.fragments.auth;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.rb.workcatalog.MainActivity;
import com.rb.workcatalog.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmailSignInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmailSignInFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;








    public EmailSignInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EmailSignInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EmailSignInFragment newInstance(String param1, String param2) {
        EmailSignInFragment fragment = new EmailSignInFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    ImageButton closeBtn;
    MainActivity currentActivity;
    Button forgot_password_btn,next_btn,show_btn;
    EditText email_et,password_et;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        currentActivity=(MainActivity)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_email_sign_in, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        closeBtn=(ImageButton)view.findViewById(R.id.email_signin_back_btn);
        forgot_password_btn = (Button)view.findViewById(R.id.forgot_password_btn);
        next_btn=(Button)view.findViewById(R.id.email_sign_next_btn);
        show_btn=(Button)view.findViewById(R.id.email_sign_show_btn);
        email_et=(EditText)view.findViewById(R.id.email_sign_email_et);
        password_et=(EditText)view.findViewById(R.id.email_sign_password_et);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next_btn.setEnabled(!next_btn.isEnabled());
            }
        });
        show_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (show_btn.getText().toString()==getString(R.string.show)){
                    password_et.setTransformationMethod(null);
                    show_btn.setText(getString(R.string.hide));
                }else{
                    show_btn.setText(getString(R.string.show));
                    password_et.setTransformationMethod(new PasswordTransformationMethod());

                }

            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentActivity.onBackPressed();
            }
        });
        forgot_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentActivity.backStackSlideReplaceToFragment(new PasswordForgotFragment());

            }
        });
    }
}
