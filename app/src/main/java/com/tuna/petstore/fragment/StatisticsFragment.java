package com.tuna.petstore.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.tuna.petstore.R;
import com.tuna.petstore.adapter.HistoryAdapter;
import com.tuna.petstore.database.HistoryDAO;
import com.tuna.petstore.database.PetDAO;
import com.tuna.petstore.model.History;

import java.util.ArrayList;
import java.util.List;

public class StatisticsFragment extends Fragment {
    private ArrayList<String> PieEntryLabels;
    private CardView cardView;
    ConstraintLayout constraintLayout;
    private PetDAO petDAO;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.statistics_fragment, container, false);
        setHasOptionsMenu(true);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_viewass1);
        cardView = view.findViewById(R.id.cvs);
        constraintLayout = view.findViewById(R.id.lls);
        HistoryDAO historyDAO = new HistoryDAO(getContext());
        petDAO = new PetDAO(getContext());
        List<History> listhistory = historyDAO.getAllHistoryAsc();
        HistoryAdapter historyAdapter = new HistoryAdapter(getContext(), listhistory);
        recyclerView.setAdapter(historyAdapter);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);

        if (petDAO.getAllPet().size() != 0) {
            RelativeLayout relativeLayout = view.findViewById(R.id.rl);
            relativeLayout.setVisibility(View.VISIBLE);
        }
        if (historyDAO.getAllHistoryAsc().size() != 0) {
            constraintLayout.setVisibility(View.INVISIBLE);
            cardView.setVisibility(View.VISIBLE);
        }


        /////
        final CollapsingToolbarLayout collapsingToolbar = view.findViewById(R.id.coll);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = view.findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
        Glide.with(getContext()).load(R.drawable.pet).into((ImageView) view.findViewById(R.id.backdrop));
        ///


        PieChart pieChart = view.findViewById(R.id.chart1);
        ArrayList<Entry> entries = new ArrayList<>();
        PieEntryLabels = new ArrayList<>();
        if (petDAO.getKhoe() != 0) {
            entries.add(new BarEntry(petDAO.getKhoe(), 0));
        }
        if (petDAO.getBT() != 0) {
            entries.add(new BarEntry(petDAO.getBT(), 1));
        }
        if (petDAO.getYeu() != 0) {
            entries.add(new BarEntry(petDAO.getYeu(), 2));
        }

        AddValuesToPieEntryLabels();
        PieDataSet pieDataSet = new PieDataSet(entries, "");

        PieData pieData = new PieData(PieEntryLabels, pieDataSet);

        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        pieChart.setData(pieData);

        pieChart.animateY(3000);
        return view;
    }


    private void AddValuesToPieEntryLabels() {
        if (petDAO.getKhoe() != 0) {
            PieEntryLabels.add("Strong");
        }
        if (petDAO.getBT() != 0) {
            PieEntryLabels.add("Normal");
        }
        if (petDAO.getYeu() != 0) {
            PieEntryLabels.add("Weak");
        }

    }


}
