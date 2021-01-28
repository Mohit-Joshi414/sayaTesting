package com.example.SalonAtYourAbode.ui.salons;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.SalonAtYourAbode.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Salon_List_Adapter extends RecyclerView.Adapter<Salon_List_Adapter.ViewHolder> {

    LayoutInflater inflater;
    List<Salon_list_getter_setter> salonList;
    Context context;
    ArrayList<Integer> id_service_provider= new ArrayList<Integer>();
//    int id_service_provider=0;

    public Salon_List_Adapter(Context ctx, List<Salon_list_getter_setter> salonList) {
        this.inflater = LayoutInflater.from(ctx);
        context=ctx;
        this.salonList = salonList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_salon_list_card_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // bind the data
        holder.salonUser_name.setText(salonList.get(position).getname());
        holder.salonEmail.setText(salonList.get(position).getEmail());
        id_service_provider.add(salonList.get(position).getId());
        Picasso.get().load(salonList.get(position).getImage()).into(holder.image);
        Log.i("TAG", "onBindViewHolder: "+id_service_provider);


    }

    @Override
    public int getItemCount() {
        return salonList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView salonUser_name,salonEmail;
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            salonUser_name= itemView.findViewById(R.id.salonName);
            salonEmail = itemView.findViewById(R.id.salonEmail);
            image = itemView.findViewById(R.id.coverImage);

            // handle onClick

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Toast.makeText(context, "position"+position, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, com.example.SalonAtYourAbode.ui.salons.services.Service_Detail_Activity.class);
            intent.putExtra("Id_Service_provider",id_service_provider.get(position));
            intent.putExtra("Name_Service_provider",salonList.get(position).getname());
            context.startActivity(intent);

        }
    }
}
