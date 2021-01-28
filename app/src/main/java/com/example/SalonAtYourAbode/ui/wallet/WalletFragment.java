package com.example.SalonAtYourAbode.ui.wallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.SalonAtYourAbode.MainActivity;
import com.example.SalonAtYourAbode.R;
import com.example.SalonAtYourAbode.ui.RetrofitClient;
import com.example.SalonAtYourAbode.ui.login.LoginFragment;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletFragment extends Fragment {

    Button spendNowSayaCash;
    TextView sayaCash;
    public static int walletCash;
    Boolean msg;
    int User_Id = LoginFragment.User_Id;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_wallet, container, false);

        spendNowSayaCash = root.findViewById(R.id.spendNowBTN);
        sayaCash = root.findViewById(R.id.sayaCashDisplay);
        WalletResponse();

        spendNowSayaCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.navController.navigate(R.id.nav_salon);
            }
        });

        return root;
    }

    private void WalletResponse(){
        Call<ResponseBody> call1 = RetrofitClient.
                getInstance().
                getApi().
                wallet(User_Id);
        call1.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                String s = null;
                JSONObject jsonObject;
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
                        walletCash = jsonObject.getInt("wallet_cash");
                        msg = jsonObject.getBoolean("msg");
                    }
                    if(msg){
                        sayaCash.setText(String.valueOf(walletCash));
                    }
                    else {
                        Toast.makeText(getContext(), "Your Wallet is Not Created Yet.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}