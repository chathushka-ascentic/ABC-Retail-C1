package abc.ap.com.abcfashions.view;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import abc.ap.com.abcfashions.R;


/**
 * Created by: Aparna Prasad
 */
public class LoginActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_container);

        LoginFragment fragmentLogin = new LoginFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentLogin).commit();

    }


}

