package com.example.hourstracker.ui.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hourstracker.R;
import com.example.hourstracker.ui.Models.Shift;
import com.example.hourstracker.ui.ViewModels.ShiftsViewModel;

import java.util.ArrayList;

public class ShiftsAdapter extends
        RecyclerView.Adapter<ShiftsAdapter.ShiftViewHolder> {
    Context context;
    ArrayList<Shift> allShifts;
    ShiftAdapterListener listener;
    ShiftsViewModel shiftsViewModel;
    private int selectedPosition = RecyclerView.NO_POSITION;
    public ShiftsAdapter(Context context, ShiftAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        ShiftsViewModel myViewModel = new ViewModelProvider((AppCompatActivity) context).get(ShiftsViewModel.class);
        this.shiftsViewModel = myViewModel;
        myViewModel.getShifts(context).observe((AppCompatActivity) context, countries -> {
            this.allShifts = countries;

            notifyDataSetChanged();

        });

//myViewModel.AddNewShift(new Shift());
        myViewModel.getSelectedShift().observe((AppCompatActivity) context, shift -> {
            notifyItemChanged(this.selectedPosition);
            this.selectedPosition = this.allShifts.indexOf(shift);

            notifyItemChanged(this.selectedPosition);

        });
    }

    @NonNull
    @Override
    public ShiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.rc_shift_row, parent, false);
        ShiftViewHolder viewHolder = new ShiftViewHolder(itemView);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ShiftViewHolder holder, int position) {
//        holder.itemView.set(this.selectedPosition==position? Color.BLUE:Color.WHITE);
        holder.itemView.setSelected(selectedPosition==position);
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return this.allShifts.size();
    }

    //private ArrayList<Shift> getShownCountries(ArrayList<string?>){
//
//}
    public void updateCountriesList(final ArrayList<Shift> countriesArray) {
        this.allShifts.clear();
        this.allShifts = countriesArray;
        notifyDataSetChanged();
    }


    private ArrayList<Shift> getShownCountries(ArrayList<String> shownCountriesNames) {
        ArrayList<Shift> result = new ArrayList<Shift>();
        for (Shift shift : allShifts) {

//            if (shownCountriesNames.contains(shift.getName()))
            result.add(shift);
        }
        return result;
    }

    public void onItemLongCLick(int position) {
        Shift shift = this.allShifts.get(position);
        this.allShifts.remove(shift);
        //this.shiftsViewModel.deleteCountry(shift);
        //this.listener.updateShownShiftsList(false, shift.getName());
        if(this.selectedPosition>position){ // reset the selected position only if the current deleted one is before him in the list , else no need to reset the selected position
//            this.selectedPosition = RecyclerView.NO_POSITION;
//            this.countriesViewModel.setSelectedCountry(null);
            this.selectedPosition =  this.selectedPosition-1;
        }
        if( this.selectedPosition==position){
                        this.selectedPosition = RecyclerView.NO_POSITION;

                        this.shiftsViewModel.setSelectedShift(null);

        }

        shiftsViewModel.notifyChangedShiftsList();
        this.notifyDataSetChanged(); // notify the view for changed dataset in order to refresh the view.
    }

    public Shift getItem(int position) {
        return this.allShifts.get(position);
    }

    class ShiftViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView startTime;
        TextView endTime;
        TextView totalHours;
        TextView date;
        TextView Notes;
        int positionInt;
        public ShiftViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            startTime = itemView.findViewById(R.id.startTime);
            endTime = itemView.findViewById(R.id.endTime);
            Notes = itemView.findViewById(R.id.notes);
            totalHours = itemView.findViewById(R.id.totalHours);
            date = itemView.findViewById(R.id.date);

        }

        private void bindData(int position) {
            final Shift shift = allShifts.get(position);
            positionInt = position;

            startTime.setText(shift.getStartTime()!=null?shift.getStartTime().toString():"");
            endTime.setText(shift.getEndTime()!=null?shift.getEndTime().toString():"");
            Notes.setText(shift.getNotes()!=null?shift.getNotes():"");
            totalHours.setText(""+shift.getTotalHours());
            date.setText(shift.getShiftDate()!=null?shift.getShiftDate().toString():"");
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onItemLongCLick(position);
//                    Toast.makeText(context,
//                            //String.format("The shift (%s) deleted successfully", shift.getName()),
//                            Toast.LENGTH_LONG).show();
                    return false;
                }
            });
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
////                    notifyItemChanged(selectedPosition);
//                    shiftsViewModel.setSelectedShift(shift);
////                    notifyItemChanged(getLayoutPosition());
//                    ((ShiftAdapterListener) context).onShiftChange(position, shift);
//
//                }
//            });
        }





    }

    public interface ShiftAdapterListener {
        public void updateShownShiftsList(boolean toAdd, String name);
        public void onShiftChange(int position, Shift shift);
    }
}
