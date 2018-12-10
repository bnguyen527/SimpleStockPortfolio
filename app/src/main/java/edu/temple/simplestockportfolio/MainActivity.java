package edu.temple.simplestockportfolio;

import android.content.res.Resources;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements PortfolioFragment.ParentListener {

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        Resources res = getResources();
        String[] stocks = res.getStringArray(R.array.stocks_array);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        PortfolioFragment portfolioFragment = PortfolioFragment.newInstance(stocks);
        fragmentTransaction.add(R.id.container1, portfolioFragment).commit();
    }

    @Override
    public void onStockPicked(String stock) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        StockDetailsFragment stockDetailsFragment = StockDetailsFragment.newInstance(stock);
        fragmentTransaction.replace(R.id.container1, stockDetailsFragment).addToBackStack(null).commit();
    }
}
