package com.myexample.android.shriramexecutive;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class DataActivity extends AppCompatActivity {
    public static final String LOG_TAG = DataActivity.class.getSimpleName();
    private static final String DATA_UPLOAD_REQUEST_URL = "https://testingdataservice.000webhostapp.com/uploaddata.php";
    //Objects to hold layout controls and fragments
    ProgressDialog progressDialog;
    private Button submitButton;
    private TextView fragmentHeading;
    private BranchDetailsFragment branchDetailsFragment;
    private CustomerDetailsFragment customerDetailsFragment;
    private ProductDetailsFragment productDetailsFragment;
    //Variables to hold values of every fragment to avoid it from vanishing
    static String tosetdate,torevdate,otherBranch;
    static int selectedBranch=0;
    static String mCustomerName,mCustomerAddress,mCustomerMobileNumber;
    static String mProductMake,mProductModel,mProductMfgYear,mProductValuation,mProductLoanAmount,mProductTenture,mProductDealerName,mProductCIBILScore,mProductCIBILStatus;
    private int position = 0;
    public static final String sharedPref = "AuthPref";
    Boolean isUploaded=false;
    String userID,userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        progressDialog = new ProgressDialog(DataActivity.this, ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        TextView empidname = (TextView) findViewById(R.id.empidname);
        Bundle bundle = getIntent().getExtras();
        userID=bundle.getString("userID");
        userName=bundle.getString("userName");
        String text = "Executive code: \n"+userID+"\n\nExecutive Name: \n"+userName;
        if (empidname != null) {
            empidname.setText(text);
        }
        submitButton = (Button) findViewById(R.id.submitButton);
        fragmentHeading = (TextView) findViewById(R.id.fragmentHeading);

        //Init variables those are static
        initVariables();
        setDate();
        //Calling initial fragment
        branchDetailsFragment();
        submitButton.setText("NEXT");
        fragmentHeading.setText("Branch Details");
        position++;

        //Attaching onClick Listener to button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 1 && branchDetailsFragment.validate()) {
                    //Next Fragment
                    tosetdate=BranchDetailsFragment.sdate;
                    torevdate=BranchDetailsFragment.rdate;
                    selectedBranch=BranchDetailsFragment.mBranchSpinner.getSelectedItemPosition();
                    if(BranchDetailsFragment.otherBox.getVisibility()==View.VISIBLE){
                        otherBranch=BranchDetailsFragment.otherBox.getText().toString();
                        Log.v(LOG_TAG,otherBranch);
                    }else{
                        BranchDetailsFragment.otherBox.setText("");
                        otherBranch="";
                        Log.v(LOG_TAG,otherBranch);
                    }
                    customerDetailFragment();
                    submitButton.setText("NEXT");
                    fragmentHeading.setText("Customer Details");
                    position++;

//                    Toast.makeText(DataActivity.this, "Fragment: " + position + "\nMonth: "
//                            + branchDetailsFragment.getmDate() + "\nDivision: "
//                            + branchDetailsFragment.getmDivision() + "\nBranch: "
//                            + branchDetailsFragment.getmBranch(), Toast.LENGTH_LONG).show();
                } else if (position == 2 && customerDetailsFragment.validate()) {

                    //To hide keyboard if it is opened
                    if (getCurrentFocus() != null) {
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                    //Next Fragment
                    productDetailsFragment();
                    submitButton.setText("SUBMIT");
                    fragmentHeading.setText("Product Details");
                    position++;
//                    Toast.makeText(DataActivity.this, "Fragment: " + position + "\nName: "
//                            + customerDetailsFragment.get_mCustomerName()
//                            + "\nAddress: " + customerDetailsFragment.get_mCustomerAddress()
//                            + "\nMobile Number: " + customerDetailsFragment.get_mMobileNumber(), Toast.LENGTH_LONG).show();

                } else if (position == 3 && productDetailsFragment.validate()) {

//
//                    Toast.makeText(DataActivity.this, "Fragment: "
//                            + position + "\nProductDetails\nMake: "
//                            + productDetailsFragment.get_mMakeEditText()
//                            + "\nModel: " + productDetailsFragment.get_mModelEditText()
//                            + "\nMFG YEAR: "
//                            + productDetailsFragment.get_mMfgYearEditText()
//                            + "\nValuation: " + productDetailsFragment.get_mValuationEditText()
//                            + "\nLoan Amount: " + productDetailsFragment.get_mLoanAmountEditText()
//                            + "\nTenture: " + productDetailsFragment.get_mTentureEditText(), Toast.LENGTH_LONG).show();

                    //Data insertion on server
//                    Toast.makeText(DataActivity.this, "Ready to upload onto server....", Toast.LENGTH_LONG).show();

                    try{
                        //noinspection unchecked
                        new BackGroundTask().execute(loadUserFields());
                    }catch (Exception e){
                        progressDialog.dismiss();
                        Toast.makeText(DataActivity.this,"Unable to Upload or Server Down",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        Button logout = (Button) findViewById(R.id.logoutButton);
        assert logout != null;
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Removing all fragments from stack
                while (position != 1) {
                    getFragmentManager().popBackStack();
                    position--;
                }
                clearPreferences();
                finish();
                Intent intent = new Intent(DataActivity.this, SignInActivity.class);
                startActivity(intent);

            }
        });
    }
    /**
     *
     */
    public void initVariables(){
        position=selectedBranch = 0;
        tosetdate=torevdate=otherBranch=mCustomerName=mCustomerAddress=mCustomerMobileNumber="";
        mProductMake=mProductModel=mProductMfgYear=mProductValuation=mProductLoanAmount=mProductTenture=mProductDealerName=mProductCIBILScore=mProductCIBILStatus="";

    }

    /**
     * It will create new fragment within activity
     */
    private void branchDetailsFragment(){
        branchDetailsFragment = new BranchDetailsFragment();
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, branchDetailsFragment);
        transaction.commit();
    }

    /**
     * It will create new fragment withing activity
     */
    private void customerDetailFragment(){
        customerDetailsFragment = new CustomerDetailsFragment();
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container,customerDetailsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * It will create new fragment within activity
     */

    private void productDetailsFragment(){
        productDetailsFragment = new ProductDetailsFragment();
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, productDetailsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount()>0){
            if(position==2){
                mCustomerName = customerDetailsFragment.get_mCustomerName();
                mCustomerAddress = customerDetailsFragment.get_mCustomerAddress();
                mCustomerMobileNumber = customerDetailsFragment.get_mMobileNumber();
                fragmentHeading.setText("Branch Details");
            } else if(position ==3){
                mProductMake = productDetailsFragment.get_mMakeEditText();
                mProductModel = productDetailsFragment.get_mModelEditText();
                mProductMfgYear = productDetailsFragment.get_mMfgYearEditText();
                mProductValuation = productDetailsFragment.get_mValuationEditText();
                mProductLoanAmount = productDetailsFragment.get_mLoanAmountEditText();
                mProductTenture = productDetailsFragment.get_mTentureEditText();
                mProductDealerName = productDetailsFragment.get_mDealerNameEditText();
                mProductCIBILScore = productDetailsFragment.get_mCIBILEditText();
                mProductCIBILStatus = productDetailsFragment.get_mCIBILStatusEditText();
                submitButton.setText("NEXT");
                fragmentHeading.setText("Customer Details");
            }
            getFragmentManager().popBackStack();
            position--;
        }
        else {
//            super.onBackPressed();
            finish();
        }
    }
    public void clearPreferences(){
        SharedPreferences.Editor editor = getSharedPreferences(sharedPref, MODE_PRIVATE).edit();
        editor.putString("id",null);
        editor.putString("pass", null);
        editor.putString("name", null);
        editor.apply();
    }

    /**
     * Insert fields into HashMap which are posted with request
     * @return DataToPost
     */
    private HashMap<String,String> loadUserFields(){
        HashMap<String,String> DataToPost = new HashMap<>();

        DataToPost.put("EMPCODE",userID);
        DataToPost.put("EMPNAME",userName);

        //Branch Details
        DataToPost.put("DAY", branchDetailsFragment.getmDate());
        DataToPost.put("DIVISION", branchDetailsFragment.getmDivision());
        DataToPost.put("BRANCH",branchDetailsFragment.getmBranch());

        //Customer Details
        DataToPost.put("CNAME", customerDetailsFragment.get_mCustomerName());
        DataToPost.put("ADDRESS", customerDetailsFragment.get_mCustomerAddress());
        DataToPost.put("MOBILE",customerDetailsFragment.get_mMobileNumber());

        //Product Details
        DataToPost.put("MAKE", productDetailsFragment.get_mMakeEditText());
        DataToPost.put("MODEL",productDetailsFragment.get_mModelEditText());
        DataToPost.put("MFGYEAR",productDetailsFragment.get_mMfgYearEditText());
        DataToPost.put("VALUATION",productDetailsFragment.get_mValuationEditText());
        DataToPost.put("LAMOUNT",productDetailsFragment.get_mLoanAmountEditText());
        DataToPost.put("TENTURE",productDetailsFragment.get_mTentureEditText());
        DataToPost.put("DNAME",productDetailsFragment.get_mDealerNameEditText());
        DataToPost.put("CIBILSCORE",productDetailsFragment.get_mCIBILEditText());
        DataToPost.put("CIBILSTATUS",productDetailsFragment.get_mCIBILStatusEditText());
        return DataToPost;
    }

    private class BackGroundTask extends AsyncTask<HashMap<String,String>,Void,String>{
        @Override
        protected void onPreExecute() {
            progressDialog.onStart();
            progressDialog.show();
            progressDialog.setMessage("Processing....");
        }

        @SafeVarargs
        @Override
        protected final String doInBackground(HashMap<String, String>... params) {
            // Create URL object
            URL url = createUrl(DATA_UPLOAD_REQUEST_URL);

            // Perform HTTP request to the URL and receive a JSON response back
            String result = null;
            try {
                result = makeHttpRequest(url,params[0]);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error closing input stream", e);
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("1")){
                isUploaded=true;
            }
            if(isUploaded){
                //Removing all fragments from stack
                while (position != 1) {
                    getFragmentManager().popBackStack();
                    position--;
                }
                getFragmentManager().beginTransaction().remove(branchDetailsFragment).commit();
                initVariables();
                //Calling initial fragment
                branchDetailsFragment();
                submitButton.setText("NEXT");
                fragmentHeading.setText("Branch Details");
                position++;
                isUploaded=false;
                progressDialog.dismiss();
                Toast.makeText(DataActivity.this,"Data Uploaded Successfully!",Toast.LENGTH_LONG).show();
            }else{
                progressDialog.dismiss();
                Toast.makeText(DataActivity.this,"Unable to Upload or Server Down",Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url,HashMap<String,String> DataFields) throws IOException {
        String response = "";
        // If the URL is null, then return early.
        if (url == null) {
            return response;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(40000 /* milliseconds */);
            urlConnection.setConnectTimeout(45000 /* milliseconds */);
            urlConnection.setRequestMethod("POST");
            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(DataFields));
            writer.flush();
            writer.close();
            os.close();
            urlConnection.connect();
            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                response = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return response;
    }


    private static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }



    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public void setDate(){
        DatePicker mDate = new DatePicker(getBaseContext());
        int year,month,day;
        year = mDate.getYear();
        month=mDate.getMonth();
        month++;
        day=mDate.getDayOfMonth();

        String yearstr,monthstr,daystr;
        yearstr = year+"";
        monthstr=month+"";
        daystr=day+"";

        if(month<10){
            monthstr="0"+month;
        }
        if(day<10){
            daystr="0"+day;
        }
        tosetdate=daystr+"-"+monthstr+"-"+yearstr;
        torevdate=yearstr+"-"+monthstr+"-"+daystr;
    }
}
