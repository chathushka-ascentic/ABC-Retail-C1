package abc.ap.com.abcfashions.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import abc.ap.com.abcfashions.R;
import abc.ap.com.abcfashions.model.Users;
import abc.ap.com.abcfashions.services.DataService;
import abc.ap.com.abcfashions.utils.LocalSharedPreferences;

/**
 * Created by: Aparna Prasad
 */

public class SplashActivity extends AppCompatActivity
{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        userLoginValdiation();

    }


     private void userLoginValdiation() {
        boolean status = false;
        try {

            final Users users = new Users();

            Boolean userLoggedIn = LocalSharedPreferences.GetInstance(SplashActivity.this).IsUserStored();

            if (userLoggedIn)
            {
                //check and get db values for the current user
                DataService dataService = new DataService(getApplicationContext());
                status = dataService.Login(users);
            }

            if (status)
            {

                App.LoggedInUser = users;

                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("gotoMenu",getIntent().getIntExtra("gotoMenu", 0));
                intent.putExtra("branchId",getIntent().getIntExtra("branchId", 0));
                startActivity(intent);
                finish();

            }
            else {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }


        }
        catch (final Exception e)
        {

            e.printStackTrace();

        }

    }


}