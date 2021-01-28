package com.example.SalonAtYourAbode.ui.salons.services;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.SalonAtYourAbode.R;
import com.example.SalonAtYourAbode.ui.RetrofitClient;
import com.example.SalonAtYourAbode.ui.login.LoginFragment;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Service_Detail_Activity extends AppCompatActivity {

    private ShimmerFrameLayout mShimmerViewContainer;
    RecyclerView recyclerView, recyclerViewCart;
    SwipeRefreshLayout refreshLayout;

    List<Service_Detail_Getter_Setter> serviceDetailGetterSetters;
    private static String JSON_URL = "https://sayatest.pythonanywhere.com/resapi/service/?format=json";
    int userId = LoginFragment.User_Id;
    OkHttpClient client = new OkHttpClient();
    Service_Detail_Adapter adapter;
    Button ServiceAddToCartBtn;
    public static int id_service_provider1;
    static View cardView;
    public static TextView total, count;
    int slash_count, digit_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);
        Intent intent = getIntent();
        cardView = findViewById(R.id.cardView);
        id_service_provider1 = intent.getIntExtra("Id_Service_provider", 0);
        String title = intent.getStringExtra("Name_Service_provider");
        System.out.println(title);

        Toolbar toolbar = (Toolbar) findViewById(R.id.service_toolbar);
        total = findViewById(R.id.total);
        count = findViewById(R.id.item_count);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_vector_test);
        getSupportActionBar().setTitle(title);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        mShimmerViewContainer = findViewById(R.id.service_shimmer_view_container);
//        mShimmerViewContainer.startShimmerAnimation();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Toast.makeText(this, "Id_Service_provider" + id_service_provider1, Toast.LENGTH_SHORT).show();
        ServiceAddToCartBtn = findViewById(R.id.serviceAddToCartBtn);
        refreshLayout = findViewById(R.id.ServiceswipeRefresh);
        recyclerView = findViewById(R.id.serviceList);
        recyclerViewCart = findViewById(R.id.cartDetail);
        serviceDetailGetterSetters = new ArrayList<>();
        extractSalonDetails();

        refreshLayout.setColorSchemeResources(R.color.colorAccent);

        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.

                        extractSalonDetails();
                    }
                }
        );
    }

    private void extractSalonDetails() {
        final JSONArray[] jsonExtraArray = new JSONArray[1];
        Request request = new Request.Builder()
                .url(JSON_URL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    final String myResponse = response.body().string();
                    System.out.println(myResponse);
                    Service_Detail_Activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray jsonArray = new JSONArray(myResponse);
                                jsonExtraArray[0] = jsonArray;
                                serviceDetailGetterSetters.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Service_Detail_Getter_Setter servicedetailgettersetter = new Service_Detail_Getter_Setter();
                                    slash_count = 5;
                                    digit_count = 0;
                                    for (int j = 0; j < jsonObject.getString("service_provider_id").length(); j++) {
                                        if (jsonObject.getString("service_provider_id").charAt(j) == '/') {
                                            slash_count = slash_count - 1;
                                        }
                                        if (slash_count == 0 && jsonObject.getString("service_provider_id").charAt(j) != '/') {
                                            digit_count = digit_count + Integer.parseInt(String.valueOf(jsonObject.getString("service_provider_id").charAt(j)));
                                            digit_count = digit_count * 10;
                                        }
                                    }
                                    digit_count = digit_count / 10;
                                    if (digit_count == id_service_provider1 && (jsonObject.getString("service_status").equals("active") || jsonObject.getString("service_status").equals("Active"))) {
                                        servicedetailgettersetter.setServiceId(jsonObject.getString("service_id"));
                                        servicedetailgettersetter.setId(jsonObject.getString("service_provider_id"));
                                        servicedetailgettersetter.setService_name(jsonObject.getString("name_of_service"));
                                        servicedetailgettersetter.setService_status(jsonObject.getString("service_status"));
                                        servicedetailgettersetter.setPrice(jsonObject.getString("price"));
                                        servicedetailgettersetter.setInformation(jsonObject.getString("information"));
                                        servicedetailgettersetter.setImage(jsonObject.getString("image"));

                                        serviceDetailGetterSetters.add(servicedetailgettersetter);
                                    }
                                    System.out.println(11);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            adapter = new Service_Detail_Adapter(getApplicationContext(), serviceDetailGetterSetters, recyclerViewCart);


                            if (adapter.getItemCount() == 0) {
                                recyclerView.setVisibility(View.GONE);
                                TextView emptyView = findViewById(R.id.emptyElement);
                                emptyView.setVisibility(View.VISIBLE);
                            }
                            recyclerView.setAdapter(adapter);
                            mShimmerViewContainer.stopShimmerAnimation();
                            mShimmerViewContainer.setVisibility(View.GONE);

                            refreshLayout.setRefreshing(false);
                        }
                    });
                }
            }
        });

        retrofit2.Call<ResponseBody> call1 = RetrofitClient.
                getInstance().
                getApi().
                cartDetail(userId);
        call1.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, @NotNull retrofit2.Response<ResponseBody> response) {
                String s = null;
                JSONObject jsonObject, jsonObject1;
                try {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        s = response.body().string();
                        System.out.println(s);
                    } else {
                        Toast.makeText(Service_Detail_Activity.this, "No Response", Toast.LENGTH_LONG).show();
                    }
                    if (s != null) {
                        jsonObject = new JSONObject(s);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        Service_Detail_Adapter.cart_detail_getter_setter.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject1 = jsonArray.getJSONObject(i);
                            Service_Detail_Getter_Setter servicedetailgettersetter = new Service_Detail_Getter_Setter();

                            System.out.println(jsonObject1.getInt("user_id") + " _____________________");
                            if (LoginFragment.User_Id == jsonObject1.getInt("user_id")) {
                                servicedetailgettersetter.setId(jsonObject1.getString("service_provider_id"));

//                                for (int j = 0; j < jsonExtraArray[0].length(); j++) {
//                                    jsonObject1 = jsonExtraArray[0].getJSONObject(j);
//
//                                    slash_count = 5;
//                                    digit_count = 0;
//                                    for (int k = 0; k < jsonObject.getString("service_id").length(); k++) {
//                                        if (jsonObject.getString("service_id").charAt(k) == '/') {
//                                            slash_count = slash_count - 1;
//                                        }
//                                        if (slash_count == 0 && jsonObject.getString("service_id").charAt(k) != '/') {
//                                            digit_count = digit_count + Integer.parseInt(String.valueOf(jsonObject.getString("service_id").charAt(k)));
//                                            digit_count = digit_count * 10;
//                                        }
//                                    }
//                                    digit_count = digit_count / 10;

//                                    if (Integer.parseInt(jsonObject1.getString("service_id")) == digit_count) {
                                        servicedetailgettersetter.setServiceId(jsonObject1.getString("service_id"));
                                        servicedetailgettersetter.setService_name(jsonObject1.getString("service_id__name_of_service"));
                                        servicedetailgettersetter.setService_status(jsonObject1.getString("service_id__service_status"));
                                        servicedetailgettersetter.setPrice(jsonObject1.getString("service_id__price"));
                                        servicedetailgettersetter.setInformation(jsonObject1.getString("service_id__information"));
                                        servicedetailgettersetter.setImage(jsonObject1.getString("service_id__image"));
//                                    }


//}
                                Service_Detail_Adapter.cart_detail_getter_setter.add(servicedetailgettersetter);
                            }
                            System.out.println(11);
                        }


                        Cart_List_Adapter cart_list_adapter = new Cart_List_Adapter(getApplicationContext(), Service_Detail_Adapter.cart_detail_getter_setter);

                        if (Service_Detail_Adapter.cart_detail_getter_setter.size() != 0) {
                            recyclerViewCart.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerViewCart.setAdapter(cart_list_adapter);
                        }
                    }

                    }catch(Exception e){
                        e.printStackTrace();
                    }

                }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Service_Detail_Activity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }


        });
        Button place_order = findViewById(R.id.placeOrder);
        place_order.setOnClickListener(view -> {
            if (Service_Detail_Adapter.cart_detail_getter_setter.size() == 0) {
                Toast.makeText(Service_Detail_Activity.this, "Please Add Some Service In Cart For Placing Order.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Service_Detail_Activity.this, "place order", Toast.LENGTH_SHORT).show();
                Intent intent =
                        new Intent(Service_Detail_Activity.this, com.example.SalonAtYourAbode.ui.salons.services.payu.StartPaymentActivity.class);

                startActivity(intent);
            }
        });



    }
    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    protected void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }


}