package com.example.SalonAtYourAbode.ui;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Multipart;
import retrofit2.http.Part;
import okhttp3.RequestBody;

public interface Api {

    @FormUrlEncoded
    @POST("loginAndroid")
    Call<ResponseBody> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("signupAndroid")
    Call<ResponseBody> signUp(
            @Field("user_name") String userName,
            @Field("email") String email,
            @Field("mobile") String  mobile,
            @Field("address") String address,
            @Field("gender") String gender,
            @Field("district") String district,
            @Field("password") String password,
            @Field("re_password") String rePassword
    );
	
	
	@Multipart
    @POST("hash_genrator")
    Call<ResponseBody> getHashCall(
            @Part("key") RequestBody key,
			@Part("txnid") RequestBody txnId,
            @Part("productinfo") RequestBody producinfo,
            @Part("firstname") RequestBody firstname,
            @Part("email") RequestBody email,
            @Part("user_id") RequestBody userId
    );

    @FormUrlEncoded
    @POST("hash_response")
    Call<ResponseBody> hashResponse(
            @Field("key") String key,
            @Field("txnid") String txnId,
            @Field("amount") String amount,
            @Field("productinfo") String producinfo,
            @Field("firstname") String firstname,
            @Field("email") String email,
            @Field("status") String status,
            @Field("hash") String hash,
            @Field("bank_ref_num") String bank_ref_num,
            @Field("phone") String phone,
            @Field("addedon") String addedon
    );
	
    @FormUrlEncoded
    @POST("contactAndroid")
    Call<ResponseBody> contactUs(
            @Field("user_id") int userId,
            @Field("subject") String sub,
            @Field("message") String msg
    );

    @FormUrlEncoded
    @POST("forgotPasswordAndroid")
    Call<ResponseBody> emailVerifyFP(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("otpAndroid")
    Call<ResponseBody> otpVerifyFP(
            @Field("otp") String Otp,
            @Field("password") String password,
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("getserviceAndroid")
    Call<ResponseBody> serviceList(
            @Field("service_provider_id") int serviceProviderId
    );

    @FormUrlEncoded
    @POST("getSalonAndroid")
    Call<ResponseBody> salonList(
            @Field("salon_type") String salonType,
            @Field("user_id") int userId
    );

    @FormUrlEncoded
    @POST("getcartAndroid")
    Call<ResponseBody> cartDetail(
            @Field("user_id") int userId
    );

    @FormUrlEncoded
    @POST("cartlistAdd")
    Call<ResponseBody> addToCart(
            @Field("user_id") int userId,
            @Field("service_provider_id") int serviceProvId,
            @Field("service_id") String  serviceId
    );
    @FormUrlEncoded
    @POST("cartlistRemove")
    Call<ResponseBody> removeFromCart(
            @Field("user_id") int userId,
            @Field("service_id") String serviceId
    );


    @FormUrlEncoded
    @POST("walletAndroid")
    Call<ResponseBody> wallet(
            @Field("user_id") int userId
    );

    @FormUrlEncoded
    @POST("orderListAndroid")
    Call<ResponseBody> orderList(
            @Field("user_id") int userId
    );

    @FormUrlEncoded
    @POST("orderdetailAndroid")
    Call<ResponseBody> orderDetail(
            @Field("txnid") String txnId
    );
}
