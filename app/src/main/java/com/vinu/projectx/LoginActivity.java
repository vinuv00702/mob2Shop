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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.HashMap;


public class LoginActivity extends AppCompatActivity {

    public TextView signUp_text;
    public EditText userName, passWord;
    public Button signIn_button;
    String name, pass;
    String url = "https://mob2shop.000webhostapp.com/mob2Shop/login.php";

    private ProgressDialog progressDialog;
    private HTTPURLConnection httpURLConnection;
    private JSONObject jsonObject;

    String pref = "Demo";
    SharedPreferences sharedPreferences;

    // Set Preference for the activity.
    private void setViewPreference(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setViewPreference();
        this.bindViewComponents();
        this.setUpViewActions();

        sharedPreferences = getSharedPreferences(pref,MODE_PRIVATE);
        boolean logged = sharedPreferences.getBoolean("logged",false);

        if (logged) {
            navigateToHome();
        }
    }

    private void navigateToHome(){
        int log_id=sharedPreferences.getInt("log_id",0);
        Intent intent = new Intent(LoginActivity.this, Home.class);
        intent.putExtra("log_id", log_id);
        startActivity(intent);
    }

    private void navigateToRegistration(){
        startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
    }

    // Binding the UI components in the Activity
    private void bindViewComponents(){
        userName = (EditText)findViewById(R.id.userName);
        passWord = (EditText)findViewById(R.id.passWord);
        signUp_text = findViewById(R.id.signUp_text);
        signIn_button = (Button)findViewById(R.id.signIn_button);
    }


    // Action listners for 'Sign In' and 'Sign Up'
    private void setUpViewActions(){
        final Context cntx = this;
        signUp_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToRegistration();
                finish();
            }
        });
        signIn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
                new NetworkService().loginRequest(cntx,name,pass);
            }
        });
    }



    private void showLoading(){
        progressDialog= new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Please Wait..!");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }


    private void validate() {
        pass = passWord.getText().toString();
        name = userName.getText().toString();
        if (name.isEmpty()||pass.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Enter Username&Password",
                    Toast.LENGTH_SHORT).show();
        } else {
            new LoginTask().execute(new String[]{url});
        }
    }

     class LoginTask extends AsyncTask<String,Void,String> {

        String response;
        HashMap<String,String>postParams;
        int log_id = 0;

        protected void onPreExecute(){
            super.onPreExecute();

            progressDialog= new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Please Wait..!");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... strings) {

            postParams =  new HashMap<>();
            postParams.put("name", name);
            postParams.put("pass", pass);
            String url = strings[0];

            httpURLConnection = new HTTPURLConnection();
            response = httpURLConnection.ServerData(url,postParams);

            try {
                jsonObject = new JSONObject(response);
                Log.d("result",jsonObject.toString());
            }catch (JSONException e){
                e.printStackTrace();
            }
            return response;
        }

        protected void onPostExecute(String s){

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            try {
                log_id = Integer.parseInt(jsonObject.getString("success"));
                Toast.makeText(LoginActivity.this, "" + name, Toast.LENGTH_SHORT).show();

                if (log_id != 0) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("logged", true);
                    editor.putInt("log_id", log_id);
                    editor.putString("name", name);
                    editor.putString("pass", pass);
                    editor.commit();
                    Intent intent = new Intent(LoginActivity.this, Home.class);
                    intent.putExtra("log_id", log_id);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
