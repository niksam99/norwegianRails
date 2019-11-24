package com.oslomet.norwegianrails.adaptor;

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
import com.oslomet.norwegianrails.viewmodel.TicketItem;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder> {

    public List<TicketItem> cardItems;
    private Context context;



    public TicketAdapter(List<TicketItem> cardItems,Context context){
        this.context=context;
        this.cardItems = cardItems;

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView from;
        TextView to;
        TextView timeFrom;
        TextView timeTo;
        TextView track;
        TextView travel_time;
        TextView price;
        Button cancel;

        public ViewHolder(View itemView) {
            super(itemView);
            this.from = itemView.findViewById(R.id.from);
            this.timeFrom = itemView.findViewById(R.id.time_from);
            this.to = itemView.findViewById(R.id.to);
            this.timeTo = itemView.findViewById(R.id.time_to);
            this.track = itemView.findViewById(R.id.track);
            this.travel_time =itemView.findViewById(R.id.travel_time);
            this.price = itemView.findViewById(R.id.price);
            this.cancel = itemView.findViewById(R.id.btn_cancel);

        }
    }

    @Override
    public TicketAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket,parent,false);
        return new TicketAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TicketAdapter.ViewHolder holder, final int position) {
        holder.from.setText("From: " +cardItems.get(position).from);
        holder.to.setText("To: "+cardItems.get(position).to);
        holder.timeFrom.setText("Time: "+cardItems.get(position).time_from);
        holder.timeTo.setText("Time: "+cardItems.get(position).time_to);
        holder.travel_time.setText("Duration: "+cardItems.get(position).travel_time+"min");
        holder.track.setText(cardItems.get(position).track);
        holder.price.setText("Price: Kr" + cardItems.get(position).price);
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        String date = mDay + "-" + (mMonth + 1) + "-" + mYear;
        String purchase = cardItems.get(position).getDate();;

        Date present = null, purchaseDate = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            present = sdf.parse(date);
            purchaseDate = sdf.parse(purchase);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (present.compareTo(purchaseDate) > 0  ) {
            holder.cancel.setEnabled(true);

        } else if (present.compareTo(present) < 0) {
            holder.cancel.setEnabled(false);

        } else if (present.compareTo(present) == 0) {
            String[] time = cardItems.get(position).time_from.split(":");
            String hour = time[0];
            String minute = time[1];
            if((mHour>Integer.valueOf(hour))){
                holder.cancel.setEnabled(false);
            }else if(mHour == Integer.valueOf(hour)) {
                if(mMinute>Integer.valueOf(minute)){
                    holder.cancel.setEnabled(false);
                }
            }
            else{
                holder.cancel.setEnabled(true);
            }

        }

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = cardItems.get(position).getId();
                String price = cardItems.get(position).getPrice();
                cancelTicket(id,price);

            }
        });

    }
    private void cancelTicket(final String id,final String price){

        String tag_string_req = "Cancel Ticket";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_CANCEL_TICKET, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("response",response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");


                    if (!error) {

                        SharedPreferences pref = context.getSharedPreferences("LoginDetail", MODE_PRIVATE);
                        String uid = pref.getString("uid", "false");
                        Wallet wallet = new Wallet();
                        wallet.addToWallet(uid,price,"ticket",context);
                        Toast.makeText(context,
                                "Ticket Canceled Successfully and payment is return back", Toast.LENGTH_LONG).show();
                        Fragment ticket = Ticket.newInstance();
                        FragmentTransaction transaction =((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout,ticket);
                        transaction.commit();

                    } else {
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(context,
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
                params.put("id", id);
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


