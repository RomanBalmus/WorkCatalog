package com.rb.workcatalog.fragments.tabs;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rb.workcatalog.MainActivity;
import com.rb.workcatalog.R;
import com.rb.workcatalog.adapters.ConfigurationRecyclerAdapter;
import com.rb.workcatalog.adapters.TwoWayFileAdapter;
import com.rb.workcatalog.firebasedatabase.FireDB;
import com.rb.workcatalog.fragments.gallery.BucketListFragment;
import com.rb.workcatalog.models.MappObject;
import com.rb.workcatalog.utils.PrefManager;

import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MappingConfigurationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MappingConfigurationFragment extends Fragment implements ConfigurationRecyclerAdapter.OnLabelChangedListener,BucketListFragment.OnFragmentInteractionListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button createBtn,addMediaBtn,closeBtn;
    MainActivity currentActivity;
    ConfigurationRecyclerAdapter cfgAdapter;
    ArrayList<Map.Entry>cfgItems;
    public MappingConfigurationFragment() {
        // Required empty public constructor
    }
    static final int OPEN_MEDIA_PICKER = 1;  // Request code

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MappingConfigurationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MappingConfigurationFragment newInstance(String param1, String param2) {
        MappingConfigurationFragment fragment = new MappingConfigurationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private MappObject mapp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mapp = (MappObject)getArguments().getSerializable("mapp_object");
        }
        currentActivity=(MainActivity)getActivity();

    }
    TwoWayView lvTest;
    TwoWayFileAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mapping_configuration, container, false);
    }
    ArrayList<String>pathsArray;
    TextView mappName;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mappName=(TextView)view.findViewById(R.id.mappingNameTextView);

        mappName.setText(mapp.getName());
        Button modifyName=(Button)view.findViewById(R.id.mappingNameChangeBtn);
        modifyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showModifyAlert(mappName.getText().toString());
            }
        });

        closeBtn = (Button)view.findViewById(R.id.back_btn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentActivity.onBackPressed();
            }
        });
        Log.e("mapp: ",""+mapp.getMapp());

        createBtn=(Button)view.findViewById(R.id.saveCfgBtn);
        addMediaBtn=(Button)view.findViewById(R.id.addFileBtn);
        addMediaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BucketListFragment bucket=new BucketListFragment();
                bucket.setmListener(MappingConfigurationFragment.this);
                currentActivity.backStackSlideAddToFragment(bucket);

            }
        });
        pathsArray = new ArrayList<>();
        adapter = new TwoWayFileAdapter(currentActivity,pathsArray);
        lvTest = (TwoWayView) view.findViewById(R.id.twoWayView);
        lvTest.setAdapter(adapter);


        RecyclerView messagesView = (RecyclerView)view. findViewById(R.id.cfg_listView);
        cfgItems = new ArrayList<>();
        Iterator it = mapp.getMapp().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();

            cfgItems.add(pair);
            it.remove(); // avoids a ConcurrentModificationException
        }
        Collections.sort(cfgItems, new Comparator<Map.Entry>() {
            @Override
            public int compare(Map.Entry o1, Map.Entry o2) {
                return o1.getKey().toString().compareToIgnoreCase(o2.getKey().toString());
            }
        });

        //Collections.reverse(cfgItems);


        // Create adapter passing in the sample user data
        cfgAdapter = new ConfigurationRecyclerAdapter(currentActivity, cfgItems);
        // Attach the adapter to the recyclerview to populate items
        messagesView.setAdapter(cfgAdapter);
        // Set layout manager to position the items
        messagesView.setLayoutManager(new LinearLayoutManager(currentActivity));

        cfgAdapter.setOnScoreSavedListener(this);

        cfgAdapter.notifyDataSetChanged();
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("cfgs: ",""+cfgItems);
                Log.e("paths: ",""+pathsArray);

                currentActivity.showProgressDialog();
                StringBuilder strBuilder=new StringBuilder("");
                for (Map.Entry mp :
                        cfgItems) {
                    if (!TextUtils.isEmpty(mp.getValue().toString())) {
                        strBuilder.append(mp.getKey().toString()+":\n");
                        strBuilder.append(mp.getValue().toString()+"\n");

                    }

                }


                mapp.setDescription(strBuilder.toString());


                FireDB.getInstance().setOnTransactionCompleteListener(new FireDB.TransactionCompleteListener() {
                    @Override
                    public void onRewriteComplete(MappObject rMapp) {
                        if (pathsArray.size()>0) {
                            saveFileObjectToBackgroudUpload(rMapp);
                        }
                        currentActivity.hideProgressDialog();

                    }
                });
                FireDB.getInstance().rewriteMappForKey(mapp);



            }
        });




    }

    void saveFileObjectToBackgroudUpload(MappObject rMapp){

        PrefManager pref = new PrefManager(currentActivity);
        pref.saveObjectForKey(pathsArray,rMapp.getObject_key_uid());
    }
    /**
     * Matches code in MediaProvider.computeBucketValues. Should be a common
     * function.
     */


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onScoreSaved(Map.Entry me,int position) {

        cfgItems.set(position,me);

    }

void showModifyAlert(String title){
    AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
    builder.setTitle(getString(R.string.modify)+": "+title);

// Set up the input
    final EditText input = new EditText(currentActivity);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
    input.setInputType(InputType.TYPE_CLASS_TEXT );
    builder.setView(input);

// Set up the buttons
    builder.setPositiveButton(getString(R.string.modify), new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            String m_Text = input.getText().toString();
            if (!TextUtils.isEmpty(m_Text)){
                mappName.setText(m_Text);
                mapp.setName(m_Text);
                FireDB.getInstance().updateMappName(mapp);
                dialog.dismiss();
            }
        }
    });
    builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    });

    builder.show();
}



    @Override
    public void onFragmentInteraction(List<String> paths) {
        Log.e("array list",""+paths);
        pathsArray.addAll(paths);
        if (pathsArray.size()>0 && lvTest.getVisibility()==View.GONE){
            lvTest.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }
}
