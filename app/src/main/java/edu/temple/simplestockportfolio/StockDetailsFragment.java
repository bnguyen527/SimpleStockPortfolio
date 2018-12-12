package edu.temple.simplestockportfolio;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StockDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StockDetailsFragment extends Fragment {
    private static final String STOCK_KEY = "stock";

    private Stock stock;
    private TextView stockSymbol;
    private TextView stockName;
    private TextView stockPrice;
    private TextView stockOpenPrice;
    private WebView stockChart;

    public StockDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param stock stock.
     * @return A new instance of fragment StockDetailsFragment.
     */
    public static StockDetailsFragment newInstance(Stock stock) {
        StockDetailsFragment fragment = new StockDetailsFragment();
        Bundle args = new Bundle();
        args.putByteArray(STOCK_KEY, stock.serialize());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            stock = Stock.deserialize(getArguments().getByteArray(STOCK_KEY));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stock_details, container, false);
        stockSymbol = view.findViewById(R.id.stockSymbol);
        stockName = view.findViewById(R.id.stockName);
        stockPrice = view.findViewById(R.id.stockPrice);
        stockOpenPrice = view.findViewById(R.id.stockOpenPrice);
        stockChart = view.findViewById(R.id.stockChart);
        stockSymbol.setText(stock.getSymbol());
        stockName.setText(stock.getCompanyName());
        stockPrice.setText("Last Price: $" + stock.getPrice());
        stockOpenPrice.setText("Opening Price: $" + stock.getOpenPrice());
        stockChart.getSettings().setJavaScriptEnabled(true);
        stockChart.loadUrl("https://macc.io/lab/cis3515/?symbol=" + stock.getSymbol());
        return view;
    }
}
