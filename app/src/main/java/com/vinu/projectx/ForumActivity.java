package com.vinu.projectx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ForumActivity extends AppCompatActivity {
    ImageView imageView;
    TextView textName, textPrice,textDesc;
    Button buyBtn, cartBtn;
    Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        textName = (TextView)findViewById(R.id.textName);
        textPrice = (TextView)findViewById(R.id.textPrice);
        imageView = (ImageView)findViewById(R.id.imageView2);

        Intent intent = getIntent();
        //Bitmap bitmap = getIntent().getExtras().getParcelable("image");

//        byte[] bytes = getIntent().getByteArrayExtra("image");
//        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        String name = intent.getStringExtra("name");
        String price = intent.getStringExtra("price");
        Bitmap bitmap = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("image"),
                0,getIntent().getByteArrayExtra("image").length);


       // bitmap = intent.getExtras().getParcelable("image");



        textPrice.setText("INR: "+price+"/-");
        textName.setText(name);
        imageView.setImageBitmap(bitmap);

//        Picasso.get()
//                .load(url)
//                .into(imageView);

    }
}
