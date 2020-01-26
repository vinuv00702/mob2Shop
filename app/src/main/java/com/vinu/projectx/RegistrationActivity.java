package com.vinu.projectx;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class RegistrationActivity extends AppCompatActivity {

    TextView signIn_text;
    EditText userName, userEmail, passWord;
    Spinner spinCountry;
    Button signUp_button;
    String url = "https://mob2shop.000webhostapp.com/mob2Shop/register.php";
    public String name,email,pass;

    private ProgressDialog progressDialog;
    private HTTPURLConnection httpURLConnection;
    private JSONObject jsonObject;

    String pref = "Demo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_registration);

        signIn_text = findViewById(R.id.signIn_text);
        signIn_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                finish();
            }
        });

        userName = (EditText) findViewById(R.id.userName);
        userEmail = (EditText)findViewById(R.id.userEmail);
        passWord = (EditText)findViewById(R.id.passWord);
        spinCountry = (Spinner)findViewById(R.id.spinCountry);

        signUp_button = (Button)findViewById(R.id.signUp_button);
        signUp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getData();
            }
        });




    }

    private void getData() {

        name = userName.getText().toString();
        email = userEmail.getText().toString();
        pass = passWord.getText().toString();

        if(name.isEmpty()) {
            Toast.makeText(getApplicationContext(),"enter username",Toast.LENGTH_SHORT).show();
        }else if (email.isEmpty()) {
            Toast.makeText(getApplicationContext(),"enter email",Toast.LENGTH_SHORT).show();
        }else if (pass.isEmpty()) {
            Toast.makeText(getApplicationContext(),"enter password",Toast.LENGTH_SHORT).show();
        }else {

            new RegisterTask().execute(new String[]{url});
        }


    }

    private class RegisterTask extends AsyncTask <String,Integer,String>{

        String response = "";
        int success= 0;
        HashMap<String,String> postDataParams;

        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(RegistrationActivity.this);
            progressDialog.setMessage("Registering..");
            progressDialog.setCancelable(false);
            progressDialog.show();



        }

        @Override
        protected String doInBackground(String... strings) {

            postDataParams = new HashMap<>();
            postDataParams.put("name",name);
            postDataParams.put("email",email);
            postDataParams.put("pass",pass);

            httpURLConnection = new HTTPURLConnection();
            String url = strings[0];
            response = httpURLConnection.ServerData(url,postDataParams);
            try{
                jsonObject = new JSONObject(response);
                Log.d("Result",jsonObject.toString());
            }catch (Exception e){
                Log.d("ERROR",e.getMessage());
                return e.getMessage();
            }

            return response;
        }

        protected void onPostExecute(String result) {

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            try {
                success = jsonObject.getInt("success");

                if (success==1) {
                    Toast.makeText(getApplicationContext(),"Registered",
                            Toast.LENGTH_SHORT).show();

                    SharedPreferences sharedPreferences = getSharedPreferences(pref, Context.MODE_PRIVATE);

                    Intent intent =new Intent(RegistrationActivity.this,LoginActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT);
                }

            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}


