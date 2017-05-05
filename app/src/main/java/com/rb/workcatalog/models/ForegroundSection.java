package com.rb.workcatalog.models;

/**
 * Created by Programmer on 21/04/17.
 */

public class ForegroundSection {
    String section_title;
    String section_path;

    public String getSection_title() {
        return section_title;
    }

    public void setSection_title(String section_title) {
        this.section_title = section_title;
    }

    public String getSection_path() {
        return section_path;
    }

    public void setSection_path(String section_path) {
        this.section_path = section_path;
    }

    public String getString_res() {
        return string_res;
    }

    public void setString_res(String string_res) {
        this.string_res = string_res;
    }

    String string_res;
}
