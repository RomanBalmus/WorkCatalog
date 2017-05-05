package com.rb.workcatalog.firebaseauth;

/**
 * Created by Programmer on 14/04/17.
 */
public  interface FirAuthListener {




      void userDidLogged();
      void userDidNotLogged(String error);

}
