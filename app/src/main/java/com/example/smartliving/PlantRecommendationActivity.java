package com.example.smartliving;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartliving.model.SilectricCurrency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.LinkedHashSet;
import java.util.Locale;

public class PlantRecommendationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plants_recom);
        FullScreencall();
        ImageView plantimage = (ImageView) findViewById(R.id.Plantimageview);
        plantimage.setImageResource(R.drawable.prayerplant);

        TextView planttextview= (TextView) findViewById(R.id.Planttextdesc);
        planttextview.setText(R.string.prayerplant_desc_short);




        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        Button buynow=(Button) findViewById(R.id.buynow);
        buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.google.com/search?q=prayer+plant&sa=X&biw=1920&bih=922&tbm=shop&sxsrf=ALiCzsaIBL1nLhzSvgRcmZ0MOTszekO9gw%3A1664896667134&ei=m048Y5W2B-SH8QOqr4ngCw&oq=Prayer&gs_lcp=Cgtwcm9kdWN0cy1jYxADGAAyCggAELEDEIMBEEMyBQgAEIAEMgUIABCABDIFCAAQgAQyBQgAEIAEMgUIABCABDIFCAAQgAQyBQgAEIAEMgUIABCABDIFCAAQgAQ6CAgAEIAEELADOgoIABCABBCwAxAYOgkIABAeELADEAg6CQgAEB4QsAMQGDoLCK4BEMoDELADECc6BAgjECc6BAgAEEM6BAgAEAM6CwgAEIAEELEDEIMBSgQIQRgBUOQwWKU3YKI_aANwAHgAgAGXAYgB6AWSAQMwLjaYAQCgAQHIARDAAQE&sclient=products-cc"));
                startActivity(intent);
            }
        });

        LinearLayout linearlayout = (LinearLayout) findViewById(R.id.content_main);
        linearlayout.setOnTouchListener(new OnSwipeTouchListener(PlantRecommendationActivity.this) {
            public void onSwipeTop() {
                Toast.makeText(PlantRecommendationActivity.this, "5+ Plant required", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PlantRecommendationActivity.this, PlantActivity1.class);
                startActivity(intent);
            }
            public void onSwipeRight() {
                Toast.makeText(PlantRecommendationActivity.this, "Dashboard", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PlantRecommendationActivity.this, MainActivity.class);
                startActivity(intent);
            }
            public void onSwipeLeft() {
                Toast.makeText(PlantRecommendationActivity.this, "Dashboard", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PlantRecommendationActivity.this, MainActivity.class);
                startActivity(intent);
            }
            public void onSwipeBottom() {
                Toast.makeText(PlantRecommendationActivity.this, "5+ Plant required", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PlantRecommendationActivity.this, PlantActivity2.class);
                startActivity(intent);
            }

        });

    }

    public void goToSo (View view) {
        goToUrl ( "http://stackoverflow.com/");
    }

    public void goToSu (View view) {
        goToUrl ( "http://superuser.com/");
    }

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    public void FullScreencall() {
        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    }

