package com.example.SalonAtYourAbode.ui.salons.services;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.SalonAtYourAbode.R;
import com.example.SalonAtYourAbode.ui.RetrofitClient;
import com.example.SalonAtYourAbode.ui.salons.services.Service_Detail_Getter_Setter;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.SalonAtYourAbode.ui.login.LoginFragment.User_Id;
import static com.example.SalonAtYourAbode.ui.salons.services.Service_Detail_Adapter.cart_detail_getter_setter;
import static com.example.SalonAtYourAbode.ui.salons.services.Service_Detail_Adapter.recyclerViewCart;

public class Cart_List_Adapter extends RecyclerView.Adapter<Cart_List_Adapter.ViewHolder> {
    LayoutInflater inflater;
    List<Service_Detail_Getter_Setter> cartDetailgettersetters;
    static Context context;
    String id_service_provider, service_status, service_info,serviceId;

    public Cart_List_Adapter(Context applicationContext, List<Service_Detail_Getter_Setter> cartDetailGetterSetters) {
        this.inflater = LayoutInflater.from(applicationContext);
        this.cartDetailgettersetters = cartDetailGetterSetters;
        context = applicationContext;
    }

    @NonNull
    @Override
    public Cart_List_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_service_detail_list_card, parent, false);
        return new Cart_List_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        id_service_provider = cartDetailgettersetters.get(position).getId();
        service_status = cartDetailgettersetters.get(position).getService_status();
        serviceId = cartDetailgettersetters.get(position).getServiceId();
        System.out.println("serviceid" + serviceId);
        service_info = cartDetailgettersetters.get(position).getInformation();
        holder.information.setText(cartDetailgettersetters.get(position).getInformation());
        holder.serviceName.setText(cartDetailgettersetters.get(position).getService_name());
        if (cartDetailgettersetters.get(position).getPrice() != null) {
            holder.servicePrice.setText(String.valueOf(cartDetailgettersetters.get(position).getPrice()));
        } else {
            holder.servicePrice.setText(0);
        }
        Picasso.get().load(cartDetailgettersetters.get(position).getImage()).into(holder.image);
    }


    @Override
    public int getItemCount() {
        Log.i("TAG", "ViewHolder: hi  " + cartDetailgettersetters.size());
        return cartDetailgettersetters.size();
    }

public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView serviceName, servicePrice, information;
    ImageView image;
    Button removeCart;

    public ViewHolder(View view) {
        super(view);
        System.out.println(123);
        System.out.println();
        serviceName = view.findViewById(R.id.serviceName);
        servicePrice = view.findViewById(R.id.servicePrice);
        information = view.findViewById(R.id.serviceInforamtion);
        image = view.findViewById(R.id.coverImage1);
        removeCart = view.findViewById(R.id.serviceAddToCartBtn);
        removeCart.setText("Remove");

        removeCart.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        System.out.println("remove from cart");
        int position = getAdapterPosition();
        int userId=User_Id;

        String serviceId=cart_detail_getter_setter.get(position).getServiceId();
        System.out.println(position + "______________________" + userId + "________________________" + serviceId);


        Call<ResponseBody> call1 = RetrofitClient.
                getInstance().
                getApi().
                removeFromCart(userId, serviceId);
        call1.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                String s=null;
                try {
                    if(response.isSuccessful()){
                        assert response.body() != null;
                        s = response.body().string();
                        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                        System.out.println(s);
                        cart_detail_getter_setter.remove(position);
                        Cart_List_Adapter cart_list_adapter = new Cart_List_Adapter(context, cart_detail_getter_setter);
                        recyclerViewCart.setLayoutManager(new LinearLayoutManager(context));
                        recyclerViewCart.setAdapter(cart_list_adapter);
                    }
                    else {
                        Toast.makeText(context, "no response", Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
}
