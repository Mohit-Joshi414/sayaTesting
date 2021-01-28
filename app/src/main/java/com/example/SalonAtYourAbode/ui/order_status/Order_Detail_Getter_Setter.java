package com.example.SalonAtYourAbode.ui.order_status;

public class Order_Detail_Getter_Setter {
    private String paymentStatus;
    private String imageUrl;
    private String salonName;
    private String txnId;
    private String masterName,salonContact;
    private String orderData;
    private String orderStatus;
    private String payNow;

    public String getPayNow() {
        return payNow;
    }

    public void setPayNow(String payNow) {
        this.payNow = payNow;
    }

    public String getPayLater() {
        return payLater;
    }

    public void setPayLater(String payLater) {
        this.payLater = payLater;
    }

    private String payLater;


    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public String getOrderData() {
        return orderData;
    }

    public void setOrderData(String orderData) {
        this.orderData = orderData;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Order_Detail_Getter_Setter() {
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getImageUrl() {
        String url = "https://sayatest.pythonanywhere.com/media/";
        System.out.println(url+imageUrl);
        return url+imageUrl;
    }

    public String getTxnId() {
        return txnId;
    }


    public String getSalonName() {
        return salonName;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;

    }

    public String getSalonContact() {
        return salonContact;
    }

    public void setSalonContact(String salonContact) {
        this.salonContact = salonContact;
    }

    public void setSalonName(String salonName) {
        this.salonName = salonName;
    }
}
