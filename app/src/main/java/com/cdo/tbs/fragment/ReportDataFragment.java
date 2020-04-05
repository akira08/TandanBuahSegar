package com.cdo.tbs.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.cdo.tbs.R;
import com.cdo.tbs.dialogfragment.AEReportDataDialogFragment;
import com.cdo.tbs.dialogfragment.DeleteReportDataDialogFragment;
import com.cdo.tbs.model.Report;
import com.cdo.tbs.model.ReportData;
import com.cdo.tbs.sqlite.TbsReportCrud;
import com.cdo.tbs.sqlite.TbsReportDataCrud;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportDataFragment extends Fragment {
    @BindView(R.id.reportData_tl) TableLayout reportData_tl;
    @BindView(R.id.addReportData_fb) FloatingActionButton addReportData_fb;
    @BindView(R.id.noData_tv) TextView noData_tv;

    private Report report;
    private List<ReportData> reportDataList;
    private TbsReportDataCrud reportDataCrud;
    private DecimalFormat decim = new DecimalFormat("#,###.##");

    private AEReportDataDialogFragment AEReportDataDialog;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_data, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        report = (Report) getArguments().getSerializable(ReportFragment.REPORT);
        reportDataCrud = new TbsReportDataCrud(getContext());

        getReportDataById();

        addReportData_fb.setOnClickListener(v -> {
            AEReportDataDialog = AEReportDataDialogFragment.newInstance(
                    AEReportDataDialogFragment.ADD_ACTION,
                    getString(R.string.add_report_data_title),
                    getString(R.string.add),
                    null,
                    report
            );
            AEReportDataDialog.show(getChildFragmentManager(), "addReportDataDialog");
        });
    }

    public void showUpdateDialog(ReportData reportData){
        AEReportDataDialog = AEReportDataDialogFragment.newInstance(
                AEReportDataDialogFragment.EDIT_ACTION,
                getString(R.string.edit_report_data_title),
                getString(R.string.save),
                reportData,
                report
        );
        AEReportDataDialog.show(getChildFragmentManager(), "editReportDataDialog");
    }

    public void showDeleteDialog(ReportData reportData){
        DeleteReportDataDialogFragment deleteReportDataDialog = DeleteReportDataDialogFragment.newInstance(reportData);
        deleteReportDataDialog.show(getChildFragmentManager(), "deleteReportDataDialog");
    }

    public void getReportDataById(){
        reportDataList = reportDataCrud.getReportDataByIdReport(report.getId());

        if(reportDataList.size() > 0){
            reportData_tl.setVisibility(View.VISIBLE);
            noData_tv.setVisibility(View.GONE);
            initTableContent();
        } else {
            reportData_tl.setVisibility(View.GONE);
            noData_tv.setVisibility(View.VISIBLE);
        }
    }

    public boolean addReportData(String date, int unit, int tons, int margin, long idReport) {
        long insertRes = reportDataCrud.insertReportData(date, unit, tons, margin, idReport);

        if(insertRes == -1){
            return false;
        } else {
            getReportDataById();
            return true;
        }
    }

    public boolean updateReportData(ReportData reportData, String date, int unit, int tons, int margin){
        int updateRes = reportDataCrud.updateReportData(reportData, date, unit, tons, margin);

        if(updateRes == 0){
            return false;
        } else {
            getReportDataById();
            return true;
        }
    }

    public boolean deleteReportData(ReportData reportData){
        int deleteRes = reportDataCrud.deleteReportData(reportData);

        if(deleteRes == 0){
            return false;
        } else {
            getReportDataById();
            return true;
        }
    }

    public void initTableHeader(){
        TableRow tableRow = new TableRow(getContext());
        tableRow.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.tableHeader));

        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(5, 20, 5, 20);

        TextView date = new TextView(getContext());
        date.setText(R.string.date_label);
        date.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        date.setLayoutParams(params);
        date.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
        date.setMinWidth(400);
        date.setTypeface(date.getTypeface(), Typeface.BOLD);
        date.setTextSize(15);
        tableRow.addView(date);

        TextView unit = new TextView(getContext());
        unit.setText(R.string.unit_label);
        unit.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        unit.setLayoutParams(params);
        unit.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
        unit.setMinWidth(400);
        unit.setTypeface(unit.getTypeface(), Typeface.BOLD);
        unit.setTextSize(15);
        tableRow.addView(unit);

        TextView tons = new TextView(getContext());
        tons.setText(R.string.tons_label);
        tons.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tons.setLayoutParams(params);
        tons.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
        tons.setMinWidth(400);
        tons.setTypeface(tons.getTypeface(), Typeface.BOLD);
        tons.setTextSize(15);
        tableRow.addView(tons);

        TextView margin = new TextView(getContext());
        margin.setText(R.string.margin_label);
        margin.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        margin.setLayoutParams(params);
        margin.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
        margin.setMinWidth(400);
        margin.setTypeface(margin.getTypeface(), Typeface.BOLD);
        margin.setTextSize(15);
        tableRow.addView(margin);

        TextView txmTv = new TextView(getContext());
        txmTv.setText(R.string.txm_label);
        txmTv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        txmTv.setLayoutParams(params);
        txmTv.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
        txmTv.setMinWidth(400);
        txmTv.setTypeface(txmTv.getTypeface(), Typeface.BOLD);
        txmTv.setTextSize(15);
        tableRow.addView(txmTv);

        TextView action = new TextView(getContext());
        action.setText(R.string.action_label);
        action.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        action.setLayoutParams(params);
        action.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
        action.setMinWidth(400);
        action.setTypeface(action.getTypeface(), Typeface.BOLD);
        action.setTextSize(15);
        tableRow.addView(action);

        reportData_tl.addView(tableRow);
    }

    public void initTableContent(){
        reportData_tl.removeAllViews();
        initTableHeader();
        int sumTxm = 0;
        int sumTons = 0;
        for(int i=0 ; i<reportDataList.size() ; i++){
            ReportData reportData = reportDataList.get(i);

            TableRow tableRow = new TableRow(getContext());

            TextView date = new TextView(getContext());
            date.setText(reportDataList.get(i).getDate());
            date.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            date.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
            date.setGravity(Gravity.CENTER);
            date.setTextSize(15);
            tableRow.addView(date);

            TextView unit = new TextView(getContext());
            unit.setText(String.valueOf(reportDataList.get(i).getUnit()));
            unit.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
            unit.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            unit.setTextSize(15);
            tableRow.addView(unit);

            TextView tons = new TextView(getContext());
            sumTons += reportDataList.get(i).getTons();
            tons.setText(decim.format(reportDataList.get(i).getTons()));
            tons.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
            tons.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tons.setTextSize(15);
            tableRow.addView(tons);

            TextView margin = new TextView(getContext());
            margin.setText(decim.format(reportDataList.get(i).getMargin()));
            margin.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
            margin.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            margin.setTextSize(15);
            tableRow.addView(margin);

            int txm = reportDataList.get(i).getTons() * reportDataList.get(i).getMargin();
            sumTxm += txm;
            TextView txmTv = new TextView(getContext());
            txmTv.setText(decim.format(txm));
            txmTv.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
            txmTv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            txmTv.setTextSize(15);
            tableRow.addView(txmTv);

            TableRow.LayoutParams paramButton = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            );
            paramButton.gravity = Gravity.CENTER;

            LinearLayout buttonWrapper = new LinearLayout(getContext());
            buttonWrapper.setOrientation(LinearLayout.HORIZONTAL);
            buttonWrapper.setLayoutParams(paramButton);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.width = 200;

            TypedValue outValue = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);

            ImageButton edit = new ImageButton(getContext());
            edit.setImageResource(R.drawable.ic_edit);
            edit.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.edit_button));
            edit.setForeground(ContextCompat.getDrawable(getContext(), outValue.resourceId));
            edit.setLayoutParams(params);
            edit.setOnClickListener(v -> {
                showUpdateDialog(reportData);
            });

            buttonWrapper.addView(edit);

            ImageButton delete = new ImageButton(getContext());
            delete.setImageResource(R.drawable.ic_delete);
            delete.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.delete_button));
            delete.setForeground(ContextCompat.getDrawable(getContext(), outValue.resourceId));
            delete.setLayoutParams(params);
            delete.setOnClickListener(v -> {
                showDeleteDialog(reportData);
            });

            buttonWrapper.addView(delete);

            tableRow.addView(buttonWrapper);

            reportData_tl.addView(tableRow);
        }

        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(5, 20, 5, 20);

        TableRow tableRow = new TableRow(getContext());
        tableRow.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.tableHeader));

        TextView total = new TextView(getContext());
        total.setText(R.string.total);
        total.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
        total.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        total.setTypeface(total.getTypeface(), Typeface.BOLD);
        total.setTextSize(15);
        total.setLayoutParams(params);

        TextView totalTons = new TextView(getContext());
        totalTons.setText(decim.format(sumTons));
        totalTons.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
        totalTons.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        totalTons.setTypeface(totalTons.getTypeface(), Typeface.BOLD);
        totalTons.setTextSize(15);
        totalTons.setLayoutParams(params);

        TextView totalTxm = new TextView(getContext());
        totalTxm.setText(decim.format(sumTxm));
        totalTxm.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
        totalTxm.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        totalTxm.setTypeface(totalTxm.getTypeface(), Typeface.BOLD);
        totalTxm.setTextSize(15);
        totalTxm.setLayoutParams(params);

        tableRow.addView(total);
        tableRow.addView(new RelativeLayout(getContext()));
        tableRow.addView(totalTons);
        tableRow.addView(new RelativeLayout(getContext()));
        tableRow.addView(totalTxm);
        tableRow.addView(new RelativeLayout(getContext()));

        reportData_tl.addView(tableRow);

    }
}
