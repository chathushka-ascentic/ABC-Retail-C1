package abc.ap.com.abcfashions.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import abc.ap.com.abcfashions.R;
import abc.ap.com.abcfashions.model.Branch;
import abc.ap.com.abcfashions.model.Order;
import abc.ap.com.abcfashions.model.Users;
import abc.ap.com.abcfashions.services.DataService;
import abc.ap.com.abcfashions.utils.GPSLocationFused;
import abc.ap.com.abcfashions.utils.LocalSharedPreferences;
import abc.ap.com.abcfashions.utils.RunTimePermissionChecker;


/**
 * Created by: Aparna Prasad
 */

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private TextView fullName;
    Fragment mFragmentToSet = null;

    //
    TabLayout tabLayout;
    private static Order order =null;
    int branchIdFromNotification=0;
    NavigationView navigationView;
    int finalMenu=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View hView = navigationView.getHeaderView(0);

        fullName = (TextView) hView.findViewById(R.id.fullName);

        setHeader();


        // Initializing Toolbar and setting it as the actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(final MenuItem menuItem) {

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                menuItem.setEnabled(false);

                mFragmentToSet = null;

                finalMenu=0;

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {

                    case R.id.order:

                        finalMenu=1;
                        if (new RunTimePermissionChecker(MainActivity.this,true).managePermission(191)) {
                            mFragmentToSet = new FragmentOrder();
                            getSupportFragmentManager().beginTransaction().replace(R.id.frame, mFragmentToSet, mFragmentToSet.getClass().getSimpleName()).addToBackStack(mFragmentToSet.getClass().getSimpleName()).commit();
                        }
                        menuItem.setEnabled(true);
                        break;

                    case R.id.offers:

                        finalMenu=2;
                        if (new RunTimePermissionChecker(MainActivity.this,true).managePermission(191)) {
                            mFragmentToSet = new OfferListFragment();
                            Bundle args = new Bundle();
                            args.putInt("branchId", branchIdFromNotification);
                            mFragmentToSet.setArguments(args);

                            getSupportFragmentManager().beginTransaction().replace(R.id.frame, mFragmentToSet, mFragmentToSet.getClass().getSimpleName()).addToBackStack(mFragmentToSet.getClass().getSimpleName()).commit();

                            branchIdFromNotification = 0;
                        }
                        menuItem.setEnabled(true);
                        break;

                    case R.id.storelocator:

                        mFragmentToSet = new StoreListFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, mFragmentToSet, mFragmentToSet.getClass().getSimpleName()).addToBackStack(mFragmentToSet.getClass().getSimpleName()).commit();

                        menuItem.setEnabled(true);
                        break;

                    case R.id.profile:

                        mFragmentToSet = new UserProfileFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, mFragmentToSet, mFragmentToSet.getClass().getSimpleName()).addToBackStack(mFragmentToSet.getClass().getSimpleName()).commit();

                        menuItem.setEnabled(true);
                        break;

                    case R.id.logOut:

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                LocalSharedPreferences localSharedPreferences = new LocalSharedPreferences(MainActivity.this);
                                localSharedPreferences.ClearStoredUser();

                                DataService dataService = new DataService(getApplicationContext());
                                dataService.ClearDb();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();

                                        menuItem.setEnabled(true);
                                    }
                                });
                            }
                        }).start();

                        break;
                    default:
                        break;

                }


                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openD, R.string.closeD) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setHeader();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0); // this disables the animation
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary
        actionBarDrawerToggle.syncState();


        //set checked item from push notifications
        int menuFragment = getIntent().getIntExtra("gotoMenu", 0);

        if (menuFragment != 0) //coming from push notifications
        {
                branchIdFromNotification = getIntent().getIntExtra("branchId",0);
                navigationView.getMenu().performIdentifierAction(R.id.offers, 1);
                navigationView.setCheckedItem(R.id.offers);
        }
        else
        {
            navigationView.getMenu().performIdentifierAction(R.id.order, 0);
            navigationView.setCheckedItem(R.id.order);
        }

    }



    private void setHeader() {
        try {
            if (!App.LoggedInUser.equals(null)) {
                fullName.setText(App.LoggedInUser.getFullname());
            }

        } catch (NullPointerException e) {

            Users users = new Users();
            try {
                DataService dataService = new DataService(getApplicationContext());
                dataService.Login(users);
                App.LoggedInUser = users;

                fullName.setText(App.LoggedInUser.getFullname());


            } catch (Exception e1) {
                e1.printStackTrace();
            }

            e.printStackTrace();
        }
    }



    @Override
    public void onBackPressed() {
        try {

            if (drawerLayout.isDrawerOpen(Gravity.START)) {
                drawerLayout.closeDrawer(Gravity.START);
            } else {

                int count = getSupportFragmentManager().getBackStackEntryCount();
                if (count > 1) {
                    getSupportFragmentManager().popBackStackImmediate();
                } else {
                    finish();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }





    public TabLayout getTabLayout() {
        return tabLayout;
    }

    public void setTabLayout(TabLayout tabLayout) {
        this.tabLayout = tabLayout;
    }

    public static void setOrder(Order order) {
        MainActivity.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public int getBranchId()
    {
        try
        {
            DataService dataService = new DataService(getApplicationContext());
            ArrayList<Branch> branches = dataService.getList();

            for (int i = 0; i < branches.size(); i++) {
                Branch branch = branches.get(i);

                float[] results = new float[1];
                Location.distanceBetween(GPSLocationFused.latitude,  GPSLocationFused.longitude, branch.getLatitude(), branch.getLongitude(), results);
                float distanceInMeters = results[0];
                if(distanceInMeters < 500)
                {
                    return branch.getBranchId();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return 0;

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 191: {
                if ((Build.VERSION.SDK_INT >= 23)) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        //never ask again, allow
                        if (MainActivity.this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                            if(finalMenu == 1) {
                                build();
                                navigationView.getMenu().performIdentifierAction(R.id.order, 0);
                                navigationView.setCheckedItem(R.id.order);
                            }
                            else if(finalMenu == 2)
                            {
                                build();
                                navigationView.getMenu().performIdentifierAction(R.id.offers, 0);
                                navigationView.setCheckedItem(R.id.offers);

                            }
                        }
                        else {
                             new RunTimePermissionChecker(MainActivity.this).permissionDenied();

                        }
                    }
                    /*else {
                        //deny
                    }*/
                }
                break;
            }

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    public void build()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {

                GPSLocationFused.latitude = 0;
                GPSLocationFused.longitude = 0;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        GPSLocationFused.getInstance(getApplicationContext()).buildGoogleApiClient();
                    }
                }
                else
                {
                    GPSLocationFused.getInstance(getApplicationContext()).buildGoogleApiClient();

                }

            }
        }).start();
    }

}
