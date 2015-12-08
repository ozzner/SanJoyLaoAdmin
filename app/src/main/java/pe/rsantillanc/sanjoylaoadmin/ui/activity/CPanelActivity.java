package pe.rsantillanc.sanjoylaoadmin.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.SaveCallback;
import com.parse.SendCallback;

import pe.rsantillanc.sanjoylaoadmin.R;
import pe.rsantillanc.sanjoylaoadmin.util.Const;

public class CPanelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpanel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Parse.initialize(this, getString(R.string.parse_application_id), getString(R.string.parse_client_key));
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParsePush.subscribeInBackground(Const.SJL_CHANNEL_ADMIN, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!= null)
                    Log.e(Const.DEBUG, "Error!, subscribe: " + e.getMessage());
                else
                    Log.e(Const.DEBUG, "Success!, subscribe!: " );
            }
        });

        ParsePush push = new ParsePush();
        push.setChannel(Const.SJL_CHANNEL_ADMIN);
        push.setMessage("Orden recibida");
        push.sendInBackground(new SendCallback() {
            @Override
            public void done(ParseException e) {
                if (e!= null)
                    Log.e(Const.DEBUG, "Error!, notify: " + e.getMessage());
                else
                    Log.e(Const.DEBUG, "Success!, notify!: " );
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cpanel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
