package com.oslomet.norwegianrails.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.oslomet.norwegianrails.R;

import java.util.ArrayList;

public class SearchTo extends Fragment {
    String selection;
    public static SearchTo newInstance() {
        SearchTo fragment = new SearchTo();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_to, container, false);
        if (this.getArguments() != null) {
           selection= this.getArguments().getString("selection");
        }
        TextView tvWhereFrom = v.findViewById(R.id.whereFrom);
        ImageView ivClear = v.findViewById(R.id.clear);
        AutoCompleteTextView suggestion_box = v.findViewById(R.id.suggestion_box);
        String display = "From " +"<b>" + selection + "</b> " + "to";
        tvWhereFrom.setText(Html.fromHtml(display));
        ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Search search= new Search();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, search)
                        .addToBackStack(null)
                        .commit();
            }
        });
        ArrayList places = new ArrayList<>();
        places.add("Nationaltheatret");
        places.add("Oslo S");
        places.add("Lillestrom");
        places.add("Oslo Lufthavn");
        places.add("Eidsvoll Verk");
        places.add("Eidsvoll");
        places.remove(selection);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_dropdown_item,places);
        suggestion_box.setAdapter(adapter);
        suggestion_box.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                String to = (String)parent.getItemAtPosition(position);
                Fragment selectDateTime = SelectDateTime.newInstance();
                Bundle bundle=new Bundle();
                bundle.putString("to", to);
                bundle.putString("from",selection);
                selectDateTime.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("search");
                transaction.replace(R.id.frame_layout,selectDateTime);
                transaction.commit();
            }
        });
        return v;

    }


}
