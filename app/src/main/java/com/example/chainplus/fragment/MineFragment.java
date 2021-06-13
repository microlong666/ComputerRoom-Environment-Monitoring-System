package com.example.chainplus.fragment;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.chainplus.R;
import com.example.chainplus.databinding.FragmentConnectSettingBinding;
import com.example.chainplus.databinding.FragmentMineBinding;
import com.example.chainplus.viewModel.DataViewModel;

import androidx.appcompat.view.menu.ListMenuItemView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;


public class MineFragment extends Fragment {
    private ListView listView;
    private ArrayList<ListItem> list;

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

        listView = binding.settingMenu;
        list = new ArrayList<>();

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
        MyListViewAdapter adapter = new MyListViewAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, new ConnectSettingFragment()).addToBackStack(null).commit();
                } else if (position == 1) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, new LinkageSettingFragment()).addToBackStack(null).commit();
                }
            }
        });

//        // 其他菜单
//        listView = binding.otherMenu;
//        list = new ArrayList<>();
//
//        // 检查更新
//        item = new ListItem();
//        item.setImage(getActivity().getDrawable(R.drawable.ic_download));
//        item.setTitle("检查更新");
//        list.add(item);
//        // 帮助和反馈
//        item = new ListItem();
//        item.setImage(getActivity().getDrawable(R.drawable.ic_edit));
//        item.setTitle("帮助和反馈");
//        list.add(item);
//        // 关于
//        item = new ListItem();
//        item.setImage(getActivity().getDrawable(R.drawable.ic_info));
//        item.setTitle("关于");
//        list.add(item);
//        // 返回adapter
//        adapter = new MyListViewAdapter();
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0){
//                    // todo: 检查更新
//                } else if (position == 1) {
//                    // todo: 帮助和反馈
//                } else if (position == 2) {
//                    // todo: 关于
//                }
//            }
//        });

        return binding.getRoot();
    }

    class MyListViewAdapter extends BaseAdapter {
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
                listItemView.imageView = (ImageView) convertView.findViewById(R.id.image);
                listItemView.textView = (TextView) convertView.findViewById(R.id.title);

                // 将ListItemView对象传递给convertView
                convertView.setTag(listItemView);
            } else {
                // 从converView中获取ListItemView对象
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


    /**
     * 封装两个视图组件的类
     */
    class ListItemView {
        ImageView imageView;
        TextView textView;
    }

    /**
     * 封装了两个资源的类
     */
    class ListItem {
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
}