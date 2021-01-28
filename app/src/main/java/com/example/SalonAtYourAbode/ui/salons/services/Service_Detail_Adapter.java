package com.example.SalonAtYourAbode.ui.salons.services;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.example.SalonAtYourAbode.R;
import com.example.SalonAtYourAbode.ui.RetrofitClient;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Service_Detail_Adapter extends Adapter<Service_Detail_Adapter.ViewHolder> {

    LayoutInflater inflater;
    static List<Service_Detail_Getter_Setter> serviceDetailgettersetters;
    static Context context;
    static RecyclerView recyclerViewCart;
    String id_service_provider, service_status, service_info;
    static ArrayList<Service_Detail_Getter_Setter> cart_detail_getter_setter;

    public static int sum = 0;

    public Service_Detail_Adapter(Context applicationContext, List<Service_Detail_Getter_Setter> serviceDetailGetterSetters, RecyclerView recyclerView) {
        this.inflater = LayoutInflater.from(applicationContext);
        this.serviceDetailgettersetters = serviceDetailGetterSetters;
        context = applicationContext;
        recyclerViewCart = recyclerView;
        cart_detail_getter_setter = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_service_detail_list_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        id_service_provider = serviceDetailgettersetters.get(position).getId();
        service_status = serviceDetailgettersetters.get(position).getService_status();
        service_info = serviceDetailgettersetters.get(position).getInformation();
        System.out.println(service_status + "1");
        if (service_status.equals("active") || service_status.equals("Active")) {
            holder.information.setText(serviceDetailgettersetters.get(position).getInformation());
            holder.serviceName.setText(serviceDetailgettersetters.get(position).getService_name());
            if (serviceDetailgettersetters.get(position).getPrice() != null) {
                holder.servicePrice.setText(String.valueOf(serviceDetailgettersetters.get(position).getPrice()));
            } else {
                holder.servicePrice.setText(0);
            }
            Picasso.get().load(serviceDetailgettersetters.get(position).getImage()).into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        Log.i("TAG", "ViewHolder: hi  " + serviceDetailgettersetters.size());
        return serviceDetailgettersetters.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView serviceName, servicePrice, information;
        ImageView image;
        CardView cardServiceDetail;
        Button addCart;
        View cardView1;
        Animation cardAnimation;
        public ViewHolder(View view) {
            super(view);
            System.out.println(123);
            System.out.println();
            serviceName = view.findViewById(R.id.serviceName);
            servicePrice = view.findViewById(R.id.servicePrice);
            information = view.findViewById(R.id.serviceInforamtion);
            image = view.findViewById(R.id.coverImage1);
            addCart = view.findViewById(R.id.serviceAddToCartBtn);
            cardServiceDetail = view.findViewById(R.id.serviceDetailCard);
            cardView1 = Service_Detail_Activity.cardView;
            cardAnimation = AnimationUtils.loadAnimation(context,R.anim.sequential);
            addCart.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            System.out.println("add to cart");
            cardView1.setVisibility(View.VISIBLE);
            cardView1.startAnimation(cardAnimation);
            int position = getAdapterPosition();
            int userId = com.example.SalonAtYourAbode.ui.login.LoginFragment.User_Id;
            int serviceProvId = Service_Detail_Activity.id_service_provider1;
            String serviceId;

            int slash_count =5;
            int digit_count=0;
            for(int j=0;j<serviceDetailgettersetters.get(position).getId().length();j++){
                if(serviceDetailgettersetters.get(position).getId().charAt(j) == '/'){
                    slash_count=slash_count-1;
                }
                if(slash_count == 0 && serviceDetailgettersetters.get(position).getId().charAt(j) != '/'){
                    digit_count = digit_count + Integer.parseInt(String.valueOf(serviceDetailgettersetters.get(position).getId().charAt(j)));
                    digit_count =digit_count *10;
                }
            }
            digit_count = digit_count /10;

            if(cart_detail_getter_setter.size() != 0){
                System.out.println(serviceDetailgettersetters.get(position).getId() +"   -----   "+ cart_detail_getter_setter.get(0).getId());
                if(digit_count == Integer.parseInt(cart_detail_getter_setter.get(0).getId())){
                    boolean flag=true;

                    for(int i=0;i<cart_detail_getter_setter.size();i++){
                        System.out.println(serviceDetailgettersetters.get(position).getServiceId() +"  ********  "+ cart_detail_getter_setter.get(i).getServiceId());
                        if(serviceDetailgettersetters.get(position).getServiceId().equals(cart_detail_getter_setter.get(i).getServiceId())){
                            flag = false;
                            break;
                        }
                    }
                    if(flag){
                        System.out.println(position);
                        cart_detail_getter_setter.add(serviceDetailgettersetters.get(position));
                        com.example.SalonAtYourAbode.ui.salons.services.Cart_List_Adapter cart_list_adapter =
                                new com.example.SalonAtYourAbode.ui.salons.services.Cart_List_Adapter(context, cart_detail_getter_setter);
                        recyclerViewCart.setLayoutManager(new LinearLayoutManager(context));
                        recyclerViewCart.setAdapter(cart_list_adapter);
                        serviceId = serviceDetailgettersetters.get(position).getServiceId();

						Call<ResponseBody> call1 = RetrofitClient.
                                getInstance().
                                getApi().
                                addToCart(userId, serviceProvId, serviceId);
                        call1.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                                String s = null;
                                try {
                                    if (response.isSuccessful()) {
                                        assert response.body() != null;
                                        s = response.body().string();
                                        System.out.println(s);
                                    } else {
                                        Toast.makeText(context, "no response", Toast.LENGTH_LONG).show();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (s != null) {
                                    try {
                                        JSONObject jsonObject1 = new JSONObject(s);
                                        JSONArray jsonArray = jsonObject1.getJSONArray("data");
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        System.out.println(jsonObject.getString("service_id__service_id") + " " + jsonObject.getString("service_id__name_of_service"));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else {
                        Toast.makeText(context, "already added service", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(context, "Service cant be added", Toast.LENGTH_SHORT).show();
                }
            }else{

                System.out.println(position);

                Service_Detail_Getter_Setter servicedetailgettersetter = new Service_Detail_Getter_Setter();

                servicedetailgettersetter.setId(String.valueOf(digit_count));
                servicedetailgettersetter.setServiceId("https://sayatest.pythonanywhere.com/resapi/service/".concat(serviceDetailgettersetters.get(position).getServiceId()).concat("/"));
                servicedetailgettersetter.setService_name(serviceDetailgettersetters.get(position).getService_name());
                servicedetailgettersetter.setService_status(serviceDetailgettersetters.get(position).getService_status());
                servicedetailgettersetter.setPrice(serviceDetailgettersetters.get(position).getPrice());
                servicedetailgettersetter.setInformation(serviceDetailgettersetters.get(position).getInformation());
                servicedetailgettersetter.setImage(serviceDetailgettersetters.get(position).getImage());

                cart_detail_getter_setter.add(servicedetailgettersetter);

                com.example.SalonAtYourAbode.ui.salons.services.Cart_List_Adapter cart_list_adapter =
                        new com.example.SalonAtYourAbode.ui.salons.services.Cart_List_Adapter(context, cart_detail_getter_setter);
                recyclerViewCart.setLayoutManager(new LinearLayoutManager(context));
                recyclerViewCart.setAdapter(cart_list_adapter);
                serviceId = serviceDetailgettersetters.get(position).getServiceId();
				
				Call<ResponseBody> call1 = RetrofitClient.
                                getInstance().
                                getApi().
                                addToCart(userId, serviceProvId, serviceId);
                        call1.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                                String s = null;
                                try {
                                    if (response.isSuccessful()) {
                                        assert response.body() != null;
                                        s = response.body().string();
                                        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                                        System.out.println(s);
                                    } else {
                                        Toast.makeText(context, "no response", Toast.LENGTH_LONG).show();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (s != null) {
                                    try {
                                        JSONObject jsonObject1 = new JSONObject(s);
                                        JSONArray jsonArray = jsonObject1.getJSONArray("data");
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        System.out.println(jsonObject.getString("service_id__service_id") + " " + jsonObject.getString("service_id__name_of_service"));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }




            Service_Detail_Activity.count.setText("Total item = " + cart_detail_getter_setter.size());

            sum = 0;
            for(int i = 0; i < cart_detail_getter_setter.size(); i++){
                sum = sum + Integer.parseInt(cart_detail_getter_setter.get(i).getPrice());
            }

            Service_Detail_Activity.total.setText("Total = " + sum);
        }
    }
}
