package com.example.hourstracker.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.hourstracker.R;
import com.example.hourstracker.databinding.FragmentHomeBinding;
import com.example.hourstracker.ui.Dialogs.NoteDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements NoteDialogFragment.INoteDialogListener {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private Chronometer chronometer;
    private long pauseOffSet = 0;
    private boolean isPlaying = false;
    private ToggleButton toggleButton;
    private Button reset_btn;
    private List<Integer> times = new ArrayList<>();
    private int listIndex = 0;
    private String m_Text = "";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.onCreate(savedInstanceState);
       //viewy setContentView(R.layout.activity_main);
        chronometer = view.findViewById(R.id.chronometer);
        toggleButton = view.findViewById(R.id.Toggle);
//        reset_btn = view.findViewById(R.id.reset_btn);
        toggleButton.setText(null);
        toggleButton.setTextOn(null);
        toggleButton.setTextOff(null);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean buttonState) {
                //buttonState: true if button is pressed, otherwise false
                if(buttonState){
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    // SystemClock.elapsedRealtime() is the number of milliseconds since the device was turned on.
                    // without the following line, when we stop the chronometer for a specific time, it will start again with the specific time that passed in the background.
                    // i.e. if we stopped the timer at 5:00 for 30 seconds, and start again, it'll start from 5:30 instead of 5:00
                    showToast("start timer");
//                    chronometer.setBase(SystemClock.elapsedRealtime()- pauseOffSet);
//                    System.out.println(SystemClock.elapsedRealtime()- pauseOffSet);
                    chronometer.start();
                    isPlaying = true;
                }else{
//                    NoteDialogFragment.newInstance(HomeFragment.this).show(getFragmentManager(),"");
                    showToast("pause timer");
                    chronometer.stop();
//                    pauseOffSet = SystemClock.elapsedRealtime()- chronometer.getBase();
                    isPlaying = false;
                    updateTimesList();
                    showDialog();
                }
            }
        });

        // in our project, we decided that the restart could be done only when the timer is running. i.e, isPlaying == true
//        reset_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(isPlaying){
//                    showToast("reset timer");
//                    chronometer.setBase(SystemClock.elapsedRealtime());
//                    pauseOffSet = 0;
//                    chronometer.start();
//                    isPlaying = true;
//                    calculateTotalTime();
//                }
//            }
//        });
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Title");

// Set up the input
        final EditText input = new EditText(getActivity());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
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

    @Override
    public void onFinishSelectPrecision(String note) {

    }
}