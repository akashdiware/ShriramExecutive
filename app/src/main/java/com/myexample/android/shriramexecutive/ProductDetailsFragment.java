package com.myexample.android.shriramexecutive;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDetailsFragment extends Fragment {
    //To hold reference of Fragment UI controls
    private EditText _mMakeEditText,_mModelEditText,_mMfgYearEditText,_mValuationEditText,_mLoanAmountEditText,_mTentureEditText,_mDealerNameEditText,_mCIBILEditText,_mCIBILStatusEditText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_details, container, false);
        _mMakeEditText = (EditText) view.findViewById(R.id.product_make_text);
        _mModelEditText = (EditText) view.findViewById(R.id.product_model_text);
        _mMfgYearEditText = (EditText) view.findViewById(R.id.product_mfgyear_text);
        _mValuationEditText = (EditText) view.findViewById(R.id.product_valuation_text);
        _mLoanAmountEditText = (EditText) view.findViewById(R.id.product_loanamount_text);
        _mTentureEditText = (EditText) view.findViewById(R.id.product_tenture_text);
        _mDealerNameEditText = (EditText) view.findViewById(R.id.product_delearname_text);
        _mCIBILEditText = (EditText) view.findViewById(R.id.product_cibil_text);
        _mCIBILStatusEditText = (EditText) view.findViewById(R.id.product_cibilstatus_text);
        _mMakeEditText.setText(DataActivity.mProductMake);
        _mModelEditText.setText(DataActivity.mProductModel);
        _mMfgYearEditText.setText(DataActivity.mProductMfgYear);
        _mValuationEditText.setText(DataActivity.mProductValuation);
        _mLoanAmountEditText.setText(DataActivity.mProductLoanAmount);
        _mTentureEditText.setText(DataActivity.mProductTenture);
        _mDealerNameEditText.setText(DataActivity.mProductDealerName);
        _mCIBILEditText.setText(DataActivity.mProductCIBILScore);
        _mCIBILStatusEditText.setText(DataActivity.mProductCIBILStatus);
        _mMakeEditText.requestFocus();
        return view;
    }
    public boolean validate(){
        boolean flag = true;
        if(get_mMakeEditText().isEmpty()){
            _mMakeEditText.setError("Required");
            _mMakeEditText.requestFocus();
            flag = false;
        }else{
            _mMakeEditText.setError(null);
        }
        if(get_mModelEditText().isEmpty()){
            _mModelEditText.setError("Required");
            _mMakeEditText.requestFocus();
            flag = false;
        }else{
            _mModelEditText.setError(null);
        }
        if(get_mMfgYearEditText().isEmpty()){
            _mMfgYearEditText.setError("Required");
            _mMfgYearEditText.requestFocus();
            flag = false;
        }else{
            _mMfgYearEditText.setError(null);
        }

        if(get_mValuationEditText().isEmpty()){
            _mValuationEditText.setError("Required");
            _mValuationEditText.requestFocus();
            flag = false;
        }else{
            _mValuationEditText.setError(null);
        }
        if(get_mLoanAmountEditText().isEmpty()){
            _mLoanAmountEditText.setError("Required");
            _mLoanAmountEditText.requestFocus();
            flag = false;
        }else{
            _mLoanAmountEditText.setError(null);
        }
        if(get_mTentureEditText().isEmpty()){
            _mTentureEditText.setError("Required");
            _mTentureEditText.requestFocus();
            flag = false;
        }else{
            _mTentureEditText.setError(null);
        }
        if(get_mDealerNameEditText().isEmpty()){
            _mDealerNameEditText.setError("Required");
            _mDealerNameEditText.requestFocus();
            flag = false;
        }else{
            _mDealerNameEditText.setError(null);
        }
        if(get_mCIBILEditText().isEmpty()){
            _mCIBILEditText.setError("Required");
            _mCIBILEditText.requestFocus();
            flag = false;
        }else{
            _mCIBILEditText.setError(null);
        }
        if(get_mCIBILStatusEditText().isEmpty()){
            _mCIBILStatusEditText.setError("Required");
            _mCIBILStatusEditText.requestFocus();
            flag = false;
        }else{
            _mCIBILStatusEditText.setError(null);
        }
        return flag;
    }
//Getter for fragment
    public String get_mMakeEditText() {
        return _mMakeEditText.getText().toString();
    }

    public String  get_mModelEditText() {
        return _mModelEditText.getText().toString();
    }

    public String get_mMfgYearEditText() {
        return _mMfgYearEditText.getText().toString();
    }

    public String get_mValuationEditText() {
        return _mValuationEditText.getText().toString();
    }

    public String get_mLoanAmountEditText() {
        return _mLoanAmountEditText.getText().toString();
    }

    public String  get_mTentureEditText() {
        return _mTentureEditText.getText().toString();
    }

    public String  get_mDealerNameEditText() {
        return _mDealerNameEditText.getText().toString();
    }

    public String get_mCIBILEditText() {
        return _mCIBILEditText.getText().toString();
    }

    public String get_mCIBILStatusEditText() {
        return _mCIBILStatusEditText.getText().toString();
    }
}
