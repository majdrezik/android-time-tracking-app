package com.example.hourstracker.ui.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.hourstracker.R;


public class ExitAppDialogFragment extends DialogFragment {


    private IExitAppDialogFragment listener;
    public ExitAppDialogFragment() {
    }


    public static ExitAppDialogFragment newInstance() {
        ExitAppDialogFragment fragment = new ExitAppDialogFragment();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        try {
            this.listener = (IExitAppDialogFragment) context;
        } catch (ClassCastException e) {
            this.listener=null;
//            throw new ClassCastException("the class " +
//                    getActivity().getClass().getName() +
//                    " must implements the interface 'IExitAppDialogFragment'");
        }
        super.onAttach(context);
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return new AlertDialog.Builder(getActivity())
                .setTitle("System warning!")
                .setMessage("Are you sure you want to exit the app?")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if(listener!=null){
                                    ((IExitAppDialogFragment)getActivity()).BeforeExitApp();
                                }
                                getActivity().finishAffinity();

                            }
                        }
                )
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exit_app_dialog, container, false);
    }
    public interface IExitAppDialogFragment {
        void BeforeExitApp();
    }
}