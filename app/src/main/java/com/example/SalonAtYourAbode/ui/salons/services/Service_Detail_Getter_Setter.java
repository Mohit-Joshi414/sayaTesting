package com.example.SalonAtYourAbode.ui.salons.services;

public class Service_Detail_Getter_Setter {
    private String service_name;
    private String service_status;
    private String image,information,id,price,serviceId,imageUrl;
//    private int price;

    public Service_Detail_Getter_Setter() {}

    public String getService_name() {
        System.out.println(service_name);
        return service_name;
    }

//    public String getImageUrl() {
//        String Url = "https://sayatest.pythonanywhere.com/media/";
//        return Url+imageUrl;
//    }
//
//    public void setImageUrl(String imageUrl) {
//        this.imageUrl = imageUrl;
//    }

    public String getService_status() {
        System.out.println(service_status);
        return service_status;
    }

    public String getImage() {
        return image;
    }

    public String getInformation() {
        System.out.println(information);
        return information;
    }

    public String getId() {
        System.out.println(id);
        return id;

    }
    public String getServiceId(){
        System.out.println(serviceId);
        return serviceId;
    }

    public String getPrice()
    {
        System.out.println(price);
        return price;
    }

    public void setService_name(String service_name)
    {
        this.service_name = service_name;
    }

    public void setService_status(String service_status) {
        this.service_status = service_status;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setServiceId(String id){
        this.serviceId = id;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
