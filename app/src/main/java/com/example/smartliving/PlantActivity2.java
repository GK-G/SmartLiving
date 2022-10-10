package com.example.smartliving;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PlantActivity2 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FullScreencall();
        setContentView(R.layout.activity_plants_recom2);

        TextView planttextview= (TextView) findViewById(R.id.RubberTextDesc);
        planttextview.setText(R.string.Rubberplant_desc_short);

        LinearLayout linearlayout = (LinearLayout) findViewById(R.id.Plant_content_Rubberplant);
        linearlayout.setOnTouchListener(new OnSwipeTouchListener(PlantActivity2.this) {
            public void onSwipeTop() {
                Toast.makeText(PlantActivity2.this, "15+ Plant required", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PlantActivity2.this, PlantRecommendationActivity.class);
                startActivity(intent);
            }
            public void onSwipeRight() {
                Toast.makeText(PlantActivity2.this, "Dashboard", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PlantActivity2.this, MainActivity.class);
                startActivity(intent);
            }
            public void onSwipeLeft() {
                Toast.makeText(PlantActivity2.this, "Dashboard", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PlantActivity2.this, MainActivity.class);
                startActivity(intent);
            }
            public void onSwipeBottom() {
                Toast.makeText(PlantActivity2.this, "15+ Plant required", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PlantActivity2.this, PlantActivity1.class);
                startActivity(intent);
            }

        });

        Button buynow=(Button) findViewById(R.id.buynow2);
        buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.google.com/search?q=Rubberplant&sxsrf=ALiCzsbfRc9UukET9elZPIHx1SZLujDZpQ:1664896614194&source=lnms&tbm=shop&sa=X&ved=2ahUKEwi2gZua78b6AhUE1HMBHWwICMkQ_AUoAnoECAMQBA&biw=1920&bih=922&dpr=1"));
                startActivity(intent);
            }
        });


    }

    public void FullScreencall() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

}
