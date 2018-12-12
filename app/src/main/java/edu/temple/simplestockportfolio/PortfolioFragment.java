package edu.temple.simplestockportfolio;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ParentListener} interface
 * to handle interaction events.
 * Use the {@link PortfolioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PortfolioFragment extends Fragment {

    private static final String PORTFOLIO_KEY = "portfolio";

    private static Portfolio portfolio;
    private static TextView addStockHint;
    private ListView portfolioListView;
    private static PortfolioAdapter portfolioAdapter;

    private ParentListener parentListener;

    public PortfolioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param portfolio stock portfolio.
     * @return A new instance of fragment PortfolioFragment.
     */
    public static PortfolioFragment newInstance(Portfolio portfolio) {
        PortfolioFragment fragment = new PortfolioFragment();
        Bundle args = new Bundle();
        args.putByteArray(PORTFOLIO_KEY, portfolio.serialize());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            portfolio = Portfolio.deserialize(getArguments().getByteArray(PORTFOLIO_KEY));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_portfolio, container, false);
        addStockHint = view.findViewById(R.id.addStockHint);
        if (portfolio.isEmpty())
            addStockHint.setText(R.string.add_stock_hint);
        portfolioListView = view.findViewById(R.id.portfolioListView);
        portfolioAdapter = new PortfolioAdapter((Context) parentListener, portfolio);
        portfolioListView.setAdapter(portfolioAdapter);
        portfolioListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                parentListener.onStockPicked(position);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ParentListener) {
            parentListener = (ParentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement ParentListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        parentListener = null;
    }

    public static void onNewStockAdded(Stock stock) {
        if (portfolio.isEmpty())
            addStockHint.setText("");
        portfolio.addStock(stock);
        portfolioAdapter.notifyDataSetChanged();
    }

    public static void onStockUpdated() {
        portfolioAdapter.notifyDataSetChanged();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface ParentListener {
        void onStockPicked(int position);
    }
}
