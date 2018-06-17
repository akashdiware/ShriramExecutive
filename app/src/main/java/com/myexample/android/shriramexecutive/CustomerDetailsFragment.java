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
public class CustomerDetailsFragment extends Fragment {
    //Objects to hold references of the UI elements of fragment
    private EditText _mCustomerName,_mCustomerAddress,_mMobileNumber;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_details, container, false);
        _mCustomerName = (EditText) view.findViewById(R.id.customer_name_text);
        _mCustomerAddress = (EditText) view.findViewById(R.id.cust_address_text);
        _mMobileNumber = (EditText) view.findViewById(R.id.mobile_number_text);
        _mCustomerName.setText(DataActivity.mCustomerName);
        _mCustomerAddress.setText(DataActivity.mCustomerAddress);
        _mMobileNumber.setText(DataActivity.mCustomerMobileNumber);
        return view;
    }

    public boolean validate(){
        boolean flag = true;
        if(get_mCustomerName().isEmpty()){
            _mCustomerName.setError("Enter Name");
            _mCustomerName.requestFocus();
            flag = false;
        }
        else
            _mCustomerName.setError(null);
        if(get_mCustomerAddress().isEmpty()){
            _mCustomerAddress.setError("Enter Address");
            _mCustomerAddress.requestFocus();
            flag = false;
        }
        else
            _mCustomerAddress.setError(null);
        if(get_mMobileNumber().isEmpty() || get_mMobileNumber().length()<10){
            _mMobileNumber.setError("Enter Mobile Number");
            _mMobileNumber.requestFocus();
            flag = false;
        }
        else
            _mMobileNumber.setError(null);
        return flag;
    }

    //Getter and Setter for fragment
    public String get_mCustomerName() {
        return _mCustomerName.getText().toString();
    }

    public String get_mCustomerAddress() {
        return _mCustomerAddress.getText().toString();
    }

    public String get_mMobileNumber() {
        return _mMobileNumber.getText().toString();
    }
}
