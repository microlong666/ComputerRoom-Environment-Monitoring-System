package com.example.chainplus.fragment;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chainplus.R;
import com.example.chainplus.databinding.FragmentDataBinding;
import com.example.chainplus.util.ColorUtil;
import com.example.chainplus.viewModel.DataViewModel;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

public class DataFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getActivity().getWindow().setStatusBarColor(getActivity().getColor(R.color.background));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_data, container, false);
        DataViewModel data = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(DataViewModel.class);
        binding.setData(data);
        binding.setLifecycleOwner(this);
        data.getHealth().observe(getActivity(), integer -> {
            binding.circularProgress.setProgress((float) data.getHealth().getValue());

            // 重绘图标，getActivity有可能是null
            FragmentActivity context = getActivity();
            if (context != null) {
                Drawable drawable = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_leaf);
                drawable.setColorFilter(ColorUtil.getColorFromHealth(data.getHealth().getValue()), PorterDuff.Mode.SRC_ATOP);
                binding.imageView3.setImageDrawable(drawable);
            }
        });
        data.getTemperature().observe(getActivity(), aDouble -> binding.textView.setTextColor(ColorUtil.setTextColor(data.getTemperature().getValue(), "".equals(data.getTemperatureThreshold().getValue()) ? 1000 : Double.parseDouble(data.getTemperatureThreshold().getValue()))));
        data.getTemperature().observe(getActivity(), aDouble -> binding.textView6.setTextColor(ColorUtil.setTextColor(data.getHumidity().getValue(), "".equals(data.getHumidityThreshold().getValue()) ? 1000 : Double.parseDouble(data.getHumidityThreshold().getValue()))));
        data.getTemperature().observe(getActivity(), aInteger -> binding.textView12.setTextColor(ColorUtil.setTextColor(data.getPm25().getValue(), "".equals(data.getPm25Threshold().getValue()) ? 100 : Integer.parseInt(data.getPm25Threshold().getValue()))));

        return binding.getRoot();
    }


    @BindingAdapter("android:src")
    public static void setSrc(AppCompatImageView view, int resId) {
        view.setImageResource(resId);
    }
}