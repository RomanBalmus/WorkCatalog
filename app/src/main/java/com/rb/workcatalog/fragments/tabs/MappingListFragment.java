package com.rb.workcatalog.fragments.tabs;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.Query;
import com.rb.workcatalog.MainActivity;
import com.rb.workcatalog.R;
import com.rb.workcatalog.firebasedatabase.FireDB;
import com.rb.workcatalog.fragments.main.BasicFragment;
import com.rb.workcatalog.models.MappObject;

import java.util.Iterator;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MappingListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MappingListFragment extends BasicFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseListAdapter mAdapter;

    public MappingListFragment() {
        // Required empty public constructor
    }
    MainActivity currentActivity;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MappingListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MappingListFragment newInstance(String param1, String param2) {
        MappingListFragment fragment = new MappingListFragment();
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

        return inflater.inflate(R.layout.fragment_mapping_list, container, false);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }

    }

    Button showCreateBtn;
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showCreateBtn = (Button)view.findViewById(R.id.show_create_mapp_btn);
        showCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentActivity.backStackSlideReplaceToFragment(new CreateMappingFragment());
            }
        });

        ListView messagesView = (ListView)view. findViewById(R.id.mapping_list_view);

        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = FireDB.getInstance().getMappReference();

        Log.e("the query: ",postsQuery.toString());



        mAdapter = new FirebaseListAdapter<MappObject>(currentActivity, MappObject.class,  R.layout.mapp_row_layout, postsQuery) {
            @Override
            protected void populateView(View view, MappObject chatMessage, int position) {


                StringBuilder string=new StringBuilder(chatMessage.getName()+"\n\n");


                Iterator it = chatMessage.getMapp().entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();

                    string.append(pair.getKey()+"\n");
                    it.remove(); // avoids a ConcurrentModificationException
                }
                ((TextView) view.findViewById(R.id.mapp_row_textView)).setText(string.toString()+"\n"+chatMessage.getAuthor()+"\n"+chatMessage.getCreated_at());

            }

        };
        messagesView.setAdapter(mAdapter);
        messagesView.setEmptyView(view.findViewById(R.id.emptyView));
        messagesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment frg = new MappingConfigurationFragment();
                Bundle extra = new Bundle();
                extra.putSerializable("mapp_object",(MappObject)mAdapter.getItem(position));
                frg.setArguments(extra);
                currentActivity.backStackSlideReplaceToFragment(frg);

            }
        });

    }
}