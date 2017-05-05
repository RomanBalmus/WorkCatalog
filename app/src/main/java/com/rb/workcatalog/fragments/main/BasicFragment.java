package com.rb.workcatalog.fragments.main;

import android.support.v4.app.Fragment;

import com.rb.workcatalog.models.TabItem;

/**
 * Created by Programmer on 22/04/17.
 */

public class BasicFragment extends Fragment{

    public TabItem getTheTab() {
        return theTab;
    }

    public void setTheTab(TabItem theTab) {
        this.theTab = theTab;
    }

    TabItem theTab;

}
