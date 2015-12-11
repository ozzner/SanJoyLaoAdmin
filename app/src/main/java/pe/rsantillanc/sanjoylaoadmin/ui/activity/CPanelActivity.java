package pe.rsantillanc.sanjoylaoadmin.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParsePush;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pe.rsantillanc.sanjoylaoadmin.R;
import pe.rsantillanc.sanjoylaoadmin.util.Const;

public class CPanelActivity extends AppCompatActivity {

    TextView orderID;
    TextView clientName;
    TextView clientDni;
    TextView clientPhone;
    TextView orderConsole;
    EditText price;
    Button confirm;
    Button cancel;

    String amount;
    String orderObjectId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpanel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Log.e(Const.DEBUG, "CPanel OnCreate.");

        orderID = (TextView) findViewById(R.id.order_id);
        clientName = (TextView) findViewById(R.id.tv_client_name);
        clientPhone = (TextView) findViewById(R.id.tv_client_phone);
        clientDni = (TextView) findViewById(R.id.tv_client_dni);
        orderConsole = (TextView) findViewById(R.id.tv_order_detail_console);
        price = (EditText) findViewById(R.id.et_price);
        confirm = (Button) findViewById(R.id.confirm);
        cancel = (Button) findViewById(R.id.cancel);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParsePush confirm = new ParsePush();
                confirm.setData(buildJsonOrderStatus());
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

//        ParsePush push = new ParsePush();
//        push.setChannel(Const.SJL_CHANNEL_ADMIN);
//        push.setMessage("Orden recibida");
//        push.sendInBackground(new SendCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e!= null)
//                    Log.e(Const.DEBUG, "Error!, notify: " + e.getMessage());
//                else
//                    Log.e(Const.DEBUG, "Success!, notify!: " );
//            }
//        });


    }

    private JSONObject buildJsonOrderStatus() {
        JSONObject body = new JSONObject();

        return null;
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(Const.DEBUG, "CPanel onNewIntent.");
        long totalTime = 0;
        JSONObject dataParse;
        orderConsole.setText("");

        try {

            dataParse = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            JSONObject bodyJSON = dataParse.getJSONObject("data");
            amount = bodyJSON.getString("amount");
            orderObjectId = bodyJSON.getString("objectId");

            String cdni = bodyJSON.getString("clientDni");
            String cphone = bodyJSON.getString("clientPhone");
            String cname = bodyJSON.getString("clientName");

            JSONArray details = (JSONArray) bodyJSON.get("details");
            String console = "";

            for (int x = 0; x < details.length(); x++) {
                JSONObject detail = new JSONObject(details.get(x).toString());

                console += "[ " + detail.getString("plateName") + " ]\n" +
                        "Precio: " + detail.getString("platePrice") + "\n" +
                        "Cantidad: " + detail.getString("plateCounter") + "\n" +
                        "Tiempo: " + detail.getString("plateTime") + "\n";
                console += "---------------------------------------------\n\n";
                totalTime += detail.getInt("plateTime");
            }

            console += "===================================\n";
            console += " Importe total: S/. " + amount + "\n\n";

            orderConsole.setText(console);

            //ID
            orderID.setText("ORDER: " + orderObjectId);

            //Client
            clientName.setText(cname);
            clientDni.setText(String.valueOf(cdni));
            clientPhone.setText(String.valueOf(cphone));

            //Time
            price.setText(String.valueOf(totalTime));

        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error parsing. " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(Const.DEBUG, "CPanel JSONException. ", e);
        }

    }
}
