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
 * Use the {@link OtherOptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OtherOptionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    OtherOptionListener mCallback;
    // Container Activity must implement this interface
    public interface OtherOptionListener {
        public void optionSelected(String option);
    }


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageButton closeBtn;
    Button fb_btn,google_btn,create_btn;
    MainActivity currentActivity;
    public OtherOptionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OtherOptionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OtherOptionFragment newInstance(String param1, String param2) {
        OtherOptionFragment fragment = new OtherOptionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        currentActivity=(MainActivity)getActivity();
        mCallback=currentActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_other_option, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        closeBtn=(ImageButton)view.findViewById(R.id.option_back_btn);
        fb_btn=(Button)view.findViewById(R.id.option_facebook_btn);
        google_btn=(Button)view.findViewById(R.id.option_google_btn);
        create_btn=(Button)view.findViewById(R.id.option_create_btn);
        fb_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback!=null){
                    mCallback.optionSelected(fb_btn.getText().toString());
                }
            }
        });
        google_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback!=null){
                    mCallback.optionSelected(google_btn.getText().toString());
                }
            }
        });
        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentActivity.createAccountFromOptions();
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentActivity.onBackPressed();
            }
        });
    }


}
