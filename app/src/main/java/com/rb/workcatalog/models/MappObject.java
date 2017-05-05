package com.rb.workcatalog.models;

import android.text.TextUtils;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Programmer on 18/04/17.
 */
@IgnoreExtraProperties
public class MappObject implements Serializable{
    public String getCreated_at() {
        return created_at;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

    public String user_uid;
    public String getKey_uid() {
        return key_uid;
    }

    public void setKey_uid(String key_uid) {
        this.key_uid = key_uid;
    }

    String key_uid;
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String created_at;
    public MappObject(){

    }
    public Map<String, Object> mapp;

    public String name;
    public String author;

    public String getObject_key_uid() {
        return object_key_uid;
    }

    public void setObject_key_uid(String object_key_uid) {
        this.object_key_uid = object_key_uid;
    }

    public String object_key_uid;
    public Map<String, Object> getMapp() {
        return mapp;
    }

    public void setMapp(Map<String, Object> mapp) {
        this.mapp = mapp;
    }

    public String getName() {
        if (TextUtils.isEmpty(name)){
            String digits = this.key_uid.replaceAll("\\D+","");
            if (!TextUtils.isEmpty(digits)) {
                name = "Mapp #" + digits;
            }else{
                name = "Mapp #" + System.currentTimeMillis();

            }
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public ArrayList<String> getPaths() {
        return paths;
    }

    public void setPaths(ArrayList<String> lables) {
        this.paths = lables;
    }

    public ArrayList<String> paths;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String description;

    public String getGalleryImagePath() {
        return gallery_image_path;
    }

    public void setGalleryImagePath(String galleryImagePath) {
        this.gallery_image_path = galleryImagePath;
    }

    public String gallery_image_path;
}
