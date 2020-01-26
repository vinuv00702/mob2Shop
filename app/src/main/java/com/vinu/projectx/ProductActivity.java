package com.vinu.projectx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductActivity extends AppCompatActivity {
    JSONArray jsonArray;
    JSONObject object=null;
    ArrayList <Product>arrayList;
    Bitmap bitmapImage=null;
    List<Product>rowItems;
    ListView listView;
    List<Bitmap>bitmaps = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
//        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refreshData(); // your code
//                pullToRefresh.setRefreshing(false);


        new GetImage().execute();
    }

    class GetImage extends AsyncTask<Void,Void,JSONArray> {

        ProgressDialog progressDialog;

        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog = progressDialog.show(ProductActivity.this,"Please wait..!",null);
            progressDialog.setMessage("Loading");
        }

        @Override
        protected JSONArray doInBackground(Void... voids) {

            String url = "https://mob2shop.000webhostapp.com/mob2Shop/productJsonpush.php";

            try {
                HTTPURLConnection httpurlConnection = new HTTPURLConnection();
                HashMap<String, String> postDataParams = new HashMap<String, String>();

                String json = httpurlConnection.ServerData(url,postDataParams);

                if (json != null) {

                    jsonArray = new JSONArray(json);

                }
            }catch (JSONException e) {
                e.printStackTrace();
            }

            return jsonArray;
        }

        protected void onPostExecute(JSONArray result){
            super.onPostExecute(result);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            arrayList=new ArrayList<Product>();

            try {
                for (int i=0;i<result.length();i++) {
                    object= result.getJSONObject(i);

                    byte[] outImage = Base64.decode(object.getString("image"), Base64.DEFAULT);

                    bitmapImage= BitmapFactory.decodeByteArray(outImage,0,outImage.length);

                    bitmaps.add(bitmapImage);

                    String proid = object.getString("proid");

                    Product item = new Product(proid,object.getString("productname"),
                            object.getString("productprice"),bitmapImage);

                    arrayList.add(item);
                }

                //listView =(ListView)findViewById(R.id.listView);
                CustomListViewAdapter adapter = new CustomListViewAdapter(ProductActivity.this,R.layout.activity_layout,arrayList);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();


            }catch (final JSONException e){
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
