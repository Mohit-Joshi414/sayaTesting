package com.example.SalonAtYourAbode.ui.contact_us;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.SalonAtYourAbode.R;
import com.example.SalonAtYourAbode.ui.RetrofitClient;
import com.example.SalonAtYourAbode.ui.login.LoginFragment;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsFragment extends Fragment {

	EditText subject, message;
    Button contact_Btn;
    String sub, msg;
    Boolean contact;
    int userId = LoginFragment.User_Id;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_contact_us, container, false);

		subject = root.findViewById(R.id.mail_subject);
        message = root.findViewById(R.id.mail_text);
        contact_Btn = root.findViewById(R.id.contact_btn);
        contact_Btn.setOnClickListener(view -> send());

        return root;
    }
	
	public void send() {
        sub = subject.getText().toString().trim();
        msg = message.getText().toString().trim();

        System.out.println(sub + "  " + msg + "  " + userId);
        Call<ResponseBody> call = RetrofitClient.
                getInstance().
                getApi().
                contactUs(userId, sub, msg);
        System.out.println(12345);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                String s = null;
                System.out.println(response.body() );
                JSONObject jsonObject = null;
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
                        contact = jsonObject.getBoolean("contact");
                    }
                    if(contact){
                        String message1=jsonObject.getString("msg");
                        message.setText(" ");
                        subject.setText(" ");
                        Toast.makeText(getContext(), message1, Toast.LENGTH_SHORT).show();
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