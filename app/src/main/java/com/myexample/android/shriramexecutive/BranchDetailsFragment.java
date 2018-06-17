package com.myexample.android.shriramexecutive;


import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class BranchDetailsFragment extends Fragment {
    //Objects to hold references of used controls in fragment
    public static EditText otherBox;
    public static Spinner mDivisionSpinner,mBranchSpinner;
    public static TextView mSetDate;
    public static String sdate,rdate;
    //Variables to hold controls values
    private String mDivision,mBranch;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_branch_details, container, false);



        mSetDate = (TextView) view.findViewById(R.id.setDate);
        BranchDetailsFragment.sdate=DataActivity.tosetdate;
        BranchDetailsFragment.rdate=DataActivity.torevdate;
        BranchDetailsFragment.mSetDate.setText(sdate);
        mSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        otherBox = (EditText) view.findViewById(R.id.otherBranch);
        BranchDetailsFragment.otherBox.setText(DataActivity.otherBranch);
        //Populate mDivisionSpinner object
        mDivisionSpinner = (Spinner) view.findViewById(R.id.divisions_spinner);
        final ArrayAdapter<CharSequence> divisionadapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.divisionsArray, android.R.layout.simple_spinner_item);
        divisionadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDivisionSpinner.setAdapter(divisionadapter);

        //Populate mBranchSpinner object
        mBranchSpinner = (Spinner) view.findViewById(R.id.branches_spinner);
        mDivisionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String division = mDivisionSpinner.getSelectedItem().toString();
                changeBranch(division);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

       mBranchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               String selectedItem = mBranchSpinner.getSelectedItem().toString();
               if (selectedItem.equals("OTHER")) {
                   otherBox.setVisibility(View.VISIBLE);
               } else {
                   otherBox.setVisibility(View.INVISIBLE);
               }
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });

        Log.v("BranchDetailsFragment", DataActivity.otherBranch);
        return view;
    }
    /**
     *
     * @param division select branches according to the division
     */
    private void changeBranch(String division){
        switch (division) {
            case "AKOLA": {
                final ArrayAdapter<CharSequence> branchAdapter = ArrayAdapter.createFromResource(getActivity(),
                        R.array.branchAkolaArray, android.R.layout.simple_spinner_item);
                branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mBranchSpinner.setAdapter(branchAdapter);
                mBranchSpinner.setSelection(DataActivity.selectedBranch);
                break;
            }
            case "AMRAVATI": {
                final ArrayAdapter<CharSequence> branchAdapter = ArrayAdapter.createFromResource(getActivity(),
                        R.array.branchAmravatiArray, android.R.layout.simple_spinner_item);
                branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mBranchSpinner.setAdapter(branchAdapter);
                mBranchSpinner.setSelection(DataActivity.selectedBranch);
                break;
            }
            case "CHANDRAPUR": {
                final ArrayAdapter<CharSequence> branchAdapter = ArrayAdapter.createFromResource(getActivity(),
                        R.array.branchChandrapurArray, android.R.layout.simple_spinner_item);
                branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mBranchSpinner.setAdapter(branchAdapter);
                mBranchSpinner.setSelection(DataActivity.selectedBranch);
                break;
            }
            case "GONDIA": {
                final ArrayAdapter<CharSequence> branchAdapter = ArrayAdapter.createFromResource(getActivity(),
                        R.array.branchGondiaArray, android.R.layout.simple_spinner_item);
                branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mBranchSpinner.setAdapter(branchAdapter);
                mBranchSpinner.setSelection(DataActivity.selectedBranch);
                break;
            }
            default: {
                final ArrayAdapter<CharSequence> branchAdapter = ArrayAdapter.createFromResource(getActivity(),
                        R.array.branchNagpurArray, android.R.layout.simple_spinner_item);
                branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mBranchSpinner.setAdapter(branchAdapter);
                mBranchSpinner.setSelection(DataActivity.selectedBranch);
                break;
            }
        }
    }

    /**
     *
     * @return boolean value
     */
    public boolean validate(){
        if(mBranchSpinner.getSelectedItem().toString().equals("SELECT")) {
            TextView errorText = (TextView)mBranchSpinner.getSelectedView();
            errorText.setTextColor(Color.RED);
            mBranchSpinner.setFocusable(true);
            mBranchSpinner.setFocusableInTouchMode(true);
            mBranchSpinner.requestFocus();
            return false;
        }else if((otherBox.getVisibility()==View.VISIBLE && otherBox.getText().toString().isEmpty())){
            otherBox.setError("");
            otherBox.setFocusable(true);
            otherBox.setFocusableInTouchMode(true);
            otherBox.requestFocus();
            return false;
        }else {
            otherBox.setError(null);
        }
        mDivision = mDivisionSpinner.getSelectedItem().toString();
        if(otherBox.getVisibility()==View.VISIBLE){
        mBranch = otherBox.getText().toString();
        }else{
            mBranch = mBranchSpinner.getSelectedItem().toString();
        }
        return true;
    }

    public String getmDate() {
        return rdate;
    }

    /**
     *
     * @return String
     */
    public String getmDivision() {
        return mDivision;
    }

    /**
     *
     * @return String
     */
    public String getmBranch() {
        return mBranch;
    }
    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }
}
