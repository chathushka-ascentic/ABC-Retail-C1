package abc.ap.com.abcfashions.view;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.LinkedHashMap;

import abc.ap.com.abcfashions.R;
import abc.ap.com.abcfashions.model.Users;
import abc.ap.com.abcfashions.services.DataService;
import abc.ap.com.abcfashions.services.SyncService;
import abc.ap.com.abcfashions.services.VolleySingletonController;
import abc.ap.com.abcfashions.services.VolleyUtils;
import abc.ap.com.abcfashions.utils.LocalSharedPreferences;


/**
 * Created by: Aparna Prasad
 */
public class LoginFragment extends Fragment {

    private EditText _email;
    private EditText _password;
    private Button _login;
    private Button _createAcc;
    private ProgressBar _progressBar;
    private View view;
    private DataService dataService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        try
        {

            view = inflater.inflate(R.layout.login_view, container, false);

            _email = (EditText) view.findViewById(R.id.input_email);
            _password = (EditText) view.findViewById(R.id.input_password);
            _login = (Button) view.findViewById(R.id.btn_login);
            _createAcc = (Button) view.findViewById(R.id.btn_create_acc);
            _progressBar = (ProgressBar) view.findViewById(R.id.progressBarSpinner);
            TextView _version = (TextView) view.findViewById(R.id.version);

            PackageManager manager = getActivity().getPackageManager();
            PackageInfo info = manager.getPackageInfo(
                    getActivity().getPackageName(), 0);
            String version = info.versionName;
            _version.setText("Version:"+version);

             dataService = new DataService(getActivity().getApplicationContext());

            _login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputMethodManager inputManager = (InputMethodManager)
                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);

                    _createAcc.setEnabled(false);

                    login();


                }
            });

            _createAcc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        loadingSignUpView();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private boolean validate() {
        boolean valid = true;

        String email = _email.getText().toString();
        String password = _password.getText().toString();

        if (email.trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            App.setSnackbar(view, getString(R.string.emailValidation));
            valid = false;
        }
        else if (password.trim().isEmpty() || password.length() < 4 || password.length() > 10) {
            App.setSnackbar(view, getString(R.string.passwordValidation));

            valid = false;
        }

        return valid;

    }

    private void login() {
        _login.setVisibility(View.INVISIBLE);
        _progressBar.setVisibility(View.VISIBLE);

        if (!validate()) {
            _login.setEnabled(true);
            _createAcc.setEnabled(true);
            _login.setVisibility(View.VISIBLE);
            _progressBar.setVisibility(View.INVISIBLE);

            return;

        }


        final String email = _email.getText().toString();
        final String password = _password.getText().toString();


        try {

                    dataService.ClearDb();
                    LocalSharedPreferences.GetInstance(getContext()).ClearStoredUser();

                    LinkedHashMap<String, String> queryParamCollection = new LinkedHashMap<>();
                    queryParamCollection.put("username", email);
                    queryParamCollection.put("pass", password);

                    VolleyUtils.makeJsonObjectRequestGetSync(getActivity(), "login.php", queryParamCollection, "ParkCustomerLoginByMobileAndPassword", new VolleySingletonController.VolleyResponseListener() {

                        @Override
                        public void onError(String message) {
                            try {
                                _login.setVisibility(View.VISIBLE);
                                _progressBar.setVisibility(View.INVISIBLE);

                                _createAcc.setEnabled(true);

                                App.setSnackbar(getView(), getResources().getString(R.string.internetCheckMsg));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onResponse(String res) {
                            try {

                                if (!new JsonParser().parse(res).isJsonNull())
                                {
                                    JsonObject responseJson = ((JsonObject) new JsonParser().parse(res)).getAsJsonObject();

                                    Gson gson = new GsonBuilder().create();
                                    Users users = gson.fromJson(responseJson.toString(), Users.class);

                                    App.LoggedInUser = users;
                                    dataService.insertUser(users);

                                    //login success run async task
                                    getData();

                                }
                                else
                                {

                                    _login.setVisibility(View.VISIBLE);
                                    _progressBar.setVisibility(View.INVISIBLE);

                                    _createAcc.setEnabled(true);

                                    App.setSnackbar(getView(), getResources().getString(R.string.authenticationFailed));
                                    loadingSignUpView();

                                }

                            } catch (final Exception exception) {
                                _login.setVisibility(View.VISIBLE);
                                _progressBar.setVisibility(View.INVISIBLE);
                                _createAcc.setEnabled(true);

                                exception.printStackTrace();
                                App.setSnackbar(getView(), getResources().getString(R.string.internetCheckMsg));

                            }

                        }

                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

    }


    private void loadingSignUpView() {
        UserRegistrationFragment userMobileRegistrationFragment = new UserRegistrationFragment();
        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragmentContainer, userMobileRegistrationFragment).commit();
    }

    private void getData()
    {

        SyncService.registerListener(new SyncService.SyncCompleteListener() {
            @Override
            public void onSuccess() {

                LocalSharedPreferences localSharedPreferences = LocalSharedPreferences.GetInstance(getContext());
                localSharedPreferences.StoreUser();

                SyncService.unregisterListener();

                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("gotoMenu",0);
                startActivity(intent);
                getActivity().finish();

            }

            @Override
            public void onError() {

                try {

                    SyncService.unregisterListener();

                    dataService.ClearDb();
                    LocalSharedPreferences.GetInstance(getContext()).ClearStoredUser();


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            _login.setVisibility(View.VISIBLE);
                            _progressBar.setVisibility(View.INVISIBLE);
                            _createAcc.setEnabled(true);

                            App.setSnackbar(getView(), getResources().getString(R.string.internetCheckMsg));

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        SyncService syncService = new SyncService(getActivity().getApplicationContext());
        syncService.StartMainSync(getActivity(), App.LoggedInUser.getUserId());
    }


    @Override
    public void onPause() {

        super.onPause();
        _login.setVisibility(View.VISIBLE);
        _progressBar.setVisibility(View.INVISIBLE);
    }
}
