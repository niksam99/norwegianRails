package com.oslomet.norwegianrails.adaptor;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.oslomet.norwegianrails.ui.Ticket;
import com.oslomet.norwegianrails.ui.Wallet;
import com.oslomet.norwegianrails.viewmodel.SearchItem;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.oslomet.norwegianrails.datasource.AppConfig;
import com.oslomet.norwegianrails.datasource.AppController;

import static android.content.Context.MODE_PRIVATE;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    public List<SearchItem> cardItems;
    private Context context;

    Activity activity;

    public SearchAdapter(List<SearchItem> cardItems,Context context, Activity activity){
        this.context=context;
        this.cardItems = cardItems;
        this.activity = activity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView from;
        TextView to;
        TextView timeFrom;
        TextView timeTo;
        TextView track;
        TextView travelTime;
        TextView price;
        Button buyTicket;

        public ViewHolder(View itemView) {
            super(itemView);
            this.from = itemView.findViewById(R.id.from);
            this.timeFrom = itemView.findViewById(R.id.time_from);
            this.to = itemView.findViewById(R.id.to);
            this.timeTo = itemView.findViewById(R.id.time_to);
            this.track = itemView.findViewById(R.id.track);
            this.travelTime =itemView.findViewById(R.id.travel_time);
            this.price = itemView.findViewById(R.id.price);
            this.buyTicket = itemView.findViewById(R.id.btn_buy_ticket);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.from.setText(cardItems.get(position).from);
        holder.to.setText(cardItems.get(position).to);
        holder.timeFrom.setText(cardItems.get(position).time_from);
        holder.timeTo.setText(cardItems.get(position).time_to);
        holder.track.setText(cardItems.get(position).track);
        holder.travelTime.setText(cardItems.get(position).travelTime +" min");
        holder.price.setText("Price: Kr" + cardItems.get(position).price);
        holder.buyTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = context.getSharedPreferences("LoginDetail", MODE_PRIVATE);
                String login = prefs.getString("login", "false");
                String uid = prefs.getString("uid", "null");
                if (login.equals("true")) {
                    SharedPreferences pref = context.getSharedPreferences("Wallet", MODE_PRIVATE);
                    String wallet_amount = pref.getString("wallet_amount", "0");
                    String price = cardItems.get(position).getPrice();
                    String from = cardItems.get(position).getFrom();
                    String to = cardItems.get(position).getTo();
                    String timeFrom = cardItems.get(position).getTimeFrom();
                    String timeTo = cardItems.get(position).getTimeTo();
                    String travelTime = cardItems.get(position).getTravelTime();
                    String track = cardItems.get(position).getTrack();
                    String date = cardItems.get(position).getDate();
                    assert wallet_amount != null;
                    Log.e("wallet_amount",wallet_amount);
                    if(Integer.valueOf(wallet_amount)>=Integer.valueOf(price))
                    {
                        Log.e("value",uid + " " + from  + " " + to  + " " + timeFrom + " " +
                                timeTo + " " + travelTime + " " + track + " " + price +" " + date);
                        purchaseTicket(uid,from,to,timeFrom,timeTo,travelTime,track,price,date);
                    }else{
                        Toast.makeText(context, "Insufficient balance",
                                Toast.LENGTH_SHORT).show();
                        Fragment wallet = Wallet.newInstance();
                        FragmentTransaction transaction =((FragmentActivity)context).getSupportFragmentManager()
                                .beginTransaction().addToBackStack(null);
                        transaction.replace(R.id.frame_layout,wallet);
                        transaction.commit();

                    }
                }else{
                    Toast.makeText(context, "Login first for purchase",
                            Toast.LENGTH_SHORT).show();
                     }


            }
        });
    }
    private void purchaseTicket(final String uid,final String from,final String to,final String
                                timeFrom,final String timeTo, final String travelTime,
                                final String track, final String price,final String date){

        String tag_string_req = "Purchase Ticket";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_BUY_TICKET, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("response",response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");


                    if (!error) {

                        Toast.makeText(context,
                                "Successfully purchased ticket", Toast.LENGTH_LONG).show();
                        String wallet_amount = jObj.getString("wallet_amount");
                        SharedPreferences pref = context.getSharedPreferences("Wallet", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("wallet_amount", wallet_amount);
                        editor.apply();
                        Log.e("wallet_amount after buy",wallet_amount);
                        Fragment ticket = Ticket.newInstance();
                        FragmentTransaction transaction =((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout,ticket);
                        transaction.commit();

                    } else {
                        Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_LONG).show();
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
                    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = null;
                    if (cm != null) {
                        activeNetwork = cm.getActiveNetworkInfo();
                    }
                    if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                        Toast.makeText(context, "Server is not connected to internet.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Your device is not connected to internet.",
                                Toast.LENGTH_SHORT).show();
                    }
                } else if (error instanceof NetworkError || error.getCause() instanceof ConnectException
                        || (error.getCause().getMessage() != null
                        && error.getCause().getMessage().contains("connection"))) {
                    Toast.makeText(context, "Your device is not connected to internet.",
                            Toast.LENGTH_SHORT).show();
                } else if (error.getCause() instanceof MalformedURLException) {
                    Toast.makeText(context, "Bad Request.", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError || error.getCause() instanceof IllegalStateException
                        || error.getCause() instanceof JSONException
                        || error.getCause() instanceof XmlPullParserException) {
                    Toast.makeText(context, "Parse Error (because of invalid json or xml).",
                            Toast.LENGTH_SHORT).show();
                } else if (error.getCause() instanceof OutOfMemoryError) {
                    Toast.makeText(context, "Out Of Memory Error.", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(context, "server couldn't find the authenticated request.",
                            Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError || error.getCause() instanceof ServerError) {
                    Toast.makeText(context, "Server is not responding.", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError || error.getCause() instanceof SocketTimeoutException
                        || error.getCause() instanceof ConnectTimeoutException
                        || error.getCause() instanceof SocketException
                        || (error.getCause().getMessage() != null
                        && error.getCause().getMessage().contains("Connection timed out"))) {
                    Toast.makeText(context, "Connection timeout error",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "An unknown error occurred.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("unique_id", uid);
                params.put("from", from);
                params.put("to", to);
                params.put("time_from", timeFrom);
                params.put("time_to", timeTo);
                params.put("travel_time", travelTime);
                params.put("track", track);
                params.put("price", price);
                params.put("date", date);
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

    @Override
    public int getItemCount() {
        return cardItems.size();
    }


}

