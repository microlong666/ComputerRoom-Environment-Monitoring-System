package com.example.bighomework2.fragment;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bighomework2.R;
import com.example.bighomework2.databinding.FragmentDataBinding;
import com.example.bighomework2.viewModel.DataViewModel;

public class DataFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        com.example.bighomework2.databinding.FragmentDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_data, container, false);
        DataViewModel data = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(DataViewModel.class);
        binding.setData(data);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @BindingAdapter("android:src")
    public static void setSrc(AppCompatImageView view, int resId) {
        view.setImageResource(resId);
    }
}