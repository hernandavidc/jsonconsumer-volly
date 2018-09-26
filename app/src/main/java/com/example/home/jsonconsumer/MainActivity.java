package com.example.home.jsonconsumer;

import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
                //Construccion de la notificacion;
                NotificationCompat.Builder builder= new NotificationCompat.Builder(v.getContext());
                builder.setAutoCancel(true);
                builder.setContentTitle("Notificacion Basica");
                builder.setContentText("Done el listado de personas!");
                builder.setSubText("Toca para ver la documentacion acerca de Anndroid.");

                //Enviar la notificacion
                NotificationManager notificationManager= (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(1, builder.build());


            }
        });
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
