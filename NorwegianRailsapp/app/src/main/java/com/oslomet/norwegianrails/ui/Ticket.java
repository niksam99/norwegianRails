package com.oslomet.norwegianrails.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.oslomet.norwegianrails.datasource.AppConfig;
import com.oslomet.norwegianrails.datasource.AppController;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.oslomet.norwegianrails.adaptor.TicketAdapter;
import com.oslomet.norwegianrails.viewmodel.TicketItem;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class Ticket extends Fragment {
    RecyclerView recyclerView;
    TicketItem ticketItem;
    private List<TicketItem> items = new ArrayList<>(0);
    public static Ticket newInstance() {
        Ticket fragment = new Ticket();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ticket, container, false);
        SharedPreferences pref = getActivity().getSharedPreferences("LoginDetail", MODE_PRIVATE);
        String login = pref.getString("login", "false");
        TextView detail =v.findViewById(R.id.detail);
        if(login.equals("true")){
            recyclerView = v.findViewById(R.id.recycler_ticket);
            SharedPreferences prefs = getActivity().getSharedPreferences("LoginDetail", MODE_PRIVATE);
            String uid = prefs.getString("uid", "uid");
            getTicket(uid);
            detail.setVisibility(View.GONE);
        }else{
            detail.setVisibility(View.VISIBLE);
        }

        return v;

    }
    private void getTicket(final String uid) {
        String tag_string_req = "ticket";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_TICKET, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        JSONArray jsonArray = jObj.getJSONArray("ticket");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String id = jsonObject.optString("id");
                            String from = jsonObject.optString("from");
                            String to = jsonObject.optString("to");
                            String time_from = jsonObject.optString("time_from");
                            String time_to = jsonObject.optString("time_to");
                            String travel_time = jsonObject.optString("travel_time");
                            String track = jsonObject.optString("track");
                            String price = jsonObject.optString("price");
                            String date = jsonObject.optString("date");
                             ticketItem = new TicketItem(id,from,to,time_from,time_to,travel_time,
                            track,price,date) ;
                            items.add(ticketItem);

                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setHasFixedSize(true);

                        recyclerView.setAdapter(new TicketAdapter(items, getContext()));



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



}
