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

import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SendCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pe.rsantillanc.sanjoylaoadmin.R;
import pe.rsantillanc.sanjoylaoadmin.util.Const;

public class CPanelActivity extends AppCompatActivity {

    TextView orderID;
    TextView typeOrder;
    TextView clientName;
    TextView clientDni;
    TextView clientPhone;
    TextView orderConsole;
    EditText price;
    Button confirm;
    Button cancel;

    String amount;
    String orderObjectId;
    String userID;
    String type;

    boolean flagEnebled = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpanel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Log.e(Const.DEBUG, "CPanel OnCreate.");

        orderID = (TextView) findViewById(R.id.order_id);
        typeOrder = (TextView) findViewById(R.id.tv_type_order);
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
                if (flagEnebled)
                    sendNotification(Const.STATUS_CONFIRMED);
                else
                    Toast.makeText(getApplicationContext(), "Debe recibir una order!", Toast.LENGTH_LONG).show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flagEnebled)
                    sendNotification(Const.STATUS_CANCELLED);
                else
                    Toast.makeText(getApplicationContext(), "Debe recibir una order!", Toast.LENGTH_LONG).show();
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

        if (getIntent().getExtras() != null)
            onCretateOrder(getIntent());
    }

    private void sendNotification(int statusCode) {
        ParsePush confirm = new ParsePush();
        confirm.setData(buildJsonOrderStatus(statusCode));
        confirm.setChannel(Const.SJL_CHANNEL_CLIENT + userID);
        confirm.sendInBackground(new SendCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null)
                    Toast.makeText(getApplicationContext(), "Notificación enviada correctamente!", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(), "Error. " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private JSONObject buildJsonOrderStatus(int statusCode) {
        JSONObject body = new JSONObject();
        try {
            body.put("orderObjectId", orderObjectId);
            body.put("statusCode", statusCode);
            body.put("estimatedTime", Integer.parseInt(price.getText().toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return body;
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


        onCretateOrder(intent);
    }

    private void onCretateOrder(Intent intent) {
        long totalTime = 0;
        JSONObject dataParse;
        orderConsole.setText("");
        try {

            dataParse = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            JSONObject bodyJSON = dataParse.getJSONObject("data");
            amount = bodyJSON.getString("amount");
            orderObjectId = bodyJSON.getString("objectId");
            type = bodyJSON.getString("type");

            String cdni = bodyJSON.getString("clientDni");
            String cphone = bodyJSON.getString("clientPhone");
            String cname = bodyJSON.getString("clientName");
            userID = bodyJSON.getString("clientID");

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
            typeOrder.setText(type);

            //Client
            clientName.setText(cname);
            clientDni.setText(String.valueOf(cdni));
            clientPhone.setText(String.valueOf(cphone));

            //Time
            price.setText(String.valueOf(totalTime));

            flagEnebled = true;

        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error parsing. " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(Const.DEBUG, "CPanel JSONException. ", e);
        }

    }
}
