package edu.temple.simplestockportfolio;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class StockAdapter extends BaseAdapter {

    private Context context;
    private String[] stocks;

    StockAdapter(Context context, String[] stocks) {
        this.context = context;
        this.stocks = stocks;
    }

    @Override
    public int getCount() {
        return stocks.length;
    }

    @Override
    public Object getItem(int position) {
        return stocks[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView stockTextView = new TextView(context);
        stockTextView.setText(stocks[position]);
        stockTextView.setTextSize(28);
        return stockTextView;
    }
}
