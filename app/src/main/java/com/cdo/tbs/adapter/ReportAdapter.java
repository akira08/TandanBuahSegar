package com.cdo.tbs.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cdo.tbs.R;
import com.cdo.tbs.model.Report;

import java.util.ArrayList;
import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {
    private EventListener listener;
    private List<Report> reportList = new ArrayList<>();

    public ReportAdapter(EventListener listener) {
        this.listener = listener;
    }

    public interface EventListener {
        void showUpdateReport(Report report);
        void showDeleteReport(Report report);
        void moveToReportDataFragment(Report report);
    }

    public class ReportViewHolder extends RecyclerView.ViewHolder {
        public TextView reportTitle_tv, dateCreated_tv;
        public RelativeLayout reportClickable_rl;
        public ImageButton editReport_btn, deleteReport_btn;

        public ReportViewHolder(View view) {
            super(view);
            reportTitle_tv = view.findViewById(R.id.reportTitle_tv);
            dateCreated_tv = view.findViewById(R.id.dateCreated_tv);
            reportClickable_rl = view.findViewById(R.id.reportClickable_rl);
            editReport_btn = view.findViewById(R.id.editReport_btn);
            deleteReport_btn = view.findViewById(R.id.deleteReportData_btn);
        }
    }

    public void addReportList(List<Report> reportList) {
        this.reportList.clear();
        this.reportList = reportList;
        notifyDataSetChanged();
    }

    @Override
    public ReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_item, parent, false);

        return new ReportViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReportViewHolder holder, int position) {
        Report report = reportList.get(position);
        holder.reportTitle_tv.setText(report.getTitle());
        holder.dateCreated_tv.setText(report.getDateCreated());

        holder.editReport_btn.setOnClickListener(v -> {
            listener.showUpdateReport(report);
        });

        holder.deleteReport_btn.setOnClickListener(v -> {
            listener.showDeleteReport(report);
        });

        holder.reportClickable_rl.setOnClickListener(v -> {
            listener.moveToReportDataFragment(report);
        });
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }
}