package com.voxelbusters.nativeplugins.features.addressbook;

import com.voxelbusters.nativeplugins.defines.Keys;
import com.voxelbusters.nativeplugins.defines.Keys.AddressBook;
import java.util.ArrayList;
import java.util.HashMap;
import spacemadness.com.lunarconsole.BuildConfig;

public class ContactDetails {
    String displayName;
    ArrayList<String> emailList;
    String familyName;
    String givenName;
    ArrayList<String> phoneList;
    String profilePicturePath;

    public ContactDetails() {
        this.displayName = BuildConfig.FLAVOR;
        this.familyName = BuildConfig.FLAVOR;
        this.givenName = BuildConfig.FLAVOR;
        this.phoneList = new ArrayList();
        this.profilePicturePath = BuildConfig.FLAVOR;
        this.emailList = new ArrayList();
    }

    public void setNames(String displayName, String familyName, String givenName) {
        this.displayName = displayName;
        this.familyName = familyName;
        this.givenName = givenName;
    }

    public void addPhoneNumber(String eachPhoneNumber, int numberType) {
        this.phoneList.add(eachPhoneNumber);
    }

    public void setPicturePath(String path) {
        this.profilePicturePath = path;
    }

    public void addEmail(String emailContact, int emailType) {
        this.emailList.add(emailContact);
    }

    HashMap<String, Object> getHash() {
        HashMap<String, Object> map = new HashMap();
        map.put(AddressBook.DISPLAY_NAME, this.displayName);
        map.put(AddressBook.FAMILY_NAME, this.familyName);
        map.put(AddressBook.GIVEN_NAME, this.givenName);
        map.put(Keys.IMAGE_PATH, this.profilePicturePath);
        map.put(AddressBook.PHONE_NUM_LIST, this.phoneList);
        map.put(AddressBook.EMAIL_ID_LIST, this.emailList);
        return map;
    }
}
