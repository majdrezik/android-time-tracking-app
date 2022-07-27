package com.example.hourstracker.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hourstracker.R;
import com.example.hourstracker.databinding.FragmentDashboardBinding;
import com.example.hourstracker.ui.Adapters.ShiftsAdapter;
import com.example.hourstracker.ui.ViewModels.ShiftsViewModel;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    RecyclerView rvShifts;
    ShiftsAdapter adapter;
    ShiftsAdapter.ShiftAdapterListener listener;
    ShiftsViewModel myViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ShiftsViewModel myViewModel = new ViewModelProvider((AppCompatActivity) context).get(ShiftsViewModel.class);
        this.myViewModel = myViewModel;

        if(context instanceof ShiftsAdapter.ShiftAdapterListener){
            listener = (ShiftsAdapter.ShiftAdapterListener)context;
        }else{
            throw new RuntimeException(context.toString()+" Must implement ShiftAdapterListener interface.");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvShifts = view.findViewById(R.id.rvShifts);

        //final TextView textView = binding.textDashboard;
        //dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        adapter = new ShiftsAdapter(getActivity(),listener);
//        adapter.sort();
        rvShifts.setAdapter(adapter);
        rvShifts.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}