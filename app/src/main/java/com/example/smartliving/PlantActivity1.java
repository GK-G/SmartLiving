package com.example.smartliving;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PlantActivity1 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FullScreencall();
        setContentView(R.layout.activity_plants_recom1);

        TextView planttextview= (TextView) findViewById(R.id.birdnesttestdesc);
        planttextview.setText(R.string.BirdNestFernplant_desc_short);

        LinearLayout linearlayout = (LinearLayout) findViewById(R.id.Plant_content_main);
        linearlayout.setOnTouchListener(new OnSwipeTouchListener(PlantActivity1.this) {
            public void onSwipeTop() {
                Toast.makeText(PlantActivity1.this, "10+ Plant required", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PlantActivity1.this, PlantRecommendationActivity.class);
                startActivity(intent);
            }
            public void onSwipeRight() {
                Toast.makeText(PlantActivity1.this, "Dashboard", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PlantActivity1.this, MainActivity.class);
                startActivity(intent);
            }
            public void onSwipeLeft() {
                Toast.makeText(PlantActivity1.this, "Dashboard", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PlantActivity1.this, MainActivity.class);
                startActivity(intent);
            }
            public void onSwipeBottom() {
                Toast.makeText(PlantActivity1.this, "10+ Plant required", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PlantActivity1.this, PlantActivity2.class);
                startActivity(intent);
            }

        });

        Button buynow=(Button) findViewById(R.id.buynow1);
        buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.google.com/search?q=bird+nest+plant&sa=X&biw=1920&bih=922&tbm=shop&sxsrf=ALiCzsYk9UWVYcob0UpPrwAR5rRZ5JVnkA%3A1664896616535&ei=aE48Y-6KIIqY3LUPtfymgAo&ved=0ahUKEwju3qmb78b6AhUKDLcAHTW-CaAQ4dUDCAY&uact=5&oq=bird+nest+plant&gs_lcp=Cgtwcm9kdWN0cy1jYxADMgUIABCABDIFCAAQgAQyBwgAEIAEEBgyBwgAEIAEEBgyBwgAEIAEEBgyBggAEB4QFjIGCAAQHhAWMgYIABAeEBYyBggAEB4QFjIGCAAQHhAWOgoIABCABBCwAxAKOgQIIxAnOgsIABCABBCxAxCDAToECAAQAzoICAAQHhAWEBhKBAhBGAFQtwlYyjVg9jZoAXAAeACAAccBiAHcD5IBBDEuMTSYAQCgAQHIAQrAAQE&sclient=products-cc"));
                startActivity(intent);
            }
        });




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
