package authenticislam;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import authenticislam.util.Oeuvre;
import authenticislam.view.AppRater;
import authenticislam.fragment.AqeedahFragment;
import authenticislam.fragment.HadithFragment;
import authenticislam.fragment.IslamicHistoryFragment;
import authenticislam.fragment.OtherFragment;
import authenticislam.fragment.QuranFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String PACKAGE_NAME;
    public static String APP_NAME;
    public static String PATH;
    public static final String STORAGE_PERMISSION = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final int PERMISSION_REQUEST_CODE = 1;
    public static Toolbar toolbar;
    public static DrawerLayout drawer;
    public static NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);




        if(requestPermission())
            init();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_aqeedah) {
            // Handle the camera action
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame, new AqeedahFragment());
            ft.commit();
        } else if (id == R.id.menu_quran) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame, new QuranFragment());
            ft.commit();

        } else if (id == R.id.menu_hadith) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame, new HadithFragment());
            ft.commit();

        } else if (id == R.id.menu_history) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame, new IslamicHistoryFragment());
            ft.commit();

        }else if (id == R.id.menu_other) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame, new OtherFragment());
            ft.commit();

        } else if (id == R.id.menu_share) {

            try
            { Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                String sAux = "\nLet me recommend you this application\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id="+PACKAGE_NAME;
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, getString(R.string.share)));
            }
            catch(Exception e)
            { //e.toString();
            }

        } else if (id == R.id.menu_rate) {

            AppRater.showRateDialog(this, null);

        } else if (id == R.id.menu_about) {

            // Display an Dialog box with the information
            View aboutDialog = LayoutInflater.from(this).inflate(R.layout.about_popup, null);

            AlertDialog.Builder aboutPopUp = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
            aboutPopUp.setTitle(getString(R.string.menu_about));
            aboutPopUp.setView(aboutDialog);
            aboutPopUp.setIcon(R.mipmap.ic_launcher);


            aboutPopUp.setPositiveButton(R.string.txt_ok, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(final DialogInterface dialog, int which)
                {
                    dialog.dismiss();
                }
            });
            // Display the Dialog box
            aboutPopUp.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void init(){

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        PACKAGE_NAME = getApplicationContext().getPackageName();
        PATH = Environment.getExternalStorageDirectory().getPath()+"/Authentic Islam/";
        APP_NAME = getString(R.string.app_name);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setBackgroundColor(getResources().getColor(R.color.colorBackGround));

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame, new AqeedahFragment(), getString(R.string.menu_aqeedah));
        ft.commitAllowingStateLoss();
        toolbar.setTitle(getString(R.string.menu_aqeedah));


        AppRater.app_launched(this);

        initFirebase();
    }

    public Boolean requestPermission(){

        List<String> requiredPermission = new ArrayList<>();

        if(ContextCompat.checkSelfPermission(this, STORAGE_PERMISSION) != PackageManager.PERMISSION_GRANTED)
            requiredPermission.add(STORAGE_PERMISSION);


        String[] requiredPermissionArray = new String[requiredPermission.size()];
        requiredPermission.toArray(requiredPermissionArray);

        if(requiredPermissionArray.length != 0)
            ActivityCompat.requestPermissions(this, requiredPermissionArray, PERMISSION_REQUEST_CODE);

        return  ContextCompat.checkSelfPermission(this, STORAGE_PERMISSION) == PackageManager.PERMISSION_GRANTED;

    }


    public boolean areAllPermissionGranted(int[] grantResults){
        for(int value : grantResults)
            if(value == PackageManager.PERMISSION_DENIED)
                return false;
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.d("MainActivity", "onRequestPermissionResult : "+Arrays.toString(grantResults));

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if(areAllPermissionGranted(grantResults))
                        init();
                else
                    finish();

                break;
        }
    }

    public void initFirebase(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();


        //database.setPersistenceEnabled(true);

        final DatabaseReference databaseReference = database.getReference("authenticislam_db");



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot userSnapShot : dataSnapshot.getChildren()) {

                    GenericTypeIndicator<List<Oeuvre>> o = new GenericTypeIndicator<List<Oeuvre>>() {};
                    List<Oeuvre> oeuvres = userSnapShot.getValue(o);

                    //Log.d("FireBase", " userSnapShot.getValue().toString() : " + userSnapShot.getValue().toString());
                    //Log.d("FireBase", "user1.toString() " + oeuvres.toString());

                    for (int i = 0; i < oeuvres.size(); i++)
                        Log.d("Oeuvre", "item "+i+ " : "+oeuvres.get(i).toString());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("MainActivity", "Error : "+databaseError.toString());

            }
        });
    }



}
