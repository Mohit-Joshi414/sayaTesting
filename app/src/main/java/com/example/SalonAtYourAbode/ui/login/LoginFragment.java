package com.example.SalonAtYourAbode.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.SalonAtYourAbode.MainActivity;
import com.example.SalonAtYourAbode.R;

import okhttp3.ResponseBody;

import static com.example.SalonAtYourAbode.MainActivity.userMail;

public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;
	
	public static final String PREFERENCES = "Preferences" ;
    public static final String Email = "emailKey";
    public static final String Password = "PassKey";
	public static String FullName, txn_mail, mobile_no;
    public static int User_Id;
    SharedPreferences preferences;
    EditText emailView ,passwordView;
    TextView responseTextLogin;
    String email,password;
    public static boolean loginTF;
	com.example.SalonAtYourAbode.ui.LoadingDialog loadingDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        loginViewModel =
                ViewModelProviders.of(this).get(LoginViewModel.class);
				
		loadingDialog = new com.example.SalonAtYourAbode.ui.LoadingDialog(getActivity());
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        ImageView banner = root.findViewById(R.id.login_banner);
		
		emailView = root.findViewById(R.id.id);
        passwordView = root.findViewById(R.id.login_password);
        responseTextLogin = root.findViewById(R.id.error);
		Button submit = root.findViewById(R.id.login_btn);
		TextView forgetPassword = root.findViewById(R.id.forget_password);
		TextView loginRedirect = root.findViewById(R.id.sign_in_here);

        Glide.with(getContext()).load(R.drawable.banner)
                .centerCrop()
                .into(banner);
				
		preferences = getActivity().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

        if(preferences.contains(Email) && preferences.contains(Password)){
            User_Id = preferences.getInt("UserId",0);
            FullName = preferences.getString("FullName", "Guest");
            mobile_no = preferences.getString("mobile", "0000000000");
            txn_mail = preferences.getString("txn_mail", "<none>");

            Menu nav_menu = MainActivity.navigationView.getMenu();
            nav_menu.findItem(R.id.nav_login).setVisible(false);
            nav_menu.findItem(R.id.nav_sign_in).setVisible(false);
            nav_menu.findItem(R.id.nav_logout).setVisible(true);
            MainActivity.navController.navigate(R.id.nav_home);
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), com.example.SalonAtYourAbode.ui.login.forget_password.ForgetPassword.class);
                startActivity(intent);
            }
        });

        loginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.navController.navigate(R.id.nav_sign_in);
            }
        });

        return root;
    }
	
	public void submit() {

        email = emailView.getText().toString().trim();
        password = passwordView.getText().toString().trim();

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (email.length() == 0 || password.length() == 0) {
            Toast.makeText(getContext(), "Both Fields Are Compulsory...", Toast.LENGTH_LONG).show();
        } else if (password.length() <= 7) {
            responseTextLogin.setText("Password Should Be Incorrect..");
        } else if (!email.matches(emailPattern)) {
            responseTextLogin.setText("email is incorrect.");
        } else {
            loadingDialog.startLoadingDialog();
			Call<ResponseBody> call1 = com.example.SalonAtYourAbode.ui.RetrofitClient.
                    getInstance().
                    getApi().
                    login(email, password);
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

                    if (s != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            loginTF = jsonObject.getBoolean("login");
                            User_Id = jsonObject.getInt("user_id");
							FullName= jsonObject.getString("name");
							txn_mail = jsonObject.getString("email");
                            mobile_no = jsonObject.getString("mobile");
                            System.out.println(loginTF+"   "+ User_Id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (loginTF) {
                        Toast.makeText(getContext(), "login successful", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(Email, email);
                        editor.putString(Password, password);
                        editor.putInt("UserId", User_Id);
						editor.putString("FullName", FullName);
						editor.putString("mobile", mobile_no);
                        editor.putString("txn_mail", txn_mail);
                        editor.putInt("check", 1);
                        editor.apply();

                        MainActivity.userName.setText(FullName);
                        userMail.setText(txn_mail);
					
                        Menu nav_menu = MainActivity.navigationView.getMenu();
                        nav_menu.findItem(R.id.nav_login).setVisible(false);
                        nav_menu.findItem(R.id.nav_sign_in).setVisible(false);
                        nav_menu.findItem(R.id.nav_logout).setVisible(true);
						MainActivity.navController.navigate(R.id.nav_home);
						
						loadingDialog.dismissDialog();

                    } else {
                        responseTextLogin.setText("Login Failed. Invalid username or password... Please Register Yourself");
						loadingDialog.dismissDialog();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                    loadingDialog.dismissDialog();
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}