package com.example.bighomework2.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bighomework2.R;
import com.example.bighomework2.viewModel.DataViewModel;


public class ConnectSettingFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        com.example.bighomework2.databinding.FragmentConnectSettingBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_connect_setting, container, false);
        DataViewModel data = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(DataViewModel.class);
        binding.setSetting(data);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }
}