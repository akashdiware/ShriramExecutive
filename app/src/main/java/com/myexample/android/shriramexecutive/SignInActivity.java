package com.myexample.android.shriramexecutive;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class SignInActivity extends AppCompatActivity {
    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = SignInActivity.class.getSimpleName();
    private static final String LOGIN_REQUEST_URL = "https://testingdataservice.000webhostapp.com/userLogin.php";
    public static final String sharedPref = "AuthPref";
    private EditText _userID_text, _password_text;
    Button loginButton;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    private String globID,globPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        progressDialog = new ProgressDialog(SignInActivity.this, ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        loginButton = (Button) findViewById(R.id.loginButton);
        _userID_text = (EditText) findViewById(R.id.userID_text);
        _password_text = (EditText) findViewById(R.id.password_text);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    try{
                        new backgroundTask().execute();
                    }catch (Exception e){
                        progressDialog.dismiss();
                        Toast.makeText(SignInActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                        initControls();
                        loginButton.setEnabled(true);
                    }

                }
            }
        });
        //Needs to check that if user is already logged in onto system or not!
        checkPreferences();
        if(globID !=null && globPass!=null){
            _userID_text.setText(globID);
            _password_text.setText(globPass);
            loginButton.callOnClick();
        }

    }

    /**
     * @return boolean after validating all the fields!
     */
    public boolean validate() {
        boolean flag = true;
        String userID = _userID_text.getText().toString();
        String password = _password_text.getText().toString();
        if (userID.isEmpty() || userID.length() < 10) {
            _userID_text.setError("Enter Valid Employee Code!");
            flag = false;
        } else {
            _userID_text.setError(null);
        }

        if (password.isEmpty() || password.length() < 8) {
            _password_text.setError("Enter password more than 8 characters!");
            flag = false;
        } else {
            _password_text.setError(null);
        }
        return flag;
    }

    /**
     *
     */
    public void onLogin(String userID, String userPassword, String userName) {
//        Toast.makeText(SignInActivity.this,"OnLogin successfully!",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, DataActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userID",userID);
        bundle.putString("userName",userName);
        intent.putExtras(bundle);
        addPreferences(userID,userPassword,userName);
        progressDialog.dismiss();
        loginButton.setEnabled(true);
        finish();
        startActivity(intent);
    }

    /**
     *
     */
    public void initControls() {
        _userID_text.setText("");
        _password_text.setText("");
        _userID_text.requestFocus();
    }

    private class backgroundTask extends AsyncTask<Void, Void, String> {
        String userID,password;

        @Override
        protected void onPreExecute() {
            loginButton.setEnabled(false);
            //Start Progess Bar
            progressDialog.onStart();
            progressDialog.show();
            progressDialog.setMessage("Processing....");
            userID = _userID_text.getText().toString();
            password = _password_text.getText().toString();
        }

        @Override
        protected String doInBackground(Void... params) {
            // Create URL object
            URL url = createUrl(LOGIN_REQUEST_URL);

            // Perform HTTP request to the URL and receive a JSON response back
            String result = null;
            try {
                result = makeHttpRequest(url,userID,password);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error closing input stream", e);
            }
            return result;
        }

        @Override
        protected void onPostExecute(String returnedResult) {
//            Log.e(LOG_TAG,returnedResult);
//            Log.e(LOG_TAG,userID);

            //Post authentication parameters validate if valid user or not
            if(returnedResult.isEmpty() || returnedResult.equals("") || returnedResult.equals("0")){
                progressDialog.dismiss();
                Toast.makeText(SignInActivity.this, "Login Failed! OR Server Down!"+returnedResult, Toast.LENGTH_SHORT).show();
                initControls();
                loginButton.setEnabled(true);

            }else {
                onLogin(userID,password,returnedResult);
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
    private static String makeHttpRequest(URL url,String ID,String pass) throws IOException {
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
            HashMap<String,String> toPost = new HashMap<>();
            toPost.put("empid",ID);
            toPost.put("emppass",pass);
            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(toPost));

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

    public void checkPreferences(){
        sharedPreferences = getSharedPreferences(sharedPref,MODE_PRIVATE);
        globID=sharedPreferences.getString("id",null);
        globPass=sharedPreferences.getString("pass",null);
    }

    public void addPreferences(String userID, String password, String name){
        SharedPreferences.Editor editor = getSharedPreferences(sharedPref,MODE_PRIVATE).edit();
        editor.putString("id",userID);
        editor.putString("pass",password);
        editor.putString("name",name);
        editor.apply();

    }


}