package com.rb.workcatalog.utils;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * Created by Programmer on 03/05/17.
 */

@IgnoreExtraProperties
public class UserModel implements Serializable {
    String display_name="";
    String email="";
    String os="";
    String provider_email="";
    String provider_id="";
    String provider_uid="";
    String uid="";
    String role="";
    String user_photo_url_string="";
    String username="";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_photo_url_string() {
        return user_photo_url_string;
    }

    public void setUser_photo_url_string(String user_photo_url_string) {
        this.user_photo_url_string = user_photo_url_string;
    }


    String phone_number="";
    String birth_date="";
    String company_uid="";
    String company_name="";

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getProvider_email() {
        return provider_email;
    }

    public void setProvider_email(String provider_email) {
        this.provider_email = provider_email;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }

    public String getProvider_uid() {
        return provider_uid;
    }

    public void setProvider_uid(String provider_uid) {
        this.provider_uid = provider_uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public String company_role="";

    public String getCompany_role() {
        return company_role;
    }

    public void setCompany_role(String company_role) {
        this.company_role = company_role;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getCompany_uid() {
        return company_uid;
    }

    public void setCompany_uid(String company_uid) {
        this.company_uid = company_uid;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }


    public Privilege getPrivilege() {

        if (privilege==null){
            privilege = new Privilege();
        }
        return privilege;
    }

    public void setPrivilege(Privilege privilege) {
        this.privilege = privilege;
    }

    public Privilege privilege = new Privilege();






    public String toString() {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        result.append( this.getClass().getName() );
        result.append( " Object {" );
        result.append(newLine);

        //determine fields declared in this class only (no fields of superclass)
        Field[] fields = this.getClass().getDeclaredFields();

        //print field names paired with their values
        for ( Field field : fields  ) {
            result.append("  ");
            try {
                result.append( field.getName() );
                result.append(": ");
                //requires access to private field:
                result.append( field.get(this) );
            } catch ( IllegalAccessException ex ) {
                System.out.println(ex);
            }
            result.append(newLine);
        }
        result.append("}");

        return result.toString();
    }


    public static class Privilege implements Serializable {


        public Privilege(){

        }

        boolean controlEnabled=true;

        public boolean isControlEnabled() {
            return controlEnabled;
        }

        public void setControlEnabled(boolean controlEnabled) {
            this.controlEnabled = controlEnabled;
        }

        public boolean isActivationEnabled() {
            return activationEnabled;
        }

        public void setActivationEnabled(boolean activationEnabled) {
            this.activationEnabled = activationEnabled;
        }

        public boolean isGpsEnabled() {
            return gpsEnabled;
        }

        public void setGpsEnabled(boolean gpsEnabled) {
            this.gpsEnabled = gpsEnabled;
        }

        public boolean isWarehouseEnabled() {
            return warehouseEnabled;
        }

        public void setWarehouseEnabled(boolean warehouseEnabled) {
            this.warehouseEnabled = warehouseEnabled;
        }

        public boolean isInEnbaled() {
            return inEnbaled;
        }

        public void setInEnbaled(boolean inEnbaled) {
            this.inEnbaled = inEnbaled;
        }

        public boolean isOutEnabled() {
            return outEnabled;
        }

        public void setOutEnabled(boolean outEnabled) {
            this.outEnabled = outEnabled;
        }

        public boolean isDisactivationEnabled() {
            return disactivationEnabled;
        }

        public void setDisactivationEnabled(boolean disactivationEnabled) {
            this.disactivationEnabled = disactivationEnabled;
        }

        public boolean isChatEnabled() {
            return chatEnabled;
        }

        public void setChatEnabled(boolean chatEnabled) {
            this.chatEnabled = chatEnabled;
        }

        public boolean isLotEnabled() {
            return lotEnabled;
        }

        public void setLotEnabled(boolean lotEnabled) {
            this.lotEnabled = lotEnabled;
        }

        public boolean isZoneEnabled() {
            return zoneEnabled;
        }

        public void setZoneEnabled(boolean zoneEnabled) {
            this.zoneEnabled = zoneEnabled;
        }

        public boolean isDestinationClientEnabled() {
            return destinationClientEnabled;
        }

        public void setDestinationClientEnabled(boolean destinationClientEnabled) {
            this.destinationClientEnabled = destinationClientEnabled;
        }

        boolean activationEnabled=true;
        boolean gpsEnabled=true;
        boolean warehouseEnabled=true;
        boolean inEnbaled=true;
        boolean outEnabled=true;
        boolean disactivationEnabled=true;
        boolean chatEnabled=true;
        boolean lotEnabled=true;
        boolean zoneEnabled=true;
        boolean destinationClientEnabled=true;

        public boolean isArchiveEnabled() {
            return isArchiveEnabled;
        }

        public void setArchiveEnabled(boolean archiveEnabled) {
            isArchiveEnabled = archiveEnabled;
        }

        boolean isArchiveEnabled=true;

    }
}
