package com.oslomet.norwegianrails.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.oslomet.norwegianrails.R;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class Search extends Fragment {
    public static Search newInstance() {
        Search fragment = new Search();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        AutoCompleteTextView suggestion_box;
        ArrayList places = new ArrayList<>();
        suggestion_box = v.findViewById(R.id.suggestion_box);
        places.add("Nationaltheatret");
        places.add("Oslo S");
        places.add("Lillestrom");
        places.add("Oslo Lufthavn");
        places.add("Eidsvoll Verk");
        places.add("Eidsvoll");
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, places);
        suggestion_box.setAdapter(adapter);
        suggestion_box.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                String selection = (String) parent.getItemAtPosition(position);
                Fragment searchTo = SearchTo.newInstance();
                Bundle bundle = new Bundle();
                bundle.putString("selection", selection);
                searchTo.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("search");
                transaction.replace(R.id.frame_layout, searchTo);
                transaction.commit();

            }
        });
        return v;
    }
}