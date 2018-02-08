package com.example.eskild.hc;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutionException;

/**
 * Created by Eskild on 26.01.2018.
 */


public class Session implements Parcelable{
    private SharedPreferences prefs;
    private String allergier;
    private String usename;
    private String access_card;
    private int phone_number;
    public boolean access;
    private Context context;

    public Session(SharedPreferences prefs){
        this.prefs = prefs;
        if (!prefs.getBoolean("Access",false)){
            this.usename = "";
            this.allergier = "";
            this.phone_number = -1;
            this.access_card = "";
            this.access = false;
        } else {
            this.usename = prefs.getString("usename","");
            this.phone_number = prefs.getInt("phone_number",-1);
            this.allergier = prefs.getString("allergies","");
            this.access_card = prefs.getString("access_card","");
            this.access = true;
        }

    }

    public Session(Parcel in) {
        allergier = in.readString();
        usename = in.readString();
        access_card = in.readString();
        phone_number = in.readInt();

    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(allergier);
        dest.writeString(access_card);
        dest.writeString(usename);
        dest.writeInt(phone_number);


    }


    public static final Creator<Session> CREATOR = new Creator<Session>() {
        @Override
        public Session createFromParcel(Parcel in) {
            return new Session(in);
        }

        @Override
        public Session[] newArray(int size) {
            return new Session[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }



    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    // Get functions for the SessionClass

    public String getusename() {return this.usename;}
    public String getAcces_card() {
        return this.access_card;
    }
    public int getTelefon() {
        return this.phone_number;
    }
    public String getAllergier() {
        return this.allergier;
    }
    public Context getContext() {return this.context;}

    public String getToken() {
        String token = prefs.getString("Token", "");
        if (!token.equals("")){
            token = "token " + token.substring(10, token.length() - 2);
        }
        return token;
    }

    // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    // Set functions for Session

    public void setPrefs(SharedPreferences prefs) {
        this.prefs = prefs;
    }
    public void setAccess(Boolean access){
        prefs.edit().putBoolean("Access",access);
    }
    public void setusename(String usename) {
        prefs.edit().putString("usename", usename).commit();
        this.usename = usename;
    }
    public void setToken(String Token) {
        prefs.edit().putString("Token", Token).commit();
    }
    public void setAccesCard(String accesCard) {
        prefs.edit().putString("access_card", accesCard).commit();
        this.access_card = accesCard;
    }
    public void setPhoneNumber(int phoneNumber) {
        prefs.edit().putInt("phone_number", phoneNumber).commit();
        this.phone_number = phoneNumber;
    }
    public void setAccess(){
        prefs.edit().putBoolean("Access",true).commit();
        this.access = true;
    }
    public void setAllergies(String allergies) {
        prefs.edit().putString("allergies", allergies).commit();
        this.allergier = allergies;
    }

    public void setImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        prefs.edit().putString("image",encodedImage).commit();
    }
    public Bitmap DecodedImage(String encodedImage) {
        String previouslyEncodedImage = prefs.getString("image", "");
        Bitmap bitmap = null;
        if( !previouslyEncodedImage.equalsIgnoreCase("") ){
            byte[] b = Base64.decode(previouslyEncodedImage, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        }
        return bitmap;
    }
    public int setValues() {
        Session session = this;
        BackgroundWorker_profile worker = new BackgroundWorker_profile(session);
        try {
            int respons = worker.execute(this).get();
            return respons;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        };
        return 0;
    }
    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    // Logout of Session
    public void Logout(){
        this.usename = "";
        this.allergier = "";
        this.phone_number = -1;
        this.access_card = "";
        this.access = false;
        prefs.edit().clear().commit();
    }

}