package com.example.home.jsonconsumer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.os.Build;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    //De tipo volly, esta variable es donde se van a hacer las peticiones, se hace en segundo plano, es como una cola
    private RequestQueue queue;
    private TextView mTextView;

    private final static int NOTIFICACION_ID = 0;
    private final static String CHANNEL_ID = "NOTIFICACION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.txtViewID);
        queue = Volley.newRequestQueue(this);

        final Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                obtenerDatosVolley();
                createNotificationChannel();
                createNotification();
            }
        });
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Noticacion";
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void createNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_sms_black_24dp);
        builder.setContentTitle("Notificacion Android");
        builder.setContentText("Cargado el listado de nombres");
        builder.setColor(Color.BLUE);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setLights(Color.MAGENTA, 1000, 1000);
        builder.setVibrate(new long[]{1000,1000,1000,1000,1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());
    }

    public void obtenerDatosVolley(){
        String url = "https://api.androidhive.info/contacts/";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray JsonRes = response.getJSONArray("contacts");
                    for(int i =0; i <JsonRes.length(); i++){
                        JSONObject objectoContact = JsonRes.getJSONObject(i);
                        String name = objectoContact.getString("name");
                        String presentValStr = mTextView.getText().toString();
                        presentValStr += "\n" + name;
                        mTextView.setText(presentValStr);
                        Toast.makeText(MainActivity.this, "nombres: "+name, Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e){
                    Toast.makeText(MainActivity.this, "Error en el metodo GET", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            //excepcion
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }
}
