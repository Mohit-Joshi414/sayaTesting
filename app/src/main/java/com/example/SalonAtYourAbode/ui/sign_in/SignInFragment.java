package com.example.SalonAtYourAbode.ui.sign_in;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.SalonAtYourAbode.MainActivity;
import com.example.SalonAtYourAbode.R;
import com.example.SalonAtYourAbode.ui.RetrofitClient;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInFragment extends Fragment {

    EditText usernameView, emailView, mobileView, addressView, passwordView, re_password;
    TextView responseTextRegister;
    Spinner genderView, districtView;

    String msg;
    Boolean signUp;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_sign_in, container, false);
        ImageView banner = root.findViewById(R.id.sign_in_banner);
        TextView signinRedirect = root.findViewById(R.id.login_here);

        usernameView = root.findViewById(R.id.name);
        emailView = root.findViewById(R.id.email);
        mobileView = root.findViewById(R.id.mobile_no);
        addressView = root.findViewById(R.id.address);
        genderView = root.findViewById(R.id.gender);
        districtView = root.findViewById(R.id.district);
        passwordView = root.findViewById(R.id.password);
        re_password = root.findViewById(R.id.confirm_password);
        responseTextRegister = root.findViewById(R.id.error);

        Button submit = root.findViewById(R.id.sign_in_btn);

        Glide.with(getContext()).load(R.drawable.banner)
                .centerCrop()
                .into(banner);

        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                register();
            }
        });

        signinRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.navController.navigate(R.id.nav_login);
            }
        });

        return root;
    }
	
	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void register() {
        String user_name = usernameView.getText().toString().trim();
        String email = emailView.getText().toString().trim();
        String mobile = mobileView.getText().toString().trim();
        String address = addressView.getText().toString().trim();
        String gender = genderView.getSelectedItem().toString().trim();
        String district = districtView.getSelectedItem().toString().trim();
        String password = passwordView.getText().toString().trim();
        String rePassword = re_password.getText().toString().trim();

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (mobile.length() == 0 || email.length() == 0 || user_name.length() == 0 || address.length() ==0 ||
                gender.length() == 0 ||
                district.length() == 0 || password.length() == 0 || rePassword.length()==0) {
            Toast.makeText(getContext(), "All Input Fields are Compulsory", Toast.LENGTH_LONG).show();
            responseTextRegister.setText("All Input Fields are Compulsory.");
        }else if(password.length()<=7 || rePassword.length()<=7){
            responseTextRegister.setText("Password must be minimum of 8 character.");
        }
        else if (!password.equals(rePassword)){
            responseTextRegister.setText("Both Password field must be same.");
        }
        else if (mobile.length()!=10){
            responseTextRegister.setText("Mobile number is invalid.");
        }
        else if (!email.matches(emailPattern)){
            responseTextRegister.setText("email is invalid.");
        }
        else {
            Call<ResponseBody> call1 = RetrofitClient.
                    getInstance().
                    getApi().
                    signUp(user_name,email,mobile,address,gender,district,password,rePassword);
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
                            Toast.makeText(getContext(), "No Response", Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if(s!=null){
                        try {
                            JSONObject jsonObject= new JSONObject(s);
                            msg = jsonObject.getString("msg");
                            signUp = jsonObject.getBoolean("signup");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if(signUp){
                        responseTextRegister.setText("Registration completed successfully.");
                        MainActivity.navController.navigate(R.id.nav_login);
                    }
                    else {
                        responseTextRegister.setText(msg);
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}