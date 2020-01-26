package com.vinu.projectx;

import android.graphics.Bitmap;

public class Product {
        private String id;
        private String name;
        private String price;
        private Bitmap image;

        public Product(String id, String name, String price, Bitmap image) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.image = image;
        }

//    public Product(int i, String s, String s1, double v, int i1, int macbook) {
//    }

        public String getId() {
            return id;
        }

        public void setId(String id){
            this.id=id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name){
            this.name=name;
        }


        public String getPrice() {
            return price;
        }

        public void setPrice(String price){
            this.price=price;
        }

        public Bitmap getImage() {
            return image;
        }

        public void setImage(Bitmap image){
            this.image=image;
        }
}

