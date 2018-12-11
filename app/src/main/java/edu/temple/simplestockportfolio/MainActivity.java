package edu.temple.simplestockportfolio;

import android.content.res.Resources;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements PortfolioFragment.ParentListener, AddStockFragment.DialogListener {

    FragmentManager fragmentManager;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        Resources res = getResources();
        ArrayList<String> stocks = new ArrayList<>(Arrays.asList(res.getStringArray(R.array.stocks_array)));
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        PortfolioFragment portfolioFragment = PortfolioFragment.newInstance(stocks);
        fragmentTransaction.add(R.id.container1, portfolioFragment).commit();

        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new AddStockFragment();
                dialog.show(fragmentManager, "AddStockFragment");
            }
        });
    }

    public void onStockPicked(String stock) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        StockDetailsFragment stockDetailsFragment = StockDetailsFragment.newInstance(stock);
        fragmentTransaction.replace(R.id.container1, stockDetailsFragment).addToBackStack(null).commit();
    }

    public void onNewStockAdded(DialogFragment dialog, String stock) {
        PortfolioFragment.onNewStockAdded(stock);
    }
}
