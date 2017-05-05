package com.rb.workcatalog.fragments.tabs;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.rb.workcatalog.MainActivity;
import com.rb.workcatalog.R;
import com.rb.workcatalog.adapters.MappAdapter;
import com.rb.workcatalog.firebasedatabase.FireDB;
import com.rb.workcatalog.models.MappObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateMappingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateMappingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText labelEt;
    Button createBtn,addBtn,closeBtn;
    MainActivity currentActivity;
    public CreateMappingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateMappingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateMappingFragment newInstance(String param1, String param2) {
        CreateMappingFragment fragment = new CreateMappingFragment();
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_mapping, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        closeBtn=(Button)view.findViewById(R.id.back_btn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentActivity.onBackPressed();
            }
        });

        final EditText mappingName=(EditText)view.findViewById(R.id.mappingNameET);
        labelEt=(EditText)view.findViewById(R.id.labelET);
        ListView messagesView = (ListView)view. findViewById(R.id.cfg_listView);
        final ArrayList<String> cfgItems = new ArrayList<>();
        final MappAdapter cfgAdapter= new MappAdapter(currentActivity,cfgItems);
        messagesView.setAdapter(cfgAdapter);
        addBtn=(Button)view.findViewById(R.id.addCfgBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(labelEt.getText().toString())){
                    cfgItems.add(labelEt.getText().toString());
                    cfgAdapter.notifyDataSetChanged();
                }
            }
        });
        createBtn=(Button)view.findViewById(R.id.saveCfgBtn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentActivity.showProgressDialog();
                Map<String,Object> map = new HashMap<>();

                for (String lbl :
                        cfgItems) {
                    map.put(lbl,"");
                }
                MappObject mapp = new MappObject();
                if (!TextUtils.isEmpty(mappingName.getText().toString())){
                    mapp.setName(mappingName.getText().toString());
                }
                mapp.setMapp(map);
                FireDB.getInstance().saveMapp(mapp);
                currentActivity.hideProgressDialog();

            }
        });
    }




}
