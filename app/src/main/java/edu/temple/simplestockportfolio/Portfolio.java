package edu.temple.simplestockportfolio;

import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

class Portfolio implements Serializable {

    private ArrayList<Stock> stocks;

    Portfolio() {
        stocks = new ArrayList<>();
    }

    Portfolio(ArrayList<Stock> stocks) {
        this.stocks = stocks;
    }

    Portfolio(JSONArray portfolioJSONArray) {
        stocks = new ArrayList<>();
        for (int i = 0; i < portfolioJSONArray.length(); i++) {
            try {
                addStock(new Stock(portfolioJSONArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void addStock(Stock stock) {
        if (findStockPosition(stock) < 0)
            stocks.add(stock);
    }

    public void updateStock(Stock stock) {
        int position = findStockPosition(stock);
        if (position >= 0)
            stocks.set(position, stock);
    }

    public boolean isEmpty() {
        return stocks.isEmpty();
    }

    public Stock getStock(int position) {
        return stocks.get(position);
    }

    private int findStockPosition(Stock stock) {
        return stocks.indexOf(stock);
    }

    public int size() {
        return stocks.size();
    }

    public JSONArray exportAsJSONArray() {
        JSONArray portfolioJSONArray = new JSONArray();
        for (Stock stock : stocks)
            portfolioJSONArray.put(stock.getStockAsJSON());
        return portfolioJSONArray;
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
    public static Portfolio deserialize(byte[] bytes) {
        ByteArrayInputStream byteIn = new ByteArrayInputStream(bytes);
        try {
            ObjectInputStream objectIn = new ObjectInputStream(byteIn);
            return (Portfolio) objectIn.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Stock> getStocks() {
        return stocks;
    }
}
