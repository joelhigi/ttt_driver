package com.tartantransporttracker.driver;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewbinding.ViewBinding;

import com.google.android.material.navigation.NavigationView;
import com.tartantransporttracker.driver.managers.UserManager;
import com.tartantransporttracker.driver.ui.route.AdminViewRoute;


import java.util.Locale;

public abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public abstract  T getViewBinding();
    protected T binding;
    private UserManager userManager = UserManager.getInstance();
    private  DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    public void onCreate(@Nullable Bundle savedInstance){
        super.onCreate(savedInstance);
        initBinding();
//        drawerLayout = findViewById(R.id.drawLayout);
//        navigationView = findViewById(R.id.navView);
//        View hView = navigationView.getHeaderView(0);
    }

    private void initBinding(){
        binding = getViewBinding();
        View view = binding.getRoot();
        setContentView(view);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.view_routes:
                Intent intent = new Intent(this, AdminViewRoute.class);
                startActivity(intent);
                break;

            case R.id.logout_button:
                userManager.signOut(this).addOnSuccessListener(aVoid ->{
                    finish();
                });
                break;
            case R.id.language_btn:
                showChangeLanguageDialog();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showChangeLanguageDialog(){
        final String[] languages = {"French", "English"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setSingleChoiceItems(languages, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (i == 0){
                    setLocale("fr");
                    recreate();
                }else{
                    setLocale("en");
                    recreate();
                }
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void setLocale(String lang){
        Locale locale = new Locale(lang);
        locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        setLocale(language);
    }
}
