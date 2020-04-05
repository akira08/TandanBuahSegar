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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.cdo.tbs.R;
import com.cdo.tbs.fragment.ReportFragment;
import com.cdo.tbs.model.Report;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AEReportDialogFragment extends DialogFragment {
    private static final String DIALOG_TITLE = "dialogTitle";
    private static final String CONFIRM_BUTTON = "confirmButton";
    private static final String REPORT = "report";
    private static final String ACTION = "action";
    public static final int ADD_ACTION = 0;
    public static final int EDIT_ACTION = 1;

    private int action;
    private String dialogTitle;
    private String confirmButton;
    private Report report;

    @BindView(R.id.cancel_btn) Button cancel_btn;
    @BindView(R.id.actionReport_btn) Button actionReport_btn;
    @BindView(R.id.dialogReportData_title_tv) TextView dialogReport_title_tv;
    @BindView(R.id.reportTitle_et) EditText reportTitle_et;

    public static AEReportDialogFragment newInstance(int action, String dialogTitle, String confirmButton, Report report) {
        AEReportDialogFragment fragment = new AEReportDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ACTION, action);
        args.putString(DIALOG_TITLE, dialogTitle);
        args.putString(CONFIRM_BUTTON, confirmButton);
        args.putSerializable(REPORT, report);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.action = getArguments().getInt(ACTION);;
            this.dialogTitle = getArguments().getString(DIALOG_TITLE);
            this.confirmButton = getArguments().getString(CONFIRM_BUTTON);
            this.report = (Report) getArguments().getSerializable(REPORT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.fragment_add_edit_report_dialog, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dialogReport_title_tv.setText(dialogTitle);
        actionReport_btn.setText(confirmButton);

        cancel_btn.setOnClickListener(v -> {
            dismiss();
        });

        switch (action) {
            case (ADD_ACTION):
                actionReport_btn.setOnClickListener(v -> {
                    String reportTitle = reportTitle_et.getText().toString();
                    if(((ReportFragment) AEReportDialogFragment.this.getParentFragment()).addReport(reportTitle)){
                        Toast.makeText(getContext(), "Report saved!", Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else {
                        Toast.makeText(getContext(), "Oops. Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case (EDIT_ACTION):
                reportTitle_et.setText(report.getTitle());
                actionReport_btn.setOnClickListener(v -> {
                    String reportTitle = reportTitle_et.getText().toString();
                    if(((ReportFragment) AEReportDialogFragment.this.getParentFragment()).updateReport(report, reportTitle)){
                        Toast.makeText(getContext(), "Report saved!", Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else {
                        Toast.makeText(getContext(), "Oops. Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            default:
                break;
        }
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
