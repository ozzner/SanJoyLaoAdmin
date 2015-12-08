package pe.rsantillanc.sanjoylaoadmin.receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

import pe.rsantillanc.sanjoylaoadmin.util.Const;

/**
 * Created by RenzoD on 07/12/2015.
 */
public class IncomingOrderReceiver extends ParsePushBroadcastReceiver {

    public IncomingOrderReceiver() {
        super();
    }


    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);

        Log.e(Const.DEBUG, "Incoming push notification");

        if (intent == null)
            return;

        String string = intent.getExtras().getString("com.parse.Data");
        Log.e(Const.DEBUG, "Data: " + string);

    }
}
