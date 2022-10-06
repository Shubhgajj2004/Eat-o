package com.shubh.eato.Models;

public class ItemsExploreModel {

    private String Img, ItemName, Key;
    private int ItemPrice, ItemRating, MonthlyBuy;

    public ItemsExploreModel(){}

    public ItemsExploreModel(int itemPrice, int itemRating, int monthlyBuy, String img, String itemName) {
        ItemPrice = itemPrice;
        ItemRating = itemRating;
        MonthlyBuy = monthlyBuy;
        Img = img;
        ItemName = itemName;

    }

    public ItemsExploreModel(String key) {
        Key = key;
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

    public int getItemRating() {
        return ItemRating;
    }

    public void setItemRating(int itemRating) {
        ItemRating = itemRating;
    }

    public int getMonthlyBuy() {
        return MonthlyBuy;
    }

    public void setMonthlyBuy(int monthlyBuy) {
        MonthlyBuy = monthlyBuy;
    }
}
