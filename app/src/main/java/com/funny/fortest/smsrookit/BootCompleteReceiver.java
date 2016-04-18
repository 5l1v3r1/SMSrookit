package com.funny.fortest.smsrookit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompleteReceiver extends BroadcastReceiver {
    public BootCompleteReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Intent startIntent = new Intent(SMSApplication.getContext(), MainService.class);
        SMSApplication.getContext().startService(startIntent);
    }
}
