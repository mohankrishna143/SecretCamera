package com.android.krishsecretcamera;

import android.content.Context;

import java.util.TreeMap;

import listeners.PictureCapturingListener;
import services.PictureCapturingServiceImpl;

public class Captureimage implements PictureCapturingListener{

    Context context;

    public void Captureimage(Context ctx){
        this.context=ctx;
        /*pictureService = PictureCapturingServiceImpl.getInstance(Captureimage.this);
        pictureService.startCapturing(this);*/
    }




    @Override
    public void onCaptureDone(String pictureUrl, byte[] pictureData) {

    }

    @Override
    public void onDoneCapturingAllPhotos(TreeMap<String, byte[]> picturesTaken) {

    }
}
