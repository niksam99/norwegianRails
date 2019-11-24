package com.oslomet.norwegianrails.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.oslomet.norwegianrails.datasource.AppConfig;
import com.oslomet.norwegianrails.datasource.AppController;

import android.text.Html;
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
import com.oslomet.norwegianrails.adaptor.SearchAdapter;
import com.oslomet.norwegianrails.viewmodel.SearchItem;

import org.apache.http.conn.ConnectTimeoutException;
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


public class SearchResult extends Fragment {
    String from, to, date, time, track;
    int travelTime;
    String id_from, place_from, track_1_from, track_2_from,
            id_to, place_to, track_1_to, track_2_to;
    String time_from, time_to;
    SearchItem searchItem;
    RecyclerView recyclerView;
    private List<SearchItem> items = new ArrayList<>(0);

    public static SearchResult newInstance() {
        SearchResult fragment = new SearchResult();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_result, container, false);
        if (this.getArguments() != null) {
            from = this.getArguments().getString("from");
            to = this.getArguments().getString("to");
            date = this.getArguments().getString("date");
            time = this.getArguments().getString("time");
        }
        TextView tvFromTo = v.findViewById(R.id.fromTo);
        String display = "From " + "<b>" + from + "</b> " + "to " + "<b>" + to + "</b> ";
        tvFromTo.setText(Html.fromHtml(display));
        recyclerView = v.findViewById(R.id.recycler_view);
        search(from, to);
        return v;
    }

    private void search(final String from, final String to) {
        String tag_string_req = "search";
        StringRequest strReq = new StringRequest(Request.Method.POST,
            AppConfig.URL_TRAIN, new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            try {
                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");

                if (!error) {
                    id_from = jObj.getString("id_from");
                    place_from = jObj.getString("place_from");
                    track_1_from = jObj.getString("track_1_from");
                    track_2_from = jObj.getString("track_2_from");
                    id_to = jObj.getString("id_to");
                    place_to = jObj.getString("place_to");
                    track_1_to = jObj.getString("track_1_to");
                    track_2_to = jObj.getString("track_2_to");

                } else {
                    String errorMsg = jObj.getString("message");
                    Toast.makeText(getActivity(),
                            errorMsg, Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                Log.e("error", "error");
                e.printStackTrace();
            }
            String[] splitTime = time.split(":");
            String splitTime1 = splitTime[0];
            String splitTime2 = splitTime[1];
            int checkTime;

            int getTrack = Integer.valueOf(id_from) - Integer.valueOf(id_to);
            int price = Math.abs(getTrack);
            if (getTrack < 0) {
                track = "Track 1";
                travelTime = Integer.valueOf(track_1_to) - Integer.valueOf(track_1_from);
                checkTime = Integer.valueOf(track_1_from) - Integer.valueOf(splitTime2);
                if (checkTime < 0) {
                    splitTime1 = String.valueOf(Integer.valueOf(splitTime1) + 1);

                }


            } else {
                track = "Track 2";
                travelTime = Integer.valueOf(track_2_to) - Integer.valueOf(track_2_from);
                checkTime = Integer.valueOf(track_2_from) - Integer.valueOf(splitTime2);
                if (checkTime < 0) {
                    splitTime1 = String.valueOf(Integer.valueOf(splitTime1) + 1);

                }
            }
            for (int i = 0; i < 5; i++) {
                if (track.equals("Track 1")){
                    Log.e("check time",""+splitTime1);
                    if(Integer.valueOf(splitTime1)>=24)
                    {
                        splitTime1 = "0";
                    }
                    time_from = Integer.valueOf(splitTime1) + i + ":" +track_1_from;
                    time_to = Integer.valueOf(splitTime1) + i + ":" +track_1_to;
                }else{
                    time_from = Integer.valueOf(splitTime1) + i + ":" +track_2_from;
                    time_to = Integer.valueOf(splitTime1) + i + ":" +track_2_to;
                }

                searchItem = new SearchItem(place_from, place_to, time_from, time_to, track, String.valueOf(travelTime),
                        String.valueOf(price * 35),date);
                items.add(searchItem);

            }
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setHasFixedSize(true);

            recyclerView.setAdapter(new SearchAdapter(items, getContext(),getActivity()));
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
            params.put("from", from);
            params.put("to", to);
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