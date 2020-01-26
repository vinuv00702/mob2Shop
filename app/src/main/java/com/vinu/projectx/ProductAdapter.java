package com.vinu.projectx;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    public  String KEY_NAME = "name";
    public  String KEY_PRICE = "price";



    private Context context;
    private List<Product>item;
    private CardView cardView;

    public ProductAdapter(Context context, List<Product>item) {
        this.context= context;
        this.item = item;
    }
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_layout,parent,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductAdapter.ProductViewHolder holder, final int position) {

         final Product product =item.get(position);

        holder.name.setText(product.getName());
        holder.price.setText("INR: " + product.getPrice()+" /-");
        holder.image.setImageBitmap(product.getImage());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product1 = item.get(position);
                Intent i =new Intent(v.getContext(),ForumActivity.class);
                i.putExtra(KEY_NAME,product1.getName());
                i.putExtra(KEY_PRICE,product1.getPrice());
                //i.putExtra("image",product1.getImage());

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                product1.getImage().compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
                byte[] bytes = byteArrayOutputStream.toByteArray();

               i.putExtra("image",bytes);
//
//                Bundle bundle = new Bundle();
//                bundle.putParcelable("image", product1.getImage() );



                v.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        public TextView name,price;
        public ImageView image;
        public LinearLayout linearLayout;
        public CardView cardView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.textViewTitle);
            price = (TextView)itemView.findViewById(R.id.textViewPrice);
            image = (ImageView)itemView.findViewById(R.id.imageView);
//            linearLayout = (LinearLayout)itemView.findViewById(R.id.linearLayout);
            cardView = (CardView)itemView.findViewById(R.id.cardView);
        }
    }
}
