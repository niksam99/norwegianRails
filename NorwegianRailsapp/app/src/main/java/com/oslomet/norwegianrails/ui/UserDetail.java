package com.oslomet.norwegianrails.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.oslomet.norwegianrails.R;

import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;


public class UserDetail extends Fragment {
    public static UserDetail newInstance() {
        UserDetail fragment = new UserDetail();
        return fragment;
    }
    private TextView tvName, tvEmail, tvCreate;
    private Button btnLogout;
    SharedPreferences prefs;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_detail, container, false);
        tvName = v.findViewById(R.id.name);
        tvEmail = v.findViewById(R.id.email);
        tvCreate = v.findViewById(R.id.create);
        btnLogout = v.findViewById(R.id.btnLogout);
        prefs = getActivity().getSharedPreferences("LoginDetail", MODE_PRIVATE);
        String name = prefs.getString("name", "name");
        String email = prefs.getString("email","email");
        String createAt = prefs.getString("createAt","createAt");
        String[] splitCreateAt = createAt.split("\\s+");

        Log.e("name",name + email + createAt);
        tvName.setText(name);
        tvEmail.setText(email);
        tvCreate.setText(splitCreateAt[0]);
        btnLogout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                prefs = getActivity().getSharedPreferences("LoginDetail", MODE_PRIVATE);
                prefs .edit().clear().apply();
                Profile profile= new Profile();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, profile)
                        .addToBackStack(null)
                        .commit();
            }

        });
        return v;
    }
}