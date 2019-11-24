package com.oslomet.norwegianrails.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.oslomet.norwegianrails.R;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.FragmentTransaction;
import com.oslomet.norwegianrails.datasource.AppConfig;
import com.oslomet.norwegianrails.datasource.AppController;

import static android.content.Context.MODE_PRIVATE;

public class Wallet extends  Fragment {
    public static Wallet newInstance() {
        Wallet fragment = new Wallet();
        return fragment;
    }
    SharedPreferences pref;
    String wallet_amount;
    String uid;
    TextView tvWalletAmount;
    Button btnAddToWallet;
    EditText etAddToWallet;
    String amount;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_wallet, container, false);
        pref = getActivity().getSharedPreferences("LoginDetail", MODE_PRIVATE);
        uid = pref.getString("uid", "false");
        tvWalletAmount = v.findViewById(R.id.tvWalletAmount);
        btnAddToWallet = v.findViewById(R.id.btnAddToWallet);
        etAddToWallet = v.findViewById(R.id.etAddToWallet);
        loadWallet(uid);
        checkValidation();
        etAddToWallet.addTextChangedListener(mWatcher);
        btnAddToWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount = etAddToWallet.getText().toString();
                Log.e("amount",amount);
                addToWallet(uid,amount,"wallet",getContext());


            }
        });
        return v;
    }

    private void checkValidation() {
        // TODO Auto-generated method stub

        if ((TextUtils.isEmpty(etAddToWallet.getText())))
            btnAddToWallet.setEnabled(false);
        else
            btnAddToWallet.setEnabled(true);

    }

    TextWatcher mWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO Auto-generated method stub
            checkValidation();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }
    };

    public  void loadWallet(final String uid){

        String tag_string_req = "load Wallet";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOAD_WALLET, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("response",response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");


                    if (!error) {
                        wallet_amount = jObj.getString("wallet_amount");
                        tvWalletAmount.setText("Wallet Amount: Kr."+wallet_amount);


                    } else {
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getActivity(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e("error", "error");
                    e.printStackTrace();
                }
                }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError) {
                    ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = null;
                    if (cm != null) {
                        activeNetwork = cm.getActiveNetworkInfo();
                    }
                    if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                        Toast.makeText(getActivity(), "Server is not connected to internet.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Your device is not connected to internet.",
                                Toast.LENGTH_SHORT).show();
                    }
                } else if (error instanceof NetworkError || error.getCause() instanceof ConnectException
                        || (error.getCause().getMessage() != null
                        && error.getCause().getMessage().contains("connection"))) {
                    Toast.makeText(getActivity(), "Your device is not connected to internet.",
                            Toast.LENGTH_SHORT).show();
                } else if (error.getCause() instanceof MalformedURLException) {
                    Toast.makeText(getActivity(), "Bad Request.", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError || error.getCause() instanceof IllegalStateException
                        || error.getCause() instanceof JSONException
                        || error.getCause() instanceof XmlPullParserException) {
                    Toast.makeText(getActivity(), "Parse Error (because of invalid json or xml).",
                            Toast.LENGTH_SHORT).show();
                } else if (error.getCause() instanceof OutOfMemoryError) {
                    Toast.makeText(getActivity(), "Out Of Memory Error.", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(getActivity(), "server couldn't find the authenticated request.",
                            Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError || error.getCause() instanceof ServerError) {
                    Toast.makeText(getActivity(), "Server is not responding.", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError || error.getCause() instanceof SocketTimeoutException
                        || error.getCause() instanceof ConnectTimeoutException
                        || error.getCause() instanceof SocketException
                        || (error.getCause().getMessage() != null
                        && error.getCause().getMessage().contains("Connection timed out"))) {
                    Toast.makeText(getActivity(), "Connection timeout error",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "An unknown error occurred.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("unique_id", uid);
                return params;
            }
        };
        int custom_timeout_ms = 15000;
        DefaultRetryPolicy policy = new DefaultRetryPolicy(custom_timeout_ms,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        strReq.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public  void addToWallet(final String uid, final String amount, final String from, final Context context){

        String tag_string_req = "Add Wallet";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ADD_WALLET, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("response",response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");


                    if (!error) {

                       String wallet_amount = jObj.getString("wallet_amount");
                        SharedPreferences pref = context.getApplicationContext().getSharedPreferences("Wallet", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("wallet_amount", wallet_amount);
                        editor.apply();
                        if(from.equals("wallet")){
                            Toast.makeText(getActivity(),
                                    "Wallet Updated Successfully", Toast.LENGTH_LONG).show();

                            Fragment wallet = newInstance();
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.frame_layout, wallet);
                            transaction.commit();
                        }


                    } else {
                        Toast.makeText(getActivity(),
                                "Something went wrong", Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    Log.e("error", "error");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError) {
                    ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = null;
                    if (cm != null) {
                        activeNetwork = cm.getActiveNetworkInfo();
                    }
                    if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                        Toast.makeText(getActivity(), "Server is not connected to internet.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Your device is not connected to internet.",
                                Toast.LENGTH_SHORT).show();
                    }
                } else if (error instanceof NetworkError || error.getCause() instanceof ConnectException
                        || (error.getCause().getMessage() != null
                        && error.getCause().getMessage().contains("connection"))) {
                    Toast.makeText(getActivity(), "Your device is not connected to internet.",
                            Toast.LENGTH_SHORT).show();
                } else if (error.getCause() instanceof MalformedURLException) {
                    Toast.makeText(getActivity(), "Bad Request.", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError || error.getCause() instanceof IllegalStateException
                        || error.getCause() instanceof JSONException
                        || error.getCause() instanceof XmlPullParserException) {
                    Toast.makeText(getActivity(), "Parse Error (because of invalid json or xml).",
                            Toast.LENGTH_SHORT).show();
                } else if (error.getCause() instanceof OutOfMemoryError) {
                    Toast.makeText(getActivity(), "Out Of Memory Error.", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(getActivity(), "server couldn't find the authenticated request.",
                            Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError || error.getCause() instanceof ServerError) {
                    Toast.makeText(getActivity(), "Server is not responding.", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError || error.getCause() instanceof SocketTimeoutException
                        || error.getCause() instanceof ConnectTimeoutException
                        || error.getCause() instanceof SocketException
                        || (error.getCause().getMessage() != null
                        && error.getCause().getMessage().contains("Connection timed out"))) {
                    Toast.makeText(getActivity(), "Connection timeout error",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "An unknown error occurred.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("unique_id", uid);
                params.put("wallet_amount", amount);
                return params;
            }
        };
        int custom_timeout_ms = 15000;
        DefaultRetryPolicy policy = new DefaultRetryPolicy(custom_timeout_ms,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        strReq.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }




}
