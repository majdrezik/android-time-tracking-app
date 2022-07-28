package com.example.hourstracker;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.hourstracker.BroadcastReceivers.GPSReceiver;
import com.example.hourstracker.databinding.ActivityMainBinding;
import com.example.hourstracker.ui.Adapters.ShiftsAdapter;
import com.example.hourstracker.ui.Dialogs.ExitAppDialogFragment;
import com.example.hourstracker.ui.Models.Shift;
import com.example.hourstracker.ui.Models.Time;
import com.example.hourstracker.ui.ViewModels.ShiftsViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.hourstracker.R;


import java.time.ZoneId;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements ShiftsAdapter.ShiftAdapterListener {

    private ActivityMainBinding binding;
private GPSReceiver gpsChangeReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        IntentFilter filter = new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION);
        filter.addAction(Intent.ACTION_PROVIDER_CHANGED);
        gpsChangeReceiver= new GPSReceiver();
//        ShiftsViewModel shiftsViewModel = new ViewModelProvider(this).get(ShiftsViewModel.class);
        registerReceiver(gpsChangeReceiver,filter);

//        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main2);


        NavigationUI.setupWithNavController(binding.navView, navController);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.exit_btn:
//                getSupportFragmentManager().beginTransaction().add(android.R.id.content, ).addToBackStack(null).commit();
                ExitAppDialogFragment.newInstance().show(getSupportFragmentManager(),"EXIT");
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void updateShownShiftsList(boolean toAdd, String name) {

    }

    @Override
    public void onShiftChange(int position, Shift shift) {

    }
}