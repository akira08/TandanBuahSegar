package com.cdo.tbs.dialogfragment;

import android.app.DatePickerDialog;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cdo.tbs.R;
import com.cdo.tbs.fragment.ReportDataFragment;
import com.cdo.tbs.fragment.ReportFragment;
import com.cdo.tbs.model.Report;
import com.cdo.tbs.model.ReportData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AEReportDataDialogFragment extends DialogFragment {
    private static final String DIALOG_TITLE = "dialogTitle";
    private static final String CONFIRM_BUTTON = "confirmButton";
    private static final String REPORT_DATA = "reportData";
    private static final String REPORT = "report";
    private static final String ACTION = "action";
    public static final int ADD_ACTION = 0;
    public static final int EDIT_ACTION = 1;

    private int action;
    private String dialogTitle;
    private String confirmButton;
    private ReportData reportData;
    private Report report;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);


    @BindView(R.id.cancel_btn) Button cancel_btn;
    @BindView(R.id.actionReportData_btn) Button actionReportData_btn;
    @BindView(R.id.dialogReportData_title_tv) TextView dialogReportData_title_tv;
    @BindView(R.id.dateInput_et) EditText dateInput_et;
    @BindView(R.id.unitInput_et) EditText unitInput_et;
    @BindView(R.id.tonsInput_et) EditText tonsInput_et;
    @BindView(R.id.marginInput_et) EditText marginInput_et;

    public static AEReportDataDialogFragment newInstance(int action, String dialogTitle, String confirmButton, ReportData reportData, Report report) {
        AEReportDataDialogFragment fragment = new AEReportDataDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ACTION, action);
        args.putString(DIALOG_TITLE, dialogTitle);
        args.putString(CONFIRM_BUTTON, confirmButton);
        args.putSerializable(REPORT_DATA, reportData);
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
            this.reportData = (ReportData) getArguments().getSerializable(REPORT_DATA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.fragment_add_edit_report_data_dialog, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dialogReportData_title_tv.setText(dialogTitle);
        actionReportData_btn.setText(confirmButton);

        cancel_btn.setOnClickListener(v -> {
            dismiss();
        });

        dateInput_et.setOnClickListener(v -> {
            showDateDialog();
        });

        switch (action) {
            case (ADD_ACTION):
                actionReportData_btn.setOnClickListener(v -> {
                    String date = dateInput_et.getText().toString();
                    int unit = Integer.parseInt(unitInput_et.getText().toString());
                    int tons = Integer.parseInt(tonsInput_et.getText().toString());
                    int margin = Integer.parseInt(marginInput_et.getText().toString());
                    long idReport = report.getId();

                    if(((ReportDataFragment) AEReportDataDialogFragment.this.getParentFragment()).addReportData(date, unit, tons, margin, idReport)){
                        Toast.makeText(getContext(), "Report Data saved!", Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else {
                        Toast.makeText(getContext(), "Oops. Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case (EDIT_ACTION):
                dateInput_et.setText(reportData.getDate());
                unitInput_et.setText(String.valueOf(reportData.getUnit()));
                tonsInput_et.setText(String.valueOf(reportData.getTons()));
                marginInput_et.setText(String.valueOf(reportData.getMargin()));
                actionReportData_btn.setOnClickListener(v -> {
                    String date = dateInput_et.getText().toString();
                    int unit = Integer.parseInt(unitInput_et.getText().toString());
                    int tons = Integer.parseInt(tonsInput_et.getText().toString());
                    int margin = Integer.parseInt(marginInput_et.getText().toString());
                    if(((ReportDataFragment) AEReportDataDialogFragment.this.getParentFragment()).updateReportData(reportData, date, unit, tons, margin)){
                        Toast.makeText(getContext(), "Report data saved!", Toast.LENGTH_SHORT).show();
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

        window.setLayout((int) (width * 0.90), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
    }

    private void showDateDialog(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(getContext(), (view, year, monthOfYear, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            dateInput_et.setText(dateFormatter.format(newDate.getTime()));
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
