package com.example.SalonAtYourAbode.ui.salons;

public class Salon_list_getter_setter {
    private String user_name;
    private String email;
    private String image;
    private int id;

    public Salon_list_getter_setter(){}

    public String getname() {
        System.out.println(2);
        return user_name;
    }

    public void setname(String user_name) {
        this.user_name = user_name;
        System.out.println(1);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        String Url = "https://sayatest.pythonanywhere.com/media/";
        return Url.concat(image);
    }

    public void setImage(String image) {
        this.image = image;
        System.out.println(13);
    }
}
