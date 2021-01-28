package com.example.SalonAtYourAbode.ui.salons;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.SalonAtYourAbode.MainActivity;
import com.example.SalonAtYourAbode.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.SalonAtYourAbode.ui.login.LoginFragment;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.JsonAdapter;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.SalonAtYourAbode.MainActivity.userMail;

public class SalonFragment extends Fragment {

    private ShimmerFrameLayout mShimmerViewContainer;
    RecyclerView recyclerView;
    List<Salon_list_getter_setter> salonList;
    Salon_List_Adapter adapter;
    int userId = LoginFragment.User_Id;
    SwipeRefreshLayout refreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_salon, container, false);

        recyclerView = root.findViewById(R.id.salonList);
        mShimmerViewContainer = root.findViewById(R.id.salon_shimmer_view_container);
        mShimmerViewContainer.startShimmerAnimation();
        salonList = new ArrayList<>();
        extractSalons();

        refreshLayout = root.findViewById(R.id.swipeRefresh);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);

        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.

                        extractSalons();
                    }
                }
        );

        return root;
    }

    private void extractSalons() {
        Call<ResponseBody> call1 = com.example.SalonAtYourAbode.ui.RetrofitClient.
                getInstance().
                getApi().
                salonList("All",userId);
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(s!=null){
                    try {
                        jsonObject = new JSONObject(s);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for(int i=0; i<jsonArray.length();i++){
                            jsonObject1 = jsonArray.getJSONObject(i);
                            Salon_list_getter_setter salonListgettersetter = new Salon_list_getter_setter();
                            salonListgettersetter.setId(jsonObject1.getInt("service_provider_id"));
                            salonListgettersetter.setname(jsonObject1.getString("name"));
                            salonListgettersetter.setEmail(jsonObject1.getString("email"));
                            salonListgettersetter.setImage(jsonObject1.getString("image"));

                            salonList.add(salonListgettersetter);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new Salon_List_Adapter(getContext(), salonList);

                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);

                    recyclerView.setAdapter(adapter);
                    refreshLayout.setRefreshing(false);
            }

        }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
            });
    }
}