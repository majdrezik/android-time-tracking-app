package com.example.hourstracker.ui.home;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.hourstracker.MainActivity;
import com.example.hourstracker.R;
import com.example.hourstracker.Service.ExceededHoursService;
import com.example.hourstracker.databinding.FragmentHomeBinding;
import com.example.hourstracker.ui.Dialogs.NoteDialogFragment;
import com.example.hourstracker.ui.Models.Shift;
import com.example.hourstracker.ui.ViewModels.ShiftsViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.N)
public class HomeFragment extends Fragment implements NoteDialogFragment.INoteDialogListener {
    int LOCATION_REFRESH_TIME = 15000; // 15 seconds to update
    int LOCATION_REFRESH_DISTANCE = 500; // 500 meters to update
    int SERVICE_ID=2;
    private FragmentHomeBinding binding;
    private LocationManager mLocationManager;
    ActivityResultLauncher<String[]> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts
                            .RequestMultiplePermissions(), result -> {
                        Boolean fineLocationGranted = result.getOrDefault(
                                Manifest.permission.ACCESS_FINE_LOCATION, false);
                        Boolean coarseLocationGranted = result.getOrDefault(
                                Manifest.permission.ACCESS_COARSE_LOCATION,false);
                        if (fineLocationGranted != null && fineLocationGranted) {
                            // Precise location access granted.
                        } else if (coarseLocationGranted != null && coarseLocationGranted) {
                            // Only approximate location access granted.
                        } else {
                            // No location access granted.
                        }
                    }
            );
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            locationPermissionRequest.launch(new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                    LOCATION_REFRESH_DISTANCE, mLocationListener);


        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public static Location currLocation;
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
            currLocation = location;
        }
    };

    private Chronometer chronometer;
    private long pauseOffSet = 0;
    private boolean isPlaying = false;
    private ToggleButton toggleButton;
    private Button reset_btn;
    private List<Integer> times = new ArrayList<>();
    private int listIndex = 0;
    private String m_Text = "";
    private HomeViewModel homeViewModel;
    private ShiftsViewModel shiftsViewModel;
    private Date lastEndTime;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.onCreate(savedInstanceState);
        chronometer = view.findViewById(R.id.chronometer);
        toggleButton = view.findViewById(R.id.Toggle);
        toggleButton.setText(null);
        toggleButton.setTextOn(null);
        toggleButton.setTextOff(null);
        homeViewModel = new ViewModelProvider((AppCompatActivity) getActivity()).get(HomeViewModel.class);
        shiftsViewModel =  new ViewModelProvider((AppCompatActivity) getActivity()).get(ShiftsViewModel.class);
        Shift saveShift= homeViewModel.getSavedEntrance(getActivity());

        if(saveShift!=null){
            Date currentTime = Calendar.getInstance().getTime();
            long fromMS = 0;
            if(saveShift.getEndTime()==null){
                fromMS=currentTime.getTime();
            }else{
                fromMS=saveShift.getEndTime().getTime();
            }
            long diffMS = fromMS-saveShift.getStartTime().getTime();
            long diff = diffMS/1000; // parse to seconds
            long diffMinutes = diff/60; // parse to minutes
            int hours = (int)diffMinutes/60;
            int minutes =(int)(diffMinutes -hours*60)%60;
            chronometer.setBase(SystemClock.elapsedRealtime() - diffMS);

            if(saveShift.getEndTime()==null){
                chronometer.start();
                ((ToggleButton)view.findViewById(R.id.Toggle)).setChecked(true);
                isPlaying=true;

            }else{
                chronometer.stop();
                ((ToggleButton)view.findViewById(R.id.Toggle)).setChecked(false);
                isPlaying=false;
            }

            //            chronometer.setText(hours+":"+minutes+":"+diff%60);
        }
        this.refreshSummary();
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                ToggleButton bnt = (ToggleButton) view.findViewById(R.id.Toggle);
                Boolean buttonState =bnt.isChecked();

                //buttonState: true if button is pressed, otherwise false
                if(buttonState){
//                    myViewModel.setCurrentShift();

                    saveShiftEntranceToDB();
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    // SystemClock.elapsedRealtime() is the number of milliseconds since the device was turned on.
                    // without the following line, when we stop the chronometer for a specific time, it will start again with the specific time that passed in the background.
                    // i.e. if we stopped the timer at 5:00 for 30 seconds, and start again, it'll start from 5:30 instead of 5:00
                    showToast("start timer");

                    chronometer.start();
                    isPlaying = true;
                }else{
                    bnt.setChecked(true);
                    lastEndTime =  Calendar.getInstance().getTime();
                    NoteDialogFragment dilaog= NoteDialogFragment.newInstance(HomeFragment.this,"");
                    dilaog.show(getFragmentManager(),"note");

                }
                HomeFragment.this.refreshSummary();;

            }
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    void refreshSummary(){
        Shift saveShift= homeViewModel.getSavedEntrance(getActivity());
        TextView summary = HomeFragment.this.getView().findViewById(R.id.summaryHome);
if(summary==null){
    return ;
}
        if(saveShift!=null){
            DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
            String summaryTxt = "";
            summaryTxt =dateFormat.format(saveShift.getStartTime())+"-";
            Intent serviceIntent = new Intent(getActivity(),ExceededHoursService.class );
            serviceIntent.putExtra("startDate",saveShift.getStartTime());
            if(saveShift.getEndTime()!=null){
                summaryTxt+=dateFormat.format(saveShift.getEndTime());
                getActivity().getApplicationContext().stopService(serviceIntent);

            }else{
                getActivity().getApplicationContext().startService(serviceIntent);
            }
            summary.setText(summaryTxt);

        }else{
            summary.setText("");
        }
    }
    private void saveShiftEntranceToDB() {
        Shift newShift = new Shift();
        Date currentTime = Calendar.getInstance().getTime();
        if(currLocation!=null) {
            newShift.setStartLatitude(currLocation.getLatitude());
            newShift.setStartLongitude(currLocation.getLongitude());
        }
        newShift.setStartTime(currentTime);
        homeViewModel.saveEntrance(newShift,getActivity());
    }


    private void updateTimesList(){
        int stoppedMilliseconds = 0;

        String chronoText = chronometer.getText().toString();
        String array[] = chronoText.split(":");
        if (array.length == 2) {
            stoppedMilliseconds = Integer.parseInt(array[0]) * 60
                    + Integer.parseInt(array[1]) ;
        } else if (array.length == 3) {
            stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60
                    + Integer.parseInt(array[1]) * 60
                    + Integer.parseInt(array[2]);
        }
//        int printValue;
        int maxSoFar=0;
        times.add(listIndex++, stoppedMilliseconds);
        System.out.println("--------------------------");
        System.out.println("added " + stoppedMilliseconds);
    }

    private void printList(){
        for(int i=0;i<times.size();i++){
            System.out.println(times.get(i));
        }
    }


    private int calculateTotalTime(){
        if(times.get(0) == null){
            System.out.println("No times stored...");
            System.exit(1);
        }
        int totalTime=times.get(0);
        int index=1;
        while (index < times.size()){
            totalTime += times.get(index) - times.get(index-1);
            index++;
        }
        System.out.println("totalTime : " + totalTime);
        return totalTime;
    }

    private void showToast(String msg){
        Toast.makeText(getActivity().getApplicationContext(),msg,Toast.LENGTH_SHORT).show();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onFinishEnterNote(String note) {
        ToggleButton bnt = (ToggleButton) getActivity().findViewById(R.id.Toggle);
        bnt.setChecked(false);
        showToast("pause timer");
        chronometer.stop();
        isPlaying = false;
        updateTimesList();
        Shift currShift = homeViewModel.getSavedEntrance(getActivity());
        currShift.setEndTime(lastEndTime);
        currShift.setNotes(note);
        if(currLocation!=null) {
            currShift.setEndLatitude(currLocation.getLatitude());
            currShift.setEndLongitude(currLocation.getLongitude());
        }
        this.homeViewModel.saveEntrance(currShift,getActivity());
        this.shiftsViewModel.addNewShift(currShift,getActivity());
        this.refreshSummary();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCancelEnterNote() {
        this.refreshSummary();

//        ToggleButton bnt = (ToggleButton) getActivity().findViewById(R.id.Toggle);
//        bnt.setChecked(true);
    }


}