package com.shubh.eato.Models;

public class itemCartModel {

    private String Img, ItemName, Key;
    private int ItemPrice, Amount;

    public itemCartModel(){}

    public itemCartModel(String img, String itemName, int itemPrice, int amount) {
        Img = img;
        ItemName = itemName;
        ItemPrice = itemPrice;
        Amount = amount;
    }

    public itemCartModel(String Key)
    {
        this.Key = Key;
    }

    public String getImg() {
        return Img;
    }

    public void setImg(String img) {
        Img = img;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public int getItemPrice() {
        return ItemPrice;
    }

    public void setItemPrice(int itemPrice) {
        ItemPrice = itemPrice;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }
}
