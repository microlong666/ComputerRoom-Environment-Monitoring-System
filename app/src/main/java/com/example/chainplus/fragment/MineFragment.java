package com.example.chainplus.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chainplus.AboutActivity;
import com.example.chainplus.R;
import com.example.chainplus.databinding.FragmentMineBinding;
import com.example.chainplus.viewModel.DataViewModel;

import java.util.ArrayList;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
        FragmentMineBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mine, container, false);
        DataViewModel data = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(DataViewModel.class);
        binding.setData(data);
        binding.setLifecycleOwner(this);

        ListView listView = binding.settingMenu;
        ArrayList<ListItem> list = new ArrayList<>();

        // 连接设置菜单项
        ListItem item = new ListItem();
        item.setImage(getActivity().getDrawable(R.drawable.ic_connect_setting));
        item.setTitle("连接设置");
        list.add(item);
        // 联动设置
        item = new ListItem();
        item.setImage(getActivity().getDrawable(R.drawable.ic_linkage_setting));
        item.setTitle("联动设置");
        list.add(item);
        // 返回adapter
        MyListViewAdapter adapter = new MyListViewAdapter(list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0){
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.from_right, R.anim.out_left).replace(R.id.fragmentContainerView, new ConnectSettingFragment()).addToBackStack(null).commit();
            } else if (position == 1) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.from_right, R.anim.out_left).replace(R.id.fragmentContainerView, new LinkageSettingFragment()).addToBackStack(null).commit();
            }
        });

        // 其他菜单
        listView = binding.otherMenu;
        list = new ArrayList<>();

        // 检查更新
        item = new ListItem();
        item.setImage(getActivity().getDrawable(R.drawable.ic_download));
        item.setTitle("检查更新");
        list.add(item);
        // 帮助和反馈
        item = new ListItem();
        item.setImage(getActivity().getDrawable(R.drawable.ic_edit));
        item.setTitle("帮助和反馈");
        list.add(item);
        // 关于
        item = new ListItem();
        item.setImage(getActivity().getDrawable(R.drawable.ic_info));
        item.setTitle("关于");
        list.add(item);
        // 返回adapter
        adapter = new MyListViewAdapter(list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0){
                Toast.makeText(getContext(), "已经是最新版本", Toast.LENGTH_SHORT).show();
            } else if (position == 1) {
                Uri uri = Uri.parse("https://github.com");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                getActivity().startActivity(intent);
            } else if (position == 2) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(getActivity(), AboutActivity.class));
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }

    /**
     * 封装两个视图组件的类
     */
    static class ListItemView {
        ImageView imageView;
        TextView textView;
    }

    /**
     * 封装了两个资源的类
     */
    static class ListItem {
        private Drawable image;
        private String title;

        public Drawable getImage() {
            return image;
        }

        public void setImage(Drawable image) {
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

    }

    class MyListViewAdapter extends BaseAdapter {

        private final ArrayList<ListItem> list;

        public MyListViewAdapter(ArrayList<ListItem> list) {
            this.list = list;
        }

        /**
         * 返回item的个数
         */
        @Override
        public int getCount() {
            return list.size();
        }

        /**
         * 返回item的内容
         */
        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * 返回item的视图
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListItemView listItemView;

            // 初始化item view
            if (convertView == null) {
                // 通过LayoutInflater将xml中定义的视图实例化到一个View中
                convertView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.menu_item_layout, null);

                // 实例化一个封装类ListItemView，并实例化它的两个域
                listItemView = new ListItemView();
                listItemView.imageView = convertView.findViewById(R.id.image);
                listItemView.textView = convertView.findViewById(R.id.title);

                // 将ListItemView对象传递给convertView
                convertView.setTag(listItemView);
            } else {
                // 从convertView中获取ListItemView对象
                listItemView = (ListItemView) convertView.getTag();
            }

            // 获取到mList中指定索引位置的资源
            Drawable img = list.get(position).getImage();
            String title = list.get(position).getTitle();

            // 将资源传递给ListItemView的两个域对象
            listItemView.imageView.setImageDrawable(img);
            listItemView.textView.setText(title);

            // 返回convertView对象
            return convertView;
        }

    }
}