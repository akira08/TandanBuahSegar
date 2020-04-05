package com.cdo.tbs.dialogfragment;

import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.cdo.tbs.R;
import com.cdo.tbs.fragment.ReportDataFragment;
import com.cdo.tbs.fragment.ReportFragment;
import com.cdo.tbs.model.Report;
import com.cdo.tbs.model.ReportData;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeleteReportDataDialogFragment extends DialogFragment {
    private static final String DELETE_REPORT_DATA = "deleteReport";

    private ReportData reportData;

    @BindView(R.id.cancel_btn) Button cancel_btn;
    @BindView(R.id.deleteReportData_btn) Button deleteReportData_btn;
    @BindView(R.id.deleteWording_tv) TextView deleteWording_tv;

    public static DeleteReportDataDialogFragment newInstance(ReportData reportData) {
        DeleteReportDataDialogFragment fragment = new DeleteReportDataDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(DELETE_REPORT_DATA, reportData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.reportData = (ReportData) getArguments().getSerializable(DELETE_REPORT_DATA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.fragment_delete_report_data_dialog, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cancel_btn.setOnClickListener(v -> {
            dismiss();
        });

        deleteWording_tv.setText(String.format(getString(R.string.delete_report_data_wording), reportData.getDate()));
        deleteReportData_btn.setOnClickListener(v -> {
            if(((ReportDataFragment) DeleteReportDataDialogFragment.this.getParentFragment()).deleteReportData(reportData)){
                Toast.makeText(getContext(), "Report data deleted!", Toast.LENGTH_SHORT).show();
                dismiss();
            } else {
                Toast.makeText(getContext(), "Oops. Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        Window window = getDialog().getWindow();
        Point size = new Point();

        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);

        int width = size.x;

        window.setLayout((int) (width * 0.75), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
    }
}
