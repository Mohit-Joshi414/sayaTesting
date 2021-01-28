package com.example.SalonAtYourAbode;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.SalonAtYourAbode.ui.login.LoginFragment;

import java.io.ByteArrayOutputStream;

import static com.example.SalonAtYourAbode.ui.login.LoginFragment.PREFERENCES;

public class ProfileActivity extends AppCompatActivity {

    ImageView[] imageViews;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        SharedPreferences preferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.service_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_vector_test);
        getSupportActionBar().setTitle("Profile");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView ivUserPhoto = findViewById(R.id.imageView);
        TextView userName = findViewById(R.id.username);
        userName.setText(LoginFragment.FullName);
        String img_str = preferences.getString("userphoto", "");
        assert img_str != null;
        if (!img_str.equals("")) {
            //decode string to image
            String base = img_str;
            byte[] imageAsBytes = Base64.decode(base.getBytes(), Base64.DEFAULT);
            ivUserPhoto.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
            MainActivity.profileImage.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        }
        imageViews = new ImageView[13];
        imageViews[0] = findViewById(R.id.imageView2);
        imageViews[1] = findViewById(R.id.imageView3);
        imageViews[2] = findViewById(R.id.imageView4);
        imageViews[3] = findViewById(R.id.imageView5);
        imageViews[4] = findViewById(R.id.imageView6);
        imageViews[5] = findViewById(R.id.imageView7);
        imageViews[6] = findViewById(R.id.imageView8);
        imageViews[7] = findViewById(R.id.imageView9);
        imageViews[8] = findViewById(R.id.imageView10);
        imageViews[9] = findViewById(R.id.imageView11);
        imageViews[10] = findViewById(R.id.imageView12);
        imageViews[11] = findViewById(R.id.imageView13);
        imageViews[12] = findViewById(R.id.imageView);

        int[] src = {R.drawable.avtar1, R.drawable.avtar2, R.drawable.avtar3, R.drawable.avtar4, R.drawable.avtar5, R.drawable.avtar6, R.drawable.avtar7, R.drawable.avtar8, R.drawable.avtar9, R.drawable.avtar10, R.drawable.avtar11, R.drawable.avtar12};

        imageViews[0].setOnClickListener(view -> {
            imageViews[12].setImageResource(src[0]);
            setProfilePhoto(view);
        });
        imageViews[1].setOnClickListener(view -> {
            imageViews[12].setImageResource(src[1]);
            setProfilePhoto(view);
        });
        imageViews[2].setOnClickListener(view -> {
            imageViews[12].setImageResource(src[2]);
            setProfilePhoto(view);
        });
        imageViews[3].setOnClickListener(view -> {
            imageViews[12].setImageResource(src[3]);
            setProfilePhoto(view);
        });
        imageViews[4].setOnClickListener(view -> {
            imageViews[12].setImageResource(src[4]);
            setProfilePhoto(view);
        });
        imageViews[5].setOnClickListener(view -> {
            imageViews[12].setImageResource(src[5]);
            setProfilePhoto(view);
        });
        imageViews[6].setOnClickListener(view -> {
            imageViews[12].setImageResource(src[6]);
            setProfilePhoto(view);
        });
        imageViews[7].setOnClickListener(view -> {
            imageViews[12].setImageResource(src[7]);
            setProfilePhoto(view);
        });
        imageViews[8].setOnClickListener(view -> {
            imageViews[12].setImageResource(src[8]);
            setProfilePhoto(view);
        });
        imageViews[9].setOnClickListener(view -> {
            imageViews[12].setImageResource(src[9]);
            setProfilePhoto(view);
        });
        imageViews[10].setOnClickListener(view -> {
            imageViews[12].setImageResource(src[10]);
            setProfilePhoto(view);
        });
        imageViews[11].setOnClickListener(view -> {
            imageViews[12].setImageResource(src[11]);
            setProfilePhoto(view);
        });



    }

    public void setProfilePhoto(View view) {
        ImageView ivphoto = (ImageView) findViewById(R.id.imageView);
        //code image to string
        ivphoto.buildDrawingCache();
        Bitmap bitmap = ivphoto.getDrawingCache();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        byte[] image = stream.toByteArray();
        String img_str = Base64.encodeToString(image, 0);
        String base = img_str;
        byte[] imageAsBytes = Base64.decode(base.getBytes(), Base64.DEFAULT);
//        ImageView ivsavedphoto = (ImageView) this.findViewById(R.id.imageView13);
//        ivsavedphoto.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)
//        );
        //save in sharedpreferences
        SharedPreferences preferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userphoto", img_str);
        editor.commit();
    }
}