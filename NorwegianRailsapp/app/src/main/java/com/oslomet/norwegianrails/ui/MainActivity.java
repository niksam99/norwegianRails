package com.oslomet.norwegianrails.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.oslomet.norwegianrails.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {
    SharedPreferences prefs;
    Bundle bundle;
    String login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.bottomNavigationSearchMenuId:
                                selectedFragment = Search.newInstance();
                                break;
                            case R.id.bottomNavigationTicketMenuId:
                                selectedFragment = Ticket.newInstance();
                                break;

                            case R.id.bottomNavigationWalletMenuId:
                                prefs = getSharedPreferences("LoginDetail", MODE_PRIVATE);
                                login = prefs.getString("login", "false");
                                if (login.equals("true")) {
                                    selectedFragment = Wallet.newInstance();
                                }else{
                                    selectedFragment = Profile.newInstance();
                                    bundle=new Bundle();
                                    bundle.putString("from", "wallet");
                                    selectedFragment.setArguments(bundle);
                                }
                                break;

                            case R.id.bottomNavigationProfileMenuId:
                                prefs = getSharedPreferences("LoginDetail", MODE_PRIVATE);
                                String login = prefs.getString("login", "false");
                                if (login.equals("true")) {
                                    selectedFragment = UserDetail.newInstance();
                                }else{
                                    selectedFragment = Profile.newInstance();
                                    bundle=new Bundle();
                                    bundle.putString("from", "profile");
                                    selectedFragment.setArguments(bundle);
                                }
                                break;
                        }

                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, Search.newInstance());
        transaction.commit();
    }
    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        }
        else if(getFragmentManager().getBackStackEntryCount() == 1) {
            moveTaskToBack(false);
        }
        else {
            getFragmentManager().popBackStack();
        }
    }

}
