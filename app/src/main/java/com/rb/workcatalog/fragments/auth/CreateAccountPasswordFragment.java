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
 * Use the {@link CreateAccountPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateAccountPasswordFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ImageButton closeBtn;
    MainActivity currentActivity;
    Button nextBtn,showBtn;




    CreateAccountListener mCallback;
    // Container Activity must implement this interface
    public interface CreateAccountListener {
        public void tryToCreateAccount();
    }


    public CreateAccountPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateAccountPasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateAccountPasswordFragment newInstance(String param1, String param2) {
        CreateAccountPasswordFragment fragment = new CreateAccountPasswordFragment();
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
        currentActivity = (MainActivity)getActivity();
        mCallback=currentActivity;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_account_password, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        closeBtn=(ImageButton)view.findViewById(R.id.create_close_btn);
        nextBtn=(Button)view.findViewById(R.id.create_next_btn);
        showBtn=(Button)view.findViewById(R.id.create_password_show_btn);
        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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
