package com.example.SalonAtYourAbode.ui.salons.services.payu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.example.SalonAtYourAbode.MainActivity;
import com.example.SalonAtYourAbode.R;
import com.example.SalonAtYourAbode.ui.login.LoginFragment;
import com.example.SalonAtYourAbode.ui.salons.SalonFragment;
import com.example.SalonAtYourAbode.ui.salons.services.Service_Detail_Activity;
import com.example.SalonAtYourAbode.ui.salons.services.Service_Detail_Adapter;
import com.example.SalonAtYourAbode.ui.wallet.WalletFragment;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartPaymentActivity extends AppCompatActivity {

    PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();
    //declare paymentParam object
    PayUmoneySdkInitializer.PaymentParam paymentParam = null;

    String TAG ="mainActivity", status, bank_ref_num, addedon, hash;/*, amount = "36", firstname = "mohit", email = "jshibhanu415@gmail.com", userId = "1", txnid = "txn12345678", phone = "7898563677";*/

    int amountFetch = (int) (Service_Detail_Adapter.sum * 0.30);
    String amount = String.valueOf(amountFetch);

    String phone = LoginFragment.mobile_no;
    String  firstname = LoginFragment.FullName;
    String email = LoginFragment.txn_mail;


    String prodname ="saya", merchantId ="7290855", merchantkey="bJGBbqHp";  //   first test key only

    String userId = String.valueOf(LoginFragment.User_Id);
    String serviceProviderId = String.valueOf(Service_Detail_Activity.id_service_provider1);

    Random rand = new Random();
    String random1 = String.valueOf(999 + rand.nextInt(90001));
    String random2 = String.valueOf(999 + rand.nextInt(90001));

    String txnid = random1.concat("us").concat(userId).concat("ct").concat(String.valueOf(rand.nextInt(10))).concat("Sp").concat(serviceProviderId).concat(random2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_payment);
        System.out.println(amount + " " +  phone + " " + firstname + " " + email + " " + userId + " " + serviceProviderId + " " + txnid);

        if(WalletFragment.walletCash >= 200){
            amount = String.valueOf(1);
            WalletFragment.walletCash = 0;
        }

//        Intent intent = getIntent();
//        phone = intent.getExtras().getString("phone");
//        amount = intent.getExtras().getString("amount");

        startpay();

    }
    public void startpay(){

        builder.setAmount(amount)                          // Payment amount
                .setTxnId(txnid)                     // Transaction ID
                .setPhone(phone)                   // User Phone number
                .setProductName(prodname)                   // Product Name or description
                .setFirstName(firstname)                              // User First name
                .setEmail(email)              // User Email ID
                .setsUrl("https://www.payumoney.com/mobileapp/payumoney/success.php")     // Success URL (surl)
                .setfUrl("https://www.payumoney.com/mobileapp/payumoney/failure.php")     //Failure URL (furl)
                .setUdf1("")
                .setUdf2("")
                .setUdf3("")
                .setUdf4("")
                .setUdf5("")
                .setUdf6("")
                .setUdf7("")
                .setUdf8("")
                .setUdf9("")
                .setUdf10("")
                .setIsDebug(true)                              // Integration environment - true (Debug)/ false(Production)
                .setKey(merchantkey)                        // Merchant key
                .setMerchantId(merchantId);


        try {
            paymentParam = builder.build();
            // generateHashFromServer(paymentParam );
            getHashkey();

        } catch (Exception e) {
            Log.e(TAG, " error s "+e.toString());
        }

    }

    public void getHashkey(){
        ServiceWrapper service = new ServiceWrapper(null);
        Call<ResponseBody> call = service.newHashCall(merchantkey, txnid, prodname,
                firstname, email, userId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e(TAG, "hash res "+response.body());
                String merchantHash= null;
                try {
                    assert response.body() != null;
                    merchantHash = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (merchantHash.isEmpty() || merchantHash.equals("")) {
                    Toast.makeText(StartPaymentActivity.this, "Could not generate hash", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "hash empty");
                } else {
                    // mPaymentParams.setMerchantHash(merchantHash);
                    try {
                        JSONObject jsonObject = new JSONObject(merchantHash);
                        merchantHash = jsonObject.getString("hash");
                        hash = merchantHash;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    paymentParam.setMerchantHash(merchantHash);
                    System.out.println(paymentParam.getParams());
                    // Invoke the following function to open the checkout page.
                    // PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam, StartPaymentActivity.this,-1, true);
                    PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam, StartPaymentActivity.this, R.style.AppTheme_default, false);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("StartPaymentActivity", "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data != null) {
            TransactionResponse transactionResponse = data.getParcelableExtra( PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE );

            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {

                // Response from Payumoney
                String payuResponse = transactionResponse.getPayuResponse();

                // Response from SURl and FURL
                String merchantResponse = transactionResponse.getTransactionDetails();


                System.out.println(payuResponse);
                System.out.println(merchantResponse);
                JSONObject jsonObject;
                JSONObject result;

                try {
                    jsonObject = new JSONObject(payuResponse);

                    result = jsonObject.getJSONObject("result");

                    status = result.getString("status");
                    bank_ref_num = result.getString("bank_ref_num");
                    addedon = result.getString("addedon");

                    System.out.println(status + "________ " + bank_ref_num + "___________ " + addedon);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if(transactionResponse.getTransactionStatus().equals( TransactionResponse.TransactionStatus.SUCCESSFUL )){
                    //Success Transaction
//                    addNotification();

                    Call<ResponseBody> call1 = com.example.SalonAtYourAbode.ui.RetrofitClient.
                            getInstance().
                            getApi().
                            hashResponse(merchantkey, txnid, amount, prodname, firstname, email, status, hash, bank_ref_num, phone, addedon);
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
                                    Toast.makeText(getApplicationContext(), "No Response", Toast.LENGTH_LONG).show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    finish();
                } else{
                    //Failure Transaction
                    Call<ResponseBody> call1 = com.example.SalonAtYourAbode.ui.RetrofitClient.
                            getInstance().
                            getApi().
                            hashResponse(merchantkey, txnid, amount, prodname, firstname, email, status, hash, bank_ref_num, phone, addedon);
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
                                    Toast.makeText(getApplicationContext(), "No Response", Toast.LENGTH_LONG).show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    finish();
                }

                Log.e(TAG, "tran "+payuResponse+"---"+ merchantResponse);
            } /* else if (resultModel != null && resultModel.getError() != null) {
                Log.d(TAG, "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                Log.d(TAG, "Both objects are null!");
            }*/
        }
    }

//    private void addNotification() {
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            NotificationChannel channel = new NotificationChannel("MyNotification","MyNotification",NotificationManager.IMPORTANCE_DEFAULT);
//            NotificationManager manager = getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(channel);
//        }
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(StartPaymentActivity.this,"MyNotification")
//                .setContentTitle("Title")
//                .setSmallIcon(R.drawable.scissor_saya)
//                .setAutoCancel(false)
//                .setContentText("This is Notification");
//
//
//        Intent notificationIntent = new Intent(StartPaymentActivity.this, Notification.class);
//        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        //notification message will get at NotificationView
//        notificationIntent.putExtra("message", "This is a notification message");
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(StartPaymentActivity.this, 0, notificationIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(pendingIntent);
//
//        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(StartPaymentActivity.this);
//        managerCompat.notify(999,builder.build());
//    }
//
}
