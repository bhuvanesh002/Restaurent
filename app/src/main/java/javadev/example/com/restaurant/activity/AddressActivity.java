package javadev.example.com.restaurant.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javadev.example.com.restaurant.R;
import javadev.example.com.restaurant.fragment.AddressFragment;
import javadev.example.com.restaurant.fragment.NewAddressFragment;
import javadev.example.com.restaurant.ui.SmartAppConstants;

public class AddressActivity extends AppCompatActivity implements LocationListener {


    private LocationManager locationManager;
    private String provider;
    Toolbar mToolbar;
    String locality, maddress, zipcode, state;
    int flag = 0;
    RelativeLayout mGetLocationButton;
    TextView mStoresTextView, mGpsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        Typeface face = Typeface.createFromAsset(getAssets(), SmartAppConstants.BOLD_FONT);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_add);
        mGetLocationButton = (RelativeLayout) findViewById(R.id.detectloc);

        mGpsTextView = (TextView) findViewById(R.id.button_textview);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.black));
        mGpsTextView.setTypeface(face);

        SharedPreferences prefs = getSharedPreferences("address", MODE_PRIVATE);
        String name = prefs.getString("name", null);
        if (name == null) {
            flag = 0;
        } else {
            flag = 1;
        }
        if (flag == 0) {
            mGetLocationButton.setVisibility(View.GONE);
            NewAddressFragment fragment_addaddress = new NewAddressFragment();
            String backStateName = fragment_addaddress.getClass().getName();
            FragmentTransaction mAddMemberFragmentTransaction = getSupportFragmentManager().beginTransaction();
            mAddMemberFragmentTransaction.replace(R.id.frame, fragment_addaddress);
            mAddMemberFragmentTransaction.addToBackStack(backStateName);
            mAddMemberFragmentTransaction.commit();
            getSupportActionBar().setTitle(R.string.enter_address);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        } else {
            AddressFragment fragment_address = new AddressFragment();
            String backStateName = fragment_address.getClass().getName();
            FragmentTransaction mMemberFragmentTransaction = getSupportFragmentManager().beginTransaction();
            mMemberFragmentTransaction.replace(R.id.frame, fragment_address);
            mMemberFragmentTransaction.addToBackStack(backStateName);
            mMemberFragmentTransaction.commit();
            getSupportActionBar().setTitle(R.string.delivery);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        mGetLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(AddressActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AddressActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if (ActivityCompat.checkSelfPermission(AddressActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AddressActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Location location = locationManager.getLastKnownLocation(provider);


                if (location != null) {


                    Log.e("provider", "***");

                    System.out.println("Provider " + provider + " has been selected.");
                    onLocationChanged(location);

                    NewAddressFragment fragment_address = new NewAddressFragment();
                    Bundle args = new Bundle();
                    args.putString("locality", locality);
                    args.putString("state", state);
                    args.putString("zipcode", zipcode);
                    args.putString("address", maddress);
                    fragment_address.setArguments(args);
                    FragmentTransaction mSroresFragmentTransaction = getSupportFragmentManager().beginTransaction();
                    mSroresFragmentTransaction.replace(R.id.frame, fragment_address);
                    mSroresFragmentTransaction.commit();
                    mGetLocationButton.setVisibility(View.GONE);

                } else {

                }


            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("latftgtgt");
        if (ActivityCompat.checkSelfPermission(AddressActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AddressActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(AddressActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AddressActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(provider, 1000, 1, this);
    }


    @Override
    public void onPause() {
        super.onPause();
        System.out.println("latftgtgtdsdfpaaaaaaaa");
        if (ActivityCompat.checkSelfPermission(AddressActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AddressActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(AddressActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AddressActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        //You had this as int. It is advised to have Lat/Loing as double.
        double lat = location.getLatitude();
        double lng = location.getLongitude();

        Geocoder geoCoder = new Geocoder(AddressActivity.this, Locale.getDefault());
        StringBuilder builder = new StringBuilder();
        try {
            List<Address> address = geoCoder.getFromLocation(lat, lng, 1);





            locality = address.get(0).getLocality();
            maddress = address.get(0).getAddressLine(0);
            state = address.get(0).getAdminArea();
            zipcode = address.get(0).getPostalCode();
            System.out.println("addre" + maddress);
            System.out.println("locakity" + locality);
            System.out.println("state" + state);
            System.out.println("zipcode" + zipcode);
            System.out.println("lan" + String.valueOf(lng));


        } catch (IOException e) {
            // Handle IOException
        } catch (NullPointerException e) {
            // Handle NullPointerException
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {


    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(AddressActivity.this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onProviderDisabled(String provider) {
        showSettingsAlert();
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddressActivity.this);

        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                finish();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
