package com.example.hourstracker.ui.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.hourstracker.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoteDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class NoteDialogFragment extends DialogFragment{
    private TextView note;
    private Dialog alertDialog;
    private INoteDialogListener listener;
    private View dialogView;
    public static NoteDialogFragment newInstance(INoteDialogListener listener) {
        NoteDialogFragment frag = new NoteDialogFragment();
        frag.listener = listener;
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = requireActivity().getLayoutInflater();
dialogView = inflater.inflate(R.layout.note_dialog, null);
        alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("Set precision:")
                .setView(dialogView)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                listener.onFinishSelectPrecision(note.getText().toString());
                            }
                        }
                )
                .create();
        return alertDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        note = dialogView.findViewById(R.id.note_dialog);

        if(getArguments()!=null){
        }

        return super.onCreateView(inflater,container,savedInstanceState);
    }




    public interface INoteDialogListener {
        void onFinishSelectPrecision(String note);

    }


}