package com.example.hourstracker.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hourstracker.ui.Models.Shift;
import com.example.hourstracker.ui.Models.Time;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<Shift> shift;
    private Shift currentShift;
    public HomeViewModel() {
        shift = new MutableLiveData<>();
    }

    public LiveData<Shift> getShift() {
        return shift;
    }

    public void setCurrentShift(Shift currentShift ){
        this.currentShift=currentShift;
        this.shift.setValue(currentShift);
    }
}