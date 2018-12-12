package edu.temple.simplestockportfolio;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class PortfolioAdapter extends BaseAdapter {

    private Context context;
    private Portfolio portfolio;

    PortfolioAdapter(Context context, Portfolio portfolio) {
        this.context = context;
        this.portfolio = portfolio;
    }

    @Override
    public int getCount() {
        return portfolio.size();
    }

    @Override
    public Object getItem(int position) {
        return portfolio.getStock(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView stockTextView = new TextView(context);
        Stock cur = portfolio.getStock(position);
        stockTextView.setText(cur.getSymbol() + ": $" + cur.getPrice());
        if (cur.getPrice() >= cur.getOpenPrice())
            stockTextView.setBackgroundColor(Color.GREEN);
        else
            stockTextView.setBackgroundColor(Color.RED);
        stockTextView.setTextSize(28);
        return stockTextView;
    }
}
