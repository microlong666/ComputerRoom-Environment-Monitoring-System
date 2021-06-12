package com.example.chainplus.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chainplus.R;
import com.example.chainplus.viewModel.DataViewModel;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


public class MineFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        view.findViewById(R.id.textView15);
        DataViewModel data = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(DataViewModel.class);
        
        return view;
    }
}