package com.rb.workcatalog.models;

import com.google.firebase.database.IgnoreExtraProperties;
import com.rb.workcatalog.fragments.main.BasicFragment;

/**
 * Created by Programmer on 18/04/17.
 */
@IgnoreExtraProperties

public class TabItem {
    int title;

    public String getKey_uid() {
        return key_uid;
    }

    public void setKey_uid(String key_uid) {
        this.key_uid = key_uid;
    }

    String key_uid;
    public BasicFragment getFragment() {
        return fragment;
    }

    public void setFragment(BasicFragment fragment) {
        this.fragment = fragment;
    }

    BasicFragment fragment;

    String class_android;

    public String getClass_android() {
        return class_android;
    }

    public void setClass_android(String class_android) {
        this.class_android = class_android;
    }







    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }






    public String getString_res() {
        return string_res;
    }

    public void setString_res(String string_res) {
        this.string_res = string_res;
    }

    String string_res;




}
