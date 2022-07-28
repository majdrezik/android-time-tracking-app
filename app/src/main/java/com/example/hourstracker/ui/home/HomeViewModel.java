package com.example.hourstracker.ui.home;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hourstracker.ui.Models.Shift;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

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
    public void saveEntrance(Shift shift, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shifts", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();
//        Gson gson = new Gson();
        String json = gson.toJson(shift);
        editor.putString("entrance",json);
        editor.commit();
    }
    public Shift getSavedEntrance(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shifts", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Type type = new TypeToken<Shift>(){}.getType();
        try{
            return gson.fromJson(sharedPreferences.getString("entrance","null"),type);
        }catch(JsonParseException ex){
            this.deleteEntrance(context);
            return null;
        }

    }
    public void deleteEntrance(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shifts", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("entrance");
        editor.commit();
    }
}