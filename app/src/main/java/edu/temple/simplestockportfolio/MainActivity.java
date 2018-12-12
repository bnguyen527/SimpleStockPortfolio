package edu.temple.simplestockportfolio;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements PortfolioFragment.ParentListener, AddStockFragment.DialogListener {

    private Portfolio portfolio;
    private FragmentManager fragmentManager;
    private FloatingActionButton floatingActionButton;
    private Thread portfolioUpdater;
    private File file;
    private final String FILENAME = "portfolio_file";

    Handler stocksAdder = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            try {
                JSONObject stocksInfo = new JSONObject((String) msg.obj);
                Stock stock = new Stock(stocksInfo);
                portfolio.addStock(stock);
                PortfolioFragment.onNewStockAdded(stock);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }
    });

    Handler stocksUpdater = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            try {
                JSONObject stocksInfo = new JSONObject((String) msg.obj);
                Stock stock = new Stock(stocksInfo);
                portfolio.updateStock(stock);
                PortfolioFragment.onStockUpdated();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        file = new File(getFilesDir(), FILENAME);
        portfolio = new Portfolio();
        try {
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String temp = reader.readLine();
            while (temp != null) {
                stringBuilder.append(temp);
                temp = reader.readLine();
            }
            JSONArray portfolioJSONArray = new JSONArray(stringBuilder.toString());
            portfolio = new Portfolio(portfolioJSONArray);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        PortfolioFragment portfolioFragment = PortfolioFragment.newInstance(portfolio);
        fragmentTransaction.add(R.id.container1, portfolioFragment).commit();
        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new AddStockFragment();
                dialog.show(fragmentManager, "AddStockFragment");
            }
        });

        portfolioUpdater = new Thread(new Runnable() {
            @Override
            public void run() {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    while (!Thread.interrupted()) {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            break;
                        }
                        for (Stock stock : portfolio.getStocks()) {
                            if (Thread.interrupted())
                                break;
                            try {
                                URL url = new URL("http://dev.markitondemand.com/MODApis/Api/v2/Quote/json/?symbol=" + stock.getSymbol());
                                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                                StringBuilder stringBuilder = new StringBuilder();
                                String temp = reader.readLine();
                                while (temp != null) {
                                    stringBuilder.append(temp);
                                    temp = reader.readLine();
                                }
                                String response = stringBuilder.toString();
                                Message msg = Message.obtain();
                                msg.obj = response;
                                stocksUpdater.sendMessage(msg);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
        portfolioUpdater.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        portfolioUpdater.interrupt();
    }

    @Override
    protected void onStop() {
        super.onStop();
        portfolioUpdater.interrupt();
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(portfolio.exportAsJSONArray().toString(4).getBytes());
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        portfolioUpdater = new Thread(new Runnable() {
            @Override
            public void run() {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    while (!Thread.interrupted()) {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            break;
                        }
                        for (Stock stock : portfolio.getStocks()) {
                            if (Thread.interrupted())
                                break;
                            try {
                                URL url = new URL("http://dev.markitondemand.com/MODApis/Api/v2/Quote/json/?symbol=" + stock.getSymbol());
                                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                                StringBuilder stringBuilder = new StringBuilder();
                                String temp = reader.readLine();
                                while (temp != null) {
                                    stringBuilder.append(temp);
                                    temp = reader.readLine();
                                }
                                String response = stringBuilder.toString();
                                Message msg = Message.obtain();
                                msg.obj = response;
                                stocksUpdater.sendMessage(msg);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
        portfolioUpdater.start();
    }

    public void onStockPicked(int position) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        StockDetailsFragment stockDetailsFragment = StockDetailsFragment.newInstance(portfolio.getStock(position));
        fragmentTransaction.replace(R.id.container1, stockDetailsFragment).addToBackStack(null).commit();
    }

    public void onNewStockAdded(DialogFragment dialog, final String ticker) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    try {
                        URL url = new URL("http://dev.markitondemand.com/MODApis/Api/v2/Quote/json/?symbol=" + ticker);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                        StringBuilder stringBuilder = new StringBuilder();
                        String temp = reader.readLine();
                        while (temp != null) {
                            stringBuilder.append(temp);
                            temp = reader.readLine();
                        }
                        String response = stringBuilder.toString();
                        Message msg = Message.obtain();
                        msg.obj = response;
                        stocksAdder.sendMessage(msg);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
