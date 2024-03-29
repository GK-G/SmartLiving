package com.example.smartliving;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartliving.model.DbConnection;
import com.example.smartliving.model.Usage;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Currency;

public class MainActivity extends AppCompatActivity {

    private final int USAGE_ACTIVITY_REQ = 1;
    private final int ELECTRIC_FEE_ACTIVITY_REQ = 3;
    private SharedPreferences silectricPreferences;
    private DbConnection dbConnection;
    double totalCO2;
    private static int SPLASH_SCREEN_TIME_OUT=2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     
        Toolbar toolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);

        silectricPreferences = getSharedPreferences("silectricPreferences", Context.MODE_PRIVATE);

        dbConnection = new DbConnection(this, getResources().openRawResource(R.raw.initial_data));

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //This method is used so that your splash activity
        //can cover the entire screen.

       // setContentView(R.layout.activity_main);
        //this will bind your MainActivity.class file with activity_main.


        checkPreferences();
        initInputDays();
        initAddUsageButton();
        initListUsage();
        calculateTotal();
        FullScreen_call();



    }


    public void FullScreen_call() {
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

    private void calculateTotal() {
        EditText daysEditText = (EditText) findViewById(R.id.daysEditText);
        int days = Integer.parseInt(daysEditText.getText().toString());

        double totalWattDaily = dbConnection.getTotalWattDaily();
        double totalKwh = totalWattDaily * days / 1000; //divide 1000 for convert Watt to KWatt
        totalCO2 = totalKwh * 0.385;





        DecimalFormat kwhFormat = (DecimalFormat) DecimalFormat.getNumberInstance();
//        kwhFormat.applyPattern("###,###.##");
//        kwhFormat.setDecimalSeparatorAlwaysShown(false);
        TextView totalElectricUsageTextView = (TextView) findViewById(R.id.totalElectricUsageTextView);
        String totalKwHString = kwhFormat.format(totalKwh) + " KwH ";
        totalElectricUsageTextView.setText(totalKwHString);

        TextView totalCO2emissionTextView = (TextView) findViewById(R.id.totalCO2emissionTextView);
        final String totalCO2String = kwhFormat.format(totalCO2) + " Kg ";
        totalCO2emissionTextView.setText(totalCO2String);




        double feeUsagePerKwh = (double) silectricPreferences.getFloat("usage_fee_per_kwh", 1);
        double feeBase = (double) silectricPreferences.getFloat("basic_fee", 0);
        double feeOthers = (double) silectricPreferences.getFloat("others_fee", 0);


        double totalMonthlyUsage = totalKwh * feeUsagePerKwh;
        totalMonthlyUsage = totalMonthlyUsage + feeBase + feeOthers;

        String currencyCode = silectricPreferences.getString("currency_code", "USD");
        Currency currency = Currency.getInstance(currencyCode);

        DecimalFormat monthlyFeeFormat = (DecimalFormat) DecimalFormat.getCurrencyInstance(getResources().getConfiguration().locale);
        DecimalFormatSymbols monthlyFeeFormatSymbols = new DecimalFormatSymbols();
        monthlyFeeFormatSymbols.setCurrencySymbol(currency.getSymbol());
//        monthlyFeeFormatSymbols.setMonetaryDecimalSeparator(',');
//        monthlyFeeFormatSymbols.setGroupingSeparator('.');
        monthlyFeeFormat.setDecimalFormatSymbols(monthlyFeeFormatSymbols);
//        monthlyFeeFormat.setDecimalSeparatorAlwaysShown(false);
        TextView totalElectricFeeTextView = (TextView) findViewById(R.id.totalElectricFeeTextView);
        String totalMonthlyFeeString = monthlyFeeFormat.format(totalMonthlyUsage);
        totalElectricFeeTextView.setText(totalMonthlyFeeString);

        //ImageView plantimage=(ImageView) findViewById(R.id.Plantimageview);
        //plantimage.setImageDrawable(null);
        //plantimage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.prayerplant));
        //plantimage.setImageResource(R.mipmap.ic_launcher);
        //Bitmap bImage = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher);
        //plantimage.setImageBitmap(bImage);

        //Set Plant Recommendation
        //android:src="@mipmap/ic_launcher"

        Button recommendationbutton= (Button) findViewById(R.id.recommendation_button);
        recommendationbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalCO2 > 0 && totalCO2 < 100) {
                    Toast.makeText(MainActivity.this, "CO2 is above "+(Math.floor(totalCO2)),
                            Toast.LENGTH_LONG).show();
                    Toast.makeText(MainActivity.this, "5+ Plant required ",
                            Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(MainActivity.this, PlantRecommendationActivity.class);
                    startActivity(intent);
                }
                else if(totalCO2 > 100 && totalCO2 < 200){
                    Intent intent = new Intent(MainActivity.this, PlantActivity1.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "CO2 is above 100",
                            Toast.LENGTH_LONG).show();
                    Toast.makeText(MainActivity.this, "10+ Plant required ",
                            Toast.LENGTH_LONG).show();
                }
                else if(totalCO2 > 200 && totalCO2 < 400){
                    Intent intent = new Intent(MainActivity.this, PlantActivity2.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "CO2 is above 200",
                            Toast.LENGTH_LONG).show();
                    Toast.makeText(MainActivity.this, "15+ Plant required ",
                            Toast.LENGTH_LONG).show();
                }
                else if(totalCO2 > 400){
                    Toast.makeText(MainActivity.this, "CO2 is above threshold Value.",
                            Toast.LENGTH_LONG).show();
                    Toast.makeText(MainActivity.this, "Don't Use Appliances for long.",
                            Toast.LENGTH_LONG).show();
                    Toast.makeText(MainActivity.this, "Its Hazardous to your health",
                            Toast.LENGTH_LONG).show();
                    Toast.makeText(MainActivity.this, "Plant Trees in between house.",
                            Toast.LENGTH_LONG).show();

                }
                else {
                    Toast.makeText(MainActivity.this, "Please add Appliances",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        //Button btn = (Button)findViewById(R.id.open_activity_button);
        //
        //btn.setOnClickListener(new View.OnClickListener() {
        //        @Override
        //        public void onClick(View v) {
        //            startActivity(new Intent(MainActivity.this, MyOtherActivity.class));
        //        }
        //});

    }



    private void initInputDays() {

        final EditText daysEditText = (EditText) findViewById(R.id.daysEditText);
        int daysInPreferences = silectricPreferences.getInt("number_of_days", 30);
        daysEditText.setText(String.valueOf(daysInPreferences));


        daysEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int days;
                days = Integer.parseInt(daysEditText.getText().toString());


                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setTitle("Number of Days: ");
                final NumberPicker np = new NumberPicker(v.getContext());

                np.setMinValue(1);
                np.setMaxValue(10000);
                np.setWrapSelectorWheel(false);
                np.setValue(days);

                np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        int days = newVal;
                        daysEditText.setText(String.valueOf(days));

                        SharedPreferences.Editor editPref = silectricPreferences.edit();
                        editPref.putInt("number_of_days", days);
                        editPref.apply();

                        calculateTotal();

                    }
                });
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        np.clearFocus();
                        int days = np.getValue();
                        daysEditText.setText(String.valueOf(days));

                        SharedPreferences.Editor editPref = silectricPreferences.edit();
                        editPref.putInt("number_of_days", days);
                        editPref.apply();

                        calculateTotal();
                    }
                });


                final FrameLayout parent = new FrameLayout(v.getContext());
                parent.addView(np, new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER));

                alert.setView(parent);
                alert.show().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                ;
            }
        });

    }

    private void initAddUsageButton() {
        ImageButton addUsageButton = (ImageButton) findViewById(R.id.addUsageButton);
        addUsageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent usageActivity = new Intent(view.getContext(), UsageActivity.class);
                Usage usage = null;
                usageActivity.putExtra("usage", usage);
                startActivityForResult(usageActivity, USAGE_ACTIVITY_REQ);
            }
        });
    }

    private void initListUsage() {
        ArrayList<Usage> usageList = dbConnection.getUsageList();

        ListView list = (ListView) findViewById(R.id.usageListView);

        ListAdapter adapter = new UsageListAdapter(this, usageList);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent usageActivity = new Intent(view.getContext(), UsageActivity.class);
                Usage usage = (Usage) parent.getItemAtPosition(position);
                usageActivity.putExtra("usage", usage);

                startActivityForResult(usageActivity, USAGE_ACTIVITY_REQ);
            }

        });

    }

    private void checkPreferences() {


        boolean hasInitiated = silectricPreferences.getBoolean("has_initiated", false);
       /* int a=1;
        if(a==1){
            Intent intent = new Intent(MainActivity.this, login.class);
            startActivity(intent);
        }*/
        if (!hasInitiated) {

            SharedPreferences.Editor editorSharedPref = silectricPreferences.edit();
            editorSharedPref.putBoolean("has_initiated", true);
            editorSharedPref.putFloat("usage_fee_per_kwh", 0.20f);
            editorSharedPref.putFloat("basic_fee", 0);
            editorSharedPref.putFloat("others_fee", 0);
            editorSharedPref.putInt("number_of_days", 30);
            editorSharedPref.putString("currency_code", Currency.getInstance(getResources().getConfiguration().locale).getCurrencyCode());
            editorSharedPref.apply();

            Intent intent = new Intent(this, OptionsActivity.class);
            startActivityForResult(intent, ELECTRIC_FEE_ACTIVITY_REQ);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        initListUsage();
        calculateTotal();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_electric_price) {

            Intent intent = new Intent(this, OptionsActivity.class);
            startActivityForResult(intent, ELECTRIC_FEE_ACTIVITY_REQ);

            return true;
        } else if (id == R.id.action_electronic_type) {

            Intent intent = new Intent(this, ElectronicListActivity.class);
            startActivity(intent);

            return true;
        }
        else if (id == R.id.action_AQI) {

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse("https://app.cpcbccr.com/AQI_India/"));
            startActivity(intent);

        return true;
    }

        return super.onOptionsItemSelected(item);
    }

}


class UsageListAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Usage> usageList;
    private static LayoutInflater inflater = null;

    public UsageListAdapter(Activity activity, ArrayList<Usage> usageList) {
        this.activity = activity;
        this.usageList = usageList;
        inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return usageList.size();
    }

    @Override
    public Object getItem(int position) {
        return usageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (convertView == null)
            view = inflater.inflate(R.layout.list_usage, null);


        TextView nameElectronicOnListRow = (TextView) view.findViewById(R.id.electronicNameOnListRow);
        TextView totalWattagePerDayOnListRow = (TextView) view.findViewById(R.id.totalWattagePerDayOnListRow);
        TextView totalUsageHoursPerDayOnListRow = (TextView) view.findViewById(R.id.totalUsageHoursPerDayOnListRow);
        TextView numberOfElectronicOnListRow = (TextView) view.findViewById(R.id.numberOfElectronicOnListRow);

        Usage usage = usageList.get(position);


        DecimalFormat df = new DecimalFormat("0.##");

        try {
            String formattedHours = df.format(usage.getTotalUsageHoursPerDay()) + " hours";
            String formattedWatt = df.format(usage.getTotalWattagePerDay()) + " watt";
            String formattedNumberOfElectronic = df.format(usage.getNumberOfElectronic()) + " electronics";

            nameElectronicOnListRow.setText(usage.getElectronic().getElectronicName());
            totalWattagePerDayOnListRow.setText(formattedWatt);
            totalUsageHoursPerDayOnListRow.setText(formattedHours) ;
            numberOfElectronicOnListRow.setText(formattedNumberOfElectronic);
        } catch (Exception e) {
            Log.e("MAinActivity", e.getMessage());
        }

        return view;
    }

}

