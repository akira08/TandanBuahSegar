package com.cdo.tbs.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cdo.tbs.R;
import com.cdo.tbs.adapter.ReportAdapter;
import com.cdo.tbs.dialogfragment.AEReportDialogFragment;
import com.cdo.tbs.dialogfragment.DeleteReportDialogFragment;
import com.cdo.tbs.model.Report;
import com.cdo.tbs.sqlite.TbsReportCrud;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportFragment extends Fragment implements ReportAdapter.EventListener {
    public static final String REPORT = "report";
    private static final String TAG = ReportFragment.class.getSimpleName();
    private TbsReportCrud reportCrud;
    private List<Report> reportList = new ArrayList<>();
    private AEReportDialogFragment AEReportDialog;
    private DeleteReportDialogFragment deleteReportDialog;
    private ReportAdapter adapter;

    @BindView(R.id.report_rv) RecyclerView report_rv;
    @BindView(R.id.noData_tv) TextView noData_tv;
    @BindView(R.id.addReport_fb) FloatingActionButton addReport_fb;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new ReportAdapter(this);
        reportCrud = new TbsReportCrud(getContext());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        report_rv.setLayoutManager(mLayoutManager);
        report_rv.setItemAnimator(new DefaultItemAnimator());
        report_rv.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        report_rv.setAdapter(adapter);

        getAllReport();

        addReport_fb.setOnClickListener(v -> {
            AEReportDialog = AEReportDialogFragment.newInstance(
                    AEReportDialogFragment.ADD_ACTION,
                    getString(R.string.add_report_title),
                    getString(R.string.add),
                    null
            );
            AEReportDialog.show(getChildFragmentManager(), "addReportDialog");
        });
    }

    public void getAllReport(){
        reportList = reportCrud.getReportAll();

        if(reportList.size() > 0){
            report_rv.setVisibility(View.VISIBLE);
            noData_tv.setVisibility(View.GONE);
        } else {
            report_rv.setVisibility(View.GONE);
            noData_tv.setVisibility(View.VISIBLE);
        }

        adapter.addReportList(reportList);
    }

    public boolean addReport(String title) {
        long insertRes = reportCrud.insertReport(title);

        if(insertRes == -1){
            return false;
        } else {
            getAllReport();
            return true;
        }
    }

    public boolean updateReport(Report report, String title){
        int updateRes = reportCrud.updateReport(report, title);

        if(updateRes == 0){
            return false;
        } else {
            getAllReport();
            return true;
        }
    }

    public boolean deleteReport(Report report){
        int deleteRes = reportCrud.deleteReport(report);

        if(deleteRes == 0){
            return false;
        } else {
            getAllReport();
            return true;
        }
    }

    @Override
    public void showUpdateReport(Report report) {
        AEReportDialog = AEReportDialogFragment.newInstance(
                AEReportDialogFragment.EDIT_ACTION,
                getString(R.string.edit_report_title),
                getString(R.string.save),
                report
        );
        AEReportDialog.show(getChildFragmentManager(), "editReportDialog");
    }

    @Override
    public void showDeleteReport(Report report) {
        deleteReportDialog = DeleteReportDialogFragment.newInstance(report);
        deleteReportDialog.show(getChildFragmentManager(), "deleteReportDialog");
    }

    @Override
    public void moveToReportDataFragment(Report report) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(REPORT, report);
        NavHostFragment.findNavController(ReportFragment.this)
                .navigate(R.id.action_ReportFragment_to_ReportDataFragment, bundle);
    }
}
