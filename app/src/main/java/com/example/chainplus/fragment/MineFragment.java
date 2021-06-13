package com.example.chainplus.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.chainplus.R;
import com.example.chainplus.viewModel.DataViewModel;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


public class MineFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getActivity().getWindow().setStatusBarColor(getActivity().getColor(R.color.background));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getActivity().getWindow().setStatusBarColor(getActivity().getColor(R.color.background));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        TextView health = view.findViewById(R.id.textView15);
        ProgressBar bar = view.findViewById(R.id.progressBar);
        DataViewModel data = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(DataViewModel.class);
        data.getHealth().observe(getActivity(), integer -> {
            health.setText(String.valueOf(integer));
            bar.setProgress(integer);
        });
        return view;
    }
}