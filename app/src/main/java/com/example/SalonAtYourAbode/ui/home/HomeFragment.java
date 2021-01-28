package com.example.SalonAtYourAbode.ui.home;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.SalonAtYourAbode.R;
import com.bumptech.glide.Glide;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        ImageView banner = root.findViewById(R.id.home_banner);
        ImageView banner1 = root.findViewById(R.id.home_banner_1);
        ImageView banner2 = root.findViewById(R.id.home_banner_2);
        ListView serviceList = root.findViewById(R.id.services_list);

        String[] serviceName = {
                "Threading",
                "Hair Treatment",
                "Facial and Skin Care",
                "Bridal Makeup",
                "Pedicure",
                "Classic Haircut",
                "Beard and Moustache",
                "Beard Trimming",
                "Hair Treatment",
                "Haircut and Shampoo"
        };

        Integer[] serviceImage = {
                R.drawable.img_1,
                R.drawable.img_2,
                R.drawable.img_3,
                R.drawable.img_4,
                R.drawable.img_5,
                R.drawable.img_6,
                R.drawable.img_7,
                R.drawable.img_8,
                R.drawable.img_9,
                R.drawable.img_10
        };

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        Glide.with(getContext()).load(R.drawable.banner)
                .centerCrop()
                .into(banner);
				
		WindowManager wm = (WindowManager) getContext().getSystemService(getContext().WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;

        Glide.with(getContext()).load(R.drawable.banner1)
                .override((int)(width-40), (int)(width-52))
                .into(banner1);

        Glide.with(getContext()).load(R.drawable.banner)
                .centerCrop()
                .into(banner2);

        //ViewGroup
        serviceList.setAdapter(new ServicesAdapter(this, serviceName, serviceImage));

        return root;
    }
}