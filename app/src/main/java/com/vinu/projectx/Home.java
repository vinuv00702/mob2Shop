package com.vinu.projectx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Home extends AppCompatActivity {

    JSONArray jsonArray;
    JSONObject object = null;
    ArrayList<Product> arrayList;
    Bitmap bitmapImage = null;
    List<Product> rowItems;
    ListView listView;
    List<Bitmap> bitmaps = new ArrayList<>();
    RecyclerView recyclerView;
    CardView cardView;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);


        new ImageGet().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent serverIntent = null;
        switch (item.getItemId()) {
            case R.id.item1:
                SharedPreferences preferences =getSharedPreferences("Demo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                finish();
                return true;
        }
        return false;
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();

            //moveTaskToBack(false);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exitByBackKey() {
        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to exit application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {

                        finish();
                        //close();


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }


    private class ImageGet extends AsyncTask<Void, Void, JSONArray> {

        ProgressDialog progressDialog;

        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = progressDialog.show(Home.this, "Please wait..!", null);
            progressDialog.setMessage("Loading");
        }

        @Override
        protected JSONArray doInBackground(Void... voids) {

            String url = "https://mob2shop.000webhostapp.com/mob2Shop/productJsonpush.php";

            try {
                HTTPURLConnection httpurlConnection = new HTTPURLConnection();
                HashMap<String, String> postDataParams = new HashMap<String, String>();

                String json = httpurlConnection.ServerData(url, postDataParams);

                if (json != null) {

                    jsonArray = new JSONArray(json);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jsonArray;
        }

        protected void onPostExecute(JSONArray result) {
            super.onPostExecute(result);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            arrayList = new ArrayList<Product>();

            try {
                for (int i = 0; i < result.length(); i++) {
                    object = result.getJSONObject(i);

                    byte[] outImage = Base64.decode(object.getString("image"), Base64.DEFAULT);

                    bitmapImage = BitmapFactory.decodeByteArray(outImage, 0, outImage.length);

                    bitmaps.add(bitmapImage);

                    String proid = object.getString("proid");

                    Product item = new Product(proid, object.getString("productname"),
                            object.getString("productprice"), bitmapImage);

                    arrayList.add(item);
                }

                recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
                ProductAdapter adapter = new ProductAdapter(Home.this,arrayList);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(adapter);

                adapter.notifyDataSetChanged();
                recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {


                    GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

                        @Override public boolean onSingleTapUp(MotionEvent motionEvent) {

                            return true;
                        }

                    });
                    @Override
                    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                       View child = rv.findChildViewUnder(e.getX(), e.getY());

                        if(child != null && gestureDetector.onTouchEvent(e)) {

                            int RecyclerViewItemPosition = rv.getChildAdapterPosition(child);

//                            Intent intent = new Intent(getApplicationContext(),ForumActivity.class);
////                            Bundle bundle = new Bundle();
////                            bundle.putInt("image",bitmapImage);
//                            try {
//                                intent.putExtra("name",object.getString("productname"));
//                                intent.putExtra("price",object.getString("productprice"));
//                                intent.putExtra("image",object.getString("image"));
//
//
//                            } catch (JSONException ex) {
//                                ex.printStackTrace();
//                            }
//                            startActivity(intent);
//
//                            //Toast.makeText(MainActivity.this, SubjectNames.get(RecyclerViewItemPosition), Toast.LENGTH_LONG).show();
                        }
                        return false;
                    }

                    @Override
                    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {


                    }

                    @Override
                    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                    }
                });


            } catch (final JSONException e) {
                //e.printStackTrace();
                Log.e("json", "Json parsing error: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
        }
    }
}
