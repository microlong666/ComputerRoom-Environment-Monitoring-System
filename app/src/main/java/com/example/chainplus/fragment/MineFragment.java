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
import com.example.chainplus.util.Page;
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

        // ?????????????????????
        ListItem item = new ListItem();
        item.setImage(getActivity().getDrawable(R.drawable.ic_connect_setting));
        item.setTitle("????????????");
        list.add(item);
        // ????????????
        item = new ListItem();
        item.setImage(getActivity().getDrawable(R.drawable.ic_linkage_setting));
        item.setTitle("????????????");
        list.add(item);
        // ??????adapter
        MyListViewAdapter adapter = new MyListViewAdapter(list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0){
                data.getPosition().setValue(Page.CONNECT_SETTING);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.from_right, R.anim.out_left, R.anim.from_left, R.anim.out_right).replace(R.id.fragmentContainerView, new ConnectSettingFragment()).addToBackStack(null).commit();
            } else if (position == 1) {
                data.getPosition().setValue(Page.LINK_SETTING);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.from_right, R.anim.out_left, R.anim.from_left, R.anim.out_right).replace(R.id.fragmentContainerView, new LinkageSettingFragment()).addToBackStack(null).commit();
            }
        });

        // ????????????
        listView = binding.otherMenu;
        list = new ArrayList<>();

        // ????????????
        item = new ListItem();
        item.setImage(getActivity().getDrawable(R.drawable.ic_download));
        item.setTitle("????????????");
        list.add(item);
        // ???????????????
        item = new ListItem();
        item.setImage(getActivity().getDrawable(R.drawable.ic_edit));
        item.setTitle("???????????????");
        list.add(item);
        // ??????
        item = new ListItem();
        item.setImage(getActivity().getDrawable(R.drawable.ic_info));
        item.setTitle("??????");
        list.add(item);
        // ??????adapter
        adapter = new MyListViewAdapter(list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0){
                Toast.makeText(getContext(), "?????????????????????", Toast.LENGTH_SHORT).show();
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
     * ??????????????????????????????
     */
    static class ListItemView {
        ImageView imageView;
        TextView textView;
    }

    /**
     * ???????????????????????????
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
         * ??????item?????????
         */
        @Override
        public int getCount() {
            return list.size();
        }

        /**
         * ??????item?????????
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
         * ??????item?????????
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListItemView listItemView;

            // ?????????item view
            if (convertView == null) {
                // ??????LayoutInflater???xml????????????????????????????????????View???
                convertView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.menu_item_layout, null);

                // ????????????????????????ListItemView??????????????????????????????
                listItemView = new ListItemView();
                listItemView.imageView = convertView.findViewById(R.id.image);
                listItemView.textView = convertView.findViewById(R.id.title);

                // ???ListItemView???????????????convertView
                convertView.setTag(listItemView);
            } else {
                // ???convertView?????????ListItemView??????
                listItemView = (ListItemView) convertView.getTag();
            }

            // ?????????mList??????????????????????????????
            Drawable img = list.get(position).getImage();
            String title = list.get(position).getTitle();

            // ??????????????????ListItemView??????????????????
            listItemView.imageView.setImageDrawable(img);
            listItemView.textView.setText(title);

            // ??????convertView??????
            return convertView;
        }

    }
}