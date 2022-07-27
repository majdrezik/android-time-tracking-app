package com.example.hourstracker.ui.ViewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hourstracker.ui.Models.Shift;

import java.util.ArrayList;

public class ShiftsViewModel extends ViewModel {
    private ArrayList<Shift> allShifts = new ArrayList<>();
    private MutableLiveData<ArrayList<Shift>> shifts;
    private ArrayList<Shift> shiftsList;
    private MutableLiveData<Shift> selectedShift;
    private  Context context;
    public synchronized LiveData<ArrayList<Shift>> getShifts(Context context) {
        if (shifts == null) {
            shifts = new MutableLiveData<>();
            this.context = context;
            loadShifts(context);
        }

        return shifts;
    }

    private void loadShifts(Context context) {
        //countriesList = CountryXMLParser.parseCountries(context);
        allShifts = new ArrayList<>(shiftsList);
        this.shifts.setValue(shiftsList);
    }

    public void loadShifts() {

        //CountryXMLParser.parseCountries(context);
    }


    public synchronized  void setSelectedShift(Shift shift){
        if(this.selectedShift ==null){
            selectedShift = new MutableLiveData<>();
        }
        this.selectedShift.postValue(shift);
    }
    public synchronized LiveData<Shift> getSelectedShift(){
        if(this.selectedShift ==null){
            selectedShift = new MutableLiveData<>();
        }
        return selectedShift;
    }

    public void notifyChangedShiftsList(){
        this.shifts.setValue(this.shiftsList);
    }


    public void reloadAllCountriesFromDB(){
            this.shiftsList = new ArrayList<Shift>(this.allShifts);
            this.notifyChangedShiftsList();
    }

}
