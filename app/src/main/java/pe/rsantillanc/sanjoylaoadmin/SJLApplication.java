package pe.rsantillanc.sanjoylaoadmin;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.SaveCallback;

import pe.rsantillanc.sanjoylaoadmin.util.Const;

/**
 * Created by RenzoD on 08/12/2015.
 */
public class SJLApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, getString(R.string.parse_application_id), getString(R.string.parse_client_key));
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParsePush.subscribeInBackground(Const.SJL_CHANNEL_ADMIN, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null)
                    Log.e(Const.DEBUG, "Error!, subscribe: " + e.getMessage());
                else
                    Log.e(Const.DEBUG, "Success!, subscribe!: ");
            }
        });
    }
}
