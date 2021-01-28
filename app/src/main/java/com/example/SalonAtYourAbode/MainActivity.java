/*
    Om Shri Ganeshay: Nam:
*/
package com.example.SalonAtYourAbode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.SalonAtYourAbode.ui.login.LoginFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.transition.Slide;

import info.androidhive.fontawesome.FontDrawable;

import static com.example.SalonAtYourAbode.ui.login.LoginFragment.Email;
import static com.example.SalonAtYourAbode.ui.login.LoginFragment.PREFERENCES;
import static com.example.SalonAtYourAbode.ui.login.LoginFragment.Password;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static NavigationView navigationView;
    public static NavController navController;
    DrawerLayout drawer;
	private long lastBackPressed;
	public static TextView userName, userMail;
	public static ImageView profileImage;
	TextView editProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FontDrawable drawable = new FontDrawable(this, R.string.fa_cut_solid, true, false);
        FloatingActionButton fab = findViewById(R.id.fab);

        drawable.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        fab.setImageDrawable(drawable);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        View nav_header = navigationView.getHeaderView(0);

        userName = nav_header.findViewById(R.id.user_name);
        userMail = nav_header.findViewById(R.id.user_mail);
        profileImage = nav_header.findViewById(R.id.profileImage);
        editProfile = nav_header.findViewById(R.id.edit_profile);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_login, R.id.nav_sign_in, R.id.nav_salon, R.id.nav_wallet, R.id.nav_order_status, R.id.nav_contact_us, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        /*int[] icons = {
                R.string.fa_home_solid, R.string.fa_user_solid, R.string.fa_user_plus_solid,
                R.string.fa_store_solid, R.string.fa_wallet_solid, R.string.fa_shipping_fast_solid, R.string.fa_mail_bulk_solid, R.string.fa_sign_out_alt_solid
        };*/
        renderMenuIcons(navigationView.getMenu());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.nav_salon);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     * Looping through menu icons are applying font drawable
     */
    private void renderMenuIcons(Menu menu) {
        menu.findItem(R.id.nav_logout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                com.example.SalonAtYourAbode.ui.LoadingDialog loadingDialog =
                        new com.example.SalonAtYourAbode.ui.LoadingDialog(MainActivity.this);
                loadingDialog.startLoadingDialog();
                drawer.closeDrawer(GravityCompat.START);

                SharedPreferences preferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();

                LoginFragment.User_Id = 0;
                LoginFragment.FullName = "Guest";
                LoginFragment.mobile_no = "0000000000";
                LoginFragment.txn_mail = "guest@gmail.com";

                menu.findItem(R.id.nav_login).setVisible(true);
                menu.findItem(R.id.nav_sign_in).setVisible(true);
                menu.findItem(R.id.nav_logout).setVisible(false);

                navController.navigate(R.id.nav_home);

                userName.setText("Guest");
                userMail.setText("guest@gmail.com");

                Toast.makeText(getApplicationContext(),"Logout",Toast.LENGTH_LONG).show();
                loadingDialog.dismissDialog();

                return true;
            }
        });

        SharedPreferences preferences;
        preferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        String img_str = preferences.getString("userphoto", "");
        assert img_str != null;

        if (!img_str.equals("")) {
            //decode string to image
            String base = img_str;
            byte[] imageAsBytes = Base64.decode(base.getBytes(), Base64.DEFAULT);
            MainActivity.profileImage.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        }

        if(preferences.contains(Email) && preferences.contains(Password)){
            LoginFragment.User_Id = preferences.getInt("UserId",0);
            LoginFragment.FullName = preferences.getString("FullName", "Guest");
            LoginFragment.mobile_no = preferences.getString("mobile", "0000000000");
            LoginFragment.txn_mail = preferences.getString("txn_mail", "<none>");

            userName.setText(LoginFragment.FullName);
            userMail.setText(LoginFragment.txn_mail);

            menu.findItem(R.id.nav_login).setVisible(false);
            menu.findItem(R.id.nav_sign_in).setVisible(false);
            menu.findItem(R.id.nav_logout).setVisible(true);
        }else{
            userName.setText("Guest");
            userMail.setText("guest@gmail.com");
            LoginFragment.FullName = "Guest";

            menu.findItem(R.id.nav_login).setVisible(true);
            menu.findItem(R.id.nav_sign_in).setVisible(true);
			menu.findItem(R.id.nav_logout).setVisible(false);
		}
    }

	@Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if(navigationView.getCheckedItem() == navigationView.getMenu().findItem(R.id.nav_home)) {
            if (lastBackPressed + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
            } else {
                Toast.makeText(this, "Touch again to EXIT", Toast.LENGTH_SHORT).show();
            }

            lastBackPressed = System.currentTimeMillis();
        }else {
            super.onBackPressed();
        }
	}
}
