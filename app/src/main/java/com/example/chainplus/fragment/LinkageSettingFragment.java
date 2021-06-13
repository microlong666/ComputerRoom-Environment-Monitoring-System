package com.example.chainplus.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chainplus.R;
import com.example.chainplus.databinding.FragmentLinkageSettingBinding;
import com.example.chainplus.viewModel.DataViewModel;

public class LinkageSettingFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getActivity().getWindow().setStatusBarColor(getActivity().getColor(R.color.white));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentLinkageSettingBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_linkage_setting, container, false);
        DataViewModel data = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(DataViewModel.class);
        binding.setSetting(data);
        binding.setLifecycleOwner(this);
        return inflater.inflate(R.layout.fragment_linkage_setting, container, false);
    }
}