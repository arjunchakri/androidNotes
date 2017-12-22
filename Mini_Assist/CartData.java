package com.sherlock.shoppingassist;

public class CartData {
    public String productId;
    public String productName;
    public String productPrice;
    public String productQuantity;

    public CartData(String a,String b, String c,String d){
        productId = a;
        productName = b;
        productPrice = c;
        productQuantity = d;
    }
}
