package com.example.SalonAtYourAbode.ui.login.forget_password;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

public class ForgetPassword extends AppCompatActivity {

    EditText emailView, passwordViewNew, passwordViewConform, OTP;
    String email, password, password2,Otp;
    Button changePass,forgetPass;
    Boolean otp;
    String otp1;
    TextView responseTextVE,responseTextFP;
    LinearLayout linearLayout, linearLayout1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        linearLayout = findViewById(R.id.linearLayout);
        linearLayout1 = findViewById(R.id.linearLayout1);
        linearLayout1.setVisibility(View.GONE);

        changePass = findViewById(R.id.fp_btn);
        responseTextVE = findViewById(R.id.responseTextLogin);
        emailView = findViewById(R.id.fp_email);

        Toolbar toolbar = (Toolbar) findViewById(R.id.service_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_vector_test);
        getSupportActionBar().setTitle("Forget Password");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        changePass.setOnClickListener(view -> fp_email());
    }
    public void fp_email(){
        email = emailView.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

//        System.out.println(email+" " + password +" " + password2);
        if (email.length() == 0) {
            responseTextVE.setText("Please enter data in all fields");
            Toast.makeText(getApplicationContext(), "Something is wrong. Please check your inputs...", Toast.LENGTH_LONG).show();
        }
        else if (!email.matches(emailPattern)) {
            responseTextVE.setText("email is incorrect.");
        }
        else{
            Call<ResponseBody> call1 = RetrofitClient.
                    getInstance().
                    getApi().
                    emailVerifyFP(email);
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
                            Toast.makeText(ForgetPassword.this, "No Response", Toast.LENGTH_LONG).show();
                        }
                        if(s!=null){
                            jsonObject = new JSONObject(s);
                            otp= jsonObject.getBoolean("otp");
                        }
                        if(otp){
                            linearLayout.setVisibility(View.GONE);
                            linearLayout1.setVisibility(View.VISIBLE);

                            passwordViewNew = findViewById(R.id.fp_newPass);
                            passwordViewConform = findViewById(R.id.fp_confirmPass);
                            responseTextFP = findViewById(R.id.responseTexFP);
                            OTP = findViewById(R.id.fp_otp);
                            forgetPass = findViewById(R.id.fp_btn1);
                            forgetPass.setOnClickListener(view -> fp_Otp_Verify());

                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                    Toast.makeText(ForgetPassword.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void fp_Otp_Verify(){
        password = passwordViewNew.getText().toString().trim();
        password2 = passwordViewConform.getText().toString().trim();
        Otp = OTP.getText().toString().trim();
        System.out.println(password+"  "+ password2 +"  "+ Otp);

        if (!password.equals(password2)) {
            responseTextFP.setText("Please enter both the password fields same for conformination...");
            Toast.makeText(getApplicationContext(), "both password fields mismatch...", Toast.LENGTH_LONG).show();
        }
        else if(password.length() == 0 || Otp.length() == 0){
            responseTextFP.setText("All Fields are compulsory.");
        }
        else if(password.length() <= 7){

            responseTextFP.setText("Password is too short.");
        }
        else{
            Call<ResponseBody> call2 = RetrofitClient.
                    getInstance().
                    getApi().
                    otpVerifyFP(Otp,password,email);
            call2.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    String s = null;
                    System.out.println(email);
                    JSONObject jsonObject;
                    try {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            s = response.body().string();
                            System.out.println(s);
                            responseTextFP.setText("All done.");

                        } else {
                            Toast.makeText(ForgetPassword.this, "No Response", Toast.LENGTH_LONG).show();
                        }
                        if(s!=null){
                            jsonObject = new JSONObject(s);
                            otp1= jsonObject.getString("otp");
                        }
                        if(otp1.equals("valid")){
                            finish();
                        }
                        else {
                            responseTextFP.setText("OTP Invalid.");
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                    Toast.makeText(ForgetPassword.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

}