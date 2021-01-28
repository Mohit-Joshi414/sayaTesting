package com.example.SalonAtYourAbode.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.SalonAtYourAbode.R;
import com.bumptech.glide.Glide;

public class ServicesAdapter extends BaseAdapter {

    private final HomeFragment context;
    private final String[] serviceName;
    private final Integer[] serviceImage;

    public ServicesAdapter(HomeFragment context, String[] serviceName, Integer[] serviceImage){
        super();

        this.context = context;
        this.serviceName = serviceName;
        this.serviceImage = serviceImage;
    }

    private class ViewHolder {
        TextView sName;
        ImageView sImage;
    }

        @Override
    public int getCount() {
        return serviceName.length;
    }

    @Override
    public Object getItem(int i) {
        return serviceName[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        LayoutInflater inflater = context.getLayoutInflater();

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home_services_list, null);
            holder = new ViewHolder();

            holder.sName = view.findViewById(R.id.home_service_name);
            holder.sImage = view.findViewById(R.id.home_service_image);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.sName.setText(serviceName[i]);
        Glide.with(context).load(serviceImage[i])
                .into(holder.sImage);

        return view;
    }
}
