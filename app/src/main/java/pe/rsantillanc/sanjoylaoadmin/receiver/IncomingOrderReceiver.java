package pe.rsantillanc.sanjoylaoadmin.receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

import pe.rsantillanc.sanjoylaoadmin.ui.activity.CPanelActivity;
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

        Intent cpanelIntent = new Intent(context, CPanelActivity.class);
        cpanelIntent.putExtras(intent.getExtras());
        cpanelIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        context.startActivity(cpanelIntent);
    }
}
