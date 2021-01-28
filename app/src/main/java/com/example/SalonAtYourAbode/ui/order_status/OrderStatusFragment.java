package com.example.SalonAtYourAbode.ui.order_status;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.SalonAtYourAbode.R;
import com.example.SalonAtYourAbode.ui.RetrofitClient;
import com.example.SalonAtYourAbode.ui.login.LoginFragment;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderStatusFragment extends Fragment {

    int UserId = LoginFragment.User_Id;
    static List<Order_Detail_Getter_Setter> orderDetailSetterSetters;
    RecyclerView recyclerView;
    Order_Detail_Adapter orderDetailAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_order_status, container, false);

        recyclerView = root.findViewById(R.id.orderStatusRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        orderDetailSetterSetters = new ArrayList<>();
        orderDetail();

        return root;
    }

    public void orderDetail(){
        Call<ResponseBody> call1 = RetrofitClient.
                getInstance().
                getApi().
                orderList(UserId);
        call1.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                String s = null;
                JSONObject jsonObject,jsonObject1;
                try {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        s = response.body().string();
                        System.out.println(s);
                    } else {
                        Toast.makeText(getContext(), "No Response", Toast.LENGTH_LONG).show();
                    }
                    if(s!=null){
                        jsonObject = new JSONObject(s);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for(int i=0; i<jsonArray.length();i++){
                            jsonObject1 = jsonArray.getJSONObject(i);
                            Order_Detail_Getter_Setter orderDetailSetterSetter = new Order_Detail_Getter_Setter();
                            orderDetailSetterSetter.setPaymentStatus(jsonObject1.getString("payment_status"));
                            orderDetailSetterSetter.setSalonName(jsonObject1.getString("service_provider_id__shopname"));
                            orderDetailSetterSetter.setImageUrl(jsonObject1.getString("service_provider_id__image"));
                            orderDetailSetterSetter.setTxnId(jsonObject1.getString("txnid"));
                            orderDetailSetterSetter.setMasterName(jsonObject1.getString("service_provider_id__name"));
                            orderDetailSetterSetter.setSalonContact(jsonObject1.getString("service_provider_id__mobile"));
                            orderDetailSetterSetter.setPayNow(jsonObject1.getString("amount_sah"));
                            orderDetailSetterSetter.setPayLater(jsonObject1.getString("amount_service"));
                            orderDetailSetterSetter.setOrderStatus(jsonObject1.getString("order_status"));
                            orderDetailSetterSetter.setOrderData(jsonObject1.getString("order_data"));
                            orderDetailSetterSetters.add(orderDetailSetterSetter);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                orderDetailAdapter = new Order_Detail_Adapter(getContext(), orderDetailSetterSetters);
                recyclerView.setAdapter(orderDetailAdapter);

            }
            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}