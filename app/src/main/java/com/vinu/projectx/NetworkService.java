package com.vinu.projectx;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NetworkService  {


    RequestQueue QUEUE;
    String URL;


    public void loginRequest(Context context, final String username, final String password){

        QUEUE = Volley.newRequestQueue(context);
        String baseUrl = context.getResources().getString(R.string.BASE_URL);
        String url = baseUrl+"mob2Shop/login.php";

        StringRequest request = new StringRequest(Request.Method.POST, baseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Hii");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error");
                    }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> parms = new HashMap<String, String>();
                parms.put("name",username);
                parms.put("pass",password);
                return parms;
            }
        };

        QUEUE.add(request);
    }


}
