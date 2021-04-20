package com.android.krishsecretcamera;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;

public class MailSender {

    Context ctx;




    public   MailSender(Context contxt){
        this.ctx=contxt;
    }

    public void sendTestEmail() {

        ArrayList<String> imageList=attachImages();
        String valu=getStringPreferenceData("Mail_Id");
        if(isInternetConnection()&&imageList.size()>0) {
            if(valu.length()>0) {
                BackgroundMail.newBuilder(ctx)
                        .withUsername("laxmimohana1213@gmail.com")
                        .withPassword("krish123")
                        .withMailTo(valu)
                        /* .withMailCc("cc-email@gmail.com")
                         .withMailBcc("bcc-email@gmail.com")*/
                        .withSubject("Thief Images")
                        .withBody("Please find the attached images")
                        .withAttachments(imageList)
                        .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                            @Override
                            public void onSuccess() {
                                //do some magic
                                deleteImages();
                            }
                        })
                        .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                            @Override
                            public void onFail() {
                                //do some magic
                            }
                        })
                        .send();
            }
        }/*else{
            setMobileDataEnabled(ctx,true);
        }*/
    }


    private  void deleteImages(){
        try {
            String root = Environment.getExternalStorageDirectory().getAbsolutePath();
            File myDir = new File(root + "/saved_images");
            //myDir.delete();
            File[] files = myDir.listFiles();
            for (int i = 0; i < files.length; i++)
            {
                files[i].delete();
                //Log.d("Files", "FileName:" + files[i].getName());
            }
            myDir.delete();
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private ArrayList<String> attachImages(){
        ArrayList<String> fileList=new ArrayList<>();
        try {
            String root = Environment.getExternalStorageDirectory().getAbsolutePath();
            File myDir = new File(root + "/saved_images");
            //File directory = new File(path);
            if(myDir.exists()) {
                File[] files = myDir.listFiles();
                for (int i = 0; i < files.length; i++) {
                    fileList.add(files[i].getAbsolutePath());
                    //Log.d("Files", "FileName:" + files[i].getName());
                }
            }
           return fileList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return fileList;
    }


    public  boolean isInternetConnection()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private  String getStringPreferenceData(String type){
        SharedPreferences sp = ctx.getSharedPreferences(ctx.getResources().getString(R.string.app_name), 0);
        String cb1 = sp.getString(type, "");
        return cb1;
    }

    private void setMobileDataEnabled(Context context, boolean enabled) {
            try {
                TelephonyManager telephonyService = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
                Method setMobileDataEnabledMethod = Objects.requireNonNull(telephonyService).getClass().getDeclaredMethod("setDataEnabled", boolean.class);
                setMobileDataEnabledMethod.invoke(telephonyService, enabled);
            } catch (Exception ex) {
                Log.e("MainActivity", "Error setting mobile data state", ex);
            }

    }
}
