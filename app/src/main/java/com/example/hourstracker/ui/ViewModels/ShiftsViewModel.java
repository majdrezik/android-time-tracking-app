package com.example.hourstracker.ui.ViewModels;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hourstracker.ui.Models.Shift;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ShiftsViewModel extends ViewModel {
    private ArrayList<Shift> allShifts = new ArrayList<>();
    private MutableLiveData<ArrayList<Shift>> shifts;
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
public void addNewShift(Shift newShift){
    allShifts.add(newShift);
    SharedPreferences sharedPreferences = context.getSharedPreferences("shifts", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    Gson gson = new Gson();
    String json = gson.toJson(allShifts);
    editor.putString("shifts",json);
    editor.commit();
    this.shifts.setValue(allShifts);

}
    private void loadShifts(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shifts", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Shift>>(){}.getType();

        allShifts = new ArrayList<>(gson.fromJson(sharedPreferences.getString("shifts","[]"),type));
        this.shifts.setValue(allShifts);
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
        this.shifts.setValue(this.allShifts);
    }


    public void reloadAllCountriesFromDB(){
            this.allShifts = new ArrayList<Shift>(this.allShifts);
            this.notifyChangedShiftsList();
    }

}
