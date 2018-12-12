package edu.temple.simplestockportfolio;

import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

class Stock implements Serializable {

    private String symbol;
    private String companyName;
    private double price;
    private double openPrice;

    public Stock(String symbol, String companyName, double price, double openPrice) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.price = price;
        this.openPrice = openPrice;
    }

    Stock(JSONObject stock) {
        try {
            this.symbol = stock.getString("Symbol");
            this.companyName = stock.getString("Name");
            this.price = stock.getDouble("LastPrice");
            this.openPrice = stock.getDouble("Open");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    @Override
    public boolean equals(Object obj) {
        Stock otherStock = (Stock) obj;
        return symbol.equalsIgnoreCase(otherStock.symbol);
    }

    public JSONObject getStockAsJSON() {
        JSONObject stock = new JSONObject();
        try {
            stock.put("Symbol", symbol);
            stock.put("Name", companyName);
            stock.put("LastPrice", price);
            stock.put("Open", openPrice);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return stock;
    }

    public byte[] serialize() {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOut = new ObjectOutputStream(byteOut);
            objectOut.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteOut.toByteArray();
    }

    @Nullable
    public static Stock deserialize(byte[] bytes) {
        ByteArrayInputStream byteIn = new ByteArrayInputStream(bytes);
        try {
            ObjectInputStream objectIn = new ObjectInputStream(byteIn);
            return (Stock) objectIn.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
